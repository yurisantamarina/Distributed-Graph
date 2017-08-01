package grafo;

import io.atomix.catalyst.transport.Address;
import io.atomix.copycat.client.ConnectionStrategies;
import io.atomix.copycat.client.CopycatClient;
import io.atomix.copycat.client.CopycatClient.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grafo.*;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

import br.ufu.get.GetExisteAresta;
import br.ufu.get.GetExisteVertice;
import br.ufu.get.GetGetCusto;
import br.ufu.get.GetListaDeVizinhos;
import br.ufu.get.GetPrintarDadosAresta;
import br.ufu.get.GetPrintarDadosVertice;
import br.ufu.get.GetTemVizinho;
import br.ufu.put.PutAdicionarArestaPeloVertice;
import br.ufu.put.PutAlterarCorVertice;
import br.ufu.put.PutAlterarDescricaoAresta;
import br.ufu.put.PutAlterarDescricaoVertice;
import br.ufu.put.PutAlterarPesoAresta;
import br.ufu.put.PutAlterarPesoVertice;
import br.ufu.put.PutCopia;
import br.ufu.put.PutCriarAresta;
import br.ufu.put.PutCriarVertice;
import br.ufu.put.PutDeletarArestaPeloVertice;
import br.ufu.put.PutDeletarVertice;
import br.ufu.put.PutRemoveMapArestas;
import br.ufu.put.PutRemoveMapArestasPorVertice;
//import br.ufu.putcommand.PutCommand;
//import br.ufu.putcommand.PutCommand;
import br.ufu.put.PutSetCusto;

public class GrafoHandler implements grafo.Iface {
	boolean isModoApresentacao = true; //Variavel utilizada para que as operacao demorem um certo tempo, para a apresentacao e o professor 
	boolean conectado = false;
	int idServidor;
	
	TTransport transport0;
	TProtocol protocol0;
	grafo.Client client0;
	
	TTransport transport1;
	TProtocol protocol1;
	grafo.Client client1;
	
	CopycatClient copyCatClient;
    CopycatClient copyCatClient2;
    CopycatClient copyCatClient3;
    
	public GrafoHandler(int idServidor) {
		this.idServidor = idServidor;
	}
	
	private grafo.Client getServidor(int hash){
		if(idServidor == 0){
			if(hash == 1){
				return client0;
			}else{
				return client1;
			}
		}else if(idServidor == 1){
			if(hash == 0){
				return client0;
			}else{
				return client1;
			}
		}else{
			if(hash == 0){
				return client0;
			}else{
				return client1;
			}
		}
	}
	
	private void Conectar(){
		int porta1, porta2;
		int portaCC1, portaCC2, portaCC3;
		
		if(idServidor == 0){
			porta1 = 9091;
			porta2 = 9092;
			
			portaCC1 = 5500;
			portaCC2 = 5501;
			portaCC3 = 5502;
		}else if(idServidor == 1){
			porta1 = 9090;
			porta2 = 9092;
			
			portaCC1 = 5510;
			portaCC2 = 5511;
			portaCC3 = 5512;
		}else{
			porta1 = 9090;
			porta2 = 9091;
			
			portaCC1 = 5520;
			portaCC2 = 5521;
			portaCC3 = 5522;
		}
		
		try{
			transport0 = new TSocket("localhost", porta1);
			transport0.open();
			protocol0 = new  TBinaryProtocol(transport0);
			client0 = new grafo.Client(protocol0);
			
			transport1 = new TSocket("localhost", porta2);
			transport1.open();
			protocol1 = new  TBinaryProtocol(transport1);
			client1 = new grafo.Client(protocol1);
			conectado = true;
			System.out.println("Servidor " + this.idServidor + " conectado aos outros 2");
		}catch(TException x){
			System.out.println("NAO CRIOU");
		}
		
		try{
			copyCatClient = CopycatClient.builder(new Address("localhost", portaCC1))
	                .withConnectionStrategy(ConnectionStrategies.FIBONACCI_BACKOFF)
	                .build();
	        copyCatClient2 = CopycatClient.builder(new Address("localhost", portaCC2))
	                .withConnectionStrategy(ConnectionStrategies.FIBONACCI_BACKOFF)
	                .build();
	        copyCatClient3 = CopycatClient.builder(new Address("localhost", portaCC3))
	                .withConnectionStrategy(ConnectionStrategies.FIBONACCI_BACKOFF)
	                .build();

	        copyCatClient.connect().join();
	        copyCatClient2.connect().join();
	        copyCatClient3.connect().join();
	        
		}catch(Exception E){
			System.out.println("NAO CRIOU COPYCAT");
		}
	}

