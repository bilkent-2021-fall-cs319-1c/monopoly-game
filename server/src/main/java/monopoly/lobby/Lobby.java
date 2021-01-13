package monopoly.lobby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import monopoly.GameStartListener;
import monopoly.Identifiable;
import monopoly.MonopolyException;
import monopoly.gameplay.Game;
import monopoly.network.GameServer;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.important.packet_data.UserListPacketData;
import monopoly.network.packet.important.packet_data.lobby.LobbyPacketData;
import monopoly.network.packet.realtime.RealTimeNetworkPacket;

/**
 * Lobby functionality
 * 
 * @author Javid Baghirov
 * @version Nov 29, 2020
 */
public class Lobby implements Identifiable {
	public static final int MIN_LOBBY_LIMIT = 2;
	public static final int MAX_LOBBY_LIMIT = 6;

	private static int lastLobbyId = 0;

	@Getter
	private int id;
	@Setter(AccessLevel.PACKAGE)
	private String name;
	private int playerLimit;
	@Setter(AccessLevel.PACKAGE)
	private String password;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private boolean isPublic;

	/**
	 * Will be notified when the game starts. Supports only one listener. Setting
	 * new one will override the previous one
	 */
	@Getter
	@Setter
	private GameStartListener gameStartListener;

	@Getter
	private Game game;

	@Getter
	private List<User> users;
	private List<User> bannedUsers;
	private LobbyOwner owner;

	/**
	 * Creates a lobby
	 * 
	 * @param title    specified title
	 * @param limit    limit on the number of maximum players
	 * @param isPublic status of being either public or private
	 * @param password specified pass code
	 * 
	 * @throws MonopolyException when the specified limit is out of bounds of
	 *                           minimum or maximum limits
	 */
	public Lobby(String title, int limit, boolean isPublic, String password) throws MonopolyException {
		users = Collections.synchronizedList(new ArrayList<User>());
		bannedUsers = Collections.synchronizedList(new ArrayList<User>());
		id = ++lastLobbyId;

		name = title;
		setPlayerLimit(limit);
		this.isPublic = isPublic;
		this.password = password;
		owner = null;
		game = null;
	}

	/**
	 * Sets the player limit to a specified number
	 * 
	 * @param limit the number to be set to
	 * @throws MonopolyException when the specified limit is out of bounds of
	 *                           minimum or maximum limits
	 */
	void setPlayerLimit(int limit) throws MonopolyException {
		if (limit >= MIN_LOBBY_LIMIT && limit <= MAX_LOBBY_LIMIT) {
			playerLimit = limit;
		} else {
			throw new MonopolyException(PacketType.ERR_INVALID_LOBBY_LIMIT);
		}
	}

	/**
	 * A user joins to the current lobby. If it is the first user, becomes the owner
	 * of this lobby
	 * 
	 * @param userJoining      the joining user
	 * @param providedPassword the pass code specified by the user, will not be
	 *                         considered for public lobbies
	 * @throws MonopolyException when either the user is null, the user is banned,
	 *                           the lobby is full, the passwords do not match (if
	 *                           the lobby is private), the user is already in the
	 *                           lobby, or game has started on this lobby
	 */
	void userJoin(User userJoining, String providedPassword) throws MonopolyException {
		if (userJoining == null) {
			throw new MonopolyException();
		}
		if (bannedUsers.contains(userJoining)) {
			throw new MonopolyException(PacketType.ERR_BANNED);
		}
		if (users.size() >= playerLimit) {
			throw new MonopolyException(PacketType.ERR_LOBBY_FULL);
		}
		if (!isPublic && !password.equals(providedPassword)) {
			throw new MonopolyException(PacketType.ERR_WRONG_PASSWORD);
		}
		if (users.contains(userJoining)) {
			throw new MonopolyException(PacketType.ERR_ALREADY_IN_LOBBY);
		}
		if (isInGame()) {
			throw new MonopolyException(PacketType.ERR_LOBBY_IN_GAME);
		}

		if (users.isEmpty()) {
			owner = new LobbyOwner(userJoining);
		}
		users.add(userJoining);

		// Send join success notification to the joining user
		GameServer.getInstance().sendSuccessfulJoinNotification(userJoining);

		// Send join notification to everyone in the lobby
		sendJoinedNotification(userJoining);
	}

