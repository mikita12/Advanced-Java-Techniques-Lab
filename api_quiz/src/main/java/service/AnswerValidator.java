package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AnswerValidator {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Extracts the integer "value" for a given port from the API JSON response.
     * Strategy:
     * - Traverse tree and collect object nodes that contain a "value" field.
     * - If only one candidate exists, return it.
     * - If multiple exist and a port is provided, try to find a field in the same object that matches the port name (case-insensitive).
     */
    public Integer extractValueFromJson(String json, String port) throws IOException {
        if (json == null || json.isBlank()) return null;
        JsonNode root = mapper.readTree(json);

        List<JsonNode> parentsWithValue = new ArrayList<>();
        collectObjectsWithValue(root, parentsWithValue);
        if (parentsWithValue.isEmpty()) return null;

        if (parentsWithValue.size() == 1) {
            JsonNode v = parentsWithValue.get(0).get("value");
            if (v == null) return null;
            if (v.isNumber()) return v.asInt();
            try { return Integer.parseInt(v.asText()); } catch (NumberFormatException e) { return null; }
        }

        // Multiple candidates - try to match port
        for (JsonNode parent : parentsWithValue) {
            for (String candidateField : new String[]{"port","name","nazwa","nazwaPortu","nazwa_portu","Nazwa","TERYT_NAME"}) {
                JsonNode fieldNode = parent.get(candidateField);
                if (fieldNode != null && fieldNode.isTextual()) {
                    if (fieldNode.asText().equalsIgnoreCase(port)) {
                        JsonNode v = parent.get("value");
                        if (v == null) continue;
                        if (v.isNumber()) return v.asInt();
                        try { return Integer.parseInt(v.asText()); } catch (NumberFormatException e) { return null; }
                    }
                }
            }
        }

        // Fallback: first numeric value
        for (JsonNode parent : parentsWithValue) {
            JsonNode v = parent.get("value");
            if (v == null) continue;
            if (v.isNumber()) return v.asInt();
            try { return Integer.parseInt(v.asText()); } catch (NumberFormatException ignored) {}
        }

        return null;
    }

    private void collectObjectsWithValue(JsonNode node, List<JsonNode> out) {
        if (node.isObject()) {
            if (node.has("value")) out.add(node);
            node.fieldNames().forEachRemaining(field -> {
                JsonNode child = node.get(field);
                collectObjectsWithValue(child, out);
            });
        } else if (node.isArray()) {
            for (JsonNode item : node) collectObjectsWithValue(item, out);
        }
    }

    /**
     * Validate user's numeric answer against API JSON for a port, returning localized feedback message.
     */
    public String validate(int userAnswer, String json, String port, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("international.messages", locale);
        try {
            Integer correct = extractValueFromJson(json, port);
            if (correct == null) {
                return "Could not determine correct answer from API";
            }
            if (userAnswer == correct) {
                return MessageFormat.format(bundle.getString("answer.correct"), Integer.toString(correct));
            } else {
                return MessageFormat.format(bundle.getString("answer.incorrect"), Integer.toString(correct), Integer.toString(userAnswer));
            }
        } catch (IOException e) {
            return "Error parsing API response: " + e.getMessage();
        }
    }
}
