package policia.transito.model;

import java.io.Serializable;

public class Combo implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String id;
	private String value;
	
	public Combo(String id, String valor)
	{
		this.id = id;
		this.value = valor;
	}

	public String getId() {
		return id;
	}

	public String getValue() {
		return value;
	}
	
	
}
