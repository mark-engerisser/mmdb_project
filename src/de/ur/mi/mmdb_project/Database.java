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
	
	public void addTask(Task task) {
		ContentValues content = new ContentValues();
		content.put(T_SRC_ADD, task.m_Src);
		content.put(T_DEST_ADD, task.m_Dest);
		content.put(T_DRIVER, 0);
		content.put(T_STATUS, task.m_Status);
		
		m_Db.insert(TABLE_TASKS, null, content);
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
	
	public ArrayList<Task> getAllTasks() {
		ArrayList<Task> tasks = new ArrayList<Task>();
		
		Cursor cursor = m_Db.query(TABLE_TASKS, null, null, null, null, null, null);
		
		if (cursor.moveToFirst()) {
			do {
				Task task = new Task(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));
				tasks.add(task);
			} while (cursor.moveToNext());
		}
		
		return tasks;
	}
	
	public Driver getDriverForId(int id) {
		
		String condition = D_PERS_ID + "=?";
		
		Cursor cursor = m_Db.query(TABLE_DRIVER, null, condition, new String[] {String.valueOf(id)}, null, null, null);
		
		if (cursor.moveToFirst()) {
			Driver driver = new Driver(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
			return driver;
		}
		
		return null;
	}
	
	public Task getTaskForDriverId(int id) {
		
		String condition = T_DRIVER + "=?";
		
		Cursor cursor = m_Db.query(TABLE_TASKS, null, condition, new String[] {String.valueOf(id)}, null, null, null);
		
		if (cursor.moveToFirst()) {
			Task task = new Task(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));
			return task;
		}
		return null;
	}
	
	public Task getBestTaskForAddress(int addressId) {
		Address driverPosition = getAddressForId(addressId);
		
		String condition = T_STATUS + "=0";
		
		Cursor cursor = m_Db.query(TABLE_TASKS, null, condition, null, null, null, null);
		
		Task bestTask = null;
		int bestScore = 999999999;
		
		if (cursor.moveToFirst()) {
			do {
				Task task = new Task(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));
				Address task_src = getAddressForId(task.m_Src);
				
				int diffAvenue = Math.abs(task_src.m_Avenue - driverPosition.m_Avenue);
				int diffStreet = Math.abs(task_src.m_Street - driverPosition.m_Street);
				int score = diffAvenue + diffStreet;
				
				if (score <= bestScore) {
					bestScore = score;
					bestTask = task;
				}
			} while (cursor.moveToNext());
		}
		return bestTask;
	}
	
	public int getIdForAdress(int avenue, int street) {
		String condition = P_AVENUE + "=? AND " + P_STREET + "=?";
		
		Cursor cursor = m_Db.query(TABLE_POSITIONS, null, condition, new String[] {String.valueOf(avenue), String.valueOf(street)}, null, null, null);
		
		if (cursor.moveToFirst()) {
			return cursor.getInt(0);
		} else {
			ContentValues content = new ContentValues();
			content.put(P_AVENUE, avenue);
			content.put(P_STREET, street);
			return (int) m_Db.insert(TABLE_POSITIONS, null, content);
		}
	}
	
	public Address getAddressForId(int id) {
		String condition = P_ID + "=?";
		
		Cursor cursor = m_Db.query(TABLE_POSITIONS, null, condition, new String[] {String.valueOf(id)}, null, null, null);
		
		if (cursor.moveToFirst()) {
			Address address = new Address(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2));
			return address;
		}
		return null;
	}
	
	public void editDriver(Driver driver) {
		
		String condition = D_PERS_ID + "=?";
		
		ContentValues values = new ContentValues();
		values.put(D_PERS_ID, driver.m_Id);
		values.put(D_NAME, driver.m_Name);
		values.put(D_CUR_POS, driver.m_CurPos);
		
		m_Db.update(TABLE_DRIVER, values, condition, new String[] {String.valueOf(driver.m_Id)});
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
