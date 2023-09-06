package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {


    // GUI properties
    final int windowWidth = 1200;
    final int windowHeight = 800;
    final int boardWidth = 500;
    final int boardHeight = 500;

    // Elements
    BokuBoard board;
    // TODO: control panel, info board, console, ...

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Window properties
        primaryStage.setTitle("Boku");
        primaryStage.setResizable(false);

        // Layout
        BorderPane root = new BorderPane();

        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        board = new BokuBoard(boardWidth,  boardHeight);

        root.setCenter(board);
        root.setBottom(btn);

        primaryStage.setScene(new Scene(root, windowWidth, windowHeight));
        primaryStage.show();
    }
}