package br.ufu.put;

import io.atomix.copycat.Command;

public class PutCriarVertice implements Command<Object> {
	private int id;
	private String nome;
	private String tipo;
	private String cast;
	
	public PutCriarVertice(int id, String nome, String tipo, String cast) {
		this.id = id;
		this.nome = nome;
		this.tipo = tipo;
		this.cast = cast;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getTipo() {
		return tipo;
	}

	public String getCast() {
		return cast;
	}

	

}
