package policia.transito.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import policia.transito.R;
import policia.transito.model.ItemList;

public class ItemCategoriaInfracao implements ItemList
{

	private View currentView;
	private String categoryName;
	private int totalSelectd;
	private TextView viewName;
	private TextView viewNumSeleted;

	public ItemCategoriaInfracao(String categoria) 
	{
		this.categoryName = categoria;
	}

	@Override
	public View prepareView(LayoutInflater inflater, Context appContext, ViewGroup viewGroup, View enterView, int indexPosition) 
	{
		this.currentView =  inflater.inflate(R.layout.items_dinamics, null);
		this.currentView.findViewById(R.id.itemGroupInfacao).setVisibility(View.VISIBLE);
		this.viewName = (TextView) this.currentView.findViewById(R.id.tvItemGroupInfracaoNome);
		this.viewNumSeleted = (TextView) currentView.findViewById(R.id.itemGroupInfacaoNumSelecionados);
		
		
		this.viewName.setText(this.categoryName);
		if(this.totalSelectd == 0)
			this.viewNumSeleted.setVisibility(View.GONE);
		this.viewNumSeleted.setText(totalSelectd+"");
		return this.currentView;
	}
	
	public void added()
	{
		this.totalSelectd++;
		if(this.totalSelectd >0)
			this.viewNumSeleted.setVisibility(View.VISIBLE);
		this.viewNumSeleted.setText(totalSelectd+"");
	}
	
	public void removed()
	{
		this.totalSelectd--;
		if(this.totalSelectd == 0)
			this.viewNumSeleted.setVisibility(View.GONE);
		this.viewNumSeleted.setText(totalSelectd+"");
		Log.i("APP", "ITEM INFRACAO REMOVIDO");
	}

	public String getCategoryName()
	{
		return categoryName;
	}

	public int getTotalSelectd()
	{
		return totalSelectd;
	}
	
	

}
