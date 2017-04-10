import org.jetbrains.annotations.Nullable;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Application {

    private static Vocables vocables = new Vocables();

    /**
     * Starts the vocable application
     *
     * @param args command line args
     */
    public static void main(String[] args) {
        vocables.add(new Vocable() {{
            setData(new Data() {{
                setEnglish("Car");
                setGerman("Auto");
            }});
        }});
        vocables.add(new Vocable() {{
            setData(new Data() {{
                setEnglish("Hello");
                setGerman("Hallo");
            }});
        }});
        vocables.add(new Vocable() {{
            setData(new Data() {{
                setEnglish("World");
                setGerman("Welt");
            }});
        }});
        Scanner scanner;
        String text;
        Vocable currentVocable;
        while (true) {
            printMenu();
            scanner = new Scanner(System.in);
            if (scanner.hasNextInt()) {
                label:
                switch (scanner.nextInt()) {
                    case 1:
                        currentVocable = vocables.get();
                        while (currentVocable != null) {
                            System.out.println("Deutsch : " + currentVocable.getData().getGerman());
                            System.out.println("Englisch : " + currentVocable.getData().getEnglish());
                            System.out.println("--------------------");
                            currentVocable = currentVocable.getNext();
                        }
                        break;
                    case 2:
                        System.out.println("Bitte geben sie den deutschen Vokabelnamen ein");
                        text = new Scanner(System.in).nextLine();
                        Vocable searchedVocable = vocables.findVocable(text, Vocable.Language.GERMAN);
                        if (searchedVocable != null) {
                            System.out.println("Englisch : " + searchedVocable.getData().getEnglish());
                            System.out.println("--------------------");
                        } else {
                            System.out.println("Vokabel konnte nicht gefunden werden");
                        }
                        break;
                    case 3:
                        System.out.println("Bitte geben sie die zu hinzuzufügende Vokabel in folgenden Format ein");
                        System.out.println("{Englisch;Deutsch}");
                        Vocable vocable = readNewVocable();
                        if (vocable != null) {
                            vocables.add(vocable);
                            System.out.println("Vokabel erfolgreich hinzugefügt");
                        } else {
                            System.out.println("Eingegebene Vokabel konnte nicht erkannt werden");
                        }
                        break;
                    case 4:
                        System.out.println("Bitte geben sie die zu löschende Vokabel in folgenden Format ein");
                        System.out.println("{german/english;text}");
                        Scanner currentScanner = new Scanner(System.in);
                        if (currentScanner.hasNextLine()) {
                            text = currentScanner.nextLine();
                            String[] texts = text.split(";");
                            if (texts.length != 2) {
                                System.out.println("Falsches Vokabel lösch Format");
                                break;
                            }
                            String languageText = texts[0];
                            text = texts[1];
                            final Vocable.Language language;
                            switch (languageText) {
                                case "german":
                                    language = Vocable.Language.GERMAN;
                                    break;
                                case "english":
                                    language = Vocable.Language.ENGLISH;
                                    break;
                                default:
                                    System.out.println("Ungültige Sprache: " + languageText);
                                    break label;
                            }
                            Vocable vocableToDelete = vocables.findVocable(text, language);
                            if (vocableToDelete == null) {
                                System.out.println("Vokabel konnte nicht gefunden werden");
                                break;
                            }
                            if (vocables.removeVocable(vocableToDelete)) {
                                System.out.println("Vokabel erfolgreich gelöscht");
                            } else {
                                System.out.println("Vokabel konnte nicht gefunden werden");
                            }
                        }
                        break;
                    case 5:
                        System.out.println("Bitte geben sie die deutsche Übersetzung zur folgender Vokabel an: ");
                        int length = 0;
                        currentVocable = vocables.get();
                        while (currentVocable != null) {
                            length++;
                            currentVocable = currentVocable.getNext();
                        }
                        int index = ThreadLocalRandom.current().nextInt(0, length);
                        currentVocable = vocables.get();
                        length = 0;
                        while (currentVocable != null) {
                            if (length == index) {
                                break;
                            }
                            length++;
                            currentVocable = currentVocable.getNext();
                        }
                        if (currentVocable != null) {
                            System.out.println(currentVocable.getData().getGerman());
                            scanner = new Scanner(System.in);
                            if (scanner.hasNextLine()) {
                                String translation = scanner.nextLine();
                                if (translation.equals(currentVocable.getData().getEnglish())) {
                                    System.out.println("Korrekt");
                                } else {
                                    System.out.println("Falsch, Lösung: " + currentVocable.getData().getEnglish());
                                }
                            } else {
                                System.out.println("Ungültige Eingabe");
                            }
                        } else {
                            System.out.println("Es konnten keine Vokabeln gefunden werden");
                        }
                        break;
                }
            } else {
                System.out.println("Bitte geben Sie eine gültige Zahl ein");
            }
        }
    }

    @Nullable
    private static Vocable readNewVocable() {
        String text = new Scanner(System.in).nextLine();
        String[] texts = text.split(";");
        if (texts.length != 2) {
            return null;
        }
        return new Vocable() {{
            setData(new Data() {{
                setEnglish(texts[0]);
                setGerman(texts[1]);
            }});
        }};
    }

    private static void printMenu() {
        System.out.println("-- Menü --");
        System.out.println("-- 1) Ausgabe --");
        System.out.println("-- 2) Suchen --");
        System.out.println("-- 3) Einfügen --");
        System.out.println("-- 4) Löschen --");
        System.out.println("-- 5) Abfrage --");
    }
}