	public synchronized String criarVertice(int nome, int cor, String descricao, double peso) throws TException{
		if(!conectado) Conectar();
		int hash = (int)((new Integer(nome)).toString().hashCode()%3);
		
		String result = "";
		if(hash == this.idServidor){
			System.out.println("Inserindo vértice no servidor " + this.idServidor);
			
			
			if (!existeVertice(nome)) {
				getServidorCopycat().submit(new PutCriarVertice(nome, cor, descricao, peso)).join();
				result = "Vertice criado com sucesso.";
			} else {
				result = "O vertice ja existe!";
			}
		}else{
			result = getServidor(hash).criarVertice(nome, cor, descricao, peso);
		}
		
		return result;
	}
	
	public synchronized String alterarCorVertice(int nome, int cor)  throws TException{
		if(!conectado) Conectar();
		int hash = (int)((new Integer(nome)).toString().hashCode()%3);
		
		String result = "";
		if(hash == this.idServidor){
			System.out.println("Alterando cor do vertice no servidor " + this.idServidor);
			if (existeVertice(nome)) {
				getServidorCopycat().submit(new PutAlterarCorVertice(nome, cor)).join();
				result = "Cor alterada com sucesso!";
			} else {
				result = "O vertice nao existe!";
			}
		}else{
			result = getServidor(hash).alterarCorVertice(nome, cor);
		}
		
		return result;
	}
	
	public synchronized String alterarDescricaoVertice(int nome, String descricao)  throws TException{
		if(!conectado) Conectar();
		int hash = (int)((new Integer(nome)).toString().hashCode()%3);
		
		String result = "";
		if(hash == this.idServidor){
			System.out.println("Alterando descricao do vertice no servidor " + this.idServidor);
			if (existeVertice(nome)) {
				getServidorCopycat().submit(new PutAlterarDescricaoVertice(nome, descricao)).join();
				result = "Descricao alterada com sucesso!";
			} else {
				result = "O vertice nao existe!";
			}
		}else{
			result = getServidor(hash).alterarDescricaoVertice(nome, descricao);
		}
		
		return result;
	}
	
	public synchronized String alterarPesoVertice(int nome, double peso)  throws TException{
		if(!conectado) Conectar();
		int hash = (int)((new Integer(nome)).toString().hashCode()%3);
		
		String result = "";
		if(hash==this.idServidor){
			System.out.println("Alterando peso do vertice no servidor " + this.idServidor);
			if (existeVertice(nome)) {
				getServidorCopycat().submit(new PutAlterarPesoVertice(nome, peso)).join();
				result = "Peso alterado com sucesso!";
			} else {
				result = "O vertice nao existe!";
			}
		}else{
			result = getServidor(hash).alterarPesoVertice(nome, peso);
		}
			
		return result;
	}
	
	public synchronized String consultarVertice(int nome)  throws TException{
		if(!conectado) Conectar();
		int hash = (int)((new Integer(nome)).toString().hashCode()%3);
		
		String result = "";
		if(hash==this.idServidor){
			System.out.println("Consultando vértice no servidor " + this.idServidor);
			if (existeVertice(nome)) {
				System.out.println("Existe");
				result = printarDadosVertice(nome);
			} else {
				result = "O vertice nao existe!";
			}
		}else{
			result = getServidor(hash).consultarVertice(nome);
		}
		
		return result;
	}
	
