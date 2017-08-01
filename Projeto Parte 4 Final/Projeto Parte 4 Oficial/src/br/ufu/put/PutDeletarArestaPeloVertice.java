package br.ufu.put;

import io.atomix.copycat.Command;

public class PutDeletarArestaPeloVertice implements Command<Object> {
	private int id;
	
	public PutDeletarArestaPeloVertice(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
}
