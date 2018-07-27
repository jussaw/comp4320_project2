package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.nio.file.Files;
import java.util.Collections;
public class UDPClient {
public static final String FILE_NAME = "TestFile.html";
public static final int PORT_NUMBER = 10025;
    public static void main(String args[]) throws Exception
    {

      //create client socket
      DatagramSocket clientSocket = new DatagramSocket();

      //translate hostname to IP address USING DNS
      InetAddress IPAddress = InetAddress.getByName("tux055");

      byte[] sendData = new byte[1024];

      IGremlin gremlin = new GremlinImpl();
      IErrorDetection errorDetec = new ErrorDetectionImpl();
      IAssembler assembler = new AssemblerImpl();

      float probabilityOfError = Float.parseFloat(args[0]);
      float probabilityOfLoss = Float.parseFloat(args[1]);

      sendData = new String("GET " + FILE_NAME + " HTTP/1.0").getBytes();

      //create datagram with data-to-send, length, IP addr, port
      DatagramPacket sendPacket =
         new DatagramPacket(sendData, sendData.length, IPAddress, PORT_NUMBER);

      //send datagram to server
      clientSocket.send(sendPacket);
      System.out.println("Client sent HTTP request");

      //prepare to receive packets
      while(!assembler.isComplete()) {
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        //read datagram from server
        clientSocket.receive(receivePacket);

        String receivedData = new String(receivePacket.getData());
        receivePacket.setLength(receivedData.length());

        DatagramPacket gremlinedPacket = gremlin.corruptPacket(receivePacket, probabilityOfError, probabilityOfLoss);

        if (gremlinedPacket != null && errorDetec.detectErrors(gremlinedPacket)) {
          System.out.println("Packet error occured");
          sendNAK(SRPacket.parseSequenceNumber(receivePacket), clientSocket, IPAddress, PORT_NUMBER);
        } else if (gremlinedPacket != null) {
            sendACK(SRPacket.parseSequenceNumber(receivePacket), clientSocket, IPAddress, PORT_NUMBER);
            assembler.newPacketIn(gremlinedPacket);
        } else {
          // packet loss, do nothing
        }

        //writeDataToFile(assembler.getAssembledDocument(), FILE_NAME);
      }
      writeDataToFile(assembler.getAssembledDocument(), FILE_NAME);
    }


    public static boolean writeDataToFile(byte[] data, String fileName) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
            out.print(new String(data));
            out.flush();
            out.close();
            System.out.println("Wrote data to " + FILE_NAME);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void sendNAK(int sequenceNumber, DatagramSocket clientSocket, InetAddress senderAddr, int port) throws IOException {
      String sendData = "NAK: " + Integer.toString(sequenceNumber);
      DatagramPacket nakPacket = new DatagramPacket(sendData.getBytes(), sendData.length(), senderAddr, port);
      clientSocket.send(nakPacket);
      System.out.println("Sending NAK: " + sequenceNumber);
    }
    public static void sendACK(int sequenceNumber, DatagramSocket clientSocket, InetAddress senderAddr, int port) throws IOException {
      String sendData = "ACK: " + Integer.toString(sequenceNumber);
      DatagramPacket ackPacket = new DatagramPacket(sendData.getBytes(), sendData.length(), senderAddr, port);
      clientSocket.send(ackPacket);
      System.out.println("Sending ACK: " + sequenceNumber);
    }
}
