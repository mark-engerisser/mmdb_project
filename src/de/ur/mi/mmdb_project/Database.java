package de.ur.mi.mmdb_project;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Database {
	
	private DbOpenHelper m_OpenHelper;
	
	private SQLiteDatabase m_Db;
	
	private static final String TABLE_DRIVER = "_drivers";
	private static final String D_PERS_ID = "_persid";
	private static final String D_NAME = "_name";
	private static final String D_CUR_POS = "_curpos";
	
	private static final String TABLE_TASKS = "_tasks";
	private static final String T_TASK_ID = "_taskid";
	private static final String T_SRC_ADD = "_srcadd";
	private static final String T_DEST_ADD = "_dest_add";
	private static final String T_STATUS = "_status";
	private static final String T_DRIVER = "_driver";
	
	private static final String TABLE_POSITIONS = "_positions";
	private static final String P_ID = "_id";
	private static final String P_AVENUE = "_avenue";
	private static final String P_STREET = "_street";
	
	public Database(Context context) {
		m_OpenHelper = new DbOpenHelper(context, "mmdb_project_db", null, 1);
	}
	
	public void openDb() {
		try {
			m_Db = m_OpenHelper.getWritableDatabase();
		} catch (SQLException e) {
			m_Db = m_OpenHelper.getReadableDatabase();
		}
	}
	
	public void closeDb() {
		m_Db.close();
	}
	
	public void addDriver(String name, int pos) {
		ContentValues content = new ContentValues();
		content.put(D_NAME, name);
		content.put(D_CUR_POS, pos);
		
		m_Db.insert(TABLE_DRIVER, null, content);
	}
	
	public ArrayList<Driver> getAllDrivers() {
		ArrayList<Driver> drivers = new ArrayList<Driver>();
		
		Cursor cursor = m_Db.query(TABLE_DRIVER, null, null, null, null, null, null);
		
		if (cursor.moveToFirst()) {
			do {
				Driver driver = new Driver(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
				drivers.add(driver);
			} while (cursor.moveToNext());
		}
		
		return drivers;
	}
	
	class DbOpenHelper extends SQLiteOpenHelper {
		
		private static final String m_sTABLE_TASKS = 
				"create table " + TABLE_TASKS + " (" +
				T_TASK_ID + " integer primary key autoincrement, " +
				T_DRIVER + " integer, " +
				T_SRC_ADD + " integer, " +
				T_DEST_ADD + " integer, " +
				T_STATUS + " integer);";
		
		private static final String m_sTABLE_DRIVER = 
				"create table " + TABLE_DRIVER + " (" +
				D_PERS_ID + " integer primary key autoincrement, " +
				D_NAME + " text not null, " +
				D_CUR_POS + " integer);";
		
		private static final String m_sTABLE_POSITIONS = 
				"create table " + TABLE_POSITIONS + " (" +
				P_ID + " integer primary key autoincrement, " +
				P_AVENUE + " integer not null, " +
				P_STREET + " integer not null);";
		

		public DbOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(m_sTABLE_DRIVER);
			db.execSQL(m_sTABLE_TASKS);
			db.execSQL(m_sTABLE_POSITIONS);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
