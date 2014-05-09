package com.beer.bottles;

import java.sql.SQLException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;



public class DrunkDb {
	
	static final String ROWID = "id";
	static final String BEERNAME = "beerName";
	static final String DESCRIPTION = "description";
	static final String BEERIBU = "ibu";
	static final String BEERABV = "abv";
	static final String BEERSTYLE = "style";
	static final String BEERID = "breweryDB_id";
	static final String WOULDDRINK = "wouldDrinkAgain";
	static final String COMMENTS = "comments";
	static final String TAG = "DrunkDatabaseHelper";
	
	static final String DATABASE_NAME = "drunkBeers";
	static final String TABLE_NAME = "drunkList";
	static final int DATABASE_VERSION = 1;
	
	static final String DATABASE_CREATE = "create table drunkList (id integer primary key autoincrement, " +
			"beerName text not null, description text not null, ibu float not null, abv float not null, " +
			"style text not null, breweryDB_id text not null, wouldDrinkAgain int not null, comments text)";
	
final Context context;
	
	DatabaseHelper DBH;
	SQLiteDatabase db;
	
	public DrunkDb(Context ctx){
		this.context = ctx;
		DBH = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		DatabaseHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
			/*try {
				db.execSQL(DATABASE_CREATE);
			}catch (SQLException e){
				throw new RuntimeException(e);
			}*/
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " +
					newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS drunkList");
			onCreate(db);
			
			
		}
	}
	
	//Opens the database
	public DrunkDb open() throws SQLException{
		db = DBH.getWritableDatabase();
		return this;
	}
	
	//Closes the database
	public void close(){
		DBH.close();
	}

	//insert a beer into the database
	public long insertBeer(String name, String description, String ibu, String abv, String style, String beerID, int wouldDrink, String comments){
		ContentValues initialValues = new ContentValues();
		initialValues.put(BEERNAME, name);
		initialValues.put(DESCRIPTION, description);
		initialValues.put(BEERIBU, ibu);
		initialValues.put(BEERABV, abv);
		initialValues.put(BEERSTYLE, style);
		initialValues.put(BEERID, beerID);
		initialValues.put(WOULDDRINK, wouldDrink);
		initialValues.put(COMMENTS, comments);
		return db.insert(TABLE_NAME, null, initialValues);
	}
	
	
	public int GetCount(){
		String sql = "SELECT COUNT(*) FROM drunkList";
		SQLiteStatement statement = db.compileStatement(sql);
		long count = statement.simpleQueryForLong();
		Integer i = (int) (long) count; 
		return i;
	}
	
	//retrieves beer with BreweryDB ID
	public Cursor getBeerWithId(String beerId) throws SQLException{
		Cursor c = 
				db.query(true, TABLE_NAME, new String[] {BEERNAME, DESCRIPTION, BEERSTYLE, BEERIBU, BEERABV, WOULDDRINK, COMMENTS, ROWID}, BEERID + "=" + "'" + beerId + "'", null, null,null, null, null);
		if (c != null){
			c.moveToFirst();
		}
		return c;
	}
	
	//retrieves all beers
	public Cursor getAllBeers(){
		return db.query(TABLE_NAME, new String[] {BEERNAME, DESCRIPTION, BEERSTYLE, BEERIBU, BEERABV, BEERID, ROWID, WOULDDRINK, COMMENTS}, null, null, null, null, ROWID + " desc");
		
	}


}
