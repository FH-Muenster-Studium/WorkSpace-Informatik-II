import java.sql.*;
import java.util.ArrayList;

/**
 * Created by fabianterhorst on 26.05.17.
 */
public class DatabaseManager implements VocableManager {

    private static final String DATABASE_URL = "jdbc:hsqldb:hsql://locahost/dbname";
    private static final String DATABASE_USER_NAME = "user";
    private static final String DATABASE_USER_PASSWORD = "password";

    private Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER_NAME, DATABASE_USER_PASSWORD);
        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public boolean save(Vocable vocable) {
        return false;
    }

    @Override
    public boolean delete(Vocable vocable) {
        return false;
    }

    @Override
    public Vocable findVocable(String text, Vocable.Language language) {
        return null;
    }

    @Override
    public Vocable getRandomVocable() {
        return null;
    }

    @Override
    public ArrayList<Vocable> getAllVocables() {
        ArrayList<Vocable> vocables = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM VOCABLES");
            ResultSet resultSet = statement.executeQuery();
            do {
                vocables.add(Vocable.fromResultSet(resultSet));
            } while (resultSet.next());
            resultSet.close();
            statement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return vocables;
    }
}
