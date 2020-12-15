package monopoly.ui.controller.join_lobby;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import monopoly.network.packet.important.packet_data.LobbyPacketData;

/**
 * Lobby data wrapper for JavaFX table
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */
public class LobbyDisplayData {
	@Getter
	private LobbyPacketData packetData;

	private StringProperty owner;
	private StringProperty name;

	private BooleanProperty isPrivate;

	private IntegerProperty playerCount;
	private IntegerProperty limit;
	private StringProperty fullnessRatio;

	/**
	 * Initializes the wrapper data with the given lobby packet data
	 */
	public LobbyDisplayData(LobbyPacketData lobby) {
		packetData = lobby;

		owner = new SimpleStringProperty(lobby.getOwnerUsername());
		name = new SimpleStringProperty(lobby.getName());

		isPrivate = new SimpleBooleanProperty(!lobby.isPublic());

		fullnessRatio = new SimpleStringProperty();
		playerCount = new SimpleIntegerProperty(lobby.getPlayerCount());
		limit = new SimpleIntegerProperty(lobby.getPlayerLimit());
		fullnessRatio.bind(Bindings.concat(this.playerCount, " / ", this.limit));
	}

	public String getOwner() {
		return owner.get();
	}

	public void setOwner(String owner) {
		this.owner.set(owner);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public Integer getPlayerCount() {
		return playerCount.get();
	}

	public void setPlayerCount(Integer playerCount) {
		this.playerCount.set(playerCount);
	}

	public Integer getLimit() {
		return limit.get();
	}

	public void setLimit(Integer limit) {
		this.limit.set(limit);
	}

	public Boolean getIsPrivate() {
		return isPrivate.get();
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate.set(isPrivate);
	}

	public String getFullnessRatio() {
		return fullnessRatio.get();
	}
}
