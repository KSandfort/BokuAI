package gui;

import engine.GameController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

/**
 * This class executes the game application.
 * @author      Konstantin Sandfort
 * @version     1.0
 * @since       Oct 2023
 */
@Getter
@Setter
public class App extends Application {

    // System.out debug flag
    static final boolean DEBUG_LOG = true;

    // Engine
    GameController gameController = new GameController(this);

    // GUI properties
    final int windowWidth = 1200;
    final int windowHeight = 800;
    final int boardWidth = 500;
    final int boardHeight = 500;
    final int buttonWidth = 300;
    final int buttonHeight = 50;

    String player1Selection = "Human Player";
    String player2Selection = "Human Player";

    // Elements
    BokuBoard board;
    VBox controlPanel = new VBox();
    Label player1Label = new Label("Player 1");
    Label player2Label = new Label("Player 2:");
    ComboBox<String> player1DropDown = new ComboBox<>();
    ComboBox<String> player2DropDown = new ComboBox<>();
    Button startButton = new Button("Start Game");
    Button resetButton = new Button("Reset Game");
    Button undoMoveButton = new Button("Undo Last Move");
    Button continueButton = new Button("Continue (Agent vs Agent)");

    HBox infoPanel = new HBox();
    Label wPlayerTimeElapsedLabel = new Label("00:00");
    Label bPlayerTimeElapsedLabel = new Label("00:00");
    Label whoToTurnLabel = new Label("Game not started yet");

    Label whitePlayerStatsLabel = new Label("White Player Stats");
    Label blackPlayerStatsLabel = new Label("Black Player Stats");
    VBox statsPanel = new VBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Window properties
        primaryStage.setTitle("Boku Game");
        primaryStage.setResizable(false);

        // Layout
        BorderPane root = new BorderPane();

        board = new BokuBoard(boardWidth, boardHeight, gameController);
        gameController.setBokuBoard(board);

        // --- Info Panel Setup ---
        infoPanel.setAlignment(Pos.CENTER);
        infoPanel.setPadding(new Insets(20, 20, 20, 20));
        infoPanel.setSpacing(20);

        whoToTurnLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24));
        whoToTurnLabel.setTextFill(Color.web("0xF58800"));

        wPlayerTimeElapsedLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24));
        wPlayerTimeElapsedLabel.setTextFill(Color.web("0xDDDDDD"));

        bPlayerTimeElapsedLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24));
        bPlayerTimeElapsedLabel.setTextFill(Color.web("0xDDDDDD"));

        infoPanel.getChildren().add(wPlayerTimeElapsedLabel);
        infoPanel.getChildren().add(whoToTurnLabel);
        infoPanel.getChildren().add(bPlayerTimeElapsedLabel);

        // --- Control Panel Setup ---
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.setPadding(new Insets(20, 20, 20, 20));
        controlPanel.setSpacing(20);

        // Dropdown Menus

        player1Label.setFont(Font.font("Roboto", FontWeight.BOLD, 24));
        player1Label.setTextFill(Color.web("0xDDDDDD"));

        player2Label.setFont(Font.font("Roboto", FontWeight.BOLD, 24));
        player2Label.setTextFill(Color.web("0xDDDDDD"));

        controlPanel.getChildren().add(player1Label);
        controlPanel.getChildren().add(player1DropDown);
        controlPanel.getChildren().add(player2Label);
        controlPanel.getChildren().add(player2DropDown);

        player1DropDown.getItems().add("Human Player");
        player1DropDown.getItems().add("Random");
        player1DropDown.getItems().add("Alpha Beta");
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
        player2DropDown.getItems().add("Alpha Beta");
        player2DropDown.getSelectionModel().selectFirst();
        player2DropDown.setOnAction((event) -> {
            String selection = player2DropDown.getSelectionModel().getSelectedItem();
            player2Selection = selection;
            if (DEBUG_LOG) {
                System.out.println("Player 2 Selection: " + selection);
            }
        });

        controlPanel.getChildren().add(startButton);
        controlPanel.getChildren().add(resetButton);
        controlPanel.getChildren().add(undoMoveButton);
        controlPanel.getChildren().add(continueButton);

        startButton.setPrefWidth(buttonWidth);
        startButton.setPrefHeight(buttonHeight);
        startButton.setFont(Font.font("Roboto", FontWeight.BOLD, 24));
        startButton.setOnAction(e -> gameController.initNewGame(player1Selection, player2Selection));

        undoMoveButton.setPrefWidth(buttonWidth);
        undoMoveButton.setPrefHeight(buttonHeight);
        undoMoveButton.setFont(Font.font("Roboto", FontWeight.BOLD, 24));
        undoMoveButton.setOnAction(e -> gameController.undoSingleMove());

        resetButton.setPrefWidth(buttonWidth);
        resetButton.setPrefHeight(buttonHeight);
        resetButton.setFont(Font.font("Roboto", FontWeight.BOLD, 24));
        resetButton.setOnAction(e -> gameController.resetGame());

        continueButton.setPrefWidth(buttonWidth);
        continueButton.setPrefHeight(buttonHeight);
        continueButton.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
        continueButton.setOnAction(e -> gameController.agentVsAgentOneStep());

        // --- Stats Panel Setup ---
        whitePlayerStatsLabel.setPrefWidth(300);
        whitePlayerStatsLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 12));
        whitePlayerStatsLabel.setTextFill(Color.web("0xDDDDDD"));

        blackPlayerStatsLabel.setPrefWidth(300);
        blackPlayerStatsLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 12));
        blackPlayerStatsLabel.setTextFill(Color.web("0xDDDDDD"));

        statsPanel.getChildren().add(whitePlayerStatsLabel);
        statsPanel.getChildren().add(blackPlayerStatsLabel);
        statsPanel.setAlignment(Pos.CENTER);
        statsPanel.setPadding(new Insets(20, 20, 20, 20));
        statsPanel.setSpacing(20);

        // --- Root Panel Setup ---
        root.setCenter(board);
        root.setTop(infoPanel);
        root.setLeft(controlPanel);
        root.setRight(statsPanel);

        root.setStyle("-fx-background-color:#051821;");
        primaryStage.setScene(new Scene(root, windowWidth, windowHeight));

        primaryStage.show();
    }

    /**
     * Changes the label of who's turn it is.
     * @param gameState Current game state of the game controller.
     */
    public void setWhoToTurnLabel(int gameState) {
        if (gameState == 1) {
            whoToTurnLabel.setText("White to turn!");
        }
        else if (gameState == 2) {
            whoToTurnLabel.setText("Black to turn!");
        }
    }

    /**
     * Displays a new information window with the information that a player has won the game.
     * @param whitePlayer flag if the white player has won
     */
    public void displayGameWonWindow(boolean whitePlayer) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        String infoString;
        if (whitePlayer) {
            infoString = " <<< White Player Won The Game! >>> ";
        }
        else {
            infoString = " <<< Black Player Won The Game! >>> ";
        }
        a.setTitle("Game Over!");
        a.setHeaderText(infoString);
        a.setContentText("You can close the application now!");
        a.showAndWait().ifPresent(rs -> {});
    }
}