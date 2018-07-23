package server;
import java.util.HashTable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public interface ISelectiveRepeat { 
	public int windowBase;
	public int topOfWindow;
	public DatagramSocket serverSocket;
	public void transmit(DatagramPacket[] packetsToSend);
}