package policia.transito;

import policia.transito.adapter.DefaultItemList;
import policia.transito.adapter.DefaultItemList.ItemTypes;
import policia.transito.adapter.GeralListAdapter;
import policia.transito.data.DefaultDatas;
import policia.transito.model.DualValues;
import socket.listener.ClienteService;
import socket.listener.OnReciveTranfer;
import socket.modelo.Transfer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class TecladoName extends Activity implements OnClickListener, OnItemSelectedListener, DefaultDatas, OnReciveTranfer
{
	private Spinner spinerVaforitos;
	private TextView tvReadInputText;
	private TeclaTypes type;
	private String readMatricula;
	private View goBack;
	
	/**
	 * PARA AS MATRICULAS
	 * STATE 1 - ESPERANDO TIPO MATRICULA  <br/>
	 * STATE 2 - ESPERANDO PRIMEIRO NUMERO <br/>
	 * STATE 3 - ESPERANDO SEGUNDO NUMERO  <br/>
	 * STATE 4 - ESPERANDO TERCEIRO NUMERO
	 * STATE 5 - ESPERANDO O QUARTO NUMERO
	 * STATE 6+- ESPERANDO AS LETRAS
	 */
	
	private int wait;
	private TextView titleTeclado;
	private GeralListAdapter adapter;
	private String oldValues;
	private int codResult;
	private ClienteService service;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_teclado_name);
		
		this.service = ClienteService.findCurrentConnection();
		service.setOnTransitActivity(this, this);
		
		init();
		Bundle values = this.getIntent().getExtras();
		this.type =  TeclaTypes.valueOf(values.getString(TECLADO_TYPE));
		this.oldValues = values.getString(TECLADO_CURRENT_VALUE);
		this.codResult = values.getInt(TECLADO_COD_INTENT);
		
		
		this.tvReadInputText.setText(oldValues);
		
	}
	
	@SuppressWarnings("unchecked")
	private void init()
	{
		this.tvReadInputText = (TextView) findViewById(R.id.tvTecladoReadInputText);
		this.goBack = findViewById(R.id.areaNavegationGoBack);
		findViewById(R.id.areaNavegationGoFront).setVisibility(View.GONE);
		this.titleTeclado = (TextView) findViewById(R.id.areaNavegationTitle);
		this.titleTeclado.setGravity(Gravity.LEFT);
		titleTeclado.setText("Nome do condutor");
		
		
		this.goBack.setOnClickListener(this);
	}
	
	@Override
	public void onBackPressed()
	{
		cancelWrite();
		super.onBackPressed();
	}

	private void cancelWrite() {
		Bundle values = new Bundle();
		values.putString(TECLADO_OLD_VALUE, this.oldValues);
		values.putString(TECLADO_RESULT, TeclaResust.CANCEL.name());
		Intent response = this.getIntent();
		response.putExtras(values);
		setResult(this.codResult, response);
		this.finish();
	}

	@Override
	public void onClick(View v)
	{
		try
		{
			if(v instanceof Button)
			{
				Button bt = (Button) v;
				String text = bt.getText().toString();
				this.write(text, 'N');
			}
			else if (v.equals(this.goBack))
				cancelWrite();
		}catch(Exception ex)
		{
			Log.e("APP", "Teclado - Erro ao tratar o evento do botão: "+ex.getMessage(), ex);
			this.reseteReadText();
		}
		
	}
	
	/**
	 * Ecrevre os texto no ecran
	 * @param text O texto que deve ser escrito
	 * @param typeInput O tipo do texto {M - Matricula | N - Numero e Letras}
	 */
	private void write(String text, char typeInput) 
	{
		String oldText = this.readOutputText();
		Log.i("APP", "INICIAL -> OLD = "+oldText+" | TEXT = "+text + " | TYPE = "+typeInput+ " | STATE = "+wait);
		switch (this.type)
		{
			case MATRICULA:
				// Para a matricula so esta sendo aceita apenas 15 carracteres o que coresponse ao STP de 5 Letras
				if(oldText.length()==15) return;
				// EXEMPLE STP 67-90 BBBBB
				//STATE EX:  1 23-45 6++
				if(this.wait == 1 && typeInput == 'M')
				{
					writeOutputText(text);
					this.readMatricula = text;
					this.wait ++;
				}
//				else if(wait == 2 && typeInput == 'M')
//				{
//					writeOutputText(text);
//					this.readMatricula = text;
//				}
				else if(this.readMatricula != null && readMatricula.equals("STP") && this.wait>1)
				{
					if((this.wait == 2 && typeInput == 'N') 
							|| (this.wait == 6 && text.charAt(0)>= 'A' && text.charAt(0) <= 'Z'))
					{
						writeOutputText(oldText+" "+text);
						this.wait++;
					}
					
					else if((this.wait == 3 || this.wait == 5 || (text.charAt(0)>= 'A' && text.charAt(0) <= 'Z')) 
							&& typeInput == 'N')
					{
						writeOutputText(oldText+text);
						this.wait++;
					}
					else if(this.wait == 4 && typeInput == 'N')
					{
						writeOutputText(oldText+"-"+text);
						this.wait ++;
					}
				}
				else if(this.wait > 1)
				{
					writeOutputText(oldText+""+text);
					this.wait++;
				}
				oldText = this.readOutputText();
				break;
			case CARTA:
				if(typeInput == 'N' 
				&& !text.equals(" ") 
				&& !text.equals("-") 
				&& text.charAt(0) >= '0'
				&& text.charAt(0) <= '9')
				{
					writeOutputText(oldText+text);
					this.wait++;
				}
				break;
				
		}
		
	}

	
	public void backSpace (View view)
	{
		try
		{
			String oldText = this.readOutputText();
			switch(this.type)
			{
				case MATRICULA:
					if(this.wait == 2)
					{
						this.writeOutputText("");
						this.readMatricula = null;
						this.wait = 1;
						reseteReadText();
					}
					//Quando for lido anteriomente uma matricula de STP
					//       012345678901
					//EXEPLE STP 60-98 X
					//STATE    1 23 45 6++
					         
					if(this.readMatricula != null && this.readMatricula.equals("STP"))
					{
						if(this.wait == 3 || this.wait == 5 || this.wait == 6)
						{
							 this.writeOutputText(oldText.substring(0, oldText.length()-2));
							 this.wait--;
						}
						else if(this.wait == 4 || this.wait > 6)
						{
							this.writeOutputText(oldText.substring(0, oldText.length()-1));
							this.wait --;
						}
					}
					else if(wait>2 && oldText.length()>0)
					{
						this.writeOutputText(oldText.substring(0, oldText.length()-1));
						this.wait--;
					}
					
					break;
				case CARTA:
					this.writeOutputText(oldText.substring(0, oldText.length()-1));
					this.wait--;
			}
		}
		catch(Exception ex)
		{
			Log.e("APP", "Teclado - Error ao limpar a tela: "+ex.getMessage(), ex);
		}
		
	}
	
	/**
	 * Quando clicar no botao ok
	 * @param ver
	 */
	public void onClickOk(View ver)
	{
		Bundle values = new Bundle();
		values.putString(TECLADO_RESULT, TeclaResust.SUCCESS.name());
		values.putString(TECLADO_NEW_VALUE, this.tvReadInputText.getText().toString());
		values.putString(TECLADO_OLD_VALUE, this.oldValues);
		
		getIntent().putExtras(values);
		this.setResult(10, getIntent());
		finish();
	}

	private void reseteReadText() 
	{
		this.wait = 1;
		this.tvReadInputText.setTag("");
		this.readMatricula = null;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long idComponets) 
	{
		try 
		{
			Log.i("APP", "CLICK NA LISTA POSICAO = "+index);
			if(index == 0) return;
			
			String text  = ((TextView) arg1.findViewById(R.id.tvItemSpinerValueFAV)).getText().toString();
			this.spinerVaforitos.setSelection(0);
			char c = (text.endsWith("-") || text.equals(" "))? 'N': 'M';
			this.write(text, c);
		} catch (Exception e) 
		{
			Log.e("APP", "Teclado - Error ao receber os favoritos: "+e.getMessage(), e);
			this.reseteReadText();
		}
	}

	private String readOutputText() 
	{
		return this.tvReadInputText.getText().toString();
	}
	
	private void writeOutputText(String text) 
	{
		this.tvReadInputText.setText(text);
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) 
	{
		
	}
	
	
	public static enum TeclaTypes
	{
		CARTA,
		MATRICULA,
		NOME_CONDUTOR
	}
	
	public static enum TeclaResust
	{
		SUCCESS,
		CANCEL
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
