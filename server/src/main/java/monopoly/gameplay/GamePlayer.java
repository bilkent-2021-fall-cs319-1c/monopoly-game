package monopoly.gameplay;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import monopoly.MonopolyException;
import monopoly.lobby.User;
import monopoly.gameplay.tiles.*;
import monopoly.network.GameServer;
import monopoly.network.packet.important.packet_data.gameplay.DicePacketData;
import monopoly.network.packet.important.packet_data.gameplay.PlayerPacketData;
import monopoly.network.packet.important.packet_data.gameplay.TilePacketData;

import java.util.ArrayList;

/**
 * A game player
 * 
 * @author Javid Baghirov
 * @version Dec 13, 2020
 */

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class GamePlayer extends User {
	private static final int START_BALANCE = 1500;

	private int balance;
	private int tileIndex;
	private Tile tile;
	private ArrayList<Property> properties;
	private boolean inJail;
	private boolean rolledDouble;

	// private Game game;

	public GamePlayer(User user) {
		super(user);
		user.setGamePlayerInstance(this);
		setGamePlayerInstance(this);

		balance = START_BALANCE;
		tileIndex = 0;
		updateTile();
		properties = new ArrayList<>();
		inJail = false;
		// this.game = game;
	}

	public DicePacketData rollDice() throws MonopolyException {
		return getLobby().getGame().rollDice(this);
	}

	public TilePacketData move() {
		try {
			getLobby().getGame().move(this);
		} catch (MonopolyException e) {
			GameServer.getInstance().sendImportantPacket( e.getAsPacket(), getId());
		}

		return new TilePacketData(tile.getName(), tile.getDescription(), "", "", tileIndex);
	}

	public void updateTile() {
		Board board = getLobby().getGame().getBoard();

		tile = board.getTiles().get(tileIndex);
	}

	public PlayerPacketData getPlayerAsPacket() {
		return new PlayerPacketData(getId(), getUsername(), balance);
	}

	public void goToJail()
	{
		if(!inJail)
		{
			Board board = getLobby().getGame().getBoard();
			tile = board.getTiles().get(board.getJAIL_POSITION());
			tileIndex = tile.getIndex();
			inJail = true;
		}
	}

	/*

	//This functionality exists within the property tile's doAction method. Commented for now

	public void payRent()
	{
		if(tile instanceof PropertyTile)
			if(!((PropertyTile) tile).getProperty().getOwner().equals(this)
					&& ((PropertyTile) tile).getProperty().getOwner() != null)
			{
				balance -= ((PropertyTile) tile).getProperty().getRentCost();
			}
	}
	*/
	public boolean buyProperty()
	{
		updateTile();
		if(tile instanceof PropertyTile)
			if(((PropertyTile) tile).getProperty().getTitleDeed().getBuyCost() <= balance
				&& !properties.contains(((PropertyTile) tile).getProperty()))
			{
				((PropertyTile) tile).getProperty().setOwner(this);
				balance -= ((PropertyTile) tile).getProperty().getTitleDeed().getBuyCost();
				return true;
			}
			else
				return false;
		else
			return false;
	}

	public boolean buildHouse()
	{
		boolean flag = false;
		if(tile instanceof PropertyTile)
			if(((PropertyTile) tile).getProperty() instanceof Street)
				if(((StreetTitleDeedData)(((PropertyTile) tile).getProperty().getTitleDeed())).getHouseCost() <= balance)
				{
					flag = ((Street)(((PropertyTile) tile).getProperty())).buildHouse();
				}
		return flag;
	}

	public boolean buildHotel()
	{
		boolean flag = false;
		if(tile instanceof PropertyTile)
			if(((PropertyTile) tile).getProperty() instanceof Street)
				if(((StreetTitleDeedData)(((PropertyTile) tile).getProperty().getTitleDeed())).getHouseCost() <= balance)
				{
					flag = ((Street)(((PropertyTile) tile).getProperty())).buildHotel();
				}
		return flag;
	}

}
