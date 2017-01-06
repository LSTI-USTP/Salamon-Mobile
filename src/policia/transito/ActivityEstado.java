package policia.transito;

import policia.transito.data.DefaultDatas;
import socket.listener.OnReciveTranfer;
import socket.modelo.Transfer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ActivityEstado extends Activity implements OnReciveTranfer, DefaultDatas, OnClickListener
{

	private View botaoVoltar;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_satate);
	    overridePendingTransition(R.anim.translate_direita_to_esquerda, R.anim.translate_esquerda_to_direita);
		init();
	}
	
	private void init()
	{
		TextView tituloForm = (TextView)findViewById(R.id.areaNavegationTitle);
		tituloForm.setText("");
		findViewById(R.id.areaNavegationGoFront).setVisibility(View.GONE);
		String result = getIntent().getExtras().getString(RESULTADO);
		TextView txtProcura = (TextView) findViewById(R.id.tvStateProcura);
		TextView titulo = (TextView) findViewById(R.id.tituloEstadoCartaMatricula);
		TextView txtMultas = (TextView) findViewById(R.id.tvStateMultas);
		TextView txtGravidade = (TextView) findViewById(R.id.tvStateGravidade);
		TextView txtCarta = (TextView) findViewById(R.id.tvStateCarta);
		TextView txtEstadoVeiculo = (TextView) findViewById(R.id.tvStateVeiculo);
		TextView txtLivrete = (TextView) findViewById(R.id.tvStateLivrete);

		
		this.botaoVoltar = (View) findViewById(R.id.areaNavegationGoBack);
		this.botaoVoltar.setOnClickListener(this);
		if(result != null)
		{
			if(result.split(";")[0].equals("carta"))
			{
				titulo.setText("Estado de Carta");
				findViewById(R.id.viewStatePrendeCarta).setVisibility(View.VISIBLE);
				findViewById(R.id.viewStatePrendVeiculo).setVisibility(View.GONE);
				findViewById(R.id.viewStatePrendLivrete).setVisibility(View.GONE);
				
				txtProcura.setText((result.split(";")[1].equals("0") || result.split(";")[1].equals(""))? "Nenhuma" : "Procurado");
				txtMultas.setText((result.split(";")[2].equals("0") || result.split(";")[2].equals(""))? "Nenhuma" : result.split(";")[2]);
				
				if(result.split(";")[3].equals("0") || result.split(";")[3].equals(""))
					txtGravidade.setText("Nenhuma");
				else if(result.split(";")[3].equals("1"))
					txtGravidade.setText("Leve");
				else if(result.split(";")[3].equals("2"))
					txtGravidade.setText("Grave");
				else txtGravidade.setText("Muito grave");
				
				txtCarta.setText((result.split(";")[4].equals("0") || result.split(";")[4].equals(""))? "Não apreendido" : "Apreendido");
			}
			else
			{
				titulo.setText("Estado de Matricula");
				findViewById(R.id.viewStatePrendeCarta).setVisibility(View.GONE);
				findViewById(R.id.viewStatePrendVeiculo).setVisibility(View.VISIBLE);
				findViewById(R.id.viewStatePrendLivrete).setVisibility(View.VISIBLE);
				
				txtProcura.setText((result.split(";")[1].equals("0") || result.split(";")[1].equals(""))? "Nenhuma" : "Procurado");
				txtMultas.setText((result.split(";")[2].equals("0") || result.split(";")[2].equals(""))? "Nenhuma" : result.split(";")[2]);
				
				if(result.split(";")[3].equals("0") || result.split(";")[3].equals(""))
					txtGravidade.setText("Nenhuma");
				else if(result.split(";")[3].equals("1"))
					txtGravidade.setText("Leve");
				else if(result.split(";")[3].equals("2"))
					txtGravidade.setText("Grave");
				else txtGravidade.setText("Muito grave");
				
				txtEstadoVeiculo.setText((result.split(";")[4].equals("0") || result.split(";")[4].equals(""))? "Não apreendido" : "Apreendido");
				txtLivrete.setText((result.split(";")[5].equals("0") || result.split(";")[5].equals(""))? "Não apreendido" : "Apreendido");
			}
			
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

	@Override
	public void onClick(View clique) 
	{
		if(clique.equals(this.botaoVoltar))
		{
			finish();
		}
			
		
	}

	
}
