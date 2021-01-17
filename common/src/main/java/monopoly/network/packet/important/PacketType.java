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

	GET_LOBBY_COUNT, LOBBY_COUNT, GET_LOBBIES, LOBBY_LIST, GET_USERS_IN_LOBBY, USERS_IN_LOBBY,

	CREATE_LOBBY, JOIN_LOBBY, JOIN_SUCCESS, PLAYER_JOIN, LEAVE_LOBBY, LEAVE_SUCCESS, PLAYER_LEFT,

	CHANGE_LOBBY_DATA, LOBBY_DATA_CHANGE,

	BAN_PLAYER, PLAYER_KICK,

	SET_READY, SET_READY_SUCCESS, PLAYER_READY,

	GAME_START, GET_GAME_DATA, GAME_DATA,

	PLAYER_TURN_ORDER, PLAYER_TURN, TURN_COMPLETE,

	ROLL_DICE, DICE_RESULT, TOKEN_MOVED,

	ACT_GAIN_MONEY, ACT_GIVE_MONEY, ACT_GO_TO_JAIL, ACT_EARN_GET_OUT_OF_JAIL_CARD, ACT_BUY_OR_AUCTION, ACT_DO_NOTHING,

	BUY_PROPERTY, PROPERTY_BOUGHT, BUILD_HOUSE, BUILD_HOTEL, SELL_HOUSE, SELL_HOTEL, GET_RENT_COST, RENT_COST,
	HOUSE_BUILT, HOTEL_BUILT, HOUSE_SOLD, HOTEL_SOLD,

	OPEN_MIC, CLOSE_MIC, OPEN_WEBCAM, CLOSE_WEBCAM, SEND_TEXT,

	INITIATE_TRADE, TRADE_INITIATED, ADD_TO_TRADE, ADDED_TO_TRADE, REMOVE_FROM_TRADE, REMOVED_FROM_TRADE, ACCEPT_TRADE,
	TRADE_ACCEPTED, REJECT_TRADE, TRADE_REJECTED, TRADE_COMPLETE,

	INITIATE_AUCTION, AUCTION_INITIATED, AUCTION_TURN, INCREASE_BID, BID_INCREASED, SKIP_BID, BID_SKIPPED, CURRENT_BID,
	AUCTION_COMPLETE,

	MORTGAGE_PROPERTY, UNMORTGAGE_PROPERTY, PROPERTY_MORTGAGED, PROPERTY_UNMORTGAGED,

	PLAYER_BALANCE,

	PROPERTY_DATA_CHANGE, TITLE_DEED_DATA,

	PLAYER_BANKRUPT, GAME_END,

//	Error Types
	ERR_UPDATE_REQUIRED, ERR_USERNAME_IN_USE, ERR_LOBBY_FULL, ERR_ALREADY_IN_LOBBY, ERR_NOT_IN_LOBYY,
	ERR_INVALID_LOBBY_LIMIT, ERR_WRONG_FORMAT, ERR_DISCONNECTED, ERR_UNKNOWN, ERR_BANNED, ERR_WRONG_PASSWORD,
	ERR_NOT_ENOUGH_BALANCE, ERR_NOT_MORTGAGED, ERR_MORTGAGED, ERR_NOT_ALL_HOUSES_BUILT, ERR_TRADE_EMPTY,
	ERR_NO_PROPERTY_OWNED, ERR_NOT_PLAYER_TURN, ERR_ALREADY_HAS_HOTEL, ERR_NOT_BID_TURN, ERR_LOBBY_IN_GAME,
	ERR_NOT_OWNED, ERR_BUILDINGS_NOT_BALANCED
}
