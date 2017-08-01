namespace java grafo
namespace py grafo

service grafo {
	string criarVertice(1:i32 id, 2:string nome, 3:string tipo, 4:string cast),
	bool existeVertice(1: i32 id),
	string consultarVertice(1:i32 id),
	string deletarVertice(1:i32 id),
	string getTipoVertice(1:i32 id),
	string criarAresta(1:i32 id1, 2:i32 id2, 3:i32 nota),
	bool existeAresta(1: i32 id1, 2: i32 id2),
	string consultarAresta(1:i32 id1, 2:i32 id2),
	string deletarAresta(1:i32 id1, 2:i32 id2),
	
	string listarVerticesVizinhos(1:i32 nome),
	string printarDadosVertice(1:i32 nome),
	string printarDadosAresta(1:i32 vertice1, 2:i32 vertice2),
	void adicionarArestaPeloVertice(1:i32 verticeOrigem, 2:i32 verticeDestino),
	i32 getCusto(1:i32 a, 2:i32 b),
	void setCusto(1:i32 a, 2:i32 b, 3:i32 peso),
	void copia(),
	string getNomeVertice(1:i32 id),
	string caminhoMinimo(1:i32 origem, 2:i32 destino),
	void deletarArestaPeloVertice(1:i32 vertice),
	void removeMapArestasPorVertice(1:i32 vertice1, 2:i32 vertice2),
	void removeMapArestas(1:string key),
	string listarFilmesPorGrupo(1:string listaIds),
	list<i32> getListVizinhos(1:i32 id)
	
}
