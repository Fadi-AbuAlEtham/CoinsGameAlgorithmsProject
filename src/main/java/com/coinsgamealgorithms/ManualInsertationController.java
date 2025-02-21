package com.coinsgamealgorithms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class ManualInsertationController {

    @FXML
    private ListView<String> coinListView; // ListView to display coins
    @FXML
    private TextField coinInputField; // Input field for editing a selected coin
    @FXML
    private Button updateCoinButton; // Button to update selected coin
    @FXML
    private Button resetButton; // Button to reset all coins to an empty state
    @FXML
    private Button addCoinButton; // Button to add a new coin
    @FXML
    private Button saveAndReturnButton; // Button to save coins and return to the previous scene

    private ObservableList<String> coinList; // Observable list bound to the ListView
    private ArrayList<Integer> coinArrayList; // ArrayList to manage coin values dynamically
    private static int size;

    // Initializes the coin management GUI and sets up bindings and actions
    public void initialize() {
        // Initialize coinArrayList with placeholders or existing values
        coinArrayList = new ArrayList<>();
        for (int i = 0; i < GameConfig.totalCoins; i++) {
            // Add existing values from GameConfig.coins if available, otherwise add null
            if (i < GameConfig.coins.length && GameConfig.coins[i] != 0) {
                coinArrayList.add(GameConfig.coins[i]); // Use existing coin value
            } else {
                coinArrayList.add(null); // Placeholder for unfilled slots
            }
        }

        // Initialize coinList with current values or placeholders for display
        coinList = FXCollections.observableArrayList();
        for (Integer coin : coinArrayList) {
            coinList.add(coin != null ? String.valueOf(coin) : ""); // Display empty string for null values
        }

        // Bind coinList to the ListView for real-time updates
        coinListView.setItems(coinList);

        // Set up a listener to populate the TextField when a coin is selected
        coinListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            coinInputField.setText(newValue); // Display the selected value in the TextField
            coinInputField.setDisable(false); // Enable the TextField for editing
        });

        // Set up button actions
        updateCoinButton.setOnAction(e -> updateSelectedCoin()); // Update the selected coin
        resetButton.setOnAction(e -> resetCoins());              // Reset all coins to initial state
        addCoinButton.setOnAction(e -> addNewCoin());            // Add a new coin
        saveAndReturnButton.setOnAction(e -> saveAndReturn());   // Save coins and return to the previous screen

        // Enable adding coins when the Enter key is pressed
        coinInputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addNewCoin(); // Add the new coin on Enter key press
            }
        });

        // Perform an initial check to set limits on input and button actions
        checkCoinLimit();
    }

    // Checks if the coinArrayList has reached its limit based on non-null entries and adjusts input availability
    private void checkCoinLimit() {
        // Count the number of non-null entries in the coinArrayList
        long filledCount = coinArrayList.stream()
                .filter(value -> value != null) // Filter out null values
                .count();

        // If the number of filled slots equals or exceeds the total allowed coins
        if (filledCount >= GameConfig.totalCoins) {
            // Disable the input field and the "Add Coin" button
            coinInputField.setDisable(true);
            addCoinButton.setDisable(true);
        } else {
            // Enable the input field and the "Add Coin" button
            coinInputField.setDisable(false);
            addCoinButton.setDisable(false);
        }
    }

    // Adds a new coin to the coinArrayList and updates the ListView and GameConfig
    private void addNewCoin() {
        // Retrieve the input value from the coinInputField
        String newValue = coinInputField.getText();

        // Validate the input
        if (isValidInput(newValue)) {
            int intValue = Integer.parseInt(newValue); // Convert the valid input to an integer

            // Find the first null slot in coinArrayList and replace it with the new value
            for (int i = 0; i < coinArrayList.size(); i++) {
                if (coinArrayList.get(i) == null) {
                    coinArrayList.set(i, intValue); // Update the internal array list
                    coinList.set(i, newValue);      // Update the ListView display
                    break;                         // Stop after finding the first empty slot
                }
            }

            // Update size and mark GameConfig as non-empty
            size++;
            GameConfig.isEmpty = false;

            // Sync the updated coinArrayList with GameConfig.coins array
            updateGameConfigCoins();

            // Clear the input field for the next entry
            coinInputField.clear();

            // Check if the limit of coins is reached and disable further input if necessary
            checkCoinLimit();
        } else {
            // Show an error alert if the input is invalid
            showAlert("Add Error", "Please enter a positive coin value.");
        }
    }

    /*
    // Adds a new coin with the value from coinInputField, if within the allowed total count
    private void addNewCoin() {
        String newValue = coinInputField.getText();

        if (isValidInput(newValue)) {
            int intValue = Integer.parseInt(newValue);
            coinArrayList.add(intValue); // Add new value to ArrayList
            coinList.add(newValue); // Update ListView display
            updateGameConfigCoins(); // Update GameConfig.coins array
            coinInputField.clear(); // Clear input field after adding
            size++;
            checkCoinLimit(); // Check if the limit is reached to disable further input
        } else {
            showAlert("Add Error", "Please enter a valid coin value.");
        }
    }
*/

    // Updates the value of the selected coin in the coinArrayList and ListView
    private void updateSelectedCoin() {
        // Get the index of the selected item in the ListView
        int selectedIndex = coinListView.getSelectionModel().getSelectedIndex();

        // Retrieve the new value from the input field
        String newValue = coinInputField.getText();

        // Validate that an item is selected and the new value is valid
        if (selectedIndex >= 0 && isValidInput(newValue)) {
            int intValue = Integer.parseInt(newValue); // Convert the valid input to an integer

            // Update the selected item in both the internal array and the ListView
            coinArrayList.set(selectedIndex, intValue); // Update internal ArrayList
            coinList.set(selectedIndex, newValue);      // Update ListView display

            // Sync the updated coinArrayList with GameConfig.coins array
            updateGameConfigCoins();

            // Clear the input field to prepare for the next action
            coinInputField.clear();
        } else {
            // Show an error alert if no item is selected or the input is invalid
            showAlert("Update Error", "Please select a coin and enter a valid value.");
        }
    }

    // Resets coins to an empty state in both ArrayList and ListView
    private void resetCoins() {
        // Reinitialize the coins array in GameConfig to an empty state with the expected size
        GameConfig.coins = new int[GameConfig.totalCoins];
        size = 0; // Reset the counter for entered coins
        GameConfig.isEmpty = true;

        // Reset coinArrayList with null placeholders up to the total number of coins
        coinArrayList.clear();
        for (int i = 0; i < GameConfig.totalCoins; i++) {
            coinArrayList.add(null);
        }

        // Reset coinList with empty strings to show a cleared state in the ListView
        coinList.clear();
        for (int i = 0; i < GameConfig.totalCoins; i++) {
            coinList.add(""); // Display empty strings in the ListView
        }

        // Re-enable the input field and button for adding new coins
        coinInputField.setDisable(false);
        addCoinButton.setDisable(false);

        // Refresh ListView to reflect the cleared state with placeholders
        coinListView.setItems(coinList);
    }



