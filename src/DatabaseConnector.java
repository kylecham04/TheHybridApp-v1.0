import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    final String url = "jdbc:mysql://localhost:3306/myfitnesstracker";
    final String username = "root";
    final String dataBasePassword = "Thehybridapp12!";

    public Connection getConnection(){
        Connection con = null;
        try{
            con = DriverManager.getConnection(url, username, dataBasePassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return con;
    }
}
