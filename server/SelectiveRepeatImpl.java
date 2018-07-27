package server;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Vector;
import java.util.Date;
import java.io.IOException;
import java.net.SocketException;
public class SelectiveRepeatImpl implements ISelectiveRepeat {

	public DatagramSocket serverSocket;
	public static final int WINDOW_SIZE = 8;
	public static final long TIMEOUT_INTERVAL = 100;
	public static final int PORT_NUMBER = 10025;

	// initialize array of SRPacket based on packetsToSend
	Vector<SRPacket> allPackets;
	Vector<SRPacket> window;

	public SelectiveRepeatImpl(DatagramSocket serverSocket) throws SocketException {
		this.serverSocket = serverSocket;
		this.serverSocket.setSoTimeout(100);
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

		int numOfAcks = 0;
		int numOfPackets = allPackets.size();
		boolean[] packetsAlreadyAcked = new boolean[numOfPackets];
		for (boolean packetAcked : packetsAlreadyAcked) {
			packetAcked = false;
		}
		// begin sending packets
		while (allPackets.size() > 0 || numOfAcks < numOfPackets) {
			// if there is room in the window send a packet
			//System.out.println("allPackets size = " + allPackets.size());
			//System.out.println("window size = " + window.size());
			if (window.size() < WINDOW_SIZE && allPackets.size() > 0) {

				SRPacket packet = allPackets.remove(0);
				packet.setSentTime();
				System.out.println("Sending packet:" + packet.getSequenceNumber());
				serverSocket.send(packet.getDatagramPacket());
				window.add(packet);
			}
			// if there isn't room in the window and the packet hasn't timed out, wait to receive ACK/NAK
			else if (window.size() > 0 && !window.get(0).isAcked() && new Date().getTime() - window.get(0).getSentTime() < TIMEOUT_INTERVAL) {
				try {
					byte[] receiveBuf = new byte[1024];
					DatagramPacket ackPacket = new DatagramPacket(receiveBuf, receiveBuf.length);
					serverSocket.receive(ackPacket);

					// If ACK received then process ACK, elseprocess NAK
					String data = new String(ackPacket.getData()).trim();
					String sequenceNumberString = data.split(" ")[1].trim();
					//System.out.println("data length = "+ data.length());
					//System.out.println("data = " + data);
					//System.out.println("sequenceNumberString = " + sequenceNumberString);
					int sequenceNumber = Integer.parseInt(sequenceNumberString);
					if (parseACK(ackPacket)) {
						processACK(sequenceNumber);
						if (packetsAlreadyAcked[sequenceNumber] == false) {
							packetsAlreadyAcked[sequenceNumber] = true;
							numOfAcks++;
						}
					}
					else {
						processNAK(sequenceNumber);
					}
				} catch (IOException ioe) {
					System.out.println("Timed out waiting for ACK/NAK");
				}
			}
			// a packet times out
			else if (window.size() > 0){
				//System.out.println("Packet timed out");

				// retransmit timed out packet
				SRPacket retransmitPacket = window.remove(0);
				retransmitPacket.setSentTime();
				serverSocket.send(retransmitPacket.getDatagramPacket());
				window.add(retransmitPacket);
			}
			//System.out.println("numOfPackets = " + numOfPackets);
			//System.out.println("numOfAcks = " + numOfAcks);
		}
	}

	// Processes an ACK. Finds where the packet is in window then ACKs it.
	// Then it runs a while loop which will remove all the consecutive ACKed
	// packets at the front of the window.
	public void processACK(int sequenceNumber) {
		for (int i = 0; i < window.size(); i++) {
			SRPacket packet = window.remove(i);
			if (packet.getSequenceNumber() == sequenceNumber) {
				packet.ack();
				window.insertElementAt(packet, i);
				while(window.get(0).isAcked()) {
					window.remove(0);
				}
			}
		}
	}

	// Processes a NAK. Finds where the NAK'd packet is in the window
	// then removes it from the window. Then it retransmitts the packet
	// and adds it back to the end of the window.
	public void processNAK(int sequenceNumber) throws Exception {
		for (int i = 0; i < window.size(); i++) {
			if (window.get(i).getSequenceNumber() == sequenceNumber) {
				System.out.println("Retransmitting NAK");
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