//    // Checks if the coinArrayList has reached the limit and disables input if so
//    private void checkCoinLimit() {
//        if (coinArrayList.size() >= GameConfig.totalCoins) {
//            coinInputField.setDisable(true);
//            addCoinButton.setDisable(true);
//        } else {
//            coinInputField.setDisable(false);
//            addCoinButton.setDisable(false);
//        }
//    }

    // Saves the coin values to GameConfig and returns to the previous scene
    private void saveAndReturn() {
        updateGameConfigCoins(); // Save current coins to GameConfig

        // Check if user has filled all required coins
        if (coinArrayList.size() < GameConfig.totalCoins || coinArrayList.contains(null)) {
            showIncompleteCoinsAlert();
        } else {
            returnToPreviousScene();
        }
    }

    // Prompts the user to handle incomplete coin entries and offers options to continue or fill randomly
    private void showIncompleteCoinsAlert() {
        // Create a confirmation alert dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Incomplete Coins"); // Set the title of the alert
        alert.setHeaderText("You have entered only " + size + " out of " + GameConfig.totalCoins + " coins."); // Inform the user about the missing coins
        alert.setContentText("Do you want to continue inserting values, or fill the rest randomly?"); // Provide options to proceed

        // Define button types for the user options
        ButtonType continueButton = new ButtonType("Continue Inserting");
        ButtonType fillRandomButton = new ButtonType("Fill Randomly");

        // Set the button options for the alert dialog
        alert.getButtonTypes().setAll(continueButton, fillRandomButton);

        // Show the alert and wait for the user's choice
        Optional<ButtonType> result = alert.showAndWait();

        // Handle the user's choice
        if (result.isPresent() && result.get() == fillRandomButton) {
            requestRandomRangeAndFill(); // Fill the remaining coins randomly if the user selects this option
        }
    }

    // Requests a random range from the user and fills remaining coins with values in that range
    private void requestRandomRangeAndFill() {
        // Create a dialog to prompt the user for the minimum value
        TextInputDialog minDialog = new TextInputDialog();
        minDialog.setTitle("Random Range - Minimum"); // Set dialog title
        minDialog.setHeaderText("Enter the minimum value for random coins:"); // Provide instructions

        // Show the dialog and capture the user's input
        Optional<String> minResult = minDialog.showAndWait();
        if (!minResult.isPresent() || !isValidInput(minResult.get())) {
            // Validate the input and display an alert if it's invalid
            showAlert("Invalid Input", "Please enter a valid minimum value.");
            return; // Exit if input is invalid
        }
        int minValue = Integer.parseInt(minResult.get()); // Parse the valid minimum value

        // Create a dialog to prompt the user for the maximum value
        TextInputDialog maxDialog = new TextInputDialog();
        maxDialog.setTitle("Random Range - Maximum"); // Set dialog title
        maxDialog.setHeaderText("Enter the maximum value for random coins:"); // Provide instructions

        // Show the dialog and capture the user's input
        Optional<String> maxResult = maxDialog.showAndWait();
        if (!maxResult.isPresent() || !isValidInput(maxResult.get())) {
            // Validate the input and display an alert if it's invalid
            showAlert("Invalid Input", "Please enter a valid maximum value.");
            return; // Exit if input is invalid
        }
        int maxValue = Integer.parseInt(maxResult.get()); // Parse the valid maximum value

        // Ensure the minimum value is less than the maximum value
        if (minValue >= maxValue) {
            showAlert("Range Error", "The maximum value must be greater than the minimum value.");
            return; // Exit if the range is invalid
        }

        // Fill the remaining coins with random values within the specified range
        fillRemainingCoinsRandomly(minValue, maxValue);

        // Return to the previous scene after filling the coins
        returnToPreviousScene();
    }

    // Fills remaining coin slots in the coinArrayList with random values within the specified range
    private void fillRemainingCoinsRandomly(int min, int max) {
        // Create a Random object for generating random numbers
        Random random = new Random();

        // Iterate through the coinArrayList to find empty (null) slots
        for (int i = 0; i < coinArrayList.size(); i++) {
            if (coinArrayList.get(i) == null) {
                // Generate a random value within the range [min, max]
                int randomValue = random.nextInt(max - min + 1) + min;

                // Set the random value in the internal coinArrayList
                coinArrayList.set(i, randomValue);

                // Update the corresponding value in the ListView display
                coinList.set(i, String.valueOf(randomValue));

                // Increment the size counter to track the number of filled slots
                size++;
            }
        }

        // Mark GameConfig as non-empty since all slots are now filled
        GameConfig.isEmpty = false;

        // Update GameConfig.coins to reflect the new values
        updateGameConfigCoins();
    }

    // Updates GameConfig.coins to reflect changes in coinArrayList
    private void updateGameConfigCoins() {
        GameConfig.coins = coinArrayList.stream().map(coin -> coin == null ? 0 : coin).mapToInt(Integer::intValue).toArray();
    }

    // Returns to the "insertCoins" scene, enabling relevant UI elements
    private void returnToPreviousScene() {
        try {
            // Load the "insertCoins.fxml" file to return to the previous scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("insertCoins.fxml"));
            VBox root = loader.load();

            // Get the controller for the "insertCoins" scene
            InsertCoinsController controller = loader.getController();

            // Enable the "Play" button to allow starting the game
            controller.getBtPlay().setDisable(false);

            // Make the player name input section visible
            controller.getHbInsertCoinsPlayerName().setVisible(true);

            // Make the player turn selection section visible
            controller.getHbRbPlayersTurn().setVisible(true);

            // Replace the current scene's root with the loaded "insertCoins" layout
            Scene scene = saveAndReturnButton.getScene();
            scene.setRoot(root);

        } catch (IOException e) {
            // Handle exceptions that occur while loading the FXML file
            showAlert("Navigation Error", "Could not return to the previous scene.");
            e.printStackTrace(); // Print stack trace for debugging
        }
    }

    // Validates the input in coinInputField to ensure it's a positive integer
    private boolean isValidInput(String input) {
        try {
            int value = Integer.parseInt(input);
            return value > 0; // Ensure non-negative and non-zero values
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Helper method to show an alert with a given title and message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
