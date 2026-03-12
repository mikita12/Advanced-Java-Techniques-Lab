module personparser.app {

    exports app;

    requires javafx.controls;
    requires javafx.fxml;

    requires personparser.data;
    requires personparser.processing;

    opens app to javafx.fxml;

}