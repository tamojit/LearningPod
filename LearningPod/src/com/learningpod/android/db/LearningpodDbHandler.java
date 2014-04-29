package com.learningpod.android.db;

import java.io.File;

import com.learningpod.android.beans.UserProgressInfo;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LearningpodDbHandler {

	private LearningpodSqliteHelper dbHelper;
	private SQLiteDatabase database;
	private Context context;
	
	public LearningpodDbHandler(Context context){
		dbHelper = new LearningpodSqliteHelper(context);
		this.context = context;
	}
	
	public static boolean doesDatabaseExist(Context context) {
	    File dbFile=context.getDatabasePath(LearningpodSqliteHelper.DATABASE_NAME);
	    return dbFile.exists();
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void deleteAllDataFromDB(){
		database.delete(LearningpodSqliteHelper.USER_PROGRESS_TRACKER, null, null);
	}
	public void close() {
		dbHelper.close();
	}
	
	public void saveUserProgressInfo(UserProgressInfo progressInfo){
		
	}
	
}
