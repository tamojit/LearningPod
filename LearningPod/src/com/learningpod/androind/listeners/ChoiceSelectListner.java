package com.learningpod.androind.listeners;

import java.util.List;

import com.learningpod.android.R;
import com.learningpod.android.activities.PodQuestionActivity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChoiceSelectListner implements OnClickListener{

	private PodQuestionActivity activity;
	private boolean isButtonClicked;
	public ChoiceSelectListner(PodQuestionActivity activity, boolean isButtonClicked){
		this.activity = activity;
		this.isButtonClicked = isButtonClicked;
	}
	
	@Override
	public void onClick(View v) {
		// if the screen is in explanation mode, don't allow any action
		if(activity.isCurrentScreenForExplanation()){
			return;
		} 
		// enable the submit button
		((Button)activity.findViewById(R.id.btnsubmitnext)).setEnabled(true);
		
		if(!isButtonClicked){ // listener has been invoked by clicking on the full view
			// go through each button in the list
			List<Button> selectChoiceButtons = activity.getSelectChoiceButtons();
			for(int idx=0;idx<selectChoiceButtons.size();idx++){
				Button btn = selectChoiceButtons.get(idx);
				
				if(btn==v.findViewById(R.id.btnselectchoice)){
					btn.setBackgroundResource(activity.getBlueButtonIds()[idx]);
					activity.setCurrentSelectedChoiceIndex(idx);
					if(v.getTag()==Integer.valueOf(1)){
						activity.setCurrentSelectedChoiceCorrect(true);
					}else{
						activity.setCurrentSelectedChoiceCorrect(false);
					}
				}else{
					btn.setBackgroundResource(activity.getGreytButtonIds()[idx]);
				}
			}
		}
		else { // listener has been invoked by clicking on the button 
			// go through each button in the list
			List<Button> selectChoiceButtons = activity.getSelectChoiceButtons();
			for(int idx=0;idx<selectChoiceButtons.size();idx++){
				Button btn = selectChoiceButtons.get(idx);
				
				if(btn==v){
					btn.setBackgroundResource(activity.getBlueButtonIds()[idx]);
					activity.setCurrentSelectedChoiceIndex(idx);
					if(v.getTag()==Integer.valueOf(1)){
						activity.setCurrentSelectedChoiceCorrect(true);
					}else{
						activity.setCurrentSelectedChoiceCorrect(false);
					}
					
				}else{
					btn.setBackgroundResource(activity.getGreytButtonIds()[idx]);
				}
			}
		}
	
	}

}
