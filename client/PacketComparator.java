package client;
import java.net.DatagramPacket;
import java.util.Comparator;
public class PacketComparator implements Comparator<DatagramPacket> {
	public PacketComparator() {}
	public int compare(DatagramPacket a, DatagramPacket b) {
		return SRPacket.parseSequenceNumber(a) - SRPacket.parseSequenceNumber(b);
	}
	public boolean equals(DatagramPacket a, DatagramPacket b) {
		return SRPacket.parseSequenceNumber(a) == SRPacket.parseSequenceNumber(b);
	}
}
