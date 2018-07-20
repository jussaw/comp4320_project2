package server;
import java.net.DatagramPacket;
public interface ISegmentation {
	// accepts data in bytes, converts into equally sized packets
	DatagramPacket[] segmentPackets(byte[] data, int packetSize);
	int calculateChecksum(byte[] buf);
	public byte[] includeHeaderLines(byte[] buf, int sequenceNumber);
}