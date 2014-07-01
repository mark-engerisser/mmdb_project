package de.ur.mi.mmdb_project;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Database m_Db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		m_Db = new Database(this);
		
		initUI();
	}

	private void initUI() {
		Button ManagerButton = (Button)findViewById(R.id.manager_edit_drivers);
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
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("Fahrer Login");
				
				final View dialogView = getLayoutInflater().inflate(R.layout.login_dialog, null);
				builder.setView(dialogView);
				builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
					
				});
				builder.setPositiveButton("Anmelden", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						int driverid = 0;
						EditText persnum = (EditText)dialogView.findViewById(R.id.editText1);
						driverid = Integer.parseInt(persnum.getText().toString());
						
						m_Db.openDb();
						Driver driver = m_Db.getDriverForId(driverid);
						m_Db.closeDb();
						
						if (driver == null) {
							Toast.makeText(MainActivity.this, "Personalnummer existiert nicht", Toast.LENGTH_LONG).show();
						} else {
							Intent intent = new Intent(MainActivity.this, DriverActivity.class);
							intent.putExtra("driverid", driverid);
							startActivity(intent);
						}
						dialog.dismiss();
					}
					
				});
				builder.create().show();
			}
			
		});
	}
}