	public synchronized String deletarVertice(int nome)  throws TException{
		if(!conectado) Conectar();
		int hash = (int)((new Integer(nome)).toString().hashCode()%3);
		
		String result = "";
		if(hash==this.idServidor){
			if (existeVertice(nome)) {
				deletarArestaPeloVertice(nome);
				
				getServidorCopycat().submit(new PutDeletarVertice(nome)).join();
				result = "Vertice deletado com sucesso.";
			} else {
				result = "O vertice nao existe!";
			}
		}else{
			result = getServidor(hash).deletarVertice(nome);
		}
		
		return result;
	}
	
	public void removeMapArestasPorVertice(int vertice1, int vertice2) throws TException{
		int hash = (int)((new Integer(vertice1)).toString().hashCode()%3);
		if(hash == this.idServidor){
			boolean temVizinho = (boolean)getServidorCopycat().submit(new GetTemVizinho(vertice1)).join();
			if(temVizinho){
				getServidorCopycat().submit(new PutRemoveMapArestasPorVertice(vertice1, vertice2)).join();
			}
		}else{
			getServidor(hash).removeMapArestasPorVertice(vertice1, vertice2);
		}
	} 
	
	public void removeMapArestas(String key) throws TException{
		int hash = (int)(key.hashCode()%3);
		if(hash == this.idServidor){
			getServidorCopycat().submit(new PutRemoveMapArestas(key)).join();
		}else{
			getServidor(hash).removeMapArestas(key);
		}
	}

	public void deletarArestaPeloVertice(int vertice) throws TException{
		int hash = (int)((new Integer(vertice)).toString().hashCode()%3);
		if(hash==this.idServidor){
			boolean temVizinho = (boolean)getServidorCopycat().submit(new GetTemVizinho(vertice)).join();
			
			if (temVizinho) {
				List<Integer> listVerticesDestino = (List<Integer>)getServidorCopycat().submit(new GetListaDeVizinhos(vertice)).join();
				
				for (Integer verticeDestino : listVerticesDestino) {
					String key1 = vertice + "-" + verticeDestino;
					String key2 = verticeDestino + "-" + vertice;
					
					int cont = 0;
					
					if (existeAresta(vertice, verticeDestino)) {
						removeMapArestas(key1);
						cont++;
					} 
					
					if (existeAresta(verticeDestino, vertice)) {
						removeMapArestas(key2);
						cont++;
					}
					
					if (cont == 2) {
						removeMapArestasPorVertice(verticeDestino, vertice);
					}
				}
				
				getServidorCopycat().submit(new PutDeletarArestaPeloVertice(vertice)).join();
			}
		}else{
			getServidor(hash).deletarArestaPeloVertice(vertice);
		}
		
	}
	
	public String deletarAresta(int vertice1, int vertice2) throws TException{
		String key = vertice1 + "-" + vertice2;
		int hash = (int)(key.hashCode()%3);
		if(hash==this.idServidor){
			String result = "";
			if(existeAresta(vertice1, vertice2)){
				removeMapArestas(key);
				removeMapArestasPorVertice(vertice1, vertice2);
				result = "Aresta deletada com sucesso";
			}else{
				result = "Aresta inexistente";
			}
				
			return result;
		}else{
			return getServidor(hash).deletarAresta(vertice1, vertice2);
		}
		
	}

