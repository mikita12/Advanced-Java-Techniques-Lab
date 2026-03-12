package data;

import java.time.LocalDate;
import java.util.List;

public class PersonRecord {

    private final List<String> names;
    private final String surname;
    private final String maidenSurname;
    private final LocalDate birthDate;

    public PersonRecord(List<String> names, String surname, String maidenSurname, LocalDate birthDate) {
        this.names = names;
        this.surname = surname;
        this.maidenSurname = maidenSurname;
        this.birthDate = birthDate;
    }

    public List<String> getNames() {
        return names;
    }

    public String getSurname() {
        return surname;
    }

    public String getMaidenSurname() {
        return maidenSurname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}
