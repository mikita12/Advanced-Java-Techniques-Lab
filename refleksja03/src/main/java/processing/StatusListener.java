package processing;
public interface StatusListener {
    /**
     * Metoda słuchacza
     * @param s - status przetwarzania zadania
     */
    void statusChanged(Status s);
}
