package grafo;

import grafo.*;

public class Aresta {
	private int vertice1;
	private int vertice2;
	private double peso;
	private boolean isBiDirecional;
	private String descricao;
	
	public Aresta(int vertice1, int vertice2, double peso, boolean isBiDirecional, String descricao) {
		this.vertice1 = vertice1;
		this.vertice2 = vertice2;
		this.peso = peso;
		this.isBiDirecional = isBiDirecional; 
		this.descricao = descricao;
	}

	public int getVertice1() {
		return vertice1;
	}

	public void setVertice1(int vertice1) {
		this.vertice1 = vertice1;
	}

	public int getVertice2() {
		return vertice2;
	}

	public void setVertice2(int vertice2) {
		this.vertice2 = vertice2;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public boolean isBiDirecional() {
		return isBiDirecional;
	}

	public void setBiDirecional(boolean isBiDirecional) {
		this.isBiDirecional = isBiDirecional;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
