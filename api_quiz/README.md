API Quiz — desktopowa aplikacja quizowa (backend)
=================================================

Krótki opis
----------
To repozytorium zawiera backendową logikę aplikacji quizowej (Java 17+) dla desktopu, która:
- komunikuje się z REST API TransStat (GUS) przez `TranstatService`,
- generuje pytania (`QuestionGenerator`) i waliduje odpowiedzi (`AnswerValidator`),
- wspiera internacjonalizację (polski/angielski) oraz prostą obsługę form liczebnych w polskim.

Ważne: GUI (FXML + MainController) jest częścią projektu, ale żadne nowe okna/komponenty nie zostały dodane — README koncentruje się na budowie i uruchomieniu.

Wymagania
---------
- Java 17 lub nowsza
- Maven 3.6+
- Dostęp do internetu (do wykonywania zapytań do API TranStat)
- Środowisko graficzne (aplikacja korzysta z JavaFX)

Struktura (najważniejsze pliki)
-------------------------------
- `src/main/java/api/TranstatService.java` — HTTP client do TransStat API
- `src/main/java/service/QuestionGenerator.java` — tworzy pytania (używa serwisu + i18n)
- `src/main/java/service/AnswerValidator.java` — parsuje JSON i waliduje odpowiedź
- `src/main/java/ui/MainController.java` — kontroler JavaFX (łączy UI z backendem, obsługuje zmianę języka)
- `src/main/resources/main-view.fxml` — istniejące FXML (nie modyfikować)
- `src/main/resources/international/*.properties`, `international/MessagesBundle.java` — zasoby i klasa ListResourceBundle

Budowanie projektu
------------------
Projekt używa Mavena. Dodałem konfigurację `maven-shade-plugin`, więc build generuje też „fat” (shaded) JAR zawierający zależności.

1) Zbuduj projekt (pomija testy):

```bash
mvn -DskipTests package
```

2) Po pomyślnym buildzie znajdziesz pliki:

- `target/api_quiz-1.0-SNAPSHOT.jar` (standardowy artifact)
- `target/api_quiz-1.0-SNAPSHOT-shaded.jar` (wykonywalny fat-jar z manifestem wskazującym `ui.MainApp`)

Uruchamianie
------------
Proste uruchomienie fat-JAR:

```bash
java -jar target/api_quiz-1.0-SNAPSHOT-shaded.jar
```

Uwaga: aplikacja używa JavaFX — jeżeli napotkasz komunikat typu "JavaFX runtime components are missing", możesz uruchomić przez plugin maven (bez tworzenia JAR) w trybie developerskim:

```bash
mvn javafx:run
```

Albo jawnie wskazać moduły JavaFX (jeśli używasz JavaFX SDK zamiast zawartych zależności):

```bash
java --module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml -jar target/api_quiz-1.0-SNAPSHOT-shaded.jar
```

(Uwaga: `maven-shade-plugin` tworzy fat-JAR, ale w zależności od platformy i classifierów JavaFX natywne biblioteki mogą wymagać dodatkowego ustawienia `--module-path`.)

Platform-specific notes
-----------------------
- W `pom.xml` zależności JavaFX mają ustawiony `classifier` na `linux`. Jeśli chcesz budować i uruchamiać na Windows lub macOS, zamień classifier na `win` lub `mac` i przebuduj (albo użyj profili Maven zestawionych z platformami).
- Dla produkcyjnej dystrybucji rozważ `jpackage` (tworzy natywne instalatory) lub `jlink` (tworzy lekki runtime z wymaganymi modułami).

Jak działa lokalizacja i typy statków
------------------------------------
- UI ma ComboBox języka (`languageCombo`) — zmiana języka ustawia `Locale` w `MainController` i ponownie generuje pytanie.
- ComboBox typów statków (`shipTypeCombo`) pokazuje etykiety zlokalizowane według wybranego języka (np. "Passenger" / "Pasażerski"), ale wartości wysyłane do API pozostają w formie oczekiwanej przez TransStat (w tym projekcie przyjmujemy, że są to polskie etykiety: "Pasażerski", "Towarowy", "Tankowiec"). Jeśli Twój przypadek użycia wymaga innej wartości API, zmień mapowanie w `MainController.refreshShipTypesForLocale()`.
