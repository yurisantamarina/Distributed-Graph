package br.ufu.put;

import io.atomix.copycat.Command;

public class PutCriarAresta implements Command<Object> {
	private int vertice1;
	private int vertice2;
	private double peso;
	private boolean isBiDirecional;
	private String descricao;
	
	public PutCriarAresta(int vertice1, int vertice2, double peso, boolean isBiDirecional, String descricao) {
		this.vertice1 = vertice1;
		this.vertice2 = vertice2;
		this.peso = peso;
		this.isBiDirecional = isBiDirecional; 
		this.descricao = descricao;
	}

	public int getVertice1() {
		return vertice1;
	}

	public int getVertice2() {
		return vertice2;
	}

	public double getPeso() {
		return peso;
	}

	public boolean getIsBiDirecional() {
		return isBiDirecional;
	}
	public String getDescricao() {
		return descricao;
	}

}
