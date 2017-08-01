package br.ufu.get;
import io.atomix.copycat.Query;

public class GetGetListVizinhos implements Query<Object> {
	private int id;
	
	public GetGetListVizinhos(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
	
}