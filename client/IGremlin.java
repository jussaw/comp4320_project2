package client;
import java.net.DatagramPacket;

public interface IGremlin {
	// corrupts a packet if the provided probability is met
   DatagramPacket corruptPacket(DatagramPacket packet, float corrupProb, float lossProb);
   int pcktSz = 256;
   //int corruptsLeft = -1;
}