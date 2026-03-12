package data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CsvLoader {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public FileData load(Path path) throws IOException {

        List<String> lines = Files.readAllLines(path);
        List<PersonRecord> records = new ArrayList<>();
        List<String> preview = new ArrayList<>();

        int previewLimit = Math.min(5, lines.size());

        for (int i = 1; i < lines.size(); i++) {

            String line = lines.get(i);

            if (preview.size() < previewLimit) {
                preview.add(line);
            }

            String[] parts = line.split(",");

            if (parts.length >= 5) {

                List<String> names = List.of(parts[0], parts[1]);
                String surname = parts[2];
                String maidenSurname = parts[3];
                LocalDate birthDate = LocalDate.parse(parts[4], DATE_FORMATTER);

                records.add(
                        new PersonRecord(names, surname, maidenSurname, birthDate)
                );
            }
        }

        return new FileData(records, preview);
    }
}