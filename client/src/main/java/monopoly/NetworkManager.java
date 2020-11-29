package monopoly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import monopoly.network.Client;
import monopoly.network.ServerInfo;
import monopoly.network.packet.important.ImportantNetworkPacket;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.important.packet_data.BooleanPacketData;
import monopoly.network.packet.important.packet_data.IntegerPacketData;
import monopoly.network.packet.important.packet_data.LobbyListPacketData;
import monopoly.network.packet.important.packet_data.LobbyPacketData;
import monopoly.network.packet.realtime.RealTimeNetworkPacket;
import monopoly.ui.ClientApplication;
import monopoly.ui.GameplayController;

public class NetworkManager {
	private Client client;

	private PacketType waitingForResponseType;
	private ImportantNetworkPacket responsePacket;

	private List<ErrorListener> errorListeners;

	public NetworkManager() throws IOException {
		waitingForResponseType = null;
		errorListeners = Collections.synchronizedList(new ArrayList<ErrorListener>());
		client = new MonopolyClient(ServerInfo.IP);
	}

	/**
	 * Adds a new error listener. Uniqueness is not checked, meaning that the
	 * listener would be notified the number of times it is registered
	 * 
	 * @param listener
	 */
	public void addErrorListener(ErrorListener listener) {
		errorListeners.add(listener);
	}

	/**
	 * Removes the given listener. If the listener was not added, nothing happens.
	 * If multiple instances were added, only the first one is removed.
	 * 
	 * @param listener
	 */
	public void removeErrorListener(ErrorListener listener) {
		errorListeners.remove(listener);
	}

	private void notifyAllErrorListeners(ImportantNetworkPacket errorPacket) {
		errorListeners.parallelStream().forEach(l -> l.errorOccured(errorPacket));
	}

	/**
	 * Queries the server for the number of open and not-in-game lobbies. This
	 * method blocks until the response is received, or an error packet is received,
	 * in which case the error listeners are notified. Note that, this method may
	 * return before all the listeners have been notified.
	 * 
	 * @return The number of lobbies, or -1 if an error packet is received.
	 */
	public int getNumberOfLobbies() {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.GET_LOBBY_COUNT);
		ImportantNetworkPacket response = askAndGetResponse(request, PacketType.LOBBY_COUNT);

