package com.learningpod.android.activities;


import java.util.List;

import android.os.Bundle;
import android.widget.TextView;

import com.learningpod.android.BaseActivity;
import com.learningpod.android.R;
import com.learningpod.android.beans.pods.PodBean;

public class HomeScreenActivity extends BaseActivity {

	private List<PodBean> pods = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		String userName = extras.get("username").toString();
		//get list of pods
		pods  = (List<PodBean>)extras.getSerializable("pods");
		
		setContentView(R.layout.home_screen);
		
		TextView txtUserName = (TextView)findViewById(R.id.username);
		txtUserName.setText("Hello " + userName);
		// this is only for testing
		//populatePods();
		
	}
	
	private void populatePods(){
		PodBean pod = new PodBean();
		pod.setTitle("Learning Pod 1");
		pod.setDescription("This is the description of learning Pod 1");
		
		PodBean pod1 = new PodBean();
		pod1.setTitle("Learning Pod 1");
		pod1.setDescription("This is the description of learning Pod 1");
		
		PodBean pod2 = new PodBean();
		pod2.setTitle("Learning Pod 1");
		pod2.setDescription("This is the description of learning Pod 1");
		
		PodBean pod3 = new PodBean();
		pod3.setTitle("Learning Pod 1");
		pod3.setDescription("This is the description of learning Pod 1");
		
		pods.add(pod);
		pods.add(pod1);
		pods.add(pod2);
		pods.add(pod3);
		
	}
}