	public synchronized String criarAresta(int vertice1, int vertice2, double peso, boolean isBiDirecional, String descricao)  throws TException{
		if(!conectado) Conectar();
		String key = vertice1 + "-" + vertice2;
		int hash = (int)(key.hashCode()%3);
		
		String result = "";
		
		if(hash == this.idServidor){
			if (existeVertice(vertice1) && existeVertice(vertice2)) {
				if (!existeAresta(vertice1, vertice2)) {
					System.out.println("Inserindo aresta no servidor " + this.idServidor);
					
					getServidorCopycat().submit(new PutCriarAresta(vertice1, vertice2, peso, isBiDirecional, descricao)).join();
					
					adicionarArestaPeloVertice(vertice1, vertice2);
					
					result = "Aresta criada com sucesso!";
				} else {
					result = "A aresta ja existe!";
				}
			} else {
				result = "O vertice nao existe!";
			}
		}else{
			result = getServidor(hash).criarAresta(vertice1, vertice2, peso, isBiDirecional, descricao);
		}
		
		return result;
	}

	public void adicionarArestaPeloVertice(int verticeOrigem, int verticeDestino) throws TException{
		if(!conectado) Conectar();
		
		int hash = (int)((new Integer(verticeOrigem)).toString().hashCode()%3);
		
		if(hash == this.idServidor){
			getServidorCopycat().submit(new PutAdicionarArestaPeloVertice(verticeOrigem, verticeDestino)).join();
		}else{
			getServidor(hash).adicionarArestaPeloVertice(verticeOrigem, verticeDestino);
		}
	}
	
	public synchronized String alterarPesoAresta(int vertice1, int vertice2, double peso)  throws TException{
		if(!conectado) Conectar();
		
		String result = "A aresta nao existe";
		String key1 = vertice1 + "-" + vertice2;
		int hash1 = (int)(key1.hashCode()%3);
		
		String key2 = vertice2 + "-" + vertice1;
		int hash2 = (int)(key2.hashCode()%3);
		
		if (existeAresta(vertice1, vertice2)) {	
			if(hash1 == this.idServidor){
				getServidorCopycat().submit(new PutAlterarPesoAresta(vertice1, vertice2, peso)).join();
				result = "Peso alterado com sucesso!";
				
			}else{
				result = getServidor(hash1).alterarPesoAresta(vertice1, vertice2, peso);
			}
			
		} 
	    if(existeAresta(vertice2, vertice1)) {
			if(hash2 == this.idServidor){
				getServidorCopycat().submit(new PutAlterarPesoAresta(vertice2, vertice1, peso)).join();
				result = "Peso alterado com sucesso!";
			}else{
				result = getServidor(hash2).alterarPesoAresta(vertice2, vertice1, peso);
			}
		}
		
		return result;
	}
	
	public synchronized String alterarDescricaoAresta(int vertice1, int vertice2, String descricao)  throws TException{
		if(!conectado) Conectar();
		
		String result = "A aresta nao existe!";
		String key1 = vertice1 + "-" + vertice2;
		int hash1 = (int)(key1.hashCode()%3);
		
		String key2 = vertice2 + "-" + vertice1;
		int hash2 = (int)(key2.hashCode()%3);
		
		if (existeAresta(vertice1, vertice2)) {	
			if(hash1 == this.idServidor){
				getServidorCopycat().submit(new PutAlterarDescricaoAresta(vertice1, vertice2, descricao)).join();
				result = "Descricao alterada com sucesso!";
			}else{
				result = getServidor(hash1).alterarDescricaoAresta(vertice1, vertice2, descricao);
			}
			
		} 
		if(existeAresta(vertice2, vertice1)) {
			if(hash2 == this.idServidor){
				getServidorCopycat().submit(new PutAlterarDescricaoAresta(vertice2, vertice1, descricao)).join();
				result = "Descricao alterada com sucesso!";
			}else{
				result = getServidor(hash2).alterarDescricaoAresta(vertice2, vertice1, descricao);
			}
		}
		
		
		return result;
	}
	
	public synchronized String consultarAresta(int vertice1, int vertice2)  throws TException{
		if(!conectado) Conectar();
		
		String key = vertice1 + "-" + vertice2;
		int hash = (int)(key.hashCode()%3);
		
		String result = "";
		
		if(hash == this.idServidor){
			if (existeAresta(vertice1, vertice2)) {
				result = printarDadosAresta(vertice1, vertice2);
			} else {
				result = "A aresta nao existe!";
			}
		}else{
			result = getServidor(hash).consultarAresta(vertice1, vertice2);
		}
		
		
		return result;
	}
	
