package client;
import java.net.DatagramPacket;
import java.util.Comparator;
public class PacketComparator implements Comparator<DatagramPacket> {
	public PacketComparator() {}
		public int compare(DatagramPacket a, DatagramPacket b) {
			return this.getSequenceNumber(a) - this.getSequenceNumber(b);
		}
		public boolean equals(DatagramPacket a, DatagramPacket b) {
			return this.getSequenceNumber(a) == this.getSequenceNumber(b);
		}
		public int getSequenceNumber(DatagramPacket packet) {
			return SRPacket.parseSequenceNumber(packet);
		}
	}
