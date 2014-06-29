package de.ur.mi.mmdb_project;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;

public class ManagerActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
		
		initUI();
	}

	private void initUI() {
		Button editDrivers = (Button)findViewById(R.id.manager_edit_drivers);
		Button editTasks = (Button)findViewById(R.id.manager_edit_tasks);
		
		editDrivers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ManagerActivity.this, ManageDriversActivity.class);
				startActivity(intent);
			}
			
		});
		
		editTasks.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
	}

}
