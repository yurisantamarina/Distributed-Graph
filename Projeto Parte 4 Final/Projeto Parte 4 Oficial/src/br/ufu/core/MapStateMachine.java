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

	// OK
	public boolean getExisteVertice(Commit<GetExisteVertice> commit) {
		try {
			return mapVertices.containsKey(commit.operation().getId());
		} finally {
			commit.close();
		}
	}

	// OK
	public boolean getExisteAresta(Commit<GetExisteAresta> commit) {
		try {
			String key1 = commit.operation().getId1() + "-"
					+ commit.operation().getId2();
			return mapArestas.containsKey(key1);
		} finally {
			commit.close();
		}
	}

	// OK
	public int getGetCusto(Commit<GetGetCusto> commit) {
		try {
			String key = commit.operation().getId1() + "-"
					+ commit.operation().getId2();
			if (mapCopia.containsKey(key)) {
				return mapCopia.get(key).getPeso();
			}
			return Integer.MAX_VALUE;
		} finally {
			commit.close();
		}
	}

	// OK
	public boolean getTemVizinho(Commit<GetTemVizinho> commit) {
		try {
			return mapArestasPorVertice.containsKey(commit.operation().getId());
		} finally {
			commit.close();
		}
	}

	// OK
	public List<Integer> getListaDeVizinhos(Commit<GetListaDeVizinhos> commit) {
		try {
			List<Integer> listVerticesDestino = mapArestasPorVertice.get(commit
					.operation().getId());
			return listVerticesDestino;
		} finally {
			commit.close();
		}
	}

	// OK
	public String getPritarDadosVertice(Commit<GetPrintarDadosVertice> commit) {
		try {
			StringBuilder builder = new StringBuilder();
			Vertice vertice = mapVertices.get(commit.operation().getId());
			
			builder.append("ID: " + vertice.getId() + "\n");
			builder.append("Nome: " + vertice.getNome() + "\n");

			if (vertice.getTipo().equals("F")) {
				builder.append("Tipo: Filme\n");
				builder.append("Cast: " + vertice.getCast() + "\n");
			} else {
				builder.append("Tipo: Pessoa\n");
			}

			return builder.toString() + "\n";
		} finally {
			commit.close();
		}
	}

	// OK
	public String getPrintarDadosAresta(Commit<GetPrintarDadosAresta> commit) {

		try {
			Aresta aresta;
			String key = commit.operation().getId1() + "-"
					+ commit.operation().getId2();
			StringBuilder builder = new StringBuilder();
			aresta = mapArestas.get(key);
			
			builder.append("Nota atribuida ao filme pela pessoa: "
					+ aresta.getNota() + "\n");
			
			return builder.toString() + "\n";
		} finally {
			commit.close();
		}
	}

	// OK
	public String getGetTipoVertice(Commit<GetGetTipoVertice> commit) {

		try {
			return mapVertices.get(commit.operation().getId()).getTipo();
		} finally {
			commit.close();
		}
	}

	// OK
	public String getGetNomeVertice(Commit<GetGetNomeVertice> commit) {

		try {
			return mapVertices.get(commit.operation().getId()).getNome();
		} finally {
			commit.close();
		}
	}
	
	public List<Integer> getGetListVizinhos(Commit<GetGetListVizinhos> commit) {

		try {
			return mapArestasPorVertice.get(commit.operation().getId());
		} finally {
			commit.close();
		}
	}

	// OK
	public void putCriarVertice(Commit<PutCriarVertice> commit) {
		try {
			PutCriarVertice operation = commit.operation();
			Vertice vertice = new Vertice(operation.getId(),
					operation.getNome(), operation.getTipo(),
					operation.getCast());
			mapVertices.put(operation.getId(), vertice);
		} finally {
			commit.close();
		}
	}


	// OK
	public void putCriarAresta(Commit<PutCriarAresta> commit) {
		try {
			Aresta aresta = new Aresta(commit.operation().getId1(), commit
					.operation().getId2(), commit.operation().getNota());

			String key = commit.operation().getId1() + "-"
					+ commit.operation().getId2();
			mapArestas.put(key, aresta);
		} finally {
			commit.close();
		}
	}

	// OK
	public void putAdicionarArestaPeloVertice(
			Commit<PutAdicionarArestaPeloVertice> commit) {
		try {
			List<Integer> listVerticesDestino = null;
			int id1 = commit.operation().getId1();
			int id2 = commit.operation().getId2();

			if (mapArestasPorVertice.containsKey(id1)) {
				listVerticesDestino = mapArestasPorVertice.get(id1);
			} else {
				listVerticesDestino = new ArrayList<Integer>();
			}

			listVerticesDestino.add(id2);
			mapArestasPorVertice.put(id1, listVerticesDestino);
		} finally {
			commit.close();
		}
	}

	
	// OK
	public void putSetCusto(Commit<PutSetCusto> commit) {
		try {
			int a, b;
			int c;
			a = commit.operation().getId1();
			b = commit.operation().getId2();
			c = commit.operation().getCusto();

			Aresta aresta = null;
			String key = a + "-" + b;
			if (mapCopia.containsKey(key)) {
				aresta = mapCopia.get(key);
				aresta.setPeso(c);
			} else {
				aresta = new Aresta(a, b, 0, c);
			}

			mapCopia.put(key, aresta);
		} finally {
			commit.close();
		}
	}

	// OK
	public void putCopia(Commit<PutCopia> commit) {
		try {
			mapCopia.clear();
			Aresta aresta, aux;
			for (Map.Entry<String, Aresta> entry : mapArestas.entrySet()) {
				aux = entry.getValue();
				aresta = new Aresta(aux.getId1(), aux.getId2(), 0,
						aux.getPeso());

				mapCopia.put(entry.getKey(), aresta);
			}

		} finally {
			commit.close();
		}
	}

	// OK
	public void putDeletarVertice(Commit<PutDeletarVertice> commit) {
		try {
			mapVertices.remove(commit.operation().getId());
		} finally {
			commit.close();
		}
	}

	// OK
	public void putRemoveMapArestas(Commit<PutRemoveMapArestas> commit) {
		try {
			mapArestas.remove(commit.operation().getKey());
		} finally {
			commit.close();
		}
	}

	// OK
	public void putRemoveMapArestasPorVertice(
			Commit<PutRemoveMapArestasPorVertice> commit) {
		try {
			mapArestasPorVertice.get(commit.operation().getId1()).remove(
					(Integer) commit.operation().getId2());
		} finally {
			commit.close();
		}
	}

	// OK
	public void putDeletarArestaPeloVertice(
			Commit<PutDeletarArestaPeloVertice> commit) {
		try {
			mapArestasPorVertice.remove(commit.operation().getId());
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
