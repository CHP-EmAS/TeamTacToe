package javaBeans;

import Interfaces.DatabaseConnection;

public class UserBean
{
    private String nickname;
    private String password;

    private int score;

    private String alert;
    private boolean loggedIn;

    public UserBean()
    {
        nickname = "";
        password = "";

        score = 0;
        alert = "";
        loggedIn = false;
    }

    public void validateLogin() {

        loggedIn = DatabaseConnection.validateUser(nickname,password);

        if(DatabaseConnection.isReady()) {
            if (loggedIn) {
                alert = "Erfolgreich eingeloggt!";
                System.out.println("UserBean: User " + nickname + " logged in successfully!");
            } else alert = "Benutzername oder Passwort falsch!";
        } else {
            System.out.println("mhh " + alert);
            alert = "Login momentan nicht moeglich! :(";
            System.out.println("mhh2 " + alert);
            System.out.println("UserBean: Failed to validate User <" + nickname + ">! ERROR: No connection to Database!");
        }
    }
    public void register() {

        loggedIn = DatabaseConnection.insertNewUser(nickname,password);

        if(DatabaseConnection.isReady()) {
            if (loggedIn) {
                alert = "Registreirung erfolgreich!";
                System.out.println("UserBean: User " + nickname + " was registered successfully!");
            }
            else alert = "Benutzername bereits vergeben.";
        }
        else {
            System.out.println("UserBean: Failed to validate User <" + nickname + ">! ERROR: No connection to Database!");
            alert = "Registrierung momentan nicht möglich! :(";
        }
    }

    public void addScore(int n) { this.score = n; }
    public void setNickname(String name) { this.nickname = name; }
    public void setPassword(String password) { this.password = password; }
    public void setAlert(String alert){this.alert = alert;}

    public boolean getLoggedIn() { return loggedIn; }

    public String getAlert() {
        System.out.println("Info: " + alert);
        String temp = alert;
        alert = "";
        return temp;
    }
    public String getNickname() { System.out.println("lolololol"); return nickname;}
    public int getScore() { return score; }
}
