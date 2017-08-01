package br.ufu.put;

import io.atomix.copycat.Command;

public class PutDeletarVertice implements Command<Object> {
	private int id;
	
	public PutDeletarVertice(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
}
