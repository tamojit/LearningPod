package com.learningpod.android.activities;

import java.util.HashMap;
import java.util.List;

import com.learningpod.android.BackgroundAsyncTasks;
import com.learningpod.android.BackgroundTasks;
import com.learningpod.android.BaseActivity;
import com.learningpod.android.ContentCacheStore;
import com.learningpod.android.R;
import com.learningpod.android.beans.UserProfileBean;
import com.learningpod.android.beans.pods.PodBean;
import com.learningpod.android.db.LearningpodDbHandler;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MapActivity extends BaseActivity implements OnClickListener{
	
	
	private float lastX;
	private ViewFlipper mapFlipper;
	private List<PodBean> pods = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// get user profile and pods from content cache
		UserProfileBean userProfileBean = ContentCacheStore.getContentCache().getLoggedInUserProfile();		
		//get list of pods. getting 
		pods  = ContentCacheStore.getContentCache().getPods();		
		setContentView(R.layout.home_screen);
		// create the view flipper
		mapFlipper = new ViewFlipper(this);
		LayoutParams mapParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		mapFlipper.setLayoutParams(mapParams);
		
		// get Inflater instance
		LayoutInflater inflater = getLayoutInflater();
		// inflate and create the Map Views
		View mapView1 = createMap1View();
		View mapView2 = inflater.inflate(R.layout.mapview2, null);
		View mapView3 = inflater.inflate(R.layout.mapview3, null);
		
		// add listeners to next and previous buttons
		mapView1.findViewById(R.id.btnmap1next).setOnClickListener(this);
		mapView2.findViewById(R.id.btnmap2next).setOnClickListener(this);
		mapView2.findViewById(R.id.btnmap2prev).setOnClickListener(this);
		mapView3.findViewById(R.id.btnmap3prev).setOnClickListener(this);
		
		// add views to the flipper
		mapFlipper.addView(mapView1,0);		
		mapFlipper.addView(mapView2,1);
		mapFlipper.addView(mapView3,2);
		setContentView(mapFlipper);
		
	}
	
	private View createMap1View(){
		// get the user id
		String userId = ContentCacheStore.getContentCache().getLoggedInUserProfile().getId();
		// get Inflater instance
		LayoutInflater inflater = getLayoutInflater();
		View mapView1 = inflater.inflate(R.layout.mapview1, null);
		int numberOfQuestionsCompleted = 0;
		LearningpodDbHandler dbHandler = new LearningpodDbHandler(this);
		dbHandler.open();		
		
		// planet 1
		// get all views
		PodBean pod1 = pods.get(0);
		numberOfQuestionsCompleted =  dbHandler.getUserProgressStatus(ContentCacheStore.getContentCache().getLoggedInUserProfile().getId(), pod1.getPodId());
		ImageButton btnPlanet1 = (ImageButton)mapView1.findViewById(R.id.planet1);
		TextView txtNamePlanet1 = (TextView)mapView1.findViewById(R.id.planet1name);
		ImageView imgSpaceshipPlanet1 = (ImageView)mapView1.findViewById(R.id.planet1ss);
		View progressBarRedPlanet1 = mapView1.findViewById(R.id.planet1pbred);
		View progressBarBluePlanet1 = mapView1.findViewById(R.id.planet1pbblue);
		
		// set the state 
		btnPlanet1.setOnClickListener(this);
		btnPlanet1.setTag("0");
		txtNamePlanet1.setText(pod1.getTitle());
		if(numberOfQuestionsCompleted!=0){
			if(numberOfQuestionsCompleted==pod1.getPodElements().size()){
				// change the image to the one with the flag
				imgSpaceshipPlanet1.setVisibility(View.VISIBLE);
			}else{
				imgSpaceshipPlanet1.setVisibility(View.VISIBLE);
				((LinearLayout.LayoutParams)progressBarRedPlanet1.getLayoutParams()).weight = (float)(0.2*numberOfQuestionsCompleted);
				((LinearLayout.LayoutParams)progressBarBluePlanet1.getLayoutParams()).weight = (float)(1.0-(0.2*numberOfQuestionsCompleted));
				progressBarRedPlanet1.setVisibility(View.VISIBLE);
				progressBarBluePlanet1.setVisibility(View.VISIBLE);
			}
			
		}
		dbHandler.close();
		return mapView1;
	}
	
	// Method to handle touch event like left to right swap and right to left swap
    public boolean onTouchEvent(MotionEvent touchevent) 
    {
                 switch (touchevent.getAction())
                 {
                        // when user first touches the screen to swap
                         case MotionEvent.ACTION_DOWN: 
                         {
                             lastX = touchevent.getX();
                             break;
                        }
                         case MotionEvent.ACTION_UP: 
                         {
                             float currentX = touchevent.getX();
                             
                             // if left to right swipe on screen
                             if (lastX < currentX) 
                             {
                                  // If no more View/Child to flip
                                 if (mapFlipper.getDisplayedChild() == 0)
                                     break;
                                 
                                 // set the required Animation type to mapFlipper
                                 // The Next screen will come in form Left and current Screen will go OUT from Right 
                                 mapFlipper.setInAnimation(this, R.anim.in_from_left);
                                 mapFlipper.setOutAnimation(this, R.anim.out_to_right);
                                 // Show the previous Screen
                                 mapFlipper.showPrevious();
                             }
                             
                             // if right to left swipe on screen
                             if (lastX > currentX)
                             {
                                 if (mapFlipper.getDisplayedChild() == 2)
                                     break;
                                 // set the required Animation type to mapFlipper
                                 // The Next screen will come in form Right and current Screen will go OUT from Left 
                                 mapFlipper.setInAnimation(this, R.anim.in_from_right);
                                 mapFlipper.setOutAnimation(this, R.anim.out_to_left);
                                 // Show The next Screen
                                 mapFlipper.showNext();
                             }
                             break;
                         }
                 }
                 return false;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v instanceof ImageButton){
			int selectedPlatentId = Integer.parseInt(v.getTag().toString());
			PodBean selectedPod = pods.get(selectedPlatentId);
			HashMap<String,Object> params = new HashMap<String,Object>();
			params.put("selectedPod",selectedPod);
			new BackgroundAsyncTasks(MapActivity.this, params).execute(BackgroundTasks.LOAD_POD_QUESTIONS);
		}
		else if (v.getId()==R.id.btnmap1next || v.getId()==R.id.btnmap2next){
			 // set the required Animation type to mapFlipper
            // The Next screen will come in form Right and current Screen will go OUT from Left 
            mapFlipper.setInAnimation(this, R.anim.in_from_right);
            mapFlipper.setOutAnimation(this, R.anim.out_to_left);
            // Show The Previous Screen
            mapFlipper.showNext();
		}
		else if (v.getId()==R.id.btnmap2prev || v.getId()==R.id.btnmap3prev){
			            
            // set the required Animation type to mapFlipper
            // The Next screen will come in form Left and current Screen will go OUT from Right 
            mapFlipper.setInAnimation(this, R.anim.in_from_left);
            mapFlipper.setOutAnimation(this, R.anim.out_to_right);
            // Show the previous Screen
            mapFlipper.showPrevious();
		}
		 
		
	}


}
