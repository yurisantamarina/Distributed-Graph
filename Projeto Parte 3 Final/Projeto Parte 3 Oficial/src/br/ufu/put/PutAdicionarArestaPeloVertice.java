package br.ufu.put;

import io.atomix.copycat.Command;

public class PutAdicionarArestaPeloVertice implements Command<Object> {
	private int verticeOrigem;
	private int verticeDestino;
	
	public PutAdicionarArestaPeloVertice(int verticeOrigem, int verticeDestino) {
		this.verticeOrigem = verticeOrigem;
		this.verticeDestino = verticeDestino;
	}

	public int getVerticeOrigem(){
		return this.verticeOrigem;
	}
	
	public int getVerticeDestino(){
		return this.verticeDestino;
	}

}
