package monopoly;

import lombok.Data;
import monopoly.network.packet.important.PacketType;

@Data
public class Error {
	private String title;
	private String info;

	public Error(PacketType errorType) {
		title = getErrorTitle(errorType);
		info = getErrorInfo(errorType);
	}

	public Error(String title, String info) {
		this.title = title;
		this.info = info;
	}

	private String getErrorTitle(PacketType errorType) {
		if (errorType == PacketType.ERR_ALREADY_IN_LOBBY) {
			return "Already in a Lobby";
		}
		if (errorType == PacketType.ERR_DISCONNECTED) {
			return "Network Error";
		}
		if (errorType == PacketType.ERR_INVALID_LOBBY_LIMIT) {
			return "Invalid Lobby Limit";
		}
		if (errorType == PacketType.ERR_LOBBY_FULL) {
			return "Lobby is Full";
		}
		if (errorType == PacketType.ERR_NOT_IN_LOBYY) {
			return "Not in a Lobby";
		}
		if (errorType == PacketType.ERR_UPDATE_REQUIRED) {
			return "Update Required";
		}
		if (errorType == PacketType.ERR_USERNAME_IN_USE) {
			return "Username Already in Use";
		}
		return "Error";
	}

	private String getErrorInfo(PacketType errorType) {
		if (errorType == PacketType.ERR_ALREADY_IN_LOBBY) {
			return "You are already in a lobby. To perform the action, please leave the current lobby.";
		}
		if (errorType == PacketType.ERR_DISCONNECTED) {
			return "You have disconnected from the server. Please check your network and try again.";
		}
		if (errorType == PacketType.ERR_INVALID_LOBBY_LIMIT) {
			return "You have entered an invalid lobby player limit. Valid limits are between 2 and 6";
		}
		if (errorType == PacketType.ERR_LOBBY_FULL) {
			return "The lobby is full. Try other lobbies.";
		}
		if (errorType == PacketType.ERR_NOT_IN_LOBYY) {
			return "You are not in a lobby. To perform the action, please enter a lobby.";
		}
		if (errorType == PacketType.ERR_UPDATE_REQUIRED) {
			return "You need to update the game to benefit from the new cool features. You will be unable to play until you update to the new version.";
		}
		if (errorType == PacketType.ERR_USERNAME_IN_USE) {
			return "This username is being used by another player. Please try another username.";
		}
		return "An error occured while handling your request. If you think this is an accident, inform the developers.";
	}
}
