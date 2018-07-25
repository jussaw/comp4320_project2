package server;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Vector;

public class SelectiveRepeatImpl implements ISelectiveRepeat {

	public DatagramSocket serverSocket;
	public static final int WINDOW_SIZE = 8;
	public static final long TIMEOUT_INTERVAL = 100;
	public void transmit(DatagramPacket[] packetsToSend) {
		
		// initialize array of SRPacket based on packetsToSend
		Vector<SRPacket> allPackets = new Vector<SRPacket>(packetsToSend);
		Vector<SRPacket> window = new Vector<SRPacket>();

		// begin sending packets
		while (allPackets.size() > 0) {
			// if there is room in the window send a packet
			if (window.size() < WINDOW_SIZE) {
				DatagramPacket packet = allPackets.remove(0);
				packet.setSendTime();
				packet.ack();
				System.out.println("Sending packet:" + packet.getSequenceNumber());
				serverSocket.send(packet.getDatagramPacket());
				window.add(packet);
			}
			// if there isn't room in the window and the packet hasn't timed out, wait to receive ACK/NAK 
			else if (!window.get(0).isAcked() && new Date().getTime() - window.get(0).getSentTime() < TIMEOUT_INTERVAL) {
				DatagramPacket ackPacket;
				serverSocket.receive(ackPacket);
				// TODO PROCESS ACK/NAK

			} 
			// a packet times out
			else {
				// retransmit timed out packet 
				DatagramPacket retransmitPacket = window.remove(0);
				retransmitPacket.setSendTime();
				serverSocket.send(retransmitPacket.getDatagramPacket);
				window.add(retransmitPacket);

			}
		}
	}
}