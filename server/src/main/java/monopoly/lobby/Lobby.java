package monopoly.lobby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

/**
 * Lobby Setup
 * 
 * @author Javid Baghirov
 * @version Nov 29, 2020
 */

@Getter
@Setter
@EqualsAndHashCode
public class Lobby {
    
    private static long lastUsedId = 0;
    private long id;
    
    private String name;
    private int playerLimit;
    private String password;
    private boolean isPublic;
    
    private boolean inGame;
    
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
		
		id = ++lastUsedId;
		
		setName(title);
		setPlayerLimit(playerLimit);
		setPublic(isPublic);
		setPassword(password);
		setInGame(false);
		setHost(null);
    }
    
    /**
     * Adds a user to the list
     * 
     * @param player user to be added
     */
    public void addPlayer(User player) {
		if (player != null && players.size() < playerLimit) {
		    players.add(player);
		}
    }

    /**
     * Removes a user from the list
     * 
     * @param player user to be removed
     */
    public void removePlayer(User player) {
    	if (player != null) {
    		players.remove(player);
    	}
    }
    
    public int getPlayerCount() {
    	return players.size();
    }
    
    public boolean getPublic() {
    	return isPublic;
    }
    
    /**
     * Bans a specified player
     * 
     * @param player the user to be banned
     */
    public void ban( User player) {
		bannedPlayers.add( player);
		player.leaveLobby();
    }
    
    public void startGame() {
    	setInGame( true);
    	
    	//To be implemented in later versions
    }
    
}
