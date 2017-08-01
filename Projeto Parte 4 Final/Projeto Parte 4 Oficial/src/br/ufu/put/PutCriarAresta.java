package br.ufu.put;

import io.atomix.copycat.Command;

public class PutCriarAresta implements Command<Object> {
	private int id1;
	private int id2;
	private int peso;
	private String descricao;
	private int nota;
	
	public PutCriarAresta(int id1, int id2, int nota) {
		this.id1 = id1;
		this.id2 = id2;
		this.peso = 1;
		this.descricao = "Pessoa " + id1 + " assistiu o filme " + id2;
		this.nota = nota;
	}

	public int getId1() {
		return id1;
	}

	public int getId2() {
		return id2;
	}

	public int getNota() {
		return nota;
	}

	public int getPeso() {
		return peso;
	}

	public String getDescricao() {
		return descricao;
	}


}
