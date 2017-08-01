package grafo;

import grafo.*;

public class Vertice {
	private int nome;
	private int cor;
	private String descricao;
	private double peso;
	
	public Vertice(int nome, int cor, String descricao, double peso) {
		this.nome = nome;
		this.cor = cor;
		this.descricao = descricao;
		this.peso = peso;
	}

	public int getNome() {
		return nome;
	}

	public void setNome(int nome) {
		this.nome = nome;
	}

	public int getCor() {
		return cor;
	}

	public void setCor(int cor) {
		this.cor = cor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}
}
