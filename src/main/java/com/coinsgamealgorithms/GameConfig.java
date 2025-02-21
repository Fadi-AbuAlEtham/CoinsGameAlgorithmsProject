package com.coinsgamealgorithms;

public class GameConfig {
    // Data field to specify the game mode
    public static String whoCalled = "";
    // Data field to specify the insertion type of coins
    public static String insertationType = "";
    //Data field to specify the total
    public static int totalCoins = 0;
    //Data field to specify player one's name
    public static String player1Name = "";
    //Data field to specify player two's name
    public static String player2Name = "";
    //Data field to initialize the coins array
    public static int[] coins = new int[totalCoins];
    //Data field to specify the initial range for random insertion
    public static int fromRange = 0;
    //Date field to specify the end of the range for random insertion
    public static int toRange = 0;
    //Data field to check if it's the first player turn
    public static boolean player1Turn = false;
    public static boolean player2Turn = false;
    public static boolean isEmpty = true;

    // Method to initialize the coins array if not already initialized
    public static void initializeCoinsArray() {
        if (coins == null || coins.length != totalCoins) {
            coins = new int[totalCoins];
        }
    }
}

