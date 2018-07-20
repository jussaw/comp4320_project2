package server;
import java.net.DatagramPacket;
import java.util.ArrayList;

public class SegmentationImpl implements ISegmentation {
    SegmentationImpl(){
    }
    // accepts data in bytes, converts into equally sized packets
    public DatagramPacket[] segmentPackets(byte[] data, int packetSize) {
    }
    
    public int calculateChecksum(byte[] buf) {
        return 0;
    }
    // adds checksum and sequence number to data buffer
    public byte[] includeHeaderLines(byte[] buf, int sequenceNumber) {
        return new byte[];
    }

}