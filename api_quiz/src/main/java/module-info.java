module api_quiz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;

    // Jackson modules (automatic module names provided by jackson artifacts)
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;

    // open ui package for FXMLLoader reflective access
    opens ui to javafx.fxml;

    // other packages can be exported if needed by reflection; keep them open if you use reflection
    opens international;
    opens model;

    exports ui;
}
