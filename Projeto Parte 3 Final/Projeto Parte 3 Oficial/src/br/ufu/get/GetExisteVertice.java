package br.ufu.get;
import io.atomix.copycat.Query;

public class GetExisteVertice implements Query<Object> {
	private int nome;

	public GetExisteVertice(int nome) {
		this.nome = nome;
	}

	public int getNome() {
		return nome;
	}
}