	/**
	 * Notifies the user about ready state being changed and checks if the game
	 * should start
	 * 
	 * @param user  the user to be notified
	 * @param ready new state of the user
	 * @throws MonopolyException if game already started in this lobby
	 */
	void userReadyChange(User user, boolean ready) throws MonopolyException {
		if (isInGame()) {
			throw new MonopolyException(PacketType.ERR_LOBBY_IN_GAME);
		}
		sendReadyNotification(user);

		if (ready)
			checkGameStart();
	}

	/**
	 * User is removed and is notified
	 * 
	 * @param user the user to be removed
	 */
	void userLeft(User user) {
		users.remove(user);
		// TODO handle all conditions, such as if the game has already started
		sendLeftNotification(user);
	}

	public int getPlayerCount() {
		return users.size();
	}

	/**
	 * Bans a specified player
	 * 
	 * @param user the user to be banned
	 */
	public void ban(User user) {
		bannedUsers.add(user);
	}

	/**
	 * The game is initiated for the current lobby and the users are notified. This
	 * method runs in a new thread
	 * 
	 * @throws MonopolyException if the game has already started in this lobby
	 */
	private void startGame() throws MonopolyException {
		if (isInGame()) {
			throw new MonopolyException(PacketType.ERR_LOBBY_IN_GAME);
		}

		game = new Game(users);
		if (gameStartListener != null) {
			gameStartListener.gameStarted(this);
		}

		// Notify everyone that the game started
		new Thread(
				() -> users.parallelStream().forEach(user -> GameServer.getInstance().sendGameStartNotification(user)))
						.start();
	}

	/**
	 * @throws MonopolyException if the game has already started in this lobby
	 */
	private void checkGameStart() throws MonopolyException {
		if (getPlayerCount() >= 2) {
			synchronized (users) {
				for (User user : users) {
					if (!user.isReady())
						return;
				}
			}
			startGame();
		}
	}

	public boolean checkLobbyClose() {
		return getPlayerCount() == 0;
	}

	/**
	 * Sends playerJoined info to everyone. This method runs in a new thread
	 * 
	 * @param userJoined the user who joined
	 */
	private void sendJoinedNotification(User userJoined) {
		new Thread(() -> users.parallelStream()
				.forEach(user -> GameServer.getInstance().sendPlayerJoinNotification(userJoined, user))).start();
	}

	/**
	 * Sends playerLeft info to everyone. This method runs in a new thread
	 * 
	 * @param userLeft the user who left
	 */
	private void sendLeftNotification(User userLeft) {
		new Thread(() -> users.parallelStream()
				.forEach(user -> GameServer.getInstance().sendPlayerLeaveNotification(userLeft, user))).start();
	}

	/**
	 * Sends playerReady info to everyone. This method runs in a new thread
	 * 
	 * @param userReady the user who is ready
	 */
	private void sendReadyNotification(User userReady) {
		new Thread(() -> users.parallelStream()
				.forEach(user -> GameServer.getInstance().sendPlayerReadyNotification(userReady, user))).start();
	}

	/**
	 * Sends packet data of a user to everyone except himself. This method runs in a
	 * new thread
	 * 
	 * @param fromUserId the user to sent the data of
	 * @param packet     the data to be sent
	 */
	public void sendRealtimePacket(int fromUserId, RealTimeNetworkPacket packet) {
		new Thread(() -> users.parallelStream().forEach(user -> {
			if (user.getId() != fromUserId) {
				GameServer.getInstance().sendRealTimePacket(packet, user.getId());
			}
		})).start();
	}

	public boolean isInGame() {
		return game != null;
	}

	public LobbyPacketData getAsPacket() {
		return new LobbyPacketData(id, name, password, isPublic, owner.getUsername(), users.size(), playerLimit);
	}

	/**
	 * Creates a player list packet data for the players in the lobby
	 * 
	 * @return the player list data
	 */
	public UserListPacketData getUsersAsPacket() {
		UserListPacketData userList = new UserListPacketData();
		users.forEach(user -> userList.add(user.getAsPacket()));

		return userList;
	}
}
