package monopoly.lobby;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

/**
 * A General User
 * 
 * @author Javid Baghirov
 * @version Nov 29, 2020
 */

@Getter
@Setter
@EqualsAndHashCode
public class User {
    private String username;
    private Lobby lobby;
    private int connectionId;
    private boolean micOpen;
    private boolean camOpen;
    private boolean ready;
    private boolean isHost;
    
    
    /**
     * Creates a user
     * 
     * @param name the specified user name
     * @param connectionID the connection address of the user
     */
    public User(String name, int connectionId) {
		setUsername(name);
		setLobby(null);
		setReady(false);
		setMicOpen(false);
		setCamOpen(false);
		setIsHost(false);
		setConnectionId(connectionId);
    }
    
    /**
     * Creates a lobby 
     * 
     * @param name 	specified title
     * @param limit 	the connection address of the user
     * @param isPublic  status of being either public or private
     * @param password  specified pass code
     * 
     * @return LobbyOwner returns an object of this type
     */
    public LobbyOwner createLobby(String name, int limit, boolean isPublic, String password) {
		lobby = new Lobby(name, limit, isPublic, password);
		lobby.addPlayer(this);
	
		LobbyOwner host = new LobbyOwner(username, connectionId, lobby);
		lobby.setHost(host);
	
		return host;
    }
    
    
    /**
     * Either successfully joins a lobby or fails
     * 
     * @param lobby	specified lobby object to be joined
     * @param password	specified pass code
     * 
     * @return boolean returns true if joining is successful, false if not
     */
    public boolean joinLobby(Lobby lobby, String password) {
		boolean isBanned = false;
	
		for (int i = 0; i < lobby.getBannedPlayers().size(); i++) {
			    if (lobby.getBannedPlayers().get(i).getConnectionId() == this.getConnectionId()) {
				isBanned = true;
				break;
		    }
		}
	
		if ( isBanned || (lobby.getPlayerCount() == lobby.getPlayerLimit()) || (!lobby.getPassword().equals(password)))
		{
		    return false;
		}
	
		lobby.addPlayer(this);
		this.lobby = lobby;
	
		return true;
    }
    
    public void leaveLobby() {
		lobby.removePlayer( this);
		
		setLobby(null);
		setReady(false);
		setMicOpen(false);
		setCamOpen(false);
		setIsHost(false);
    }

    public void setIsHost(boolean isHost) {
    	this.isHost = isHost;
    }
    
}
