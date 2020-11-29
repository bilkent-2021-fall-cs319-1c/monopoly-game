package monopoly.ui.join_lobby;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import monopoly.network.packet.important.packet_data.LobbyPacketData;

public class LobbyDisplayData {
	private StringProperty owner;
	private StringProperty name;

	private BooleanProperty isPrivate;

	private IntegerProperty playerCount;
	private IntegerProperty limit;
	private StringProperty fullnessRatio;

	public LobbyDisplayData(LobbyPacketData lobby) {
		this.owner = new SimpleStringProperty(lobby.getOwnerUsername());
		this.name = new SimpleStringProperty(lobby.getName());

		this.isPrivate = new SimpleBooleanProperty(!lobby.isPublic());

		fullnessRatio = new SimpleStringProperty();
		this.playerCount = new SimpleIntegerProperty(lobby.getPlayerCount());
		this.limit = new SimpleIntegerProperty(lobby.getPlayerLimit());
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
