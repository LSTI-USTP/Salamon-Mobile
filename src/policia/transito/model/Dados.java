package policia.transito.model;

import java.io.Serializable;

public class Dados implements Serializable
{
	private int estadoCondutor;
	private int existenciaCarta;
	private int incompatibilidade;
	private String numCarta;
	private int categoriaVeiculo;
	private int livrete;
	private String numMatricula;
	
	public Dados(int estadoCondutor, int existenciaCarta,int incompatibilidade, String numCarta, int categoriaVeiculo,int livrete, String numMatricula) 
	{
		this.estadoCondutor = estadoCondutor;
		this.existenciaCarta = existenciaCarta;
		this.incompatibilidade = incompatibilidade;
		this.numCarta = numCarta;
		this.categoriaVeiculo = categoriaVeiculo;
		this.livrete = livrete;
		this.numMatricula = numMatricula;
	}

	public int getEstadoCondutor() {
		return estadoCondutor;
	}

	public int getExistenciaCarta() {
		return existenciaCarta;
	}

	public int getIncompatibilidade() {
		return incompatibilidade;
	}

	public String getNumCarta() {
		return numCarta;
	}

	public int getCategoriaVeiculo() {
		return categoriaVeiculo;
	}

	public int getLivrete() {
		return livrete;
	}

	public String getNumMatricula() {
		return numMatricula;
	}
	
	
	
	
}
