package monopoly.lobby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Lobby Setup
 * 
 * @author Javid Baghirov
 * @version Nov 23, 2020
 */

@Getter
@Setter
public class Lobby {
    private String name;
    private int playerLimit;
    private boolean isPublic;
    private String password;
    private boolean started;
    private List<User> players;
    private List<User> bannedPlayers;
    private LobbyOwner host;
    
    
    /**
     * Creates a lobby 
     * 
     * @param title 	specified title
     * @param limit 	the connection address of the user
     * @param isPublic  status of being either public or private
     * @param password  specified pass code
     */
    public Lobby(String title, int limit, boolean isPublic, String password) {
	players = Collections.synchronizedList(new ArrayList<User>());
	bannedPlayers = Collections.synchronizedList(new ArrayList<User>());
	
	setName(title);
	setPlayerLimit(playerLimit);
	setPublic(isPublic);
	setPassword(password);
	setStarted(false);
	setHost(null);
    }
    
    /**
     * Adds a user to the list
     * 
     * @param player user to be added
     */
    public void addPlayer(User player) {
	if (players.size() < playerLimit) {
	    players.add(player);
	}
    }

    /**
     * Removes a user from the list
     * 
     * @param player user to be removed
     */
    public void removePlayer(User player) {
	players.remove(player);
    }
    
    /**
     * Gets the size of the player list
     * 
     * @return int value to be returned
     */
    public int getPlayerCount() {
	return players.size();
    }
    
    /**
     * Bans a specified player
     * 
     * @param player the user to be banned
     */
    public void ban( User player)
    {
	bannedPlayers.add( player);
	player.leaveLobby();
    }
}
