package processing;

public class ProcessingResult {

    private final int recordCount;
    private final String mostCommonName;
    private final String mostCommonSurname;

    public ProcessingResult(int recordCount, String mostCommonName, String mostCommonSurname) {
        this.recordCount = recordCount;
        this.mostCommonName = mostCommonName;
        this.mostCommonSurname = mostCommonSurname;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public String getMostCommonName() {
        return mostCommonName;
    }

    public String getMostCommonSurname() {
        return mostCommonSurname;
    }
}
