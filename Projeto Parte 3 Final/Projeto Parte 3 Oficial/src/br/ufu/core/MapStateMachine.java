package br.ufu.core;

import br.ufu.get.*;
import br.ufu.put.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grafo.Aresta;
import grafo.Vertice;
import io.atomix.copycat.server.Commit;
import io.atomix.copycat.server.Snapshottable;
import io.atomix.copycat.server.StateMachine;
import io.atomix.copycat.server.storage.snapshot.SnapshotReader;
import io.atomix.copycat.server.storage.snapshot.SnapshotWriter;

public class MapStateMachine extends StateMachine implements Snapshottable {
	Map<Integer, Vertice> mapVertices = new HashMap<Integer, Vertice>();
	Map<String, Aresta> mapArestas = new HashMap<String, Aresta>();
	Map<String, Aresta> mapCopia = new HashMap<String, Aresta>();

	Map<Integer, List<Integer>> mapArestasPorVertice = new HashMap<Integer, List<Integer>>();

	public boolean getExisteVertice(Commit<GetExisteVertice> commit) {
		try {
			return mapVertices.containsKey(commit.operation().getNome());
		} finally {
			commit.close();
		}
	}

	public boolean getExisteAresta(Commit<GetExisteAresta> commit) {
		try {
			String key1 = commit.operation().getV1() + "-"
					+ commit.operation().getV2();
			return mapArestas.containsKey(key1);
		} finally {
			commit.close();
		}
	}

	public double getGetCusto(Commit<GetGetCusto> commit) {
		try {
			String key = commit.operation().getV1() + "-"
					+ commit.operation().getV2();
			if (mapCopia.containsKey(key)) {
				return mapCopia.get(key).getPeso();
			}
			return Double.MAX_VALUE;
		} finally {
			commit.close();
		}
	}

	public boolean getTemVizinho(Commit<GetTemVizinho> commit) {
		try {
			return mapArestasPorVertice.containsKey(commit.operation()
					.getNome());
		} finally {
			commit.close();
		}
	}

	public List<Integer> getListaDeVizinhos(Commit<GetListaDeVizinhos> commit) {
		try {
			List<Integer> listVerticesDestino = mapArestasPorVertice.get(commit
					.operation().getNome());
			return listVerticesDestino;
		} finally {
			commit.close();
		}
	}

	public String getPritarDadosVertice(Commit<GetPrintarDadosVertice> commit) {
		Vertice vertice;
		try {
			StringBuilder builder = new StringBuilder();
			vertice = mapVertices.get(commit.operation().getNome());

			builder.append("Nome do vertice: " + vertice.getNome() + "\n");
			builder.append("Cor do vertice: " + vertice.getCor() + "\n");
			builder.append("Descricao do vertice: " + vertice.getDescricao()
					+ "\n");
			builder.append("Peso do vertice: " + vertice.getPeso() + "\n");
			return builder.toString() + "\n";
		} finally {
			commit.close();
		}
	}

	public String getPrintarDadosAresta(Commit<GetPrintarDadosAresta> commit) {

		try {
			Aresta aresta;
			String key = commit.operation().getVertice1() + "-"
					+ commit.operation().getVertice2();
			StringBuilder builder = new StringBuilder();
			aresta = mapArestas.get(key);
			builder.append("Nome do vertice 1: " + aresta.getVertice1() + "\n");
			builder.append("Nome do vertice 2: " + aresta.getVertice2() + "\n");
			builder.append("Peso da aresta: " + aresta.getPeso() + "\n");
			builder.append("E aresta bidirecional: "
					+ (aresta.isBiDirecional() ? "S" : "N") + "\n");
			builder.append("Descricao da aresta: " + aresta.getDescricao()
					+ "\n");

			return builder.toString() + "\n";
		} finally {
			commit.close();
		}
	}

	public void putCriarVertice(Commit<PutCriarVertice> commit) {
		try {
			PutCriarVertice operation = commit.operation();
			Vertice vertice = new Vertice(operation.getNome(),
					operation.getCor(), operation.getDescricao(),
					operation.getPeso());
			mapVertices.put(operation.getNome(), vertice);
		} finally {
			commit.close();
		}
	}

	public void putAlterarCorVertice(Commit<PutAlterarCorVertice> commit) {
		try {
			mapVertices.get(commit.operation().getNome()).setCor(
					commit.operation().getCor());
		} finally {
			commit.close();
		}
	}

	public void putAlterarDescricaoVertice(
			Commit<PutAlterarDescricaoVertice> commit) {
		try {
			mapVertices.get(commit.operation().getNome()).setDescricao(
					commit.operation().getDescricao());
		} finally {
			commit.close();
		}
	}

