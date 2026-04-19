module app {
    requires javafx.controls;
    requires javafx.fxml;

    requires serviceloader.example;

    uses ex.api.AnalysisService;

    opens app to javafx.fxml;
    exports app;
}