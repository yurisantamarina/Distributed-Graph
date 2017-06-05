import java.io.*;
import java.net.*;
public class DatagramOutputStream extends ByteArrayOutputStream {
  DatagramSocket socket;
  DatagramPacket packet;
  
  public DatagramOutputStream
  (DatagramSocket socket, InetAddress address, int port) {
    this (socket, address, port, 512);
  }
  public DatagramOutputStream
  (DatagramSocket socket, InetAddress address, int port, int initialSize) {
    super (initialSize);
    this.socket = socket;
    packet = new DatagramPacket (buf, 0, address, port);
  }
  public synchronized void flush () throws IOException {
    if (count >= 65508)
      throw new IOException ("Packet overflow (" + count + ") bytes");
    packet.setData (buf, 0, count);
    socket.send (packet);
    reset ();
  }
}
