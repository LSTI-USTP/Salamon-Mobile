package policia.transito.data;

import java.util.ArrayList;
import java.util.HashMap;

import policia.transito.model.Condutor;
import policia.transito.model.DualValues;
import policia.transito.model.Infracao;
import policia.transito.model.Infracao.ModoCoima;
import policia.transito.model.Infracao.NivelGravidade;
import policia.transito.model.Infracao.TipoInfracao;
import policia.transito.model.Veiculo;
import socket.modelo.Transfer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BaseInfracao extends GeralBase implements DefaultDatas
{	
	public static final int DB_VERSION=3;
	public BaseInfracao(Context context)
	{
		super(context, T_INFRACAO,DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		createTableInfracao(db);
		createTableDriverState(db);
		createTableCarCategory(db);
		createTableInfracaoSelecionada(db);
	}
	

	private void createTableInfracao(SQLiteDatabase db) 
	{
		Log.i("APP", "CRIANDO A TABELA INFRACAO...");
		String sql  = "CREATE TABLE "+T_INFRACAO+"( "+INFRACAO_ID+" INTEGER PRIMARY KEY NOT NULL," +
				""+INFRACAO_ARTIGO +" TEXT,"
				+INFRACAO_CATEGORIA+" TEXT,"
				+INFRACAO_DESCRICAO+" TEXT,"
				+INFRACAO_LINHA+" TEXT,"
				+INFRACAO_MODO_COIMA+" TEXT,"
				+INFRACAO_NOME+" TEXT,"
				+INFRACAO_TIPO+" TEXT)";
		
		db.execSQL(sql);
		
		Log.i("APP", "TABELA INFRACAO CRIADA.");		
	}
	
	private void createTableInfracaoSelecionada(SQLiteDatabase db)
	{
		Log.i("APP", "A criar entidade de infrações selecionadas");
		String sql ="CREATE TABLE "+T_INFRACAO_SELECIONADA+"( "+INFRACAO_SELECIONADA_ID+" INTEGER PRIMARY KEY NOT NULL, "+
				""+INFRACAO_SELECIONADA_GRAVIDADE+" INTEGER NOT NULL)";
		db.execSQL(sql);
		Log.i("APP", "Entidade de infrações selecionadas criada");
	}
	
	private void dropTableInfracoesSelecionadas(SQLiteDatabase database)
	{
		String sql ="DROP TABLE "+T_INFRACAO_SELECIONADA+";";
		database.execSQL(sql);
	}
	private void createTableCarCategory(SQLiteDatabase database)
	{
		Log.i("APP", "A CRIAR A TABELA CATEGÓRIA DE VEICULO");
		String sql ="CREATE TABLE "+T_CATEGORIA_VEICULO+"( "+CATEGORIA_VEICULO_ID+" INTEGER PRIMARY KEY NOT NULL, "+
		""+CATEORIA_VEICULO_DESC +" TEXT NOT NULL)";
		database.execSQL(sql);
	}
	
	private void dropTableCarCategory(SQLiteDatabase database)
	{
		String sql ="DROP TABLE "+T_CATEGORIA_VEICULO+";";
		database.execSQL(sql);
	}
	/**
	 * CRIA A ENTIDADE ESTADO DE CONDUTOR
	 * @param database
	 */
	private void createTableDriverState(SQLiteDatabase database)
	{
		Log.i("APP","A criar tabela estado de condutor");
		String sql ="CREATE TABLE "+T_DRIVER_STATE+"( "+DRIVER_STATE_ID+" INTEGER PRIMARY KEY NOT NULL, "+
				""+DRIVER_STATE_DESC +" TEXT NOT NULL)";
		database.execSQL(sql);
	}
	
	private void createTableAgent(SQLiteDatabase database)
	{
		Log.i("APP", "A criar a entidade agente");
		
	}
	
	private void dropTableDriverState(SQLiteDatabase database)
	{
		String sql ="DROP TABLE "+T_DRIVER_STATE+";";
		database.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int newVersion, int oldVersion)
	{
		this.dataBase.delete(T_INFRACAO, null, null);
		this.dataBase.delete(T_DRIVER_STATE, null, null);
		this.dataBase.delete(T_CATEGORIA_VEICULO, null,null);
		this.dataBase.delete(T_INFRACAO_SELECIONADA, null, null);
		onCreate(db);
	}

	private void dropTableInfracao(SQLiteDatabase db) {
		String sql = "DROP TABLE "+T_INFRACAO+"; ";
		db.execSQL(sql);
	}
	
	/**
	 * Preencher dados das
	 * @param recivedData
	 */
	public void atualizarInfracoes(Transfer recivedData)
	{
		this.dropTableInfracao(dataBase);
		this.createTableInfracao(dataBase);
		
		this.dataBase.delete(T_INFRACAO, null, null);
		for (HashMap<String, String> map: recivedData.getListMaps())
		{
			ContentValues values = new ContentValues();
			values.put(INFRACAO_ARTIGO, map.get("ARTICLE"));
			values.put(INFRACAO_CATEGORIA, map.get("CATEGORY"));
			values.put(INFRACAO_DESCRICAO, map.get("OTHER INFRACAO"));
			values.put(INFRACAO_ID, map.get("ID"));
			values.put(INFRACAO_LINHA, map.get("LINE"));
			
			ModoCoima modoCoima = (map.get("ID MODO COIMA").endsWith("1"))? ModoCoima.DEFAULT
					:(map.get("ID MODO COIMA").endsWith("2"))? ModoCoima.FREQUENTE
							:ModoCoima.GRAVITARIO;
			
			values.put(INFRACAO_MODO_COIMA, modoCoima.name());
			values.put(INFRACAO_NOME, map.get("NAME"));
			values.put(INFRACAO_TIPO, map.get("TYPE INFRACAO"));
			
			dataBase.insert(T_INFRACAO, null, values);
		}
	}
	
	public void guardarInfracoesSelecionadas(ArrayList<Infracao> infracoes)
	{
		Log.i("APP", "Guardando infrações selecionadas...");
		this.dropTableInfracoesSelecionadas(dataBase);
		this.createTableInfracaoSelecionada(dataBase);
		this.dataBase.delete(T_INFRACAO_SELECIONADA, null, null);
		if(infracoes != null &&  infracoes.size()>0)
		{
			for(Infracao infracao: infracoes)
			{
				ContentValues values = new ContentValues();
				values.put(INFRACAO_SELECIONADA_ID, infracao.getId());
				values.put(INFRACAO_SELECIONADA_GRAVIDADE, infracao.getGravidade());
				dataBase.insert(T_INFRACAO_SELECIONADA, null, values);
			}	
			Log.i("APP", "Infrações guardadas");
		}
	}
	
	
	
	public ArrayList<Infracao> infracoesSelecionadas()
	{
		ArrayList<Infracao> arrayList = new ArrayList<Infracao>();
		Cursor cursor = dataBase.query(T_INFRACAO_SELECIONADA, new String[]{"*"}, null, null, null, null, null);
		if(cursor.getCount()>0)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			cursor.moveToFirst();
			do
			{
				BaseADIM.mapCurrentPosition(map, cursor);
				arrayList.add(new Infracao(map.get(INFRACAO_SELECIONADA_ID).toString(), map.get(INFRACAO_SELECIONADA_GRAVIDADE).toString()));
				
			}while(cursor.moveToNext());
		}
		return arrayList;
	}
	
	
	/**
	 * Insere dados na tabela estado de condutor em sql lite
	 * @param transfer
	 */
	public void loadDriverState(Transfer transfer)
	{
		this.dropTableDriverState(dataBase);
		this.createTableDriverState(dataBase);
		
		this.dataBase.delete(T_DRIVER_STATE, null, null);
		for(HashMap<String, String> map : transfer.getListMaps())
		{
			ContentValues values = new ContentValues();
			values.put(DRIVER_STATE_ID, map.get(CAMPO_ID_ESTADO_CONDUTOR));
			values.put(DRIVER_STATE_DESC, map.get(CAMPO_DESCRICAO_ESTADO_CONDUTOR));
			dataBase.insert(T_DRIVER_STATE, null, values);
		}	
	}
	/**
	 * Carrega todas as categorias de veiculo
	 * @param transfer
	 */
	public void carregarCategoriasVeiculo(Transfer transfer)
	{
		this.dropTableCarCategory(dataBase);
		this.createTableCarCategory(dataBase);
		
		this.dataBase.delete(T_CATEGORIA_VEICULO, null, null);
		for(HashMap<String, String> map :transfer.getListMaps())
		{
			ContentValues values = new ContentValues();
			values.put(CATEGORIA_VEICULO_ID, map.get(CAMPO_ID_CATEGORIA_VEICULO));
			values.put(CATEORIA_VEICULO_DESC, map.get(CAMPO_DESCRICAO_CATEGORIA_VEICULO));
			dataBase.insert(T_CATEGORIA_VEICULO, null, values);
		}
	}
	
	public ArrayList<Veiculo> getCategoriasVeiculo()
	{
		ArrayList<Veiculo> categorias = new ArrayList<Veiculo>();
		Cursor cursor = dataBase.query(T_CATEGORIA_VEICULO, new String[]{"*"}, null, null, null, null, null);
		if(cursor.getCount()>0)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			cursor.moveToFirst();
			do
			{
				BaseADIM.mapCurrentPosition(map, cursor);
				Veiculo veiculo = new Veiculo(map.get(CATEGORIA_VEICULO_ID).toString(), map.get(CATEORIA_VEICULO_DESC).toString(),
						null,null);
				categorias.add(veiculo);
				
			}while(cursor.moveToNext());		
		}
		return categorias;
	}
	

	
	/**
	 * Carregar a lista de todas as categorias de todas as infracoe
	 * @return
	 */
	public ArrayList<String> getCategoriasInfracao()
	{
		ArrayList<String> listCategoria= new ArrayList<String>();
		Cursor cursor = dataBase.query(T_INFRACAO, new String[]{"DISTINCT "+INFRACAO_CATEGORIA}, null, null, null, null, INFRACAO_CATEGORIA+" ASC");
		if(cursor.getCount()>0)
		{
			cursor.moveToFirst();
			do
			{
				listCategoria.add(cursor.getString(0));
			}while(cursor.moveToNext());
		}
		return listCategoria;
	}
	
	
	public ArrayList<Infracao> getInfracaoCategoria(String categoriaName)
	{
		ArrayList<Infracao> listaInfracao = new ArrayList<Infracao>();
		String where [] = {categoriaName};
		Cursor cursor = dataBase.query(T_INFRACAO, new String[]{"*"}, " "+INFRACAO_CATEGORIA+" = ?", where, null, null, INFRACAO_CATEGORIA+" ASC, "+INFRACAO_NOME+" ASC");
		if(cursor.getCount()>0)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			cursor.moveToFirst();
			do
			{
				map.clear();
				BaseADIM.mapCurrentPosition(map, cursor);
				Infracao infracoa = prepareInfracao(map);
				
				listaInfracao.add(infracoa);
			}while(cursor.moveToNext());
		}
		return listaInfracao;
	}
	
	public ArrayList<String> getInfracaoNome()
	{
		ArrayList<String> infracao = new ArrayList<String>();
		Cursor cursor = dataBase.query(T_INFRACAO, new String[]{"*"}, null, null, null, null,null);
		if(cursor.getCount()>0)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			cursor.moveToFirst();
			do
			{
				map.clear();
				BaseADIM.mapCurrentPosition(map, cursor);
				infracao.add(map.get(INFRACAO_NOME).toString());
				
			}while(cursor.moveToNext());
		}
		return infracao;
	}
	
	
	public ArrayList<Condutor> getEstadosCondutores()
	{
		ArrayList<Condutor> listaEstadosCondutor = new ArrayList<Condutor>();
		Cursor cursor = dataBase.query(T_DRIVER_STATE, new String[]{"*"},null,null,null,null,null);
		if(cursor.getCount()>0)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			cursor.moveToFirst();
			do
			{
				map.clear();
				BaseADIM.mapCurrentPosition(map, cursor);
				Condutor condutor = new Condutor(map.get(DRIVER_STATE_ID).toString(), map.get(DRIVER_STATE_DESC).toString());
				listaEstadosCondutor.add(condutor);
			}while(cursor.moveToNext());
		}
		return listaEstadosCondutor;
	}
	/**
	 * Preparar a nova infacaao
	 * @param map
	 * @return
	 */
	private Infracao prepareInfracao(HashMap<String, Object> map) 
	{
		String name = map.get(INFRACAO_NOME).toString();
		String artigo = map.get(INFRACAO_ARTIGO).toString();
		String categoria = map.get(INFRACAO_CATEGORIA).toString();
		Object descrisao = map.get(INFRACAO_DESCRICAO);
		String id = map.get(INFRACAO_ID).toString();
		String linha = map.get(INFRACAO_LINHA).toString();
		String modoCoima = map.get(INFRACAO_MODO_COIMA).toString();
		String tipo = map.get(INFRACAO_TIPO).toString();
		
		Infracao infracoa = new Infracao(TipoInfracao.valueOf(tipo.toUpperCase()), ModoCoima.valueOf(modoCoima.toUpperCase()), name, artigo, categoria, linha, id, (descrisao != null)? descrisao.toString(): null);
		return infracoa;
	}
	
	/**
	 * Essa funcao serve para recaregar as infrações selecionada
	 * @param listSelect
	 * @return
	 */
	public ArrayList<Infracao> reLoadInfracaoSelecionada (ArrayList<DualValues<String, NivelGravidade>> listSelect)
	{
		ArrayList<Infracao> listInfracaoSelecionada = new ArrayList<Infracao>();
		Cursor cursor = dataBase.query(T_INFRACAO, new String[]{"*"}, null, null, null, null, INFRACAO_CATEGORIA+" ASC, "+INFRACAO_NOME+" ASC");
		if(cursor.getCount()>0)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			cursor.moveToFirst();
			do
			{
				map.clear();
				BaseADIM.mapCurrentPosition(map, cursor);
				for(int i =0; i<listSelect.size(); i++)
				{
					Log.i("APP", "LEFT VALUE = "+(listSelect.get(i).getLeftValue()));
					
					if(listSelect.get(i).getLeftValue().equals(map.get(INFRACAO_ID)))
					{
						Infracao infracao = prepareInfracao(map);
						
						infracao.setSelected(true);
						infracao.setNivelGravidade(listSelect.get(i).getRigthValue());
						listInfracaoSelecionada.add(infracao);
						listSelect.remove(i);
						break;
					}
					
				}
			}while(cursor.moveToNext());
		}
		return listInfracaoSelecionada;
	}

	public void carregarCategoriaVeiculo(Transfer transfer) 
	{
		this.dropTableCarCategory(dataBase);
		this.createTableCarCategory(dataBase);
		this.dataBase.delete(T_CATEGORIA_VEICULO, null, null);
		
		for(HashMap<String, String> map : transfer.getListMaps())
		{
			ContentValues values = new ContentValues();
			values.put(CATEGORIA_VEICULO_ID, map.get(CAMPO_ID_CATEGORIA_VEICULO));
			values.put(CATEORIA_VEICULO_DESC, map.get(CAMPO_DESCRICAO_CATEGORIA_VEICULO));
			dataBase.insert(T_CATEGORIA_VEICULO, null, values);
		}	
	}
	
	
	
	
}
