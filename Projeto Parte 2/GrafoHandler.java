package grafo;

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

public class GrafoHandler implements grafo.Iface {
	Map<Integer, Vertice> mapVertices = new HashMap<Integer, Vertice>();
	Map<String, Aresta> mapArestas = new HashMap<String, Aresta>();
	Map<String, Aresta> mapCopia = new HashMap<String, Aresta>();
	
	Map<Integer, List<Integer>> mapArestasPorVertice = new HashMap<Integer, List<Integer>>();
	
	boolean isModoApresentacao = true; //Variavel utilizada para que as operacao demorem um certo tempo, para a apresentacao e o professor 
	boolean conectado = false;
	int idServidor;
	
	TTransport transport0;
	TProtocol protocol0;
	grafo.Client client0;
	
	TTransport transport1;
	TProtocol protocol1;
	grafo.Client client1;
	
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
		
		if(idServidor == 0){
			porta1 = 5003;
			porta2 = 5001;
		}else if(idServidor == 1){
			porta1 = 9090;
			porta2 = 5001;
		}else{
			porta1 = 9090;
			porta2 = 5003;
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
	}

	//VERTICES
	public synchronized String criarVertice(int nome, int cor, String descricao, double peso) throws TException{
		if(!conectado) Conectar();
		int hash = (int)((new Integer(nome)).toString().hashCode()%3);
		
		String result = "";
		if(hash == this.idServidor){
			System.out.println("Inserindo vértice no servidor " + this.idServidor);
			if (!existeVertice(nome)) {
				Vertice vertice = new Vertice(nome, cor, descricao, peso);
				mapVertices.put(nome, vertice);

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
				mapVertices.get(nome).setCor(cor);
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
				mapVertices.get(nome).setDescricao(descricao);
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
				mapVertices.get(nome).setPeso(peso);
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

				mapVertices.remove(nome);
				result = "Vertice deletado com sucesso.";
			} else {
				result = "O vertice nao existe!";
			}
		}else{
			result = getServidor(hash).deletarVertice(nome);
		}
		
		return result;
	}

