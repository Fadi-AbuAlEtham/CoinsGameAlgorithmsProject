package com.coinsgamealgorithms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class InsertCoinsController {

    @FXML
    private Button btInsertNumCoins;

    @FXML
    private Button btPlay;

    @FXML
    private Button btSuggFour;

    @FXML
    private Button btSuggOne;

    @FXML
    private Button btSuggThree;

    @FXML
    private Button btSuggTwo;

    @FXML
    private HBox hbCoinsRange;

    @FXML
    private HBox hbInsertCoinsPlayerName;

    @FXML
    private HBox hbRbPlayersTurn;

    @FXML
    private HBox hbSuggestions;

    @FXML
    private RadioButton rbPlayerOne;

    @FXML
    private RadioButton rbPlayerTwo;

    @FXML
    private TextField txtInsertNumCoins;

    @FXML
    private TextField txtInstCoinsPlayerOneName;

    @FXML
    private TextField txtInstCoinsPlayerTwoName1;

    @FXML
    private TextField txtRangeFrom;

    @FXML
    private TextField txtRangeTo;

    @FXML
    private VBox vbInsertCoinsScene;

    Alert alert;

    public Button getBtPlay() {
        return btPlay;
    }

    public HBox getHbInsertCoinsPlayerName() {
        return hbInsertCoinsPlayerName;
    }

    public HBox getHbRbPlayersTurn() {
        return hbRbPlayersTurn;
    }

    @FXML
    void initialize() {
        // Set the default text for the "Insert Number of Coins" button
        btInsertNumCoins.setText("Insert");

        // Hide the suggestions box and enable the number of coins text field by default
        hbSuggestions.setVisible(false);
        txtInsertNumCoins.setDisable(false);

        // Create a ToggleGroup for the player selection radio buttons
        ToggleGroup group = new ToggleGroup();
        rbPlayerOne.setToggleGroup(group); // Add Player One radio button to the group
        rbPlayerTwo.setToggleGroup(group); // Add Player Two radio button to the group

        // Pre-fill the number of coins if already set in GameConfig
        if (GameConfig.totalCoins != 0)
            txtInsertNumCoins.setText(GameConfig.totalCoins + "");

        // Handle the range inputs based on GameConfig values
        if (GameConfig.fromRange == 0) {
            txtRangeFrom.setText(""); // Clear the range from field if not set
        }
        if (GameConfig.toRange == 0) {
            txtRangeTo.setText(""); // Clear the range to field if not set
        }
        if (GameConfig.fromRange > 0 && GameConfig.toRange > 0) {
            // Pre-fill the range inputs if valid values are available
            txtRangeFrom.setText(GameConfig.fromRange + "");
            txtRangeTo.setText(GameConfig.toRange + "");
        }

        // Adjust UI visibility and behavior based on the insertion type
        if (GameConfig.insertationType.equalsIgnoreCase("File")) {
            hbCoinsRange.setVisible(false); // Hide coin range options
            txtInsertNumCoins.setDisable(true); // Disable the number of coins input
            btInsertNumCoins.setVisible(false); // Hide the insert button
            if (GameConfig.totalCoins > 0) {
                btInsertNumCoins.setText("Edit"); // Change button text to "Edit"
                hbInsertCoinsPlayerName.setVisible(true); // Show player name fields
                hbRbPlayersTurn.setVisible(true); // Show turn selection
                btPlay.setDisable(false); // Enable the play button
            } else {
                btInsertNumCoins.setText("Insert"); // Set button text to "Insert"
                hbInsertCoinsPlayerName.setVisible(true); // Show player name fields
                hbRbPlayersTurn.setVisible(true); // Show turn selection
                btPlay.setDisable(true); // Disable the play button
            }
        } else if (GameConfig.insertationType.equalsIgnoreCase("Manual")) {
            hbCoinsRange.setVisible(false); // Hide coin range options
            if (GameConfig.totalCoins > 0) {
                btInsertNumCoins.setText("Edit"); // Change button text to "Edit"
                txtInsertNumCoins.setDisable(false); // Enable the number of coins input
                btInsertNumCoins.setVisible(true); // Show the insert button
                hbInsertCoinsPlayerName.setVisible(true); // Show player name fields
                hbRbPlayersTurn.setVisible(true); // Show turn selection
                btPlay.setDisable(false); // Enable the play button
            } else {
                btInsertNumCoins.setText("Insert"); // Set button text to "Insert"
                txtInsertNumCoins.setDisable(false); // Enable the number of coins input
                btInsertNumCoins.setVisible(true); // Show the insert button
                hbInsertCoinsPlayerName.setVisible(false); // Hide player name fields
                hbRbPlayersTurn.setVisible(false); // Hide turn selection
                btPlay.setDisable(true); // Disable the play button
            }
        } else if (GameConfig.insertationType.equalsIgnoreCase("Random")) {
            if (GameConfig.totalCoins > 0) {
                btInsertNumCoins.setText("Edit"); // Change button text to "Edit"
                txtInsertNumCoins.setDisable(false); // Enable the number of coins input
                btInsertNumCoins.setVisible(true); // Show the insert button
                hbCoinsRange.setVisible(true); // Show coin range options
                hbInsertCoinsPlayerName.setVisible(true); // Show player name fields
                hbRbPlayersTurn.setVisible(true); // Show turn selection
                btPlay.setDisable(false); // Enable the play button
            } else {
                btInsertNumCoins.setText("Insert"); // Set button text to "Insert"
                txtInsertNumCoins.setDisable(false); // Enable the number of coins input
                hbCoinsRange.setVisible(false); // Hide coin range options
                btInsertNumCoins.setVisible(true); // Show the insert button
                hbInsertCoinsPlayerName.setVisible(false); // Hide player name fields
                hbRbPlayersTurn.setVisible(false); // Hide turn selection
                btPlay.setDisable(true); // Disable the play button
            }
        }

        // Pre-fill player names if they exist in GameConfig
        if (!GameConfig.player1Name.isEmpty() && !GameConfig.player2Name.isEmpty()) {
            txtInstCoinsPlayerOneName.setText(GameConfig.player1Name);
            txtInstCoinsPlayerTwoName1.setText(GameConfig.player2Name);
        } else if (!GameConfig.player1Name.isEmpty()) {
            txtInstCoinsPlayerOneName.setText(GameConfig.player1Name);
        } else if (!GameConfig.player2Name.isEmpty()) {
            txtInstCoinsPlayerTwoName1.setText(GameConfig.player2Name);
        } else {
            // Clear player names if not set
            txtInstCoinsPlayerOneName.setText(GameConfig.player1Name);
            txtInstCoinsPlayerTwoName1.setText(GameConfig.player2Name);
        }

        // Pre-select the appropriate radio button based on the starting turn in GameConfig
        if (GameConfig.player1Turn) {
            rbPlayerOne.setSelected(true); // Select Player 1
        } else if (GameConfig.player2Turn) {
            rbPlayerTwo.setSelected(true); // Select Player 2
        } else {
            // Deselect both radio buttons if no turn is set
            rbPlayerOne.setSelected(false);
            rbPlayerTwo.setSelected(false);
        }
    }

    // Displays an alert dialog with the specified type, header, and content
    private void showAlert(Alert.AlertType alertType, String header, String content) {
        // Create a new Alert dialog with the specified alert type
        alert = new Alert(alertType);

        // Set the header text for the alert
        alert.setHeaderText(header);

        // Set the content text (message) for the alert
        alert.setContentText(content);

        // Show the alert dialog
        alert.show();
    }


    @FXML
    void onTxtKeyPressed(ActionEvent event) {
        try {
            // Get the user input from the text field and trim any extra spaces
            String input = txtInsertNumCoins.getText().trim();

            // Handle behavior based on the insertion type from GameConfig
            if (GameConfig.insertationType.equalsIgnoreCase("File")) {
                // If the insertion type is "File", hide the coin range options and exit
                hbCoinsRange.setVisible(false);
                return;
            }

            if (GameConfig.insertationType.equalsIgnoreCase("Random")) {
                // Handle input validation for "Random" insertion type
                if (input.isEmpty()) {
                    // Show error alert for empty input
                    showAlert(Alert.AlertType.ERROR, "Error: Empty Field", "Please enter the number of coins!");
                    return;
                } else if (Integer.parseInt(input) < 1) {
                    // Show error alert for negative or zero input
                    showAlert(Alert.AlertType.ERROR, "Error: Negative or Zero Number", "Please enter a positive number!");
                    return;
                } else if (Integer.parseInt(input) % 2 != 0) {
                    // Show error alert for odd numbers and suggest correction
                    showAlert(Alert.AlertType.ERROR, "Error: Invalid Odd Number", "Please enter an even number!");
                    displaySuggestions(); // Display suggestions for valid input
                    return;
                } else {
                    // Valid input: update GameConfig and enable related UI elements
                    GameConfig.totalCoins = Integer.parseInt(input);
                    hbCoinsRange.setVisible(true);
                    hbInsertCoinsPlayerName.setVisible(true);
                    hbRbPlayersTurn.setVisible(true);
                    btPlay.setDisable(false);
                    return;
                }
            } else {
                // Handle input validation for other insertion types (e.g., "Manual")
                if (input.isEmpty()) {
                    // Show error alert for empty input
                    showAlert(Alert.AlertType.ERROR, "Error: Empty Field", "Please enter the number of coins!");
                    return;
                } else if (Integer.parseInt(input) < 1) {
                    // Show error alert for negative or zero input
                    showAlert(Alert.AlertType.ERROR, "Error: Negative or Zero Number", "Please enter a positive number!");
                    return;
                } else if (Integer.parseInt(input) % 2 != 0) {
                    // Show error alert for odd numbers and suggest correction
                    showAlert(Alert.AlertType.ERROR, "Error: Invalid Number", "Please enter an even number!");
                    displaySuggestions(); // Display suggestions for valid input
                    return;
                } else {
                    // Valid input: update GameConfig and load the manual insertion scene
                    GameConfig.totalCoins = Integer.parseInt(input);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("manualInsertation.fxml"));
                    VBox vbox = loader.load();
                    vbInsertCoinsScene.getScene().setRoot(vbox);
                }
            }
        } catch (NumberFormatException e) {
            // Handle invalid number format exception
            showAlert(Alert.AlertType.ERROR, "Error: Invalid Number", "Please enter a valid number!");
            e.printStackTrace();
        } catch (IOException e) {
            // Handle IO exceptions when loading the FXML file
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading the scene.");
            e.printStackTrace();
        }
    }

    // Displays suggestions for valid even and positive numbers near the user's input
    private void displaySuggestions() {
        // Make the suggestions box visible
        hbSuggestions.setVisible(true);

        // Calculate the initial suggestions by adjusting the user's input
        int suggestionOne = Integer.parseInt(txtInsertNumCoins.getText().trim()) - 3;
        int suggestionTwo = Integer.parseInt(txtInsertNumCoins.getText().trim()) - 1;
        int suggestionThree = Integer.parseInt(txtInsertNumCoins.getText().trim()) + 1;
        int suggestionFour = Integer.parseInt(txtInsertNumCoins.getText().trim()) + 3;

        // Ensure all suggestions are even and positive
        if (suggestionOne <= 0 || suggestionOne % 2 != 0) {
            suggestionOne = 2; // Default to 2 if invalid
        }
        if (suggestionTwo <= 0 || suggestionTwo % 2 != 0) {
            suggestionTwo = 4; // Default to 4 if invalid
        }
        if (suggestionThree <= 0 || suggestionThree % 2 != 0) {
            suggestionThree = 6; // Default to 6 if invalid
        }
        if (suggestionFour <= 0 || suggestionFour % 2 != 0) {
            suggestionFour = 8; // Default to 8 if invalid
        }

        // Ensure unique suggestions by adjusting any duplicates
        if (suggestionTwo == suggestionOne) {
            suggestionTwo += 2; // Increment suggestionTwo to ensure uniqueness
        }
        if (suggestionThree == suggestionOne || suggestionThree == suggestionTwo) {
            suggestionThree += 2; // Increment suggestionThree to ensure uniqueness
        }
        if (suggestionFour == suggestionOne || suggestionFour == suggestionTwo || suggestionFour == suggestionThree) {
            suggestionFour += 2; // Increment suggestionFour to ensure uniqueness
        }

        // Update the text of suggestion buttons with the calculated values
        btSuggOne.setText(suggestionOne + "");
        btSuggTwo.setText(suggestionTwo + "");
        btSuggThree.setText(suggestionThree + "");
        btSuggFour.setText(suggestionFour + "");
    }

    @FXML
    void onInsertClicked(ActionEvent event) {
        try {
            // Get the user's input from the text field and remove extra spaces
            String input = txtInsertNumCoins.getText().trim();

            // Handle behavior based on the insertion type from GameConfig
            if (GameConfig.insertationType.equalsIgnoreCase("File")) {
                // If the insertion type is "File", hide the coin range options and exit
                hbCoinsRange.setVisible(false);
                return;
            }

            // Handle the "Random" insertion type
            if (GameConfig.insertationType.equalsIgnoreCase("Random")) {
                // Check for empty input
                if (input.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error: Empty Field", "Please enter the number of coins!");
                    return;
                }
                // Check for negative or zero input
                else if (Integer.parseInt(input) < 1) {
                    showAlert(Alert.AlertType.ERROR, "Error: Negative or Zero Number", "Please enter a positive number!");
                    return;
                }
                // Check for odd input and provide suggestions
                else if (Integer.parseInt(input) % 2 != 0) {
                    showAlert(Alert.AlertType.ERROR, "Error: Invalid Number", "Please enter an even number!");
                    displaySuggestions();
                    return;
                }
                // Valid input: update GameConfig and enable UI elements
                else {
                    GameConfig.totalCoins = Integer.parseInt(input);
                    hbCoinsRange.setVisible(true);
                    hbInsertCoinsPlayerName.setVisible(true);
                    hbRbPlayersTurn.setVisible(true);
                    btPlay.setDisable(false);
                    return;
                }
            }
            // Handle the "Manual" insertion type and other cases
            else {
                // Check for empty input
                if (input.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error: Empty Field", "Please enter the number of coins!");
                    return;
                }
                // Check for negative or zero input
                else if (Integer.parseInt(input) < 1) {
                    showAlert(Alert.AlertType.ERROR, "Error: Negative or Zero Number", "Please enter a positive number!");
                    return;
                }
                // Check for odd input and provide suggestions
                else if (Integer.parseInt(input) % 2 != 0) {
                    showAlert(Alert.AlertType.ERROR, "Error: Invalid Number", "Please enter an even number!");
                    displaySuggestions();
                    return;
                }
                // Valid input: update GameConfig and load the manual insertion scene
                else {
                    GameConfig.totalCoins = Integer.parseInt(input);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("manualInsertation.fxml"));
                    VBox vbox = loader.load();
                    vbInsertCoinsScene.getScene().setRoot(vbox);
                }
            }
        }
        // Handle invalid number format exceptions
        catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error: Invalid Number", "Please enter a valid number!");
            e.printStackTrace();
        }
        // Handle IO exceptions when loading the FXML file
        catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading the scene.");
            e.printStackTrace();
        }
    }

