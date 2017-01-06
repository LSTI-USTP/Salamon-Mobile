package policia.transito.model;

import java.io.Serializable;

public class Fiscalizacao implements Serializable
{

	private static final long serialVersionUID = 1L;
	private int id;
	private String numCarta;
	private String numMatricula;
	private int livrete;
	private int incompatibilidadeCarta;
	private int estadoCondutor;
	private String descricaoEstadoCondutor;
	private int categoriaVeiculo;
	private String descricaoCategoriaVeiculo;
	private int existenciaCarta;
	private String latitude;
	private String longitude;
	private String zonaFiscalizacao;
	private String nomeCondutor;
	
	public Fiscalizacao()
	{	
	}
	
	
	
	public String getNomeCondutor() {
		return nomeCondutor;
	}



	public void setNomeCondutor(String nomeCondutor) {
		this.nomeCondutor = nomeCondutor;
	}



	public void setNumCarta(String numCarta) {
		this.numCarta = numCarta;
	}


	public void setNumMatricula(String numMatricula) {
		this.numMatricula = numMatricula;
	}

	

	public String getLatitude() {
		return latitude;
	}


	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}


	public String getLongitude() {
		return longitude;
	}


	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}


	public String getZonaFiscalizacao() {
		return zonaFiscalizacao;
	}


	public void setZonaFiscalizacao(String zonaFiscalizacao) {
		this.zonaFiscalizacao = zonaFiscalizacao;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public void setLivrete(int livrete) {
		this.livrete = livrete;
	}


	public void setIncompatibilidadeCarta(int incompatibilidadeCarta) {
		this.incompatibilidadeCarta = incompatibilidadeCarta;
	}


	public void setEstadoCondutor(int estadoCondutor) {
		this.estadoCondutor = estadoCondutor;
	}


	public void setDescricaoEstadoCondutor(String descricaoEstadoCondutor) {
		this.descricaoEstadoCondutor = descricaoEstadoCondutor;
	}


	public void setCategoriaVeiculo(int categoriaVeiculo) {
		this.categoriaVeiculo = categoriaVeiculo;
	}


	public void setDescricaoCategoriaVeiculo(String descricaoCategoriaVeiculo) {
		this.descricaoCategoriaVeiculo = descricaoCategoriaVeiculo;
	}


	public void setExistenciaCarta(int existenciaCarta) {
		this.existenciaCarta = existenciaCarta;
	}


	public String getNumCarta() {
		return numCarta;
	}

	public String getNumMatricula() {
		return numMatricula;
	}

	public int getLivrete() {
		return livrete;
	}

	public int getIncompatibilidadeCarta() {
		return incompatibilidadeCarta;
	}

	public int getEstadoCondutor() {
		return estadoCondutor;
	}

	public String getDescricaoEstadoCondutor() {
		return descricaoEstadoCondutor;
	}

	public int getCategoriaVeiculo() {
		return categoriaVeiculo;
	}

	public String getDescricaoCategoriaVeiculo() {
		return descricaoCategoriaVeiculo;
	}

	public int getExistenciaCarta() {
		return existenciaCarta;
	}


	@Override
	public String toString() {
		return "Fiscalizacao [numCarta=" + numCarta + ", numMatricula="
				+ numMatricula + ", livrete=" + livrete
				+ ", incompatibilidadeCarta=" + incompatibilidadeCarta
				+ ", estadoCondutor=" + estadoCondutor
				+ ", descricaoEstadoCondutor=" + descricaoEstadoCondutor
				+ ", categoriaVeiculo=" + categoriaVeiculo
				+ ", descricaoCategoriaVeiculo=" + descricaoCategoriaVeiculo
				+ ", existenciaCarta=" + existenciaCarta + "]";
	}
	
	
	
	
}
