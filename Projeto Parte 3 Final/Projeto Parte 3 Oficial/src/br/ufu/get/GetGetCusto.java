package br.ufu.get;
import io.atomix.copycat.Query;

public class GetGetCusto implements Query<Object> {
	private int vertice1;
	private int vertice2;
	
	public GetGetCusto(int vertice1, int vertice2) {
		this.vertice1 = vertice1;
		this.vertice2 = vertice2;
	}

	public int getV1() {
		return this.vertice1;
	}
	
	public int getV2() {
		return this.vertice2;
	}
}