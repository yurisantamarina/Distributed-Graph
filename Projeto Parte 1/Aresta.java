package trabalhopt1;
import java.util.Scanner;
import java.util.*;
 
public class Aresta {
	int id;
	int v1, v2;
	double peso;
	boolean direcionada;
	String descricao;
	public Aresta(int id, int v1, int v2, double peso, boolean direcionada, String descricao){
		this.id = id;
		this.v1 = v1;
		this.v2 = v2;
		this.peso = peso;
		this.direcionada = direcionada;
		this.descricao = descricao;
	}
	
	public int getId(){
		return this.id;
	}
	
	public int getV1(){
		return this.v1;
	}
	
	public int getV2(){
		return this.v2;
	}
	
	public String getDescricao(){
		return this.descricao;
	}
	
	public double getPeso(){
		return this.peso;
	}
	
	public boolean getDirecionada(){
		return this.direcionada;
	}
	
	
	public void setDescricao(String descricao){
		this.descricao = descricao;
	}
	
	public void setPeso(double peso){
		this.peso = peso;
	}
}
