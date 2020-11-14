package tr.com.bilkent.monopoly.network.packet;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * A generic network packet to be sent over the network.
 * 
 * @author Ziya Mukhtarov
 * @version Nov 14, 2020
 */
@Getter @Setter
public class NetworkPacket implements Serializable {
	private static final long serialVersionUID = 4336363027314300293L;
	
	private String sourceAddress;
	private long creationTime;
	
	public NetworkPacket(String ipAddress) {
		sourceAddress = ipAddress;
		creationTime = System.nanoTime();
	}
}
