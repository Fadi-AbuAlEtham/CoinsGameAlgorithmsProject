package com.coinsgamealgorithms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainMenuController {
    @FXML
    private BorderPane bdMainScene;

    @FXML
    private Button btComputer;

    @FXML
    private Button btTwoPlayers;

    @FXML
    private Label headerLabel;

    @FXML
    private Button btInfo;

    @FXML
    private Label lblInsertCoin;


    // Initialize method to set up initial states or event handlers
    @FXML
    public void initialize() {
        assert bdMainScene != null : "fx:id=\"bdMainScene\" was not injected: check your FXML file 'mainMenu.fxml'.";
        assert btComputer != null : "fx:id=\"btComputer\" was not injected: check your FXML file 'mainMenu.fxml'.";
        assert btTwoPlayers != null : "fx:id=\"btTwoPlayers\" was not injected: check your FXML file 'mainMenu.fxml'.";
        assert headerLabel != null : "fx:id=\"headerLabel\" was not injected: check your FXML file 'mainMenu.fxml'.";
        assert lblInsertCoin != null : "fx:id=\"lblInsertCoin\" was not injected: check your FXML file 'mainMenu.fxml'.";

        // Example: Add hover actions or click events here if needed
        btComputer.setOnMouseEntered(event -> btComputer.setScaleX(1.1));
        btComputer.setOnMouseExited(event -> btComputer.setScaleX(1.0));
        btComputer.setOnMouseEntered(event -> btComputer.setScaleY(1.1));
        btComputer.setOnMouseExited(event -> btComputer.setScaleY(1.0));

        btTwoPlayers.setOnMouseEntered(event -> btTwoPlayers.setScaleX(1.1));
        btTwoPlayers.setOnMouseExited(event -> btTwoPlayers.setScaleX(1.0));
        btTwoPlayers.setOnMouseEntered(event -> btTwoPlayers.setScaleY(1.1));
        btTwoPlayers.setOnMouseExited(event -> btTwoPlayers.setScaleY(1.0));

        btInfo.setOnMouseEntered(event -> btInfo.setScaleX(1.1));
        btInfo.setOnMouseExited(event -> btInfo.setScaleX(1.0));
        btInfo.setOnMouseEntered(event -> btInfo.setScaleY(1.1));
        btInfo.setOnMouseExited(event -> btInfo.setScaleY(1.0));

    }

    // Navigates to the "inserting options" scene for a game between two computers
    @FXML
    void onComputerClicked(ActionEvent event) throws IOException {
        // Load the "inserting options" FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("insertingOptions.fxml"));
        BorderPane bdPane = loader.load();

        // Update GameConfig to indicate the game is between two computers
        GameConfig.whoCalled = "Two Computers";

        // Set the current scene's root to the "inserting options" layout
        bdMainScene.getScene().setRoot(bdPane);
    }

    // Navigates to the "inserting options" scene for a game between two players
    @FXML
    void onTwoPlayersClicked(ActionEvent event) throws IOException {
        // Load the "inserting options" FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("insertingOptions.fxml"));
        BorderPane bdPane = loader.load();

        // Update GameConfig to indicate the game is between two players
        GameConfig.whoCalled = "Two Players";

        // Set the current scene's root to the "inserting options" layout
        bdMainScene.getScene().setRoot(bdPane);
    }

    // Navigates to the "information" scene
    @FXML
    void onInfoClicked(ActionEvent event) throws IOException {
        // Load the "information" FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("information.fxml"));
        BorderPane bdPane = loader.load();

        // Set the current scene's root to the "information" layout
        bdMainScene.getScene().setRoot(bdPane);
    }


}
