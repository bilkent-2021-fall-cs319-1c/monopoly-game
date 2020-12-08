package monopoly;

import lombok.Getter;
import monopoly.network.packet.important.ImportantNetworkPacket;
import monopoly.network.packet.important.PacketType;

@Getter
public class MonopolyException extends Exception {
	private static final long serialVersionUID = 1976012003937529596L;

	private final PacketType errorType;

	public MonopolyException(PacketType errorType) {
		this.errorType = errorType;
	}

	public ImportantNetworkPacket getAsPacket() {
		return new ImportantNetworkPacket(errorType);
	}
}
