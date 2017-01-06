package policia.transito.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import policia.transito.model.DualValues;
import policia.transito.model.ItemList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

@SuppressLint("NewApi") public class GeralExpandableAdapter extends BaseExpandableListAdapter
{

	private ArrayList<DualValues<ItemList, ArrayList<ItemList>>> listData;
	
	private Context context;
	
	public GeralExpandableAdapter(Context context) 
	{
		this.context = context;
		this.listData = new ArrayList<DualValues<ItemList,ArrayList<ItemList>>>();
	}
	
	/**
	 * Adicionar uma nova mascara de novo grupo
	 * @param group
	 */
	public void addGroupFrame(ItemList group)
	{
		DualValues<ItemList, ArrayList<ItemList>> dual = new DualValues<ItemList, ArrayList<ItemList>>();
		dual.setRigthValue(new ArrayList<ItemList>());
		dual.setLeftValue(group);
		this.listData.add(dual);
	}
	
	/**
	 * Adicionar um nuvo filho do grupo
	 * @param indexGroup
	 * @param childrem
	 */
	public void addChildrenGroup(int indexGroup, ItemList childrem)
	{
		 this.listData.get(indexGroup).getRigthValue().add(childrem);
		
	}
	
	public void removeData(int indexGroup)
	{
		this.listData.get(indexGroup).getRigthValue().clear();
	}
	
	
	/**
	 * Adicionar um grupo pro completo
	 * @param listItem
	 */
	public void addGroup(DualValues<ItemList, ArrayList<ItemList>> listItem)
	{
		this.listData.add(listItem);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) 
	{
		return listData.get(groupPosition).getRigthValue().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition*groupPosition + this.listData.size()+1;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) 
	{
		final ItemList item =  this.listData.get(groupPosition).getRigthValue().get(childPosition);
	 	return prepareView(convertView, parent, item, childPosition);
	}

	@Override
	public int getChildrenCount(int groupPosition) 
	{
		return this.listData.get(groupPosition).getRigthValue().size();
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		
		return listData.get(groupPosition).getLeftValue();
	}

	@Override
	public int getGroupCount()
	{
		return listData.size();
	}

	@Override
	public long getGroupId(int groupPosition) 
	{
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) 
	{
		final ItemList item =  this.listData.get(groupPosition).getLeftValue();
	 	return prepareView(convertView, parent, item, groupPosition);
		
	}

	private View prepareView(View convertView, ViewGroup parent, ItemList item, int indexPosition) 
	{
		try
		{
			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View ver = item.prepareView(inflater, this.context, parent, convertView, indexPosition);
			return ver;
		 	 
		}catch(Exception ex)
		{
			Log.e("APP", "ERROR AO PREPARAR PARA A VIEW: "+ex.getMessage(), ex);
		}
	 	return null;
		
	}
	
	

	@Override
	public boolean hasStableIds() 
	{
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) 
	{
		return false;
	}

}
