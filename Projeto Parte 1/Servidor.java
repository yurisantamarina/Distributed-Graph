package trabalhopt1;
 
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
 
// Generated code
import trabalhopt1.*;
 
import java.util.HashMap;
 
public class Servidor {
 
	public static Grafo handler;
 
	public static trabalhopt1.Processor processor;

	public static void main(String [] args) {
		try {
			handler = new Grafo();
			processor = new trabalhopt1.Processor(handler);
 
			TServerTransport serverTransport = new TServerSocket(9090);
			TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
 
			System.out.println("Starting...");
			server.serve();
 
		}catch (Exception x){
			x.printStackTrace();
         }
	}

}

