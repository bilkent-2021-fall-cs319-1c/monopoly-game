package monopoly.lobby;

import lombok.Getter;
import lombok.Setter;

/**
 * User who is the owner of a Lobby
 * 
 * @author Javid Baghirov
 * @version Nov 23, 2020
 */

@Getter @Setter
public class LobbyOwner extends User	
{
        /**
         * Creates a lobby owner
         * 
         * @param name specified user name
         * @param connectionID the connection address of the user
         * @lobby lobby the lobby that the user owns
         */
	public LobbyOwner( String name, String connectionID, Lobby lobby)
	{
		super( name, connectionID);
		setIsHost( true);
		setLobby( lobby);
	}
	
	/**
         * Sets the lobby name
         * 
         * @param name the new name to be set to
         */
	public void setLobbyName( String name)
	{
	    getLobby().setName( name);
	}
	
	/**
         * Sets the status of the lobby
         * 
         * @param isPublic status of being either public or private
         * @param password the pass code to be set
         */
	public void setLobbyStatus( boolean isPublic, String password)
	{
	    getLobby().setPublic( isPublic);
	    getLobby().setPassword( password);
	}
	
	/**
         * Bans the specified user
         * 
         * @param player the user to be banned
         */
	public void ban( User player)
	{
	    getLobby().ban( player);
	}
}
