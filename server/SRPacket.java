package server;
import java.net.DatagramPacket;
import java.util.Date;
public class SRPacket {

	private DatagramPacket packet;
	private int sequenceNumber;
	private int parsedChecksum;
	private boolean acked;
	private long sentTime;

	public SRPacket(DatagramPacket packet) {
		this.packet = packet;
		this.sequenceNumber = SRPacket.parseSequenceNumber(packet);
		this.parsedChecksum = SRPacket.parseChecksum(packet);
		this.acked = false;
	}

	public void ack() {
		this.acked = true;
	}
	public boolean isAcked() {
		return this.acked;
	}
	public int getSequenceNumber() {
		return this.sequenceNumber;
	}
	public int getParsedChecksum() {
		return this.parsedChecksum;
	}
	public DatagramPacket getDatagramPacket() {
		return this.packet;
	}
	public static int parseSequenceNumber(DatagramPacket packet) {
		try {
			String headersAndData = new String(packet.getData());
			String headers = headersAndData.substring(0, headersAndData.indexOf("\r\n\r\n"));
			String headersArr[] = headers.split("\r\n");
			for (String header : headersArr) {
				if (header.split(" ")[0].equals("Sequence-Number:")) {
					return Integer.parseInt(header.split(" ")[1]);
				}
			}
		} catch (Exception e) {
			System.out.println("Unable to parse sequence number!");
			return -1;
		}
		System.out.println("Unable to parse sequence number!");
		return -1;
	}
	public static int parseChecksum(DatagramPacket packet) {
		try {
			String headersAndData = new String(packet.getData());
			String headers = headersAndData.substring(0, headersAndData.indexOf("\r\n\r\n"));
			String headersArr[] = headers.split("\r\n");
			for (String header : headersArr) {
				if (header.split(" ")[0].equals("Checksum:")) {
					return Integer.parseInt(header.split(" ")[1]);
				}
			}
		} catch (Exception e) {
			System.out.println("Unable to parse checksum!");
			return 0;
		}
		System.out.println("Unable to parse checksum!");
		return 0;
	}
	public static byte[] getData(DatagramPacket packet) {
		String headersAndData = new String(packet.getData());
		String data = headersAndData.substring(headersAndData.indexOf("\r\n\r\n") + 4);
		return data.getBytes();
	}

	public void setSentTime() {
		this.sentTime = new Date().getTime();
	}
	public long getSentTime() {
		return this.sentTime;
	}
}
