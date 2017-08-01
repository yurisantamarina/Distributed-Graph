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
import br.ufu.get.GetGetListVizinhos;
import br.ufu.get.GetGetNomeVertice;
import br.ufu.get.GetGetTipoVertice;
import br.ufu.get.GetListaDeVizinhos;
import br.ufu.get.GetPrintarDadosAresta;
import br.ufu.get.GetPrintarDadosVertice;
import br.ufu.get.GetTemVizinho;
import br.ufu.put.PutAdicionarArestaPeloVertice;
import br.ufu.put.PutCopia;
import br.ufu.put.PutCriarAresta;
import br.ufu.put.PutCriarVertice;
import br.ufu.put.PutDeletarArestaPeloVertice;
import br.ufu.put.PutDeletarVertice;
import br.ufu.put.PutRemoveMapArestas;
import br.ufu.put.PutRemoveMapArestasPorVertice;
import br.ufu.put.PutSetCusto;

public class GrafoHandler implements grafo.Iface {
	boolean isModoApresentacao = true; // Variavel utilizada para que as
										// operacao demorem um certo tempo, para
										// a apresentacao e o professor
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

	private grafo.Client getServidor(int hash) {
		if (idServidor == 0) {
			if (hash == 1) {
				return client0;
			} else {
				return client1;
			}
		} else if (idServidor == 1) {
			if (hash == 0) {
				return client0;
			} else {
				return client1;
			}
		} else {
			if (hash == 0) {
				return client0;
			} else {
				return client1;
			}
		}
	}

	private void Conectar() {
		int porta1, porta2;
		int portaCC1, portaCC2, portaCC3;

		if (idServidor == 0) {
			porta1 = 9091;
			porta2 = 9092;

			portaCC1 = 5500;
			portaCC2 = 5501;
			portaCC3 = 5502;
		} else if (idServidor == 1) {
			porta1 = 9090;
			porta2 = 9092;

			portaCC1 = 5510;
			portaCC2 = 5511;
			portaCC3 = 5512;
		} else {
			porta1 = 9090;
			porta2 = 9091;

			portaCC1 = 5520;
			portaCC2 = 5521;
			portaCC3 = 5522;
		}

		try {
			transport0 = new TSocket("localhost", porta1);
			transport0.open();
			protocol0 = new TBinaryProtocol(transport0);
			client0 = new grafo.Client(protocol0);

			transport1 = new TSocket("localhost", porta2);
			transport1.open();
			protocol1 = new TBinaryProtocol(transport1);
			client1 = new grafo.Client(protocol1);
			conectado = true;
			System.out.println("Servidor " + this.idServidor
					+ " conectado aos outros 2");
		} catch (TException x) {
			System.out.println("NAO CRIOU");
		}

		try {
			copyCatClient = CopycatClient
					.builder(new Address("localhost", portaCC1))
					.withConnectionStrategy(
							ConnectionStrategies.FIBONACCI_BACKOFF).build();
			copyCatClient2 = CopycatClient
					.builder(new Address("localhost", portaCC2))
					.withConnectionStrategy(
							ConnectionStrategies.FIBONACCI_BACKOFF).build();
			copyCatClient3 = CopycatClient
					.builder(new Address("localhost", portaCC3))
					.withConnectionStrategy(
							ConnectionStrategies.FIBONACCI_BACKOFF).build();

			copyCatClient.connect().join();
			copyCatClient2.connect().join();
			copyCatClient3.connect().join();

		} catch (Exception E) {
			System.out.println("NAO CRIOU COPYCAT");
		}
	}

	// OK
	public synchronized String criarVertice(int id, String nome, String tipo,
			String cast) throws TException {
		if (!conectado)
			Conectar();
		int hash = (int) ((new Integer(id)).toString().hashCode() % 3);

		String result = "";
		if (hash == this.idServidor) {
			String palavra = tipo.equals("F") ? "filme" : "pessoa";
			System.out.println("Inserindo " + palavra + " no servidor "
					+ this.idServidor);

			if (!existeVertice(id)) {
				getServidorCopycat().submit(
						new PutCriarVertice(id, nome, tipo, cast)).join();
				if (tipo.equals("F")) {
					result = "Filme adicionado com sucesso";
				} else {
					result = "Pessoa adicionada com sucesso";
				}
			} else {
				if (tipo == "F") {
					result = "O filme já existe";
				} else {
					result = "A pessoa já existe";
				}
			}
		} else {
			result = getServidor(hash).criarVertice(id, nome, tipo, cast);
		}

		return result;
	}

