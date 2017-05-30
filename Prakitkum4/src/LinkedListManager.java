import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class LinkedListManager implements VocableManager {

    private Vocable start;

    private File file;

    /**
     * Initializes the linked vocable list
     */
    LinkedListManager(File file) {
        start = null;
        this.file = file;
        try {
            loadFile(file);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void loadFile(File file) throws IOException {
        FileUtils.createFileIfNotExists(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Vocable vocable;
            while ((line = reader.readLine()) != null) {
                vocable = Vocable.fromString(line);
                if (vocable == null) continue;
                add(vocable);
            }
        }
    }

    public void saveToFile(File file) throws IOException {
        FileUtils.createFileIfNotExists(file);
        try (Writer writer = new BufferedWriter(new FileWriter(file))) {
            Vocable currentVocable = start;
            StringBuilder stringBuffer = new StringBuilder();
            while (currentVocable != null) {
                stringBuffer.append(currentVocable.toString());
                stringBuffer.append(System.lineSeparator());
                currentVocable = currentVocable.getNext();
            }
            writer.append(stringBuffer.toString());
        }
    }

    /**
     * Insert a given vocable object to the vocable linked list
     *
     * @param vocable Vocable object to insert
     */
    @Override
    public boolean save(Vocable vocable) {
        boolean addResult = add(vocable);
        if (addResult) {
            try {
                saveToFile(file);
            } catch (IOException io) {
                io.printStackTrace();
                delete(vocable);
                return false;
            }
        }
        return addResult;
    }

    private boolean add(Vocable vocable) {
        Vocable currentVocable = this.start;
        if (currentVocable == null) {
            start = vocable;
            return true;
        }
        while (currentVocable != null) {
            if (currentVocable.getNext() == null) {
                currentVocable.setNext(vocable);
                vocable.setPrev(currentVocable);
                return true;
            } else {
                currentVocable = currentVocable.getNext();
            }
        }
        return false;
    }

    /**
     * Deletes a given vocable object from the linked vocable list
     *
     * @param vocable vocable object to remove
     * @return true if vocable was deleted successfully else false
     */
    @Override
    public boolean delete(Vocable vocable) {
        Vocable currentVocable = this.start;
        while (currentVocable != null) {
            if (currentVocable.equals(vocable)) {
                Vocable nextVocable = currentVocable.getNext();
                Vocable preview = currentVocable.getPrev();
                if (nextVocable != null) {
                    if (preview != null) {
                        preview.setNext(nextVocable);
                        nextVocable.setPrev(preview);
                    } else {
                        this.start = nextVocable;
                    }
                } else {
                    if (preview != null) {
                        preview.setNext(null);
                    }
                }
                return true;
            }
            currentVocable = currentVocable.getNext();
        }
        return false;
    }

    /**
     * Search for a vocable from the given text and language
     *
     * @param text     Text to find
     * @param language language from the text to find
     * @return the vocable to find
     */
    @Nullable
    @Override
    public Vocable findVocable(String text, Vocable.Language language) {
        Vocable currentVocable = this.start;
        while (currentVocable != null) {
            Vocable.Data data = currentVocable.getData();
            final String currentText;
            if (language == Vocable.Language.ENGLISH) {
                currentText = data.getEnglish();
            } else if (language == Vocable.Language.GERMAN) {
                currentText = data.getGerman();
            } else {
                currentText = null;
            }
            if (currentText != null && currentText.equals(text)) {
                return currentVocable;
            }
            currentVocable = currentVocable.getNext();
        }
        return null;
    }

    @Override
    public Vocable getRandomVocable() {
        ArrayList<Vocable> vocables = getAllVocables();
        int index = ThreadLocalRandom.current().nextInt(0, vocables.size());
        return vocables.get(index);
    }

    @Override
    public ArrayList<Vocable> getAllVocables() {
        ArrayList<Vocable> vocables = new ArrayList<>();
        Vocable currentVocable = start;
        while (currentVocable != null) {
            vocables.add(currentVocable);
            currentVocable = currentVocable.getNext();
        }
        return vocables;
    }

    @Override
    public String toString() {
        return "Verkettete Liste Vokabelmanager";
    }
}
