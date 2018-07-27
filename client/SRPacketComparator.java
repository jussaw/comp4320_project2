package client;
import java.util.Comparator;
import java.net.DatagramPacket;

public class SRPacketComparator implements Comparator<DatagramPacket> {


   public SRPacketComparator() {

   }
   public int compare(DatagramPacket a, DatagramPacket b) {

      return SRPacket.parseSequenceNumber(a) - SRPacket.parseSequenceNumber(b);
   }
   public boolean equals(DatagramPacket a, DatagramPacket b) {
      return SRPacket.parseSequenceNumber(a) == SRPacket.parseSequenceNumber(b);
   }
}
