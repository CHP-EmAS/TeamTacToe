package Beans;

import Interfaces.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserBean
{
    private String nickname;
    private String password;

    private String alert;

    private boolean isValid;

    public UserBean()
    {
        nickname = "";
        password = "";
        alert = "";
        isValid = false;
    }

    public void validateLogin()
    {
        isValid = false;

        DatabaseConnection db = new DatabaseConnection("TeamTacToe","tomcat","tomcat");

        if(db.isReady()) {
            ResultSet rs = db.executeQuery("SELECT COUNT(*) AS amount FROM user WHERE nickname='" + nickname + "' AND passwd=PASSWORD('" + password + "');");

            try {
                if (rs.next()) {
                    if (rs.first()) {
                        isValid = (rs.getInt("amount") == 1);
                    }
                }
            } catch (SQLException e) {}

            if(isValid)
            {
                alert = "Erfolgreich eingeloggt!";
                System.out.println("UserBean: User " + nickname + " logged in successfully!");
            }
            else alert = "Benutzername oder Passwort falsch!";

            db.closeConnection();
        }
        else
        {
            alert = "Login momentan nicht möglich! :(";
            System.out.println("UserBean: Failed to connect to Database!");
        }
    }

    public void setNickname(String name)
    {
        this.nickname = name;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean isValid()
    {
        return isValid;
    }

    public String getAlert()
    {
        return alert;
    }

    public String getNickname(){return nickname;}
}
