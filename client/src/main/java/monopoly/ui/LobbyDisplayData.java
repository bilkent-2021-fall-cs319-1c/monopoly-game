package monopoly.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LobbyDisplayData {
	private StringProperty owner;
	private StringProperty name;

	private BooleanProperty isPrivate;

	private IntegerProperty playerCount;
	private IntegerProperty limit;
	private StringProperty fullnessRatio;

	public LobbyDisplayData(String owner, String lobbyName, int playerCount, int limit, boolean isPrivate) {
		this.owner = new SimpleStringProperty(owner);
		this.name = new SimpleStringProperty(lobbyName);

		this.isPrivate = new SimpleBooleanProperty(isPrivate);

		fullnessRatio = new SimpleStringProperty(playerCount + " / " + limit);
		this.playerCount = new SimpleIntegerProperty(playerCount);
		this.limit = new SimpleIntegerProperty(limit);
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
