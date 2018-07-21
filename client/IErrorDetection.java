package client;
import java.net.DatagramPacket;
public interface IErrorDetection {
	// checks the validity of a packet given a checksum
	boolean detectErrors(DatagramPacket packet);
}