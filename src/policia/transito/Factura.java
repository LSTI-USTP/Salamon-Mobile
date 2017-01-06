package policia.transito;

import policia.transito.data.DefaultDatas;
import socket.listener.ClienteService;
import socket.listener.OnReciveTranfer;
import socket.modelo.Transfer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Factura extends Activity implements OnClickListener, DefaultDatas, OnReciveTranfer
{
	private Button btInicio;
	private TextView codigo;
	private TextView numCartaViatura;
	private TextView valorPagar;
	private ClienteService service;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_factura);
		overridePendingTransition(R.anim.translate_direita_to_esquerda, R.anim.translate_esquerda_to_direita);
		this.service = ClienteService.findCurrentConnection();
		this.service.setOnTransitActivity(this, this);
		this.init();
	}
	
	private void init()
	{
		this.btInicio = (Button) findViewById(R.id.inicio);
		this.codigo = (TextView) findViewById(R.id.codigo);
		this.numCartaViatura = (TextView) findViewById(R.id.numCartaViatura);
		this.valorPagar = (TextView) findViewById(R.id.valorPagar);
		this.btInicio.setOnClickListener(this);

		String result = getIntent().getExtras().getString(RESUTADO_FISCALIZACAO);
		this.codigo.setText(result.split(";")[0]);
		this.valorPagar.setText(result.split(";")[2]);
		if(result.split(";")[1].startsWith("/"))
		{
			String matricula = result.split(";")[1];
			this.numCartaViatura.setText(matricula.split("/")[1]);
		}
		else this.numCartaViatura.setText(result.split(";")[1]);
	}
	
	@Override
	public void onClick(View v)
	{
		if(v.equals(this.btInicio))
		{
			Bundle values = new Bundle();
			values.putString(NEXT_COD, NEXT_SUCCESS);
			getIntent().putExtras(values);
			this.setResult(Principal.COD_CALL_NEXT, getIntent());
			finish();
		}	
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
	public void onConnectLost() {
		// TODO Auto-generated method stub
		
	}
	
}
