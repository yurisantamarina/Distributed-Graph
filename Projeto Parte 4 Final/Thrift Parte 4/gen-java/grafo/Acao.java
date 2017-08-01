package grafo;


public interface Acao {
	public static int CADASTRAR_PESSOA_FILME 		= 1;
	public static int CONSULTA_PESSOA_FILME			= 2;
	public static int DELETAR_PESSOA_FILME 			= 3;
	
	public static int CRIAR_ARESTA 					= 4;
	public static int CONSULTAR_ARESTA				= 5;
	public static int DELETAR_ARESTA				= 6;

	public static int LISTAR_VERTICES_VIZINHOS		= 7;
	public static int LISTAR_FILMES_POR_GRUPO		= 8;
	public static int CAMINHO_MINIMO				= 9;
		
	public static int FINALIZAR						= 0;
}
