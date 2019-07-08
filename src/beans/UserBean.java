package Beans;

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

    public void validateLogin()
    {
        isValid = false;

        DatabaseConnection db = new DatabaseConnection("TeamTacToe","tomcat","tomcat");

        if(db.isReady()) {
            try {
                PreparedStatement stmt = db.getPrepareStatement("SELECT score FROM user WHERE nickname=? AND passwd=PASSWORD(?);");
                stmt.setString(1, nickname);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    isValid = true;
                    this.score = rs.getInt("score");

                    // Mehr als ein Datensatz gefunden. Sollte nicht vorkommen, da nickname PK ist
                    if(rs.next()){isValid = false;}
                } else isValid = false; //Kein Datensatz gefunden
            } catch (SQLException e) {e.printStackTrace();}

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

    public void addScore(int n) {
        if(!isValid || n == 0){
            System.out.println("UserBean: Cannot add score to User! ERROR: User not valid or n=0");
            return;
        }

        DatabaseConnection db = new DatabaseConnection("TeamTacToe","tomcat","tomcat");

        if(db.isReady()) {
            try {
                //Score nochmals aus Datenbank holen, falls dieser sich im laufe der Sitzung geändert hat!
                PreparedStatement stmtSelect = db.getPrepareStatement("SELECT score FROM user WHERE nickname=?");
                stmtSelect.setString(1, nickname);
                ResultSet rsSelect = stmtSelect.executeQuery();

                if (rsSelect.next()) this.score = rsSelect.getInt("score");
                else{
                    System.out.println("UserBean: Can't add score to User <" + nickname + ">! ERROR: 404");
                    return;
                }

                //Score um n erhöhen
                score += n;

                //Neuen Score in Datenbank schreiben
                PreparedStatement stmtUpdate = db.getPrepareStatement("UPDATE user SET score=? WHERE nickname=? AND passwd=?");
                stmtUpdate.setInt(1, score);
                stmtUpdate.setString(2, nickname);
                stmtUpdate.setString(3, password);
                int changes = stmtUpdate.executeUpdate();

                if(changes <= 0) System.out.println("UserBean: Error while add score to User <" + nickname + ">! ERROR: Nothing changed");

            } catch (SQLException e) {e.printStackTrace();}

            db.closeConnection();
        }
        else System.out.println("UserBean: Failed to connect to Database!");
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

    public String getNickname()
    {
        return nickname;
    }

    public int getScore()
    {
        return score;
    }
}
