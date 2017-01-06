package policia.transito.adapter;

import policia.transito.R;
import policia.transito.model.ItemList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemFiscalizacaoGroup implements ItemList
{
	private String name;
	private boolean opem;
	private int typeGroup;
	private ItemFiscalizacaoBody body;
	
	public ItemFiscalizacaoGroup(int typeGroup) 
	{
		this.typeGroup = typeGroup;
	}
	
	@Override
	public View prepareView(LayoutInflater inflater, Context appContext,
			ViewGroup viewGroup, View enterView, int indexPosition) 
	{
		View view = inflater.inflate(R.layout.items_dinamics, null);
		View view2 = inflater.inflate(R.layout.item_navegation, null);
		
		view.setVisibility(View.VISIBLE);
		View verGroup = view.findViewById(R.id.itemGroupTitle);
		verGroup.setVisibility(View.VISIBLE);
		
		//
		if(typeGroup == 1) this.name = view.getContext().getString(R.string.dataDriver);
		else if(typeGroup == 2) this.name = view.getContext().getString(R.string.dataCard);
		else this.name = view.getContext().getString(R.string.dataCar);
		
		TextView tv = (TextView) view.findViewById(R.id.tvGroupTitle);
		tv.setText(getName());
		
		if(isOpem())
			verGroup.findViewById(R.id.itemGroupTitelIconEstado).setBackgroundResource(R.drawable.backgraound_circular_selecionado);
		else if(isComplet())
			verGroup.findViewById(R.id.itemGroupTitelIconEstado).setBackgroundResource(R.drawable.backgraound_circular_verde);
		else verGroup.findViewById(R.id.itemGroupTitelIconEstado).setBackgroundResource(R.drawable.backgraound_circular_vermelho);
		return view;
	}
	
	

	public int getTypeGroup() {
		return typeGroup;
	}



	public void setTypeGroup(int typeGroup) {
		this.typeGroup = typeGroup;
	}



	public boolean isOpem() {
		return opem;
	}

	public void setOpem(boolean opem) 
	{
		this.opem = opem;
	}

	public boolean isComplet() 
	{
		return this.body.isComplet();
	}


	public String getName()
	{
		return name;
	}


	public void setBody(ItemFiscalizacaoBody body) 
	{
		this.body = body;
	}
	
	

}
