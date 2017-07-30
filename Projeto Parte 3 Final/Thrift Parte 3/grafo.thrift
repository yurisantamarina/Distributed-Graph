namespace java grafo
namespace py grafo

service grafo {
	string criarVertice(1:i32 nome, 2:i32 cor, 3:string descricao, 4:double peso),
	bool existeVertice(1: i32 nome),
	string alterarCorVertice(1:i32 nome, 2:i32 cor),
	string alterarDescricaoVertice(1:i32 nome, 2:string descricao),
	string alterarPesoVertice(1:i32 nome, 2:double peso),
	string consultarVertice(1:i32 nome),
	string deletarVertice(1:i32 nome),
	string deletarAresta(1:i32 vertice1, 2:i32 vertice2),
	string criarAresta(1:i32 vertice1, 2:i32 vertice2, 3:double peso, 4:bool isBiDirecional, 5:string descricao),
	bool existeAresta(1: i32 vertice1, 2: i32 vertice2),
	string alterarPesoAresta(1:i32 vertice1, 2:i32 vertice2, 3:double peso),
	string alterarDescricaoAresta(1:i32 vertice1, 2:i32 vertice2, 3:string descricao),
	string consultarAresta(1:i32 vertice1, 2:i32 vertice2),
	string listarVerticesDeUmaAresta(1:i32 nomeVertice1, 2:i32 nomeVertice2),
	string listarArestasDeUmVertice(1:i32 nomeVertice1),
	string listarVerticesVizinhos(1:i32 nome),
	string printarDadosVertice(1:i32 nome),
	string printarDadosAresta(1:i32 vertice1, 2:i32 vertice2),
	void adicionarArestaPeloVertice(1:i32 verticeOrigem, 2:i32 verticeDestino),
	double getCusto(1:i32 a, 2:i32 b),
	void setCusto(1:i32 a, 2:i32 b, 3:double peso),
	void copia(),
	string caminhoMinimo(1:i32 origem, 2:i32 destino),
	void deletarArestaPeloVertice(1:i32 vertice),
	void removeMapArestasPorVertice(1:i32 vertice1, 2:i32 vertice2),
	void removeMapArestas(1:string key)
	
}
