package Interfaces;

import java.sql.*;

public class DatabaseConnection
{
    private static Connection conn = null;
    private static Boolean connectionReady;

    public DatabaseConnection(String databaseName,String user, String passwd)
    {
        connectionReady = openConnection(databaseName,user, passwd);
    }

    private static Boolean openConnection(String databaseName,String user, String passwd) {
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

    private static Boolean closeConnection()
    {
        try
        {
            conn.close();
            return true;
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }

        return false;
    }

    public static ResultSet executeQuery(String query )
    {
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

    public static Boolean isReady()
    {
        return connectionReady;
    }
}
