package monopoly.lobby;

import lombok.Getter;
import lombok.Setter;

/**
 * User who is the owner of a Lobby
 * 
 * @author Javid Baghirov
 * @version Nov 23, 2020
 */
@Getter
@Setter
public class LobbyOwner extends User {
	public LobbyOwner(User user) {
		super(user);
	}

	public void setLobbyName(String name) {
		getLobby().setName(name);
	}

	/**
	 * Sets the status of the lobby
	 * 
	 * @param isPublic status of being either public or private
	 * @param password the pass code to be set
	 */
	public void setLobbyStatus(boolean isPublic, String password) {
		getLobby().setPublic(isPublic);
		getLobby().setPassword(password);
	}

	/**
	 * Bans the specified user
	 * 
	 * @param player the user to be banned
	 */
	public void ban(User player) {
		getLobby().ban(player);
	}
}
