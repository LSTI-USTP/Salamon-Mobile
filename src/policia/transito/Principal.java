package policia.transito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import policia.transito.Teclado.TeclaResust;
import policia.transito.Teclado.TeclaTypes;
import policia.transito.adapter.GeralExpandableAdapter;
import policia.transito.adapter.ItemFiscalizacaoBody;
import policia.transito.adapter.ItemFiscalizacaoGroup;
import policia.transito.data.BaseFiscalizacao;
import policia.transito.data.BaseInfracao;
import policia.transito.data.DefaultDatas;
import policia.transito.model.ActivityName;
import policia.transito.model.Agente;
import policia.transito.model.Combo;
import policia.transito.model.Condutor;
import policia.transito.model.DualValues;
import policia.transito.model.Fiscalizacao;
import policia.transito.model.ItemList;
import policia.transito.model.Localizacao;
import policia.transito.model.OnIntentTreater;
import policia.transito.model.Veiculo;
import socket.listener.ClienteService;
import socket.listener.OnReciveTranfer;
import socket.modelo.Transfer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author Helcio Guadalupe
 *
 */



@SuppressLint("NewApi")
public class Principal extends Activity implements OnClickListener, OnIntentTreater, DefaultDatas, OnGroupExpandListener,OnGroupCollapseListener, OnReciveTranfer
{
	private static final long MIN_TIME_BW_UPDATES = 0;

	private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

	public int COD_CALL_TECLADO = 10;
	public static int COD_CALL_NEXT = 25;
	
	ListView list_view, list_infra;
	ArrayAdapter<String> adapter, adaptered;
	ArrayList<String> list=new ArrayList<String>();
	String list_litem;
	Object mActionMode;
	private BaseFiscalizacao baseCurrentOperacao;
	Button concluir;
	Button infracao;
	private Localizacao localizacao;
	TextView tvCarta;
	private Fiscalizacao fiscalizacao;
	private ExpandableListView expandList;
	private GeralExpandableAdapter adapterExpand;
	private ClienteService service;
	private String infoCarta;
	private String infoMatricula;
	private EditText nomeCondutor;

