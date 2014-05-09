package com.beer.bottles;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RecentBeers extends ListActivity{
	
	String[] triedBeers;
	String[] triedBeerIds;
	int[] wouldDrink;
	//ListView lv;
	TextView emptyText;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.recentbeers);
		//lv = getListView();
		emptyText = (TextView) findViewById(R.id.emptyText);
		int count;
		DrunkDb db = new DrunkDb(this);
		
		try{
			db.open();
			count = db.GetCount();
			wouldDrink = new int[count];
			triedBeers = new String[count];
			triedBeerIds = new String[count];
			Cursor c = db.getAllBeers();
			if (c.moveToFirst() && count > 0){
				for (int x=0;x<count;x++){
					triedBeers[x] = c.getString(0);
					triedBeerIds[x] = c.getString(5);
					wouldDrink[x] = c.getInt(7);
					c.moveToNext();
										
				} 
				
			}
			db.close();
			
			
		}catch (java.sql.SQLException e) {
			
			e.printStackTrace();
		}
		
		if(triedBeers.length > 0){
			//lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, triedBeers));
			setListAdapter(new MyCustomAdapter(RecentBeers.this, R.layout.triedbeerrow, triedBeers));
		}else{
			setContentView(R.layout.recentbeers);
			emptyText.setText("You haven't tried any beers yet.");
		}
		
		
	}
	
public class MyCustomAdapter extends ArrayAdapter<String>{
		
		public MyCustomAdapter(Context context, int textViewResourceId, String[] objects){
			
			super(context, textViewResourceId, objects);
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			//return super.getView(position, convertView, parent);
			
			
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.triedbeerrow, parent, false);
			TextView label = (TextView) row.findViewById(R.id.beerName);
			label.setText(triedBeers[position]);
			ImageView icon = (ImageView) row.findViewById(R.id.icon);
			if (wouldDrink[position] == 0){
				icon.setImageResource(R.drawable.thumbs_down);
			}else{
				icon.setImageResource(R.drawable.thumbs_up);
			}
			
			return row;
			
			
			
			
		}
		
		
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		String beerId = triedBeerIds[position];
		try{
			Class myClass = Class.forName("com.beer.bottles.RecentDetails");
			Intent myIntent = new Intent(RecentBeers.this, myClass);
			myIntent.putExtra("beerId", beerId);		
			startActivity(myIntent);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		try{
			Class myClass = Class.forName("com.beer.bottles.Queue");
			Intent myIntent = new Intent(RecentBeers.this, myClass);
				
			startActivity(myIntent);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}

}
