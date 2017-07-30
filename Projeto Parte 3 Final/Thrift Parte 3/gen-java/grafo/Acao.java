package grafo;


public interface Acao {
	public static int CRIAR_VERTICE 				= 1;
	public static int ALTERAR_COR_VERTICE 			= 2;
	public static int ALTERAR_DESCRICAO_VERTICE		= 3;
	public static int ALTERAR_PESO_VERTICE 			= 4;
	public static int CONSULTAR_VERTICE				= 5;
	public static int DELETAR_VERTICE 				= 6;
	
	public static int CRIAR_ARESTA 					= 7;
	public static int ALTERAR_PESO_ARESTA 			= 8;
	public static int ALTERAR_DESCRICAO_ARESTA		= 9;
	public static int CONSULTAR_ARESTA				= 10;
	public static int DELETAR_ARESTA				= 11;

	public static int LISTAR_VERTICES_DE_UMA_ARESTA	= 12;
	public static int LISTAR_ARESTAS_DE_UM_VERTICE 	= 13;
	public static int LISTAR_VERTICES_VIZINHOS		= 14;
	public static int CAMINHO_MINIMO				= 15;
		
	public static int FINALIZAR						= 0;
}