	public synchronized String listarVerticesDeUmaAresta(int nomeVertice1, int nomeVertice2)  throws TException{
		if(!conectado) Conectar();
		
		String key = nomeVertice1 + "-" + nomeVertice2;
		int hash = (int)(key.hashCode()%3);
		
		String result = "";
		
		if(hash == this.idServidor){
			if (existeVertice(nomeVertice1) && existeVertice(nomeVertice2)) {
				if (existeAresta(nomeVertice1, nomeVertice2)) {
					result += printarDadosVertice(nomeVertice1);
					
					result += printarDadosVertice(nomeVertice2);
					
				} else {
					result = "A aresta nao existe!";
				}
			} else {
				result = "O vertice nao existe!";
			}
		}else{
			result = getServidor(hash).listarVerticesDeUmaAresta(nomeVertice1, nomeVertice2);
		}
		
		return result;
	}
	
	public synchronized String listarArestasDeUmVertice(int nomeVertice1)  throws TException{
		if(!conectado) Conectar();
		
		int hash = (int)((new Integer(nomeVertice1)).toString().hashCode()%3);
		String result = "";
		
		if(hash == this.idServidor){
			if (existeVertice(nomeVertice1)) {
				System.out.println("Estou no servidor " + this.idServidor + " pegando as arestas de " + nomeVertice1);
				boolean temVizinho = (boolean)getServidorCopycat().submit(new GetTemVizinho(nomeVertice1)).join();
				
				if (temVizinho) {
					List<Integer> listVerticesDestino = (List<Integer>)getServidorCopycat().submit(new GetListaDeVizinhos(nomeVertice1)).join();
					
					for (Integer verticeDestino : listVerticesDestino) {
						String key1 = nomeVertice1 + "-" + verticeDestino;
						String key2 = verticeDestino + "-" + nomeVertice1;
						
						if (existeAresta(nomeVertice1, verticeDestino)) {
							result += printarDadosAresta(nomeVertice1, verticeDestino);
						} 
					
					}
				} else {
					result = "O vertice nao possui nenhuma aresta!";
				}
			} else {
				result = "O vertice nao existe!";
			}
		}else{
			result = getServidor(hash).listarArestasDeUmVertice(nomeVertice1);
		}
		
		return result;
	}
	
	public synchronized String listarVerticesVizinhos(int nome)  throws TException{
		if(!conectado) Conectar();
		
		int hash = (int)((new Integer(nome)).toString().hashCode()%3);
		String result = "";
		
		if(hash == this.idServidor){
			if (existeVertice(nome)) {
				boolean temVizinho = (boolean)getServidorCopycat().submit(new GetTemVizinho(nome)).join();
				if (temVizinho) {
					List<Integer> listVerticesDestino = (List<Integer>)getServidorCopycat().submit(new GetListaDeVizinhos(nome)).join();
					
					for (Integer verticeDestino : listVerticesDestino) {
						result += printarDadosVertice(verticeDestino);
					}
				} else {
					result = "O vertice nao possui vertices vizinhos!";
				}
			} else {
				result = "O vertice nao existe!";
			}
		}else{
			result = getServidor(hash).listarVerticesVizinhos(nome);
		}
		
		return result;
	}
	
	public synchronized double getCusto(int a, int b) throws TException{
		if(a==b) return 0.0;
		String key = a + "-" + b;
		int hash = (int)(key.hashCode()%3);
		if(hash == this.idServidor){
			return (double)getServidorCopycat().submit(new GetGetCusto(a, b)).join();
		}else{
			return getServidor(hash).getCusto(a, b);
		}
	}
	
