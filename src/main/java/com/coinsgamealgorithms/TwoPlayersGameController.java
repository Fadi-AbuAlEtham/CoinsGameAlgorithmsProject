package com.coinsgamealgorithms;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class TwoPlayersGameController {
    @FXML
    private BorderPane bdGameScene;

    @FXML
    private FlowPane flowPaneGameScene;

    @FXML
    private Text lblPlayerOneName;

    @FXML
    private Text lblPlayerOneScore;

    @FXML
    private Text lblPlayerTwoName;

    @FXML
    private Text lblPlayerTwoScore;

    @FXML
    private ImageView playerOneImage;

    @FXML
    private ImageView playerTwoImage;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ScrollPane scrollPanePlayerOne;

    @FXML
    private ScrollPane scrollPanePlayerTwo;

    @FXML
    private TextField txtPlayerOneScore;

    @FXML
    private TextField txtPlayerTwoScore;

    @FXML
    private VBox vbPlayerOneGameCoins;

    @FXML
    private VBox vbPlayerTwoGameCoins;

    @FXML
    private TextArea txtArea;

    private boolean isAnimating;
    private Image coinImage;
    private int playerOneScore = 0;
    private int playerTwoScore = 0;
    private boolean isPlayerOneTurn;

    public void setPlayerOneTurn(boolean playerOneTurn) {
        isPlayerOneTurn = playerOneTurn;
    }

    // Sets up the initial labels for Player One and Player Two with their respective names from GameConfig
    @FXML
    void initialize() {
        // Display Player One's name using GameConfig
        lblPlayerOneName.setText(GameConfig.player1Name);

        // Display Player Two's name using GameConfig
        lblPlayerTwoName.setText(GameConfig.player2Name);
    }

    // Initializes the game UI and logic
    @FXML
    public void initializeGame() {
        // Check if the center of the game scene is not already set to the scroll pane
        if (bdGameScene.getCenter() != scrollPane) {
            bdGameScene.setCenter(scrollPane); // Set the center to the scroll pane
        }

        // Update Player One and Player Two labels with their names
        lblPlayerOneName.setText(GameConfig.player1Name);
        lblPlayerTwoName.setText(GameConfig.player2Name);

        // Enable interactions with the game flow pane
        flowPaneGameScene.setDisable(false);

        // Initialize the coins array in GameConfig
        GameConfig.initializeCoinsArray();

        // Populate the game board with the initialized coins
        initializeCoins();

        // Update the player scores at the start of the game
        updateScores();

        // Apply visual effects to indicate the starting player
        applyTurnEffect(playerOneImage, isPlayerOneTurn); // Highlight Player One's turn if they start
        applyTurnEffect(playerTwoImage, !isPlayerOneTurn); // Dim Player Two's turn if they don't start
    }

    // Initializes the coins in the game based on the insertion type and populates the game board
    public void initializeCoins() {
        // Update the player name and score labels
        lblPlayerOneName.setText(GameConfig.player1Name);
        lblPlayerTwoName.setText(GameConfig.player2Name);
        lblPlayerOneScore.setText(GameConfig.player1Name);
        lblPlayerTwoScore.setText(GameConfig.player2Name);

        // Handle "Random" insertion type
        if (GameConfig.insertationType.equalsIgnoreCase("Random")) {
            System.out.println("Random Line 361");

            // Initialize coins array and clear the game board
            Random random = new Random();
            GameConfig.initializeCoinsArray();
            flowPaneGameScene.getChildren().clear();

            // Load the coin image resource
            coinImage = new Image(TwoPlayersGameController.class.getResource("coin image.png").toExternalForm());

            // Get the maximum value as a string and calculate its length
            String maxValueString = String.valueOf(GameConfig.toRange);
            int maxValueLength = maxValueString.length();

            // Adjust image size based on the length of the maximum value
            double baseSize = 60; // Base size for the coin image
            double scaleFactor = 10; // Scale factor for length
            double adjustedSize = baseSize + (maxValueLength * scaleFactor);

            // Populate the board with randomly generated coins within the range
            for (int i = 0; i < GameConfig.coins.length; i++) {
                int range = GameConfig.toRange - GameConfig.fromRange + 1;
                GameConfig.coins[i] = random.nextInt(range) + GameConfig.fromRange;
                System.out.println("Generated random coin value: " + GameConfig.coins[i]);

                // Create and configure the coin visual representation
                ImageView imageView = new ImageView(coinImage);
                imageView.setFitWidth(adjustedSize);
                imageView.setFitHeight(adjustedSize);

                Label coinValueLabel = new Label(String.valueOf(GameConfig.coins[i]));
                coinValueLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

                StackPane coinPane = new StackPane(imageView, coinValueLabel);
                coinPane.setId("coinPane-" + i); // Set an ID for tracking
                coinPane.setOnMouseClicked(event -> handleCoinSelection(coinPane));

                flowPaneGameScene.getChildren().add(coinPane);
            }
            enableFirstAndLastCoins();

            // Handle "Manual" insertion type
        } else if (GameConfig.insertationType.equalsIgnoreCase("Manual")) {
            System.out.println("Manual Line 383");

            // Clear the game board
            flowPaneGameScene.getChildren().clear();
            coinImage = new Image(TwoPlayersGameController.class.getResource("coin image.png").toExternalForm());

            // Get the maximum value as a string and calculate its length
            String maxValueString = String.valueOf(Arrays.stream(GameConfig.coins).max().getAsInt());
            int maxValueLength = maxValueString.length();

            // Adjust image size based on the length of the maximum value
            double baseSize = 60; // Base size for the coin image
            double scaleFactor = 10; // Scale factor for length
            double adjustedSize = baseSize + (maxValueLength * scaleFactor);

            if (GameConfig.coins == null || GameConfig.coins.length == 0) {
                System.out.println("Error Line 410");
            }

            // Populate the board with manually inserted coins or placeholders
            for (int i = 0; i < GameConfig.coins.length; i++) {
                ImageView imageView = new ImageView(coinImage);
                imageView.setFitWidth(adjustedSize);
                imageView.setFitHeight(adjustedSize);

                Label coinValueLabel = new Label(String.valueOf(GameConfig.coins[i]));
                coinValueLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

                StackPane coinPane = new StackPane(imageView, coinValueLabel);
                coinPane.setId("coinPane-" + i); // Set an ID for tracking
                coinPane.setDisable(false);
                coinPane.setOnMouseClicked(event -> handleCoinSelection(coinPane));

                flowPaneGameScene.getChildren().add(coinPane);
            }
            enableFirstAndLastCoins();

            // Handle "File" insertion type
        } else if (GameConfig.insertationType.equalsIgnoreCase("File")) {
            System.out.println("File Line 415");

            // Initialize coins array and clear the game board
            GameConfig.initializeCoinsArray();
            flowPaneGameScene.getChildren().clear();
            coinImage = new Image(TwoPlayersGameController.class.getResource("coin image.png").toExternalForm());

            // Get the maximum value as a string and calculate its length
            String maxValueString = String.valueOf(Arrays.stream(GameConfig.coins).max().getAsInt());
            int maxValueLength = maxValueString.length();

            // Adjust image size based on the length of the maximum value
            double baseSize = 60; // Base size for the coin image
            double scaleFactor = 10; // Scale factor for length
            double adjustedSize = baseSize + (maxValueLength * scaleFactor);

            // Populate the board with coins from the file
            for (int i = 0; i < GameConfig.coins.length; i++) {
                ImageView imageView = new ImageView(coinImage);
                imageView.setFitWidth(adjustedSize);
                imageView.setFitHeight(adjustedSize);

                Label coinValueLabel = new Label(String.valueOf(GameConfig.coins[i]));
                coinValueLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

                StackPane coinPane = new StackPane(imageView, coinValueLabel);
                coinPane.setId("coinPane-" + i); // Set an ID for tracking
                coinPane.setOnMouseEntered(event -> coinPane.setCursor(Cursor.HAND));
                coinPane.setOnMouseClicked(event -> handleCoinSelection(coinPane));

                flowPaneGameScene.getChildren().add(coinPane);
            }
            enableFirstAndLastCoins();
        }
    }

   /* // Initializes the coins with random values and displays them in the FlowPane
    public void initializeCoins() {
        Random random = new Random();

        flowPaneGameScene.getChildren().clear();
        coinImage = new Image(TwoPlayersGameController.class.getResource("coin image.png").toExternalForm());

        for (int i = 0; i < coins.length; i++) {
            coins[i] = random.nextInt(100) + 1;

            ImageView imageView = new ImageView(coinImage);
            imageView.setFitWidth(60);
            imageView.setFitHeight(60);

            Label coinValueLabel = new Label(String.valueOf(coins[i]));
            coinValueLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

            StackPane coinPane = new StackPane(imageView, coinValueLabel);
//            coinPane.setDisable(true); // Initially disable all coins

            coinPane.setOnMouseClicked(event -> handleCoinSelection(coinPane));

            flowPaneGameScene.getChildren().add(coinPane);
        }

        // Enable only the first and last coins at the start
//        enableFirstAndLastCoins();
    }*/

    // Handles the selection and animation of a coin during the game
    private void handleCoinSelection(StackPane coinPane) {
        // Prevent selection if an animation is in progress or the coin is invalid
        if (isAnimating || !isFirstOrLastCoin(coinPane)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(isAnimating
                    ? "Please wait for the current move to complete!"
                    : "You can only select the first or last coin!");
            alert.show();
            return;
        }

        // Retrieve the value of the selected coin
        int selectedCoinValue = Integer.parseInt(((Label) coinPane.getChildren().get(1)).getText());
        VBox targetVBox = isPlayerOneTurn ? vbPlayerOneGameCoins : vbPlayerTwoGameCoins;

        // Create a clone of the selected coin for animation and final placement
        StackPane coinClone = new StackPane();
        ImageView clonedImageView = new ImageView(coinImage);
        clonedImageView.setFitWidth(60);
        clonedImageView.setFitHeight(60);
        Label clonedLabel = new Label(String.valueOf(selectedCoinValue));
        clonedLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        coinClone.getChildren().addAll(clonedImageView, clonedLabel);

        // Calculate initial and target positions for animation
        double startX = coinPane.localToScene(coinPane.getBoundsInLocal()).getMinX();
        double startY = coinPane.localToScene(coinPane.getBoundsInLocal()).getMinY();
        double endX, endY;
        if (isPlayerOneTurn) {
            endX = scrollPanePlayerOne.localToScene(targetVBox.getBoundsInLocal()).getCenterX();
            endY = scrollPanePlayerOne.localToScene(targetVBox.getBoundsInLocal()).getCenterY();
        } else {
            endX = scrollPanePlayerTwo.localToScene(targetVBox.getBoundsInLocal()).getCenterX();
            endY = scrollPanePlayerTwo.localToScene(targetVBox.getBoundsInLocal()).getCenterY();
        }
        double midX = (startX + endX) / 2; // Calculate midpoint for smoother transition

        // Create horizontal animation to the midpoint
        TranslateTransition horizontalTransition = new TranslateTransition(Duration.seconds(0.3), coinPane);
        horizontalTransition.setByX(midX - startX);

        // Create vertical animation from the midpoint to the target VBox
        TranslateTransition verticalTransition = new TranslateTransition(Duration.seconds(0.3), coinPane);
        verticalTransition.setByX(endX - midX);
        verticalTransition.setByY(endY - startY);

        // Chain animations: horizontal movement followed by vertical movement
        horizontalTransition.setOnFinished(e -> verticalTransition.play());
        verticalTransition.setOnFinished(event -> {
            // After the animation, add the cloned coin to the target VBox
            targetVBox.getChildren().add(coinClone);

            // Update scores and remove the original coin from FlowPane
            if (isPlayerOneTurn) {
                playerOneScore += selectedCoinValue;
            } else {
                playerTwoScore += selectedCoinValue;
            }
            flowPaneGameScene.getChildren().remove(coinPane);
            updateScores();

            // Check if the game has ended, otherwise toggle turns
            if (flowPaneGameScene.getChildren().isEmpty()) {
                endGame();
            } else {
                isPlayerOneTurn = !isPlayerOneTurn;
                applyTurnEffect(playerOneImage, isPlayerOneTurn);
                applyTurnEffect(playerTwoImage, !isPlayerOneTurn);
                enableFirstAndLastCoins(); // Update coin states for the next turn
            }

            // Unlock selection after animation completion
            isAnimating = false;
        });

        // Lock animations and start with the horizontal movement
        isAnimating = true;
        horizontalTransition.play();
    }

//    // Method to enable only the first and last coins in the FlowPane
//    private void enableFirstAndLastCoins() {
//        for (int i = 0; i < flowPaneGameScene.getChildren().size(); i++) {
//            StackPane coinPane = (StackPane) flowPaneGameScene.getChildren().get(i);
//            // Disable all coins first
//            coinPane.setDisable(true);
//        }
//        // Enable the first and last coins if they exist
//        if (!flowPaneGameScene.getChildren().isEmpty()) {
//            flowPaneGameScene.getChildren().get(0).setDisable(false); // First coin
//            flowPaneGameScene.getChildren().get(flowPaneGameScene.getChildren().size() - 1).setDisable(false); // Last coin
//        }
//    }

    // Enables interaction only for the first and last coins in the FlowPane and dims others
    private void enableFirstAndLastCoins() {
        // Iterate through all coins (StackPane) in the FlowPane
        for (int i = 0; i < flowPaneGameScene.getChildren().size(); i++) {
            StackPane coinPane = (StackPane) flowPaneGameScene.getChildren().get(i);

            // Clear any previous dimmed styles to reset the state
            coinPane.getStyleClass().remove("coin-dimmed");

            // Dim and disable interaction for coins that are neither the first nor the last
            if (i != 0 && i != flowPaneGameScene.getChildren().size() - 1) {
                coinPane.getStyleClass().add("coin-dimmed"); // Add a dimmed style class
                coinPane.setOnMouseClicked(null);           // Remove click event handler
            } else {
                // Enable interaction for the first and last coins
                coinPane.setOnMouseClicked(event -> handleCoinSelection(coinPane)); // Add click handler
                coinPane.setCursor(Cursor.HAND); // Change cursor to a hand for clickable coins
            }
        }
    }

    // Applies a visual effect to indicate the current player's turn
    private void applyTurnEffect(ImageView playerImage, boolean isTurnActive) {
        // Create a scale transition to animate the player's image size
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), playerImage);

        if (isTurnActive) {
            // If it's the player's turn, apply a growth and shine effect
            scaleTransition.setToX(1.2); // Increase the X scale by 20% (grow larger)
            scaleTransition.setToY(1.2); // Increase the Y scale by 20%
            playerImage.setEffect(new Glow(0.5)); // Apply a glow effect with intensity 0.5
        } else {
            // If it's not the player's turn, remove the grow and shine effect
            scaleTransition.setToX(1.0); // Restore the original X scale
            scaleTransition.setToY(1.0); // Restore the original Y scale
            playerImage.setEffect(null); // Remove any applied effects (e.g., glow)
        }

        // Play the scale transition animation
        scaleTransition.play();
    }

    // Check if a coinPane is the first or last element in the FlowPane
    private boolean isFirstOrLastCoin(StackPane coinPane) {
        if (flowPaneGameScene.getChildren().isEmpty()) {
            return false;
        }
        return coinPane == flowPaneGameScene.getChildren().get(0) || coinPane == flowPaneGameScene.getChildren().get(flowPaneGameScene.getChildren().size() - 1);
    }

    // Updates the score labels for both players and applies an animation to the active player's score
    private void updateScores() {
        // Update the score text fields with the current scores
        txtPlayerOneScore.setText(String.valueOf(playerOneScore)); // Update Player One's score
        txtPlayerTwoScore.setText(String.valueOf(playerTwoScore)); // Update Player Two's score

        // Apply an animation to the score label of the active player
        if (isPlayerOneTurn) {
            animateScoreChange(txtPlayerOneScore); // Animate Player One's score if it's their turn
        } else {
            animateScoreChange(txtPlayerTwoScore); // Animate Player Two's score if it's their turn
        }
    }

    // Animates the score label with a "pop" effect and a brief dimming transition
    private void animateScoreChange(TextField scoreField) {
        // Create a scale transition to make the score label "pop"
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), scoreField);
        scaleTransition.setFromX(1.0); // Start with the original size
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.2); // Temporarily grow by 20% in width
        scaleTransition.setToY(1.2); // Temporarily grow by 20% in height
        scaleTransition.setAutoReverse(true); // Shrink back to the original size after growing

        // Create a fade transition to make the score change more noticeable
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.3), scoreField);
        fadeTransition.setFromValue(1.0); // Start fully opaque
        fadeTransition.setToValue(0.7); // Briefly dim to 70% opacity
        fadeTransition.setAutoReverse(true); // Return to full opacity after dimming

        // Play both transitions together
        scaleTransition.play();
        fadeTransition.play();
    }


    // Ends the game, calculates the winner, and displays the result
    private void endGame() {
        // Create a new TextArea to display the game result
        txtArea = new TextArea();
        txtArea.getStyleClass().clear(); // Clear any existing styles
        txtArea.getStyleClass().add("custom-text-area"); // Add a custom style class for result formatting
        txtArea.setEditable(false); // Make the TextArea read-only
        txtArea.getStyleClass().add("text-area"); // Apply a CSS style class for visual consistency

        // Determine the winner based on the scores
        String winner;
        if (playerOneScore > playerTwoScore) {
            winner = "Game Over!\n\nCongratulations! Player 1: " + lblPlayerOneName.getText() +
                    " you won! :) \n\nHard Luck Player Two :(\n\n";
        } else if (playerTwoScore > playerOneScore) {
            winner = "Game Over!\n\nCongratulations! Player 2: " + lblPlayerTwoName.getText() +
                    " you won!\n\n";
        } else {
            winner = "It's a tie!\n"; // Handle the tie case
        }

        // Set the winner message in the TextArea
        txtArea.setText(winner);

        // Append both players' scores to the result
        txtArea.appendText("\n\nPlayer 1 (" + lblPlayerOneName.getText() + ") Score: " + playerOneScore +
                "\nPlayer 2 (" + lblPlayerTwoName.getText() + ") Score: " + playerTwoScore);

        // Display the TextArea in the center of the game scene
        bdGameScene.setCenter(txtArea);
    }

    // Handles the "Main Menu" button click, resets game settings, and navigates to the main menu
    @FXML
    void onMainMenuClicked(ActionEvent event) throws IOException {
        // Load the "mainMenu.fxml" file to navigate back to the main menu
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
        BorderPane bdPane = loader.load();

        // Replace the current scene's root with the main menu layout
        bdGameScene.getScene().setRoot(bdPane);

        // Reset all GameConfig settings to their initial states
        GameConfig.fromRange = 0;            // Reset the coin range lower bound
        GameConfig.toRange = 0;              // Reset the coin range upper bound
        GameConfig.totalCoins = 0;           // Reset the total number of coins
        GameConfig.coins = new int[GameConfig.totalCoins]; // Clear the coins array
        GameConfig.player1Name = "";         // Clear Player 1's name
        GameConfig.player2Name = "";         // Clear Player 2's name
        GameConfig.player1Turn = false;      // Reset Player 1's turn flag
        GameConfig.player2Turn = false;      // Reset Player 2's turn flag
    }

    // Handles the "Reset" button click, resets the game state, and navigates to the appropriate scene
    @FXML
    void onResetClicked(ActionEvent event) throws IOException {
        // Reset player scores to zero
        playerOneScore = 0;
        playerTwoScore = 0;
        updateScores(); // Update the score display

        // Reset turn logic based on GameConfig
        isPlayerOneTurn = GameConfig.player1Turn; // Restore the starting turn from GameConfig
        GameConfig.player1Turn = isPlayerOneTurn; // Sync Player 1's turn status
        GameConfig.player2Turn = !isPlayerOneTurn; // Set Player 2's turn to the opposite

        // Clear the coin displays for both players
        vbPlayerOneGameCoins.getChildren().clear(); // Clear Player 1's collected coins
        vbPlayerTwoGameCoins.getChildren().clear(); // Clear Player 2's collected coins

        // Ensure the center of the game scene is set to the scroll pane
        if (bdGameScene.getCenter() != scrollPane) {
            bdGameScene.setCenter(scrollPane);
        }

        // Check the insertion type and navigate to the appropriate scene
        if (GameConfig.insertationType.equalsIgnoreCase("Manual") ||
                GameConfig.insertationType.equalsIgnoreCase("File") ||
                GameConfig.insertationType.equalsIgnoreCase("Random")) {
            // Load the "insertCoins.fxml" scene for reconfiguration
            FXMLLoader loader = new FXMLLoader(getClass().getResource("insertCoins.fxml"));
            VBox vbox = loader.load();
            bdGameScene.getScene().setRoot(vbox); // Set the new root to the insert coins scene
        } else {
            // Default behavior: ensure the center remains the scroll pane
            if (bdGameScene.getCenter() != scrollPane) {
                bdGameScene.setCenter(scrollPane);
            }
        }
    }

    // Handles the "Restart" button click, resets game configuration, and navigates to the inserting options scene
    @FXML
    void onRestartClicked(ActionEvent event) throws IOException {
        // Reset player scores to zero
        playerOneScore = 0;
        playerTwoScore = 0;

        // Reset GameConfig properties to their initial state
        GameConfig.totalCoins = 0; // Reset total coins
        GameConfig.coins = new int[GameConfig.totalCoins]; // Clear the coins array
        GameConfig.player1Name = ""; // Clear Player 1's name
        GameConfig.player2Name = ""; // Clear Player 2's name
        GameConfig.player1Turn = false; // Reset Player 1's turn flag
        GameConfig.player2Turn = false; // Reset Player 2's turn flag
        GameConfig.fromRange = 0; // Reset the coin range lower bound
        GameConfig.toRange = 0; // Reset the coin range upper bound

        // Load the "insertingOptions.fxml" scene for new game configuration
        FXMLLoader loader = new FXMLLoader(getClass().getResource("insertingOptions.fxml"));
        BorderPane bdPane = loader.load();

        // Replace the current scene's root with the inserting options scene
        bdGameScene.getScene().setRoot(bdPane);
    }
}
