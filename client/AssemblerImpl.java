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

	}

	public byte[] getAssembledDocument() {
		return new byte[0];
	}

	public int getSequenceNumber(DatagramPacket packet) {
		return 0;
	}

	public int getChecksum(DatagramPacket packet) {
		return 0;
	}

	public String getPayload(DatagramPacket packet) {
		return "";
	}
	public boolean isComplete() {
		return true;	
	}
}