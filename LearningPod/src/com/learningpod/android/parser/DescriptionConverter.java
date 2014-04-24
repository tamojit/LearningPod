package com.learningpod.android.parser;

import android.util.Log;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
 

public class DescriptionConverter  implements Converter {

	private boolean canConvert = true;
	@Override
	public boolean canConvert(Class arg0) {
		// TODO Auto-generated method stub
		return canConvert;
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
		if(reader.hasMoreChildren()){
			reader.moveDown();
			canConvert =false;
			return reader.getValue();			
		}
		return null;		
		
		
	}

	

}
