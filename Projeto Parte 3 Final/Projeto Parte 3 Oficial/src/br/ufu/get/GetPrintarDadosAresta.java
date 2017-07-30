package br.ufu.get;
import io.atomix.copycat.Query;

public class GetPrintarDadosAresta implements Query<Object>{
	private int vertice1;
	private int vertice2;

	public GetPrintarDadosAresta(int vertice1, int vertice2) {
		this.vertice1 = vertice1;
		this.vertice2 = vertice2;
	}

	public int getVertice1() {
		return this.vertice1;
	}
	
	public int getVertice2() {
		return this.vertice2;
	}
}
