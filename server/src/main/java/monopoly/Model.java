package monopoly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import monopoly.lobby.Lobby;
import monopoly.lobby.User;

/**
 * Model for server, lobby and user management
 *
 * @author Alper Sarı, Javid Baghirov
 * @version Nov 29, 2020
 */
public class Model {
	private EntitiesWithId<Lobby> lobbies;
	private EntitiesWithId<User> users;

	// TODO Make it functional
	private List<Lobby> waitingLobbies;

	private GameServer server;

	public Model() throws IOException {
		lobbies = new EntitiesWithId<>();
		users = new EntitiesWithId<>();

		waitingLobbies = Collections.synchronizedList(new ArrayList<Lobby>());

		server = new GameServer(this);
	}

	/**
	 * A user is added to the list
	 * 
	 * @param username the specified name
	 * @param userId   the id of the user
	 */
	public void userLogin(String username, int userId) {
		users.add(new User(username, userId));
	}

	/**
	 * Creates a lobby
	 * 
	 * @param name     specified title
	 * @param limit    specified player limit
	 * @param isPublic status of being either public or private
	 * @param password specified pass code
	 * @param userId   the id of the user
	 * 
	 * @throws MonopolyException
	 */
	public void createLobby(String name, int limit, boolean isPublic, String password, int userId)
			throws MonopolyException {
		User user = getUserByID(userId);
		Lobby lobby = new Lobby(name, limit, isPublic, password, server);
		user.joinLobby(lobby, password);
		registerNewLobby(lobby);
	}

	private void registerNewLobby(Lobby lobby) {
		lobbies.add(lobby);
		waitingLobbies.add(lobby);
	}

	public void closeLobby(Lobby lobby) {
		lobbies.remove(lobby);
		waitingLobbies.remove(lobby);
	}

	/**
	 * A user is removed from the list
	 * 
	 * @param user the user to be logged out
	 */
	public void userLogout(User userLoggedOut) {
		users.remove(userLoggedOut);
	}

	public List<Lobby> getWaitingLobbies(int from, int to) {
		if (from < 0)
			from = 0;
		if (to > waitingLobbies.size())
			to = waitingLobbies.size();

		ArrayList<Lobby> result = new ArrayList<>();
		synchronized (waitingLobbies) {
			for (int i = from; i < to; i++) {
				result.add(waitingLobbies.get(i));
			}
		}
		return result;
	}

	/**
	 * Finds a user by using id
	 * 
	 * @param userId the specified id to be searched with
	 * 
	 * @return User if found one, null if not
	 */
	public User getUserByID(int userId) {
		return users.getByID(userId);
	}

	public Lobby getLobbyOfUser(int userId) {
		return getUserByID(userId).getLobby();
	}

	public int getLobbyCount() {
		return lobbies.size();
	}

	public Lobby getLobbyByID(int id) {
		return lobbies.getByID(id);
	}
}
