package Interfaces;

import java.sql.*;

public class DatabaseConnection
{
    private Connection conn = null;
    private Boolean connectionReady;

    public DatabaseConnection(String databaseName,String user, String passwd) {
        connectionReady = openConnection(databaseName,user, passwd);
    }

    private Boolean openConnection(String databaseName,String user, String passwd) {
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

    public void closeConnection() {
        try
        {
            conn.close();
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
    public PreparedStatement getPrepareStatement(String sqlQuery) {
        try
        {
            return conn.prepareStatement(sqlQuery);
        }
        catch(SQLException e){e.printStackTrace();}

        return null;
    }

    public Boolean isReady() {return connectionReady; }
}
