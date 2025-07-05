package de.tum.ise;

import de.tum.ise.controller.BookController;
import de.tum.ise.view.HomeScene;
import de.tum.ise.view.BookScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class H10E01ClientApplication extends Application {
    private final BookController bookController = new BookController();
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        primaryStage.setTitle("Library Management");
        showHomeScene();
        primaryStage.show();
    }

    public void showHomeScene() {
        stage.setScene(new HomeScene(this));
    }

    public void showBookScene() {
        stage.setScene(new BookScene(bookController, this));
    }

    public Stage getStage() {
        return stage;
    }
}