package br.ufu.get;
import io.atomix.copycat.Query;

public class GetListaDeVizinhos implements Query<Object> {
	private int id;

	public GetListaDeVizinhos(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
	
}