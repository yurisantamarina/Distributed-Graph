package grafo;

import java.util.Scanner;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

import grafo.*;

public class Client {

	public static void main(String[] args) {
		try {
			Scanner sc = null;
			sc = new Scanner(System.in);
			
			
			System.out.print("Digite o id do servidor que deseja se conectar (0, 1 ou 2): ");
			int idServidor = sc.nextInt();
			
			int porta = 0;
			if(idServidor==0){
				porta = 9090; 
				System.out.println("Conectando-se ao servidor 0");
			}else if(idServidor==1){
				porta = 9091; 
				System.out.println("Conectando-se ao servidor 1");
			}else if(idServidor==2){
				porta = 9092; 
				System.out.println("Conectando-se ao servidor 1");
			}else{
				porta = 9090;
				System.out.println("Servidor inválido... Conectando-se ao servidor 0");
			}
			
			TTransport transport = new TSocket("localhost", porta);
			transport.open();
 
			TProtocol protocol = new  TBinaryProtocol(transport);
			grafo.Client client = new grafo.Client(protocol);
			
			
			String result = "";

			try {
				
				int opcaoInicial = -1;

				//Variaveis auxiliares para entrada de dados
				int tipo, id, nota, idFilme, idPessoa, idPessoa2;
				String cast, nome, listaIds;
				
				while (opcaoInicial != Acao.FINALIZAR) {
					try {

						// Operacao de vertice
						System.out.println("==================================");
						System.out.println("Escolha uma das opcoes: ");
						System.out.println("==================================\n");
						System.out.println("1 - Cadastrar pessoa/filme");
						System.out.println("2 - Consultar pessoa/filme");
						System.out.println("3 - Deletar pessoa/filme");

						// Operacoes de aresta
						System.out.println("4 - Informar filme que uma pessoa assistiu");//aresta
						System.out.println("5 - Consultar dados da pessoa e do filme");
						System.out.println("6 - Deletar relação pessoa/filme"); 

						// Operacoes genericas
						System.out.println("7 - Listar filmes assitidos por uma pessoa ou\npessoas que assitiram determinado filme");//LISTAR_VERTICES_VIZINHOS
						System.out.println("8 - Listar filmes de um grupo de pessoas");
						System.out.println("9 - Caminho minimo");

						System.out.println("0 - Sair");

						opcaoInicial = sc.nextInt();

						switch (opcaoInicial) {
						case Acao.CADASTRAR_PESSOA_FILME:
							System.out.print("Deseja cadastrar pessoa ou filme? (0 - pessoa / 1 - filme): ");
							tipo = sc.nextInt();
							
							if(tipo == 0){
								System.out.println("Digite o ID da pessoa: ");
								id = sc.nextInt();
								sc.nextLine();
								
								System.out.println("Digite o nome da pessoa: ");
								nome = sc.nextLine();
								
								result = client.criarVertice(id, nome, "P", "");
							}else if(tipo == 1){
								System.out.println("Digite o ID do filme: ");
								id = sc.nextInt();
								sc.nextLine();
								
								System.out.println("Digite o nome do filme: ");
								nome = sc.nextLine();
								
								System.out.println("Digite o cast do filme (elenco): ");
								cast = sc.nextLine();
								
								result = client.criarVertice(id, nome, "F", cast);
							}else{
								result = "Valor inválido!";
							}
							
							
							
							break;

						case Acao.CONSULTA_PESSOA_FILME:
							System.out.print("Informe o ID da pessoa/filme: ");
							id = sc.nextInt();
							
							result = client.consultarVertice(id);
							break;
							
						case Acao.DELETAR_PESSOA_FILME:
							System.out.print("Informe o ID da pessoa/filme: ");
							id = sc.nextInt();
							
							result = client.deletarVertice(id);
							break;

						case Acao.CRIAR_ARESTA:
							System.out.print("Informe o ID da pessoa: ");
							idPessoa = sc.nextInt();
							
							System.out.print("Informe o ID do filme: ");
							idFilme = sc.nextInt();
							
							System.out.print("Informe o a nota dada ao filme pela pessoa (entre 0 e 10): ");
							nota = sc.nextInt();
							
							if(nota < 0 || nota > 10){
								result = "Nota inválida";
							}else{
								result = client.criarAresta(idPessoa, idFilme, nota);
								result = client.criarAresta(idFilme, idPessoa, nota);
							}
							
							break;

						case Acao.CONSULTAR_ARESTA:
							System.out.print("Informe o ID da pessoa: ");
							idPessoa = sc.nextInt();
							
							System.out.print("Informe o ID do filme: ");
							idFilme = sc.nextInt();
							
							result = client.consultarAresta(idPessoa, idFilme);
							break;
							
						case Acao.DELETAR_ARESTA:
							System.out.print("Informe o ID da pessoa: ");
							idPessoa = sc.nextInt();
							
							System.out.print("Informe o ID do filme: ");
							idFilme = sc.nextInt();
							
							result = client.deletarAresta(idPessoa, idFilme);
							result = client.deletarAresta(idFilme, idPessoa);
							break;

						case Acao.LISTAR_VERTICES_VIZINHOS:
							System.out.print("Informe o ID da pessoa/filme: ");
							id = sc.nextInt();
							
							result = client.listarVerticesVizinhos(id);
							break;
						
						case Acao.LISTAR_FILMES_POR_GRUPO:
							sc.nextLine();
							System.out.println("Informe os IDs das pessoas separados por espaço");
							listaIds = sc.nextLine();
							
							result = client.listarFilmesPorGrupo(listaIds);
							break;
						case Acao.CAMINHO_MINIMO:
							System.out.print("Informe o ID da pessoa 1: ");
							idPessoa = sc.nextInt();
							
							System.out.print("Informe o ID da pessoa 2: ");
							idPessoa2 = sc.nextInt();
							
							result = client.caminhoMinimo(idPessoa, idPessoa2);
							
							break;
						default:
							if (opcaoInicial != Acao.FINALIZAR) {
								System.out.println("Opcao invalida!");
							}
							break;
						}
						System.out.println(result);
						System.out.println("");
					} catch (Exception e) {
						System.out.println("Ocorreu um erro inesperado. Tente novamente!\n");
						sc.nextLine();
					}
				}
			} finally {
				if (sc != null) {
					sc.close();
				}
				System.out.println("Fim do programaa!");
			}
			transport.close();
		} catch (TException x) {
			x.printStackTrace();
		}
	}
}
