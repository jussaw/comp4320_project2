package server;
import java.io.*;
import java.net.*; 
import java.io.FileInputStream;
import java.util.ArrayList;
import java.nio.file.Files;

public class UDPServer {
  public static void main(String args[]) throws Exception 
    { 
      //create datagram socket at port 10025
      DatagramSocket serverSocket = new DatagramSocket(10024);
  
      byte[] receiveData = new byte[1024]; 
      byte[] sendData  = new byte[1024]; 
  
      while(true) 
        { 
        //create space for recieved datagram
          DatagramPacket receivePacket = 
             new DatagramPacket(receiveData, receiveData.length); 
           serverSocket.receive(receivePacket); 

        //recieve datagram
            String request = new String(receivePacket.getData()); 
  
        //Get IP addr port #, of sender
          InetAddress IPAddress = receivePacket.getAddress(); 
        //^
          int port = receivePacket.getPort(); 

          // process http request
          String[] requestLines = request.split("\r\n");
          String[] requestArgs = requestLines[0].split(" ");
          switch(requestArgs[0]) {
            case "GET": sendData = get(requestArgs[1]);
              ISegmentation segmentor = new SegmentationImpl();
              ISelectiveRepeat selectiveRepeat = new SelectiveRepeatImpl(serverSocket);
              String data = "HTTP/1.0 200 Document Follows\r\n"
                  + "Content-Type: text/plain\r\n"
                  + "Content-Length: " + sendData.length + "\r\n\r\n"
                  + new String(sendData);
              DatagramPacket[] packetsToSend = segmentor.segmentPackets(data.getBytes(), 512);
              selectiveRepeat.transmit(packetsToSend, IPAddress, port);
              break;
            default: System.out.println("Error: Invalid request method " + requestArgs[1]);
              break;
          }  
        }
        //end of while loop, loop pack and wait for another datagram 
    }

    public static byte[] get(String fileName) {
      try {
        return Files.readAllBytes(new File(fileName).toPath());
      } catch (IOException ioe) {
        System.out.println("Failed to open file " + fileName);
        System.out.println(ioe);
      }
      byte[] ret = {0};
      return ret;
    }

}  