package policia.transito.data;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

@SuppressLint("NewApi") 
public class BaseADIM extends GeralBase implements  DefaultDatas
{
	public static int DB_VERSION = 2;
	private Context context;
	
	public BaseADIM(Context context) 
	{
		super(context, T_ADMIN,DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		try
		{
			Log.i("APP", "Criando a base de dados ADIMN...");
			String sql = "CREATE TABLE "+T_ADMIN+"("+ADM_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
		            +ADM_USERNAME+" TEXT NOT NULL,"
					+ADM_PWD+" TEXT NOT NULL,"
					+ADM_HOSTSERVER +" TEXT,"
					+ADM_CODREGISTER + " TEXT,"
					+ADM_DTREG +" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
					+ADM_DIVICENAME+" TEXT,"
					+ADM_STATE+" INTEGER NOT NULL);";
			db.execSQL(sql);
			ContentValues map = new ContentValues();
			map.put(ADM_USERNAME, ADM_DEFAULT_USERNAME);
			map.put(ADM_PWD, ADM_DEFAULT_PWD);
			map.put(ADM_STATE, ADM_DEFAULT_STATE);
			map.put(ADM_DIVICENAME, (String)null);
			long g = db.insert(T_ADMIN, null, map);
			Log.i("APP", "DEFAULTS DADAS REGISTRED COM "+g);
		}catch(Exception ex)
		{
			Log.e("APP", ex.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		Log.i("APP", "UPGRADANDO A BASE DE DADOS T_ADMIN");
		db.execSQL("DROP TABLE "+T_ADMIN+";");
		onCreate(db);
	}
	
	/**
	 * @param userName
	 * @param pwd
	 * @return
	 */
	public boolean login(String userName, String pwd)
	{
		ArrayList<HashMap<String, Object>> listData = getTable(T_ADMIN);
		for(HashMap<String, Object> map: listData)
		{
			if(map.get(ADM_USERNAME).equals(userName) && map.get(ADM_PWD).equals(pwd))
				return true;
		}
		
		return false;
	}
	
	
	/**
	 * Obter o IP Registrado para o servidor da aplicacao
	 * @param activity
	 * @return
	 */
	public static String getIp(Activity activity)
	{
		BaseADIM base = new BaseADIM(activity);
		ArrayList<HashMap<String, Object>> lista = base.getTable(T_ADMIN);
		if(lista.size()>0 && lista.get(0).get(ADM_HOSTSERVER) != null)
			return lista.get(0).get(ADM_HOSTSERVER).toString();
		else return null;
	}
	
	public static String getDeviceName(Activity activity)
	{
		BaseADIM base = new BaseADIM(activity);
		ArrayList<HashMap<String, Object>> lista = base.getTable(T_ADMIN);
		if(lista.size()>0 && lista.get(0).get(ADM_STATE).equals("1"))
			return lista.get(0).get(ADM_DIVICENAME).toString();
		else return null;
	}
	
	/**
	 * Devolve o ip do administrador
	 * @param activity
	 * @return
	 */
	public static String getADM_ID(Activity activity)
	{
		BaseADIM baseADIM = new BaseADIM(activity);
		ArrayList<HashMap<String, Object>> lista = baseADIM.getTable(T_ADMIN);
		if(lista.size()>0)
			return lista.get(0).get(ADM_ID).toString();
     	else return null;
	}
	
	
}
