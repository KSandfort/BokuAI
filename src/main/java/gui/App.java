package gui;

import engine.GameController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class App extends Application {

    static final boolean DEBUG_LOG = true;

    // Engine
    GameController gameController = new GameController(this);

    // GUI properties
    final int windowWidth = 1200;
    final int windowHeight = 800;
    final int boardWidth = 500;
    final int boardHeight = 500;
    final int FPS = 25;

    String player1Selection = "Human Player";
    String player2Selection = "Human Player";

    // Elements
    BokuBoard board;
    VBox controlPanel = new VBox();
    Label player1Label = new Label("Player 1");
    Label player2Label = new Label("Player 2:");
    ComboBox<String> player1DropDown = new ComboBox<>();
    ComboBox<String> player2DropDown = new ComboBox<>();
    Button startPauseButton = new Button("Start Game");
    Button resetButton = new Button("Reset Game");
    Button undoMoveButton = new Button("Undo Last Move");
    Button continueButton = new Button("Continue (Agent vs Agent");

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

        board = new BokuBoard(boardWidth, boardHeight, gameController);
        gameController.setBokuBoard(board);

        // --- Info Panel Setup ---
        infoPanel.setAlignment(Pos.CENTER);
        infoPanel.setPadding(new Insets(20, 20, 20, 20));
        infoPanel.setSpacing(20);

        infoPanel.getChildren().add(totalTimeElapsedLabel);
        infoPanel.getChildren().add(whoToTurnLabel);

        // --- Control Panel Setup ---
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.setPadding(new Insets(20, 20, 20, 20));
        controlPanel.setSpacing(20);

        // Dropdown Menus
        controlPanel.getChildren().add(player1Label);
        controlPanel.getChildren().add(player1DropDown);
        controlPanel.getChildren().add(player2Label);
        controlPanel.getChildren().add(player2DropDown);

        player1DropDown.getItems().add("Human Player");
        player1DropDown.getItems().add("Random");
        player1DropDown.getItems().add("Minimax");
        player1DropDown.getSelectionModel().selectFirst();
        player1DropDown.setOnAction((event) -> {
            String selection = player1DropDown.getSelectionModel().getSelectedItem();
            player1Selection = selection;
            if (DEBUG_LOG) {
                System.out.println("Player 1 Selection: " + selection);
            }
        });

        player2DropDown.getItems().add("Human Player");
        player2DropDown.getItems().add("Random");
        player2DropDown.getItems().add("Minimax");
        player2DropDown.getSelectionModel().selectFirst();
        player2DropDown.setOnAction((event) -> {
            String selection = player2DropDown.getSelectionModel().getSelectedItem();
            player2Selection = selection;
            if (DEBUG_LOG) {
                System.out.println("Player 2 Selection: " + selection);
            }
        });

        controlPanel.getChildren().add(startPauseButton);
        controlPanel.getChildren().add(resetButton);
        controlPanel.getChildren().add(undoMoveButton);
        controlPanel.getChildren().add(continueButton);

        startPauseButton.setOnAction(e -> gameController.initNewGame(player1Selection, player2Selection));

        undoMoveButton.setOnAction(e -> gameController.undoSingleMove());

        continueButton.setOnAction(e -> gameController.agentVsAgentOneStep());

        // --- Stats Panel Setup ---
        statsPanel.setAlignment(Pos.CENTER);
        statsPanel.setPadding(new Insets(20, 20, 20, 20));
        statsPanel.setSpacing(20);
        statsPanel.getChildren().add(moveHistoryField);
        moveHistoryField.setEditable(false);
        moveHistoryField.setPrefColumnCount(20);

        // --- Root Panel Setup ---
        root.setCenter(board);
        root.setTop(infoPanel);
        root.setLeft(controlPanel);
        root.setRight(statsPanel);

        primaryStage.setScene(new Scene(root, windowWidth, windowHeight));

        AnimationTimer animator = new AnimationTimer() {
            long startTime = System.nanoTime();
            @Override
            public void handle(long arg0) {
                long currentTime = System.nanoTime();
                if (FPS <= (currentTime - startTime)) {
                    gameController.getBokuBoard().updateGUI(gameController.getBoardState());
                    startTime = currentTime;
                }
            }
        };
        animator.start();

        primaryStage.show();
    }


    public void setWhoToTurnLabel(int gameState) {
        if (gameState == 1) {
            whoToTurnLabel.setText("White to turn!");
        }
        else if (gameState == 2) {
            whoToTurnLabel.setText("Black to turn!");
        }
    }
}