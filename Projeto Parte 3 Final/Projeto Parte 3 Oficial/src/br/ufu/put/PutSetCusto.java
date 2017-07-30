package br.ufu.put;

import io.atomix.copycat.Command;

public class PutSetCusto implements Command<Object> {
	private int vertice1;
	private int vertice2;
	private double peso;
	
	public PutSetCusto(int vertice1, int vertice2, double peso) {
		this.vertice1 = vertice1;
		this.vertice2 = vertice2;
		this.peso = peso;
	}
	
	public int getV1(){
		return this.vertice1;
	}
	
	public int getV2(){
		return this.vertice2;
	}
	
	public double getPeso(){
		return this.peso;
	}
}
