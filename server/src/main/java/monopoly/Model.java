package monopoly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import monopoly.gameplay.Game;
import monopoly.lobby.Lobby;
import monopoly.lobby.User;
import monopoly.network.GameServer;

/**
 * The game model that establishes connection between classes and server side
 * operations
 *
 * @author Alper Sarı, Javid Baghirov
 * @version Nov 29, 2020
 */
public class Model implements GameStartListener {
	private EntitiesWithId<Lobby> lobbies;
	private List<Lobby> waitingLobbies;
	private EntitiesWithId<User> users;

	/**
	 * Creates a model object
	 * 
	 * @throws IOException if the server could not be opened
	 */
	public Model() {
		lobbies = new EntitiesWithId<>();
		users = new EntitiesWithId<>();

		waitingLobbies = Collections.synchronizedList(new ArrayList<Lobby>());

		// Creates a game server
		GameServer.getInstance().setModel(this);
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
	 * Creates a new lobby with the specified parameters
	 * 
	 * @param name     specified title
	 * @param limit    specified player limit
	 * @param isPublic status of being either public or private
	 * @param password specified pass code
	 * @param userId   the id of the creator (owner)
	 * 
	 * @throws MonopolyException when the specified limit is out of bounds of
	 *                           minimum or maximum limits, or the owner is already
	 *                           in a lobby
	 */
	public void createLobby(String name, int limit, boolean isPublic, String password, int userId)
			throws MonopolyException {
		User user = getUserByID(userId);
		Lobby lobby = new Lobby(name, limit, isPublic, password);
		lobby.setGameStartListener(this);
		registerNewLobby(lobby);
		user.joinLobby(lobby, password);
	}

	private void registerNewLobby(Lobby lobby) {
		lobbies.add(lobby);
		waitingLobbies.add(lobby);
	}

	public void closeLobby(Lobby lobby) {
		lobbies.remove(lobby);
		waitingLobbies.remove(lobby);
	}

	public void userLogout(User userLoggedOut) {
		users.remove(userLoggedOut);
	}

	public List<Lobby> getWaitingLobbies(int from, int to) {
		if (from < 0)
			from = 0;
		if (to > waitingLobbies.size())
			to = waitingLobbies.size();

		if (from > to)
			return Collections.emptyList();
		return new ArrayList<>(waitingLobbies.subList(from, to));
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

	/**
	 * Finds the lobby of a user
	 * 
	 * @param userId the specified user id
	 * 
	 * @return Lobby if found one, null if not
	 */
	public Lobby getLobbyOfUser(int userId) {
		return getUserByID(userId).getLobby();
	}

	public int getWaitingLobbyCount() {
		return waitingLobbies.size();
	}

	/**
	 * Finds a lobby by using id
	 * 
	 * @param id the specified id to be searched with
	 * 
	 * @return Lobby if found one, null if not
	 */
	public Lobby getLobbyByID(int id) {
		return lobbies.getByID(id);
	}

	public Game getGameOfPlayer(int playerId) {
		return getLobbyOfUser(playerId).getGame();
	}

	@Override
	public void gameStarted(Lobby lobby) {
		waitingLobbies.remove(lobby);
	}

}
