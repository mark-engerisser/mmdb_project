package de.ur.mi.mmdb_project;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class ManageTasksActivity extends Activity {
	
	private ListView m_ListView;
	private TasksListAdapter m_ListAdapter;
	
	private ArrayList<Task> m_Tasks;
	
	private Database m_Db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_managetasks);
		
		m_Tasks = new ArrayList<Task>();
		m_Db = new Database(this);
		
		initUI();
		
		m_Db.openDb();
		m_Tasks = m_Db.getAllTasks();
		m_Db.closeDb();
		m_ListAdapter.notifyDataSetChanged();
	}

	private void initUI() {
		m_ListView = (ListView)findViewById(R.id.managetasks_list);
		m_ListAdapter = new TasksListAdapter();
		m_ListView.setAdapter(m_ListAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.managedrivers, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_adddriver) {
			addDriver();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void addDriver() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Auftrag hinzufügen");
		
		final View dialogView = getLayoutInflater().inflate(R.layout.addtaskdialog, null);
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
				
				EditText src_avenue = (EditText)dialogView.findViewById(R.id.addtask_src_avenue);
				EditText src_street = (EditText)dialogView.findViewById(R.id.addtask_src_street);
				EditText dest_avenue = (EditText)dialogView.findViewById(R.id.addtask_dest_avenue);
				EditText dest_street = (EditText)dialogView.findViewById(R.id.addtask_dest_street);
				
				RadioButton RBWaiting = (RadioButton)dialogView.findViewById(R.id.radio0);
				RadioButton RBInProgress = (RadioButton)dialogView.findViewById(R.id.radio1);
				RadioButton RBDone = (RadioButton)dialogView.findViewById(R.id.radio2);
				
				int src_id = m_Db.getIdForAdress(Integer.parseInt(src_avenue.getText().toString()), Integer.parseInt(src_street.getText().toString()));
				int dest_id = m_Db.getIdForAdress(Integer.parseInt(dest_avenue.getText().toString()), Integer.parseInt(dest_street.getText().toString()));
				
				int status_id = 0;
				if (RBWaiting.isChecked() == true) {
					status_id = 0;
				} else if (RBInProgress.isChecked() == true) {
					status_id = 1;
				} else if (RBDone.isChecked() == true) {
					status_id = 2;
				}
				
				Task task = new Task(0,0, src_id, dest_id, status_id);
						
				m_Db.addTask(task);
				
				m_Tasks = m_Db.getAllTasks();
				m_Db.closeDb();
				m_ListAdapter.notifyDataSetChanged();
				dialog.dismiss();
			}
			
		});
		builder.create().show();
	}

	private class TasksListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return m_Tasks.size();
		}

		@Override
		public Object getItem(int pos) {
			return m_Tasks.get(pos);
		}

		@Override
		public long getItemId(int pos) {
			return m_Tasks.get(pos).m_Id;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup arg2) {
			Task task = (Task)getItem(pos);
			
			if (convertView == null) {
				LayoutInflater inflater = ManageTasksActivity.this.getLayoutInflater();
				convertView = inflater.inflate(R.layout.managetasks_item, null);
			}
			
			TextView src = (TextView)convertView.findViewById(R.id.tasklist_address_src);
			TextView dest = (TextView)convertView.findViewById(R.id.tasklist_dest_address);
			TextView status = (TextView)convertView.findViewById(R.id.tasklist_status);
			TextView driver = (TextView)convertView.findViewById(R.id.tasklist_driver);
			
			m_Db.openDb();
			
			Address srcadd = m_Db.getAddressForId(task.m_Src);
			Address destadd = m_Db.getAddressForId(task.m_Dest);
			src.setText(srcadd.toAddressString());
			dest.setText(destadd.toAddressString());
			
			switch (task.m_Status) {
			case 0:
				status.setText("Ausstehend");
				status.setTextColor(Color.RED);
				break;
			case 1:
				status.setText("In Bearbeitung");
				status.setTextColor(Color.YELLOW);
				break;
			case 2:
				status.setText("Abgeschlossen");
				status.setTextColor(Color.GREEN);
				break;
			}
			
			if (task.m_Driver == 0) {
				driver.setText("Kein Fahrer");
			} else {
				Driver d = m_Db.getDriverForId(task.m_Driver);
				driver.setText(d.m_Name);
			}
					
			m_Db.closeDb();
			return convertView;
		}
		
	}
}
