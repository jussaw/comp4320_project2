package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.nio.file.Files;
public class UDPClient {
public static final String FILE_NAME = "TestFile.html";
public static final int PORT_NUMBER = 10024;
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
  
      sendData = new String("GET " + FILE_NAME + " HTTP/1.0").getBytes();

      //create datagram with data-to-send, length, IP addr, port
      DatagramPacket sendPacket = 
         new DatagramPacket(sendData, sendData.length, IPAddress, PORT_NUMBER);
      DatagramPacket gremlinedPacket;
      boolean isCorrupt = false;

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
        DatagramPacket gremlinedPacket = gremlin.corruptPackets(receivePacket, probabilityOfError);
        if (gremlinedPacket == null || errorDetec.detectErrors(gremlinedPacket)) {
          System.out.println("Packet error occured.");
          sendNAK(errorDetec.getSequenceNumber(receivePacket), clientSocket, IPAddress, PORT_NUMBER);
        } else {
        	sendACK(errorDetec.getSequenceNumber(receivePacket), clientSocket, IPAddress, PORT_NUMBER);
        	assembler.newPacketIn(gremlinedPacket);
        }
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

    public void sendNAK(int sequenceNumber, DatagramSocket clientSocket, InetAddress senderAddr, int port) {
      String sendData = "NAK: " + sequenceNumber;
      DatagramPacket nakPacket = new DatagramPacket(sendData.getBytes(), sendData.length(), senderAddr, port);
      clientSocket.send(nakPacket);
    }
    public void sendACK(int sequenceNumber, DatagramSocket clientSocket, InetAddress senderAddr, int port) {
      String sendData = "ACK: " + sequenceNumber;
      DatagramPacket ackPacket = new DatagramPacket(sendData.getBytes(), sendData.length(), senderAddr, port);
      clientSocket.send(ackPacket);
    }
}