package sample.application.fingerpaint;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
*  DBをオープンする 
*/
public class MemoDBHelper extends SQLiteOpenHelper {
	
	public static final String name = "memos.db";
	public static final Integer version = 1;
	public static final CursorFactory factory = null;

	public MemoDBHelper(Context context, String name, CursorFactory factory,
			Integer version) {
		super(context, name, factory, version);
	}
	
	public MemoDBHelper(Context context) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE memoDB ("
				   + android.provider.BaseColumns._ID
				   + " INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, memo TEXT);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	

}
