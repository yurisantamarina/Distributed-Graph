package grafo;

public class Aresta {
	private int id1;
	private int id2;
	private int peso;
	private String descricao;
	private int nota;
	
	public Aresta(int id1, int id2, int nota) {
		this.id1 = id1;
		this.id2 = id2;
		this.peso = 1;
		this.descricao = "Pessoa " + id1 + " assistiu o filme " + id2;
		this.nota = nota;
	}
	
	public Aresta(int id1, int id2, int nota, int peso) {
		this.id1 = id1;
		this.id2 = id2;
		this.nota = nota;
		this.peso = peso;
	}

	public int getId1() {
		return id1;
	}

	public void setId1(int id1) {
		this.id1 = id1;
	}

	public int getId2() {
		return id2;
	}

	public void setId2(int id2) {
		this.id2 = id2;
	}

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}
	
	public int getPeso() {
		return peso;
	}
	
	public void setPeso(int peso){
		this.peso = peso;
	}

	public String getDescricao() {
		return descricao;
	}

	

}
