package client;
import java.net.DatagramPacket;
import java.util.Vector;
public class SelectiveRepeatImpl implements ISelectiveRepeat {
   public DatagramPacket rcvSocket;
   
   public void transmit(Vector<DatagramPacket> packetsToSend) {
      //receiver buffer
      //sends/receives packets
      for(DatagramPacket pts : packetsToSend){
         //if buffer is not full, send packet, add to buffer
         if(rcvBuffer.size() < window){
            rcvBuffer.add(pts);
            //rcvSocket.send(pts);
         }else{ //if buffer is full, receive ack/nak
            //rcvSocket.receive(ack);
            //if ack, move window
            if(true){ //pts.get(msg) == ack){
               rcvBuffer.removeElement(pts);//double check
               rcvBuffer.trimToSize();
               rcvBuffer.setSize(window);
            //if nak, add nak to buffer   
            }else if(false){//pts.get(msg) == nak){
               //rcvBuffer[pts] = nak;
               timeIt(pts);
            //if nothing received, start timeout timer   
            }else{
               // startTimeoutTimer();
            }
         }
      } 
   }
   
   public void timeIt(DatagramPacket nakPacket){
      //timer.stop();
      //rcvSocket.send(nakPacket);
      //timer.update();
   }
   
}