package br.ufu.get;
import io.atomix.copycat.Query;

public class GetListarArestasDeUmVertice implements Query<Object>{
	private int nomeVertice1;

	public GetListarArestasDeUmVertice(int nomeVertice1) {
		this.nomeVertice1 = nomeVertice1;
	}

	public int getNomeVertice1() {
		return this.nomeVertice1;
	}
	
}
