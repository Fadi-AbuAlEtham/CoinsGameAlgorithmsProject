package com.coinsgamealgorithms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InsertingOptionsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane bdPane;

    @FXML
    private Button btFile;

    @FXML
    private Button btManual;

    @FXML
    private Button btRandom;

    @FXML
    private Label headerLabel;

    @FXML
    void initialize() {
        assert bdPane != null : "fx:id=\"bdPane\" was not injected: check your FXML file 'insertingOptions.fxml'.";
        assert btFile != null : "fx:id=\"btFile\" was not injected: check your FXML file 'insertingOptions.fxml'.";
        assert btManual != null : "fx:id=\"btManual\" was not injected: check your FXML file 'insertingOptions.fxml'.";
        assert btRandom != null : "fx:id=\"btRandom\" was not injected: check your FXML file 'insertingOptions.fxml'.";
        assert headerLabel != null : "fx:id=\"headerLabel\" was not injected: check your FXML file 'insertingOptions.fxml'.";

        btManual.setOnMouseEntered(event -> btManual.setScaleX(1.1));
        btManual.setOnMouseExited(event -> btManual.setScaleX(1.0));
        btManual.setOnMouseEntered(event -> btManual.setScaleY(1.1));
        btManual.setOnMouseExited(event -> btManual.setScaleY(1.0));

        btRandom.setOnMouseEntered(event -> btRandom.setScaleX(1.1));
        btRandom.setOnMouseExited(event -> btRandom.setScaleX(1.0));
        btRandom.setOnMouseEntered(event -> btRandom.setScaleY(1.1));
        btRandom.setOnMouseExited(event -> btRandom.setScaleY(1.0));

        btFile.setOnMouseEntered(event -> btFile.setScaleX(1.1));
        btFile.setOnMouseExited(event -> btFile.setScaleX(1.0));
        btFile.setOnMouseEntered(event -> btFile.setScaleY(1.1));
        btFile.setOnMouseExited(event -> btFile.setScaleY(1.0));
    }

    // Navigates to the main menu
    @FXML
    void onMainMenuClicked(ActionEvent event) throws IOException {
        // Load the main menu FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
        BorderPane borderPane = fxmlLoader.load();

        // Set the scene's root to the main menu layout
        bdPane.getScene().setRoot(borderPane);
    }

    // Handles the "Manual" insertion type selection and scene transition
    @FXML
    void onManualClicked(ActionEvent event) throws IOException {
        // Load the manual insertion scene FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("insertCoins.fxml"));
        VBox vbox = loader.load();

        // Set the insertion type to "Manual"
        InsertCoinsController insertCoinsController = loader.getController();
        GameConfig.insertationType = "Manual";

        // Initialize the controller and transition to the manual insertion scene
        insertCoinsController.initialize();
        bdPane.getScene().setRoot(vbox);
    }

    // Handles the "Random" insertion type selection and scene transition
    @FXML
    void onRandomClicked(ActionEvent event) throws IOException {
        // Load the random insertion scene FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("insertCoins.fxml"));
        VBox vbox = loader.load();

        // Set the insertion type to "Random"
        InsertCoinsController insertCoinsController = loader.getController();
        GameConfig.insertationType = "Random";

        // Initialize the controller and transition to the random insertion scene
        insertCoinsController.initialize();
        bdPane.getScene().setRoot(vbox);
    }

    // Handles the "File" insertion type, validates input, and processes the file
    @FXML
    void onFileClicked(ActionEvent event) {
        // Set the insertion type to "File" in GameConfig
        GameConfig.insertationType = "File";

        // Configure the FileChooser to allow the user to select a text file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Coins File"); // Set the dialog title
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt")); // Restrict to .txt files

        // Show the file dialog and get the user's selected file
        File file = fileChooser.showOpenDialog(bdPane.getScene().getWindow());

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                // Read the total number of coins from the first line of the file
                String totalCoinsLine = reader.readLine();
                if (totalCoinsLine == null || totalCoinsLine.isEmpty() || !isValidInteger(totalCoinsLine)) {
                    // Validate that the first line is a valid integer
                    showAlert("File Error", "The file format is incorrect. The first line must contain a valid integer.");
                    return;
                }

                if (Integer.parseInt(totalCoinsLine) % 2 != 0){
                    // Validate that the first line is an even number
                    showAlert("File Error: The total number is wrong", "The first line must contain a valid even number.");
                    return;
                }

                int totalCoins = Integer.parseInt(totalCoinsLine); // Parse the total number of coins
                GameConfig.totalCoins = totalCoins; // Update GameConfig with the total coins value

                // Read the coin values from the second line of the file
                String coinValuesLine = reader.readLine();
                if (coinValuesLine == null) {
                    // Ensure the second line exists
                    showAlert("File Error", "The file format is incorrect. The second line must contain the coin values separated by spaces.");
                    return;
                }

                // Split the coin values and validate their count
                String[] coinValues = coinValuesLine.trim().split(" ");
                if (coinValues.length != totalCoins) {
                    // Ensure the number of coin values matches the total number specified
                    showAlert("File Error", "The number of coin values does not match the total number specified.");
                    return;
                }

                // Parse and validate each coin value
                int[] coinsArray = new int[totalCoins];
                for (int i = 0; i < totalCoins; i++) {
                    if (!isValidInteger(coinValues[i])) {
                        // If a value is invalid, validation is handled in isValidInteger
                        return;
                    }
                    coinsArray[i] = Integer.parseInt(coinValues[i]); // Add the valid coin value to the array
                }

                // Update GameConfig with the validated coins array
                GameConfig.coins = coinsArray;

                // Load the "insert coins" scene and initialize it
                FXMLLoader loader = new FXMLLoader(getClass().getResource("insertCoins.fxml"));
                VBox vbox = loader.load();
                InsertCoinsController insertCoinsController = loader.getController();
                insertCoinsController.initialize(); // Initialize the scene's controller
                bdPane.getScene().setRoot(vbox); // Transition to the new scene

            } catch (IOException e) {
                // Handle file reading errors
                showAlert("File Error", "An error occurred while reading the file.");
                e.printStackTrace(); // Log the error for debugging purposes
            }
        }
    }

    // Validates whether the input string is a positive integer
    private boolean isValidInteger(String input) {
        try {
            int num = Integer.parseInt(input);
            if (num <= 0) {
                showAlert("Error: Invalid number", "The number must be a positive integer.\nThere is a wrong one which is: " + input + ".");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showAlert("Error: Invalid number", "The number must be a positive integer not a string.\nThere is a wrong one which is: " + input + ".");
            return false;
        }
    }

    // Displays an alert with a given title and message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.show();
    }

}

