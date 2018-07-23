package server;
import java.util.Hashtable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
public interface ISelectiveRepeat {
	public int windowBase = 0;
	public int topOfWindow = 0;
	//public DatagramSocket serverSocket = new DatagramSocket();
	public void transmit(DatagramPacket[] packetsToSend);
}
