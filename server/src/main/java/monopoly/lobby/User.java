package monopoly.lobby;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import monopoly.Identifiable;
import monopoly.MonopolyException;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.important.packet_data.PlayerPacketData;

/**
 * A General User
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
//	private GamePlayer gamePlayerInstance;

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
	public User(String name, int connectionId) {
		id = connectionId;
		username = name;

		lobby = null;
		ready = false;
		micOpen = false;
		camOpen = false;
	}

	public void setReady(boolean ready) throws MonopolyException {
		if (lobby == null) {
			throw new MonopolyException(PacketType.ERR_NOT_IN_LOBYY);
		}

		this.ready = ready;
		lobby.userReadyChange(this, ready);
	}

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

	public void leaveLobby() throws MonopolyException {
		if (lobby == null) {
			throw new MonopolyException(PacketType.ERR_NOT_IN_LOBYY);
		}

		lobby.userLeft(this);
		lobby = null;
	}

	public PlayerPacketData getAsPacket() {
		return new PlayerPacketData(id, username, this instanceof LobbyOwner);
	}
}
