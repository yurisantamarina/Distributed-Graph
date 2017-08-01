package br.ufu.get;
import io.atomix.copycat.Query;

public class GetPrintarDadosVertice implements Query<Object>{
	private int id;

	public GetPrintarDadosVertice(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
