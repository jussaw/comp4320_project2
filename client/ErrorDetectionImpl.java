package client;
import java.net.DatagramPacket;

public class ErrorDetectionImpl implements IErrorDetection {
	public boolean detectErrors(DatagramPacket packet) {
		byte[] data = getData(packet);
		int receivedChecksum = getReceivedChecksum(packet);
		int calculatedChecksum = 0;
		for (byte b : data) {
			calculatedChecksum += new Byte(b).intValue();
		}
		System.out.println("Calculated checksum: " + calculatedChecksum);
		System.out.println("Parsed checksum: " + getReceivedChecksum(packet));
		return calculatedChecksum != getReceivedChecksum(packet);
	}
	public byte[] getData(DatagramPacket packet) {		
		String headersAndData = new String(packet.getData());
		String data = headersAndData.substring(headersAndData.indexOf("\r\n\r\n") + 4);
		return data.getBytes();
	}
	public int getReceivedChecksum(DatagramPacket packet) {
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
		return 0;
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
            return 0;
        }
        return 0;
    }
}