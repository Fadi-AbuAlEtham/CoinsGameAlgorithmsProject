module com.coinsgamealgorithms {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.coinsgamealgorithms to javafx.fxml;
    exports com.coinsgamealgorithms;
}