	public synchronized void setCusto(int a, int b, double c) throws TException{
		String key = a + "-" + b;
		int hash = (int)(key.hashCode()%3);
		if(hash==this.idServidor){
			getServidorCopycat().submit(new PutSetCusto(a, b, c)).join();
		}else{
			getServidor(hash).setCusto(a, b, c);
		}
	}
	
	public synchronized void copia() throws TException{
		getServidorCopycat().submit(new PutCopia()).join();
	}
	
	
	public synchronized String caminhoMinimo(int origem, int destino) throws TException{
		if(!conectado) Conectar();
		if(!existeVertice(origem) || !existeVertice(destino)){
			return "Algum dos vértices não existe";
		}
		copia();
		client0.copia();
		client1.copia();
		
		Map<String, Integer> pai = new HashMap<String, Integer>();
		
		for(int i = 0; i < 40; i++){
			for(int j = 0; j < 40; j++){
				pai.put(i + "-" + j, j);
			}
		}
		
		for (int k = 0; k < 40; k++)
		{
			if(!existeVertice(k)) continue;
			for (int i = 0; i < 40; i++)
			{
				if(!existeVertice(i)) continue;
				for (int j = 0; j < 40; j++)
				{
					if(!existeVertice(j)) continue;
					if(getCusto(i, k) + getCusto(k, j) < getCusto(i, j)){
						setCusto(i, j, getCusto(i, k) + getCusto(k, j));
						pai.put(i+"-"+j, pai.get(i+"-"+k));
					}
				}
			}
		}
		
		String path="";
		int a = origem, b = destino;
		
		path += origem;
		while(a != b)
		{
			a = pai.get(a + "-" + b);
			path += "-" + a;
		}
		
		
		double custo = getCusto(origem, destino);
		String result;
		if(custo < Double.MAX_VALUE){
			result = "Caminho minimo entre " + origem + " e " + destino + " = " + custo + "\n";
			result += "Passando por: " + path;
			
		}
		else result = "Não existe caminho entre " + origem + " e " + destino;
		return result;
	}
	
	public boolean existeVertice(int nome) throws TException{
		int hash = (int)((new Integer(nome)).toString().hashCode()%3);
		if(hash == this.idServidor){
			return (boolean)getServidorCopycat().submit(new GetExisteVertice(nome)).join();
		}
		else{
			return getServidor(hash).existeVertice(nome);
		}
	}
	
	public boolean existeAresta(int vertice1, int vertice2) throws TException{
		String key1 = vertice1 + "-" + vertice2;
		
		int hash1 = (int)(key1.hashCode()%3);
		boolean existeKey1 = false;
		
		if(hash1 == this.idServidor){
			
			existeKey1 = (boolean)getServidorCopycat().submit(new GetExisteAresta(vertice1, vertice2)).join();
		}else{
			existeKey1 = getServidor(hash1).existeAresta(vertice1, vertice2);
		}
		
		return existeKey1;
	}
	
	public String printarDadosVertice(int nome) throws TException{
		Vertice vertice;
		int hash = (int)((new Integer(nome)).toString().hashCode()%3);
		if(hash==this.idServidor){
			return (String)getServidorCopycat().submit(new GetPrintarDadosVertice(nome)).join();
		}else{
			return getServidor(hash).printarDadosVertice(nome);
		}
		
		
		
	}
	
	public String printarDadosAresta(int vertice1, int vertice2) throws TException{
		String key = vertice1 + "-" + vertice2;
		int hash = (int)(key.hashCode()%3);
		Aresta aresta;
		
		if(hash == this.idServidor){
			return (String)getServidorCopycat().submit(new GetPrintarDadosAresta(vertice1, vertice2)).join();
		}else{
			return getServidor(hash).printarDadosAresta(vertice1, vertice2);
		}
		
	}
	
	private CopycatClient getServidorCopycat(){
		
		return copyCatClient;
	}
	
 	private void sleep() {
		try {
			if (isModoApresentacao) {
				Thread.sleep(10000);
			}
		} catch (InterruptedException ie) {
			 
		}
		
	}
}