		if (response == null) {
			return -1;
		}
		return ((IntegerPacketData) response.getData().get(0)).getData();
	}

	/**
	 * Queries the server for the open and not-in-game lobbies in the given range.
	 * This method blocks until the response is received, or an error packet is
	 * received, in which case the error listeners are notified. Note that, this
	 * method may return before all the listeners have been notified.
	 * 
	 * @param from The starting index of the lobbies to request, inclusive
	 * @param to   The ending index of the lobbies to request, inclusive
	 * @return The list of requested lobbies, or an empty list if an error packet is
	 *         received. Note that the return value of empty list does not guarantee
	 *         that an error packet is received
	 */
	public List<LobbyPacketData> getLobbies(int from, int to) {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.GET_LOBBIES, new IntegerPacketData(from),
				new IntegerPacketData(to));
		ImportantNetworkPacket response = askAndGetResponse(request, PacketType.LOBBY_LIST);

		if (response == null) {
			return Collections.emptyList();
		}
		return ((LobbyListPacketData) response.getData().get(0)).getLobbies();
	}

	/**
	 * Asks the server to allow this user join the lobby. This method blocks until
	 * the response is received, or an error packet is received, in which case the
	 * error listeners are notified. Note that, this method may return before all
	 * the listeners have been notified.
	 * 
	 * @param lobby The lobby to join
	 * @return true if the player could join the lobby, false if an error packet is
	 *         received
	 */
	public boolean joinLobby(LobbyPacketData lobby) {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.JOIN_LOBBY, lobby);
		ImportantNetworkPacket response = askAndGetResponse(request, PacketType.JOIN_SUCCESS);

		return response != null;
	}

	/**
	 * Asks the server to allow this user leave the lobby. This method blocks until
	 * the response is received, or an error packet is received, in which case the
	 * error listeners are notified. Note that, this method may return before all
	 * the listeners have been notified.
	 * 
	 * @return true if the player could leave the lobby, false if an error packet is
	 *         received
	 */
	public boolean leaveLobby() {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.LEAVE_LOBBY);
		ImportantNetworkPacket response = askAndGetResponse(request, PacketType.LEAVE_SUCCESS);

		return response != null;
	}

	/**
	 * Asks the server to create a lobby with the given data. This method blocks
	 * until the response is received, or an error packet is received, in which case
	 * the error listeners are notified. Note that, this method may return before
	 * all the listeners have been notified.
	 * 
	 * @return true if the player could create the lobby, false if an error packet
	 *         is received
	 */
	public boolean createLobby(String lobbyName, boolean isPublic, String password, int playerLimit) {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.CREATE_LOBBY,
				new LobbyPacketData(0, lobbyName, password, isPublic, "", 0, playerLimit));
		ImportantNetworkPacket response = askAndGetResponse(request, PacketType.LOBBY_CREATED);

		return response != null;
	}

	/**
	 * Asks the server to change this user's readiness status. This method blocks
	 * until the response is received, or an error packet is received, in which case
	 * the error listeners are notified. Note that, this method may return before
	 * all the listeners have been notified.
	 * 
	 * @return true if the player could change the status, false if an error packet
	 *         is received
	 */
	public boolean setReady(boolean ready) {
		ImportantNetworkPacket request = new ImportantNetworkPacket(PacketType.SET_READY, new BooleanPacketData(ready));
		ImportantNetworkPacket response = askAndGetResponse(request, PacketType.SET_READY_SUCCESS);

		return response != null;
	}

	/**
	 * Only one of this method should run at once during the runtime, hence the
	 * synchronized keyword. It blocks until the desired network packet, or an error
	 * packet is received.
	 * 
	 * @param packet       The packet to use as a prompt
	 * @param responseType The response type to wait for
	 * @return The packet that was received as a response in the desired type, or
	 *         null if an error packet is received
	 */
	private synchronized ImportantNetworkPacket askAndGetResponse(ImportantNetworkPacket packet,
			PacketType responseType) {
		waitingForResponseType = responseType;
		responsePacket = null;
		client.sendImportantPacket(packet);

		while (responsePacket == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}

		if (responsePacket.isErrorPacket()) {
			new Thread(() -> notifyAllErrorListeners(responsePacket)).start();
			return null;
		} else {
			return responsePacket;
		}
	}

	private class MonopolyClient extends Client {
		public MonopolyClient(String serverAddress) throws IOException {
			super(serverAddress);
		}

		@Override
		public void connected(int connectionID) {
			new Thread(() -> askAndGetResponse(new ImportantNetworkPacket(PacketType.CONNECT), PacketType.ACCEPTED))
					.start();
		}

		@Override
		public void disconnected(int connectionID) {
			notifyAllErrorListeners(new ImportantNetworkPacket(PacketType.ERR_UNKNOWN));
		}

		@Override
		public void receivedRealTimePacket(int connectionID, RealTimeNetworkPacket packet) {
			Object uiController = ClientApplication.getInstance().getController();
			if (uiController instanceof GameplayController) {
				GameplayController gameplayController = (GameplayController) uiController;
				gameplayController.realTimePacketReceived(packet);
			}
		}

		@Override
		public void receivedImportantPacket(int connectionID, ImportantNetworkPacket packet) {
			if (packet.getType() == waitingForResponseType || packet.isErrorPacket()) {
				responsePacket = packet;
				synchronized (NetworkManager.this) {
					NetworkManager.this.notifyAll();
				}
			} else {
				// TODO Handle the packet that is not the result to a user request
			}
		}
	}
}
