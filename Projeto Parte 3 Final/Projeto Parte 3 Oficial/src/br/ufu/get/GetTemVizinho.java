package br.ufu.get;
import io.atomix.copycat.Query;

public class GetTemVizinho implements Query<Object> {
	private int nome;

	public GetTemVizinho(int nome) {
		this.nome = nome;
	}

	public int getNome() {
		return this.nome;
	}
	
}