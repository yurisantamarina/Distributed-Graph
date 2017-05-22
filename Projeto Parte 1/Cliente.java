package trabalhopt1;
import java.util.Scanner;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import trabalhopt1.*;
 
public class Cliente {
	
	public static void main(String [] args) {
		try{
			TTransport transport = new TSocket("localhost", 9090);
			transport.open();
	 
			TProtocol protocol = new  TBinaryProtocol(transport);
			trabalhopt1.Client client = new trabalhopt1.Client(protocol);
			
			Scanner sc = new Scanner(System.in);
			int op, id, v1, v2, dir;
			int nome, cor;
			boolean direcionada;
			String descricao;
			double peso;
			
			while (true)
			{
				System.out.println("\n1 - Criar vértice");
				System.out.println("2 - Criar aresta");
				System.out.println("3 - Deletar vértice");
				System.out.println("4 - Caminho mínimo");
				System.out.println("5 - Listar vértices de uma aresta");
				System.out.println("6 - Listar arestas de um vértice");
				System.out.println("7 - Listar vértices vizinhos");
				System.out.println("8 - Alterar cor de um vértice");
				System.out.println("9 - Alterar descrição de um vértice");
				System.out.println("10 - Alterar peso de um vértice");
				System.out.println("11 - Alterar descrição de uma aresta");
				System.out.println("12 - Alterar peso de uma aresta");
				System.out.println("0 - Sair");
				System.out.print("Digite uma opção: ");
				op = sc.nextInt();
				if(op==0) break;
				
				if(op==1){
					
					System.out.print("Digite o nome do vértice: ");
					nome = sc.nextInt();
					System.out.print("Digite a cor do vértice: ");
					cor = sc.nextInt();
					System.out.print("Digite a descrição do vértice: ");
					sc.nextLine();
					descricao = sc.nextLine();
					System.out.print("Digite o peso do vértice: ");
					peso = sc.nextDouble();
					if(client.criaVertice(nome, cor, descricao, peso)) 
						System.out.println("Vértice criado!!");
					else 
						System.out.println("Já existe um vértice com esse nome");
					
				}else if(op==2){
					
					System.out.print("Digite o id da aresta: ");
					id = sc.nextInt();
					System.out.print("Digite o nome do primeiro vértice: ");
					v1 = sc.nextInt();
					System.out.print("Digite o nome do segundo vértice: ");
					v2 = sc.nextInt();
					System.out.print("Digite o peso da aresta: ");
					peso = sc.nextDouble();
					System.out.print("A aresta é direcionada (1) ou  bidirecional (2): ");
					dir = sc.nextInt();
					if(dir==1) direcionada = true;
					else direcionada = false;
					System.out.print("Digite a descrição da aresta: ");
					sc.nextLine();
					descricao = sc.nextLine();
					if(client.criaAresta(id, v1, v2, peso, direcionada, descricao)) 
						System.out.println("Aresta criada!!");
					else 
						System.out.println("Não foi possível criar a aresta");
					
				}else if(op==3){
					
					System.out.print("Digite o nome do vértice: ");
					nome = sc.nextInt();
					if(client.deletaVertice(nome))
						System.out.println("Vértice deletado!!");
					else
						System.out.println("Vértice inexistente");
					
				}else if(op==4){
					
					System.out.print("Digite o nome do primeiro vértice: ");
					v1 = sc.nextInt();
					System.out.print("Digite o nome do segundo vértice: ");
					v2 = sc.nextInt();
					
					double d = client.caminhoMinimo(v1, v2);
					if(d < 0) 
						System.out.println("Um dos vértices não existe");
					else if(d > 1000000)
						System.out.println("Não há caminho entre " + v1 + " e " + v2);
					else
						System.out.println("Caminho mínimo entre " + v1 + " e " + v2 + " = " + d);
					
				}else if(op==5){
					
					System.out.print("Digite o id da aresta: ");
					id = sc.nextInt();
					System.out.println(client.listaVerticesAresta(id));
					
				}else if(op==6){
					
					System.out.print("Digite o nome do vértice: ");
					nome = sc.nextInt();
					System.out.println(client.listaArestasVertice(nome));
					
				}else if(op==7){
					
					System.out.print("Digite o nome do vértice: ");
					nome = sc.nextInt();
					System.out.println(client.listaVerticesVizinhos(nome));
					
				}else if(op==8){
					System.out.print("Digite o nome do vértice: ");
					nome = sc.nextInt();
					System.out.print("Digite a nova cor do vértice: ");
					cor = sc.nextInt();
					if(client.alterarCorDeUmVertice(nome, cor))
						System.out.println("Cor alterada!!");
					else
						System.out.println("Vértice inexistente");
					
				}else if(op==9){
					System.out.print("Digite o nome do vértice: ");
					nome = sc.nextInt();
					System.out.print("Digite a nova descrição do vértice: ");
					sc.nextLine();
					descricao = sc.nextLine();
					if(client.alterarDescricaoDeUmVertice(nome, descricao))
						System.out.println("Descrição alterada!!");
					else
						System.out.println("Vértice inexistente");
					
				}else if(op==10){
					System.out.print("Digite o nome do vértice: ");
					nome = sc.nextInt();
					System.out.print("Digite o novo peso do vértice: ");
					peso = sc.nextDouble();
					if(client.alterarPesoDeUmVertice(nome, peso))
						System.out.println("Peso alterado!!");
					else
						System.out.println("Vértice inexistente");
					
				}else if(op==11){
					System.out.print("Digite o id da aresta: ");
					id = sc.nextInt();
					System.out.print("Digite a nova descrição da aresta: ");
					sc.nextLine();
					descricao = sc.nextLine();
					if(client.alterarDescricaoDeUmaAresta(id, descricao))
						System.out.println("Descrição alterada!!");
					else
						System.out.println("Aresta inexistente");
					
					
				}else if(op==12){
					System.out.print("Digite o id da aresta: ");
					id = sc.nextInt();
					System.out.print("Digite o novo peso da aresta: ");
					peso = sc.nextDouble();
					if(client.alterarPesoDeUmaAresta(id, peso))
						System.out.println("Peso alterado!!");
					else
						System.out.println("Aresta inexistente");
					
				}
			}
		}catch (TException x) {
			x.printStackTrace();
		}
	}
}
