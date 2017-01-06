package policia.transito.adapter;

import java.util.ArrayList;

import policia.transito.R;
import policia.transito.adapter.DefaultItemList.ItemTypes;
import policia.transito.model.Combo;
import policia.transito.model.ItemList;
import policia.transito.model.OnIntentTreater;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class ItemFiscalizacao implements ItemList
{

	private int typeGroup;
	private ItemFiscalizacao body;
	private OnIntentTreater onClickListener;
	private ArrayList<Combo> listaCombo = new ArrayList<Combo>();
	
	public ItemFiscalizacao(int typeGroup) 
	{
		this.typeGroup = typeGroup;
	}
	
	
	public ItemFiscalizacao(int typeGroup, OnIntentTreater onIntentTreater) 
	{
		this.typeGroup = typeGroup;
		this.onClickListener = onIntentTreater;
	}

	public void setLista(ArrayList<Combo> list)
	{
		this.listaCombo = list;
	}
	public int getTypeData()
	{
		return this.typeGroup;
	}

	
	@Override
	public View prepareView(LayoutInflater inflater, Context appContext, ViewGroup viewGroup, View enterView, int indexPosition)
	{
		View view = null;
		TextView tv  = null;
		String text = "";
		View bodyView = null;
		if(body != null)
		{
			view = inflater.inflate(R.layout.items_dinamics, null);
			view.findViewById(R.id.itemGroupTitle).setVisibility(View.VISIBLE);
			tv = (TextView) view.findViewById(R.id.tvGroupTitle);
		}
		else 
		{
			bodyView  = inflater.inflate(R.layout.items_statics, null);
			Spinner spinner = (Spinner) bodyView.findViewById(R.id.spinnerFiscaCatVeiculo);
		
			
		}
		
		switch(this.typeGroup)
		{
			case 1: // carrega o estado do condutor
				text = inflater.getContext().getString(R.string.dataDriver);
				if(bodyView != null)
				{
					bodyView.findViewById(R.id.lilayFiscaDriver).setVisibility(View.VISIBLE);
					Spinner spn = (Spinner) bodyView.findViewById(R.id.spnerBodyFiscaDriver);
					GeralListAdapter adapter = new GeralListAdapter(appContext);
					for(Combo value: listaCombo)
					      adapter.addIten(new DefaultItemList(Integer.valueOf(value.getId()), value.getValue(),ItemTypes.SPINNER_ITEM));
					
					spn.setAdapter(adapter);
					
				}   
				break;
				
			case 2:
				if(bodyView != null)
				{
					bodyView.findViewById(R.id.lilayFiscaCard).setVisibility(View.VISIBLE);
					if(this.onClickListener != null)
					{
						int it = R.id.tvFiscaCarta;
						/*Comentado ate que o teclado estaja proto
						bodyView.findViewById(R.id.tvFiscaCarta).setOnClickListener(new  OnClickListener() {							
						@Override
						public void onClick(View v) 
						{
							onClickListener.treat(v, EventsCod.CLICK_CARTA, null);
						}
						});*/
					}
				}
				text = inflater.getContext().getString(R.string.dataCard); 
				break;
			case 3:
				text = inflater.getContext().getString(R.string.dataCar);
				if(bodyView !=  null)
				{
					bodyView.findViewById(R.id.relayFiscaVeiculo).setVisibility(View.VISIBLE);
					Spinner spn = (Spinner) bodyView.findViewById(R.id.spinnerFiscaCatVeiculo);
					GeralListAdapter adapter = new GeralListAdapter(appContext);
					for(Combo value: listaCombo)
					{
					      adapter.addIten(new DefaultItemList(Integer.valueOf(value.getId()), value.getValue(),ItemTypes.SPINNER_ITEM));
					     
					}
					spn.setAdapter(adapter);					
				}
				break;
		}
		if(tv != null) tv.setText(text);
		
		return (view != null)? view: bodyView;
	}
	public void setBody(ItemFiscalizacao body) 
	{
		this.body = body;
	}
}
