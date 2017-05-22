package trabalhopt1;
 
import org.apache.thrift.TException;
import java.util.HashMap;
import trabalhopt1.*;
 
import java.util.Scanner;
import java.util.*;

public class Grafo implements trabalhopt1.Iface{
	public static Grafo instance = null;
	int MAXVERTICE = 500;
	Aresta matrizAdjacencia[][] = new Aresta[MAXVERTICE][MAXVERTICE];
	double dist[][] = new double[MAXVERTICE][MAXVERTICE];
	Set<Integer> existeVertice = new TreeSet<Integer>();
	Set<Integer> existeAresta = new TreeSet<Integer>();
	Vertice listaVertices[] = new Vertice[MAXVERTICE];
	Aresta listaAresta[] = new Aresta[MAXVERTICE*100];
	boolean atualizado;
	
	public Grafo(){
		atualizado = false;
		for (int i = 0; i < MAXVERTICE; i++){
			for (int j = 0; j < MAXVERTICE; j++){
				matrizAdjacencia[i][j] = null;
			}
		}
	}
	
	public synchronized boolean criaVertice(int nome, int cor, String descricao, double peso){
		if(existeVertice.contains(nome)) return false;
		existeVertice.add(nome);
		
		atualizado = false;
		
		//~ double cnt=0;
		//~ for (double i = 0; i < 4000000000.0; i++)
		//~ {
			//~ cnt++;
		//~ }
		//~ existeVertice.add(nome);
		//~ System.out.println("Size = " + existeVertice.size());
		
		listaVertices[nome] = new Vertice(nome, cor, descricao, peso);
		return true;
	}
	
	public synchronized boolean criaAresta(int id, int v1, int v2, double peso, boolean direcionado, String descricao){
		if(!existeVertice.contains(v1) || !existeVertice.contains(v2)) return false;
		
		if(existeAresta.contains(id)) return false;
		
		atualizado = false;
		
		existeAresta.add(id);
		listaAresta[id] = new Aresta(id, v1, v2, peso, direcionado, descricao);
		
		if(direcionado){
			matrizAdjacencia[v1][v2] = new Aresta(id, v1, v2, peso, direcionado, descricao);
		}else{
			matrizAdjacencia[v1][v2] = new Aresta(id, v1, v2, peso, direcionado, descricao);
			matrizAdjacencia[v2][v1] = new Aresta(id, v1, v2, peso, direcionado, descricao);
		}
		return true;
	}
	
	public synchronized boolean deletaVertice(int v1){
		if(!existeVertice.contains(v1)) return false;
		existeVertice.remove(v1);
		
		atualizado = false;
		Aresta edge;
		for (int i = 0; i < MAXVERTICE; i++)
		{
			edge = matrizAdjacencia[v1][i];
			if(edge != null){
				existeAresta.remove(edge.getId());
				matrizAdjacencia[v1][i] = null;
			}
			
			edge = matrizAdjacencia[i][v1];
			if(edge != null){
				existeAresta.remove(edge.getId());
				matrizAdjacencia[i][v1] = null;
			}
		}
		
		return true;
	}
	
	public synchronized double caminhoMinimo(int v1, int v2){
		if(!existeVertice.contains(v1) || !existeVertice.contains(v2)) return -1.0;
		if(atualizado) return dist[v1][v2];
		
		for (int i = 0; i < MAXVERTICE; i++)
		{
			for (int j = 0; j < MAXVERTICE; j++)
			{
				dist[i][j] = (matrizAdjacencia[i][j]!=null ? matrizAdjacencia[i][j].getPeso() : 1000000000.0);
			}
		}
		
		for(int k = 0; k < MAXVERTICE; k++){
			for(int i = 0; i < MAXVERTICE; i++) {
				for(int j = 0; j < MAXVERTICE; j++) {
					if(dist[i][k] + dist[k][j] < dist[i][j]) {
						dist[i][j] = dist[i][k] + dist[k][j];
					}
				}
			}
		}
		
		atualizado = true;
		return dist[v1][v2];
	}
	
