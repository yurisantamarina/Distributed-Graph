package grafo;

import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import grafo.*;

public class Server2 {
	public static GrafoHandler 		handler;
	public static grafo.Processor 	processor;
	
	public static void main (String args []) {
		try {
			handler = new GrafoHandler(2);
			processor = new grafo.Processor(handler);
		
			TServerTransport serverTransport = new TServerSocket(5001);
		    TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
		 
			System.out.println("Starting the simple server...");
			server.serve();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