	// OK
	public synchronized String consultarVertice(int id) throws TException {
		if (!conectado)
			Conectar();
		int hash = (int) ((new Integer(id)).toString().hashCode() % 3);

		String result = "";
		if (hash == this.idServidor) {
			System.out.println("Consultando o id no servidor "
					+ this.idServidor);
			if (existeVertice(id)) {
				result = printarDadosVertice(id);
			} else {
				result = "O id não existe!";
			}
		} else {
			result = getServidor(hash).consultarVertice(id);
		}

		return result;
	}

	// OK
	public synchronized String deletarVertice(int id) throws TException {
		if (!conectado)
			Conectar();
		int hash = (int) ((new Integer(id)).toString().hashCode() % 3);

		String result = "";
		if (hash == this.idServidor) {
			if (existeVertice(id)) {
				deletarArestaPeloVertice(id);

				getServidorCopycat().submit(new PutDeletarVertice(id)).join();
				result = "ID deletado com sucesso.";
			} else {
				result = "O ID nao existe!";
			}
		} else {
			result = getServidor(hash).deletarVertice(id);
		}

		return result;
	}

	// OK
	public void removeMapArestasPorVertice(int id1, int id2) throws TException {
		int hash = (int) ((new Integer(id1)).toString().hashCode() % 3);
		if (hash == this.idServidor) {
			boolean temVizinho = (boolean) getServidorCopycat().submit(
					new GetTemVizinho(id1)).join();
			if (temVizinho) {
				getServidorCopycat().submit(
						new PutRemoveMapArestasPorVertice(id1, id2)).join();
			}
		} else {
			getServidor(hash).removeMapArestasPorVertice(id1, id2);
		}
	}

	// OK
	public void removeMapArestas(String key) throws TException {
		int hash = (int) (key.hashCode() % 3);
		if (hash == this.idServidor) {
			getServidorCopycat().submit(new PutRemoveMapArestas(key)).join();
		} else {
			getServidor(hash).removeMapArestas(key);
		}
	}

	// OK
	public void deletarArestaPeloVertice(int id) throws TException {
		int hash = (int) ((new Integer(id)).toString().hashCode() % 3);
		if (hash == this.idServidor) {
			boolean temVizinho = (boolean) getServidorCopycat().submit(
					new GetTemVizinho(id)).join();

			if (temVizinho) {
				List<Integer> listVerticesDestino = (List<Integer>) getServidorCopycat()
						.submit(new GetListaDeVizinhos(id)).join();

				for (Integer verticeDestino : listVerticesDestino) {
					String key1 = id + "-" + verticeDestino;
					String key2 = verticeDestino + "-" + id;

					int cont = 0;

					if (existeAresta(id, verticeDestino)) {
						removeMapArestas(key1);
						cont++;
					}

					if (existeAresta(verticeDestino, id)) {
						removeMapArestas(key2);
						cont++;
					}

					if (cont == 2) {
						removeMapArestasPorVertice(verticeDestino, id);
					}
				}

				getServidorCopycat()
						.submit(new PutDeletarArestaPeloVertice(id)).join();
			}
		} else {
			getServidor(hash).deletarArestaPeloVertice(id);
		}

	}

	// OK
	public String deletarAresta(int id1, int id2) throws TException {
		String key = id1 + "-" + id2;
		int hash = (int) (key.hashCode() % 3);
		if (hash == this.idServidor) {
			String result = "";
			if (existeAresta(id1, id2)) {
				removeMapArestas(key);
				removeMapArestasPorVertice(id1, id2);
				result = "Aresta deletada com sucesso";
			} else {
				result = "Aresta inexistente";
			}

			return result;
		} else {
			return getServidor(hash).deletarAresta(id1, id2);
		}

	}

	// OK
	public String getTipoVertice(int id) throws TException {
		int hash = (int) ((new Integer(id)).toString().hashCode() % 3);

		String result = "";
		if (hash == this.idServidor) {
			result = (String) getServidorCopycat().submit(
					new GetGetTipoVertice(id)).join();
		} else {
			result = getServidor(hash).getTipoVertice(id);
		}

		return result;
	}

	// OK
	public synchronized String criarAresta(int id1, int id2, int nota)
			throws TException {
		if (!conectado)
			Conectar();
		String key = id1 + "-" + id2;
		int hash = (int) (key.hashCode() % 3);

		String result = "";

		if (hash == this.idServidor) {
			if (existeVertice(id1) && existeVertice(id2)) {
				if (!existeAresta(id1, id2)) {
					String tipo1 = getTipoVertice(id1);
					String tipo2 = getTipoVertice(id2);

					if (!tipo1.equals(tipo2)) {
						System.out.println("Inserindo aresta no servidor "
								+ this.idServidor);

						getServidorCopycat().submit(
								new PutCriarAresta(id1, id2, nota)).join();

						adicionarArestaPeloVertice(id1, id2);

						result = "Aresta criada com sucesso!";
					} else {
						result = "Não é possível inserir uma aresta para IDs do mesmo tipo (pessoa-pessoa ou filme-filme)";
					}
				} else {
					result = "A aresta ja existe!";
				}
			} else {
				result = "O id nao existe!";
			}
		} else {
			result = getServidor(hash).criarAresta(id1, id2, nota);
		}

		return result;
	}

