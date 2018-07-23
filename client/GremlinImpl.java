package client;
import java.net.DatagramPacket;
import java.util.Random;

public class GremlinImpl implements IGremlin {
   // corrupts a packet if the provided probability is met
	GremlinImpl() {

	}
	public DatagramPacket corruptPacket(DatagramPacket packet, float corrupProb, float lossProb) {
		// Sets probability of loss to be between probability of corruption and 1.
		float newLossProb = lossProb + corrupProb;

		/* Makes new Random object
		 * Makes new float randCorruptDamageCorrect which will determine whether to
		 * corrupt the packet, lose the packet, or do nothing to the packet.
		 * Makes a new DatagramPacket which will store the corrupted version of packet.
		 */
		Random rand = new Random();
		float randCorruptDamageCorrect = rand.nextFloat();
		DatagramPacket corruptedPacket = packet;

		// If statement to corrupt packet
		if(randCorruptDamageCorrect < corrupProb) {
			// Makes float to check check how many bits to corrupt.
			float randHowCorruptPacket = rand.nextFloat();
			// Corrupt 1 bit.
			if(randHowCorruptPacket >= 0 && randHowCorruptPacket < .5) {
				corruptedPacket = makeCorruption(packet, 1);
			}
			// Corrupt 2 bits.
			if(randHowCorruptPacket >= .5 && randHowCorruptPacket < .8) {
				corruptedPacket = makeCorruption(packet, 2);
			}
			// Corrupt 3 bits.
			if(randHowCorruptPacket >= .8 && randHowCorruptPacket <= 1) {
				corruptedPacket = makeCorruption(packet, 3);
			}
		}

		//If statement to allow packet loss to occur
		if(randCorruptDamageCorrect >= corrupProb && randCorruptDamageCorrect < newLossProb) {
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
