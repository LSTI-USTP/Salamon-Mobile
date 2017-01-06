package policia.transito.adapter;

import java.util.ArrayList;

import policia.transito.R;
import policia.transito.adapter.DefaultItemList.ItemTypes;
import policia.transito.data.BaseInfracao;
import policia.transito.data.DefaultDatas;
import policia.transito.model.Condutor;
import policia.transito.model.ItemList;
import policia.transito.model.Veiculo;
import socket.listener.ClienteService;
import socket.listener.OnReciveTranfer;
import socket.modelo.Transfer;
import socket.modelo.Transfer.Intent;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemAtualizacao implements ItemList, OnClickListener, DefaultDatas
{
	private int tipo;
	private String titleName;
	private View refreshIcon;
	private ClienteService service;
	private GeralExpandableAdapter geralExpandableAdapter;
	private BaseInfracao baseInfracao;
	
	public ItemAtualizacao(int tipo)
	{
		this.tipo = tipo;
	}
		
	@Override
	public View prepareView(LayoutInflater inflater, Context appContext,ViewGroup viewGroup, View enterView, int indexPosition) 
	{
		View view = inflater.inflate(R.layout.items_dinamics, null);
		view.setVisibility(View.VISIBLE);
		View verGroup = view.findViewById(R.id.itemGroupTitle);
		refreshIcon = view.findViewById(R.id.itemGroupTitelIconEstado);
		refreshIcon.setBackgroundResource(R.drawable.backgraound_refresh);
		verGroup.setVisibility(View.VISIBLE);
		this.service = ClienteService.findCurrentConnection();
		
		if(tipo == 1) this.titleName = view.getContext().getString(R.string.condutor);
		else if(tipo == 2) this.titleName = view.getContext().getString(R.string.carCategory);
		else if(tipo== 3) this.titleName = view.getContext().getString(R.string.infra);
		
		TextView texto = (TextView) view.findViewById(R.id.tvGroupTitle);
		texto.setText(getTileName());	
		refreshIcon.setOnClickListener(this);
		
		baseInfracao = new BaseInfracao(appContext);
		return view;
	}
	
	public String getTileName()
	{
		return this.titleName;
	}

	@Override
	public void onClick(View clique)
	{
		Transfer transfer = new Transfer("Agente", SERVER_APP_NAME, Intent.GET);
		transfer.setMessage("Carregar informações");
	
		if(clique.equals(this.refreshIcon))
		{
			if(this.titleName.equals("Estado de condutor"))
			{
				transfer.setType(5001);
				transfer.setEspera(Espera.ESTADO_CONDUTOR.name());
				this.service.transfer(transfer);
			}
			else if(this.titleName.equals("Categoria de veiculo"))
			{
				transfer.setType(5002);
				transfer.setEspera(Espera.CATEGORIA_VEICULO.name());
				service.transfer(transfer);
			}
			else
			{
				transfer.setType(5003);
				transfer.setEspera(Espera.INFRACOES.name());
				service.transfer(transfer);
			}
		}
		
	}


	
	private enum Espera
	{
		INFRACOES,
		ESTADO_CONDUTOR,
		CATEGORIA_VEICULO
	}
}
