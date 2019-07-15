package javaBeans;

import Interfaces.DatabaseConnection;

public class UserBean
{
    private String nickname_key;

    private String alert;
    private boolean loggedIn;

    public UserBean()
    {
        nickname_key = "";

        alert = "";
        loggedIn = false;
    }

    public void validateLogin(String password) {

        loggedIn = DatabaseConnection.validateUser(nickname_key,password);

        if(DatabaseConnection.isReady()) {
            if (loggedIn) {
                alert = "Erfolgreich eingeloggt!";
                System.out.println("UserBean: User " + nickname_key + " logged in successfully!");
            } else alert = "Benutzername oder Passwort falsch!";
        } else {
            alert = "Login momentan nicht moeglich! :(";
            System.out.println("UserBean: Failed to validate User <" + nickname_key + ">! ERROR: No connection to Database!");
        }
    }
    public void register(String password) {

        loggedIn = DatabaseConnection.insertNewUser(nickname_key,password);

        if(DatabaseConnection.isReady()) {
            if (loggedIn) {
                alert = "Registreirung erfolgreich!";
                System.out.println("UserBean: User " + nickname_key + " was registered successfully!");
            }
            else alert = "Benutzername bereits vergeben.";
        }
        else {
            System.out.println("UserBean: Failed to validate User <" + nickname_key + ">! ERROR: No connection to Database!");
            alert = "Registrierung momentan nicht möglich! :(";
        }
    }

    public boolean addPoints(int points)
    {
        if(!loggedIn) return false;

        Boolean success = DatabaseConnection.addPointsToUser(nickname_key, points);

        if(success) System.out.println("UserBean: " + points + " Points was added to User " + nickname_key + "!");
        else System.out.println("UserBean: ERROR! Cannot add " + points + " Points to registered User " + nickname_key + "!");

        return success;
    }
    public void setNickname(String name) { this.nickname_key = name; }
    public void setAlert(String alert){this.alert = alert;}

    public boolean getLoggedIn() { return loggedIn; }

    public String getAlert() {
        String temp = alert;
        alert = "";
        return temp;
    }
    public String getNickname() { return this.nickname_key;}

    public int getScore()
    {
        return DatabaseConnection.getPlayerInfo(nickname_key).getInt("score");
    }
}
