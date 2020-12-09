package monopoly;

import monopoly.network.packet.important.ImportantNetworkPacket;

public interface ErrorListener {
	/**
	 * Handles the error that was sent from the server as a response to a client
	 * action
	 * 
	 * @param errorPacket The packet which contains the error information.
	 *                    {@link ImportantNetworkPacket#isErrorPacket()} is
	 *                    guaranteed to return true for this packet.
	 */
	public void errorOccured(Error error);
}
