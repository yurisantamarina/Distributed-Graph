package br.ufu.get;
import io.atomix.copycat.Query;

public class GetTemVizinho implements Query<Object> {
	private int id;

	public GetTemVizinho(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
	
}