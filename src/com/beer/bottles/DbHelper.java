package com.beer.bottles;

import java.sql.SQLException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DbHelper {
	static final String ROWID = "id";
	static final String BEERNAME = "beerName";
	static final String DESCRIPTION = "description";
	static final String BEERIBU = "ibu";
	static final String BEERABV = "abv";
	static final String BEERSTYLE = "style";
	static final String BEERID = "breweryDB_id";
	static final String BEERPOS = "queuePosition";
	static final String TAG = "DatabaseHelper";
	
	static final String DATABASE_NAME = "beers";
	static final String TABLE_NAME = "beerQueue";
	static final int DATABASE_VERSION = 1;
	
	static final String DATABASE_CREATE = "create table beerQueue (id integer primary key autoincrement, " +
			"beerName text not null, description text not null, ibu float not null, abv float not null, " +
			"style text not null, breweryDB_id int not null, queuePosition int not null)";
	
	final Context context;
	
	DatabaseHelper DBH;
	SQLiteDatabase db;
	
	public DbHelper(Context ctx){
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
			db.execSQL("DROP TABLE IF EXISTS beerQueue");
			onCreate(db);
			
			
		}
	}
	
	//Opens the database
	public DbHelper open() throws SQLException{
		db = DBH.getWritableDatabase();
		return this;
	}
	
	//Closes the database
	public void close(){
		DBH.close();
	}
	
	//insert a beer into the database
	public long insertBeer(String name, String description, String ibu, String abv, String style, String beerID, int position){
		ContentValues initialValues = new ContentValues();
		initialValues.put(BEERNAME, name);
		initialValues.put(DESCRIPTION, description);
		initialValues.put(BEERIBU, ibu);
		initialValues.put(BEERABV, abv);
		initialValues.put(BEERSTYLE, style);
		initialValues.put(BEERID, beerID);
		initialValues.put(BEERPOS, position);
		return db.insert(TABLE_NAME, null, initialValues);
	}
	
	public int GetCount(){
		String sql = "SELECT COUNT(*) FROM beerQueue";
		SQLiteStatement statement = db.compileStatement(sql);
		long count = statement.simpleQueryForLong();
		Integer i = (int) (long) count; 
		return i;
	}
	
	public int CheckForBeer(String beerId){
		String sql = "SELECT COUNT(*) FROM beerQueue WHERE " + BEERID + " = '" + beerId + "'";
		SQLiteStatement statement = db.compileStatement(sql);
		long count = statement.simpleQueryForLong();
		Integer i = (int) (long) count;
		return i;
	}
	
	//get max position
	public int getMaxPosition(){
		String sqlPos = "SELECT MAX(queuePosition) FROM beerQueue";
		SQLiteStatement posStatement = db.compileStatement(sqlPos);
		long position = posStatement.simpleQueryForLong();
		Integer posInt = (int) (long) position;
		return posInt;
	}
	
	//deletes a particular beer
	public boolean deleteBeer(long rowId){
		return db.delete(TABLE_NAME, ROWID + "=" + rowId,null) > 0;
	}
	
	//Deletes beer with beerID
	public boolean deleteBeerWithId(String beerId){
		return db.delete(TABLE_NAME, BEERID + "=" + "'" + beerId + "'", null) > 0;
	}
	
	
	
	//retrieves all beers
	public Cursor getAllBeers(){
		return db.query(TABLE_NAME, new String[] {BEERNAME, DESCRIPTION, BEERSTYLE, BEERIBU, BEERABV, BEERID, ROWID, BEERPOS}, null, null, null, null, BEERPOS);
		
	}
	
	//retrieves particular beer
	public Cursor getBeer(long rowId) throws SQLException{
		Cursor mCursor = 
				db.query(true,TABLE_NAME, new String[] {BEERNAME, DESCRIPTION, BEERSTYLE, BEERIBU, BEERABV}, ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	//retrieves beer with BreweryDB ID
	public Cursor getBeerWithId(String beerId) throws SQLException{
		Cursor c = 
				db.query(true, TABLE_NAME, new String[] {BEERNAME, DESCRIPTION, BEERSTYLE, BEERIBU, BEERABV, BEERPOS, ROWID, BEERID}, BEERID + "=" + "'" + beerId + "'", null, null,null, null, null);
		if (c != null){
			c.moveToFirst();
		}
		return c;
	}
	
	//moves beer to the top of the queue
	public boolean updateBeerPos(long rowId, int position){
		ContentValues args = new ContentValues();
		args.put(BEERPOS, position);
		return db.update(TABLE_NAME, args, ROWID + "=" + rowId, null) > 0;
		
	}
	
	//updates all positions on delete
	public boolean updateAllPos(int rowID, int position){
		ContentValues args2 = new ContentValues();
		int newPos = position - 1;
		args2.put(BEERPOS, newPos);
		return db.update(TABLE_NAME, args2, ROWID + "=" + rowID, null) > 0;
	}
	
	//increases position by one
	public boolean increaseByOne(int rowId, int position){
		ContentValues args3 = new ContentValues();
		int newPos2 = position + 1;
		args3.put(BEERPOS, newPos2);
		return db.update(TABLE_NAME, args3, ROWID + "=" + rowId, null) > 0;
	}
	
}