//    @FXML
//    void onInsertValuesClicked(ActionEvent event) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("manualInsertation.fxml"));
//        VBox vbox = loader.load();
//        GameConfig.totalCoins = Integer.parseInt(txtInsertNumCoins.getText());
//        vbInsertCoinsScene.getScene().setRoot(vbox);
//    }

    @FXML
    void onPlayClicked(ActionEvent event) throws IOException {
        try {
            // Handle Manual insertion type
            if (GameConfig.insertationType.equalsIgnoreCase("Manual")) {
                // Validate total coins against input
                if (GameConfig.totalCoins < Integer.parseInt(txtInsertNumCoins.getText().trim()) ||
                        GameConfig.totalCoins != Integer.parseInt(txtInsertNumCoins.getText().trim())) {
                    GameConfig.totalCoins = Integer.parseInt(txtInsertNumCoins.getText().trim());
                    changeTheNumberOfCoins(); // Update the number of coins
                    return;
                }

                // Ensure all coins are filled if GameConfig is not marked empty
                if (!GameConfig.isEmpty) {
                    for (int i = 0; i < GameConfig.coins.length; i++) {
                        if (GameConfig.coins[i] == 0) {
                            showAlert(Alert.AlertType.ERROR, "Error: Empty Field(s)", "Please fill all the coins values!");
                            return; // Exit early if any coin value is missing
                        }
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error: Empty Fields", "Please fill all the coins values!");
                    return;
                }
            }

            // Handle Random insertion type
            if (GameConfig.insertationType.equalsIgnoreCase("Random")) {
                // Check and update total coins if input differs
                if (GameConfig.totalCoins != Integer.parseInt(txtInsertNumCoins.getText().trim())) {
                    showAlert(Alert.AlertType.WARNING, "New Number of Coins!",
                            "The number of coins became " + Integer.parseInt(txtInsertNumCoins.getText().trim()) +
                                    " instead of " + GameConfig.totalCoins + "!");
                    GameConfig.totalCoins = Integer.parseInt(txtInsertNumCoins.getText().trim());
                }

                // Validate range inputs
                if (txtRangeFrom.getText().isEmpty() || Integer.parseInt(txtRangeFrom.getText().trim()) <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Error: Wrong Range", "Please enter a valid initial for the range!");
                    return;
                } else if (txtRangeTo.getText().isEmpty() || Integer.parseInt(txtRangeTo.getText().trim()) <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Error: Wrong Range", "Please enter a valid end for the range!");
                    return;
                } else if (Integer.parseInt(txtRangeTo.getText().trim()) < Integer.parseInt(txtRangeFrom.getText().trim())) {
                    showAlert(Alert.AlertType.ERROR, "Error", "The initial range value cannot be greater than the end value.");
                    return;
                } else {
                    // Update range values in GameConfig
                    GameConfig.fromRange = Integer.parseInt(txtRangeFrom.getText().trim());
                    GameConfig.toRange = Integer.parseInt(txtRangeTo.getText().trim());
                }
            }

            // Ensure a starting player is selected
            if (!rbPlayerOne.isSelected() && !rbPlayerTwo.isSelected()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a Player To Start!");
                return;
            }

            // Set the starting player in GameConfig
            GameConfig.player1Turn = rbPlayerOne.isSelected();
            GameConfig.player2Turn = rbPlayerTwo.isSelected();

            // Validate player names and other input fields
            if (!validateTF()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid number!");
                return;
            }
            if (txtInstCoinsPlayerOneName.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a name for player one!");
                return;
            }
            if (txtInstCoinsPlayerTwoName1.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a name for player two!");
                return;
            }

            // Update player names in GameConfig
            GameConfig.player1Name = txtInstCoinsPlayerOneName.getText();
            GameConfig.player2Name = txtInstCoinsPlayerTwoName1.getText();

            // Initialize the coins array in GameConfig
            GameConfig.initializeCoinsArray();

            // Debug: Print the current game configuration
            System.out.println("Total Coins: " + GameConfig.totalCoins);
            System.out.println("Coins Array: ");
            for (int coin : GameConfig.coins) {
                System.out.print(coin + " ");
            }
            System.out.println();

            // Transition to the game scene
            switchToGameScene();
        } catch (NumberFormatException e) {
            // Handle invalid number inputs
            showAlert(Alert.AlertType.ERROR, "Error: Invalid Input", "Please enter a valid number!");
            e.printStackTrace();
        } catch (Exception e) {
            // Handle general exceptions
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred!");
            e.printStackTrace();
        }
    }

    // Updates the UI and configuration when the number of coins changes
    private void changeTheNumberOfCoins() {
        // Update the button text to "Edit" to indicate the editing state
        btInsertNumCoins.setText("Edit");

        // Re-enable the number of coins input field for modification
        txtInsertNumCoins.setDisable(false);

        // Ensure the "Insert" button is visible for user interaction
        btInsertNumCoins.setVisible(true);

        // Hide player name input fields and turn selection options
        hbInsertCoinsPlayerName.setVisible(false);
        hbRbPlayersTurn.setVisible(false);

        // Disable the "Play" button until the required values are provided
        btPlay.setDisable(true);

        // Display a warning alert to inform the user to fill the missing values
        showAlert(Alert.AlertType.WARNING, "New Number of Coins!", "Fill the missing values first!");

        // Uncomment the following line to adjust ManualInsertationController's size logic
        // ManualInsertationController.size = GameConfig.totalCoins - (GameConfig.totalCoins - ManualInsertationController.size);
    }


    @FXML
    void onSuggOneClicked(ActionEvent event) {
        txtInsertNumCoins.setText(((Button) event.getSource()).getText().trim());
    }

    // Switches the current scene to the game scene based on the selected mode
    private void switchToGameScene() throws IOException {
        FXMLLoader loader;

        // Check which game mode was selected: "Two Computers"
        if (GameConfig.whoCalled.equalsIgnoreCase("Two Computers")) {
            // Load the FXML file for the Computer Game scene
            loader = new FXMLLoader(getClass().getResource("ComputerGame.fxml"));
            BorderPane bdPane = loader.load();

            // Get the controller for the Computer Game
            ComputerGameController computerGameController = loader.getController();

            // Set the game configuration for the Computer Game
            setGameConfig(computerGameController, null);

            // Retrieve the number of coins from the input field and update GameConfig
            int numCoins = Integer.parseInt(txtInsertNumCoins.getText().trim());
            GameConfig.totalCoins = numCoins;

            // Display a success message showing the number of coins
            showAlert(Alert.AlertType.INFORMATION, "Success", "Number of coins is: " + numCoins);

            // Set the root of the scene to the Computer Game layout
            vbInsertCoinsScene.getScene().setRoot(bdPane);
        }
        // Check which game mode was selected: "Two Players"
        else if (GameConfig.whoCalled.equalsIgnoreCase("Two Players")) {
            // Load the FXML file for the Two Players Game scene
            loader = new FXMLLoader(getClass().getResource("TwoPlayersGame.fxml"));
            BorderPane bdPane = loader.load();

            // Get the controller for the Two Players Game
            TwoPlayersGameController twoPlayersGameController = loader.getController();

            // Set the game configuration for the Two Players Game
            setGameConfig(null, twoPlayersGameController);

            // Retrieve the number of coins from the input field and update GameConfig
            int numCoins = Integer.parseInt(txtInsertNumCoins.getText().trim());
            GameConfig.totalCoins = numCoins;

            // Display a success message showing the number of coins
            showAlert(Alert.AlertType.INFORMATION, "Success", "Number of coins is: " + numCoins);

            // Set the root of the scene to the Two Players Game layout
            vbInsertCoinsScene.getScene().setRoot(bdPane);
        }
    }

    // Configures the game settings for either a ComputerGameController or TwoPlayersGameController
    private void setGameConfig(ComputerGameController controller, TwoPlayersGameController twoPlayersGameController) {
        // Initialize the coins array in GameConfig if it has not been initialized
        GameConfig.initializeCoinsArray();

        // Check if the controller is for a computer game
        if (controller != null) {
            // Set the starting player's turn in the ComputerGameController
            controller.setPlayerOneTurn(rbPlayerOne.isSelected());

            // Update the GameConfig to reflect the starting player's turn
            if (rbPlayerOne.isSelected()) {
                GameConfig.player1Turn = true; // Player One starts
            } else {
                GameConfig.player2Turn = true; // Player Two starts
            }

            // Initialize the game in the ComputerGameController
            controller.initializeGame();
        }
        // Check if the controller is for a two-player game
        else if (twoPlayersGameController != null) {
            // Set the starting player's turn in the TwoPlayersGameController
            twoPlayersGameController.setPlayerOneTurn(rbPlayerOne.isSelected());

            // Update the GameConfig to reflect the starting player's turn
            if (rbPlayerOne.isSelected()) {
                GameConfig.player1Turn = true; // Player One starts
            } else {
                GameConfig.player2Turn = true; // Player Two starts
            }

            // Initialize the game in the TwoPlayersGameController
            twoPlayersGameController.initializeGame();
        }
    }

    // Validates the text field input for the number of coins
    private boolean validateTF() {
        // Retrieve the input from the text field and trim any extra spaces
        String input = txtInsertNumCoins.getText().trim();

        try {
            // Check if the input is empty
            if (input.isEmpty()) {
                // Return false if the input field is empty
                return false;
            }
            // Check if the input is less than 1 (negative or zero)
            else if (Integer.parseInt(input) < 1) {
                // Return false if the input is not a positive number
                return false;
            }
            // Check if the input is an odd number
            else if (Integer.parseInt(input) % 2 != 0) {
                // Return false if the input is not an even number
                return false;
            }
            // Input is valid if all checks pass
            else {
                return true;
            }
        } catch (NumberFormatException e) {
            // Handle cases where the input is not a valid number
            // Return false if a NumberFormatException occurs
            return false;
        }
    }

    // Navigates back to the main menu and resets game configurations
    @FXML
    void onMainMenuClicked(ActionEvent event) throws IOException {
        // Load the main menu FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
        BorderPane borderPane = fxmlLoader.load();

        // Set the current scene's root to the main menu layout
        vbInsertCoinsScene.getScene().setRoot(borderPane);

        // Reset game configurations to their default values
        GameConfig.totalCoins = 0;          // Reset the total number of coins
        GameConfig.player1Name = "";        // Clear Player 1's name
        GameConfig.player2Name = "";        // Clear Player 2's name
        GameConfig.player1Turn = false;     // Reset Player 1's turn flag
        GameConfig.player2Turn = false;     // Reset Player 2's turn flag
        GameConfig.fromRange = 0;           // Reset the range start
        GameConfig.toRange = 0;             // Reset the range end
    }

    // Navigates back to the inserting options menu and resets game configurations
    @FXML
    void onBackClicked(ActionEvent event) throws IOException {
        // Reset game configurations to their default values
        GameConfig.totalCoins = 0;          // Reset the total number of coins
        GameConfig.player1Name = "";        // Clear Player 1's name
        GameConfig.player2Name = "";        // Clear Player 2's name
        GameConfig.player1Turn = false;     // Reset Player 1's turn flag
        GameConfig.player2Turn = false;     // Reset Player 2's turn flag
        GameConfig.fromRange = 0;           // Reset the range start
        GameConfig.toRange = 0;             // Reset the range end

        // Load the inserting options FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("insertingOptions.fxml"));
        BorderPane borderPane = fxmlLoader.load();

        // Set the current scene's root to the inserting options layout
        vbInsertCoinsScene.getScene().setRoot(borderPane);
    }
}
