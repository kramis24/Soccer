package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author *** put your name here ***
 * @version *** put date of completion here ***
 *
 */
public class SoccerDatabase implements SoccerDB {

    //
    private Hashtable<String, SoccerPlayer> database = new Hashtable<String, SoccerPlayer>();

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {

        String key = firstName+" ## "+lastName;

        if (database.get(key) != null) {
            return false;
        }

        if (uniformNumber == 0) {
            uniformNumber++;
        }
        else if (uniformNumber < 0) {
            uniformNumber = -uniformNumber;
        }

        SoccerPlayer newPlayer = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);
        database.put(key, newPlayer);

        return true;
    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {

        String key = firstName+" ## "+lastName;

        if (database.get(key) != null) {
            database.remove(key);
            return true;
        }

        return false;
    }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {

        String key = firstName+" ## "+lastName;

        return database.get(key);
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {

        String key = firstName+" ## "+lastName;

        if (database.get(key) != null) {
            SoccerPlayer p = database.get(key);
            p.bumpGoals();
            return true;
        }

        return false;
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {

        String key = firstName+" ## "+lastName;

        if (database.get(key) != null) {
            SoccerPlayer p = database.get(key);
            p.bumpYellowCards();
            return true;
        }

        return false;
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {

        String key = firstName+" ## "+lastName;

        if (database.get(key) != null) {
            SoccerPlayer p = database.get(key);
            p.bumpRedCards();
            return true;
        }

        return false;
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {
        if (teamName == null) {
            return database.size();
        }

        int count = 0;
        for (SoccerPlayer p : database.values()) {
            if (p.getTeamName().equals(teamName)) {
                count++;
            }
        }

        return count;
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {
        if (idx > numPlayers(teamName)) {
            return null;
        }

        for (SoccerPlayer p : database.values()) {
            if (teamName != null) {
                if (p.getTeamName().equals(teamName) && idx == 0) {
                    return p;
                } else if (p.getTeamName().equals(teamName)) {
                    idx--;
                }
            } else {
                if (idx == 0) {
                    return p;
                } else {
                    idx--;
                }
            }
        }

        return null;
    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @Override
    public boolean readData(File file) {
        Scanner reader = null;

        try {

            reader = new Scanner(file);

            while (reader.hasNext()) {

                String firstName = reader.nextLine();
                String lastName = reader.nextLine();
                int uniform = reader.nextInt();
                int goals = reader.nextInt();
                int yellowCards = reader.nextInt();
                int redCards = reader.nextInt();
                reader.nextLine();
                String teamName = reader.nextLine();

                String key = firstName+" ## "+lastName;

                if (database.get(key) != null) {
                    database.remove(key);
                }

                addPlayer(firstName, lastName, uniform, teamName);

                for (int i = 0; i < goals; i++) {
                    bumpGoals(firstName, lastName);
                }
                for (int i = 0; i < yellowCards; i++) {
                    bumpYellowCards(firstName, lastName);
                }
                for (int i = 0; i < redCards; i++) {
                    bumpRedCards(firstName, lastName);
                }

            }

            reader.close();

        } catch (FileNotFoundException e) {

            e.printStackTrace();
            return false;

        } catch (Exception e) {

            return false;

        }

        return true;
    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    public boolean writeData(File file) {
        PrintWriter writer = null;

        try {

            writer = new PrintWriter(file);

            for (SoccerPlayer p : database.values()) {
                writer.println(logString(p.getFirstName()));
                writer.println(logString(p.getLastName()));
                writer.println(logString(Integer.toString(p.getUniform())));
                writer.println(logString(Integer.toString(p.getGoals())));
                writer.println(logString(Integer.toString(p.getYellowCards())));
                writer.println(logString(Integer.toString(p.getRedCards())));
                writer.println(logString(p.getTeamName()));
            }

            writer.close();

        } catch (FileNotFoundException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
        return new HashSet<String>();
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
