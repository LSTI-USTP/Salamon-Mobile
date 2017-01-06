package policia.transito.model;

import java.io.Serializable;

public class Veiculo implements Serializable
{
	private String idCategoria;
	private String categoriaDescricao;
	private String	livrete;
	private String numMatricula;
	
	public Veiculo(String idCategoria,String categoriaDesc, String livrete,String numMatricula)
	{
		this.idCategoria = idCategoria;
		this.categoriaDescricao = categoriaDesc;
		this.livrete = livrete;
		this.numMatricula = numMatricula;
	}
	


	public String getIdCategoria() {
		return idCategoria;
	}

	public String getLivrete() {
		return livrete;
	}

	public String getNumMatricula() {
		return numMatricula;
	}

	public String getCategoriaDescricao() {
		return categoriaDescricao;
	}
	
	
	
}
