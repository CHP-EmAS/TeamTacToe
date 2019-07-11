package Interfaces;

import java.sql.*;
import beans.UserBean;

public class DatabaseConnection
{
    private static Connection conn = null;
    private static boolean connectionReady;

    public DatabaseConnection(String databaseName,String user, String passwd) {
        connectionReady = openConnection(databaseName,user, passwd);
    }

    private static boolean openConnection(String databaseName,String user, String passwd) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/"+databaseName, user, passwd);
            return true;
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void closeConnection() {
        try
        {
            conn.close();
            conn = null;
            connectionReady = false;
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query) {
        if(conn == null)
        {
            System.out.println("DatabaseConnection: Can't execute Query! ERROR: No Connection.");
            return null;
        }

        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            if (stmt != null)
            {
                try{ stmt.close();} catch (SQLException e ) { e.printStackTrace();}
            }
        }

        return null;
    }

    public static boolean isReady() {return connectionReady; }

    public static boolean validateUser(String nickname, String password) {
        if(!connectionReady && conn != null) return false;

        boolean isValid = false;

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT score FROM user WHERE nickname=? AND password=?;");
            stmt.setString(1, nickname);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                isValid = true;
                // Mehr als ein Datensatz gefunden. Sollte nicht vorkommen, da nickname PK ist
                if(rs.next()){isValid = false;}
            } else isValid = false; //Kein Datensatz gefunden
        } catch (SQLException e) {e.printStackTrace();}

        return isValid;
    }

    public static boolean insertNewUser(String nickname, String password) {
        if(!connectionReady && conn != null) return false;

        if(userExists(nickname)) return false;

        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO user (nickname,password,score) VALUES (?,?,0);");
            stmt.setString(1, nickname);
            stmt.setString(2, password);
            int changes = stmt.executeUpdate();

            return (changes>0);
        } catch (SQLException e) {e.printStackTrace();}

        return false;
    }

    public static Boolean userExists(String nickname) {
        if(!connectionReady && conn != null) return false;

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT nickname FROM user WHERE nickname=?;");
            stmt.setString(1, nickname);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {e.printStackTrace();}

        return false;
    }
}
