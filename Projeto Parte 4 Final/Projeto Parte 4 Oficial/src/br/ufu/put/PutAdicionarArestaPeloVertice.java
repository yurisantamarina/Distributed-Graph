package br.ufu.put;

import io.atomix.copycat.Command;

public class PutAdicionarArestaPeloVertice implements Command<Object> {
	private int id1;
	private int id2;
	
	public PutAdicionarArestaPeloVertice(int id1, int id2) {
		this.id1 = id1;
		this.id2 = id2;
	}

	public int getId1(){
		return this.id1;
	}
	
	public int getId2(){
		return this.id2;
	}

}
