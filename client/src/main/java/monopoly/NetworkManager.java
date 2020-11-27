package monopoly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import monopoly.network.Client;
import monopoly.network.ServerInfo;
import monopoly.network.packet.important.ImportantNetworkPacket;
import monopoly.network.packet.important.PacketType;
import monopoly.network.packet.important.packet_data.IntegerPacketData;
import monopoly.network.packet.realtime.RealTimeNetworkPacket;

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
			askAndGetResponse(new ImportantNetworkPacket(PacketType.CONNECT), PacketType.ACCEPTED);
		}

		@Override
		public void disconnected(int connectionID) {
			// TODO Show error unless this was requested by the client
		}

		@Override
		public void receivedRealTimePacket(int connectionID, RealTimeNetworkPacket packet) {
			// TODO Handle the real-time packet

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
