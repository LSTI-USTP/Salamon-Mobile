package policia.transito.model;

import java.io.Serializable;
import java.util.HashMap;

public class Infracao implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TipoInfracao tipoInfracacao;
	private ModoCoima modoCoima;
	private NivelGravidade nivelGravidade;
	private String nome;
	private String artigo;
	private String categoria;
	private String linha;
	private String id;
	private String descricao;
	private String gravidade;
	private boolean selected;
	
	
	
	public Infracao(HashMap<String, String>  mapaInfracao)
	{
		this.nome = mapaInfracao.get("NAME");
		this.tipoInfracacao = TipoInfracao.valueOf(mapaInfracao.get("TYPE INFRACAO").toString().toUpperCase());
		this.nivelGravidade = null;
	    String idModoCoima = mapaInfracao.get("ID MODO COIMA");
	    if(idModoCoima.equals("1"))
	    	this.modoCoima=ModoCoima.DEFAULT;
	    
	    else if(idModoCoima.equals("2"))
	    	this.modoCoima=ModoCoima.FREQUENTE;
	    else if(idModoCoima.equals("3"))
	    	this.modoCoima=ModoCoima.GRAVITARIO;
	    
		this.artigo = mapaInfracao.get("ARTICLE").toString();
		this.categoria = mapaInfracao.get("CATEGORY").toString();
		this.linha = mapaInfracao.get("LINE").toString();
		this.id = mapaInfracao.get("ID").toString();
		this.descricao = mapaInfracao.get("OTHER INFRACAO").toString();
	}
	
	public Infracao(String id, String gravidade)
	{
		this.id = id;
		this.gravidade = gravidade;
	}
	

	public Infracao(TipoInfracao tipoInfracacao, ModoCoima modoCoima,
			String nome, String artigo, String categoria, String linha,
			String id, String descricao) 
	{
		this.tipoInfracacao = tipoInfracacao;
		this.modoCoima = modoCoima;
		this.nome = nome;
		this.artigo = artigo;
		this.categoria = categoria;
		this.linha = linha;
		this.id = id;
		this.descricao = descricao;
	}




	public ModoCoima getModoCoima() {
		return modoCoima;
	}
	
	public NivelGravidade getNivelGravidade() {
		return nivelGravidade;
	}


	public void setNivelGravidade(NivelGravidade nivelGravidade) {
		this.nivelGravidade = nivelGravidade;
	}


	public void setSelected(boolean selecionado) {
		this.selected = selecionado;
	}


	public TipoInfracao getTipoInfracacao() {
		return tipoInfracacao;
	}

	public String getArtigo() {
		return artigo;
	}

	public String getCategoria() {
		return categoria;
	}
	public String getLinha() {
		return linha;
	}

	public String getNome() {
		return nome;
	}


	public String getId() {
		return id;
	}
	

	@Override
	public String toString() {
		return "Infracao [tipoInfracacao=" + tipoInfracacao + ", modoCoima="
				+ modoCoima + ", nome=" + nome + ", artigo=" + artigo
				+ ", categoria=" + categoria + ", linha=" + linha + ", id="
				+ id + ", descricao=" + descricao + ", gravidade=" + gravidade
				+ ", estado=" + selected + "]";
	}


	public boolean isSelected() {
		return selected;
	}


	public String getDescricao() {
		return descricao;
	}

	public String getGravidade() {
		return gravidade;
	}


	public enum TipoInfracao
	{
		CARTA(1),
		MOTOCICLO(2),
		CARRO(3);
		private final int idTipoInfracao;
		private TipoInfracao(int idTipoInfracao)
		{
			this.idTipoInfracao = idTipoInfracao;
		}
		public int getId()
		{
			return this.idTipoInfracao;
		}
	}
	
	public enum ModoCoima
	{
		GRAVITARIO,
		FREQUENTE,
		DEFAULT
	}
	
	public enum NivelGravidade
	{
		LEVE(1),
		GRAVE(2),
		MUITO_GRAVE(3);
		
		private final int idNivelGravidade;
		private NivelGravidade(int nivel)
		{
			this.idNivelGravidade = nivel;
		}
		
		public int getId()
		{
			return this.idNivelGravidade;
		}
		@Override
		public String toString()
		{
			return name()+" ->"+idNivelGravidade; 
		}
	}
}
