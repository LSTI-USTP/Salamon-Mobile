package policia.transito.adapter;

import java.util.ArrayList;

import policia.transito.model.ItemList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GeralListAdapter extends BaseAdapter
{
	Context context;
	ArrayList<ItemList> listData;
	
	public GeralListAdapter(Context context) 
	{
		this.context = context;
		this.listData = new ArrayList<ItemList>();
	}
	
	
	@Override
	public int getCount() 
	{
		return listData.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		try
		{
			ItemList item = this.listData.get(position);
			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View ver = item.prepareView(inflater, this.context, parent, convertView, position);
			return ver;
		 	 
		}catch(Exception ex)
		{
			Log.e("APP", "ERROR AO PREPARAR PARA A VIEW: "+ex.getMessage(), ex);
		}
	 	return null;
	}


	public void addIten(ItemList itemSpiner) 
	{
		this.listData.add(itemSpiner);
	}

}
