package client;
import java.util.Hashtable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Vector;
public interface ISelectiveRepeat {
   public int window = 8;
   public DatagramSocket serverSocket = null;
   public Vector<DatagramPacket> rcvBuffer = new Vector<DatagramPacket>(8);
   public void transmit(Vector<DatagramPacket> packetsToSend);
   public void timeIt(DatagramPacket nakPacket);
}
