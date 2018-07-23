package server;
import java.net.DatagramPacket;

public interface ISelectiveRepeat { 
	public void transmit(DatagramPacket[] packetsToSend);
}

