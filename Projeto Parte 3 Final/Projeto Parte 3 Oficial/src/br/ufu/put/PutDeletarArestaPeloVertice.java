package br.ufu.put;

import io.atomix.copycat.Command;

public class PutDeletarArestaPeloVertice implements Command<Object> {
	private int nome;
	
	public PutDeletarArestaPeloVertice(int nome){
		this.nome = nome;
	}
	
	public int getNome(){
		return this.nome;
	}
}
