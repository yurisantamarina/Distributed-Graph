namespace java trabalhopt1

service trabalhopt1
{

bool criaVertice(1: i32 nome, 2: i32 cor, 3: string descricao, 4: double peso),	
bool criaAresta(1: i32 id, 2: i32 v1, 3: i32 v2, 4: double peso, 5: bool direcionado, 6: string descricao),
bool deletaVertice(1: i32 v1),
double caminhoMinimo(1: i32 v1, 2: i32 v2),
string listaVerticesAresta(1: i32 id),
string listaArestasVertice(1: i32 v1),
string listaVerticesVizinhos(1: i32 v1),

bool alterarCorDeUmVertice(1: i32 v1, 2: i32 novaCor),
bool alterarDescricaoDeUmVertice(1: i32 v1, 2: string novaDescricao),
bool alterarPesoDeUmVertice(1: i32 v1, 2: double novoPeso),
bool alterarDescricaoDeUmaAresta(1: i32 id, 2: string novaDescricao),
bool alterarPesoDeUmaAresta(1: i32 id, 2: double novoPeso)

}
