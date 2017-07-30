package grafo;

import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import grafo.*;

public class Server {
	public static GrafoHandler 		handler;
	public static grafo.Processor 	processor;
	
	public static void main (String args []) {
		try {
			int id = Integer.parseInt(args[0]);
			handler = new GrafoHandler(id);
			processor = new grafo.Processor(handler);
			
			int porta = Integer.parseInt(args[1]);
			
			System.out.println("ID = " + id + " PORTA = " + porta);
			TServerTransport serverTransport = new TServerSocket(porta);
		    TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
		 
			System.out.println("Starting the simple server...");
			server.serve();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