	//ARESTAS
	public synchronized String criarAresta(int vertice1, int vertice2, double peso, boolean isBiDirecional, String descricao)  throws TException{
		if(!conectado) Conectar();
		String key = vertice1 + "-" + vertice2;
		int hash = (int)(key.hashCode()%3);
		
		String result = "";
		
		if(hash == this.idServidor){
			if (existeVertice(vertice1) && existeVertice(vertice2)) {
				if (!existeAresta(vertice1, vertice2)) {
					System.out.println("Inserindo aresta no servidor " + this.idServidor);
					Aresta aresta = new Aresta(vertice1, vertice2, peso, isBiDirecional, descricao);

					mapArestas.put(key, aresta);
					
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

	
	
	

	/**
	 * Cada vertice tera uma lista dos outros vertices com os quais existem arestas, assim quando for adicionado uma aresta, tenho
	 * que adicionar esses vertices na mapArestasPorVertice
	 * */
	public void adicionarArestaPeloVertice(int verticeOrigem, int verticeDestino) throws TException{
		if(!conectado) Conectar();
		
		int hash = (int)((new Integer(verticeOrigem)).toString().hashCode()%3);
		
		if(hash == this.idServidor){
			List<Integer> listVerticesDestino = null;
			
			if (mapArestasPorVertice.containsKey(verticeOrigem)) {
				listVerticesDestino = mapArestasPorVertice.get(verticeOrigem);
			} else {
				listVerticesDestino = new ArrayList<Integer>();
			}
			
			listVerticesDestino.add(verticeDestino);
			mapArestasPorVertice.put(verticeOrigem, listVerticesDestino);
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
				if (mapArestas.containsKey(key1)) {
					mapArestas.get(key1).setPeso(peso);
				}
				result = "Peso alterado com sucesso!";
			}else{
				result = getServidor(hash1).alterarPesoAresta(vertice1, vertice2, peso);
			}
			
		} 
	    if(existeAresta(vertice2, vertice1)) {
			if(hash2 == this.idServidor){
				if (mapArestas.containsKey(key2)) {
					mapArestas.get(key2).setPeso(peso);
				}
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
				if (mapArestas.containsKey(key1)) {
					mapArestas.get(key1).setDescricao(descricao);
				}
				result = "Descricao alterada com sucesso!";
			}else{
				result = getServidor(hash1).alterarDescricaoAresta(vertice1, vertice2, descricao);
			}
			
		} 
		if(existeAresta(vertice2, vertice1)) {
			if(hash2 == this.idServidor){
				if (mapArestas.containsKey(key2)) {
					mapArestas.get(key2).setDescricao(descricao);
				}
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
	
	/**
	 * Metodo responsavel por deletar as arestar a partir de um vertice
	 * Exemplo: se eu tenho o vertice 1 e ele tem ligacao com o vertice 2, entao devo deletar a aresta 1-2
	 * 
	 * Alem diso devo deletar tambem os vertices destino ao qual esse vertice esta ligado.
	 * */
	
	public void removeMapArestasPorVertice(int vertice1, int vertice2) throws TException{
		int hash = (int)((new Integer(vertice1)).toString().hashCode()%3);
		if(hash == this.idServidor){
			if(mapArestasPorVertice.containsKey(vertice1))
				mapArestasPorVertice.get(vertice1).remove((Integer) vertice2);
		}else{
			getServidor(hash).removeMapArestasPorVertice(vertice1, vertice2);
		}
	} 
	
	public void removeMapArestas(String key) throws TException{
		int hash = (int)(key.hashCode()%3);
		if(hash == this.idServidor){
			mapArestas.remove(key);
		}else{
			getServidor(hash).removeMapArestas(key);
		}
	}
	
	public void deletarArestaPeloVertice(int vertice) throws TException{
		int hash = (int)((new Integer(vertice)).toString().hashCode()%3);
		if(hash==this.idServidor){
			if (mapArestasPorVertice.containsKey(vertice)) {
				List<Integer> listVerticesDestino = mapArestasPorVertice.get(vertice);
				
				for (Integer verticeDestino : listVerticesDestino) {
					String key1 = vertice + "-" + verticeDestino;
					String key2 = verticeDestino + "-" + vertice;
					int hash1 = (int)(key1.hashCode()%3);
					int hash2 = (int)(key2.hashCode()%3);
					
					int cont = 0;
					
					//Remove as arestas do vertice (se for bidirecional ou nao)
					if (existeAresta(vertice, verticeDestino)) {
						removeMapArestas(key1);
						cont++;
					} 
					
					if (existeAresta(verticeDestino, vertice)) {
						removeMapArestas(key2);
						cont++;
					}
					
					//Remove a ligacao dos vertices na map destino na que possuem arestas bidirecionais
					//EX: suponda que existe uma aresta 1-2, 1-3 e 2-1. Nessa caso quando eu executar o comando 
					//mapArestasPorVertice.remove(vertice) a ligacao 1-2 e 1-3 ja sera deletada, contudo eu preciso pegar a lista de
					//vertices ligados ao vertice 2, para remover a ligacao 2-1
					if (cont == 2) {
						removeMapArestasPorVertice(verticeDestino, vertice);//remove vertice da lista de adjacencia do verticeDestino
						
					}
				}
				
				mapArestasPorVertice.remove(vertice);
			}
		}else{
			getServidor(hash).deletarArestaPeloVertice(vertice);
		}
		
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
				if (mapArestasPorVertice.containsKey(nomeVertice1)) {
					List<Integer> listVerticesDestino = mapArestasPorVertice.get(nomeVertice1);
					
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
				if (mapArestasPorVertice.containsKey(nome)) {
					List<Integer> listVerticesDestino = mapArestasPorVertice.get(nome);
					
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
			if(mapCopia.containsKey(key)){
				return mapCopia.get(key).getPeso();
			}
			return 10000000000.0;
		}else{
			return getServidor(hash).getCusto(a, b);
		}
	}
	
	public synchronized void setCusto(int a, int b, double c) throws TException{
		String key = a + "-" + b;
		int hash = (int)(key.hashCode()%3);
		if(hash==this.idServidor){
			if(mapCopia.containsKey(key)){
				mapCopia.get(key).setPeso(c);
			}else{
				mapCopia.put(key, new Aresta(a, b, c, false, "FLOYD WARSHALL"));
			}
		}else{
			getServidor(hash).setCusto(a, b, c);
		}
	}
	
	
	public synchronized void copia() throws TException{
		mapCopia.clear();
		for (Map.Entry<String, Aresta> entry : mapArestas.entrySet()) {
			mapCopia.put(entry.getKey(), entry.getValue());
		}
	}
	
	public synchronized double caminhoMinimo(int origem, int destino) throws TException{
		if(!conectado) Conectar();
		copia();
		client0.copia();
		client1.copia();
		
		for (int k = 0; k < 40; k++)
		{
			for (int i = 0; i < 40; i++)
			{
				for (int j = 0; j < 40; j++)
				{
					
					if(!existeVertice(k) || !existeVertice(i) || !existeVertice(j)) continue;
					if(getCusto(i, k) + getCusto(k, j) < getCusto(i, j)){
						setCusto(i, j, getCusto(i, k) + getCusto(k, j));
					}
				}
			}
		}
		return getCusto(origem, destino);
	}
	
	public boolean existeVertice(int nome) throws TException{
		int hash = (int)((new Integer(nome)).toString().hashCode()%3);
		if(hash == this.idServidor){
			return mapVertices.containsKey(nome);
		}
		else{
			return getServidor(hash).existeVertice(nome);
		}
	}
	
	public boolean existeAresta(int vertice1, int vertice2) throws TException{
		String key1 = vertice1 + "-" + vertice2;
		
		int hash1 = (int)(key1.hashCode()%3);
		boolean existeKey1 = false;
		
		if(hash1 == this.idServidor) existeKey1 = mapArestas.containsKey(key1);
		else existeKey1 = getServidor(hash1).existeAresta(vertice1, vertice2);
		
		return existeKey1;
	}
	
	public String printarDadosVertice(int nome) throws TException{
		Vertice vertice;
		int hash = (int)((new Integer(nome)).toString().hashCode()%3);
		if(hash==this.idServidor){
			StringBuilder builder = new StringBuilder();
			vertice = mapVertices.get(nome);
			builder.append("Nome do vertice: " + vertice.getNome() + "\n");
			builder.append("Cor do vertice: " + vertice.getCor() + "\n");
			builder.append("Descricao do vertice: " + vertice.getDescricao() + "\n");
			builder.append("Peso do vertice: " + vertice.getPeso() + "\n");
			return builder.toString() + "\n";
		}else{
			return getServidor(hash).printarDadosVertice(nome);
		}
		
		
		
	}
	
	public String printarDadosAresta(int vertice1, int vertice2) throws TException{
		String key = vertice1 + "-" + vertice2;
		int hash = (int)(key.hashCode()%3);
		Aresta aresta;
		
		if(hash == this.idServidor){
			StringBuilder builder = new StringBuilder();
			aresta = mapArestas.get(key);
			builder.append("Nome do vertice 1: " + aresta.getVertice1() + "\n");
			builder.append("Nome do vertice 2: " + aresta.getVertice2() + "\n");
			builder.append("Peso da aresta: " + aresta.getPeso() + "\n");
			builder.append("E aresta bidirecional: " + (aresta.isBiDirecional() ? "S" : "N") + "\n");
			builder.append("Descricao da aresta: " + aresta.getDescricao() + "\n");
			
			return builder.toString() + "\n";
		}else{
			return getServidor(hash).printarDadosAresta(vertice1, vertice2);
		}
		
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
