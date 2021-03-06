import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Vocable {

    public enum Language {
        ENGLISH("en"), GERMAN("de");

        String text;

        Language(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }

        @Nullable
        static Language fromText(String text) {
            for (Language language : values()) {
                if (language.text.equals(text)) {
                    return language;
                }
            }
            return null;
        }
    }

    private Vocable next;

    private Vocable prev;

    private Data data;

    public Vocable() {
        next = null;
        prev = null;
    }

    @Nullable
    public static Vocable fromString(String text) {
        String[] texts = text.split(";");
        if (texts.length < 2) {
            return null;
        }
        return new Vocable() {{
            setData(new Data() {{
                setEnglish(texts[0]);
                setGerman(texts[1]);
            }});
        }};
    }

    @Nullable
    public static Vocable fromResultSet(ResultSet resultSet) throws SQLException {
        String english = resultSet.getString(Language.ENGLISH.toString());
        if (english == null) {
            return null;
        }
        String german = resultSet.getString(Language.GERMAN.toString());
        if (german == null) {
            return null;
        }
        return new Vocable() {{
            setData(new Data() {{
                setEnglish(english);
                setGerman(german);
            }});
        }};
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

    @Override
    public String toString() {
        return data.toString();
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
            return "Deutsch : " + german + "\n" +
                    "Englisch : " + english;
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
