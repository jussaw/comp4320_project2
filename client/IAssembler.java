package client;
import java.net.DatagramPacket;
import java.util.ArrayList;
public interface IAssembler {
	public void newPacketIn(DatagramPacket newPacket);
	public byte[] getAssembledDocument();
	public boolean isComplete();
}