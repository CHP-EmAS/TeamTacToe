package Interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class DatabaseConnection
{
    private static Connection conn = null;
    private static boolean connectionReady;

    private static boolean openConnection(String databaseName,String user, String passwd) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/"+databaseName, user, passwd);
            connectionReady = true;
            return true;
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        connectionReady = false;
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

    public static boolean isReady() {return connectionReady; }

    public static boolean validateUser(String nickname, String password) {
        if(!connectionReady)
        {
            if(!openConnection("TeamTacToe","tomcat","tomcat"))
            {
                System.out.println("DatabaseConnection: Can't execute Query! ERROR: No Connection.");
                return false;
            }
        }

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
        if(!connectionReady)
        {
            if(!openConnection("TeamTacToe","tomcat","tomcat"))
            {
                System.out.println("DatabaseConnection: Can't execute Query! ERROR: No Connection.");
                return false;
            }
        }

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
        if(!connectionReady)
        {
            if(!openConnection("TeamTacToe","tomcat","tomcat"))
            {
                System.out.println("DatabaseConnection: Can't execute Query! ERROR: No Connection.");
                return false;
            }
        }

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT nickname FROM user WHERE nickname=?;");
            stmt.setString(1, nickname);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {e.printStackTrace();}

        return false;
    }

    public static JSONObject getBestPlayers(int limit) {
        JSONObject userList = new JSONObject();

        if(!connectionReady)
        {
            if(!openConnection("TeamTacToe","tomcat","tomcat"))
            {
                System.out.println("DatabaseConnection: Can't execute Query! ERROR: No Connection.");
                return userList;
            }
        }

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT nickname,score FROM user ORDER BY score DESC LIMIT ?");
            stmt.setInt(1, limit);

            ResultSet rs = stmt.executeQuery();

            Integer place = 1;
            int lastScore = -1;

            JSONArray jArray = new JSONArray();

            while(rs.next())
            {
                JSONObject user = new JSONObject();

                if(rs.getInt("score") == lastScore) place--;

                lastScore = rs.getInt("score");

                user.put("placment",place);
                user.put("nickname",rs.getString("nickname"));
                user.put("score",rs.getInt("score"));

                jArray.put(user);

                place++;
            }

            userList.put("list", jArray);

        } catch (SQLException e) {e.printStackTrace();}

        return userList;
    }
}
