package server;
import java.net.DatagramPacket
public class SRPacket {

	private DatagramPacket packet;
	private int sequenceNumber;
	private boolean acked;

	public SRPacket(DatagramPacket packet) {
		this.packet = packet;
		this.sequenceNumber = parseSequenceNumber(packet);
		this.acked = false;
	}

	public int parseSequenceNumber(DatagramPacket packet) {
		// TODO
		return 0;
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
}
