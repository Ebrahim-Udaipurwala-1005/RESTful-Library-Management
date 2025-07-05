package de.tum.ise.view;

import de.tum.ise.H10E01ClientApplication;
import de.tum.ise.controller.BookController;
import de.tum.ise.model.Book;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

import java.util.List;

public class BookScene extends Scene {
    private final BookController bookController;
    private final H10E01ClientApplication application;
    private final ObservableList<Book> bookList;
    private final TextField authorSearchField;
    private final ChoiceBox<Book.Genre> genreSearchBox;
    private final TableView<Book> table;

    public BookScene(BookController bookController, H10E01ClientApplication application) {
        super(new VBox(), 1200, 700);
        this.bookController = bookController;
        this.application = application;
        this.bookList = FXCollections.observableArrayList();
        this.authorSearchField = new TextField();
        authorSearchField.setPromptText("Search by Author");
        this.genreSearchBox = new ChoiceBox<>();

        this.table = createTable();
        var vBox = new VBox(10, createFilterBox(), table, createButtonBox());
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.CENTER);
        setRoot(vBox);

        // Initially load all books
        refreshTable();
    }

    private TableView<Book> createTable() {
        var tableView = new TableView<>(bookList);
        tableView.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    showPopup(row.getItem());
                }
            });
            return row;
        });

        var idColumn = new TableColumn<Book, String>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(240);

        var titleColumn = new TableColumn<Book, String>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(220);

        var authorColumn = new TableColumn<Book, String>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setPrefWidth(150);

        var genreColumn = new TableColumn<Book, Book.Genre>("Genre");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.setPrefWidth(100);

        var yearColumn = new TableColumn<Book, Integer>("Year");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publishedYear"));
        yearColumn.setPrefWidth(80);

        var statusColumn = new TableColumn<Book, Book.Status>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setPrefWidth(100);

        tableView.getColumns().addAll(idColumn, titleColumn, authorColumn, genreColumn, yearColumn, statusColumn);
        return tableView;
    }

    private HBox createFilterBox() {
        genreSearchBox.getItems().add(null); // for "All Genres"
        genreSearchBox.getItems().addAll(Book.Genre.values());

        var searchButton = new Button("Search");
        searchButton.setOnAction(event -> refreshTable());

        var hBox = new HBox(10, new Label("Author:"), authorSearchField, new Label("Genre:"), genreSearchBox, searchButton);
        hBox.setAlignment(Pos.CENTER_LEFT);
        return hBox;
    }

    private HBox createButtonBox() {
        var backButton = new Button("Back");
        backButton.setOnAction(event -> application.showHomeScene());

        var addButton = new Button("Add Book");
        addButton.setOnAction(event -> showPopup(null));

        var refreshButton = new Button("Refresh All");
        refreshButton.setOnAction(event -> {
            authorSearchField.clear();
            genreSearchBox.setValue(null);
            refreshTable();
        });

        var checkoutButton = new Button("Check Out");
        var returnButton = new Button("Return");

        // Enable/disable buttons based on selection
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            checkoutButton.setDisable(newSelection == null || newSelection.getStatus() != Book.Status.AVAILABLE);
            returnButton.setDisable(newSelection == null || newSelection.getStatus() != Book.Status.CHECKED_OUT);
        });
        checkoutButton.setDisable(true);
        returnButton.setDisable(true);

        checkoutButton.setOnAction(e -> bookController.checkoutBook(table.getSelectionModel().getSelectedItem(), this::setBooks));
        returnButton.setOnAction(e -> bookController.returnBook(table.getSelectionModel().getSelectedItem(), this::setBooks));

        var spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        var buttonBox = new HBox(10, backButton, addButton, refreshButton, spacer, checkoutButton, returnButton);
        buttonBox.setAlignment(Pos.CENTER);
        return buttonBox;
    }

    private void refreshTable() {
        String author = authorSearchField.getText();
        Book.Genre genre = genreSearchBox.getValue();
        bookController.getAllBooks(author, genre, this::setBooks);
    }

    private void showPopup(Book book) {
        var popup = new Popup();
        var titleField = new TextField();
        titleField.setPromptText("Book Title");
        var authorField = new TextField();
        authorField.setPromptText("Author");
        var isbnField = new TextField();
        isbnField.setPromptText("ISBN");
        var yearField = new TextField();
        yearField.setPromptText("Published Year");
        var genreBox = new ChoiceBox<Book.Genre>();
        genreBox.getItems().addAll(Book.Genre.values());

        if (book != null) {
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            isbnField.setText(book.getIsbn());
            yearField.setText(String.valueOf(book.getPublishedYear()));
            genreBox.setValue(book.getGenre());
        } else {
            genreBox.setValue(Book.Genre.FICTION); // Default value
        }

        var saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            if(genreBox.getValue() == null) {
                System.err.println("Genre cannot be empty.");
                return;
            }
            try {
                var newBook = book != null ? book : new Book();
                newBook.setTitle(titleField.getText());
                newBook.setAuthor(authorField.getText());
                newBook.setIsbn(isbnField.getText());
                newBook.setPublishedYear(Integer.parseInt(yearField.getText()));
                newBook.setGenre(genreBox.getValue());

                if (book == null) {
                    bookController.addBook(newBook, this::setBooks);
                } else {
                    bookController.updateBook(newBook, this::setBooks);
                }
                popup.hide();
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format for year.");
            }
        });

        var cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> popup.hide());

        var deleteButton = new Button("Delete");
        deleteButton.setTextFill(Color.RED);
        deleteButton.setOnAction(event -> {
            if (book != null) {
                bookController.deleteBook(book, this::setBooks);
            }
            popup.hide();
        });

        var buttonBar = new HBox(10, saveButton, cancelButton);
        buttonBar.setAlignment(Pos.CENTER);
        if (book != null) {
            buttonBar.getChildren().add(deleteButton);
        }

        var vBox = new VBox(10, new Label(book == null ? "Add New Book" : "Edit Book"),
                titleField, authorField, isbnField, yearField, genreBox, buttonBar);
        vBox.setAlignment(Pos.CENTER);
        vBox.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, new CornerRadii(5), null)));
        vBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        vBox.setPrefWidth(300);
        vBox.setPadding(new Insets(15));
        popup.getContent().add(vBox);
        popup.show(application.getStage());
        popup.centerOnScreen();
    }

    private void setBooks(List<Book> books) {
        Platform.runLater(() -> {
            bookList.setAll(books);
            // After setting books, re-evaluate button disable state
            int selectedIndex = table.getSelectionModel().getSelectedIndex();
            table.getSelectionModel().clearSelection();
            table.getSelectionModel().select(selectedIndex);
        });
    }
}