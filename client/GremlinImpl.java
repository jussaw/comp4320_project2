package client;
import java.net.DatagramPacket;
import java.util.Random;

public class GremlinImpl implements IGremlin {
   // corrupts a packet if the provided probability is met
	GremlinImpl() {

	}
	public DatagramPacket corruptPacket(DatagramPacket packet, float corrupProb, float lossProb) {

		return new DatagramPacket(new byte[], 0);
	}

	public DatagramPacket makeCorruption(DatagramPacket packetIn, int numOfPacketsToCorrupt) {

		return new DatagramPacket(new byte[], 0);
	}
