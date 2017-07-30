package br.ufu.get;
import io.atomix.copycat.Query;

public class GetListaDeVizinhos implements Query<Object> {
	private int nome;

	public GetListaDeVizinhos(int nome) {
		this.nome = nome;
	}

	public int getNome() {
		return this.nome;
	}
	
}