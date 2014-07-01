package de.ur.mi.mmdb_project;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class DriverActivity extends Activity implements OnClickListener {
	
	private Database m_Db;
	
	private Driver m_Driver;
	private Task m_CurTask;
	
	private Button m_NewTaskButton;
	
	private TextView m_Driver_CurPos;
	private TextView m_Driver_Name;
	private TextView m_Driver_PersNum;
	
	private TextView m_Task_Src;
	private TextView m_Task_Dest;
	private RadioButton m_Task_Status_InProgress;
	private RadioButton m_Task_Status_Done;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver);
		
		initNewTaskButton();
		initDriverUI();
		initCurTaskUI();
		
		m_Db = new Database(this);
		m_Db.openDb();
		
		getDriverInfo();
		getTaskInfo();
		fillDriverInfoWithData();
		
		m_Db.closeDb();
	}

	private void initNewTaskButton() {
		m_NewTaskButton = (Button)findViewById(R.id.driver_getnewtask);
		m_NewTaskButton.setOnClickListener(this);
	}

	private void getTaskInfo() {
		m_CurTask = m_Db.getTaskForDriverId(m_Driver.m_Id);
		if (m_CurTask == null) {
			m_Task_Src.setText("Keine Daten");
			m_Task_Dest.setText("Keine Daten");
			m_Task_Status_Done.setEnabled(false);
			m_Task_Status_InProgress.setEnabled(false);
		} else {
			Address src = m_Db.getAddressForId(m_CurTask.m_Src);
			Address dest = m_Db.getAddressForId(m_CurTask.m_Dest);
			m_Task_Src.setText(src.toAddressString());
			m_Task_Dest.setText(dest.toAddressString());
			
			m_Task_Status_InProgress.setChecked(false);
			m_Task_Status_Done.setChecked(false);
			if (m_CurTask.m_Status == 1) {
				m_Task_Status_InProgress.setChecked(true);
			} else if (m_CurTask.m_Status == 2) {
				m_Task_Status_Done.setChecked(true);
			}
		}
	}

	private void initCurTaskUI() {
		m_Task_Src = (TextView)findViewById(R.id.driver_curtask_src);
		m_Task_Dest = (TextView)findViewById(R.id.driver_curtask_dest);
		m_Task_Status_InProgress = (RadioButton)findViewById(R.id.driver_radio0);
		m_Task_Status_Done = (RadioButton)findViewById(R.id.driver_radio1);
	}

	private void fillDriverInfoWithData() {
		m_Driver_Name.setText(m_Driver.m_Name);
		m_Driver_PersNum.setText(String.valueOf(m_Driver.m_Id));
		Address address = m_Db.getAddressForId(m_Driver.m_CurPos);
		m_Driver_CurPos.setText(address.toAddressString());
	}

	private void getDriverInfo() {
		int id = getIntent().getExtras().getInt("driverid");
		
		m_Driver = m_Db.getDriverForId(id);
	}

	private void initDriverUI() {
		m_Driver_CurPos = (TextView)findViewById(R.id.driver_curpos);
		m_Driver_Name = (TextView)findViewById(R.id.driver_name);
		m_Driver_PersNum = (TextView)findViewById(R.id.driver_id);
	}

	@Override
	public void onClick(View arg0) {
		m_Db.openDb();
		Task bestTask = m_Db.getBestTaskForAddress(m_Driver.m_CurPos);
		m_Db.closeDb();
	}
}