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
	 * @param name         the specified user name
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

	public void setIsHost(boolean isHost) {
		this.isHost = isHost;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
		if (lobby != null)
			lobby.checkGameStart();
	}
}
