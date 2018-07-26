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

	/*public DatagramPacket makeCorruption(DatagramPacket packetIn, int numOfPacketsToCorrupt) {
		Random rand = new Random();
		int iter = numOfPacketsToCorrupt;
		int bitToCorrupt;
		int[] alreadyCorrupted = {1000, 1000, 1000};
		byte[] corruptedBytes = packetIn.getData();
		boolean alreadyCorruptedBool = false;
		DatagramPacket corruptedPacket = packetIn;

		while(iter > 0) {
			System.out.println("corruption num: " + iter);
			alreadyCorruptedBool = false;
			//for (int num : alreadyCorrupted) {
				//if(num < 1000){
				if (alreadyCorrupted[iter - 1] < 1000)
					alreadyCorruptedBool = true;
				}
			//}
			if(!alreadyCorruptedBool){
				bitToCorrupt = rand.nextInt(512);
				alreadyCorrupted[iter - 1] = bitToCorrupt;
				corruptedBytes[bitToCorrupt]++;
				corruptedPacket.setData(corruptedBytes);
				iter--;
			}
		}
		return corruptedPacket;
	}*/
	public DatagramPacket makeCorruption(DatagramPacket packetIn, int numOfPacketsToCorrupt) {
        Random rand = new Random();
        int iter = numOfPacketsToCorrupt;
        int bitToCorrupt;
        byte[] corruptedBytes = packetIn.getData();
        boolean[] alreadyCorrupted = new boolean[corruptedBytes.length] ;
        boolean alreadyCorruptedBool = false;
        DatagramPacket corruptedPacket = packetIn;

				for (boolean packetBools : alreadyCorrupted) {
					packetBools = false;
				}

        for(int i = 0; i < iter; i++) {
					System.out.println("corruption num: " + iter);
          bitToCorrupt = rand.nextInt(411) + 50;
          while(alreadyCorrupted[bitToCorrupt]) {
              bitToCorrupt = rand.nextInt(411) + 50;
          }
          corruptedBytes[bitToCorrupt]++;
          alreadyCorrupted[bitToCorrupt] = true;
          corruptedPacket.setData(corruptedBytes);
        }
        return corruptedPacket;
    }
}
