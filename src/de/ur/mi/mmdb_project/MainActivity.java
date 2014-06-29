package de.ur.mi.mmdb_project;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initUI();
	}

	private void initUI() {
		Button ManagerButton = (Button)findViewById(R.id.ManagerButton);
		Button DriverButton = (Button)findViewById(R.id.DriverButton);
		
		ManagerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, ManagerActivity.class);
				startActivity(intent);
			}
			
		});
		
		DriverButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, DriverActivity.class);
				startActivity(intent);
			}
			
		});
	}
}
