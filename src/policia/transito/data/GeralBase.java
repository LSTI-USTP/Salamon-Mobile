package policia.transito.data;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class GeralBase extends SQLiteOpenHelper implements DBObject
{
	protected SQLiteDatabase dataBase;
	private Context context;
	private String tableName;
   
	
	public GeralBase(Context context, String tableName, int dbVersion) 
	{
		super(context, DB_NAME+"."+tableName, null, dbVersion);
		this.dataBase = this.getWritableDatabase();
		this.tableName = tableName;
		this.context = context;
		
	}
	
	
	public void updateDataAdmin(String tableName, ContentValues local, String where, String[] whereAras) 
	{
		this.dataBase.update(tableName, local, where, whereAras);
	}
	
	/**
	 * Carregar as informacoe de uma tabela a tratalas em tempo oportuno
	 * @param tableName
	 * @param cod
	 * @param mapaSelect
	 * @param columns
	 */
	public void onSelectAll(String tableName, int cod, TableCursor mapaSelect, String ... columns)
	{
		Cursor cursor = this.dataBase.query(tableName, columns, null, null, null, null, null);
		if(cursor.getCount()>0)
		{
			boolean result = false;
			cursor.moveToFirst();
			do
			{
				HashMap<String, Object> map = new HashMap<String, Object>();
				mapCurrentPosition(map, cursor);
				result = mapaSelect.onNextRow(map, cod);
			}while(cursor.moveToNext() && result);
		}
	}
	
	/**
	 * Carregar todas as informações de uma tabela
	 * @param tableName
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getTable(String tableName)
	{
		final ArrayList<HashMap<String, Object>> listData = new ArrayList<HashMap<String,Object>>();
		onSelectAll(tableName, 0, new TableCursor() {
			
			@Override
			public boolean onNextRow(HashMap<String, Object> mapData, int codSelection)
			{
				if(codSelection == 0)listData.add(mapData);
				return codSelection == 0;
			}
		}, "*");
		return listData;
	}
	
	@SuppressLint("NewApi") public static  void mapCurrentPosition(HashMap<String, Object> map, Cursor cursor)
	{
		map.clear();
		for(int i =0; i<cursor.getColumnCount(); i++)
		{
			switch(cursor.getType(i))
			{
				case Cursor.FIELD_TYPE_BLOB:
					map.put(cursor.getColumnName(i), cursor.getBlob(i));
					break;
				case Cursor.FIELD_TYPE_FLOAT:
					map.put(cursor.getColumnName(i), cursor.getFloat(i));
				case Cursor.FIELD_TYPE_INTEGER:
					map.put(cursor.getColumnName(i), cursor.getInt(i));
				case Cursor.FIELD_TYPE_NULL:
					map.put(cursor.getColumnName(i), null);
				case Cursor.FIELD_TYPE_STRING: 
					map.put(cursor.getColumnName(i), cursor.getString(i));
				default:
					map.put(cursor.getColumnName(i), cursor.getString(i));
			}
		}
	}
	
	
	protected void createTableSequence()
	{
		String sql = "CREATE TABLE "+T_SEQUENCIA+"( "+SEQUENCIA_CODIGO+" VARCHAR(50) PRIMARY KEY NOT NULL "
				+SEQUENCIA_NUMERO + " INTEGER "+
				")";
		this.dataBase.execSQL(sql);
	}
	
	

	/**
	 * Criar uma ArrayList Mapeado a partir dos dados de uma entidade
	 * @param tableName
	 * @param db
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> tableAsArrayMap(String tableName, SQLiteDatabase db)
	{
		
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		String columns [] = {"*"};
		
		try
		{
			Cursor cursor = db.query(tableName, columns, null, null, null, null, null);
			
			if(cursor.getCount()>0)
			{
				cursor.moveToFirst();
				do
				{
					HashMap<String, Object> map = new HashMap<String, Object>();
					mapCurrentPosition(map, cursor);
				}while(cursor.moveToNext());
			}
		}catch (Exception ex) {
			Log.e("APP", ex.getMessage());
		}
		return list;
	}

}
