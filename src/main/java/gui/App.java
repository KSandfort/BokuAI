package gui;

import agent.HumanPlayer;
import engine.GameController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    // Engine
    GameController gameController = new GameController();

    // GUI properties
    final int windowWidth = 1200;
    final int windowHeight = 800;
    final int boardWidth = 500;
    final int boardHeight = 500;

    // Elements
    BokuBoard board;
    VBox controlPanel = new VBox();
    ComboBox<String> player1DropDown = new ComboBox<String>();
    ComboBox<String> player2DropDown = new ComboBox<String>();
    Button startPauseButton = new Button("Start Game");
    Button resetButton = new Button("Reset Game");
    Button undoMoveButton = new Button("Undo Last Move");

    HBox infoPanel = new HBox();
    Label totalTimeElapsedLabel = new Label("00:00");
    Label whoToTurnLabel = new Label("Game not started yet");
    VBox statsPanel = new VBox();
    TextArea moveHistoryField = new TextArea("History of all moves");

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

        board = new BokuBoard(boardWidth,  boardHeight, gameController);
        gameController.setBokuBoard(board);

        // Info Panel Setup
        infoPanel.setAlignment(Pos.CENTER);
        infoPanel.setPadding(new Insets(20, 20, 20, 20));
        infoPanel.setSpacing(20);

        infoPanel.getChildren().add(totalTimeElapsedLabel);
        infoPanel.getChildren().add(whoToTurnLabel);

        // Control Panel Setup
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.setPadding(new Insets(20, 20, 20, 20));
        controlPanel.setSpacing(20);

        controlPanel.getChildren().add(player1DropDown);
        controlPanel.getChildren().add(player2DropDown);
        controlPanel.getChildren().add(startPauseButton);
        controlPanel.getChildren().add(resetButton);
        controlPanel.getChildren().add(undoMoveButton);

        startPauseButton.setOnAction(e -> gameController.initNewGame(new HumanPlayer(), new HumanPlayer()));

        undoMoveButton.setOnAction(e -> gameController.undoSingleMove());

        // Stats Panel Setup
        statsPanel.setAlignment(Pos.CENTER);
        statsPanel.setPadding(new Insets(20, 20, 20, 20));
        statsPanel.setSpacing(20);
        statsPanel.getChildren().add(moveHistoryField);
        moveHistoryField.setEditable(false);
        moveHistoryField.setPrefColumnCount(20);

        // Root Panel Setup
        root.setCenter(board);
        root.setTop(infoPanel);
        root.setLeft(controlPanel);
        root.setRight(statsPanel);
        root.setBottom(btn);

        primaryStage.setScene(new Scene(root, windowWidth, windowHeight));
        primaryStage.show();
    }
}