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
			try {
				String headersAndData = new String(packet.getData());
				String headers = headersAndData.substring(0, headersAndData.indexOf("\r\n\r\n"));
				String headersArr[] = headers.split("\r\n");
				for (String header : headersArr) {
					if (header.split(" ")[0].equals("Sequence-number:")) {
						return Integer.parseInt(header.split(" ")[1]);
					}
				}
			} catch (Exception e) {
				System.out.println("Unable to parse sequence number!");
				return -1;
			}
			return -1;
		}
	}