	public void putAlterarPesoVertice(Commit<PutAlterarPesoVertice> commit) {
		try {
			mapVertices.get(commit.operation().getNome()).setPeso(
					commit.operation().getPeso());
		} finally {
			commit.close();
		}
	}

	public void putCriarAresta(Commit<PutCriarAresta> commit) {
		try {
			Aresta aresta = new Aresta(commit.operation().getVertice1(), commit
					.operation().getVertice2(), commit.operation().getPeso(),
					commit.operation().getIsBiDirecional(), commit.operation()
							.getDescricao());

			String key = commit.operation().getVertice1() + "-"
					+ commit.operation().getVertice2();
			mapArestas.put(key, aresta);
		} finally {
			commit.close();
		}
	}

	public void putAdicionarArestaPeloVertice(
			Commit<PutAdicionarArestaPeloVertice> commit) {
		try {
			List<Integer> listVerticesDestino = null;
			int verticeOrigem = commit.operation().getVerticeOrigem();
			int verticeDestino = commit.operation().getVerticeDestino();

			if (mapArestasPorVertice.containsKey(verticeOrigem)) {
				listVerticesDestino = mapArestasPorVertice.get(verticeOrigem);
			} else {
				listVerticesDestino = new ArrayList<Integer>();
			}

			listVerticesDestino.add(verticeDestino);
			mapArestasPorVertice.put(verticeOrigem, listVerticesDestino);
		} finally {
			commit.close();
		}
	}

	public void putAlterarPesoAresta(Commit<PutAlterarPesoAresta> commit) {
		try {
			String key1 = commit.operation().getVertice1() + "-"
					+ commit.operation().getVertice2();

			if (mapArestas.containsKey(key1)) {
				mapArestas.get(key1).setPeso(commit.operation().getPeso());
			}
		} finally {
			commit.close();
		}
	}

	public void putAlterarDescricaoAresta(
			Commit<PutAlterarDescricaoAresta> commit) {
		try {

			String key1 = commit.operation().getVertice1() + "-"
					+ commit.operation().getVertice2();

			if (mapArestas.containsKey(key1)) {
				mapArestas.get(key1).setDescricao(
						commit.operation().getDescricao());
			}
		} finally {
			commit.close();
		}
	}

	public void putSetCusto(Commit<PutSetCusto> commit) {
		try {
			int a, b;
			double c;
			a = commit.operation().getV1();
			b = commit.operation().getV2();
			c = commit.operation().getPeso();

			Aresta aresta = null;
			String key = a + "-" + b;
			if (mapCopia.containsKey(key)) {
				aresta = mapCopia.get(key);
				aresta.setPeso(c);
			} else {
				aresta = new Aresta(a, b, c, false, "FLOYD WARSHALL");
			}

			mapCopia.put(key, aresta);
		} finally {
			commit.close();
		}
	}

	public void putCopia(Commit<PutCopia> commit) {
		try {
			mapCopia.clear();
			Aresta aresta, aux;
			for (Map.Entry<String, Aresta> entry : mapArestas.entrySet()) {
				aux = entry.getValue();
				aresta = new Aresta(aux.getVertice1(), aux.getVertice2(),
						aux.getPeso(), aux.isBiDirecional(), aux.getDescricao());

				mapCopia.put(entry.getKey(), aresta);
			}

		} finally {
			commit.close();
		}
	}
	
	public void putDeletarVertice(Commit<PutDeletarVertice> commit) {
		try {
			mapVertices.remove(commit.operation().getNome());
		} finally {
			commit.close();
		}
	}
	
	public void putRemoveMapArestas(Commit<PutRemoveMapArestas> commit) {
		try {
			mapArestas.remove(commit.operation().getKey());
		} finally {
			commit.close();
		}
	}
	
	public void putRemoveMapArestasPorVertice(Commit<PutRemoveMapArestasPorVertice> commit) {
		try {
			mapArestasPorVertice.get(commit.operation().getV1()).remove((Integer) commit.operation().getV2());
		} finally {
			commit.close();
		}
	}
	
	public void putDeletarArestaPeloVertice(Commit<PutDeletarArestaPeloVertice> commit) {
		try {
			mapArestasPorVertice.remove(commit.operation().getNome());
		} finally {
			commit.close();
		}
	}
	@Override
	public void install(SnapshotReader arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void snapshot(SnapshotWriter arg0) {
		// TODO Auto-generated method stub

	}
}
