package monopoly.network.packet.important;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import monopoly.network.packet.important.packet_data.PacketData;

/**
 * A generic network packet to be sent over the network for important
 * communications. Usually, represents data that should be send over reliable
 * network (TCP). These packets all should arrive with no dropped packet and
 * should arrive in the same order they are sent
 * 
 * @author Ziya Mukhtarov
 * @version Nov 27, 2020
 */
@Getter
@Setter
public class ImportantNetworkPacket implements Serializable {
	private static final long serialVersionUID = -287461067569225738L;

	private PacketType type;
	private List<PacketData> data;

	public ImportantNetworkPacket(PacketType type, PacketData... data) {
		this.type = type;

		this.data = new ArrayList<>();
		this.data.addAll(Arrays.asList(data));
	}
}
