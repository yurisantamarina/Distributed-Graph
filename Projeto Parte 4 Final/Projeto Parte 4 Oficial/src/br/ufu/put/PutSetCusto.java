package br.ufu.put;

import io.atomix.copycat.Command;

public class PutSetCusto implements Command<Object> {
	private int id1;
	private int id2;
	private int custo;
	
	public PutSetCusto(int id1, int id2, int custo) {
		this.id1 = id1;
		this.id2 = id2;
		this.custo = custo;
	}

	public int getId1() {
		return id1;
	}

	public int getId2() {
		return id2;
	}

	public int getCusto() {
		return custo;
	}
	
	
}
