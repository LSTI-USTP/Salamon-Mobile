package policia.transito.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import policia.transito.R;
import policia.transito.model.DualValues;
import policia.transito.model.ItemList;

public class DefaultItemList implements ItemList
{
	private String name;
	private boolean seleted;
	private int id;
	private ItemTypes type;
	private View currentView;
	
	public DefaultItemList(int id, String name, ItemTypes type)
	{
		this.seleted = false;
		this.name = name;
		this.id = id;
		this.type = type;
	}
	
	public DefaultItemList()
	{
		
	}
	
	public static void generateItems(GeralListAdapter adapter, ItemTypes type, DualValues<Integer, String> ... items)
	{
		for(DualValues<Integer, String> item: items)
			adapter.addIten(new DefaultItemList(item.getLeftValue(), item.getRigthValue(), type));
		
	}
	
	
	
	@Override
	public View prepareView(LayoutInflater inflater, Context appContext, ViewGroup viewGroup, View enterView, int indexPosition)
	{
		currentView = null;
		TextView tv;
		currentView =  inflater.inflate(R.layout.items_dinamics, null);
		switch(type)
		{
			case SPINNER_ITEM: case SAMPLE_ITEM:
				currentView.findViewById(R.id.lilayItemSpiner).setVisibility(View.VISIBLE);
				tv = (TextView) currentView.findViewById(R.id.tvItemSpinerValue);
				tv.setText(name);
				break;
				
			case SPINNER_ITEM_FAVORITS:
				if(indexPosition > 0)
				{
					currentView.findViewById(R.id.lilayItemSpinerValueFAV).setVisibility(View.VISIBLE);
					tv = (TextView) currentView.findViewById(R.id.tvItemSpinerValueFAV);
					tv.setText((name == null || name.equals("VOID") || name.length() == 0)? " ": name);				
				}
				else 
				{
					currentView.findViewById(R.id.lilayItemSpinerValueFavStar).setVisibility(View.VISIBLE);
				    RatingBar star = (RatingBar) currentView.findViewById(R.id.tvItemSpinerValueFavStar);
				    star.setEnabled(false);
				}
				break;
				
			case GRUPO_INFRACAO:
				currentView.findViewById(R.id.itemGroupInfacao).setVisibility(View.VISIBLE);
				TextView nomeInfracao = (TextView) currentView.findViewById(R.id.tvItemGroupInfracaoNome);
				nomeInfracao.setText(name);
				
				break;	
		}
		return currentView;
	}
	
	public static enum ItemTypes
	{
		SAMPLE_ITEM,
		SPINNER_ITEM,
		SPINNER_ITEM_FAVORITS,
		GRUPO_INFRACAO
		
	}

}
