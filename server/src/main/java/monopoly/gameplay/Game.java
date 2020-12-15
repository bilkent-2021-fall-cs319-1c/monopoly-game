package monopoly.gameplay;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import monopoly.MonopolyException;
import monopoly.lobby.Lobby;
import monopoly.network.GameServer;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.important.packet_data.gameplay.PlayerListPacketData;

/**
 * The main game class with all the gameplay functionalities
 * 
 * @author Javid Baghirov
 * @version Dec 12, 2020
 */

public class Game {
	private Lobby lobby;

	private Dice dice;

	private List<GamePlayer> playersByTurn;
	private int turnNumber;

	/**
	 * Creates a game object
	 * 
	 * @param lobby the specified lobby that the game starts in
	 * @throws NoSuchAlgorithmException when the requested algorithm is not
	 *                                  available
	 */
	public Game (Lobby lobby) {
		playersByTurn = lobby.getUsers().parallelStream().map(u -> u.asPlayer()).collect(Collectors.toList());	
		initializeGamePlayers();
		
		//Randomize the turns
		Collections.shuffle(playersByTurn);
		
		dice = new Dice();

		this.lobby = lobby;
		turnNumber = 0;
	}
	
	private void initializeGamePlayers() {
		for (int i = 0; i < playersByTurn.size(); i++) {
			playersByTurn.set( i, new GamePlayer( lobby.getUsers().get( i)));
		}
	}

	public GamePlayer nextTurn() {
		turnNumber = (turnNumber + 1) % playersByTurn.size();
		
		return getCurrentPlayer();
	}
	
	public GamePlayer getCurrentPlayer() {
		return playersByTurn.get( turnNumber);
	}
	
	private boolean isPlayerTurn( GamePlayer player) {
		return getCurrentPlayer().equals( player);
	}
	
	public DiceData rollDice( GamePlayer player) throws MonopolyException {
		if ( !isPlayerTurn( player)) {
			//TODO ERR_NOT_PLAYER_TURN
			throw new MonopolyException(PacketType.ERR_UNKNOWN);
		}
		dice.roll();
		
		return dice.getDiceData();
	}
	
	public PlayerListPacketData getPlayersAsPacket() {
		PlayerListPacketData playerList = new PlayerListPacketData();
		playersByTurn.forEach(player -> playerList.add(player.getPlayerAsPacket()));

		return playerList;
	}
	
	public void sendTurnOrderToPlayers() {
		new Thread(() -> playersByTurn.parallelStream()
				.forEach(player -> GameServer.getInstance().sendPlayersTurnOrderNotification( player))).start();
	}
}
