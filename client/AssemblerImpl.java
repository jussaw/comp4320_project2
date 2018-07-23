package client;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Comparator;
public class AssemblerImpl implements IAssembler {

	public ArrayList<DatagramPacket> receivedPackets;
	private Comparator<DatagramPacket> packetComparator;
	private DatagramPacket nullPacket;

	AssemblerImpl() {
		this.receivedPackets = new ArrayList<DatagramPacket>();
		this.packetComparator = new PacketComparator();
	}

	public void newPacketIn(DatagramPacket newPacket) {
		if (!receivedPackets.contains(newPacket)) {
			System.out.println("NEW PACKET IN: " + SRPacket.parseSequenceNumber(newPacket) );
			if (SRPacket.parseChecksum(newPacket) == 0) { // null packet (final packet) received
				System.out.println("received null packet");
				this.nullPacket = newPacket;
			}
			this.receivedPackets.add(newPacket);
		}
	}

	public byte[] getAssembledDocument() {
		String document = "";
		this.receivedPackets.sort(this.packetComparator);
		for (DatagramPacket packet : receivedPackets) {
			document += SRPacket.getData(packet);
			System.out.println(SRPacket.getData(packet));
		}
		return document.getBytes();
	}

	public int getSequenceNumber(DatagramPacket packet) {
		return SRPacket.parseSequenceNumber(packet);
	}

	public int getChecksum(DatagramPacket packet) {
		return SRPacket.parseChecksum(packet);
	}

	public String getPayload(DatagramPacket packet) {
		String data = new String(packet.getData());
		return data.substring(data.indexOf("\r\n\r\n") + 4);
	}
	public boolean isComplete() {
		return this.nullPacket != null && SRPacket.parseSequenceNumber(nullPacket) == receivedPackets.size() - 1;
	}
}
