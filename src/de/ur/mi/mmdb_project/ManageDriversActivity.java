package de.ur.mi.mmdb_project;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class ManageDriversActivity extends ActionBarActivity {

	Database m_Db;
	
	ListView m_ListView;
	DriversListAdapter m_ListAdapter;
	
	ArrayList<Driver> m_Drivers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_managedrivers);
		
		m_Drivers = new ArrayList<Driver>();
		
		m_Db = new Database(this);
		
		m_ListView = (ListView)findViewById(R.id.managedrivers_list);
		m_ListAdapter = new DriversListAdapter(this);
		m_ListView.setAdapter(m_ListAdapter);
		
		m_Db.openDb();
		retriveDbData();
		m_Db.closeDb();
		m_ListAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.managedrivers, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_adddriver){
			addNewDriver();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void retriveDbData() {
		m_Drivers = m_Db.getAllDrivers();
	}
	
	private void addNewDriver() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Fahrer hinzufügen");
		
		final View dialogView = getLayoutInflater().inflate(R.layout.adddriverdialog, null);
		builder.setView(dialogView);
		builder.setNegativeButton("Abbrechen", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
			
		});
		builder.setPositiveButton("Hinzufügen", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				m_Db.openDb();
				
				TextView name = (TextView)dialogView.findViewById(R.id.adddriverdialogname);
				TextView pos = (TextView)dialogView.findViewById(R.id.adddriverdialogpos);
				m_Db.addDriver(name.getText().toString(), 0);
				
				retriveDbData();
				
				m_Db.closeDb();
				m_ListAdapter.notifyDataSetChanged();
				dialog.dismiss();
			}
			
		});
		builder.create().show();
	}

	private class DriversListAdapter extends BaseAdapter {
		
		private Context context;
		
		public DriversListAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return m_Drivers.size();
		}

		@Override
		public Object getItem(int arg0) {
			return m_Drivers.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return m_Drivers.get(arg0).m_Id;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup arg2) {
			
			Driver driver = (Driver)getItem(pos);
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.managedrivers_item, null);
			}
			
			TextView name = (TextView)convertView.findViewById(R.id.drivers_item_name);
			TextView id = (TextView)convertView.findViewById(R.id.drivers_item_id);
			TextView position = (TextView)convertView.findViewById(R.id.drivers_item_pos);
			
			name.setText(driver.m_Name);
			id.setText(String.valueOf(driver.m_Id));
			
			return convertView;
		}
		
	}
}