	private View voltarTelaInicial;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fiscalizacao_correcao);
        localizacao = new Localizacao(this);
        init();
        
    }
    
    private void init()
    {
    	this.service = ClienteService.findCurrentConnection();
 		this.service.setOnTransitActivity(this, this);
 		this.baseCurrentOperacao = new BaseFiscalizacao(this);
 		
         this.expandList =  (ExpandableListView) findViewById(R.id.expandableListView1);
         this.adapterExpand = new GeralExpandableAdapter(this);
         expandList.setAdapter(adapterExpand);
    
         concluir= (Button) findViewById(R.id.btFiscaConcluir);
         concluir.setOnClickListener(this);
         this.voltarTelaInicial = (View) findViewById(R.id.areaNavegationGoBack);
         this.voltarTelaInicial.setOnClickListener(this);
         infracao = (Button) findViewById(R.id.btFiscaInfracao);;
         infracao.setOnClickListener(this);
         baseCurrentOperacao = new BaseFiscalizacao(getApplicationContext());
         
         expandList.setOnGroupExpandListener(this);
         expandList.setOnGroupCollapseListener(this);
         
    	findViewById(R.id.areaNavegationGoFront).setVisibility(View.GONE); // OCULTA A SETA DE AVANÇAR
    	baseCurrentOperacao = new BaseFiscalizacao(this);
    	if(baseCurrentOperacao.getAgente().getIdTipoOperacao()==1)
    		adapterExpand.addGroup(createGroup(3));
    	else
    	{
    	    for(int i =1; i<=3; i++)
    	        adapterExpand.addGroup(createGroup(i));
    	}
    	
    
    }
	/**
     * Carrega as informações para os spinners
     * @param typeGroup
     * @return
     */
    private DualValues<ItemList, ArrayList<ItemList>> createGroup(int typeGroup)
	{	
    	ArrayList<Combo> arrayList = new ArrayList<Combo>();
    	
		DualValues<ItemList, ArrayList<ItemList>> group = new DualValues<ItemList, ArrayList<ItemList>>();
		ItemFiscalizacaoBody body =  new ItemFiscalizacaoBody(typeGroup, (OnIntentTreater) this);
		BaseInfracao baseInfracao = new BaseInfracao(this);
		ArrayList<Condutor> estadosCondutores = baseInfracao.getEstadosCondutores();
		ArrayList<Veiculo> categoriasVeiculo = new ArrayList<Veiculo>();
		categoriasVeiculo = baseInfracao.getCategoriasVeiculo();
		
		switch(typeGroup)
		{
			case 1:
				arrayList.clear();
				arrayList.add(new Combo("-1", "(Selecione)"));
				for(Condutor condutor : estadosCondutores)
					arrayList.add(new Combo(condutor.getIdEstadoCondutor(), condutor.getDescricaoEstado()));
							
				body.setLista(arrayList);	
			  	break;
			case 3:
				arrayList.clear();
				arrayList.add(new Combo("-1", "(Selecione)"));
				for(Veiculo veiculo: categoriasVeiculo)
					arrayList.add(new Combo(veiculo.getIdCategoria(), veiculo.getCategoriaDescricao()));
				
				body.setLista(arrayList);
			   break;
		}
	   ItemFiscalizacaoGroup expandItem;
	   group.setLeftValue(expandItem = new ItemFiscalizacaoGroup(typeGroup));
	   expandItem.setBody(body);
	   group.setRigthValue(new ArrayList<ItemList>(Arrays.asList(body)));
	   return group;
	}
    
    
	@Override
	public void onClick(View v) 
	{
		Bundle extras = new Bundle();	
		if(v.equals(concluir))
		{
			if(dadosFiscalizacao()== true)
			{	
				extras.putString(ACTIVITY_NAME, ActivityName.PRINCIPAL.name());
				BaseFiscalizacao baseFiscalizacao = new BaseFiscalizacao(this);
				baseFiscalizacao.regFiscalizacao(fiscalizacao);
				Intent openFinaly = new Intent(getApplicationContext(), Finalizar.class);
				openFinaly.putExtras(extras);
				startActivityForResult(openFinaly, COD_CALL_NEXT);
			}
		}
		else if(v.equals(infracao))
		{
			if(dadosFiscalizacao()== true)
			{	
				extras.putString(ACTIVITY_NAME, ActivityName.PRINCIPAL.name());
				BaseFiscalizacao baseFiscalizacao = new BaseFiscalizacao(this);
				baseFiscalizacao.regFiscalizacao(fiscalizacao);
				Intent openFinaly = new Intent(getApplicationContext(), InfracaoActivity.class);
				openFinaly.putExtras(extras);
				startActivityForResult(openFinaly, COD_CALL_NEXT);
			}
		}
		else if(v.equals(this.voltarTelaInicial))
		{
			Bundle values = new Bundle();
			values.putString(NEXT_COD, NEXT_BACK);
			getIntent().putExtras(values);
			this.setResult(ActivityInitial.COD_CALL_NEXT, getIntent());
			finish();
		}
	}


	@Override
	public void treat(View view, EventsCod cod, HashMap<String, Object> values) 
	{
		// trata do clique no tecladp

		Bundle extras = new Bundle();
		if(baseCurrentOperacao.getAgente().getIdTipoOperacao()==1)
		{
			ItemFiscalizacaoBody dadosVeiculo = (ItemFiscalizacaoBody) this.adapterExpand.getChild(0, 0);
			if(cod == EventsCod.CLICK_MATRICULA)
			{
				Intent openTeclado = new Intent(getApplicationContext(), Teclado.class);
				String oldValue = dadosVeiculo.getNumMatricula();
				openTeclado.putExtra(TECLADO_TYPE, TeclaTypes.MATRICULA.name());
				openTeclado.putExtra(TECLADO_CURRENT_VALUE, oldValue);
				openTeclado.putExtra(TECLADO_COD_INTENT, COD_CALL_TECLADO);
				startActivityForResult(openTeclado, COD_CALL_TECLADO);
			}
			else if(cod == EventsCod.CLICK_INFO_MATRICULA)
			{
				Intent intent = new Intent(this, ActivityEstado.class);
				extras.putString(RESULTADO, this.infoMatricula);
				intent.putExtras(extras);
				startActivity(intent);
			}
			
		}
		else
		{
			ItemFiscalizacaoBody dadosCondutor = (ItemFiscalizacaoBody) this.adapterExpand.getChild(0, 0);
			ItemFiscalizacaoBody dadosCarta = (ItemFiscalizacaoBody) this.adapterExpand.getChild(1, 0);
			ItemFiscalizacaoBody dadosVeiculo = (ItemFiscalizacaoBody) this.adapterExpand.getChild(2, 0);
		
			if(cod == EventsCod.CLICK_CARTA)
			{
				if(dadosCarta.getExistenciaCarta().isChecked())
				{
					Intent openTeclado = new Intent(getApplicationContext(), Teclado.class);
					String oldValue = dadosCarta.getNumCarta();
					
					openTeclado.putExtra(TECLADO_TYPE, TeclaTypes.CARTA.name());
					openTeclado.putExtra(TECLADO_CURRENT_VALUE, oldValue);
					openTeclado.putExtra(TECLADO_COD_INTENT, COD_CALL_TECLADO);
					startActivityForResult(openTeclado, COD_CALL_TECLADO);
				}
			}
			else if(cod == EventsCod.CLICK_MATRICULA)
			{
				Intent openTeclado = new Intent(getApplicationContext(), Teclado.class);
				String oldValue = dadosVeiculo.getNumMatricula();
				openTeclado.putExtra(TECLADO_TYPE, TeclaTypes.MATRICULA.name());
				openTeclado.putExtra(TECLADO_CURRENT_VALUE, oldValue);
				openTeclado.putExtra(TECLADO_COD_INTENT, COD_CALL_TECLADO);
				startActivityForResult(openTeclado, COD_CALL_TECLADO);
			}
			else if(cod == EventsCod.CLICK_INFO_MATRICULA)
			{
				Intent intent = new Intent(this, ActivityEstado.class);
				extras.putString(RESULTADO, this.infoMatricula);
				intent.putExtras(extras);
				startActivity(intent);
			}
			else if(cod == EventsCod.CLICK_INFO_CARTA)
			{
				Intent intent = new Intent(this, ActivityEstado.class);
				extras.putString(RESULTADO, this.infoCarta);
				intent.putExtras(extras);
				startActivity(intent);
			}
			else if(cod == EventsCod.CLICK_NOME_CONDUTOR)
			{
				Intent openTeclado = new Intent(getApplicationContext(), TecladoName.class);
				String oldValue = dadosCondutor.getNomeCondutor();
				openTeclado.putExtra(TECLADO_TYPE, TeclaTypes.NOME_CONDUTOR.name());
				openTeclado.putExtra(TECLADO_CURRENT_VALUE, oldValue);
				openTeclado.putExtra(TECLADO_COD_INTENT, COD_CALL_TECLADO);
				startActivityForResult(openTeclado, COD_CALL_TECLADO);
			}
		}
	}

	
	/**
	 * Quando a expandViewFecha
	 */
	@Override
	public void onGroupCollapse(int groupPosition)
	{
		ItemFiscalizacaoGroup item = (ItemFiscalizacaoGroup) adapterExpand.getGroup(groupPosition);
		item.setOpem(false);
	}

	/**
	 * Quando a expandViewAbri
	 */
	@Override
	public void onGroupExpand(int groupPosition) 
	{
		ItemFiscalizacaoGroup item = (ItemFiscalizacaoGroup) adapterExpand.getGroup(groupPosition);
		item.setOpem(true);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
	
		if(resultCode == COD_CALL_NEXT)
		{
			Bundle values = data.getExtras();
			if(values.getString(NEXT_COD).equals(NEXT_SUCCESS))
			{
				limparDados();
				//TODO AQUI DEVE SER IMPLEMENTADO A LIMPEZA DOS CAMPOS
			}
			else if(values.getString(NEXT_COD).equals(NEXT_BACK))
			{
				//TODO AQUI É QUANDO FOR VOLTADO ATRÁS
			}
			return;
		}
		
		if(baseCurrentOperacao.getAgente().getIdTipoOperacao()==1)
		{
			ItemFiscalizacaoBody dadosVeiculo = (ItemFiscalizacaoBody) this.adapterExpand.getChild(0, 0);
			if(requestCode == COD_CALL_TECLADO)
			{
				Bundle values = data.getExtras();
				TeclaResust resultTecla = TeclaResust.valueOf(values.getString(TECLADO_RESULT));
				String oldValue = values.getString(TECLADO_OLD_VALUE);
				
				if(resultTecla == TeclaResust.SUCCESS)
				{
					String newValue = values.getString(TECLADO_NEW_VALUE);
					TeclaTypes tecla = TeclaTypes.valueOf(values.getString(TECLADO_TYPE));
					if(tecla == TeclaTypes.MATRICULA)
					{
						dadosVeiculo.setNumMatricula(newValue);
						getEstadoMatricula(dadosVeiculo.getNumMatricula());
					}
				}
			}
		}
		else
		{
			ItemFiscalizacaoBody dadosCarta = (ItemFiscalizacaoBody) this.adapterExpand.getChild(1, 0);
			ItemFiscalizacaoBody dadosVeiculo = (ItemFiscalizacaoBody) this.adapterExpand.getChild(2, 0);
			ItemFiscalizacaoBody dadosCondutor = (ItemFiscalizacaoBody) this.adapterExpand.getChild(0, 0);
			
			//Quando receber o resultado do teclado
			if(requestCode == COD_CALL_TECLADO)
			{
				Bundle values = data.getExtras();
				TeclaResust resultTecla = TeclaResust.valueOf(values.getString(TECLADO_RESULT));
				String oldValue = values.getString(TECLADO_OLD_VALUE);
				
				if(resultTecla == TeclaResust.SUCCESS)
				{
					String newValue = values.getString(TECLADO_NEW_VALUE);
					TeclaTypes tecla = TeclaTypes.valueOf(values.getString(TECLADO_TYPE));
					if(tecla==TeclaTypes.CARTA)
					{
						dadosCarta.setNumCarta(newValue);
						this.getEstadoCarta(dadosCarta.getNumCarta()); // envia dados para o servidor,obtendo informações sobre a carta
					}
					else if(tecla == TeclaTypes.MATRICULA)
					{
						dadosVeiculo.setNumMatricula(newValue);
						getEstadoMatricula(dadosVeiculo.getNumMatricula());
					}
					else if(tecla == TeclaTypes.NOME_CONDUTOR)
					{
						dadosCondutor.setNomeCondutor(newValue);
					}
				}		
			}
		}
	}
	
	private boolean validarDadosVeiculo()
	{
		fiscalizacao = new Fiscalizacao();
		ItemFiscalizacaoBody dadosVeiculo = (ItemFiscalizacaoBody) this.adapterExpand.getChild(0, 0);
		if(dadosVeiculo.getDescricaoItem()==null || dadosVeiculo.getDescricaoItem().contains("Selecione"))
		{
			Toast.makeText(this, "Selecione a categoria do veiculo", Toast.LENGTH_LONG).show();
			return false;
		}
		else
		{
			fiscalizacao.setCategoriaVeiculo(dadosVeiculo.getidItem());
			fiscalizacao.setDescricaoCategoriaVeiculo(dadosVeiculo.getDescricaoItem());
			fiscalizacao.setLivrete((dadosVeiculo.isLivrete())? 1 : 0);
		}
	
		if(dadosVeiculo.getNumMatricula()==null || dadosVeiculo.getNumMatricula().equals(""))
		{
			Toast.makeText(this, "Informe o número de matricula", Toast.LENGTH_LONG).show();
			return false;
		}
		else
		{
			fiscalizacao.setNumMatricula(dadosVeiculo.getNumMatricula());
		}	
		return true;
	}
	
	private boolean validarDadosFiscalizacao()
	{
		fiscalizacao = new Fiscalizacao();
		ItemFiscalizacaoBody dadosCondutor = (ItemFiscalizacaoBody) this.adapterExpand.getChild(0, 0);
		ItemFiscalizacaoBody dadosCarta = (ItemFiscalizacaoBody) this.adapterExpand.getChild(1, 0);
		ItemFiscalizacaoBody dadosVeiculo = (ItemFiscalizacaoBody) this.adapterExpand.getChild(2, 0);
		
		if(dadosCondutor.getDescricaoItem()==null || dadosCondutor.getDescricaoItem().contains("Selecione"))
		{
			fiscalizacao.setEstadoCondutor(0);
			fiscalizacao.setDescricaoEstadoCondutor("");
		}
		else
		{
			fiscalizacao.setEstadoCondutor(dadosCondutor.getidItem());
			fiscalizacao.setDescricaoEstadoCondutor(dadosCondutor.getDescricaoItem());
			fiscalizacao.setNomeCondutor(dadosCondutor.getNomeCondutor());
		}
		if(dadosCarta.isExistencia())
		{
			fiscalizacao.setExistenciaCarta(1);
			if(dadosCarta.getCheckIncompatibilidade().isChecked())
				fiscalizacao.setIncompatibilidadeCarta(1);
			else fiscalizacao.setIncompatibilidadeCarta(0);
			
			if(dadosCarta.getNumCarta()== null || dadosCarta.getNumCarta().equals(""))
			{
				Toast.makeText(this, "Informe o número de carta", Toast.LENGTH_LONG).show();
				return false;
			}
			else
				fiscalizacao.setNumCarta(dadosCarta.getNumCarta());
		}
		else
		{
			fiscalizacao.setExistenciaCarta(0);
			fiscalizacao.setIncompatibilidadeCarta(0);
			fiscalizacao.setNumCarta("");
		}
		if(dadosVeiculo.getDescricaoItem()==null || dadosVeiculo.getDescricaoItem().contains("Selecione"))
		{
			Toast.makeText(this, "Selecione a categoria do veiculo", Toast.LENGTH_LONG).show();
			return false;
		}
		else
		{
			fiscalizacao.setCategoriaVeiculo(dadosVeiculo.getidItem());
			fiscalizacao.setDescricaoCategoriaVeiculo(dadosVeiculo.getDescricaoItem());
			fiscalizacao.setLivrete((dadosVeiculo.isLivrete())? 1 : 0);
		}

		if(dadosVeiculo.getNumMatricula()==null || dadosVeiculo.getNumMatricula().equals(""))
		{
			Toast.makeText(this, "Informe o número de matricula", Toast.LENGTH_LONG).show();
			return false;
		}
		else
		{
			fiscalizacao.setNumMatricula(dadosVeiculo.getNumMatricula());
		}	
		return true;
	}

	private void limparDados()
	{
		if(baseCurrentOperacao.getAgente().getIdTipoOperacao()==1)
		{
			ItemFiscalizacaoBody dadosVeiculo = (ItemFiscalizacaoBody) this.adapterExpand.getChild(0, 0);
			dadosVeiculo.setIdItem(-1);
			dadosVeiculo.setNumMatricula("");
			dadosVeiculo.setLivrete(false);
		}
		else
		{
			ItemFiscalizacaoBody dadosCondutor = (ItemFiscalizacaoBody) this.adapterExpand.getChild(0, 0);
			ItemFiscalizacaoBody dadosCarta = (ItemFiscalizacaoBody) this.adapterExpand.getChild(1, 0);
			ItemFiscalizacaoBody dadosVeiculo = (ItemFiscalizacaoBody) this.adapterExpand.getChild(2, 0);
			
			dadosCondutor.setIdItem(-1);
			dadosCarta.setExistencia(false);
			dadosCarta.setNumCarta("");
		    dadosCarta.setIncompatilidade(false);
		    dadosVeiculo.setIdItem(-1);
			dadosVeiculo.setNumMatricula("");
			dadosVeiculo.setLivrete(false);
		}
	}
	
	private boolean dadosFiscalizacao()
	{
		boolean valido = true;
		if(baseCurrentOperacao.getAgente().getIdTipoOperacao()==1)
			valido =validarDadosVeiculo();
		else valido= validarDadosFiscalizacao();	
		
		return valido;
	}

	private void getEstadoCarta(String numCarta)
	{
		Transfer transfer = new Transfer(Agente.getSender(this, this), SERVER_APP_NAME, 5005,socket.modelo.Transfer.Intent.GET, "Obter informações sobre a carta");
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("ID USER", this.baseCurrentOperacao.getAgente().getId()+"");
		map.put("CARTA", numCarta);
		transfer.getListMaps().add(map);
		transfer.setEspera(Espera.INFO_CARTA.name());
		service.transfer(transfer);
	}
	
	private void getEstadoMatricula(String numMatricula)
	{
		Transfer transfer = new Transfer(Agente.getSender(this, this), SERVER_APP_NAME, 5006,socket.modelo.Transfer.Intent.GET, "Obter informações sobre a matricula");
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("ID USER", this.baseCurrentOperacao.getAgente().getId()+"");
		map.put("MATRICULA", numMatricula);
		transfer.setEspera(Espera.INFO_MATRICULA.name());
		transfer.getListMaps().add(map);
	    service.transfer(transfer);
	}
	
	@Override
	public void onProcessTranfer(Transfer transfer) 
	{
		Espera espera = Espera.valueOf(transfer.getEspera());
	
		if(espera == Espera.INFO_CARTA)
		{
			if(transfer != null && transfer.getMessage().equals("true"))
			{	
				this.infoCarta = "carta"+";"+
			((transfer.getListMaps().get(0).get("PROUCURA").equals(""))? "0":transfer.getListMaps().get(0).get("PROUCURA")+"")
			+";"+((transfer.getListMaps().get(0).get("MULTAS").equals(""))? "0":transfer.getListMaps().get(0).get("MULTAS")+"")
				+";"+((transfer.getListMaps().get(0).get("GRAVIDADE").equals(""))? "0": transfer.getListMaps().get(0).get("GRAVIDADE")+"")
				+";"+((transfer.getListMaps().get(0).get("APRENSAO").equals(""))? "0" :transfer.getListMaps().get(0).get("APRENSAO")+"");	
			
				ItemFiscalizacaoBody.gravidade(transfer.getListMaps().get(0).get("GRAVIDADE").toString(),"carta");
			}
		}
		else if(espera == Espera.INFO_MATRICULA)
		{
			if(transfer != null && transfer.getMessage().equals("true"))
			{
				this.infoMatricula = "Matricula"+";"+
				((transfer.getListMaps().get(0).get("PROUCURA").equals(""))? "0": transfer.getListMaps().get(0).get("PROUCURA")+"")+";"+
				((transfer.getListMaps().get(0).get("MULTAS").equals(""))? "0":transfer.getListMaps().get(0).get("MULTAS")+"")+";"+
				((transfer.getListMaps().get(0).get("GRAVIDADE").equals(""))? "0" : transfer.getListMaps().get(0).get("GRAVIDADE")+"")+";"+
				((transfer.getListMaps().get(0).get("APRENSAO VEICULO").equals(""))? "0": transfer.getListMaps().get(0).get("APRENSAO VEICULO")+"")+";"+
				((transfer.getListMaps().get(0).get("APRENSAO_LIVRETE").equals(""))? "0": transfer.getListMaps().get(0).get("APRENSAO_LIVRETE")+"");
				
				ItemFiscalizacaoBody.gravidade(transfer.getListMaps().get(0).get("GRAVIDADE").toString(),"matricula");
				
				
			}
		}	
	}

	@Override
	public void onPosConnected() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void onConnectLost
	() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onStart() 
	{
		super.onStart();
		this.service.setOnTransitActivity(this, this);
	}

	private enum Espera
	{
		INFO_CARTA,
		INFO_MATRICULA
	}
}
