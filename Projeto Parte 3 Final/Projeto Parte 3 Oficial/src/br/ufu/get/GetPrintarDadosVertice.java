package br.ufu.get;
import io.atomix.copycat.Query;

public class GetPrintarDadosVertice implements Query<Object>{
	private int nome;

	public GetPrintarDadosVertice(int nome) {
		this.nome = nome;
	}

	public int getNome() {
		return nome;
	}
}
