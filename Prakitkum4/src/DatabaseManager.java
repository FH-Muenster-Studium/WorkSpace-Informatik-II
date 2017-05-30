import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager implements VocableManager, Closeable {

    private static final String DATABASE_URL = "jdbc:hsqldb:hsql://localhost/vokabeltrainer";
    private static final String DATABASE_USER_NAME = "usr";
    private static final String DATABASE_USER_PASSWORD = "ooz1ooHi";

    private Connection connection;

    DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER_NAME, DATABASE_USER_PASSWORD);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public boolean save(Vocable vocable) {
        String sql = "INSERT INTO vokabeln (en, de) VALUES (?, ?)";
        return executeSql(sql, vocable);
    }

    @Override
    public boolean delete(Vocable vocable) {
        String sql = "DELETE FROM vokabeln WHERE en = ? AND de = ?";
        return executeSql(sql, vocable);
    }

    private boolean executeSql(String sql, Vocable vocable) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, vocable.getData().getEnglish());
            statement.setString(2, vocable.getData().getGerman());
            return !statement.execute();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }

    @Nullable
    @Override
    public Vocable findVocable(String text, Vocable.Language language) {
        final String sql;
        if (language == Vocable.Language.GERMAN) {
            sql = "SELECT * FROM vokabeln WHERE de = ?";
        } else if (language == Vocable.Language.ENGLISH) {
            sql = "SELECT * FROM vokabeln WHERE en = ?";
        } else {
            throw new UnsupportedOperationException("Unknown Language " + language.toString());
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, text);
            ResultSet resultSet = statement.executeQuery();
            Vocable vocable = resultSet.next() ? Vocable.fromResultSet(resultSet) : null;
            resultSet.close();
            return vocable;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    @Override
    public Vocable getRandomVocable() {
        String sql = "SELECT * FROM vokabeln ORDER BY RAND() LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            Vocable vocable = resultSet.next() ? Vocable.fromResultSet(resultSet) : null;
            resultSet.close();
            return vocable;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Vocable> getAllVocables() {
        ArrayList<Vocable> vocables = new ArrayList<>();
        String sql = "SELECT * FROM vokabeln";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                vocables.add(Vocable.fromResultSet(resultSet));
            }
            resultSet.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return vocables;
    }

    @Override
    public void close() throws IOException {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Datenbank Vokabelmanager";
    }
}
