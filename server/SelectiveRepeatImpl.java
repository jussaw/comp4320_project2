package server;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SelectiveRepeatImpl implements ISelectiveRepeat {
	public int windowBase;
	public int topOfWindow;
	public DatagramSocket serverSocket;
	public static final int WINDOW_SIZE = 8;
	public void transmit(DatagramPacket[] packetsToSend) {
		//initialize window indexes
		windowBase = 0;
		topOfWindow = windowBase + WINDOW_SIZE;

		// initialize array of SRPacket based on packetsToSend
		SRPacket[] allPackets = new SRPacket[packetsToSend.length];
		for (int i = 0; i <  allPackets.length; i++) {
			allPackets[i] = new SRPacket(packetsToSend[i]);
		}
		// begin sending packets
		for (DatagramPacket packet : packetsToSend) {
			if (topOfWindow - windowBase < WINDOW_SIZE) {

			}
		}
	}
}