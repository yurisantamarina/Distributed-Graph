package br.ufu.put;

import io.atomix.copycat.Command;

public class PutAlterarPesoAresta implements Command<Object> {
	private int vertice1;
	private int vertice2;
	private double peso;
	
	public PutAlterarPesoAresta(int vertice1, int vertice2, double peso) {
		this.vertice1 = vertice1;
		this.vertice2 = vertice2;
		this.peso = peso;
	}

	public int getVertice1(){
		return this.vertice1;
	}
	
	public int getVertice2(){
		return this.vertice2;
	}
	
	public double getPeso(){
		return this.peso;
	}
}
