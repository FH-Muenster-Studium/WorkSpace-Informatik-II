
public class Vocables {

    private Vocable start;

    /**
     * Initializes the linked vocable list
     */
    public Vocables() {
        start = null;
    }

    /**
     * Insert a given vocable object to the vocable linked list
     *
     * @param vocable Vocable object to insert
     */
    public void add(Vocable vocable) {
        Vocable currentVocable = this.start;
        if (currentVocable == null) {
            start = vocable;
            return;
        }
        while (currentVocable != null) {
            if (currentVocable.getNext() == null) {
                currentVocable.setNext(vocable);
                vocable.setPrev(currentVocable);
                return;
            } else {
                currentVocable = currentVocable.getNext();
            }
        }
    }

    /**
     * Removes a given vocable object from the linked vocable list
     *
     * @param vocable vocable object to remove
     * @return true if vocable was deleted successfully else false
     */
    public boolean removeVocable(Vocable vocable) {
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
     * @param text Text to find
     * @param language language from the text to find
     * @return the vocable to find
     */
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

    /**
     * Returns the first object to iterate
     *
     * @return start Vocable object
     */
    public Vocable get() {
        return start;
    }
}
