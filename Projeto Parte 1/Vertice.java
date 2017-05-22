package trabalhopt1;
import java.util.Scanner;
import java.util.*;
 
public class Vertice {
	int nome, cor;
	String descricao;
	double peso;
	public Vertice(int a, int b, String c, double d){
		this.nome = a;
		this.cor = b;
		this.descricao = c;
		this.peso = d;
	}
	
	public int getNome(){
		return this.nome;
	}
	
	public int getCor(){
		return this.cor;
	}
	
	public String getDescricao(){
		return this.descricao;
	}
	
	public double getPeso(){
		return this.peso;
	}
	
	
	public void setCor(int cor){
		this.cor = cor;
	}
	
	public void setDescricao(String descricao){
		this.descricao = descricao;
	}
	
	public void setPeso(double peso){
		this.peso = peso;
	}
}
