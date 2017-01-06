package policia.transito.data;

import java.util.ArrayList;
import java.util.HashMap;

import policia.transito.model.Agente;
import policia.transito.model.Fiscalizacao;
import policia.transito.model.Localizacao;
import socket.modelo.Transfer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BaseFiscalizacao extends GeralBase implements DefaultDatas
{
	private static final int DB_VERSION=1;
	public BaseFiscalizacao(Context context) {
		super(context, T_FISCALIZACAO,DB_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Cria a tabela fiscalização
	 * @param database
	 */
	private void createTableFiscalizacao(SQLiteDatabase database)
	{
		String sql ="CREATE TABLE IF NOT EXISTS "+T_FISCALIZACAO+"(" +FISCA_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
				""+FISCA_NUMERO_CARTA +" VARCHAR(30),"
				+FISCA_NUMERO_MATRICULA+" VARCHAR(30),"
				+FISCA_LIVRETE +" INTEGER,"
				+FISCA_INCOMPATIBILIDADE_CARTA +" INTEGER,"
				+FISCA_ESTADO_CONDUTOR+" INTEGER,"
				+FISCA_NOME_CONDUTOR+" VARCHAR(250),"
				+FISCA_CATEGORIA_VEICULO+" INTEGER,"
				+FISCA_DESC_ESTADO_CONDUTOR+" VARCHAR(250),"
				+FISCA_DESC_CATEGORIA_VEICULO+" VARCHAR(250),"
				+FISCA_EXISTENCIA_CARTA+" INTEGER,"
				+FISCA_ESTADO+" INTEGER,"
				+FISCA_LATITUDE+" VARCHAR(250),"
				+FISCA_LONGITUDE+" VARCHAR(250),"
				+FISCA_LOCAL+" VARCHAR(250),"
				+FISCA_DATA_REGISTRO +" TIMESTAMP)";
		database.execSQL(sql);
	}
	
	private void createTableAgente(SQLiteDatabase database)
	{
		String sql ="CREATE TABLE "+T_AGENTE+"(" +AGENTE_ID +" INTEGER PRIMARY KEY NOT NULL,"+
		""+AGENTE_ID_LOGIN +" INTEGER,"
		+AGENTE_ID_ALOCACAO+" INTEGER,"
		+AGENTE_TIPO_OPERACAO+" VARCHAR(150),"
		+AGENTE_NIVEL_ATUACAO+" VARCHAR(150),"
		+AGENTE_NOME_ACESSO+" VARCHAR(150),"
		+AGENTE_NOME+" VARCHAR(150),"
		+AGENTE_APELIDO+" VARCHAR(150),"
		+AGENTE_ESTADO+" INTEGER,"
		+AGENTE_ID_TIPO_OPERACAO+" INTEGER,"
		+AGENTE_OPERACAO+" VARCHAR(150),"
		+AGENTE_AUTENTICACAO+" INTEGER NOT NULL)";
		database.execSQL(sql);
	}
	
	

	private void dropTableAgent(SQLiteDatabase database)
	{
		String sql ="DROP TABLE "+T_AGENTE+";";
		database.execSQL(sql);
	}
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		createTableFiscalizacao(db);
		createTableAgente(db);
		criarTabelaRegistoFiscalizacao(db);
	}
	
	/**
	 * Registra a fiscalização na base de dados sqllite com estado da fiscalização 2
	 */
	public void regFiscalizacao(Fiscalizacao fiscalizacao)
	{		
		Log.i("APP", "A guarda em sqlite dados de fisscalização");
		if(fiscalizacao.getNomeCondutor() == null)
		{
			Log.i("APP", "NOME DO CONDUTOR ESTA NULO" );
			return;
		}
		this.createTableFiscalizacao(dataBase);	
		ContentValues valores = new ContentValues();
	    valores.put(FISCA_NUMERO_CARTA, fiscalizacao.getNumCarta());
	    valores.put(FISCA_NUMERO_MATRICULA, fiscalizacao.getNumMatricula());
	    valores.put(FISCA_LIVRETE, fiscalizacao.getLivrete());
	    valores.put(FISCA_INCOMPATIBILIDADE_CARTA, fiscalizacao.getIncompatibilidadeCarta());
	    valores.put(FISCA_ESTADO_CONDUTOR, fiscalizacao.getEstadoCondutor());
	    valores.put(FISCA_NOME_CONDUTOR, fiscalizacao.getNomeCondutor().toString());
	    valores.put(FISCA_CATEGORIA_VEICULO, fiscalizacao.getCategoriaVeiculo());
	    valores.put(FISCA_DESC_ESTADO_CONDUTOR, fiscalizacao.getDescricaoEstadoCondutor());
	    valores.put(FISCA_DESC_CATEGORIA_VEICULO, fiscalizacao.getDescricaoCategoriaVeiculo());
	    valores.put(FISCA_EXISTENCIA_CARTA, fiscalizacao.getExistenciaCarta());
		valores.put(FISCA_ESTADO, 2);
		dataBase.insert(T_FISCALIZACAO, null, valores);
	    Log.i("APP", "Fiscalização registrada na base de dados SQLLITE");
	}
	
	public void registrarFiscalizacaoServidor()
	{
		
	}
	
	/**
	 * Autenticação de agente: 1 autenticado, 0 não autenticado
	 * @param transfer
	 * @param autenticacao
	 */
	public void guardarDadosAgente(Transfer transfer, int autenticacao)
	{
		Log.i("APP", "A guardar dados do utilizador...");
		this.dropTableAgent(dataBase);
		this.createTableAgente(dataBase);
		this.dataBase.delete(T_AGENTE, null, null);
		 for(HashMap<String, String> map: transfer.getListMaps())
		 {
			 ContentValues values = new ContentValues();
			 values.put(AGENTE_ID, map.get("ID USER"));
			 values.put(AGENTE_ID_LOGIN, map.get("ID LOGIN"));
			 values.put(AGENTE_ID_ALOCACAO, map.get("ID ALOCACAO"));
			 values.put(AGENTE_TIPO_OPERACAO, map.get("TIPO OPERACAO"));
			 values.put(AGENTE_NIVEL_ATUACAO, map.get("NIVEL ATUACAO"));
			 values.put(AGENTE_NOME_ACESSO, map.get("ACCESS NAME"));
			 values.put(AGENTE_NOME, map.get("NAME"));
			 values.put(AGENTE_APELIDO, map.get("APELIDO"));
			 values.put(AGENTE_ESTADO, map.get("STATE"));
			 values.put(AGENTE_ID_TIPO_OPERACAO, map.get("ID TIPO OPERACAO"));
			 values.put(AGENTE_OPERACAO, map.get("OPERACAO"));
			 values.put(AGENTE_AUTENTICACAO, autenticacao);
			 dataBase.insert(T_AGENTE, null, values);
		 }
		 Log.i("APP", "Dados do utilizador guardados");
	}
	/**
	 * Devolve informações da fiscalização registrada na base de dados sqllite
	 * @return
	 */
	public Fiscalizacao getFiscalizacao()
	{
		Fiscalizacao fiscalizacao = new Fiscalizacao();
		Cursor cursor = dataBase.query(T_FISCALIZACAO, new String[]{"*"}, null, null, null, null, null);
		if(cursor.getCount()>0)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			cursor.moveToFirst();
			do
			{
				BaseADIM.mapCurrentPosition(map, cursor);
				fiscalizacao.setId(Integer.valueOf(map.get(FISCA_ID).toString()));
				fiscalizacao.setEstadoCondutor(Integer.valueOf(map.get(FISCA_ESTADO_CONDUTOR).toString()));
				fiscalizacao.setDescricaoEstadoCondutor(map.get(FISCA_DESC_ESTADO_CONDUTOR).toString());
				fiscalizacao.setExistenciaCarta(Integer.valueOf(map.get(FISCA_EXISTENCIA_CARTA).toString()));
				fiscalizacao.setIncompatibilidadeCarta(Integer.valueOf(map.get(FISCA_INCOMPATIBILIDADE_CARTA).toString()));
				fiscalizacao.setNumCarta(map.get(FISCA_NUMERO_CARTA).toString());
				fiscalizacao.setCategoriaVeiculo(Integer.valueOf(map.get(FISCA_CATEGORIA_VEICULO).toString()));
				fiscalizacao.setDescricaoCategoriaVeiculo(map.get(FISCA_DESC_CATEGORIA_VEICULO).toString());
				fiscalizacao.setLivrete(Integer.valueOf(map.get(FISCA_LIVRETE).toString()));
				fiscalizacao.setNumMatricula(map.get(FISCA_NUMERO_MATRICULA).toString());
				fiscalizacao.setNomeCondutor(map.get(FISCA_NOME_CONDUTOR).toString());
				
			}while(cursor.moveToNext());		
		}
		return fiscalizacao;
	}
	
	/**
	 * Devolve a lista de infrações 
	 * @return
	 */
	public ArrayList<Fiscalizacao> listaFiscalizacao()
	{
		ArrayList<Fiscalizacao> listaInfracao = new ArrayList<Fiscalizacao>();
		Cursor cursor = dataBase.query(T_FISCALIZACAO, new String[]{"*"}, null, null, null, null, null);
		if(cursor.getCount()>0)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			cursor.moveToFirst();
			do
			{
				Fiscalizacao fiscalizacao = new Fiscalizacao();
				BaseADIM.mapCurrentPosition(map, cursor);
				fiscalizacao.setId(Integer.valueOf(map.get(FISCA_ID).toString()));
				fiscalizacao.setEstadoCondutor(Integer.valueOf(map.get(FISCA_ESTADO_CONDUTOR).toString()));
				fiscalizacao.setDescricaoEstadoCondutor(map.get(FISCA_DESC_ESTADO_CONDUTOR).toString());
				fiscalizacao.setExistenciaCarta(Integer.valueOf(map.get(FISCA_EXISTENCIA_CARTA).toString()));
				fiscalizacao.setIncompatibilidadeCarta(Integer.valueOf(map.get(FISCA_INCOMPATIBILIDADE_CARTA).toString()));
				fiscalizacao.setNumCarta(map.get(FISCA_NUMERO_CARTA).toString());
				fiscalizacao.setCategoriaVeiculo(Integer.valueOf(map.get(FISCA_CATEGORIA_VEICULO).toString()));
				fiscalizacao.setDescricaoCategoriaVeiculo(map.get(FISCA_DESC_CATEGORIA_VEICULO).toString());
				fiscalizacao.setLivrete(Integer.valueOf(map.get(FISCA_LIVRETE).toString()));
				fiscalizacao.setNumMatricula(map.get(FISCA_NUMERO_MATRICULA).toString());	
				fiscalizacao.setNomeCondutor(map.get(FISCA_NOME_CONDUTOR).toString());
				listaInfracao.add(fiscalizacao);
				
			}while(cursor.moveToNext());		
		}
		return listaInfracao;
	}
	
	public int getUltimoIdFiscalizacao()
	{
		int lastId =0;
		Cursor cursor = dataBase.query(T_FISCALIZACAO, new String[]{"*"}, null, null, null, null, null);
		if(cursor.getCount()>0)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			cursor.moveToFirst();
			do
			{
				BaseADIM.mapCurrentPosition(map, cursor);
				lastId = Integer.valueOf(map.get(FISCA_ID).toString());
			}while(cursor.moveToNext());		
		}
		return lastId;
	}
	
	public void atualizarDadosFiscalizacao(Fiscalizacao fiscalizacao)
	{
		
	}
	
	/**
	 * Registra a fiscalização na base de dados sql lite quando não há conexão com 
	 * o servidor da aplicação
	 * @param map
	 */
	public void registrarFiscalizacaoSqlLite(Transfer transfer)
	{
		Log.i("APP", "A registrar fiscalizacão em SQLlITE");
		this.criarTabelaRegistoFiscalizacao(dataBase);
		if(transfer.getListMaps().size()>0)
		{
			for(HashMap<String, String> map : transfer.getListMaps())
			{
				ContentValues values = new ContentValues();	
				values.put(FISCALIZACAO_ID, map.get(ID_FISCALIZACAO));
				values.put(FISCALIZACAO_GRAVIDADE, map.get("INFRACAO.GRAVIDADE"));
				values.put(FISCALIZACAO_INFRACAO_ID, map.get("INFRACAO.ID"));
				values.put(FISCALIZACAO_REGISTRO_ESTADO, 1);
				dataBase.insert(T_REGISTO_FISCALIZACAO, null, values);
			}
			Log.i("APP", "Fiscalizacão registada em SQLlITE");
		}
	}

	
	/**
	 * Criar a entidade para guardar os dados da fiscalização quando não houver conexão com 
	 * o servidor
	 */
	private void criarTabelaRegistoFiscalizacao(SQLiteDatabase database)
	{

		String sql ="CREATE TABLE IF NOT EXISTS "+T_REGISTO_FISCALIZACAO+"(" +ID_FISCA +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
				""+FISCALIZACAO_INFRACAO_ID +" INTEGER NOT NULL,"
				+FISCALIZACAO_GRAVIDADE+" VARCHAR(100),"
				+FISCALIZACAO_ID +" INTEGER NOT NULL,"
				+FISCALIZACAO_REGISTRO_ESTADO+" INTEGER NOT NULL,"
				+FISCA_DATA_REGISTRO +" TIMESTAMP)";
		database.execSQL(sql);
	}
	
	public Agente getAgente()
	{
		Agente agente = new Agente();
		Cursor cursor = dataBase.query(T_AGENTE, new String[]{"*"}, null, null, null, null, null);
		if(cursor.getCount()>0)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			cursor.moveToFirst();
			do
			{
				BaseADIM.mapCurrentPosition(map, cursor);
				if(map.get(AGENTE_NOME)== null)
					agente.setCodigoAcesso(map.get(AGENTE_NOME_ACESSO).toString());
				else
				{
					agente.setNome(map.get(AGENTE_NOME).toString());
					agente.setApelido(map.get(AGENTE_APELIDO).toString());
					agente.setId(Integer.valueOf(map.get(AGENTE_ID).toString()));
					agente.setIdLogin(Integer.valueOf(map.get(AGENTE_ID_LOGIN).toString()));
					agente.setEstado(map.get(AGENTE_ESTADO).toString());
					agente.setCodigoAcesso(map.get(AGENTE_NOME_ACESSO).toString());
					agente.setIdAlocacao(Integer.valueOf(map.get(AGENTE_ID_ALOCACAO).toString()));
					agente.setNivelAtuacao(map.get(AGENTE_NIVEL_ATUACAO).toString());	
					agente.setTipoOperacao(map.get(AGENTE_TIPO_OPERACAO).toString());
					agente.setIdTipoOperacao(Integer.valueOf(map.get(AGENTE_ID_TIPO_OPERACAO).toString()));
					agente.setOperacao(map.get(AGENTE_OPERACAO).toString());
					agente.setAutenticacao(Integer.valueOf(map.get(AGENTE_AUTENTICACAO).toString()));
				}
			}while(cursor.moveToNext());
		}
		return agente;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		this.dataBase.delete(T_FISCALIZACAO, null, null);
		this.dataBase.delete(T_AGENTE, null, null);
		this.dataBase.delete(T_REGISTO_FISCALIZACAO, null, null);
		onCreate(db);	
	}
	
	public void alterarEstadoFiscalizacao(int estado)
	{
		String sql ="UPDATE "+T_FISCALIZACAO+" SET "+FISCA_ESTADO+"="+estado;
		dataBase.execSQL(sql);
	}

}
