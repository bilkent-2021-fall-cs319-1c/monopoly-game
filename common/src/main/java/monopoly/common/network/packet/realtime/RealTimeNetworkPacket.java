package monopoly.common.network.packet.realtime;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * A generic network packet to be sent over the network for real-time
 * applications. Usually, represents data that can be send over unreliable
 * network (UDP). It should not matter if a few of these packets gets dropped or
 * arrive out of order.
 * 
 * @author Ziya Mukhtarov
 * @version Nov 18, 2020
 */
@Getter
@Setter
public class RealTimeNetworkPacket implements Serializable {
	private static final long serialVersionUID = 4336363027314300293L;

	private int sourceConnectionID;
	private long creationInternalTime;

	public RealTimeNetworkPacket(int connectionID) {
		creationInternalTime = System.nanoTime();
		sourceConnectionID = connectionID;
	}
}
