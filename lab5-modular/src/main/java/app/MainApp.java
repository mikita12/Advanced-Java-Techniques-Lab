package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(
                FXMLLoader.load(getClass().getResource("/view.fxml"))
        );

        stage.setScene(scene);
        stage.setTitle("Lab05 SPI");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}