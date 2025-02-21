package com.coinsgamealgorithms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InformationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane bdPane;

    @FXML
    private Button btMainMenu;

    @FXML
    private HBox hbox;

    @FXML
    private Label lblInfo;

    @FXML
    private TextArea txtAreaInfo;

    @FXML
    void onMainMenuClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
        BorderPane borderPane = fxmlLoader.load();
        bdPane.getScene().setRoot(borderPane);
    }

    @FXML
    void initialize() {
        assert bdPane != null : "fx:id=\"bdPane\" was not injected: check your FXML file 'information.fxml'.";
        assert btMainMenu != null : "fx:id=\"btMainMenu\" was not injected: check your FXML file 'information.fxml'.";
        assert hbox != null : "fx:id=\"hbox\" was not injected: check your FXML file 'information.fxml'.";
        assert lblInfo != null : "fx:id=\"lblInfo\" was not injected: check your FXML file 'information.fxml'.";
        assert txtAreaInfo != null : "fx:id=\"txtAreaInfo\" was not injected: check your FXML file 'information.fxml'.";

        txtAreaInfo.clear();
        txtAreaInfo.setEditable(false);
        txtAreaInfo.setWrapText(true);
        txtAreaInfo.setText(
                "Welcome to the Coins Game!\n\n" +
                        "Objective:\n" +
                        "The goal of the game is to collect as many coins as possible by making optimal moves. Each player can only select a coin from either end of the row. The player with the highest total score wins.\n\n" +
                        "Game Modes:\n" +
                        "• Two-Player Mode: Compete against another player.\n" +
                        "• Single-Player Mode: Play against the computer using optimal algorithms.\n\n" +
                        "How to Play:\n" +
                        "1. Coins are displayed in a row.\n" +
                        "2. On your turn, select a coin from either end.\n" +
                        "3. The game continues until all coins are collected.\n" +
                        "4. The player with the highest score wins!\n\n" +
                        "Features:\n" +
                        "• Step-by-step mode: Watch moves play out one at a time.\n" +
                        "• Optimal play: The computer uses algorithms to calculate the best moves.\n" +
                        "• Visual animations for moves and score updates.\n\n" +
                        "Controls:\n" +
                        "• \"Main Menu\" button: Return to the main menu.\n" +
                        "• Two-Player Mode: Take turns selecting coins.\n" +
                        "• Single-Player Mode: Press \"Step by Step\" to watch moves or \"Start\" to play all moves at once.\n\n" +
                        "Tips:\n" +
                        "• Think ahead! Try to anticipate your opponent's moves.\n" +
                        "• Use the table view to study the game's optimal solutions.\n\n" +
                        "Enjoy the game and may the best strategist win!"
        );

    }

}
