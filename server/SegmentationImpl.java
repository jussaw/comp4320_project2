package server;
import java.net.DatagramPacket;
import java.util.ArrayList;

public class SegmentationImpl implements ISegmentation {
    SegmentationImpl(){
    }
    // accepts data in bytes, converts into equally sized packets
    public DatagramPacket[] segmentPackets(byte[] data, int packetSize) {
        /* Calculate the number of empty items there will be in the new array
         * if necessary where the new array is a size that is a multiple
         * of packetSize
         */
        int remainingItems = 0;
        if(data.length % packetSize != 0){
            remainingItems = packetSize - (data.length % packetSize);
        }

        /* Creates a new byte array, dataNew, which is just a copy of data,
         * but it;s size is a multiple of packetSize any extra bytes are
         * extended with zeros.
         */
        byte[] dataNew = new byte[data.length +remainingItems];
        for(int i = 0; i < dataNew.length; i++){
            if(i < data.length) {
                dataNew[i] = data[i];
            } else {
                dataNew[i] = 0;
            }
        }

        /* Create new DatagramPacket aray, packetsOut, that will be
         * returned by the function. Create a new byte array, temp,
         * that will record the byte arrays stored in the packetOut.
         */
        int numberOfPackets = dataNew.length / packetSize;
        DatagramPacket[] packetsOut = new DatagramPacket[numberOfPackets];
        byte[] temp = new byte[packetSize];

        /* The outer for loop iterates through each DatagramPacket in
         * packetsOut implementing the byte array made from the inner loop
         * to each item. The inner loop assigns temp to byte arrays made up
         * all the bytes in data.
         */
        for(int i = 0; i < numberOfPackets; i++){
            for(int j = 0; j < packetSize; j++){
                //add code for out of bounds of data when copying to temp
                temp[j] = dataNew[(i*packetSize) + j];
            }
            byte[] dataWithHeaders = this.includeHeaderLines(temp, i);
            packetsOut[i] = new DatagramPacket(dataWithHeaders, dataWithHeaders.length);
            System.out.println("Created segment packet: " + new String(packetsOut[i].getData()));
        }
        ArrayList<DatagramPacket> packetsOutAL = new ArrayList<DatagramPacket>();
        for (DatagramPacket packet : packetsOut) {
          packetsOutAL.add(packet);
        }

        byte[] nullData = {0};
        nullData = includeHeaderLines(nullData, packetsOut.length);
        DatagramPacket nullPacket = new DatagramPacket(nullData, nullData.length);
        packetsOutAL.add(nullPacket);

        packetsOut = new DatagramPacket[packetsOutAL.size()];
        for(int i = 0; i < packetsOutAL.size(); i++) {
          packetsOut[i] = packetsOutAL.get(i);
        }

        return packetsOut;
    }

    public int calculateChecksum(byte[] buf) {
        int sum = 0;
        for (byte b : buf) {
            sum += new Byte(b).intValue();
        }
        return sum;
    }

    // adds checksum and sequence number to data buffer
    public byte[] includeHeaderLines(byte[] buf, int sequenceNumber) {
        String str = new String(buf);
        int sequenceIndex = sequenceNumber / 24;
        int moduloSequence = sequenceNumber % 24;
        str = "Checksum: " + this.calculateChecksum(buf) + "\r\n"
                + "Sequence-Index: " + sequenceIndex + "\r\n"
                + "Sequence-Number: " + moduloSequence +"\r\n\r\n"
                + str;
        System.out.println(str);
        return str.getBytes();
    }

}
