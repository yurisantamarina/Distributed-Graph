package br.ufu.get;
import io.atomix.copycat.Query;

public class GetGetTipoVertice implements Query<Object> {
	private int id;
	
	public GetGetTipoVertice(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
	
}