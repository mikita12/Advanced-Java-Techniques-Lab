package app;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import loader.PluginManager;
import processing.Processor;
import processing.Status;
import processing.StatusListener;

import java.util.List;

public class MainController {

    @FXML
    private TextField taskField;

    @FXML
    private ListView<Processor> processorList;

    @FXML
    private TextArea outputArea;

    private List<Processor> processors;

    private final PluginManager manager = new PluginManager("plugins");

    @FXML
    public void loadProcessors() {
        processors = manager.loadProcessors();
        processorList.getItems().setAll(processors);
    }

    @FXML
    public void executeTask() {
        Processor p = processorList.getSelectionModel().getSelectedItem();
        String task = taskField.getText();

        if (p == null) {
            outputArea.setText("Wybierz processor!");
            return;
        }

        p.submitTask(task, new StatusListener() {
            @Override
            public void statusChanged(Status s) {
                outputArea.setText("Progress: " + s.getProgress());
            }
        });

        outputArea.appendText("\nResult: " + p.getResult());
    }
}