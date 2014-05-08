package com.learningpod.android;

import com.learningpod.android.activities.AccountSelectorActivity;
import com.learningpod.android.activities.HomeScreenActivity;
import com.learningpod.android.activities.HomeScreenActivityWithSlidingMenu;
import com.learningpod.android.activities.MapActivity;
import com.learningpod.android.utility.CustomProgressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class BaseActivity  extends Activity implements DialogInterface.OnClickListener {

	private ProgressDialog progressDialog;
	private AlertDialog alertDialog;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		// progress dialog for the application.
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.hide();
		
		// alert dialog for the application.
		alertDialog = new AlertDialog.Builder(this).create();		
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", this);
		this.alertDialog.setCancelable(false);
	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	if(!(this instanceof AccountSelectorActivity)){
			getMenuInflater().inflate(R.menu.common_menu, menu);
		}
	return true;
	}
	
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==R.id.help){
			
		}
		else if(item.getItemId()==R.id.about){
			
		}
		else if(item.getItemId()==R.id.terms){
			
		}
		return true;
	}

	
	public ProgressDialog getProgressDialog() {
		return this.progressDialog;
	}

	public void showAlertDialog(String alertTitle, String alertMessage) {
		if (progressDialog.isShowing()) {
			progressDialog.hide();
		}
		this.alertDialog.setTitle(alertTitle);
		this.alertDialog.setMessage(alertMessage);
		this.alertDialog.show();
	}
	
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	

	
}