	//Lista as informações dos dois vértices da aresta recuperada pelo id
	public synchronized String listaVerticesAresta(int id){
		String s="";
		if(!existeAresta.contains(id)) return "Aresta inexistente";
		else{
			int v1, v2;
			Aresta edge = listaAresta[id];
			v1 = edge.getV1();
			v2 = edge.getV2();
			
			s += "=====Vértice 1=====\n";
			s += "Nome: "; s += listaVertices[v1].getNome() + "\n";
			s += "Cor: "; s += listaVertices[v1].getCor() + "\n";
			s += "Descrição: "; s += listaVertices[v1].getDescricao() + "\n";
			s += "Peso: "; s += listaVertices[v1].getPeso() + "\n";
			
			s += "=====Vértice 2=====\n";
			s += "Nome: "; s += listaVertices[v2].getNome() + "\n";
			s += "Cor: "; s += listaVertices[v2].getCor() + "\n";
			s += "Descrição: "; s += listaVertices[v2].getDescricao() + "\n";
			s += "Peso: "; s += listaVertices[v2].getPeso() + "\n";
			
			return s;
		}
	}
	
	//Lista todas as arestas que saem de v1 (aresta direcionada de um vértice qualquer para v1 não é listada)
	public synchronized String listaArestasVertice(int v1){
		String s="";
		if(!existeVertice.contains(v1)) return "Vértice inexistente";
		else{
			Aresta edge;
			for (int i = 0; i < MAXVERTICE; i++)
			{
				edge = matrizAdjacencia[v1][i];
				if(edge==null) continue;
				s += "Id: " + edge.getId() + "\n";
				if(edge.getDirecionada()){
					s += "Aresta direcionada de " + edge.getV1() + " para " + edge.getV2() + "\n";
				}else{
					s += "Aresta bidirecional entre " + edge.getV1() + " e " + edge.getV2() + "\n";
				}
				s += "Peso: " + edge.getPeso() + "\n";
				s += "Descrição: " + edge.getDescricao() + "\n\n";
			}
			
			return s;
		}
	}
	
	//Lista todos os vértices atingíveis em 1 passo (passando por uma aresta) a partir de v1 
	public synchronized String listaVerticesVizinhos(int v1){
		String s="";
		if(!existeVertice.contains(v1)) return "Vértice inexistente";
		else{
			int v2;
			Aresta edge;
			s += "Vizinhos: \n";
			for (int i = 0; i < MAXVERTICE; i++)
			{
				edge = matrizAdjacencia[v1][i];
				if(edge==null) continue;
				if(edge.getV1() == v1) v2 = edge.getV2();
				else v2 = edge.getV1();
				s += v2 + " ";
			}
			s += "\n";
			
			return s;
		}
	}
	
	public synchronized boolean alterarCorDeUmVertice(int v1, int novaCor){
		if(!existeVertice.contains(v1)) return false;
		listaVertices[v1].setCor(novaCor);
		return true;
	}
	public synchronized boolean alterarDescricaoDeUmVertice(int v1, String novaDescricao){
		if(!existeVertice.contains(v1)) return false;
		listaVertices[v1].setDescricao(novaDescricao);
		return true;
	}
	public synchronized boolean alterarPesoDeUmVertice(int v1, double novoPeso){
		if(!existeVertice.contains(v1)) return false;
		listaVertices[v1].setPeso(novoPeso);
		return true;
	}
	public synchronized boolean alterarDescricaoDeUmaAresta(int id, String novaDescricao){
		if(!existeAresta.contains(id)) return false;
		
		Aresta edge = listaAresta[id];
		listaAresta[id].setDescricao(novaDescricao);
		int v1 = edge.getV1();
		int v2 = edge.getV2();
		
		if(matrizAdjacencia[v1][v2]!=null){
			matrizAdjacencia[v1][v2].setDescricao(novaDescricao);
		}else if(matrizAdjacencia[v2][v1]!=null){
			matrizAdjacencia[v2][v1].setDescricao(novaDescricao);
		}
		
		return true;
	}
	public synchronized boolean alterarPesoDeUmaAresta(int id, double novoPeso){
		if(!existeAresta.contains(id)) return false;
		atualizado = false;
		
		Aresta edge = listaAresta[id];
		listaAresta[id].setPeso(novoPeso);
		int v1 = edge.getV1();
		int v2 = edge.getV2();
		
		if(edge.getDirecionada()){
			matrizAdjacencia[v1][v2].setPeso(novoPeso);
		}else{
			matrizAdjacencia[v1][v2].setPeso(novoPeso);
			matrizAdjacencia[v2][v1].setPeso(novoPeso);
		}
		
		return true;
	}
}
