import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public interface VocableManager {

    boolean save(Vocable vocable);

    boolean delete(Vocable vocable);

    @Nullable
    Vocable findVocable(String text, Vocable.Language language);

    @Nullable
    Vocable getRandomVocable();

    ArrayList<Vocable> getAllVocables();
}
