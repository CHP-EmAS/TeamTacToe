package Interfaces;

import java.sql.*;

public class DatabaseConnection
{
    private Connection conn = null;
    private Boolean connectionReady = false;

    public DatabaseConnection(String databaseName)
    {
        if(openConnection(databaseName)) connectionReady = true;
    }

    private Boolean openConnection(String databaseName) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection("localhost:3307/"+databaseName, "test", "test");
            return true;
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        return false;
    }

    public Boolean closeConnection()
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

    public Boolean isReady()
    {
        return connectionReady;
    }
}
