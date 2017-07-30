package br.ufu.put;

import io.atomix.copycat.Command;

public class PutAlterarDescricaoAresta implements Command<Object> {
	private int vertice1;
	private int vertice2;
	private String descricao;
	
	public PutAlterarDescricaoAresta(int vertice1, int vertice2, String descricao) {
		this.vertice1 = vertice1;
		this.vertice2 = vertice2;
		this.descricao = descricao;
	}

	public int getVertice1(){
		return this.vertice1;
	}
	
	public int getVertice2(){
		return this.vertice2;
	}
	
	public String getDescricao(){
		return this.descricao;
	}
}
