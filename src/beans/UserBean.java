package beans;

import java.sql.*;
import Interfaces.DatabaseConnection;

public class UserBean
{
    private String nickname;
    private String password;

    private int score;

    private String alert;
    private boolean isValid;

    public UserBean()
    {
        nickname = "";
        password = "";

        //score = 0;

        alert = "";
        isValid = false;
    }

    public void validateLogin() {
        if(DatabaseConnection.isReady()) {
            isValid = DatabaseConnection.validateUser(nickname,password);

            if(isValid) {
                alert = "Erfolgreich eingeloggt!";
                System.out.println("UserBean: User " + nickname + " logged in successfully!");
            }
            else alert = "Benutzername oder Passwort falsch!";
        }
        else {
            alert = "Login momentan nicht moeglich! :(";
            System.out.println("UserBean: Failed to validate User <" + nickname + ">! ERROR: No connection to Database!");
        }
    }
    public void register() {
        if(DatabaseConnection.isReady()) {
            isValid = DatabaseConnection.insertNewUser(nickname,password);

            if (isValid) {
                alert = "Registreirung erfolgreich!";
                System.out.println("UserBean: User " + nickname + " was registered successfully!");
            }
            else alert = "Benutzername bereits vergeben.";
        }
        else {
            alert = "Registrierung momentan nicht moeglich! :(";
            System.out.println("UserBean: Failed to validate User <" + nickname + ">! ERROR: No connection to Database!");
        }
    }

    public void addScore(int n) { this.score = n; }
    public void setNickname(String name) { this.nickname = name; }
    public void setPassword(String password) { this.password = password; }
    public void setAlert(String alert){this.alert = alert;}

    public boolean isValid() { return isValid; }

    public String getAlert() { return alert; }

    public String getNickname() { return nickname; }

    public int getScore() { return score; }
}
