import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
public class MulticastChat {
  static final String DEFAULT_GROUP = "239.1.2.3";
  static final int DEFAULT_PORT = 1234;
  static final int DEFAULT_TTL = 1;
  
  InetAddress group;
  int port;
  int ttl;
  
  public MulticastChat (InetAddress group, int port, int ttl) {
    this.group = group;
    this.port = port;
    this.ttl = ttl;
    initAWT ();
  }
  Frame frame;
  TextArea area;
  TextField field;
  public void initAWT () {
    frame = new Frame
      ("MulticastChat [" + group.getHostAddress () + ":" + port + "]");
    frame.addWindowListener (new WindowAdapter () {
      public void windowOpened (WindowEvent event) {
        field.requestFocus ();
      }
      public void windowClosing (WindowEvent event) {
        try {
          stop ();
        } catch (IOException ignored) {
        }
      }
    });
    area = new TextArea ("", 12, 24, TextArea.SCROLLBARS_VERTICAL_ONLY);
    area.setEditable (false);
    frame.add (area, "Center");
    field = new TextField ("");
    field.addActionListener (new ActionListener () {
      public void actionPerformed (ActionEvent event) {
        netSend (event.getActionCommand ());
        field.selectAll ();
      }
    });
    frame.add (field, "South");
    frame.pack ();
  }
      
  public void start () throws IOException {
    netStart ();
    frame.setVisible (true);
  }
  public void stop () throws IOException {
    netStop ();
    frame.setVisible (false);
  }
  MulticastSocket socket;
  BufferedReader in;
  OutputStreamWriter out;
  Thread listener;
  void netStart () throws IOException {
    socket = new MulticastSocket (port);
    socket.setTimeToLive (ttl);
    socket.joinGroup (group);
    in = new BufferedReader
      (new InputStreamReader (new DatagramInputStream (socket), "UTF8"));
    out =
      new OutputStreamWriter (new DatagramOutputStream (socket, group, port), "UTF8");
    listener = new Thread () {
      public void run () {
        netReceive ();
      }
    };
    listener.start ();
  }
  void netStop () throws IOException {
    listener.interrupt ();
    listener = null;
    socket.leaveGroup (group);
    socket.close ();
  }
  void netSend (String message) {
    try {
      out.write (message + "\n");
      out.flush ();
    } catch (IOException ex) {
      ex.printStackTrace ();
    }
  }
  void netReceive () {
    try {
      Thread myself = Thread.currentThread ();
      while (listener == myself) {
        String message = in.readLine ();
        area.append (message + "\n");
      }
    } catch (IOException ex) {
      area.append ("- listener stopped");
      ex.printStackTrace ();
    }
  }
  public static void main (String[] args) throws IOException {
    if ((args.length > 3) || ((args.length > 0) && args[1].endsWith ("help"))) {
      System.out.println
        ("Syntax: MulticastChat [<group:" + DEFAULT_GROUP +
         "> [<port:" + DEFAULT_PORT + ">] [<ttl:" + DEFAULT_TTL + ">]]");
      System.exit (0);
    }
    String groupStr = (args.length > 0) ? args[0] : DEFAULT_GROUP;
    InetAddress group = InetAddress.getByName (groupStr);
    int port = (args.length > 1) ? Integer.parseInt (args[1]) : DEFAULT_PORT;
    int ttl = (args.length > 2) ? Integer.parseInt (args[2]) : DEFAULT_TTL;
    MulticastChat chat = new MulticastChat (group, port, ttl);
    chat.start ();
  }
}
