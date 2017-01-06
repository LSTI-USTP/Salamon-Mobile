package policia.transito;

import policia.transito.data.BaseADIM;
import policia.transito.data.BaseFiscalizacao;
import policia.transito.data.DBObject;
import policia.transito.data.DefaultDatas;
import socket.listener.OnReciveTranfer;
import socket.listener.ClienteService;
import socket.modelo.Transfer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ActivityInitial extends Activity implements OnClickListener, DefaultDatas, DBObject, OnReciveTranfer
{
	private View viewDevice;
	private View viewTerminarSessao;
	private TextView textNomeUtilizador;
	private TextView textCodigoAcesso;
	private TextView tipoOperacao;
	private TextView textNivelAtuacao;
	private TextView textDeviceName;
	private TextView textZonaOperacao;
	private ClienteService service;
	public static int COD_CALL_NEXT = 25;
	public static int COD_NEXT_INICIAL = 25;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_initial);
		init();
	}
	
	private void init()
	{
		this.service = ClienteService.findCurrentConnection();
		this.service.setOnTransitActivity(this, this);
		
		BaseFiscalizacao baseFiscalizacao = new BaseFiscalizacao(this);
		this.textNomeUtilizador = (TextView) findViewById(R.id.telaInicialNomeUtilizador);
		this.textCodigoAcesso = (TextView) findViewById(R.id.telaInicialCodigoAcesso);
		this.tipoOperacao = (TextView) findViewById(R.id.telaInicialTipoOperacao);
		this.textNivelAtuacao = (TextView) findViewById(R.id.telaInicialNivelAtuacao);
		this.textDeviceName = (TextView) findViewById(R.id.telaInicialNomeDespositivo);
		this.textZonaOperacao = (TextView) findViewById(R.id.telaInicialZona);
		this.viewDevice =findViewById(R.id.imageInicialDivice);
		this.viewTerminarSessao = findViewById(R.id.telaInicialTerminarSessao);
		this.viewTerminarSessao.setOnClickListener(this);
		this.viewDevice.setOnClickListener(this);
		
		Log.i("APP", "NOME "+baseFiscalizacao.getAgente().getNome());
		if(baseFiscalizacao.getAgente().getAutenticacao() ==1)
		{
			this.textNomeUtilizador.setText(baseFiscalizacao.getAgente().getNome()+" "+baseFiscalizacao.getAgente().getApelido());
			this.textCodigoAcesso.setText(baseFiscalizacao.getAgente().getCodigoAcesso());
			this.tipoOperacao.setText(baseFiscalizacao.getAgente().getTipoOperacao());
			this.textNivelAtuacao.setText(baseFiscalizacao.getAgente().getNivelAtuacao());
			this.textDeviceName.setText(BaseADIM.getDeviceName(this));
			this.textZonaOperacao.setText(baseFiscalizacao.getAgente().getOperacao());
		}
		else
		{
			this.textNomeUtilizador.setText("Indisponível");
			this.textCodigoAcesso.setText(baseFiscalizacao.getAgente().getCodigoAcesso());
			this.tipoOperacao.setText("Indisponível");
			this.textNivelAtuacao.setText("Indisponível");
			this.textDeviceName.setText(BaseADIM.getDeviceName(this));
			this.textZonaOperacao.setText("Indisponível");
		}
		
		
	}

	@Override
	public void onClick(View v) 
	{
		if(v.equals(this.viewDevice))
		{
			Intent intent = new Intent(this,Principal.class); // redireciona para a tela de dados de fiscalização
			startActivity(intent);
		}
		else if(v.equals(this.viewTerminarSessao))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Fiscalização");
			builder.setMessage("Deseja terminar a sessão?");
			builder.setCancelable(true);
			builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() 
			{
				@Override // Terminar a sessão do agente logado no sistem
				public void onClick(DialogInterface dialog, int which) 
				{
					Intent intent = new Intent(ActivityInitial.this,  Login.class);
					startActivity(intent);
				}
			});
			
			builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() 
			{	
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});
			builder.show();
		}
	}
	
	@Override
	public void onBackPressed() 
	{
	}

	@Override
	public void onProcessTranfer(Transfer transfer) 
	{
		
	}

	@Override
	public void onPosConnected() 
	{
		
	}

	@Override
	public void onConnectLost() {
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if(requestCode == COD_NEXT_INICIAL)
		 {
			 Bundle values = data.getExtras();
			if(values.getString(NEXT_COD).equals(NEXT_SUCCESS))
			{
				Bundle value = new Bundle();
				value.putString(NEXT_COD, NEXT_SUCCESS);
				getIntent().putExtras(value);
				this.setResult(Login.COD_CALL_NEXT, getIntent());
				finish();
			}
		 }
	}
}
