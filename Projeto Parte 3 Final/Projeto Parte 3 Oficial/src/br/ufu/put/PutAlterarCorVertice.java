package br.ufu.put;

import io.atomix.copycat.Command;

public class PutAlterarCorVertice implements Command<Object> {
	private int nome;
	private int cor;
	
	public PutAlterarCorVertice(int nome, int cor) {
		this.nome = nome;
		this.cor = cor;
	}

	public int getNome() {
		return nome;
	}

	public int getCor() {
		return cor;
	}

}
