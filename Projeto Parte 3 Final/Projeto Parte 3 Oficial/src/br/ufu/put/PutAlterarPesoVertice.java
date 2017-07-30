package br.ufu.put;

import io.atomix.copycat.Command;

public class PutAlterarPesoVertice implements Command<Object> {
	private int nome;
	private double peso;
	
	public PutAlterarPesoVertice(int nome, double peso) {
		this.nome = nome;
		this.peso = peso;
	}

	public int getNome() {
		return nome;
	}

	public double getPeso() {
		return peso;
	}

}
