import java.io.File;
import java.io.IOException;

/**
 * Created by fabianterhorst on 18.04.17.
 */
public class FileUtils {

    public static void createFileIfNotExists(File file) throws IOException {
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("No permissions for creating files");
            }
        }
    }
}
