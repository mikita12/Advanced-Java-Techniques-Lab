package data;

import java.util.List;

public class FileData {

    private final List<PersonRecord> records;
    private final List<String> previewLines;

    public FileData(List<PersonRecord> records, List<String> previewLines) {
        this.records = records;
        this.previewLines = previewLines;
    }

    public List<PersonRecord> getRecords() {
        return records;
    }

    public List<String> getPreviewLines() {
        return previewLines;
    }
}
