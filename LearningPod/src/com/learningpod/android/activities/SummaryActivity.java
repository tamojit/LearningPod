package com.learningpod.android.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learningpod.android.BaseActivity;
import com.learningpod.android.ContentCacheStore;
import com.learningpod.android.R;
import com.learningpod.android.beans.UserProgressInfo;
import com.learningpod.android.beans.explanations.ExplanationBean;
import com.learningpod.android.beans.pods.PodBean;
import com.learningpod.android.beans.questions.QuestionBean;
import com.learningpod.android.db.LearningpodDbHandler;

public class SummaryActivity extends BaseActivity implements OnClickListener{

	private PodBean selectedPod;
 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.summarylayout);
		
		TextView goToMapView = (TextView)findViewById(R.id.gotomap);
		goToMapView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SummaryActivity.this,HomeScreenActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				SummaryActivity.this.finish();
			}
		});
		
		
		Bundle extras = getIntent().getExtras();	
		 
		// get the selected Pod
		selectedPod = (PodBean)extras.getSerializable("selectedPod");
		 
		// get the current user's progress for this pod
		
		LearningpodDbHandler dbHandler = new LearningpodDbHandler(this);
		dbHandler.open();
		List<UserProgressInfo> userProgress = dbHandler.getUserProgressDetails(ContentCacheStore.getContentCache().getLoggedInUserProfile().getId(), selectedPod.getPodId());		
		dbHandler.close();
		
		int totalQuestions = userProgress.size();
		int correctAnswers = 0;
		
		LinearLayout summaryQuesContainer = (LinearLayout)findViewById(R.id.summaryquestioncontainer);
		((TextView)findViewById(R.id.summarypodname)).setText("Summary of "+ selectedPod.getTitle());
		LayoutInflater inflater = getLayoutInflater();
		for(int idx=0;idx<userProgress.size();idx++){
			UserProgressInfo progress = userProgress.get(idx);
			View view = inflater.inflate(R.layout.summary_question_view, null);
			TextView quesSeq = (TextView)view.findViewById(R.id.summaryquestionsequence);
			quesSeq.setText(Integer.valueOf(idx+1).toString() + ". ");
			
			ImageView quesImage = (ImageView)view.findViewById(R.id.summaryQuestionicon);
			if(progress.isChoiceCorrect()){
				quesImage.setBackgroundResource(R.drawable.tick);
				correctAnswers++;
			}
			else{
				quesImage.setBackgroundResource(R.drawable.cross);
			}
			view.setId(idx);
			view.setOnClickListener(this);
			summaryQuesContainer.addView(view);
		}
		
		int percentage = (int)((correctAnswers*100/totalQuestions));
		((TextView)findViewById(R.id.correctpercentage)).setText( percentage + "%");
		
	}


	@Override
	public void onClick(View v) {
		/*Intent intent = new Intent(this,PodQuestionActivity.class );
		 intent.putExtra("selectedPod", selectedPod);
		 intent.putExtra("questions", questions);
		 intent.putExtra("explanations", explanations);
		 intent.putExtra("currentQuestionIndex", v.getId());
		 startActivity(intent);*/
		
	}
}
