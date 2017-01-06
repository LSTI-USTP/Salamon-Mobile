package policia.transito.adapter;

import policia.transito.R;
import policia.transito.model.Infracao;
import policia.transito.model.Infracao.ModoCoima;
import policia.transito.model.Infracao.NivelGravidade;
import policia.transito.model.Infracao.TipoInfracao;
import policia.transito.model.ItemList;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ItemInfracao implements ItemList
{
    private Infracao infracao;
	private ItemCategoriaInfracao group;
	public ItemInfracao(Infracao infracao)
	{
		this.infracao = infracao;
	}
	
	public Infracao getInfracao() 
	{
		return this.infracao;
	}

	
	@Override
	public View prepareView(LayoutInflater inflater, Context appContext, ViewGroup viewGroup, View enterView, int indexPosition) 
	{
		  View view = null;
		  view = inflater.inflate(R.layout.items_dinamics, null);
		  final RatingBar ratingBar =(RatingBar)view.findViewById(R.id.itemInfracaoNivelGravidade);
		  final View verInfracao = view.findViewById(R.id.itemInfracao);
		  final ImageView iconeInfracao=(ImageView) view.findViewById(R.id.itemInfracaoIcon);
		  verInfracao.setVisibility(View.VISIBLE);// apresenta a lista de infrações
		 
		  if(infracao.isSelected()) // se a infração for selecionada 
		  {
				verInfracao.setBackgroundResource(R.color.corItemSelecionado);	 
				if(infracao.getModoCoima()==ModoCoima.GRAVITARIO)
				{
					ratingBar.setVisibility(View.VISIBLE);
					if(infracao.getNivelGravidade()==NivelGravidade.LEVE)
						ratingBar.setProgress(1);
					else if(infracao.getNivelGravidade()==NivelGravidade.GRAVE)
						ratingBar.setProgress(2);
					else if(infracao.getNivelGravidade() == NivelGravidade.MUITO_GRAVE)
						ratingBar.setProgress(3);
					else ratingBar.setProgress(0);
				}
				else
				{
					ratingBar.setVisibility(View.GONE);
					ratingBar.setProgress(0);
					infracao.setNivelGravidade(null);
				}
		  }
		  else
		  {
			  verInfracao.setBackgroundResource(R.drawable.background_body_goup);
			  if(infracao.getModoCoima()==ModoCoima.GRAVITARIO)
			  {
				  ratingBar.setVisibility(View.VISIBLE);
				  ratingBar.setProgress(0);
				  this.infracao.setNivelGravidade(null);
			  }
			  else
			  {
				  ratingBar.setVisibility(View.GONE);
				  ratingBar.setProgress(0);
				  this.infracao.setNivelGravidade(null);
			  }
		  }
			   
		  if(infracao.getTipoInfracacao() == TipoInfracao.CARTA) 
				 iconeInfracao.setBackgroundResource(R.drawable.icon_condutor);
		  
		  
		  // quando for clicado no item
		  OnClickListener clickListener = new OnClickListener()
		  { // CLIQUE NO RATING BAR
			
			@Override
			public void onClick(View v)
			{
				select(!infracao.isSelected());
				Log.i("APP", "Clicou: "+infracao);
				infracao.setNivelGravidade(null);
	    	    ratingBar.setProgress(0);
				if(infracao.getModoCoima()==ModoCoima.GRAVITARIO)
				{
					select(false);
					verInfracao.setBackgroundResource(R.drawable.background_body_goup);
				}
				else if(infracao.isSelected())
				{
					verInfracao.setBackgroundResource(R.color.corItemSelecionado);
				}
				else 
				{
					verInfracao.setBackgroundResource(R.drawable.background_body_goup);
				}
			}
		};
	
		
		//quando for clicado nas estrelas 
		verInfracao.setOnClickListener(clickListener);
		ratingBar.setOnTouchListener(new OnTouchListener() 
		{	
			@Override//clique no rating bar
			public boolean onTouch(View arg0, MotionEvent arg1)
			{
			    if(arg1.getAction()== MotionEvent.ACTION_UP)
			    {
			    	Log.i("APP","Progresso "+ratingBar.getSecondaryProgress());
			 
			    	switch(ratingBar.getProgress())
					{
						case 1:
							select(true);
							verInfracao.setBackgroundResource(R.color.corItemSelecionado);
							infracao.setNivelGravidade(NivelGravidade.LEVE);
							Log.i("APP","Selecionado nivel de gravidade leve ");
				    		return true;
						case 2:
							select(true);
							verInfracao.setBackgroundResource(R.color.corItemSelecionado);
							infracao.setNivelGravidade(NivelGravidade.GRAVE);
				    		Log.i("APP","Selecionado nivel de gravidade grave ");
			    			return true;
						case 3:
							select(true);
							verInfracao.setBackgroundResource(R.color.corItemSelecionado);
							infracao.setNivelGravidade(NivelGravidade.MUITO_GRAVE);
							Log.i("APP","Selecionado nivel de gravidade muito grave ");
				    		return true;
					}
			    }
				return false;
			}
		});
		  TextView textViewNomeGrupo = (TextView) view.findViewById(R.id.tvItemInfracao);
		  textViewNomeGrupo.setText(infracao.getNome());
		return view;
	}

	protected void select(boolean select) 
	{
		
		if(select != this.infracao.isSelected())
		{
			if(select) 
				group.added();
			else group.removed();
		}
		infracao.setSelected(select);
	}

	public void setGroup(ItemCategoriaInfracao mascaraGrupo)
	{
		this.group = mascaraGrupo;
	}

	
}
