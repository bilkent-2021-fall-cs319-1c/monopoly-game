package monopoly;
import java.io.IOException;

import monopoly.network.Server;
import monopoly.network.packet.important.ImportantNetworkPacket;
import monopoly.network.packet.realtime.RealTimeNetworkPacket;

/**
 * Game Server class for server-side operations, extends abstract.
 *
 * @author Alper Sarı
 * @version Nov 22, 2020
 */

public class GameServer extends Server {

    private static GameServer instance = null;

    /**
     * Creates a TCP and UDP server
     *
     * @throws IOException if the server could not be opened
     */
    private GameServer() throws IOException {
        super();
    }

    public static GameServer getInstance() throws IOException {
        if (instance == null)
            instance = new GameServer();

        return instance;
    }

    @Override
    public void connected(int connectionID) {

    }

    @Override
    public void disconnected(int connectionID) {

    }

	@Override
	public void receivedRealTimePacket(int connectionID, RealTimeNetworkPacket packet) {
		
	}

	@Override
	public void receivedImportantPacket(int connectionID, ImportantNetworkPacket packet) {
		
	}
}
