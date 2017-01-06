package policia.transito.model;

import java.io.Serializable;

public class Condutor implements Serializable
{
	private String idEstadoCondutor;
	private String descricaoEstado;
	
	public Condutor(String idEstado, String descricaoEstado)
	{
		this.idEstadoCondutor = idEstado;
		this.descricaoEstado = descricaoEstado;
	}

	public String getIdEstadoCondutor() {
		return idEstadoCondutor;
	}

	public String getDescricaoEstado() {
		return descricaoEstado;
	}
	
	
}
