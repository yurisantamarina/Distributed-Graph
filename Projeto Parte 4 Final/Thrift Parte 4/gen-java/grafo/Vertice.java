package grafo;

import grafo.*;

public class Vertice {
	private int id;
	private String nome;
	private String tipo;
	private String cast;
	
	public Vertice(int id, String nome, String tipo, String cast) {
		this.id = id;
		this.nome = nome;
		this.tipo = tipo;
		this.cast = cast;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	
}
