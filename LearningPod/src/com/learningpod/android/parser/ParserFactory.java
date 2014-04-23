package com.learningpod.android.parser;

public class ParserFactory {

	public static GenericParser getParser(ParserType type) {
		if (type == ParserType.PROFILE_DETAILS_PARSER) {
			return new ProfileParser();
		}
		else if(type==ParserType.POD_PARSER){
			return new PodParser();
		}
		return null;
	}
}
