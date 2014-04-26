package com.learningpod.android.activities;

import java.io.IOException;
import java.io.InputStream;
import java.text.ChoiceFormat;
import java.util.ArrayList;
import java.util.List;

import com.learningpod.android.BaseActivity;
import com.learningpod.android.R;
import com.learningpod.android.beans.explanations.ExplanationBean;
import com.learningpod.android.beans.pods.PodBean;
import com.learningpod.android.beans.questions.QuestionBean;
import com.learningpod.android.beans.questions.QuestionChoiceBean;
import com.learningpod.android.parser.GenericParser;
import com.learningpod.android.parser.ParserFactory;
import com.learningpod.android.parser.ParserType;
import com.learningpod.android.utility.LearningpodException;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PodQuestionActivity extends BaseActivity implements OnClickListener {

	private List<QuestionBean> questions;
	private ArrayList<ArrayList<ExplanationBean>> explanations;
	private int currentQuestionIndex = 0;
	private int[] greyButtonsIds = new int[]{R.drawable.graya,R.drawable.grayb,R.drawable.grayc, R.drawable.grayd};
	private int[] blueButtonIds = new int[] {R.drawable.bluea,R.drawable.blueb,R.drawable.bluec,R.drawable.blued};
	private int[] greenButtonIds = new int[] {R.drawable.greena,R.drawable.greenb, R.drawable.greenc, R.drawable.greend};
	private boolean isCurrentScreenForExplanation = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.questionlayout);		
		Bundle extras = getIntent().getExtras();	
		//get list of pods
		questions  = (List<QuestionBean>)extras.getSerializable("questions");
		// get the selected Pod
		PodBean selectedPod = (PodBean)extras.getSerializable("selectedPod");
		// get list of all explanations
		explanations = (ArrayList<ArrayList<ExplanationBean>>)extras.getSerializable("explanations");
		// set the pod details 
		TextView podTitleView = (TextView)findViewById(R.id.podname);
		podTitleView.setText(selectedPod.getTitle());
		TextView podDescView = (TextView)findViewById(R.id.poddesc);
		podDescView.setText(selectedPod.getDescription());		
		
		// enable disable content based on screen state
		enableScreenState();
		showNextQuestion();
	}
	
	private void enableScreenState(){
		// get the views that has different states
		Button btnSubmitNext = (Button)findViewById(R.id.btnsubmitnext);
		LinearLayout explanationContainer = (LinearLayout)findViewById(R.id.explanationcontainer);
		TextView explanationContentView = (TextView)findViewById(R.id.explanationcontent);
		
		if(isCurrentScreenForExplanation){
			btnSubmitNext.setText("NEXT");
			explanationContainer.setVisibility(View.VISIBLE);
			// get all the explanations for this question
			ArrayList<ExplanationBean> explanationsForThisQues = explanations.get(currentQuestionIndex);
			// get the first explanation			
			ExplanationBean explanation = explanationsForThisQues.get(0);
			explanationContentView.setText(Html.fromHtml(explanation.getExplanation().getExplanationBody()));
			
		}
		else{
			btnSubmitNext.setText("SUBMIT");
			explanationContainer.setVisibility(View.INVISIBLE);			
		}
		
		btnSubmitNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isCurrentScreenForExplanation){
					// increment the question number index				
					currentQuestionIndex++;
					isCurrentScreenForExplanation =false;
					enableScreenState();
					showNextQuestion();
				}else{
					isCurrentScreenForExplanation = true;
					enableScreenState();
				}
			}
		});
	}
	
	
	private void showNextQuestion(){
		// get the next question bean
		QuestionBean nextQuestion = questions.get(currentQuestionIndex);
		// set the question body
		TextView questionBodyView = (TextView)findViewById(R.id.quesbody);
		questionBodyView.setText(Html.fromHtml(nextQuestion.getChoiceQuestion().getQuestionBody().getQuestionBodyStr()));		
		// set the question image
		ImageView questionImage = (ImageView)findViewById(R.id.quesimage);		
		AssetManager assetMgr = getAssets();
		try {
			InputStream is = assetMgr.open("pods/images/" + nextQuestion.getChoiceQuestion().getQuestionBody().getQuestionImage() + ".jpg");			
			questionImage.setImageBitmap(BitmapFactory.decodeStream(is));
			// get explanations for this question
	
			
		} catch (IOException e) {
			Log.e("question", "Image not found");
		}
		
		// get Inflater instance
		LayoutInflater inflater = getLayoutInflater();
		// get choice container
		LinearLayout choiceContainer = (LinearLayout)findViewById(R.id.choicecontainer);
		// clear the previous choices
		choiceContainer.removeAllViews();
		// show choices
		List<QuestionChoiceBean> choicesForThisQuestion = nextQuestion.getChoiceQuestion().getChoiceInteraction();
		// create a list of all select choice buttons for changing drawable later
		final List<Button> selectChoiceButtons = new ArrayList<Button>();
		for(int idx=0;idx<choicesForThisQuestion.size();idx++){
			//get the choice bean object
			QuestionChoiceBean choice = choicesForThisQuestion.get(idx);
			// inflate the choice view layout
			View choiceView = inflater.inflate(R.layout.choice_view, null);
			//set the button image and choice text
			((TextView)choiceView.findViewById(R.id.choicebody)).setText(choice.getChoiceBody());
			Button btnSelectChoice = (Button)choiceView.findViewById(R.id.btnselectchoice);
			btnSelectChoice.setBackgroundResource(greyButtonsIds[idx]);
			selectChoiceButtons.add(btnSelectChoice);
			// set the on click listener
			btnSelectChoice.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// go through each button in the list
					for(int idx=0;idx<selectChoiceButtons.size();idx++){
						Button btn = selectChoiceButtons.get(idx);
						if(btn==v){
							btn.setBackgroundResource(blueButtonIds[idx]);
						}else{
							btn.setBackgroundResource(greyButtonsIds[idx]);
						}
					}
					
				}
			});
			
			// add the choice to the choice container
			choiceContainer.addView(choiceView);
		}
		
		
	}
	
	private Object parseUtility(InputStream is,ParserType type) throws LearningpodException{
		GenericParser parser = ParserFactory.getParser(type);
		parser.parse(is);
		return parser.retrieveSerializedObject();
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
