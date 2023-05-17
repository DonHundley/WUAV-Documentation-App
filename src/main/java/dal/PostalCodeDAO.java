package dal;

import be.PostalCode;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostalCodeDAO {

    private DatabaseConnector databaseConnector;

    public PostalCodeDAO() {
        databaseConnector = new DatabaseConnector();
    }

    public List<PostalCode> getAllPostalCodes() {
        ArrayList<PostalCode> postalCodes = new ArrayList<>();

        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM postal_code";
            Statement statement = connection.createStatement();

            if(statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();

                while (resultSet.next()) {
                    String zipCode = resultSet.getString("postal_code");
                    String city = resultSet.getString("city");

                    PostalCode postalCode = new PostalCode(zipCode, city);
                    postalCodes.add(postalCode);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return postalCodes;
    }
}
