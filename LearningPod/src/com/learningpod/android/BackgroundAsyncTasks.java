package com.learningpod.android;

import java.io.IOException;
import java.util.HashMap;

import com.learningpod.android.activities.HomeScreenActivity;
import com.learningpod.android.beans.UserProfileBean;
import com.learningpod.android.parser.ParserType;
import com.learningpod.android.rest.HttpRestServiceHandler;
import com.learningpod.android.rest.RestServiceHandlerFactory;
import com.learningpod.android.utility.LearningpodException;
import com.learningpod.android.utility.UrlConstants;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;



public class BackgroundAsyncTasks extends AsyncTask<BackgroundTasks, Integer, Object> {

	private BaseActivity currentActivity = null;
	private BackgroundTasks task = null;
	private HashMap<String, Object> params;
	
	
	public BackgroundAsyncTasks(BaseActivity activity, HashMap<String, Object> params){
		this.currentActivity = activity;
		this.params = params;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		currentActivity.getProgressDialog().setMessage("Loading...");
		currentActivity.getProgressDialog().show();
	}

	@Override
	protected Object doInBackground(BackgroundTasks... tasks) {
		// TODO Auto-generated method stub
		this.task = tasks[0];
		
		// REST service handler for all rest service communication
		HttpRestServiceHandler serviceHandler = RestServiceHandlerFactory
				.getServiceHandler();
		if(task==BackgroundTasks.SELECTED_ACCOUNT_AUTHENTICATION){
			// get the selected account 
			Account selectedAccount = (Account)params.get("selectedAccount");
			
			// get the auth token for the selected account
			String authToken = updateToken(selectedAccount, true);
			// call google api for getting basic profile information if auth token is retrieved
			UserProfileBean userProfileBean = null;
			if(!authToken.equalsIgnoreCase("")){
				HashMap<String, String> urlParams = new HashMap<String, String>();
				urlParams.put("access_token", authToken);
				try {
				 userProfileBean = 	(UserProfileBean)serviceHandler.fireHttpGet(UrlConstants.PROFILE_DATA_FETCH, urlParams, ParserType.PROFILE_DETAILS_PARSER);
				} catch (LearningpodException e) {
					
					e.printStackTrace();
				}
			}else{
				// auth token not recieved. user may not have allowed access.
			}
			
			return userProfileBean ;
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		currentActivity.getProgressDialog().hide();
		if(result==null){
			// something went wrong
			return;
		}
		if(task==BackgroundTasks.SELECTED_ACCOUNT_AUTHENTICATION){
			UserProfileBean userBean = (UserProfileBean)result;
			// fetch the list of PODS 
			
			Intent intent = new Intent(currentActivity,HomeScreenActivity.class);
			intent.putExtra("username", userBean.getName());
			currentActivity.startActivity(intent);
			
		}
		
	}
	
	private boolean listAssetFiles(String path) {

	    String [] list;
	    try {
	        list = currentActivity.getAssets().list(path);
	        if (list.length > 0) {
	            // This is a folder
	            for (String file : list) {
	                if (!listAssetFiles(path + "/" + file))
	                    return false;
	            }
	        } else {
	            // This is a file
	            // TODO: add file name to an array list
	        }
	    }catch (IOException e) {
	        return false;
	    }

	    return true; 
	} 
	
	private String updateToken(Account selectedAccount, boolean invalidateToken){
		String authToken = "";
		AccountManager manager = AccountManager.get(currentActivity.getApplicationContext());
		String scope = "https://www.googleapis.com/auth/userinfo.profile";
		AccountManagerFuture<Bundle> accountManagerFuture  = manager.
				getAuthToken(selectedAccount, "oauth2:" + scope, null, currentActivity, null, null);
		try {
			Bundle authTokenBundle = accountManagerFuture.getResult();
			authToken = authTokenBundle.getString(AccountManager.KEY_AUTHTOKEN).toString();
			
			if(invalidateToken){
				manager.invalidateAuthToken("com.google", authToken);
				authToken = updateToken(selectedAccount, false);
			}
		} catch (OperationCanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthenticatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return authToken;
	}

}
