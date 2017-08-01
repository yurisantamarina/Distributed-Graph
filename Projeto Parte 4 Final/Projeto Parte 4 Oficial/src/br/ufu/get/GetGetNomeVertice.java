package br.ufu.get;
import io.atomix.copycat.Query;

public class GetGetNomeVertice implements Query<Object> {
	private int id;
	
	public GetGetNomeVertice(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
	
}