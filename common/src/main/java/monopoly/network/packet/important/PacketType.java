package monopoly.network.packet.important;

/**
 * Contains all the types of important packets required for monopoly
 * application. Can be used to establish a successful communication protocol
 * between the server and a client.
 * 
 * @author Ziya Mukhtarov
 * @version Nov 28, 2020
 */
public enum PacketType {
	CONNECT, ACCEPTED,

	CHANGE_USERNAME, USERNAME_CHANGE,

	GET_LOBBY_COUNT, LOBBY_COUNT, GET_LOBBIES, LOBBY_LIST,

	JOIN_LOBBY, JOIN_SUCCESS, PLAYER_JOIN, LEAVE_LOBBY, LEAVE_SUCCESS, PLAYER_LEFT,

	CREATE_LOBBY, LOBBY_CREATED,

	CHANGE_LOBBY_DATA, LOBBY_DATA_CHANGE,

	BAN_PLAYER, PLAYER_KICK,

	SET_READY, SET_READY_SUCCESS, PLAYER_READY,

	GAME_START,

//	Error Types
	ERR_UPDATE_REQUIRED, ERR_USERNAME_IN_USE, ERR_LOBBY_FULL, ERR_ALREADY_IN_LOBBY, ERR_NOT_IN_LOBYY,
	ERR_INVALID_LOBBY_LIMIT, ERR_WRONG_FORMAT, ERR_DISCONNECTED, ERR_UNKNOWN
}
