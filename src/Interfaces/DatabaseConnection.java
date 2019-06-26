package Interfaces;

import java.sql.*;

public class DatabaseConnection
{
    private Connection conn = null;
    private Boolean connectionReady;

    public DatabaseConnection(String databaseName,String user, String passwd)
    {
        if(openConnection(databaseName,user, passwd)) connectionReady = true;
        else connectionReady = false;
    }

    private Boolean openConnection(String databaseName,String user, String passwd) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/"+databaseName, user, passwd);
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
