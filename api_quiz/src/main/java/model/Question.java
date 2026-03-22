package model;

public class Question {
    private final String questionText;
    private final int correctAnswer;
    private final String shipType;
    private final String port;
    private final int year;

    public Question(String questionText, int correctAnswer, String shipType, String port, int year) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.shipType = shipType;
        this.port = port;
        this.year = year;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public String getShipType() {
        return shipType;
    }

    public String getPort() {
        return port;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionText='" + questionText + '\'' +
                ", correctAnswer=" + correctAnswer +
                ", shipType='" + shipType + '\'' +
                ", port='" + port + '\'' +
                ", year=" + year +
                '}';
    }
}
