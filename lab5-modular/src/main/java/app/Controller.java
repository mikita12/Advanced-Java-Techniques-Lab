package app;

import ex.api.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class Controller {

    @FXML private ComboBox<String> algorithmBox;
    @FXML private TextArea output;
    @FXML private TableView<String[]> table;
    @FXML private TextField kField;

    private List<AnalysisService> services;

    @FXML
    public void initialize() {
        services = PluginLoader.load();

        // algorytmy do ComboBox
        for (AnalysisService s : services) {
            algorithmBox.getItems().add(s.getName());
        }

        // tabela edytowalna
        table.setEditable(true);

        int columns = 2;

        for (int i = 0; i < columns; i++) {
            final int colIndex = i;

            TableColumn<String[], String> col = new TableColumn<>("C" + i);

            col.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue()[colIndex])
            );

            col.setCellFactory(TextFieldTableCell.forTableColumn());

            col.setOnEditCommit(e -> {
                e.getRowValue()[colIndex] = e.getNewValue();
            });

            table.getColumns().add(col);
        }

        // przykładowe dane
        table.getItems().add(new String[]{"1","2"});
        table.getItems().add(new String[]{"2","3"});
        table.getItems().add(new String[]{"10","11"});

        // domyślne K
        kField.setText("2");
    }

    //RUN
    @FXML
    public void runAnalysis() {
        try {
            // brak wyboru algorytmu
            if (algorithmBox.getSelectionModel().getSelectedIndex() == -1) {
                output.setText("Wybierz algorytm!");
                return;
            }

            // parsowanie K
            int k;
            try {
                k = Integer.parseInt(kField.getText());
                if (k <= 0) throw new Exception();
            } catch (Exception e) {
                output.setText("Niepoprawna wartość K!");
                return;
            }

            // sprawdzenie K vs liczba punktów
            if (k > table.getItems().size()) {
                output.setText("K nie może być większe niż liczba punktów!");
                return;
            }

            AnalysisService s =
                    services.get(algorithmBox.getSelectionModel().getSelectedIndex());

            //przekazanie K do algorytmu
            s.setOptions(new String[]{String.valueOf(k)});

            // pobieranie danych z tabeli
            String[][] data = new String[table.getItems().size()][];

            for (int i = 0; i < table.getItems().size(); i++) {
                data[i] = table.getItems().get(i);
            }

            DataSet ds = new DataSet();
            ds.setData(data);

            s.submit(ds);
            DataSet result = s.retrieve(true);

            if (result == null) {
                output.setText("Brak wyniku");
                return;
            }

            // ładny output
            StringBuilder sb = new StringBuilder();
            String[][] res = result.getData();

            for (int i = 0; i < res.length; i++) {
                sb.append("Punkt ").append(i)
                        .append(" → klaster ")
                        .append(res[i][0])
                        .append("\n");
            }

            output.setText(sb.toString());

        } catch (Exception e) {
            output.setText("Error: " + e.getMessage());
        }
    }

    //DODAWANIE WIERSZA
    @FXML
    public void addRow() {
        table.getItems().add(new String[]{"0","0"});
    }
}