	// OK
	public void adicionarArestaPeloVertice(int id1, int id2) throws TException {
		if (!conectado)
			Conectar();

		int hash = (int) ((new Integer(id1)).toString().hashCode() % 3);

		if (hash == this.idServidor) {
			getServidorCopycat().submit(
					new PutAdicionarArestaPeloVertice(id1, id2)).join();
		} else {
			getServidor(hash).adicionarArestaPeloVertice(id1, id2);
		}
	}

	// OK
	public synchronized String consultarAresta(int id1, int id2)
			throws TException {
		if (!conectado)
			Conectar();

		String key = id1 + "-" + id2;
		int hash = (int) (key.hashCode() % 3);

		String result = "";

		if (hash == this.idServidor) {
			if (existeAresta(id1, id2)) {
				System.out.println("Aresta existe");
				result += printarDadosVertice(id1);
				result += printarDadosVertice(id2);
				result += printarDadosAresta(id1, id2);
				System.out.println("OK");
			} else {
				result = "A aresta nao existe!";
			}
		} else {
			result = getServidor(hash).consultarAresta(id1, id2);
		}

		return result;
	}

	// OK
	public synchronized String listarVerticesVizinhos(int id) throws TException {
		if (!conectado)
			Conectar();

		int hash = (int) ((new Integer(id)).toString().hashCode() % 3);
		String result = "";

		if (hash == this.idServidor) {
			if (existeVertice(id)) {
				boolean temVizinho = (boolean) getServidorCopycat().submit(
						new GetTemVizinho(id)).join();
				if (temVizinho) {
					List<Integer> listVerticesDestino = (List<Integer>) getServidorCopycat()
							.submit(new GetListaDeVizinhos(id)).join();

					for (Integer verticeDestino : listVerticesDestino) {
						result += printarDadosVertice(verticeDestino);
					}
				} else {
					result = "O ID nao possui vertices vizinhos!";
				}
			} else {
				result = "O ID nao existe!";
			}
		} else {
			result = getServidor(hash).listarVerticesVizinhos(id);
		}

		return result;
	}

	// OK
	public synchronized int getCusto(int id1, int id2) throws TException {
		if (id1 == id2)
			return 0;
		String key = id1 + "-" + id2;
		int hash = (int) (key.hashCode() % 3);
		if (hash == this.idServidor) {
			return (int) getServidorCopycat().submit(new GetGetCusto(id1, id2))
					.join();
		} else {
			return getServidor(hash).getCusto(id1, id2);
		}
	}

	// OK
	public synchronized void setCusto(int id1, int id2, int custo)
			throws TException {
		String key = id1 + "-" + id2;
		int hash = (int) (key.hashCode() % 3);
		if (hash == this.idServidor) {
			getServidorCopycat().submit(new PutSetCusto(id1, id2, custo))
					.join();
		} else {
			getServidor(hash).setCusto(id1, id2, custo);
		}
	}

	// OK
	public synchronized void copia() throws TException {
		getServidorCopycat().submit(new PutCopia()).join();
	}

	// OK
	public String getNomeVertice(int id) throws TException {
		int hash = (int) ((new Integer(id)).toString().hashCode() % 3);

		String result = "";
		if (hash == this.idServidor) {
			result = (String) getServidorCopycat().submit(
					new GetGetNomeVertice(id)).join();
		} else {
			result = getServidor(hash).getNomeVertice(id);
		}

		return result;
	}

	// OK
	public synchronized String caminhoMinimo(int origem, int destino)
			throws TException {
		if (!conectado)
			Conectar();
		if (!existeVertice(origem) || !existeVertice(destino)) {
			return "Algum dos IDs não existe";
		}
		String tipo1 = getTipoVertice(origem);
		String tipo2 = getTipoVertice(destino);

		if (tipo1.equals("F") || tipo2.equals("F"))
			return "Não é possível consultar o caminho mínimo para IDs do tipo filme!";
		copia();
		client0.copia();
		client1.copia();
		Map<String, Integer> pai = new HashMap<String, Integer>();

		for (int i = 0; i < 40; i++) {
			for (int j = 0; j < 40; j++) {
				pai.put(i + "-" + j, j);
			}
		}

		for (int k = 0; k < 40; k++) {
			if (!existeVertice(k))
				continue;
			for (int i = 0; i < 40; i++) {
				if (!existeVertice(i))
					continue;
				for (int j = 0; j < 40; j++) {
					if (!existeVertice(j))
						continue;
					if ((long) getCusto(i, k) + (long) getCusto(k, j) < (long) getCusto(
							i, j)) {
						setCusto(i, j, getCusto(i, k) + getCusto(k, j));
						pai.put(i + "-" + j, pai.get(i + "-" + k));
					}
				}
			}
		}

		String path = "";
		int a = origem, b = destino;
		path += getNomeVertice(origem);
		while (a != b) {
			a = pai.get(a + "-" + b);
			path += "-" + getNomeVertice(a);
		}

		int custo = getCusto(origem, destino);
		String result;
		if (custo < Integer.MAX_VALUE) {
			result = "Caminho minimo entre " + getNomeVertice(origem) + " e "
					+ getNomeVertice(destino) + " = " + custo + "\n";
			result += "Passando por: " + path;

		} else
			result = "Não existe caminho entre " + origem + " e " + destino;
		return result;
	}

