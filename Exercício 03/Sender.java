import java.io.*;
import java.net.*;
import java.util.*;

public class Sender {
  public static void main(String[] args) {
    DatagramSocket socket = null;
    DatagramPacket outPacket = null;
    MulticastSocket socketM = null;
    DatagramPacket inPacket = null;
    
    byte[] inBuf = new byte[256];
    byte[] outBuf;
    final int PORT = 8888;
	Scanner sc = new Scanner(System.in);
    try {
      socket = new DatagramSocket();
      long counter = 0;
      String msg;
	  socketM = new MulticastSocket(8888);
      InetAddress address = InetAddress.getByName("224.2.2.3");
      socketM.joinGroup(address);
      while (true) {
		
		
        msg = sc.nextLine();
        outBuf = msg.getBytes();
 
        outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
 
        socket.send(outPacket);
 
        System.out.println("Enviando : " + msg);
        
        
        inPacket = new DatagramPacket(inBuf, inBuf.length);  
		socketM.receive(inPacket);
		msg = new String(inBuf, 0, inPacket.getLength());
		System.out.println("Recebido de " + inPacket.getAddress() + " Msg : " + msg);
        try {
          Thread.sleep(500);
        } catch (InterruptedException ie) {
        }
      }
    } catch (IOException ioe) {
      System.out.println(ioe);
    }
  }
}
