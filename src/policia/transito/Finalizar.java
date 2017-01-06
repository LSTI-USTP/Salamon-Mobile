package policia.transito;

import java.util.ArrayList;
import java.util.HashMap;

import policia.transito.adapter.GeralListAdapter;
import policia.transito.adapter.ItemInfracao;
import policia.transito.data.BaseFiscalizacao;
import policia.transito.data.BaseInfracao;
import policia.transito.data.DBObject;
import policia.transito.data.DefaultDatas;
import policia.transito.model.ActivityName;
import policia.transito.model.Agente;
import policia.transito.model.DualValues;
import policia.transito.model.Fiscalizacao;
import policia.transito.model.Infracao;
import policia.transito.model.Infracao.NivelGravidade;
import policia.transito.model.Localizacao;
import socket.listener.ClienteService;
import socket.listener.OnReciveTranfer;
import socket.modelo.Transfer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class Finalizar extends Activity implements OnClickListener, DefaultDatas, OnReciveTranfer, DBObject
{
	private ListView listInfraoSeleted;
	private GeralListAdapter adapter;
	private View retroceder;
	private Button btConcluir;
	private CheckBox checkCarta;
	private CheckBox checkVeiculo;
	private CheckBox checkLivrete;
	private CheckBox checkCondutor;
	private ArrayList<DualValues<String, NivelGravidade>> duals;
	private ArrayList<Infracao> infracoesSelecionadas;
	private Localizacao localizacao;
	private ClienteService service;
	private Fiscalizacao fiscalizacao;
	private ProgressDialog progressDialog;
	private String tipoInfracao="";
	private BaseFiscalizacao baseFiscalizacao;
	public static int COD_NEXT_FINALIZAR = 25;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_finalizar);
		this.localizacao = new Localizacao(this);
		this.init();	
		this.fiscalizacao = baseFiscalizacao.getFiscalizacao();
		Toast.makeText(this, this.fiscalizacao.getNomeCondutor(), 1);
	}
	
	private void init()
	{
		baseFiscalizacao = new BaseFiscalizacao(this);
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Registrando a fiscalização...");
		
		this.service = ClienteService.findCurrentConnection();
		this.service.setOnTransitActivity(this, this);
		
		findViewById(R.id.areaNavegationGoFront).setVisibility(View.GONE);
		TextView titulo =(TextView) findViewById(R.id.areaNavegationTitle);
		titulo.setText(R.string.concluirRegistro);
		this.retroceder = (View) findViewById(R.id.areaNavegationGoBack);
	  	retroceder.setOnClickListener(this);
	  	this.checkCarta = (CheckBox) findViewById(R.id.finalizarCheckCarta);
	  	this.checkVeiculo = (CheckBox) findViewById(R.id.finalizarCheckVeiculo);
	  	this.checkLivrete = (CheckBox) findViewById(R.id.finalizarCheckLivrete);
	  	this.checkCondutor = (CheckBox) findViewById(R.id.finalizarCheckCondutor);
		this.listInfraoSeleted = (ListView) findViewById(R.id.listFinalizarInfracaoSeleted);
		
		this.checkCarta.setOnClickListener(this);
		this.checkVeiculo.setOnClickListener(this);
		this.checkLivrete.setOnClickListener(this);
		this.checkCondutor.setOnClickListener(this);
	
		ActivityName activityName = ActivityName.valueOf(getIntent().getExtras().getString(ACTIVITY_NAME));
		this.adapter = new GeralListAdapter(this);
		this.btConcluir = (Button) findViewById(R.id.btFinalizarConcluir);
	  	this.btConcluir.setOnClickListener(this);
		BaseInfracao base = new BaseInfracao(this);
		this.infracoesSelecionadas = new ArrayList<Infracao>();
		
		if(activityName==ActivityName.INFRACAO)
		{
			this.infracoesSelecionadas = base.infracoesSelecionadas();
			this.duals = new ArrayList<DualValues<String,NivelGravidade>> ();
			
			for(Infracao infracao: this.infracoesSelecionadas)
			{
				NivelGravidade nivel = null;
				if(infracao.getGravidade().equals("0")) nivel = null;
				if(infracao.getGravidade().equals("1")) nivel = NivelGravidade.LEVE;
				if(infracao.getGravidade().equals("2")) nivel = NivelGravidade.GRAVE;
				if(infracao.getGravidade().equals("3")) nivel = NivelGravidade.MUITO_GRAVE;
				duals.add(new DualValues<String,NivelGravidade>(infracao.getId(), nivel));
			}
			ArrayList<Infracao> listInfracaoSelecionada = base.reLoadInfracaoSelecionada(duals);
			for(Infracao infracao: listInfracaoSelecionada)
			{
				adapter.addIten(new ItemInfracao(infracao));
				tipoInfracao=tipoInfracao+infracao.getTipoInfracacao().name()+"-";
			    Log.i("APP", infracao.toString());
			   
			}
			Log.i("APP", "TIPO DE INFRAÇÃO "+tipoInfracao);
	     	this.listInfraoSeleted.setAdapter(adapter);
		}   
	}

	/**
	 * Recuparar todos os click da activite aqui
	 */
	@Override
	public void onClick(View v)
	{
		if(v.equals(btConcluir))
			regFiscalizacaoServidor();
		else if(v.equals(this.checkCarta) || v.equals(this.checkVeiculo) || v.equals(this.checkLivrete))
			validarApreensao();	
		else if(v.equals(retroceder)) 
		{
			this.voltar();	
		}
	}
	
	private void voltar() 
	{
		Bundle values = new Bundle();
		values.putString(NEXT_COD, NEXT_BACK);
		getIntent().putExtras(values);
		this.setResult(Principal.COD_CALL_NEXT, getIntent());
		finish();
		
	}
	@Override
	public void onBackPressed()
	{
		voltar();
	}
	private boolean validarApreensao()
	{
		boolean valido = true;
		if(this.infracoesSelecionadas.size() == 0)
		{
			if(this.checkCarta.isChecked() == true || this.checkVeiculo.isChecked() == true || this.checkLivrete.isChecked() == true)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this, 2);
				builder.setTitle("Registro de Fiscalização");
				builder.setMessage("Não pode fazer apreensão sem infrações!");
				builder.setNeutralButton("OK", null);
				builder.setCancelable(true);
				builder.show();
				this.checkCarta.setChecked(false);
				this.checkVeiculo.setChecked(false);
				this.checkLivrete.setChecked(false);
				valido = false;
			}
		}
		return valido;
	}
	

	/**
	 * Registra a fiscalização na base de dados do servidor
	 */
	private void regFiscalizacaoServidor()
	{	
		if(!localizacao.hasLocation())
			this.localizacao.findLocation();
		
		progressDialog.show();
		this.fiscalizacao = baseFiscalizacao.getFiscalizacao();

		HashMap<String, String> map = new HashMap<String, String>();
		
		Log.i("APP", "LOCALIZACAO = "+localizacao);
		
		map.put("USER.ID", Agente.getUserLogado(this).getId()+"");
		map.put("USER.ALOCACAO",Agente.getUserLogado(this).getidAlocacao()+"");
		map.put("VEICULO.MATRICULA", fiscalizacao.getNumMatricula());
		map.put("CONDUTOR.CARTA", fiscalizacao.getNumCarta());
		map.put("CONDUTOR.STATE", fiscalizacao.getEstadoCondutor()+"");
		map.put("CONDUTOR.COMPATIL", fiscalizacao.getIncompatibilidadeCarta()+"");
		map.put("CONDUTOR.NOME", fiscalizacao.getNomeCondutor());
		map.put("VEICULO.CATEGORIA", fiscalizacao.getCategoriaVeiculo()+"");
		map.put("LOCAL.LATITUDE", localizacao.getLatitude()+"");
		map.put("LOCAL.LONGITUDE", localizacao.getLongitude()+"");
		map.put("LOCAL.LOCAL", localizacao.getAddress());
		map.put("PRENDE.VEICULO", ((this.checkVeiculo.isChecked())? "1" : "0"));
		map.put("PRENDE.CARTA", ((this.checkCarta.isChecked())? "1" : "0"));
		map.put("PRENDE.LIVRETE", ((this.checkLivrete.isChecked())? "1" : "0"));
		map.put("PRENDE.CONDUTOR", ((this.checkCondutor.isChecked())? "1" : "0"));
		map.put("EXIST.CARTA", fiscalizacao.getExistenciaCarta()+"");
		map.put("EXIST.LIVRETE", fiscalizacao.getLivrete()+"");
	
	    Transfer transfer = new Transfer(Agente.getSender(this, this), SERVER_APP_NAME, 1003, socket.modelo.Transfer.Intent.REG, "Petição para registrar fiscalização");
		transfer.getListMaps().add(map);
		transfer.setEspera(Espera.REG_FISCALIZACAO.name());
		 if(this.infracoesSelecionadas.size()>0)
		 {
			 for(Infracao infracao: infracoesSelecionadas)
			 {
				 HashMap<String, String> hashMap = new HashMap<String, String>();
				 hashMap.put("INFRACAO.ID", infracao.getId());
				if(!infracao.getGravidade().equals("0"))
					hashMap.put("INFRACAO.GRAVIDADE", infracao.getGravidade());
				else hashMap.put("INFRACAO.GRAVIDADE", null);
				
				 transfer.getListMaps().add(hashMap);
			 }
		 }
		 service.transfer(transfer);
		 
	}
	
	@Override
	public void onPosConnected() 
	{
		
	}

	@Override
	public void onProcessTranfer(Transfer transfer) 
	{
		Transfer data = transfer;	
		Espera espera = Espera.valueOf(data.getEspera());
		if(espera == Espera.REG_FISCALIZACAO)
		{
			Bundle extras = new Bundle();
			Intent telaFinal = new Intent(this, Factura.class);
			
			if(data.getMessage().equals("true"))
			{
				Log.i("APP", "ENTROU NO PROCESSB TRANSFER");
				extras.putString(RESUTADO_FISCALIZACAO,
						data.getListMaps().get(0).get(FISCALIZACAO_RESULTADO_ID)+";"+
				fiscalizacao.getNumCarta()+"/"+fiscalizacao.getNumMatricula()
						+";"+data.getListMaps().get(0).get(FISCALIZACAO_RESULTADO_VALOR_PAGAR));
				
				progressDialog.dismiss();
				telaFinal.putExtras(extras);			
				startActivityForResult(telaFinal, COD_NEXT_FINALIZAR);
			}
	    }
	}
	
	private enum Espera
	{
		REG_FISCALIZACAO
	}
	
	@Override
	public void onConnectLost()
	{
		
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		this.service.setOnTransitActivity(this, this);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		 if(requestCode == COD_NEXT_FINALIZAR)
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
	


	
}
