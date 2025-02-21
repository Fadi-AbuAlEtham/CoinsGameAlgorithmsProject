package com.coinsgamealgorithms;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ComputerGameController {
    @FXML
    private BorderPane bdGameScene;

    @FXML
    private Button btShowAnswer;

    @FXML
    private Button btShowTable;

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
    private ScrollPane scrollPane;

    @FXML
    private TextField txtPlayerOneScore;

    @FXML
    private TextField txtPlayerTwoScore;

    @FXML
    private VBox vbPlayerOneGameCoins;

    @FXML
    private VBox vbPlayerTwoGameCoins;

    private boolean isAnimating;
    Image coinImage;
    private int playerOneScore = 0;
    private int playerTwoScore = 0;
    private boolean isPlayerOneTurn;
    private TextArea txtArea;
    private TableView<ObservableList<Integer>> tbAnswer;
    private int[][] dp; // DP table
    private int sequenceIndex = 0; // Track current position in the sequence
    private List<Integer> optimalSequence; // Stores the sequence of moves
    private int expectedResult;
    private int currentStartIndex, currentEndIndex;
    private boolean continueAnimation;

    public void setPlayerOneTurn(boolean playerOneTurn) {
        isPlayerOneTurn = playerOneTurn;
    }

    @FXML
    void initialize() {
        // Set the labels for Player One and Player Two with their respective names from the GameConfig class
        lblPlayerOneName.setText(GameConfig.player1Name);
        lblPlayerTwoName.setText(GameConfig.player2Name);
    }

    @FXML
    public void initializeGame() {
        // Initialize the starting index of the coins array
        currentStartIndex = 0;

        // Initialize the ending index of the coins array
        currentEndIndex = GameConfig.coins.length - 1;

        // Ensure the scrollPane is set as the center of the game scene if not already set
        if (bdGameScene.getCenter() != scrollPane) {
            bdGameScene.setCenter(scrollPane);
        }

        // Update the player labels with the names from the GameConfig class
        lblPlayerOneName.setText(GameConfig.player1Name);
        lblPlayerTwoName.setText(GameConfig.player2Name);

        // Initialize the coins array used for the game
        GameConfig.initializeCoinsArray();

        // Initialize the display or data related to coins
        initializeCoins();

        // Precompute the DP table and the expected result for the game
        calculateDpTableAndExpectedResult(isPlayerOneTurn);

        // Initialize the game table using the precomputed DP table
        initializeTable();

        // Disable the "Show Answer" button initially
        btShowAnswer.setDisable(true);

        // Disable the "Show Table" button initially
        btShowTable.setDisable(true);
    }

    public void initializeCoins() {
        // Set the labels for Player One and Player Two with their respective names
        lblPlayerOneName.setText(GameConfig.player1Name);
        lblPlayerTwoName.setText(GameConfig.player2Name);

        // Set initial scores for both players using their names
        lblPlayerOneScore.setText(GameConfig.player1Name);
        lblPlayerTwoScore.setText(GameConfig.player2Name);

        // Check the insertion type for coins configuration
        if (GameConfig.insertationType.equalsIgnoreCase("Random")) {
            Random random = new Random();

            // Clear any existing children in the game scene FlowPane
            flowPaneGameScene.getChildren().clear();

            // Load the coin image for use in the game
            coinImage = new Image(TwoPlayersGameController.class.getResource("coin image.png").toExternalForm());

            // Get the maximum value as a string and calculate its length
            String maxValueString = String.valueOf(GameConfig.toRange);
            int maxValueLength = maxValueString.length();

            // Adjust image size based on the length of the maximum value
            double baseSize = 60; // Base size for the coin image
            double scaleFactor = 10; // Scale factor for length
            double adjustedSize = baseSize + (maxValueLength * scaleFactor);

            // Generate random coins within the defined range
            for (int i = 0; i < GameConfig.coins.length; i++) {
                int range = GameConfig.toRange - GameConfig.fromRange + 1; // Calculate the valid range
                GameConfig.coins[i] = random.nextInt(range) + GameConfig.fromRange; // Generate coin value within range

                // Create an ImageView for the coin image with adjusted size
                ImageView imageView = new ImageView(coinImage);
                imageView.setFitWidth(adjustedSize); // Set image width
                imageView.setFitHeight(adjustedSize); // Set image height

                // Create a label for the coin's value and style it
                Label coinValueLabel = new Label(String.valueOf(GameConfig.coins[i]));
                coinValueLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

                // Create a StackPane to combine the image and label
                StackPane coinPane = new StackPane(imageView, coinValueLabel);
                coinPane.setId("coinPane-" + i); // Assign a unique ID for tracking

                // Add the coin pane to the FlowPane
                flowPaneGameScene.getChildren().add(coinPane);
            }
        }
        else if (GameConfig.insertationType.equalsIgnoreCase("Manual")) {
            // Clear the FlowPane for manual coin insertion
            flowPaneGameScene.getChildren().clear();

            // Load the coin image
            coinImage = new Image(TwoPlayersGameController.class.getResource("coin image.png").toExternalForm());

            // Get the maximum value as a string and calculate its length
            String maxValueString = String.valueOf(Arrays.stream(GameConfig.coins).max().getAsInt());
            int maxValueLength = maxValueString.length();

            // Adjust image size based on the length of the maximum value
            double baseSize = 60; // Base size for the coin image
            double scaleFactor = 10; // Scale factor for length
            double adjustedSize = baseSize + (maxValueLength * scaleFactor);

            // Check if the coins array is null or empty and initialize with random values if needed
            if (GameConfig.coins == null || GameConfig.coins.length == 0) {
                Random random = new Random();
                flowPaneGameScene.getChildren().clear();
                for (int i = 0; i < GameConfig.coins.length; i++) {
                    GameConfig.coins[i] = random.nextInt(GameConfig.toRange) + GameConfig.fromRange;

                    // Create and style the coin's display
                    ImageView imageView = new ImageView(coinImage);
                    imageView.setFitWidth(adjustedSize);
                    imageView.setFitHeight(adjustedSize);

                    Label coinValueLabel = new Label(String.valueOf(GameConfig.coins[i]));
                    coinValueLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

                    StackPane coinPane = new StackPane(imageView, coinValueLabel);
                    coinPane.setId("coinPane-" + i);

                    flowPaneGameScene.getChildren().add(coinPane);
                    return; // Return early as coins have been initialized
                }
            } else {
                // If coins array is initialized, display each coin
                for (int i = 0; i < GameConfig.coins.length; i++) {
                    ImageView imageView = new ImageView(coinImage);
                    imageView.setFitWidth(adjustedSize);
                    imageView.setFitHeight(adjustedSize);

                    Label coinValueLabel = new Label(String.valueOf(GameConfig.coins[i]));
                    coinValueLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

                    StackPane coinPane = new StackPane(imageView, coinValueLabel);
                    coinPane.setId("coinPane-" + i);

                    flowPaneGameScene.getChildren().add(coinPane);
                }
            }
        } else if (GameConfig.insertationType.equalsIgnoreCase("File")) {
            // Handle coin initialization from a file
            System.out.println("File Line 415");
            GameConfig.initializeCoinsArray();

            // Clear the FlowPane and load the coin image
            flowPaneGameScene.getChildren().clear();
            coinImage = new Image(TwoPlayersGameController.class.getResource("coin image.png").toExternalForm());

            // Get the maximum value as a string and calculate its length
            String maxValueString = String.valueOf(Arrays.stream(GameConfig.coins).max().getAsInt());
            int maxValueLength = maxValueString.length();

            // Adjust image size based on the length of the maximum value
            double baseSize = 60; // Base size for the coin image
            double scaleFactor = 10; // Scale factor for length
            double adjustedSize = baseSize + (maxValueLength * scaleFactor);

            // Display coins read from the file
            for (int i = 0; i < GameConfig.coins.length; i++) {
                ImageView imageView = new ImageView(coinImage);
                imageView.setFitWidth(adjustedSize);
                imageView.setFitHeight(adjustedSize);

                Label coinValueLabel = new Label(String.valueOf(GameConfig.coins[i]));
                coinValueLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

                StackPane coinPane = new StackPane(imageView, coinValueLabel);
                coinPane.setId("coinPane-" + i);

                flowPaneGameScene.getChildren().add(coinPane);
            }
        }
    }

    @FXML
    void onMainMenuClicked(ActionEvent event) throws IOException {
        // Load the main menu FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));

        // Load the BorderPane for the main menu scene
        BorderPane bdPane = loader.load();

        // Set the current scene's root to the loaded main menu layout
        bdGameScene.getScene().setRoot(bdPane);

        // Reset the game configuration variables to their default values
        GameConfig.totalCoins = 0;               // Reset total coins to 0
        GameConfig.coins = new int[GameConfig.totalCoins]; // Reinitialize the coins array with size 0
        GameConfig.player1Name = "";            // Clear Player 1's name
        GameConfig.player2Name = "";            // Clear Player 2's name
        GameConfig.player1Turn = false;         // Reset Player 1's turn flag
        GameConfig.player2Turn = false;         // Reset Player 2's turn flag
        GameConfig.fromRange = 0;               // Reset the minimum coin range to 0
        GameConfig.toRange = 0;                 // Reset the maximum coin range to 0
    }

    @FXML
    void onResetClicked(ActionEvent event) throws IOException {
        // Reset the scores for both players to 0
        playerOneScore = 0;
        playerTwoScore = 0;

        // Reinitialize the coins for the game
        initializeCoins();

        // Set the current player's turn based on the initial game configuration
        isPlayerOneTurn = GameConfig.player1Turn;
        GameConfig.player1Turn = isPlayerOneTurn;         // Update Player 1's turn status
        GameConfig.player2Turn = !isPlayerOneTurn;        // Update Player 2's turn status

        // Clear any coins displayed for both players
        vbPlayerOneGameCoins.getChildren().clear();
        vbPlayerTwoGameCoins.getChildren().clear();

        // Reset the indices for the coins array
        currentStartIndex = 0;
        currentEndIndex = GameConfig.coins.length - 1;

        // Ensure the scrollPane is set as the center of the game scene if not already set
        if (bdGameScene.getCenter() != scrollPane) {
            bdGameScene.setCenter(scrollPane);
        }

        // Check the insertion type for the coins and handle accordingly
        if (GameConfig.insertationType.equalsIgnoreCase("Manual") ||
                GameConfig.insertationType.equalsIgnoreCase("File") ||
                GameConfig.insertationType.equalsIgnoreCase("Random")) {
            // Load the coin insertion scene if the insertion type requires it
            FXMLLoader loader = new FXMLLoader(getClass().getResource("insertCoins.fxml"));
            VBox vbox = loader.load();

            // Set the current scene's root to the loaded coin insertion layout
            bdGameScene.getScene().setRoot(vbox);
        } else {
            // Ensure the scrollPane remains the center of the game scene if no insertion scene is required
            if (bdGameScene.getCenter() != scrollPane) {
                bdGameScene.setCenter(scrollPane);
            }
        }
    }


    @FXML
    void onRestartClicked(ActionEvent event) throws IOException {
        // Reset the scores for both players to 0
        playerOneScore = 0;
        playerTwoScore = 0;

        // Reset the total number of coins to 0
        GameConfig.totalCoins = 0;

        // Reinitialize the coins array with a size of 0
        GameConfig.coins = new int[GameConfig.totalCoins];

        // Clear the player names
        GameConfig.player1Name = "";
        GameConfig.player2Name = "";

        // Reset the turn flags for both players
        GameConfig.player1Turn = false;
        GameConfig.player2Turn = false;

        // Reset the range for coin values
        GameConfig.fromRange = 0;
        GameConfig.toRange = 0;

        // Reset the indices for the coins array
        currentStartIndex = 0;
        currentEndIndex = GameConfig.coins.length - 1;

        // Load the "insertingOptions.fxml" scene for restarting the game setup
        FXMLLoader loader = new FXMLLoader(getClass().getResource("insertingOptions.fxml"));
        BorderPane bdPane = loader.load();

        // Set the current scene's root to the loaded "insertingOptions" layout
        bdGameScene.getScene().setRoot(bdPane);
    }

    @FXML
    void onStartClicked(ActionEvent event) {
        // Enable the "Show Answer" button
        btShowAnswer.setDisable(false);

        // Enable the "Show Table" button
        btShowTable.setDisable(false);

        // Check if an animation is already in progress
        if (isAnimating) {
            System.out.println("Animation already in progress. Please wait.");
            return; // Exit if animation is currently running
        }

        // Set the animation state to active
        isAnimating = true;

        // Allow the animation to continue for optimal moves
        continueAnimation = true;

        // Start or resume the animation from the current sequence index
        animateOptimalMoves(true);
    }

    @FXML
    void onStepByStepClicked(ActionEvent event) {
        // Enable the "Show Answer" button
        btShowAnswer.setDisable(false);

        // Enable the "Show Table" button
        btShowTable.setDisable(false);

        // Check if an animation is already in progress
        if (isAnimating) {
            System.out.println("Animation already in progress. Please wait.");
            return; // Exit if animation is currently running
        }

        // Set the animation to single-step mode
        continueAnimation = false;

        // Animate one step of the optimal moves
        animateOptimalMoves(false);
    }

    private void animateOptimalMoves(boolean continuous) {
        // Check if all coins in the optimal sequence have been animated
        if (sequenceIndex >= optimalSequence.size()) {
            System.out.println("All coins have been animated.");
            isAnimating = false; // Reset the animating flag
            return; // Exit as there are no more coins to animate
        }

        // Get the index of the coin to be animated
        int pickedIndex = optimalSequence.get(sequenceIndex);

        // Find the coin node in the FlowPane by its ID
        Node coinToAnimate = null;
        for (Node node : flowPaneGameScene.getChildren()) {
            if (node.getId().equals("coinPane-" + pickedIndex)) {
                coinToAnimate = node; // Coin found
                break;
            }
        }

        // If the coin is not found in the FlowPane, log an error and skip to the next coin
        if (coinToAnimate == null) {
            System.err.println("Invalid index: " + pickedIndex + ". Coin not found.");
            sequenceIndex++; // Increment the sequence index
            animateOptimalMoves(continuous); // Skip this coin and continue animating
            return;
        }

        // Determine whose turn it is based on the sequence index and the starting player's turn
        boolean currentTurn = (sequenceIndex % 2 == 0) == isPlayerOneTurn;

        // Animate the movement of the coin to the respective player's area
        animateCoinMove(pickedIndex, currentTurn);

        // Move to the next coin in the sequence
        sequenceIndex++;

        if (continuous) {
            // If continuous mode is enabled, automatically animate the next coin
            animateOptimalMoves(true);
        } else {
            // If single-step mode, reset the animating flag for the next button press
            isAnimating = false;
        }
    }

    @FXML
    void onShowAnswerClicked(ActionEvent event) {
        // Check if the current center of the game scene is not the text area for the answer
        if (bdGameScene.getCenter() != txtArea) {
            // Set the center of the game scene to the text area, displaying the answer
            bdGameScene.setCenter(txtArea);
        }
    }

    @FXML
    void onShowTableClicked(ActionEvent event) {
        // Check if the current center of the game scene is not the table for the answer
        if (bdGameScene.getCenter() != tbAnswer) {
            // Set the center of the game scene to the table, displaying the DP table or relevant data
            bdGameScene.setCenter(tbAnswer);
        }
    }

    // Initializes the DP table in the TableView using the precomputed DP values
    private void initializeTable() {
        // Create a new TableView to display the DP table
        tbAnswer = new TableView<>();
        tbAnswer.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY); // Allow unconstrained resizing of columns
        tbAnswer.getColumns().clear(); // Clear any existing columns

        // Add a column for row numbers
        addRowNumberColumn();

        // Add columns for each coin index
        for (int col = 0; col < GameConfig.coins.length; col++) {
            final int colIndex = col; // Store the column index for use in the lambda
            TableColumn<ObservableList<Integer>, Integer> column = new TableColumn<>((col + 1) + ""); // Column header as index
            column.setPrefWidth(145); // Set preferred width for the column
            column.setCellValueFactory(cellData ->
                    new SimpleObjectProperty<>(cellData.getValue().get(colIndex)) // Retrieve the value for the current column
            );
            tbAnswer.getColumns().add(column); // Add the column to the TableView
        }

        // Populate the TableView with rows containing the precomputed DP values
        for (int i = 0; i < GameConfig.coins.length; i++) {
            ObservableList<Integer> row = FXCollections.observableArrayList(); // Create a new row
            for (int j = 0; j < GameConfig.coins.length; j++) {
                row.add(dp[i][j]); // Add the DP value for the current cell
            }
            tbAnswer.getItems().add(row); // Add the row to the TableView
        }

        // Create a TextArea to display the expected result
        txtArea = new TextArea();
        txtArea.setEditable(false); // Make the TextArea read-only
        txtArea.getStyleClass().clear(); // Clear existing styles
        txtArea.getStyleClass().add("custom-text-area"); // Apply a custom style class

        // Determine the player whose turn it is and set the expected result message
        String player = GameConfig.player1Turn ?
                GameConfig.player1Name + " Player 1" :
                GameConfig.player2Name + " Player 2";
        txtArea.setText("Expected maximum coins for ( " + player + " ): " + expectedResult);
    }

    // Adds a row number column to the TableView
    private void addRowNumberColumn() {
        // Create a new TableColumn to display row numbers
        TableColumn<ObservableList<Integer>, Integer> rowNumberColumn = new TableColumn<>("Row");
        rowNumberColumn.setPrefWidth(100); // Set the preferred width for the column

        // Set the cell value factory to calculate row numbers dynamically
        rowNumberColumn.setCellValueFactory(cellData -> {
            ObservableList<Integer> rowData = cellData.getValue(); // Get the row data
            int rowIndex = tbAnswer.getItems().indexOf(rowData) + 1; // Calculate the row number (starting from 1)
            return new SimpleObjectProperty<>(rowIndex); // Return the row number as an Integer property
        });

        // Apply a custom cell factory to style the row number cells like headers
        rowNumberColumn.setCellFactory(new Callback<TableColumn<ObservableList<Integer>, Integer>, TableCell<ObservableList<Integer>, Integer>>() {
            @Override
            public TableCell<ObservableList<Integer>, Integer> call(TableColumn<ObservableList<Integer>, Integer> column) {
                return new TableCell<ObservableList<Integer>, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null); // Clear the text if the cell is empty
                        } else {
                            setText(item.toString()); // Set the row number as the cell text
                            // Apply custom styles to the cell
                            setStyle("-fx-background-color: #f0f0f0; -fx-font-weight: bold; -fx-alignment: CENTER;");
                        }
                    }
                };
            }
        });

        // Add the row number column to the TableView
        tbAnswer.getColumns().add(rowNumberColumn);
    }

    // Calculates the DP table and determines the expected maximum result for the starting player
    private void calculateDpTableAndExpectedResult(boolean isPlayerOneStarting) {
        int n = GameConfig.coins.length; // Get the number of coins
        if (n == 0) return; // Exit if there are no coins to process

        dp = new int[n][n]; // Initialize the DP table
        optimalSequence = new ArrayList<>(); // Initialize the optimal sequence list

        // initial value: Fill the diagonal of the DP table with the coin values
        for (int i = 0; i < n; i++) {
            dp[i][i] = GameConfig.coins[i]; // Single coin's value is the maximum for that interval
        }

        for (int i = 0; i < n - 1; i++) { // Explicit initial case for Two Coins
            dp[i][i + 1] = Math.max(GameConfig.coins[i], GameConfig.coins[i + 1]);
        }

        // Fill the DP table for intervals of increasing length
        for (int length = 2; length <= n; length++) { // Length of the interval
            for (int i = 0; i <= n - length; i++) { // Start index of the interval

                // Calculate the maximum coins if the player picks the first coin in the interval
                int pickFirst = GameConfig.coins[i] + Math.min(
                        getValue(dp, i + 2, (i + length - 1)),       // Opponent picks i+1
                        getValue(dp, i + 1, (i + length - 1) - 1)   // Opponent picks j
                );

                // Calculate the maximum coins if the player picks the last coin in the interval
                int pickLast = GameConfig.coins[(i + length - 1)] + Math.min(
                        getValue(dp, i, (i + length - 1) - 2),       // Opponent picks j-1
                        getValue(dp, i + 1, (i + length - 1) - 1)   // Opponent picks i
                );

                if (pickLast >= pickFirst) {
                    dp[i][(i + length - 1)] = pickLast;
                } else {
                    dp[i][(i + length - 1)] = pickFirst;
                }
            }
        }

        // Calculate the expected result based on the starting player
        if (isPlayerOneStarting) {
            // If Player 1 starts, the maximum coins they can guarantee is dp[0][n-1]
            expectedResult = dp[0][n - 1];
            System.out.println("Maximum coins Player 1 can guarantee: " + expectedResult);
        } else {
            // If Player 2 starts, calculate total coins and subtract Player 1's guaranteed maximum
            int totalCoins = 0;
            for (int coin : GameConfig.coins) {
                totalCoins += coin;
            }
            expectedResult = totalCoins - dp[0][n - 1]; // Player 2's maximum guarantee
            System.out.println("Maximum coins Player 2 can guarantee: " + expectedResult);
        }

        // Generate the optimal sequence of moves after computing the DP table
        generateOptimalSequence();
    }

    // Generates the optimal sequence of moves based on the precomputed DP table
    private void generateOptimalSequence() {
        int i = 0; // Start index of the coin array
        int j = GameConfig.coins.length - 1; // End index of the coin array
        boolean isCurrentPlayerOne = isPlayerOneTurn; // Start with the correct player's turn

        // Continue until all coins are picked
        while (i <= j) {
            // Calculate the potential value if the first coin is picked
            int pickFirst = GameConfig.coins[i] + Math.min(
                    getValue(dp, i + 2, j),       // Opponent picks the next first coin
                    getValue(dp, i + 1, j - 1)   // Opponent picks the last coin
            );

            // Calculate the potential value if the last coin is picked
            int pickLast = GameConfig.coins[j] + Math.min(
                    getValue(dp, i, j - 2),       // Opponent picks the next last coin
                    getValue(dp, i + 1, j - 1)   // Opponent picks the first coin
            );

            // Choose the optimal move based on the maximum value
            // Decide the optimal move based on the precomputed DP values
            if (dp[i][j] == pickLast || pickLast >= pickFirst) {
                optimalSequence.add(j); // Add the index of the last coin to the sequence
                j--; // Move the end index backward
            } else {
                optimalSequence.add(i); // Add the index of the first coin to the sequence
                i++; // Move the start index forward
            }

            // Alternate the turn between the players
            isCurrentPlayerOne = !isCurrentPlayerOne;
        }
    }

    // Animates the optimal sequence of coin moves step by step
    private void animateSequenceStepByStep(boolean continuous) {
        // Check if the sequence is null or if all coins have already been animated
        if (optimalSequence == null || sequenceIndex >= optimalSequence.size()) {
            System.out.println("All coins have been animated or sequence is not initialized.");
            isAnimating = false; // Reset the animating flag to allow new animations to start
            return; // Exit as there are no more coins to animate
        }

        // Get the index of the next coin to animate from the optimal sequence
        int pickedIndex = optimalSequence.get(sequenceIndex);

        // Determine whose turn it is based on the sequence index
        boolean isPlayerOneTurn = (sequenceIndex % 2 == 0); // Player 1 starts on even turns

        // Animate the movement of the coin to the respective player's area
        animateCoinMove(pickedIndex, isPlayerOneTurn);

        // Increment the sequence index to move to the next coin in the sequence
        sequenceIndex++;

        if (continuous) {
            // If continuous mode is enabled, automatically animate the next step
            animateNextStep();
        } else {
            // If single-step mode, reset the animating flag for the next button press
            isAnimating = false;
        }
    }

    // Helper to handle out-of-bound cases
    private int getValue(int[][] dp, int i, int j) {
        if (i > j || i < 0 || j >= dp.length) return 0;
        return dp[i][j];
    }



    // Animates the next step in the sequence with a delay for continuous animation
    private void animateNextStep() {
        // Check if there are more coins to animate in the sequence
        if (sequenceIndex < optimalSequence.size()) {
            // Create a pause transition to introduce a delay between animations
            PauseTransition pause = new PauseTransition(Duration.seconds(1)); // Set the delay duration

            // Define the action to perform after the delay
            pause.setOnFinished(event -> animateSequenceStepByStep(true)); // Continue animating the sequence in continuous mode

            // Start the pause transition
            pause.play();
        } else {
            // If all coins have been animated, log the completion message
            System.out.println("All coins have been animated.");

            // Reset the animating flag to allow new animations to start
            isAnimating = false;
        }
    }

    // Clones a StackPane by duplicating its children (ImageView and Label nodes)
    private StackPane cloneStackPane(StackPane original) {
        // Create a new StackPane to hold the cloned children
        StackPane clonedPane = new StackPane();

        // Iterate through each child node in the original StackPane
        for (Node node : original.getChildren()) {
            if (node instanceof ImageView) {
                // Clone an ImageView node
                ImageView originalImage = (ImageView) node; // Get the original ImageView
                ImageView clonedImage = new ImageView(originalImage.getImage()); // Create a new ImageView with the same image
                clonedImage.setFitWidth(originalImage.getFitWidth()); // Copy the width
                clonedImage.setFitHeight(originalImage.getFitHeight()); // Copy the height
                clonedPane.getChildren().add(clonedImage); // Add the cloned ImageView to the cloned StackPane
            } else if (node instanceof Label) {
                // Clone a Label node
                Label originalLabel = (Label) node; // Get the original Label
                Label clonedLabel = new Label(originalLabel.getText()); // Create a new Label with the same text
                clonedLabel.setStyle(originalLabel.getStyle()); // Copy the style of the original Label
                clonedPane.getChildren().add(clonedLabel); // Add the cloned Label to the cloned StackPane
            }
        }

        // Return the cloned StackPane with its children
        return clonedPane;
    }

    // Animates the movement of a coin from the FlowPane to the target VBox
    private void animateCoinMove(int pickedIndex, boolean isPlayerOneTurn) {
        Node coinToAnimate = null;

        // Find the coin in the FlowPane using its ID
        for (Node node : flowPaneGameScene.getChildren()) {
            if (node.getId().equals("coinPane-" + pickedIndex)) {
                coinToAnimate = node; // Coin found
                break;
            }
        }

        // Handle the case where the coin is not found
        if (coinToAnimate == null) {
            System.err.println("Invalid index: " + pickedIndex + ". Coin not found.");
            return;
        }

        // Cast the found node to a StackPane
        StackPane originalCoinPane = (StackPane) coinToAnimate;

        // Determine the target VBox based on whose turn it is
        VBox targetVBox = isPlayerOneTurn ? vbPlayerOneGameCoins : vbPlayerTwoGameCoins;

        // Clone the coin for animation
        StackPane animatedCoin = cloneStackPane(originalCoinPane);

        // Add the cloned coin to the scene for animation
        bdGameScene.getChildren().add(animatedCoin);

        // Calculate the start and end positions for the animation
        double startX = animatedCoin.localToScene(animatedCoin.getBoundsInLocal()).getMinX();
        double startY = animatedCoin.localToScene(animatedCoin.getBoundsInLocal()).getMinY();
        double endX = targetVBox.localToScene(targetVBox.getBoundsInLocal()).getMinX();
        double endY = targetVBox.localToScene(targetVBox.getBoundsInLocal()).getMinY();

        // Create a translation animation to move the cloned coin
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), animatedCoin);
        transition.setToX(endX - startX); // Set horizontal movement
        transition.setToY(endY - startY); // Set vertical movement

        // Define what happens when the animation is complete
        transition.setOnFinished(event -> {
            bdGameScene.getChildren().remove(animatedCoin); // Remove the animated coin from the scene
            targetVBox.getChildren().add(originalCoinPane); // Move the original coin to the target VBox
            flowPaneGameScene.getChildren().remove(originalCoinPane); // Remove the original coin from the FlowPane

            // Update the scores based on the coin's value
            int coinValue = Integer.parseInt(((Label) originalCoinPane.getChildren().get(1)).getText());
            updateScores(isPlayerOneTurn, coinValue);

            // Continue with the next animation step if needed
            if (continueAnimation && sequenceIndex < optimalSequence.size()) {
                animateOptimalMoves(true); // Automatically animate the next move
            } else {
                isAnimating = false; // Reset the flag if all animations are complete
            }
        });

        // Start the animation
        transition.play();
    }

    // Updates the scores for the players based on the coin value and current turn
    private void updateScores(boolean isPlayerOneTurn, int coinValue) {
        if (isPlayerOneTurn) {
            // Add the coin value to Player 1's score
            playerOneScore += coinValue;

            // Update the displayed score for Player 1
            txtPlayerOneScore.setText(String.valueOf(playerOneScore));
        } else {
            // Add the coin value to Player 2's score
            playerTwoScore += coinValue;

            // Update the displayed score for Player 2
            txtPlayerTwoScore.setText(String.valueOf(playerTwoScore));
        }
    }
}