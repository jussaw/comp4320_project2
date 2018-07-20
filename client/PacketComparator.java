package client;
import java.net.DatagramPacket;
import java.util.Comparator;
public class PacketComparator implements Comparator<DatagramPacket> {
	public PacketComparator() {}
	public int compare(DatagramPacket a, DatagramPacket b) {
		return 0;
	}
	public boolean equals(DatagramPacket a, DatagramPacket b) {
		return true;
	}
	public int getSequenceNumber(DatagramPacket packet) {
		return 0;
	}
}