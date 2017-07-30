package br.ufu.put;

import io.atomix.copycat.Command;

public class PutAlterarDescricaoVertice implements Command<Object> {
	private int nome;
	private String descricao;
	
	public PutAlterarDescricaoVertice(int nome, String descricao) {
		this.nome = nome;
		this.descricao = descricao;
	}

	public int getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}

}
