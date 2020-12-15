package monopoly.lobby;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import monopoly.Identifiable;
import monopoly.MonopolyException;
import monopoly.gameplay.GamePlayer;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.important.packet_data.UserPacketData;

/**
 * A General User who can create, join or leave a lobby
 * 
 * @author Javid Baghirov
 * @version Nov 29, 2020
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements Identifiable {
	@EqualsAndHashCode.Include
	private int id;
	private String username;

	private Lobby lobby;
	private boolean ready;
	private boolean micOpen;
	private boolean camOpen;

//	Uncomment if required (TA suggestion)
//	private LobbyOwner lobbyOwnerInstance;
	private GamePlayer gamePlayerInstance;

	/**
	 * Copy constructor for the user class
	 * 
	 * @param user the object to copied from
	 */
	public User(User user) {
		id = user.id;
		username = user.username;

		lobby = user.lobby;
		micOpen = user.micOpen;
		camOpen = user.camOpen;
		ready = user.ready;
	}

	/**
	 * Creates a user
	 * 
	 * @param name         the specified user name
	 * @param connectionID the connection address of the user
	 */
	public User(String name, int connectionID) {
		id = connectionID;
		username = name;

		lobby = null;
		ready = false;
		micOpen = false;
		camOpen = false;
	}

	/**
	 * Changes the state of being ready
	 * 
	 * @param ready the new state to be set to
	 * @throws MonopolyException when the user is not in a lobby
	 */
	public void setReady(boolean ready) throws MonopolyException {
		if (lobby == null) {
			throw new MonopolyException(PacketType.ERR_NOT_IN_LOBYY);
		}

		this.ready = ready;
		lobby.userReadyChange(this, ready);
	}

	/**
	 * Join a specified lobby
	 * 
	 * @param lobby    the lobby to be joined
	 * @param password the specified pass code
	 * 
	 * @throws MonopolyException when user is already in the specified lobby, the
	 *                           lobby do not exist, the user is banned, the lobby
	 *                           is full or the passwords do not match
	 */
	public void joinLobby(Lobby lobby, String password) throws MonopolyException {
		if (this.lobby != null) {
			throw new MonopolyException(PacketType.ERR_ALREADY_IN_LOBBY);
		}
		if (lobby == null) {
			throw new MonopolyException(PacketType.ERR_UNKNOWN);
		}

		lobby.userJoin(this, password);

		this.lobby = lobby;
		ready = false;
		micOpen = false;
		camOpen = false;
	}

	/**
	 * Leaves the lobby
	 * 
	 * @throws MonopolyException when the lobby does not exist
	 */
	public void leaveLobby() throws MonopolyException {
		if (lobby == null) {
			throw new MonopolyException(PacketType.ERR_NOT_IN_LOBYY);
		}

		lobby.userLeft(this);
		lobby = null;
	}

	public void setGamePlayerInstance(GamePlayer gamePlayerInstance) {
		this.gamePlayerInstance = gamePlayerInstance;
	}

	public GamePlayer asPlayer() {
		return gamePlayerInstance;
	}

	/**
	 * Returns the object as a packet type
	 * 
	 * @return PlayerPacketData the player data to be returned
	 */
	public UserPacketData getAsPacket() {
		return new UserPacketData(id, username, this instanceof LobbyOwner, ready);
	}
}
