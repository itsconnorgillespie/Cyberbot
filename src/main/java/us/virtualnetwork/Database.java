package us.virtualnetwork;

import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + Main.properties.getProperty("mysql.host") + ":" + Main.properties.getProperty("mysql.port")
                        + "/" + Main.properties.getProperty("mysql.database") + "?useSSL=false",
                Main.properties.getProperty("mysql.username"), Main.properties.getProperty("mysql.password"));
    }

    public static void disconnect(@NotNull Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void checkSchema() {
        PreparedStatement ps;
        try {
            Connection connection = connect();
            ps = connection.prepareStatement("SHOW TABLES LIKE ?");
            ps.setString(1, Main.properties.getProperty("mysql.table"));
            ResultSet results = ps.executeQuery();

            if (!results.next()) {
                createSchema();
                ps.close();
                disconnect(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createSchema() {
        PreparedStatement ps;
        try {
            Connection connection = connect();
            ps = connection.prepareStatement("CREATE TABLE " + Main.properties.getProperty("mysql.table") + " ("
                    + "Email VARCHAR(320) NOT NULL,"
                    + "UNIQUE KEY Email (Email) USING BTREE,"
                    + "PRIMARY KEY (Email))");
            ps.executeUpdate();
            ps.close();
            disconnect(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getEmails(){
        PreparedStatement ps;
        try {
            Connection connection = connect();
            ps = connection.prepareStatement("SELECT * FROM " + Main.properties.getProperty("mysql.table"));
            ResultSet results = ps.executeQuery();

            List<String> result = new ArrayList<>();

            while(results.next()){
                result.add(results.getString("Email"));
            }

            ps.close();
            disconnect(connection);
            return result;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
