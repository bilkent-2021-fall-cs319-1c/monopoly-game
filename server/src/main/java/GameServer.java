import monopoly.common.network.packet.NetworkPacket;
import monopoly.network.Server;

import java.io.IOException;

/**
 * Game Server class for server-side operations, extends abstract.
 *
 * @author Alper Sarı
 * @version Nov 22, 2020
 */

public class GameServer extends Server {

    public int TCP_PORT;
    public int UDP_PORT;

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
    public void receivedPacket(int connectionID, NetworkPacket packet) {

    }

    @Override
    public void receivedNotPacket(int connectionID, Object object) {

    }


}
