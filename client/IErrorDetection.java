package client;
import java.net.DatagramPacket;
public interface IErrorDetection {
	// checks the validity of a packet given a hash/checksum
	boolean detectErrors(DatagramPacket packet);
}