package client;
import java.net.DatagramPacket;

public class ErrorDetectionImpl implements IErrorDetection {
	public boolean detectErrors(DatagramPacket packet) {
		byte[] data = SRPacket.getData(packet);
		int receivedChecksum = SRPacket.parseChecksum(packet);
		int calculatedChecksum = 0;
		for (byte b : data) {
			calculatedChecksum += new Byte(b).intValue();
		}
		int parsedChecksum = SRPacket.parseChecksum(packet);
		System.out.println("Calculated checksum: " + calculatedChecksum);
		System.out.println("Parsed checksum: " + parsedChecksum);
		return calculatedChecksum != parsedChecksum;
	}
	
	
}
