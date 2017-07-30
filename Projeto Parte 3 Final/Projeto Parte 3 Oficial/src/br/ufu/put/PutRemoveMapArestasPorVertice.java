package br.ufu.put;

import io.atomix.copycat.Command;

public class PutRemoveMapArestasPorVertice implements Command<Object> {
	private int v1;
	private int v2;
	
	public PutRemoveMapArestasPorVertice(int v1, int v2){
		this.v1 = v1;
		this.v2 = v2;
	}
	
	public int getV1(){
		return this.v1;
	}
	
	public int getV2(){
		return this.v2;
	}
}
