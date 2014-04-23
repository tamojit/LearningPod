package com.learningpod.android.activities;


import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.learningpod.android.BaseActivity;
import com.learningpod.android.R;
import com.learningpod.android.beans.pods.PodBean;
import com.learningpod.android.carousel.AppUtils;
import com.learningpod.android.carousel.CarouselDataItem;
import com.learningpod.android.carousel.CarouselView;
import com.learningpod.android.carousel.CarouselViewAdapter;
import com.learningpod.android.carousel.Singleton;

public class HomeScreenActivity extends BaseActivity implements OnItemSelectedListener{
	Singleton 				m_Inst 					= Singleton.getInstance();
	CarouselViewAdapter 	m_carouselAdapter		= null;	 
	private final int		m_nFirstItem			= 1000;
	private List<PodBean> pods = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	/*	Bundle extras = getIntent().getExtras();
		String userName = extras.get("username").toString();
		//get list of pods
		pods  = (List<PodBean>)extras.getSerializable("pods");
		
		setContentView(R.layout.home_screen);
		
		TextView txtUserName = (TextView)findViewById(R.id.username);
		txtUserName.setText("Hello " + userName);*/
		// this is only for testing
		//populatePods();
		
		
  		
        // compute screen size and scaling
     	Singleton.getInstance().InitGUIFrame(this);
     	
     	int padding = m_Inst.Scale(10);
		// create the interface : full screen container
		RelativeLayout panel  = new RelativeLayout(this);
	    panel.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		panel.setPadding(padding, padding, padding, padding);
	    panel.setBackgroundDrawable(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, 
	    		new int[]{Color.WHITE, Color.GRAY}));
	    setContentView(panel); 
	    	  
	    // copy images from assets to sdcard
	    
	    AppUtils.AssetFileCopy(this, "/mnt/sdcard/black.jpg", "black.jpg", false);
	   /* AppUtils.AssetFileCopy(this, "/mnt/sdcard/black.jpg", "black.jpg", false);
	    AppUtils.AssetFileCopy(this, "/mnt/sdcard/black.jpg", "plasma3.png", false);
	    AppUtils.AssetFileCopy(this, "/mnt/sdcard/black.jpg", "plasma4.png", false);*/
	    
	    //Create carousel view documents
	    ArrayList<CarouselDataItem> Docus = new ArrayList<CarouselDataItem>();
	    for (int i=0;i<1000;i++) {
	    	CarouselDataItem docu;
	    	 if (i%4==0) docu = new CarouselDataItem("/mnt/sdcard/black.jpg", 0, "First Image "+i);
		       	else if (i%4==1) docu = new CarouselDataItem("/mnt/sdcard/black.jpg", 0, "Second Image "+i);
		        else if (i%4==2) docu = new CarouselDataItem("/mnt/sdcard/black.jpg", 0, "Third Image "+i);
		        else docu = new CarouselDataItem("/mnt/sdcard/black.jpg", 0, "4th Image "+i);
	        Docus.add(docu);
	    } 
	    
	  /*  // add the serach filter
	    EditText etSearch = new EditText(this);
	    etSearch.setHint("Search your documents");
	    etSearch.setSingleLine();
	    etSearch.setTextColor(Color.BLACK);
	    AppUtils.AddView(panel, etSearch, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 
	    		new int[][]{new int[]{RelativeLayout.CENTER_HORIZONTAL}, new int[]{RelativeLayout.ALIGN_PARENT_TOP}}, -1,-1);*/

	  /*  // add logo
	    TextView tv = new TextView(this);
	    tv.setTextColor(Color.BLACK);
	    tv.setText("www.pocketmagic.net");
	    AppUtils.AddView(panel, tv, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 
	    		new int[][]{new int[]{RelativeLayout.CENTER_HORIZONTAL}, new int[]{RelativeLayout.ALIGN_PARENT_BOTTOM}}, -1,-1);
	    */
	    // create the carousel
	    CarouselView coverFlow = new CarouselView(this);
        
	    // create adapter and specify device independent items size (scaling)
	    // for more details see: http://www.pocketmagic.net/2013/04/how-to-scale-an-android-ui-on-multiple-screens/
	    m_carouselAdapter =  new CarouselViewAdapter(this,Docus, m_Inst.Scale(400),m_Inst.Scale(300));
        coverFlow.setAdapter(m_carouselAdapter);
        coverFlow.setSpacing(-1*m_Inst.Scale(150));
        coverFlow.setSelection(Integer.MAX_VALUE / 2, true);
        coverFlow.setAnimationDuration(1000);
        coverFlow.setOnItemSelectedListener((OnItemSelectedListener) this);

        AppUtils.AddView(panel, coverFlow, LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT, 
        		new int[][]{new int[]{RelativeLayout.CENTER_IN_PARENT}},
        		-1, -1); 
		

		
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

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		 /*CarouselDataItem docu =  (CarouselDataItem) m_carouselAdapter.getItem((int) arg3);
		 if (docu!=null)
			 Toast.makeText(this, "You've clicked on:"+docu.getDocText(), Toast.LENGTH_SHORT).show();*/
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
