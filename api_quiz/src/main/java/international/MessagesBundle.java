package international;

import java.util.ListResourceBundle;

public class MessagesBundle extends ListResourceBundle {

    private static final Object[][] CONTENTS = new Object[][]{
            {"question.template", "How many {0} ships entered port {1} in {2}?"},
            {"answer.correct", "Correct! The answer is {0}."},
            {"answer.incorrect", "Incorrect. Expected {0}, but got {1}."},
            {"ship.singular", "ship"},
            {"ship.plural", "ships"},
            // Polish entries for direct bundle access if locale is pl
            {"pl.question.template", "Ile statków typu {0} wpłynęło do portu {1} w roku {2}?"},
            {"pl.answer.correct", "Poprawnie! Odpowiedź to {0}."},
            {"pl.answer.incorrect", "Niepoprawnie. Oczekiwano {0}, podano {1}."},
    };

    @Override
    protected Object[][] getContents() {
        return CONTENTS;
    }
}
