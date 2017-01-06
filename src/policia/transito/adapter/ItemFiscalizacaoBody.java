package policia.transito.adapter;

import java.util.ArrayList;

import policia.transito.R;
import policia.transito.adapter.DefaultItemList.ItemTypes;
import policia.transito.model.Combo;
import policia.transito.model.ItemList;
import policia.transito.model.OnIntentTreater;
import policia.transito.model.OnIntentTreater.EventsCod;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class ItemFiscalizacaoBody implements ItemList, OnItemSelectedListener, OnClickListener, OnCheckedChangeListener
{

	private int typeGroup;
	private OnIntentTreater onClickListener;
	private ArrayList<Combo> listaCombo = new ArrayList<Combo>();
	private int posisaoSelecionada;
	private TextView textField;
	private String descricaoItem;
	private int idItem;
	private boolean existencia;
	private boolean incompatilidade;
	private boolean livrete;
	private CheckBox existenciaCarta;
	private CheckBox checkIncompatibilidade;
	private TextView textViewNumCarta;
	private TextView textNomeCondutor;
	private String nomeCondutor;
	private String numCarta;
	private String numMatricula;
	private CheckBox checkLivrete;
	private String idMatricula;
	private Spinner spn;
	private static View iconeInfoCarta;
	private static View iconeInfoMatricula;
	
	public ItemFiscalizacaoBody(int typeGroup) 
	{
		this.typeGroup = typeGroup;
	}
	
	
	public int getIdItem() {
		return idItem;
	}


	public void setIdItem(int idItem) 
	{
		if(idItem == -1)
			idItem = 0;
		this.idItem = idItem;
		if(this.spn != null)
			spn.setSelection(idItem);

	}


	public void setDescricaoItem(String descricaoItem) {
		this.descricaoItem = descricaoItem;
	}


	public ItemFiscalizacaoBody()
	{
		
	}
	
	public ItemFiscalizacaoBody(int typeGroup, OnIntentTreater onIntentTreater) 
	{
		this.typeGroup = typeGroup;
		this.onClickListener = onIntentTreater;
	}
	
	public void setValueBox(String text)
	{
		if(this.typeGroup != 1) this.textField.setText(text);
	}
	
	

	public int getPosisaoSelecionada() {
		return posisaoSelecionada;
	}


	public void setLista(ArrayList<Combo> list)
	{
		this.listaCombo = list;
	}
	public int getTypeData()
	{
		return this.typeGroup;
	}

	@Override
	public View prepareView(LayoutInflater inflater, Context appContext, ViewGroup viewGroup, View enterView, int indexPosition)
	{
		View bodyView = null;
		bodyView  = inflater.inflate(R.layout.items_statics, null);

			switch(this.typeGroup)
			{

				case 1: // carrega o estado do condutor
					if(bodyView != null)
					{
						this.textField = (TextView) bodyView.findViewById(R.id.tvFiscaDriverName);
						bodyView.findViewById(R.id.lilayFiscaDriver).setVisibility(View.VISIBLE);
						spn = (Spinner) bodyView.findViewById(R.id.spnerBodyFiscaDriver);
						GeralListAdapter adapter = new GeralListAdapter(appContext);
						for(Combo value: listaCombo)
						{
						      adapter.addIten(new DefaultItemList(Integer.valueOf(value.getId()), value.getValue(),ItemTypes.SPINNER_ITEM));
						}
						spn.setAdapter(adapter);
						spn.setOnItemSelectedListener(this);
						spn.setSelection(this.posisaoSelecionada);
						if(this.onClickListener != null)
						{
							this.textField.setOnClickListener(this);
						}
						this.textField.setText(nomeCondutor);
					}   
					break;					
				case 2:
					if(bodyView != null)
					{
						bodyView.findViewById(R.id.lilayFiscaCard).setVisibility(View.VISIBLE);
						this.textField = (TextView) bodyView.findViewById(R.id.tvFiscaCarta).findViewById(R.id.tvCaixaTexto);
						iconeInfoCarta = (View)bodyView.findViewById(R.id.tvFiscaCarta).findViewById(R.id.layTextFilediconArea);
						if(this.onClickListener != null)
						{
							this.textField.setOnClickListener(this);
						}
						this.existenciaCarta = (CheckBox)bodyView.findViewById(R.id.checkBodyExistenciaCarta);
						this.existenciaCarta.setChecked(this.existencia);
						existenciaCarta.setOnCheckedChangeListener(this);
						this.checkIncompatibilidade = (CheckBox) bodyView.findViewById(R.id.checkBodyCompatibilidade);
						this.checkIncompatibilidade.setChecked(incompatilidade);
						this.checkIncompatibilidade.setOnCheckedChangeListener(this);
						this.textField.setText(numCarta);
						iconeInfoCarta.setOnClickListener(this);
					}
					break;
				case 3:
					if(bodyView !=  null)
					{
						bodyView.findViewById(R.id.relayFiscaVeiculo).setVisibility(View.VISIBLE);
						this.textField = (TextView) bodyView.findViewById(R.id.edFiscaNumMatricula).findViewById(R.id.tvCaixaTexto);
						iconeInfoMatricula = (View)bodyView.findViewById(R.id.edFiscaNumMatricula).findViewById(R.id.layTextFilediconArea);
						this.textField.setOnClickListener(this);
						
						this.spn = (Spinner) bodyView.findViewById(R.id.spinnerFiscaCatVeiculo);
						GeralListAdapter adapter = new GeralListAdapter(appContext);
						for(Combo value: listaCombo)
						{
						      adapter.addIten(new DefaultItemList(Integer.valueOf(value.getId()), value.getValue(),ItemTypes.SPINNER_ITEM));
						}
						spn.setAdapter(adapter);	
						spn.setOnItemSelectedListener(this);
						spn.setSelection(this.posisaoSelecionada);
						
						this.textField.setText(numMatricula);
						this.checkLivrete = (CheckBox) bodyView.findViewById(R.id.checkFiscasLivreteExist);
						this.checkLivrete.setChecked(livrete);
						this.checkLivrete.setOnCheckedChangeListener(this);		
						iconeInfoMatricula.setOnClickListener(this);
					}
					break;
			}
//		}
		return bodyView;
	}

	
	public CheckBox getCheckLivrete() {
		return checkLivrete;
	}


	public void setCheckLivrete(CheckBox checkLivrete) {
		this.checkLivrete = checkLivrete;
	}


	@Override
	public void onItemSelected(AdapterView<?> list, View arg1, int posisao, long id) 
	{
		this.posisaoSelecionada = posisao;
		this.descricaoItem = listaCombo.get(posisaoSelecionada).getValue();
		this.idItem = Integer.valueOf(listaCombo.get(posisaoSelecionada).getId());
	}
	
	public int getidItem()
	{
		return this.idItem;
	}
	
	
	public String getIdMatricula() {
		return idMatricula;
	}


	public TextView getTextViewNumCarta() {
		return textViewNumCarta;
	}


	public void setTextViewNumCarta(TextView textViewNumCarta) {
		this.textViewNumCarta = textViewNumCarta;
	}


	public String getNumCarta() {
		return numCarta;
	}


	public String getNomeCondutor()
	{
		Log.i("APP", "NOME DO CONDUTO = "+nomeCondutor);
		return nomeCondutor;
	
	}


	public void setNomeCondutor(String nomeCondutor) {
		this.nomeCondutor = nomeCondutor;
		if(textField != null)
			this.textField.setText(nomeCondutor);
	}


	public void setNumCarta(String numCarta) {
		this.numCarta = numCarta;
		if(textField != null)
			this.textField.setText(numCarta);
	}


	public String getNumMatricula()
	{
		return numMatricula;
	}


	public void setNumMatricula(String numMatricula) 
	{
		this.numMatricula = numMatricula;
		if(textField != null)
			this.textField.setText(numMatricula);
	}


	public boolean isIncompatilidade() {
		return incompatilidade;
	}


	public void setIncompatilidade(boolean incompatilidade) {
		this.incompatilidade = incompatilidade;
		if(this.checkIncompatibilidade != null)
			this.checkIncompatibilidade.setChecked(false);
	}


	public void setLivrete(boolean livrete) {
		this.livrete = livrete;
		if(this.checkLivrete != null)
			this.checkLivrete.setChecked(false);
	}


	public TextView getTextField() {
		return textField;
	}


	public void setTextField(TextView textField) {
		this.textField = textField;
	}


	public TextView getTextNomeCondutor() {
		return textNomeCondutor;
	}


	public void setTextNomeCondutor(TextView textNomeCondutor) {
		this.textNomeCondutor = textNomeCondutor;
	}


	public String getDescricaoItem()
	{
		return this.descricaoItem;
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> list) 
	{
	}


	public boolean isComplet() 
	{
		String text = (this.typeGroup != 1 && textField != null)? this.textField.getText().toString(): "";
		return (typeGroup == 1 && this.posisaoSelecionada != 0)
				||(this.typeGroup == 2 && text.length()>0)
				||(this.typeGroup == 3 && text.length()>0 && this.posisaoSelecionada>0);
	}

	@Override
	public void onClick(View clique) 
	{
		if(clique.equals(iconeInfoMatricula))
			onClickListener.treat(iconeInfoMatricula, EventsCod.CLICK_INFO_MATRICULA, null);
		else if(clique.equals(iconeInfoCarta))
			onClickListener.treat(iconeInfoCarta, EventsCod.CLICK_INFO_CARTA, null);
		else
		{
			Log.i("APP", "clique: "+clique);
			if(this.typeGroup == 2)
				onClickListener.treat(textField, EventsCod.CLICK_CARTA, null);
			else if(this.typeGroup == 3)
				onClickListener.treat(textField, EventsCod.CLICK_MATRICULA, null);
			else if(this.typeGroup == 1)
				onClickListener.treat(this.textField, EventsCod.CLICK_NOME_CONDUTOR, null);
		}
	}
	
	/**
	 * Gravidade da carta ou matricula
	 * @param gravidade
	 * @param tipo
	 */
	public static void gravidade(String gravidade, String tipo)
	{
		if(gravidade != null)
		{
			if(tipo.equals("matricula"))
			{
				if(gravidade.equals("0"))
					iconeInfoMatricula.findViewById(R.id.iconeInfoCartaMatricula).setBackgroundResource(R.drawable.icon_information_cinza);
				else if(gravidade.equals("1"))
					iconeInfoMatricula.findViewById(R.id.iconeInfoCartaMatricula).setBackgroundResource(R.drawable.icon_information_blue);
				else if(gravidade.equals("2"))
					iconeInfoMatricula.findViewById(R.id.iconeInfoCartaMatricula).setBackgroundResource(R.drawable.icon_informacao_yellow);
				else iconeInfoMatricula.findViewById(R.id.iconeInfoCartaMatricula).setBackgroundResource(R.drawable.icon_informacao_read);
			}
			else
			{
				if(gravidade.equals("0"))
					iconeInfoCarta.findViewById(R.id.iconeInfoCartaMatricula).setBackgroundResource(R.drawable.icon_information_cinza);
				else if(gravidade.equals("1"))
					iconeInfoCarta.findViewById(R.id.iconeInfoCartaMatricula).setBackgroundResource(R.drawable.icon_information_blue);
				else if(gravidade.equals("2"))
					iconeInfoCarta.findViewById(R.id.iconeInfoCartaMatricula).setBackgroundResource(R.drawable.icon_informacao_yellow);
				else iconeInfoCarta.findViewById(R.id.iconeInfoCartaMatricula).setBackgroundResource(R.drawable.icon_informacao_read);
			}
		}
	}

	public boolean isExistencia() {
		return existencia;
	}


	public void setExistencia(boolean existencia) {
		this.existencia = existencia;
		if(this.existenciaCarta != null)
			this.existenciaCarta.setChecked(false);
	}


	public CheckBox getCheckIncompatibilidade() {
		return checkIncompatibilidade;
	}


	public boolean isLivrete() {
		return livrete;
	}


	public void setCheckIncompatibilidade(CheckBox checkIncompatibilidade) {
		this.checkIncompatibilidade = checkIncompatibilidade;
	}


	public CheckBox getExistenciaCarta() {
		return existenciaCarta;
	}


	public void setExistenciaCarta(CheckBox existenciaCarta) {
		this.existenciaCarta = existenciaCarta;
	}


	@Override
	public void onCheckedChanged(CompoundButton view, boolean newState) 
	{
		if(view.equals(existenciaCarta))
		{
			this.existencia = newState;
			if(this.existencia == false)
			{
				this.checkIncompatibilidade.setChecked(false);
				this.textField.setText("");
				this.textField.setEnabled(false);
			}
			else
			{
				this.textField.setEnabled(true);
				this.textField.setText(numCarta);
			}
	
		}
		else if(view.equals(checkIncompatibilidade))
		{
			if(this.existencia== true)
				this.incompatilidade = newState;
			else this.checkIncompatibilidade.setChecked(false);
		}
		else if(view.equals(checkLivrete))
		{
			this.livrete = newState;
		}
	}
}
