package policia.transito;

import static socket.modelo.Transfer.Intent.REG;
import static socket.modelo.Transfer.Intent.VALIDATE;

import java.util.ArrayList;
import java.util.HashMap;

import policia.transito.adapter.DefaultItemList;
import policia.transito.adapter.GeralExpandableAdapter;
import policia.transito.adapter.ItemAtualizacao;
import policia.transito.adapter.DefaultItemList.ItemTypes;
import policia.transito.data.BaseADIM;
import policia.transito.data.BaseInfracao;
import policia.transito.data.DBObject;
import policia.transito.data.DefaultDatas;
import policia.transito.model.Condutor;
import policia.transito.model.Device;
import policia.transito.model.Veiculo;
import socket.listener.ClienteService;
import socket.listener.OnReciveTranfer;
import socket.modelo.Transfer;
import socket.modelo.Transfer.Intent;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;


@SuppressWarnings("deprecation")
public class Administracao extends TabActivity implements DBObject, DefaultDatas, OnReciveTranfer
{
	private Button btServerRegistrar;  
	private BaseADIM base;
	private TextView tvDiviceImei;
	private TextView tvDivicePolegada;
	private TextView tvDiviceModel;
	private TextView tvDiviceVersio;
	private TextView tvDiviceMarca;
	private EditText edServerHost;
	private EditText edServerUserName;
    private EditText edServerPWD;
	private String user;
	private String host;
	private int state;
	private ExpandableListView expandableListView;
	private GeralExpandableAdapter adapter;
	private TextView tvServerState;
	private TextView tvDiviceState;
	private ClienteService service;
	
