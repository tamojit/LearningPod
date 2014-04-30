package com.learningpod.android.activities;

import java.io.IOException;
import java.io.InputStream;
import java.text.ChoiceFormat;
import java.util.ArrayList;
import java.util.List;

import com.learningpod.android.BaseActivity;
import com.learningpod.android.ContentCacheStore;
import com.learningpod.android.R;
import com.learningpod.android.beans.UserProgressInfo;
import com.learningpod.android.beans.explanations.ExplanationBean;
import com.learningpod.android.beans.pods.PodBean;
import com.learningpod.android.beans.questions.QuestionBean;
import com.learningpod.android.beans.questions.QuestionChoiceBean;
import com.learningpod.android.db.LearningpodDbHandler;
import com.learningpod.android.parser.GenericParser;
import com.learningpod.android.parser.ParserFactory;
import com.learningpod.android.parser.ParserType;
import com.learningpod.android.utility.LearningpodException;
import com.learningpod.androind.listeners.ChoiceSelectListner;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PodQuestionActivity extends BaseActivity {

	private List<QuestionBean> questions;
	private ArrayList<ArrayList<ExplanationBean>> explanations;
	private PodBean selectedPod;
	private int currentQuestionIndex = 0;
	private int[] greyButtonsIds = new int[]{R.drawable.graya,R.drawable.grayb,R.drawable.grayc, R.drawable.grayd};
	private int[] blueButtonIds = new int[] {R.drawable.bluea,R.drawable.blueb,R.drawable.bluec,R.drawable.blued};
	private int[] greenButtonIds = new int[] {R.drawable.greena,R.drawable.greenb, R.drawable.greenc, R.drawable.greend};
	private int[] redButtonIds = new int[] {R.drawable.reda,R.drawable.redb, R.drawable.redc, R.drawable.redd};
	private List<Button> selectChoiceButtons ;
	private boolean isCurrentScreenForExplanation = false;
	private int currentSelectedChoiceIndex = -1;
	private boolean isCurrentSelectedChoiceCorrect = false;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.questionlayout);		
		Bundle extras = getIntent().getExtras();	
		//get list of pods
		questions  = (List<QuestionBean>)extras.getSerializable("questions");
		// get the selected Pod
		selectedPod = (PodBean)extras.getSerializable("selectedPod");
		// get list of all explanations
		explanations = (ArrayList<ArrayList<ExplanationBean>>)extras.getSerializable("explanations");
		// set the pod details 
		TextView podTitleView = (TextView)findViewById(R.id.podname);
		podTitleView.setText(selectedPod.getTitle());
		TextView podDescView = (TextView)findViewById(R.id.poddesc);
		podDescView.setText(selectedPod.getDescription());	
		// get the current question number for this pod and user id combination
		LearningpodDbHandler dbHandler = new LearningpodDbHandler(this);
		dbHandler.open();
		currentQuestionIndex = dbHandler.getUserProgressStatus(ContentCacheStore.getContentCache().getLoggedInUserProfile().getId(), selectedPod.getPodId());
		dbHandler.close();
		
		
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
			if(currentQuestionIndex==questions.size()-1){
				// we have reached the last question in the pod
				btnSubmitNext.setText("View Summary");
			}else{
				btnSubmitNext.setText("NEXT");
			}
			explanationContainer.setVisibility(View.VISIBLE);
			// get all the explanations for this question
			ArrayList<ExplanationBean> explanationsForThisQues = explanations.get(currentQuestionIndex);
			// get the first explanation			
			ExplanationBean explanation = explanationsForThisQues.get(0);
			explanationContentView.setText(Html.fromHtml(explanation.getExplanation().getExplanationBody()));
			// get the current selected choice button
			Button selectedButton  = selectChoiceButtons.get(currentSelectedChoiceIndex);
			// change the choice label if the selected choice is correct			
			// Also change the image of the selection button
			if(isCurrentSelectedChoiceCorrect){
				TextView choiceLabelText = (TextView)findViewById(R.id.choicelabel);						
				choiceLabelText.setText("CORRECT!");
				choiceLabelText.setTextColor(Color.parseColor("#38610B"));
				choiceLabelText.setTextSize(20);
				choiceLabelText.setTypeface(Typeface.DEFAULT_BOLD);
				// change the image of the selection button and the width of the button to 
				// fit a bigger correct or wrong image
				int buttonWidthInPx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55, getResources().getDisplayMetrics());
				selectedButton.getLayoutParams().width = buttonWidthInPx;
				selectedButton.getLayoutParams().height = buttonWidthInPx;								
				selectedButton.setBackgroundResource(greenButtonIds[currentSelectedChoiceIndex]);
				((LinearLayout.LayoutParams)selectedButton.getLayoutParams()).setMargins(0, 0, 0, 0);
				selectedButton.invalidate();
				
			}else{
				TextView choiceLabelText = (TextView)findViewById(R.id.choicelabel);						
				choiceLabelText.setText("WRONG!");
				choiceLabelText.setTextColor(Color.parseColor("#FE2E2E"));
				choiceLabelText.setTextSize(20);
				choiceLabelText.setTypeface(Typeface.DEFAULT_BOLD);
				// change the image of the selection button and the width of the button to 
				// fit a bigger correct or wrong image
				int buttonWidthInPx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55, getResources().getDisplayMetrics());
				selectedButton.getLayoutParams().width = buttonWidthInPx;
				selectedButton.getLayoutParams().height = buttonWidthInPx;
				((LinearLayout.LayoutParams)selectedButton.getLayoutParams()).setMargins(0, 0, 0, 0);								
				selectedButton.setBackgroundResource(redButtonIds[currentSelectedChoiceIndex]);
				selectedButton.invalidate();
			}
		}
		else{
			// Disabled unless one option is selected
			btnSubmitNext.setEnabled(false);
			btnSubmitNext.setText("SUBMIT");
			explanationContainer.setVisibility(View.INVISIBLE);		
			
			// set the  choice label to its default state
			TextView choiceLabelText = (TextView)findViewById(R.id.choicelabel);	
			choiceLabelText.setText("CHOSE THE CORRECT ANSWER");
			choiceLabelText.setTextColor(Color.parseColor("#000000"));
			choiceLabelText.setTextSize(13);
			choiceLabelText.setTypeface(Typeface.DEFAULT);
			
		}
		
		btnSubmitNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isCurrentScreenForExplanation){
					// clicking the Next button in explanation screen
					if(currentQuestionIndex==questions.size()-1){ 						
						// we have reached the last question in the pod
						 showAlertDialog("Learningpod Error", "Summary screen under development");
					}else{						
						// increment the question number index				
						currentQuestionIndex++;
						isCurrentScreenForExplanation =false;
						enableScreenState();
						showNextQuestion();
					}
				}else{
					// clicking the submit button in Question screen
					// save the selected option in DB
					saveSelectedChoiceInDb();					
					isCurrentScreenForExplanation = true;
					enableScreenState();
					
				}
			}
		});
	}
	
	private void saveSelectedChoiceInDb(){
		// get the  question's item id from pod bean
		String questionItemId = selectedPod.getPodElements().get(currentQuestionIndex).getItemId();
		// get the user id of the logged in user
		String userId = ContentCacheStore.getContentCache().getLoggedInUserProfile().getId();
		//get the pod id 
		String podId = selectedPod.getPodId();
		// get the selected choice id
		String choiceId = questions.get(currentQuestionIndex).getChoiceQuestion().getChoiceInteraction().get(currentSelectedChoiceIndex).getChoiceId();
		UserProgressInfo userProgress = new UserProgressInfo();
		userProgress.setUserId(userId);
		userProgress.setPodId(podId);
		userProgress.setQuestionId(questionItemId);
		userProgress.setChoiceId(choiceId);
		userProgress.setChoiceCorrect(isCurrentSelectedChoiceCorrect);
		// create a db handler and open the connection
		LearningpodDbHandler handler = new LearningpodDbHandler(this);
		handler.open();
		handler.saveUserProgressInfo(userProgress);
		handler.close();
	}
	
	private void showNextQuestion(){
		
		// show the progress status in the screen
		// get the layout container for the progress dots
		LinearLayout progressLayout = (LinearLayout)findViewById(R.id.questionprogresscontainer);
		// clear the progress layout
		progressLayout.removeAllViews();
		//convert the desired dp value to pixel for creating layout parameters
		int progressViewWidthInPx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
		LinearLayout.LayoutParams progressLayoutParams = new LinearLayout.LayoutParams(progressViewWidthInPx,progressViewWidthInPx);
		//set the margins after necessary conversions from pixels to dp
		progressLayoutParams.setMargins((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics())
				, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics())
				, 0, 0);
		progressLayoutParams.gravity = Gravity.CENTER;
		for(int idx=0;idx<questions.size();idx++){
			View progressDot = new View(this);
			progressDot.setLayoutParams(progressLayoutParams);
			progressLayout.addView(progressDot);				
			if(idx<=currentQuestionIndex){
				progressDot.setBackgroundResource(R.drawable.dotblue);
			}else{
				progressDot.setBackgroundResource(R.drawable.dotwhite);
			}
		}
		
		
		// get the next question bean
		QuestionBean nextQuestion = questions.get(currentQuestionIndex);		
		// set the question body
		TextView questionBodyView = (TextView)findViewById(R.id.quesbody);
		questionBodyView.setText(Html.fromHtml(nextQuestion.getChoiceQuestion().getQuestionBody().getQuestionBodyStr()));		
		// get the question highlighted part holder
		TextView questionBodyHighlightedView = (TextView)findViewById(R.id.quesbodyhighlighted);		
		// get the image holder
		ImageView questionImage = (ImageView)findViewById(R.id.quesimage);	
		if(nextQuestion.getChoiceQuestion().getQuestionBody().getQuestionImage()!=null){
			// set the question image
			AssetManager assetMgr = getAssets();
			try {				
				InputStream is = assetMgr.open("pods/images/" + nextQuestion.getChoiceQuestion().getQuestionBody().getQuestionImage() + ".jpg");			
				questionImage.setImageBitmap(BitmapFactory.decodeStream(is));
							
			} catch (IOException e) {
				Log.e("question", "Image not found");
			}
			questionBodyHighlightedView.getLayoutParams().height=0;
			questionImage.getLayoutParams().height=LayoutParams.WRAP_CONTENT;
		}		
		else{
			// set the question highlighted part
			questionBodyHighlightedView.setText(Html.fromHtml((nextQuestion.getChoiceQuestion().getQuestionBody().getQuestionBodyHighlighted())));
			questionImage.getLayoutParams().height=0;
			questionBodyHighlightedView.getLayoutParams().height=LayoutParams.WRAP_CONTENT;
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
	    selectChoiceButtons = new ArrayList<Button>();
		for(int idx=0;idx<choicesForThisQuestion.size();idx++){
			//get the choice bean object
			QuestionChoiceBean choice = choicesForThisQuestion.get(idx);
			// inflate the choice view layout
			View choiceView = inflater.inflate(R.layout.choice_view, null);			
			//set the button image and choice text
			((TextView)choiceView.findViewById(R.id.choicebody)).setText(Html.fromHtml(choice.getChoiceBody()));
			Button btnSelectChoice = (Button)choiceView.findViewById(R.id.btnselectchoice);
			btnSelectChoice.setBackgroundResource(greyButtonsIds[idx]);
			selectChoiceButtons.add(btnSelectChoice);
			// set the on click listener
			choiceView.setOnClickListener(new ChoiceSelectListner(this,false));	
			btnSelectChoice.setOnClickListener(new ChoiceSelectListner(this, true));
			// set the id as 1 if this is the correct choice
			if(choice.getCorrect().equalsIgnoreCase("true")){				
				choiceView.setTag(1);
				btnSelectChoice.setTag(1);
			}
			// set the id as 0 if this is the wrong choice
			else{
				choiceView.setTag(0);
				btnSelectChoice.setTag(0);
			}
			// add the choice to the choice container
			choiceContainer.addView(choiceView);
		}
		
		
	}	
	
	
	public int[] getGreytButtonIds(){
		return greyButtonsIds;
	}
	
	public int[] getBlueButtonIds(){
		return blueButtonIds;
	}
	
	public int[] getGreenButtonIds(){
		return greenButtonIds;
	}
	
	public List<Button> getSelectChoiceButtons(){
		return selectChoiceButtons; 
	}
	
	public void setCurrentSelectedChoiceCorrect(boolean currentSelectedChoiceCorrect){
		this.isCurrentSelectedChoiceCorrect = currentSelectedChoiceCorrect;
	}
	
	public boolean isCurrentSelectedChoiceCorrect(){
		return isCurrentSelectedChoiceCorrect;
	}
	
	public int getCurrentSelectedChoiceIndex(){
		return currentSelectedChoiceIndex;
	}
	
	public void setCurrentSelectedChoiceIndex(int currentSelectedChoiceIndex){
		this.currentSelectedChoiceIndex = currentSelectedChoiceIndex;
	}
	
	public boolean isCurrentScreenForExplanation(){
		return isCurrentScreenForExplanation;
	}
	
}


 
	
 