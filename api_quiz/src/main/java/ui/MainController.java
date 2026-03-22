package ui;

import api.TranstatService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Question;
import service.AnswerValidator;
import service.QuestionGenerator;

import java.time.Year;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class MainController {

    @FXML
    private Label questionLabel;

    @FXML
    private ComboBox<String> shipTypeCombo;

    @FXML
    private ComboBox<String> portCombo;

    @FXML
    private TextField answerField;

    @FXML
    private Label resultLabel;

    // language selector from FXML
    @FXML
    private ComboBox<String> languageCombo;

    // Backend services
    private TranstatService transtatService;
    private QuestionGenerator questionGenerator;
    private AnswerValidator answerValidator;
    private Locale locale;

    // Map display label -> api value for current locale (LinkedHashMap preserves insertion)
    private Map<String,String> shipLabelToApi = new LinkedHashMap<>();

    // currently displayed question
    private Question currentQuestion;

    // metoda wywoływana po kliknięciu przycisku
    @FXML
    public void handleCheck() {
        String displayShip = shipTypeCombo.getValue();
        String shipApi = displayShip == null ? null : shipLabelToApi.get(displayShip);
        String port = portCombo.getValue();
        String answer = answerField.getText();

        if (shipApi == null || port == null) {
            resultLabel.setText(locale.getLanguage().equals("pl") ? "Wybierz typ statku i port" : "Please select ship type and port");
            return;
        }

        if (answer == null || answer.isBlank()) {
            resultLabel.setText(locale.getLanguage().equals("pl") ? "Wprowadź odpowiedź" : "Please enter an answer");
            return;
        }

        int userAnswer;
        try {
            userAnswer = Integer.parseInt(answer.trim());
        } catch (NumberFormatException e) {
            resultLabel.setText(locale.getLanguage().equals("pl") ? "Odpowiedz musi być liczbą" : "Answer must be a number");
            return;
        }

        resultLabel.setText(locale.getLanguage().equals("pl") ? "Sprawdzanie..." : "Checking...");

        final String stApi = shipApi;
        final String pr = port;
        final int year = (currentQuestion != null) ? currentQuestion.getYear() : Year.now().getValue();

        new Thread(() -> {
            try {
                String json = transtatService.fetchShips(stApi, year);
                String feedback = answerValidator.validate(userAnswer, json, pr, locale);
                Platform.runLater(() -> resultLabel.setText(feedback));
            } catch (Exception e) {
                String msg = locale.getLanguage().equals("pl") ? "Błąd komunikacji z API: " : "API communication error: ";
                String full = msg + e.getMessage();
                Platform.runLater(() -> resultLabel.setText(full));
            }
        }).start();
    }

    // metoda wywoływana przy starcie UI
    @FXML
    public void initialize() {
        // prepare services and locale
        this.locale = Locale.getDefault();
        if (!"pl".equals(locale.getLanguage()) && !"en".equals(locale.getLanguage())) {
            this.locale = Locale.ENGLISH;
        }
        this.transtatService = new TranstatService();
        this.questionGenerator = new QuestionGenerator(transtatService);
        this.answerValidator = new AnswerValidator();

        // populate language combo
        if (languageCombo != null) {
            languageCombo.getItems().addAll("Polski", "English");
            if ("pl".equals(this.locale.getLanguage())) {
                languageCombo.getSelectionModel().select("Polski");
            } else {
                languageCombo.getSelectionModel().select("English");
            }
            languageCombo.setPromptText(locale.getLanguage().equals("pl") ? "Język" : "Language");
            languageCombo.valueProperty().addListener((obs, oldV, newV) -> {
                if (newV == null) return;
                if (newV.equals("Polski")) changeLocale(Locale.forLanguageTag("pl"));
                else changeLocale(Locale.ENGLISH);
            });
        }

        // populate ports (these are proper names and not translated)
        portCombo.getItems().addAll(
                "Gdańsk",
                "Gdynia",
                "Szczecin"
        );

        if (!portCombo.getItems().isEmpty()) portCombo.getSelectionModel().selectFirst();

        // populate ship types according to locale
        refreshShipTypesForLocale();

        // regenerate question when selection changes
        shipTypeCombo.valueProperty().addListener((obs, oldV, newV) -> generateQuestionAsync());
        portCombo.valueProperty().addListener((obs, oldV, newV) -> generateQuestionAsync());

        // initial prompt
        questionLabel.setText(locale.getLanguage().equals("pl") ? "Wybierz parametry i odpowiedz na pytanie" : "Choose parameters and answer the question");

        // initial question generation
        generateQuestionAsync();
    }

    private void refreshShipTypesForLocale() {
        // keep currently selected API value to try to reselect the same logical type after refresh
        String currentlySelectedApi = null;
        String currentDisplay = shipTypeCombo.getValue();
        if (currentDisplay != null) currentlySelectedApi = shipLabelToApi.get(currentDisplay);

        shipLabelToApi.clear();
        shipTypeCombo.getItems().clear();

        // Define mapping of api values (expected by API) to display labels per locale
        // API expects ship types likely in Polish; keep api values in Polish
        if ("pl".equals(locale.getLanguage())) {
            shipLabelToApi.put("Pasażerski", "Pasażerski");
            shipLabelToApi.put("Towarowy", "Towarowy");
            shipLabelToApi.put("Tankowiec", "Tankowiec");
        } else {
            // For English locale, display translated labels, but API values remain Polish
            shipLabelToApi.put("Passenger", "Pasażerski");
            shipLabelToApi.put("Cargo", "Towarowy");
            shipLabelToApi.put("Tanker", "Tankowiec");
        }

        // Populate display labels in ComboBox (keys of map)
        shipTypeCombo.getItems().addAll(shipLabelToApi.keySet());

        // try to restore selection by api value
        if (currentlySelectedApi != null) {
            // find key (display) that maps to same api value
            for (Map.Entry<String,String> e : shipLabelToApi.entrySet()) {
                if (e.getValue().equals(currentlySelectedApi)) {
                    shipTypeCombo.getSelectionModel().select(e.getKey());
                    break;
                }
            }
        }

        // if nothing selected, select first
        if (shipTypeCombo.getValue() == null && !shipTypeCombo.getItems().isEmpty()) {
            shipTypeCombo.getSelectionModel().selectFirst();
        }
    }

    private void changeLocale(Locale newLocale) {
        this.locale = newLocale;
        if (languageCombo != null) {
            languageCombo.setPromptText(locale.getLanguage().equals("pl") ? "Język" : "Language");
            // update the selected text of language combo to reflect language names
            if ("pl".equals(locale.getLanguage())) languageCombo.getSelectionModel().select("Polski");
            else languageCombo.getSelectionModel().select("English");
        }
        answerField.setPromptText(locale.getLanguage().equals("pl") ? "Wprowadź odpowiedź" : "Enter answer");
        resultLabel.setText("");
        // refresh ship type labels for new locale
        refreshShipTypesForLocale();
        // regenerate question in new locale
        generateQuestionAsync();
    }

    private void generateQuestionAsync() {
        String displayShip = shipTypeCombo.getValue();
        String shipApi = displayShip == null ? null : shipLabelToApi.get(displayShip);
        String port = portCombo.getValue();
        int year = Year.now().getValue();

        if (shipApi == null || port == null) {
            return;
        }

        questionLabel.setText(locale.getLanguage().equals("pl") ? "Generowanie pytania..." : "Generating question...");
        resultLabel.setText("");

        final String stApi = shipApi;
        final String displayShipLocal = displayShip;
        final String pr = port;
        final int yr = year;

        new Thread(() -> {
            try {
                // pass both api value and display label so question generator can show localized label
                Question q = questionGenerator.generate(stApi, displayShipLocal, pr, yr, locale);
                this.currentQuestion = q;
                Platform.runLater(() -> questionLabel.setText(q.getQuestionText()));
            } catch (Exception e) {
                String msg = locale.getLanguage().equals("pl") ? "Nie można wygenerować pytania: " : "Could not generate question: ";
                Platform.runLater(() -> questionLabel.setText(msg + e.getMessage()));
            }
        }).start();
    }
}

