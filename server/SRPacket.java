package server;
import java.net.DatagramPacket;
public class SRPacket {

	private DatagramPacket packet;
	private int sequenceNumber;
	private boolean acked;

	public SRPacket(DatagramPacket packet) {
		this.packet = packet;
		this.sequenceNumber = getSequenceNumber(packet);
		this.acked = false;
	}

	public void ack() {
		this.acked = true;
	}
	public boolean isAcked() {
		return this.acked;
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
			System.out.println("Unable to parse checksum!");
			return -1;
		}
		return -1;
	}
}
