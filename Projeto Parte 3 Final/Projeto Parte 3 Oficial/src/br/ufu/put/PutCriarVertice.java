package br.ufu.put;

import io.atomix.copycat.Command;

public class PutCriarVertice implements Command<Object> {
	private int nome;
	private int cor;
	private String descricao;
	private double peso;
	
	public PutCriarVertice(int nome, int cor, String descricao, double peso) {
		this.nome = nome;
		this.cor = cor;
		this.descricao = descricao;
		this.peso = peso;
	}

	public int getNome() {
		return nome;
	}

	public int getCor() {
		return cor;
	}

	public String getDescricao() {
		return descricao;
	}

	public double getPeso() {
		return peso;
	}

}
