package monopoly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import monopoly.lobby.Lobby;
import monopoly.lobby.LobbyOwner;
import monopoly.lobby.User;

/**
 * Model for server, lobby and user management
 *
 * @author Alper Sarı, Javid Baghirov
 * @version Nov 29, 2020
 */

@Getter
@Setter
public class Model {
	private List<Lobby> lobbies;
	private List<User> users;

	private GameServer server;

	public Model() throws IOException {
		lobbies = Collections.synchronizedList(new ArrayList<Lobby>());
		users = Collections.synchronizedList(new ArrayList<User>());

		server = new GameServer(this);
	}

	/**
	 * Creates a lobby
	 * 
	 * @param name         specified title
	 * @param limit        specified player limit
	 * @param isPublic     status of being either public or private
	 * @param password     specified pass code
	 * @param connectionID the id of the user
	 * 
	 * @return LobbyOwner returns the lobby owner
	 */
	public LobbyOwner createLobby(String name, int limit, boolean isPublic, String password, int connectionID) {
		Lobby lobby = new Lobby(this, name, limit, isPublic, password);

		userLogin("test", connectionID);
		User user = getByID(connectionID);

		// Make the user the owner of the lobby
		LobbyOwner host = new LobbyOwner(user.getUsername(), connectionID, lobby);
		lobby.setHost(host);
		user = host;
		addLobby(user.getLobby());

		// Join the user to the created lobby
		joinLobby(lobby, password, user.getConnectionId());

		return host;

	}

	/**
	 * Either successfully joins a lobby or fails
	 * 
	 * @param lobby        specified lobby object to be joined
	 * @param password     specified pass code
	 * @param connectionID id of the user
	 * 
	 * @return boolean returns true if joining is successful, false if not
	 */
	public boolean joinLobby(Lobby lobby, String password, int connectionID) {
		boolean isBanned = false;

		// Check if the user is banned
		for (int i = 0; i < lobby.getBannedPlayers().size(); i++) {
			if (lobby.getBannedPlayers().get(i).getConnectionId() == connectionID) {
				isBanned = true;
				break;
			}
		}

		if (isBanned || (lobby.getPlayerCount() == lobby.getPlayerLimit()) || (!lobby.getPassword().equals(password))) {
			return false;
		}

		User playerJoined = getByID(connectionID);
		lobby.addPlayer(playerJoined);
		playerJoined.setLobby(lobby);

		// TODO New protocol required for this!
		new Thread(() -> {
			try {
				Thread.sleep( 500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
			// Send join notification to everyone in the lobby
			lobby.getPlayers().parallelStream().forEach(player -> {
				server.sendPlayerJoinNotification(player, playerJoined);
			});
		}).start();

		return true;
	}

	public void leaveLobby(Lobby lobby, int connectionID) {
		User user = getByID(connectionID);
		lobby.removePlayer(user);

		user.setLobby(null);
		user.setReady(false);
		user.setMicOpen(false);
		user.setCamOpen(false);
		user.setIsHost(false);
	}

	/**
	 * Sends playerJoined info to everyone except himself
	 * 
	 * @param playerJoined the user who joined
	 */
	public void sendJoinedNotification(User playerJoined) {
		new Thread(() -> {
			for (int i = 0; i < playerJoined.getLobby().getPlayerCount(); i++) {
				if (!playerJoined.getLobby().getPlayers().get(i).equals(playerJoined)) {
					server.sendPlayerJoinNotification(playerJoined, playerJoined.getLobby().getPlayers().get(i));
					System.out.println("Player: " + playerJoined.getConnectionId() + " to "
							+ playerJoined.getLobby().getPlayers().get(i).getConnectionId());
				}
			}
		}).start();
	}

	/**
	 * Sends playerLeft info to everyone except himself
	 * 
	 * @param playerLeft the user who left
	 */
	public void sendLeftNotification(User playerLeft) {
		new Thread(() -> {
			for (int i = 0; i < playerLeft.getLobby().getPlayerCount(); i++) {
				if (!playerLeft.getLobby().getPlayers().get(i).equals(playerLeft)) {
					server.sendPlayerLeaveNotification(playerLeft, playerLeft.getLobby().getPlayers().get(i));
				}
			}
		}).start();
	}
	
	/**
	 * Sends playerReady info to everyone
	 * 
	 * @param playerReady the user who is ready
	 */
	public void sendReadyNotification(User playerReady) {
		new Thread(() -> {
			for (int i = 0; i < playerReady.getLobby().getPlayerCount(); i++) {
				server.sendPlayerReadyNotification(playerReady, playerReady.getLobby().getPlayers().get(i));
			}
		}).start();
	}

	/**
	 * Sends game start notification to everyone
	 * 
	 * @param lobby the lobby to start the game from
	 */
	public void sendGameStartNotification(Lobby lobby) {
		new Thread(() -> {
			for (int i = 0; i < lobby.getPlayerCount(); i++) {
				server.sendGameStartNotification(lobby.getPlayers().get(i));
			}
		}).start();
	}

	public void addLobby(Lobby lobby) {
		if (lobby != null) {
			lobbies.add(lobby);
		}
	}

	public void closeLobby(Lobby lobby) {
		if (lobby != null) {
			for (int i = 0; i < lobbies.size(); i++) {
				if (lobbies.get(i).equals(lobby)) {
					lobbies.remove(i);
					break;
				}
			}
		}
	}

	/**
	 * A user is added to the list
	 * 
	 * @param username the specified name
	 * @param id       the connection id of the user
	 */
	public void userLogin(String username, int id) {
		boolean exists = false;
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getUsername().equals(username) && users.get(i).getConnectionId() == id) {
				exists = true;
				break;
			}
		}

		if (!exists) {
			users.add(new User(username, id));
		}
	}

	/**
	 * A user is removed from the list
	 * 
	 * @param user the user to be logged out
	 */
	public void userLogout(User user) {
		if (user != null) {
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).equals(user)) {
					users.remove(i);
					break;
				}
			}
		}
	}

	/**
	 * Finds a user by using id
	 * 
	 * @param connectionID the specified id to be searched with
	 * 
	 * @return User if found one, null if not
	 */
	public User getByID(int connectionID) {
		boolean exists = false;
		int index = 0;

		// Check if there exists a user with the specified id
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getConnectionId() == connectionID) {
				exists = true;
				index = i;
				break;
			}
		}

		if (exists) {
			return users.get(index);
		} else
			return null;
	}

	/**
	 * Finds a lobby by using id
	 * 
	 * @param id the specified id to be searched with
	 * 
	 * @return Lobby if found one, null if not
	 */
	public Lobby getByID(long id) {
		boolean exists = false;
		int index = 0;

		// Check if there exists a lobby with the specified id
		for (int i = 0; i < lobbies.size(); i++) {
			if (lobbies.get(i).getId() == id) {
				exists = true;
				index = i;
				break;
			}
		}

		if (exists) {
			return lobbies.get(index);
		} else
			return null;
	}
	
	/**
	 * Finds a lobby by using connectionID of a user
	 * 
	 * @param connectionID the specified id to be searched with
	 * 
	 * @return Lobby if found one, null if not
	 */
	public Lobby findLobby(int connectionID) {
		User currentUser = getByID(connectionID);

		return currentUser.getLobby();
	}

}