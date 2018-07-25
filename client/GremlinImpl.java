package client;
import java.net.DatagramPacket;
import java.util.Random;

public class GremlinImpl implements IGremlin {
   // corrupts a packet if the provided probability is met
	GremlinImpl() {

	}
	public DatagramPacket corruptPacket(DatagramPacket packet, float corrupProb, float lossProb) {

		Random rand = new Random();
		float randCorruptDamageCorrect = rand.nextFloat();
		DatagramPacket corruptedPacket = packet;

		if(randCorruptDamageCorrect < corrupProb) {
			float randHowCorruptPacket = rand.nextFloat();
			if(randHowCorruptPacket >= 0 && randHowCorruptPacket < .5) {
				corruptedPacket = makeCorruption(packet, 1);
			}
			if(randHowCorruptPacket >= .5 && randHowCorruptPacket < .8) {
				corruptedPacket = makeCorruption(packet, 2);
			}
			if(randHowCorruptPacket >= .8 && randHowCorruptPacket <= 1) {
				corruptedPacket = makeCorruption(packet, 3);
			}
		}
		if(randCorruptDamageCorrect >= corrupProb && randCorruptDamageCorrect < lossProb) { // what was this?
			return null;
		}
		return corruptedPacket;
	}

	public DatagramPacket makeCorruption(DatagramPacket packetIn, int numOfPacketsToCorrupt) {
		Random rand = new Random();
		int iter = numOfPacketsToCorrupt, bitToCorrupt;
		int[] alreadyCorrupted = {500, 500, 500};
		byte[] corruptedBytes = packetIn.getData();
		boolean alreadyCorruptedBool = false;
		DatagramPacket corruptedPacket = packetIn;

		while(iter > 0) {
			alreadyCorruptedBool = false;
			for (int num : alreadyCorrupted) {
				if(num < 500){
					alreadyCorruptedBool = true;
				}
			}

			if(!alreadyCorruptedBool){
				bitToCorrupt = rand.nextInt(256);
				alreadyCorrupted[iter - 1] = bitToCorrupt;
				corruptedBytes[bitToCorrupt]++;
				corruptedPacket.setData(corruptedBytes);
				iter--;
			}
		}
		return corruptedPacket;
	}
}
