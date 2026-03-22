package service;

import api.TranstatService;
import model.Question;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class QuestionGenerator {

    private final TranstatService transtatService;

    public QuestionGenerator(TranstatService transtatService) {
        this.transtatService = transtatService;
    }

    /**
     * Generate a Question by fetching data and formatting localized text.
     * shipApiType is the value sent to the API (e.g. Polish term expected by Transtat).
     * displayShipType is the localized label shown to the user in the question.
     */
    public Question generate(String shipApiType, String displayShipType, String port, int year, Locale locale) throws Exception {
        // Fetch raw JSON using API-facing ship type
        String json = transtatService.fetchShips(shipApiType, year);
        // Extract numeric value for the given port
        AnswerValidator validator = new AnswerValidator();
        Integer value = validator.extractValueFromJson(json, port);
        int numeric = value == null ? 0 : value;

        // Build localized question text
        ResourceBundle bundle = ResourceBundle.getBundle("international.messages", locale);

        String template = bundle.getString("question.template");

        // Use the display label directly; templates include surrounding words (e.g. "statków typu {0}" or "{0} ships")
        String shipWord = displayShipType;

        String questionText = MessageFormat.format(template, shipWord, port, Integer.toString(year));

        return new Question(questionText, numeric, shipApiType, port, year);
    }
}
