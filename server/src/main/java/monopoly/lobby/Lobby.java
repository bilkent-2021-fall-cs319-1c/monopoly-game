package monopoly.lobby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import monopoly.GameServer;
import monopoly.Identifiable;
import monopoly.MonopolyException;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.important.packet_data.LobbyPacketData;
import monopoly.network.packet.realtime.RealTimeNetworkPacket;

/**
 * Lobby Setup
 * 
 * @author Javid Baghirov
 * @version Nov 29, 2020
 */
public class Lobby implements Identifiable {
	public static final int MIN_LOBBY_LIMIT = 2;
	public static final int MAX_LOBBY_LIMIT = 6;

	private static int lastUsedId = 0;

	@Getter
	private int id;
	@Setter(AccessLevel.PACKAGE)
	private String name;
	private int playerLimit;
	@Setter(AccessLevel.PACKAGE)
	private String password;
	@Setter(AccessLevel.PACKAGE)
	private boolean isPublic;

	@Getter
	private boolean inGame;

	private List<User> users;
	private List<User> bannedUsers;
	private LobbyOwner owner;

	private GameServer server;

	/**
	 * Creates a lobby
	 * 
	 * @param title    specified title
	 * @param limit    limit on the number of maximum players
	 * @param isPublic status of being either public or private
	 * @param password specified pass code
	 * @throws MonopolyException
	 */
	public Lobby(String title, int limit, boolean isPublic, String password, GameServer server)
			throws MonopolyException {
		this.server = server;

		users = Collections.synchronizedList(new ArrayList<User>());
		bannedUsers = Collections.synchronizedList(new ArrayList<User>());
		id = ++lastUsedId;

		name = title;
		setPlayerLimit(limit);
		this.isPublic = isPublic;
		this.password = password;
		inGame = false;
		owner = null;
	}

	void setPlayerLimit(int limit) throws MonopolyException {
		if (limit >= MIN_LOBBY_LIMIT && limit <= MAX_LOBBY_LIMIT) {
			playerLimit = limit;
		} else {
			throw new MonopolyException(PacketType.ERR_INVALID_LOBBY_LIMIT);
		}
	}

	void userJoin(User userJoining, String providedPassword) throws MonopolyException {
		if (bannedUsers.contains(userJoining)) {
//			throw new MonopolyException(PacketType.ERR_BANNED);
			throw new MonopolyException(PacketType.ERR_UNKNOWN);
		}
		if (users.size() >= playerLimit) {
			throw new MonopolyException(PacketType.ERR_LOBBY_FULL);
		}
		if (!isPublic && !password.equals(providedPassword)) {
//			throw new MonopolyException(PacketType.ERR_WRONG_PASSWORD);
			throw new MonopolyException(PacketType.ERR_UNKNOWN);
		}
		if (userJoining == null || users.contains(userJoining)) {
			throw new MonopolyException(PacketType.ERR_ALREADY_IN_LOBBY);
		}

		if (users.isEmpty()) {
			owner = new LobbyOwner(userJoining);
		}
		users.add(userJoining);

		// TODO New protocol required for this! GET_USERS_IN_LOBBY
		new Thread(() -> {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}

			// Send join notification to everyone in the lobby
			users.parallelStream().forEach(user -> server.sendPlayerJoinNotification(user, userJoining));
		}).start();
		sendJoinedNotification(userJoining);
	}

	void userReadyChange(User user, boolean ready) {
		sendReadyNotification(user);

		if (ready)
			checkGameStart();
	}

	void userLeft(User user) {
		users.remove(user);
		sendLeftNotification(user);
	}

	public int getPlayerCount() {
		return users.size();
	}

	public boolean getPublic() {
		return isPublic;
	}

	/**
	 * Bans a specified player
	 * 
	 * @param user the user to be banned
	 */
	public void ban(User user) {
		bannedUsers.add(user);
	}

	public void startGame() {
		inGame = true;

		new Thread(() -> {
			for (int i = 0; i < users.size(); i++) {
				server.sendGameStartNotification(users.get(i));
			}
		}).start();

		// To be implemented in later versions
	}

	public void checkGameStart() {
		for (User user : users) {
			if (!user.isReady())
				return;
		}
		startGame();
	}

	/**
	 * Sends playerJoined info to everyone except himself
	 * 
	 * @param userJoined the user who joined
	 */
	public void sendJoinedNotification(User userJoined) {
		new Thread(() -> users.parallelStream().forEach(user -> {
			if (!user.equals(userJoined)) {
				server.sendPlayerJoinNotification(userJoined, user);
			}
		})).start();
	}

	/**
	 * Sends playerLeft info to everyone except himself
	 * 
	 * @param userLeft the user who left
	 */
	public void sendLeftNotification(User userLeft) {
		new Thread(() -> users.parallelStream().forEach(user -> server.sendPlayerLeaveNotification(userLeft, user)))
				.start();
	}

	/**
	 * Sends playerReady info to everyone
	 * 
	 * @param userReady the user who is ready
	 */
	public void sendReadyNotification(User userReady) {
		new Thread(() -> users.parallelStream().forEach(user -> server.sendPlayerReadyNotification(userReady, user)))
				.start();
	}

	public void sendRealtimePacket(int fromUserId, RealTimeNetworkPacket packet) {
		new Thread(() -> users.parallelStream().forEach(user -> {
			if (user.getId() != fromUserId) {
				server.sendRealTimePacket(packet, user.getId());
			}
		})).start();
	}

	public LobbyPacketData getAsPacket() {
		return new LobbyPacketData(id, name, password, isPublic, owner.getUsername(), users.size(), playerLimit);
	}
}