	private String pwd;
	private BaseInfracao baseInfracao;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adm_layout_geral);
		
		this.service = ClienteService.findCurrentConnection();
		this.service.setOnTransitActivity(this, this);
		
		this.base = new BaseADIM(this);
		
		this.init();
		
		
        btServerRegistrar.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{
				updateData();
			}
		});
	}
		
	
	protected void reValidDivice()
	{
		Device divice = new Device(this);
		
		Transfer transerRevalid = new Transfer(divice.getMac(), SERVER_APP_NAME, 7002, Intent.VALIDATE, "DIVICE.REVALIDADE.REGISTRATION.STATE");
		HashMap<String, String> map = new HashMap<String, String>();

		
		map.put("MAC", divice.getMac());
		map.put("USER", BaseADIM.getADM_ID(this));
		transerRevalid.getListMaps().add(map);
		transerRevalid.setEspera(Espera.REVALID_DIVECE.name());
		service.transfer(transerRevalid);
	}


	/**
	 * Validar uma edite text
	 * @param ed
	 * @param idMessageError
	 * @return
	 */
	private boolean validadeField(EditText ed, int idMessageError)
	{
		if(ed.getText().toString() == null 
				|| ed.toString().length() == 0)
		{
			Toast.makeText(this, idMessageError, Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}


	private void init()
	{
		baseInfracao = new BaseInfracao(this);
		expandableListView = (ExpandableListView) findViewById(R.id.expandAtualizacaoComponentes);
		adapter = new GeralExpandableAdapter(getApplicationContext());
		adapter.addGroupFrame(new ItemAtualizacao(1));
		adapter.addGroupFrame(new ItemAtualizacao(2));
		adapter.addGroupFrame(new ItemAtualizacao(3));
		
		try
		{
			// ----- CARREGANDO OS VALORES DO FORMULARIO PARA A ACTIVITY
			this.tvDiviceImei = (TextView) findViewById(R.id.tvDiviceImei);
			this.tvDivicePolegada = (TextView) findViewById(R.id.tvDivicePolegada);
			this.tvDiviceModel = (TextView) findViewById(R.id.tvDiviceModel);
			this.tvDiviceVersio = (TextView) findViewById(R.id.tvDiviceVersion);
			this.tvDiviceMarca = (TextView) findViewById(R.id.tvDiviceMarca);
			this.edServerHost = (EditText) findViewById(R.id.edServerHost);
			this.edServerUserName = (EditText) findViewById(R.id.edServerAgente);
			this.edServerPWD = (EditText) findViewById(R.id.edServerAgentePWD);
			
			this.btServerRegistrar = (Button) findViewById(R.id.btServerRegistrar);
			this.tvServerState = (TextView) findViewById(R.id.tvServerState);
			this.tvDiviceState = (TextView) findViewById(R.id.tvDiviceState);
			this.tvDiviceState.setVisibility(View.GONE);
			
			// ============ Criando as tabulações ===============
	        
			createAbas("tabServer", R.id.tabHostServer, R.string.tabServer );
			createAbas("tabAtualizacao", R.id.tabAtualizacao, R.string.sync );
			createAbas("tabDivice", R.id.tabHostDespositivo, R.string.tabDespositivo);
			
			//Postando infomações do despositivo
			Device divice = new Device(this);
			this.tvDiviceImei.setText(divice.getMac());
			this.tvDivicePolegada.setText(divice.getPolegada());
			this.tvDiviceVersio.setText(divice.getVersao());
			this.tvDiviceMarca.setText(divice.getMarca());
			this.tvDiviceModel.setText(divice.getModelo());
			
			
			ArrayList<HashMap<String, Object>> listMap = base.getTable(T_ADMIN);
			HashMap<String, Object> map = listMap.get(0);
		    this.edServerHost.setText((String)map.get(ADM_HOSTSERVER));
		    this.edServerUserName.setText((String)map.get(ADM_USERNAME));
		    this.state = Integer.parseInt(map.get(ADM_STATE).toString());
		    
		    if(this.state == 1)
		    	alterState(R.string.btServerTextReValidar, R.string.state_registrede, R.color.stateColorVerde);
		    else if(this.state == 2)
		    	alterState(R.string.btServerTextReValidar, R.string.state_pendente, R.color.stateColorAzul);
		    else if(this.state == 3)
		    	alterState(R.string.btServerTextReg, R.string.state_noregistrede, R.color.stateColorCinzento);
		
		
		   expandableListView.setAdapter(adapter);
		}catch(Exception ex)
		{
			Log.e("APP", ex.getMessage(), ex);
		}
	}
	
	/**
	 * Aletera o estado das cores do label final
	 * @param textButton
	 * @param stateText
	 * @param stateColor
	 */
	private void alterState(int textButton, int stateText, int stateColor) 
	{
		this.btServerRegistrar.setText(textButton);
		this.tvServerState.setText(stateText);
		this.tvDiviceState.setText(stateText);
		this.tvServerState.setBackgroundResource(stateColor);
		this.tvDiviceState.setBackgroundResource(stateColor);
	}
	
	private void createAbas(String abaKey, int idLayout, int idStringAbaName)
	{
		//Referenciar ao layout server
		TabHost tabHost = getTabHost();
		TabSpec descritor = tabHost.newTabSpec(abaKey);
		descritor.setContent(idLayout);
		descritor.setIndicator(getString(idStringAbaName), getResources().getDrawable(R.drawable.icon_app));
		tabHost.addTab(descritor);
	}
	
	
	protected void updateData() 
	{
		if(validadeField(edServerHost, R.string.no_host)
				&& validadeField(edServerUserName, R.string.no_user)
				&& validadeField(edServerPWD, R.string.no_pwd))
		{
			this.startService();
			if(user.equals(ADM_DEFAULT_USERNAME))
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this,2);
				builder.setTitle("Administração");
				builder.setMessage(R.string.no_user_updade);
				builder.setCancelable(true);
				builder.setNeutralButton("OK", null);
				builder.show();
			}
			try
			{	
				Transfer data = new Transfer("AGENTE:"+this.user, "SERVER:PN.TRANSITO", 7001, VALIDATE, "LoginAdm");
				HashMap<String , String> map = new HashMap<String, String>();
				map.put("ACCESSNAME", this.user);
				map.put("PWD", pwd);
				data.getListMaps().add(map);
				data.setEspera(Espera.LOGIN_ADM.name());
				service.transfer(data);
				
			}catch(Exception ex)
			{
				Log.e("APP", "ERRO A LOGAR ADMINISTRADOR: "+ex.getMessage(), ex);
			}
		}
	}


	private void startService()
	{
		this.user = this.edServerUserName.getText().toString();
		this.host = this.edServerHost.getText().toString();
		this.pwd = this.edServerPWD.getText().toString();
		this.service.setHost(host);
	}
	
	private void registerDivice()
	{
		Device dispositivo = new Device(this);
        Transfer transfer = new Transfer();
        transfer.setIntent(REG);
        transfer.setType(1001);
        transfer.setMessage("RegDispositivo");
        Log.i("APP", "ID ADM: "+BaseADIM.getADM_ID(this));
        HashMap<String, String> map = new HashMap<String, String>();  
        map.put("MARCA", dispositivo.getMarca());
        map.put("MODELO", dispositivo.getModelo());
        map.put("VERSAO", dispositivo.getVersao());
        map.put("POLEGADA", dispositivo.getPolegada());
        map.put("MAC", dispositivo.getMac());
        
        transfer.getListMaps().add(map);
        transfer.setSender(BaseADIM.getADM_ID(this));
        transfer.setReciver("PN.SERVER.APP");
        
        this.startService();
        transfer.setEspera(Espera.REG_DIVICE.name());
        service.transfer(transfer);
            
	}




	@Override
	public void onProcessTranfer(Transfer data) 
	{
		Transfer transerRevalid = data;
		Espera espera = Espera.valueOf(data.getEspera());
		if(espera == Espera.REVALID_DIVECE)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this,2);
			builder.setTitle("Administração");
			builder.setMessage(transerRevalid.getMessage());
			builder.setCancelable(true);
			builder.setNeutralButton("OK", null);
			builder.show();
			ContentValues datas = new ContentValues();
			
			HashMap<String, String> map = transerRevalid.getListMaps().get(0);
			this.state =  Integer.parseInt(map.get("DIVICE.STATE"));
			
			//Quando o despositivo ja tenha sido aceite pela parte de gestão
			if(this.state == 1)
				datas.put(ADM_DIVICENAME, map.get("DIVICE.NAME"));
			
			datas.put(ADM_STATE, this.state);
			base.updateDataAdmin(T_ADMIN, datas , null, null);
			if(this.state == 1) 
			{
				alterState(R.string.btServerTextReValidar, R.string.state_registrede, R.color.stateColorVerde);
				this.tvServerState.setText(map.get("DIVICE.NAME"));
			}
			else if(this.state == 2)
				alterState(R.string.btServerTextReValidar, R.string.state_pendente, R.color.stateColorAzul);
		}
		else if(espera == Espera.LOGIN_ADM)
		{
			if(data != null && Boolean.valueOf(data.getMessage()))
			{
				Log.i("APP", "NEW LOGIN ADM TREAT");
				HashMap<String, String> map = data.getListMaps().get(0);
				ContentValues local = new ContentValues();
				local.put(ADM_HOSTSERVER, this.host);
				local.put(ADM_USERNAME, this.user);
				local.put(ADM_PWD, pwd);
				local.put(ADM_ID, map.get("ID"));
				base.updateDataAdmin(T_ADMIN, local , null, null);
				
				if(state == 3)
					registerDivice();
				else if(state == 2)
					reValidDivice();
			}
			else 
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this,2);
				builder.setTitle("Administração");
				builder.setMessage("Accesso Negado!");
				builder.setCancelable(true);
				builder.setNeutralButton("OK", null);
				builder.show();
			}
		}
		else if(espera == Espera.REG_DIVICE)
		{
			HashMap<String, String> map = data.getListMaps().get(0);
	        Toast.makeText(this, data.getMessage(), Toast.LENGTH_LONG).show();
	        this.state = Integer.parseInt(map.get("DIVICE.STATE"));
	        
	        // Guardar as informacao recebida do servidor da aplicacao
	        BaseADIM local = new BaseADIM(this);
	        ContentValues datas = new ContentValues();
	        datas.put(ADM_STATE, this.state);
	        if(this.state == 2)
	        	alterState(R.string.btServerTextReValidar, R.string.state_pendente, R.color.stateColorAzul);
	        else 
	        {
	        	alterState(R.string.btServerTextReValidar, R.string.state_registrede, R.color.stateColorVerde);
	        	datas.put(ADM_DIVICENAME, map.get("DIVICE.NAME"));
	        }
	        local.updateDataAdmin(T_ADMIN, datas, null, null);
		}
		else if(espera == Espera.INFRACOES)
		{
		    this.baseInfracao = new BaseInfracao(this);
		    this.adapter.removeData(2);
		    baseInfracao.atualizarInfracoes(data);
		    ArrayList<String> infracoes = new ArrayList<String>();
			infracoes = baseInfracao.getInfracaoNome();
			for(String infracao: infracoes)
				adapter.addChildrenGroup(2, new DefaultItemList(0, infracao, ItemTypes.SAMPLE_ITEM));
		}
		else if(espera == Espera.ESTADO_CONDUTOR)
		{
			baseInfracao.loadDriverState(data);
			adapter.removeData(0);
			ArrayList<Condutor> estadoCondutor = new ArrayList<Condutor>();
			estadoCondutor = baseInfracao.getEstadosCondutores();
			for(Condutor condutor: estadoCondutor)
				adapter.addChildrenGroup(0, new DefaultItemList(Integer.valueOf(condutor.getIdEstadoCondutor()), condutor.getDescricaoEstado(), ItemTypes.SAMPLE_ITEM));			
		}
		else if(espera == Espera.CATEGORIA_VEICULO)
		{
			this.adapter.removeData(1);
			baseInfracao.carregarCategoriasVeiculo(data);
			ArrayList<Veiculo> categoriasVeiculo = new ArrayList<Veiculo>();
			categoriasVeiculo = baseInfracao.getCategoriasVeiculo();
			for(Veiculo veiculo : categoriasVeiculo)
				adapter.addChildrenGroup(1, new DefaultItemList(Integer.valueOf(veiculo.getIdCategoria()), veiculo.getCategoriaDescricao(), ItemTypes.SAMPLE_ITEM));
		}	
	}
	
	
	private enum Espera
	{
		REVALID_DIVECE,
		LOGIN_ADM, 
		REG_DIVICE,
		INFRACOES,
		ESTADO_CONDUTOR,
		CATEGORIA_VEICULO
	}


	@Override
	public void onPosConnected()
	{
		this.edServerHost.setEnabled(false);
		
	}


	@Override
	public void onConnectLost()
	{
		this.edServerHost.setEnabled(true);
		Toast.makeText(this, "Fora do serviço", 1).show();
	}
}
