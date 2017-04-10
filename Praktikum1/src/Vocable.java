import org.jetbrains.annotations.Nullable;

public class Vocable {

    public enum Language {
        ENGLISH, GERMAN
    }

    private Vocable next;

    private Vocable prev;

    private Data data;

    public Vocable() {
        next = null;
        prev = null;
    }

    public void setNext(Vocable next) {
        this.next = next;
    }

    public void setPrev(Vocable prev) {
        this.prev = prev;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Nullable
    public Vocable getNext() {
        return next;
    }

    @Nullable
    public Vocable getPrev() {
        return prev;
    }

    public Data getData() {
        return data;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vocable) {
            Vocable vocable = (Vocable) obj;
            return vocable.data.equals(this.data);
        }
        return false;
    }

    public static class Data {

        private String german;

        private String english;

        public Data() {
            english = null;
            german = null;
        }

        public void setEnglish(String english) {
            this.english = english;
        }

        public String getEnglish() {
            return english;
        }

        public void setGerman(String german) {
            this.german = german;
        }

        public String getGerman() {
            return german;
        }

        @Override
        public String toString() {
            return "{german = " + german + "},\n" +
                    "{english = " + english + "}";
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Data) {
                Data data = (Data) obj;
                return data.german.equals(german) && data.english.equals(english);
            }
            return false;
        }
    }
}
