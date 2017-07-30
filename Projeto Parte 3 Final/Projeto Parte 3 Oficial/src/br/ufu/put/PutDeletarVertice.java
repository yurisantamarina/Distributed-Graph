package br.ufu.put;

import io.atomix.copycat.Command;

public class PutDeletarVertice implements Command<Object> {
	private int nome;
	
	public PutDeletarVertice(int nome){
		this.nome = nome;
	}
	
	public int getNome(){
		return this.nome;
	}
}
