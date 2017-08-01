package br.ufu.get;
import io.atomix.copycat.Query;

public class GetExisteVertice implements Query<Object> {
	private int id;

	public GetExisteVertice(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}