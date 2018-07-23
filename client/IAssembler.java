package client;
import java.net.DatagramPacket;
import java.util.ArrayList;
public interface IAssembler {
	public void newPacketIn(DatagramPacket newPacket);
	public int getSequenceNumber(DatagramPacket packet);
	public byte[] getAssembledDocument();
	public boolean isComplete();
}