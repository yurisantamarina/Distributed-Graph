package br.ufu.put;

import io.atomix.copycat.Command;

public class PutRemoveMapArestas implements Command<Object> {
	private String key;
	
	public PutRemoveMapArestas(String key){
		this.key = key;
	}
	
	public String getKey(){
		return this.key;
	}
}
