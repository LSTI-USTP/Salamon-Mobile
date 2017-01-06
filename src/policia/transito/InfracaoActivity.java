package policia.transito;

import java.util.ArrayList;

import policia.transito.adapter.GeralExpandableAdapter;
import policia.transito.adapter.ItemCategoriaInfracao;
import policia.transito.adapter.ItemInfracao;
import policia.transito.data.BaseInfracao;
import policia.transito.data.DBObject;
import policia.transito.data.DefaultDatas;
import policia.transito.model.ActivityName;
import policia.transito.model.Infracao;
import policia.transito.model.Infracao.ModoCoima;
import socket.listener.ClienteService;
import socket.listener.OnReciveTranfer;
import socket.modelo.Transfer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class InfracaoActivity extends Activity implements DefaultDatas, OnClickListener, DBObject, OnReciveTranfer
{
	private ExpandableListView expandableListView;
    private View avancar;
    private  View retroceder;
	private GeralExpandableAdapter adappterExpend;
	private ArrayList<Infracao> itemsSelecionados;
	private ArrayList<Infracao> infracoes;
	private ClienteService service;
	private BaseInfracao baseInfracao;
	public static int COD_CALL_NEXT_INFRACAO = 25;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_infracao);
		this.service = ClienteService.findCurrentConnection();
		this.service.setOnTransitActivity(this, this);
		init();	
	}
	

	private void init() 
	{
		baseInfracao = new BaseInfracao(this);
		expandableListView = (ExpandableListView) findViewById(R.id.expandInfracaoInfracoes);
		avancar = findViewById(R.id.areaNavegationGoFront);
		this.adappterExpend = new GeralExpandableAdapter(getApplicationContext());
		
		BaseInfracao baseInfracao = new BaseInfracao(this);
		ArrayList<String> list = baseInfracao.getCategoriasInfracao();
		this.infracoes = baseInfracao.infracoesSelecionadas();
		int count =0;
		
		if(this.infracoes.size()>0)
		{
			for(String categoria: list)
			{
				ItemCategoriaInfracao mascaraGrupo = new ItemCategoriaInfracao(categoria);
				ArrayList<Infracao> listInfarcao = baseInfracao.getInfracaoCategoria(categoria);
				adappterExpend.addGroupFrame(mascaraGrupo);
				for(Infracao item: listInfarcao)
				{
					ItemInfracao itemInfracao;
					adappterExpend.addChildrenGroup(count, itemInfracao=  new ItemInfracao(item));
					itemInfracao.setGroup(mascaraGrupo);
				}
				count++;
			}
		}
		else
		{
			for(String categoria: list)
			{
				ItemCategoriaInfracao mascaraGrupo = new ItemCategoriaInfracao(categoria);
				ArrayList<Infracao> listInfarcao = baseInfracao.getInfracaoCategoria(categoria);
				adappterExpend.addGroupFrame(mascaraGrupo);
				for(Infracao item: listInfarcao)
				{
					ItemInfracao itemInfracao;
					adappterExpend.addChildrenGroup(count, itemInfracao=  new ItemInfracao(item));
					itemInfracao.setGroup(mascaraGrupo);
				}	
				count++;
			}
		}
		
		expandableListView.setAdapter(adappterExpend);
		TextView titulo = (TextView) findViewById(R.id.areaNavegationTitle);
		titulo.setText(R.string.infracao);
		retroceder = findViewById(R.id.areaNavegationGoBack);
		avancar.setOnClickListener(this);
		retroceder.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) 
	{
		if(v.equals(this.avancar))
		{
			itemsSelecionados =  new ArrayList<Infracao>();
			Intent nextFrame = new Intent(this, Finalizar.class);
			Bundle extras = new Bundle();
			for(int iindexGroup = 0; iindexGroup<this.adappterExpend.getGroupCount(); iindexGroup++)
			{
				for(int indexChildren  =0; indexChildren<this.adappterExpend.getChildrenCount(iindexGroup); indexChildren++)
				{
					ItemInfracao item = (ItemInfracao) this.adappterExpend.getChild(iindexGroup, indexChildren);
					if(item.getInfracao().isSelected())
					{
						if(item.getInfracao().getModoCoima() == ModoCoima.GRAVITARIO && item.getInfracao().getNivelGravidade() != null)
							itemsSelecionados.add(new Infracao(item.getInfracao().getId(), item.getInfracao().getNivelGravidade().getId()+""));
						else
							itemsSelecionados.add(new Infracao(item.getInfracao().getId(), "0"));
					}
				}
			}
			
			baseInfracao.guardarInfracoesSelecionadas(itemsSelecionados);// guarda na base de dados sqllite
			extras.putString(ACTIVITY_NAME, ActivityName.INFRACAO.name());
			nextFrame.putExtras(extras);
			startActivityForResult(nextFrame, COD_CALL_NEXT_INFRACAO);
		}
		else if(v.equals(this.retroceder))
		{
			voltar();	
			
		}
	}
	
	@Override
	public void onBackPressed()
	{
		voltar();
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == COD_CALL_NEXT_INFRACAO)
		{
			Bundle values = data.getExtras();
			if(values.getString(NEXT_COD).equals(NEXT_SUCCESS))
			{
				Bundle value = new Bundle();
				value.putString(NEXT_COD, NEXT_SUCCESS);
				getIntent().putExtras(value);
				this.setResult(Principal.COD_CALL_NEXT, getIntent());
				finish();
			}	
		}	
	}
	private void voltar()
	{
		baseInfracao.guardarInfracoesSelecionadas(itemsSelecionados);	
		Bundle values = new Bundle();
		
		values.putString(NEXT_COD, NEXT_BACK);
		getIntent().putExtras(values);
		this.setResult(Principal.COD_CALL_NEXT, getIntent());
		finish();
	}
	
	@Override
	public void onProcessTranfer(Transfer transfer) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onPosConnected() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onConnectLost() 
	{
				
	}
	
}
