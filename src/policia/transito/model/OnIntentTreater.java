package policia.transito.model;

import java.util.HashMap;

import android.view.View;

public interface OnIntentTreater 
{
	/**
	 * Tratar o evento de uma view aqui
	 * @param view o parameto 
	 * @param cod codigo do evento
	 * @param values a ser tratado no evento
	 */
	public void treat(View view, EventsCod cod, HashMap<String, Object> values);
	
	
	public enum EventsCod
	{
		CLICK_CARTA,
		CLICK_MATRICULA,
		CLICK_INFO_CARTA,
		CLICK_INFO_MATRICULA,
		CLICK_NOME_CONDUTOR
	}
}
