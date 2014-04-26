package com.learningpod.android.parser;



import com.learningpod.android.beans.questions.QuestionBodyBean;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
 

public class QuestionBodyConverter  implements Converter {

	
	@Override
	public boolean canConvert(Class cl) {
		return true;
	}

	@Override
	public void marshal(Object arg0, HierarchicalStreamWriter arg1,
			MarshallingContext arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		// TODO Auto-generated method stub
		QuestionBodyBean body = new QuestionBodyBean();
		String questionBody = "";
		String parentNodeName = reader.getNodeName();
		boolean flag  = true;
		while(flag){
			String nodeName = reader.getNodeName();
			String nodeValue = reader.getValue().trim().replace("\n", "");
			if(nodeName.equals("p") && !nodeValue.equalsIgnoreCase("")){
				questionBody = questionBody + nodeValue;
			}
			
			if(nodeName.equals("i") && !nodeValue.equalsIgnoreCase("")){
				questionBody = questionBody + " <i>" + nodeValue + "</i>";
			}
			
			if(nodeName.equalsIgnoreCase("img")){
				body.setQuestionImage(reader.getAttribute(0));
			}
			if(reader.hasMoreChildren()){
				reader.moveDown();
			}else{
				if(reader.getNodeName().equalsIgnoreCase(parentNodeName)){
					break;
				}
				reader.moveUp();
			}
		}
		body.setQuestionBodyStr(questionBody);
		return body;	
	}
	
	

	

}
