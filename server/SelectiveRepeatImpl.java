package server;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Vector;
import java.util.Date;
public class SelectiveRepeatImpl implements ISelectiveRepeat {

	public DatagramSocket serverSocket;
	public static final int WINDOW_SIZE = 8;
	public static final long TIMEOUT_INTERVAL = 100;
	public static final int PORT_NUMBER = 10025;

	// initialize array of SRPacket based on packetsToSend
	Vector<SRPacket> allPackets;
	Vector<SRPacket> window;

	public SelectiveRepeatImpl(DatagramSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	public void transmit(DatagramPacket[] packetsToSend, InetAddress clientIp, int clientPort) throws Exception {

		// initialize array of SRPacket based on packetsToSend
		allPackets = new Vector<SRPacket>();
		window = new Vector<SRPacket>();

		for (DatagramPacket packet : packetsToSend) {
			packet.setPort(clientPort);
			packet.setAddress(clientIp);
			SRPacket newPacket = new SRPacket(packet);
			allPackets.add(newPacket);
		}
		// begin sending packets
		while (allPackets.size() > 0) {
			// if there is room in the window send a packet
			if (window.size() < WINDOW_SIZE) {
				SRPacket packet = allPackets.remove(0);
				packet.setSentTime();
				System.out.println("Sending packet:" + packet.getSequenceNumber());
				serverSocket.send(packet.getDatagramPacket());
				window.add(packet);
			}
			// if there isn't room in the window and the packet hasn't timed out, wait to receive ACK/NAK
			else if (!window.get(0).isAcked() && new Date().getTime() - window.get(0).getSentTime() < TIMEOUT_INTERVAL) {
				byte[] receiveBuf = new byte[1024];
				DatagramPacket ackPacket = new DatagramPacket(receiveBuf, receiveBuf.length);
				serverSocket.receive(ackPacket);
				System.out.println(new String(ackPacket.getData()));
				// If ACK received then process ACK, elseprocess NAK
				String data = new String(ackPacket.getData());
				String sequenceNumberString = data.split(" ")[1];
				int sequenceNumber = Integer.parseInt(sequenceNumberString);
				if (parseACK(ackPacket)) {
					processACK(sequenceNumber);
				}
				else {
					processNAK(sequenceNumber);
				}

			}
			// a packet times out
			else {
				// retransmit timed out packet
				SRPacket retransmitPacket = window.remove(0);
				retransmitPacket.setSentTime();
				serverSocket.send(retransmitPacket.getDatagramPacket());
				window.add(retransmitPacket);
			}
		}
	}

	// Processes an ACK. Finds where the packet is in window then ACKs it.
	// Then it runs a while loop which will remove all the consecutive ACKed
	// packets at the front of the window.
	public void processACK(int sequenceNumber) {
		for (SRPacket packet : window) {
			if (packet.getSequenceNumber() == sequenceNumber) {
				packet.ack();
				while(window.get(0).isAcked()) {
					window.remove(0);
				}
			}
		}
	}

	// Processes a NAK. Finds where the NAK'd packet is in the window
	// then removes it from the window. Then it retransmitts the packet
	// and adds it back to the end of the window.
	public void processNAK(int sequenceNumber) throws Exception{
		for (int i = 0; i < window.size(); i++) {
			if (window.get(i).getSequenceNumber() == sequenceNumber) {
				SRPacket retransmitPacket = window.remove(i);
				retransmitPacket.setSentTime();
				serverSocket.send(retransmitPacket.getDatagramPacket());
				window.add(retransmitPacket);
			}
		}
	}


	// returns true if ACK is present, returns false otherwise
	public boolean parseACK(DatagramPacket packetIn) {
		String packetData = new String(packetIn.getData());
		String[] ackData = packetData.split(" ");
		if(ackData[0].equals("ACK:")) {
			return true;
		}
		return false;
	}
}
