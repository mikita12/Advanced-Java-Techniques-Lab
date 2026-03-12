package app;

import data.CacheManager;
import data.CsvLoader;
import data.FileData;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import processing.DataProcessor;
import processing.ProcessingResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class MainController {

    @FXML
    private ListView<Path> fileList;

    @FXML
    private TextArea previewArea;

    @FXML
    private Label statsLabel;

    @FXML
    private Label cacheLabel;

    private final CacheManager cacheManager = new CacheManager();
    private final CsvLoader loader = new CsvLoader();
    private final DataProcessor processor = new DataProcessor();

    @FXML
    public void initialize() {

        fileList.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldFile, newFile) -> {

                    if (newFile != null) {
                        loadFile(newFile);
                    }
                });
    }

    private void loadFile(Path path) {

        try {

            FileData data = cacheManager.get(path);

            if (data == null) {

                data = loader.load(path);
                cacheManager.put(path, data);

                cacheLabel.setText("Loaded from disk");

            } else {

                cacheLabel.setText("Loaded from cache");

            }

            showPreview(data);

            ProcessingResult result = processor.process(data);

            statsLabel.setText(
                    "Records: " + result.getRecordCount()
                            + " | Most common name: " + result.getMostCommonName()
                            + " | Most common surname: " + result.getMostCommonSurname()
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPreview(FileData data) {

        previewArea.clear();

        for (String line : data.getPreviewLines()) {
            previewArea.appendText(line + "\n");
        }
    }

    private void loadFilesFromFolder(Path folder) {

        try (Stream<Path> paths = Files.list(folder)) {

            List<Path> csvFiles = paths
                    .filter(p -> p.toString().endsWith(".csv"))
                    .toList();

            fileList.getItems().setAll(csvFiles);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void chooseFolder() {

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select folder with CSV files");

        File folder = chooser.showDialog(fileList.getScene().getWindow());

        if (folder != null) {
            loadFilesFromFolder(folder.toPath());
        }
    }
}