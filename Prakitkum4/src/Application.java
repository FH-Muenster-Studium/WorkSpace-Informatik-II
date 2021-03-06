
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Application {

    private static VocableManager vocables = new DatabaseManager();

    private static final File vocablesFile = new File("/Users/fabianterhorst/Desktop/vocable.txt");

    private enum MenuItem {
        MENU_SHOW(1, "Ausgabe"), MENU_SEARCH(2, "Suchen"),
        MENU_ADD(3, "Einfügen"), MENU_DELETE(4, "Löschen"),
        MENU_TEST(5, "Abfrage"), MENU_MANAGER(6, "Vokabelverwaltung wechseln");

        private int index;
        private String title;

        MenuItem(int index, String title) {
            this.index = index;
            this.title = title;
        }

        @Nullable
        static MenuItem fromIndex(int index) {
            for (MenuItem menuItem : values()) {
                if (menuItem.index == index) {
                    return menuItem;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return "-- " + index + ") " + title + " --";
        }
    }

    /**
     * Starts the vocable application
     *
     * @param args command line args
     */
    public static void main(String[] args) {
        Scanner scanner;
        String text;
        while (true) {
            printMenu();
            scanner = new Scanner(System.in);
            if (scanner.hasNextInt()) {
                MenuItem menuItem = MenuItem.fromIndex(scanner.nextInt());
                if (menuItem != null) {
                    switch (menuItem) {
                        case MENU_SHOW:
                            List<Vocable> vocableList = vocables.getAllVocables();
                            for (Vocable vocable : vocableList) {
                                System.out.println(vocable);
                                System.out.println("--------------------");
                            }
                            break;
                        case MENU_SEARCH:
                            System.out.println("Bitte geben sie den deutschen Vokabelnamen ein");
                            text = new Scanner(System.in).nextLine();
                            Vocable searchedVocable = vocables.findVocable(text, Vocable.Language.GERMAN);
                            if (searchedVocable != null) {
                                System.out.println(searchedVocable);
                                System.out.println("--------------------");
                            } else {
                                System.out.println("Vokabel konnte nicht gefunden werden");
                            }
                            break;
                        case MENU_ADD:
                            System.out.println("Bitte geben sie die zu hinzuzufügende Vokabel in folgenden Format ein");
                            System.out.println("{Englisch;Deutsch}");
                            Vocable vocable = readNewVocable();
                            if (vocable != null) {
                                System.out.println(vocable);
                                System.out.println("wird nun hinzugefügt...");
                                boolean saved = vocables.save(vocable);
                                System.out.println(saved
                                        ? "Vokabel erfolgreich hinzugefügt"
                                        : "Vokabel konnte nicht hinzugefügt werden");
                            } else {
                                System.out.println("Eingegebene Vokabel konnte nicht erkannt werden");
                            }
                            break;
                        case MENU_DELETE:
                            System.out.println("Bitte geben sie die zu löschende Vokabel in folgenden Format ein");
                            System.out.println("{de/en;text}");
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
                                Vocable.Language language = Vocable.Language.fromText(languageText);
                                if (language == null) {
                                    System.out.println("Ungültige Sprache: " + languageText);
                                    break;
                                }
                                Vocable vocableToDelete = vocables.findVocable(text, language);
                                if (vocableToDelete == null) {
                                    System.out.println("Vokabel konnte nicht gefunden werden");
                                    break;
                                }
                                if (vocables.delete(vocableToDelete)) {
                                    System.out.println("Vokabel erfolgreich gelöscht");
                                } else {
                                    System.out.println("Vokabel konnte nicht gefunden oder gelöscht werden");
                                }
                            }
                            break;
                        case MENU_TEST:
                            System.out.println("Bitte geben sie die deutsche Übersetzung zur folgender Vokabel an: ");
                            Vocable currentVocable = vocables.getRandomVocable();
                            if (currentVocable != null) {
                                System.out.println("Deutsch: " + currentVocable.getData().getGerman());
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
                        case MENU_MANAGER:
                            System.out.println("Vokabelmanager wird gewechselt");
                            System.out.println("Ihr Vokabelmanager war: " + vocables);
                            switchVocableManager();
                            System.out.println("Ihr Vokabelmanager ist nun: " + vocables);
                            break;
                    }
                } else {
                    System.out.println("Unbekannter Menü-Index");
                }
            } else {
                System.out.println("Bitte geben Sie eine gültige Zahl ein");
            }
        }
    }

    @Nullable
    private static Vocable readNewVocable() {
        String text = new Scanner(System.in).nextLine();
        return Vocable.fromString(text);
    }

    private static void printMenu() {
        System.out.println("-- Menü --");
        for (MenuItem menuItem : MenuItem.values()) {
            System.out.println(menuItem);
        }
    }

    private static void switchVocableManager() {
        if (vocables instanceof DatabaseManager) {
            try {
                ((Closeable) vocables).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            vocables = new LinkedListManager(vocablesFile);
        } else if (vocables instanceof LinkedListManager) {
            vocables = new DatabaseManager();
        } else {
            throw new IllegalArgumentException("Unknown VocableManager " + vocables.getClass().getSimpleName());
        }
    }
}