	// OK
	public boolean existeVertice(int id) throws TException {
		int hash = (int) ((new Integer(id)).toString().hashCode() % 3);
		if (hash == this.idServidor) {
			return (boolean) getServidorCopycat().submit(
					new GetExisteVertice(id)).join();
		} else {
			return getServidor(hash).existeVertice(id);
		}
	}

	// OK
	public boolean existeAresta(int id1, int id2) throws TException {
		String key1 = id1 + "-" + id2;

		int hash1 = (int) (key1.hashCode() % 3);
		boolean existeKey1 = false;

		if (hash1 == this.idServidor) {

			existeKey1 = (boolean) getServidorCopycat().submit(
					new GetExisteAresta(id1, id2)).join();
		} else {
			existeKey1 = getServidor(hash1).existeAresta(id1, id2);
		}

		return existeKey1;
	}

	// OK
	public String printarDadosVertice(int id) throws TException {
		int hash = (int) ((new Integer(id)).toString().hashCode() % 3);
		if (hash == this.idServidor) {
			return (String) getServidorCopycat().submit(
					new GetPrintarDadosVertice(id)).join();
		} else {
			return getServidor(hash).printarDadosVertice(id);
		}

	}

	// OK
	public String printarDadosAresta(int id1, int id2) throws TException {
		String key = id1 + "-" + id2;
		int hash = (int) (key.hashCode() % 3);

		if (hash == this.idServidor) {
			return (String) getServidorCopycat().submit(
					new GetPrintarDadosAresta(id1, id2)).join();
		} else {
			return getServidor(hash).printarDadosAresta(id1, id2);
		}

	}
	
	public List<Integer> getListVizinhos(int id) throws TException{
		int hash = (int) ((new Integer(id)).toString().hashCode() % 3);
		if (hash == this.idServidor) {
			return (List<Integer>) getServidorCopycat().submit(new GetGetListVizinhos(id)).join();
		} else {
			return getServidor(hash).getListVizinhos(id);
		}
	}
	
	public String listarFilmesPorGrupo(String listaIds){
		if(!conectado) Conectar();
		StringBuilder result = new StringBuilder();
		try{
			String aux[] = listaIds.split(" ");
			int ids[] = new int[aux.length];
			
			for(int i = 0; i < aux.length; i++){
				ids[i] = Integer.parseInt(aux[i]);
			}
			
			result.append("Filme vistos por pelo menos uma pessoa: ");
			boolean tem = false;
			
			Map<Integer, Integer> frequencia = new HashMap<Integer, Integer>();
			for(int i = 0; i < ids.length; i++){
				if(!existeVertice(ids[i])){
					continue;
				}
				List<Integer> listVizinho = getListVizinhos(ids[i]);
				for(Integer vizinho : listVizinho){
					if(!frequencia.containsKey(vizinho)){
						result.append(getNomeVertice(vizinho) + " ");
						tem = true;
						frequencia.put(vizinho, 1);
					}else{
						frequencia.put(vizinho, frequencia.get(vizinho) + 1);
					}
				}
			}
			if(!tem){
				result.append("nenhuma das pessoas do grupo assitiram a algum filme");
			}
			
			tem = false;
			
			result.append("\n\n");
			result.append("Filmes vistos por todas as pessoas: ");
			
			for (Map.Entry<Integer, Integer> entry : frequencia.entrySet()) {
				if(entry.getValue() == ids.length){
					result.append(getNomeVertice(entry.getKey()) + " ");
					tem = true;
				}
			}
			
			if(!tem){
				result.append("não existe filme visto por todas as pessoas do grupo");
			}
		}catch(Exception e){
			e.printStackTrace();
			result.append("Formato da lista de IDs inválida");
		}
		
		return result.toString();
	}

	private CopycatClient getServidorCopycat() {

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
