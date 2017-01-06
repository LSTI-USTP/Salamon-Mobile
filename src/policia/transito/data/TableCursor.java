package policia.transito.data;

import java.util.HashMap;

public interface TableCursor 
{
	/**
	 * Na linha da proxima selecao do mapa
	 * @param mapData os dados da linha
	 * @param codSelection O codigo da selecao
	 */
	public boolean onNextRow(HashMap<String, Object> mapData, int codSelection);
}
