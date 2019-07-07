package Beans;

import Interfaces.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginRegisterBean
{
    private String method;
    private String nickname;
    private String password;

    private String alert;

    private boolean success;

    public LoginRegisterBean()
    {
        method = "";
        nickname = "";
        password = "";
        alert = "";
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public void setNickname(String name)
    {
        this.nickname = name;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean getSuccess()
    {
        success = false;

        if(method.equals("login"))
        {
            if(DatabaseConnection.isReady())
            {
                ResultSet rs = DatabaseConnection.executeQuery("SELECT COUNT(*) AS amount FROM user WHERE nickname='" + nickname + "' AND passwd=PASSWORD('" + password + "');");

                try {
                    if(rs.next())
                    {
                        if (rs.first()) {
                            success = (rs.getInt("amount") == 1);
                        }
                    }
                }
                catch(SQLException e) { e.printStackTrace(); }
            }

            if(success) alert = "Sie wurden erfolgreich eingeloggt!";
            else alert = "Benutzername oder Passwort falsch!";
        }

        return success;
    }

    public String getAlert()
    {
        return alert;
    }

    public String getNickname(){return nickname;}
}
