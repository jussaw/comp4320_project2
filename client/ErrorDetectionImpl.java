package client;
import java.net.DatagramPacket;
public class ErrorDetectionImpl implements IErrorDetection {
    // checks the validity of a packet given a checksum
    ErrorDetectionImpl() {
        this.numberOfCorruptedPackets = 0;
    }
    public int numberOfCorruptedPackets;
    public boolean detectErrors(DatagramPacket packet) {
        return false;
    }
}
