package server;
import java.net.DatagramPacket;
import java.net.InetAddress;
public interface ISelectiveRepeat { 
	public void transmit(DatagramPacket[] packetsToSend, InetAddress clientIp, int clientPort) throws Exception;
}

