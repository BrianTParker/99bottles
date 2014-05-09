package com.beer.bottles;



import com.beer.bottles.RecentBeersTest.MyCustomAdapter;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Queue extends ListActivity {
	Button submit;
	EditText text;
	String searchString;
	String[] queueList;
	String[] beerIds;
	
	TextView welcomeText;
	Button recentBeers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.queue);
		//lv = getListView();
		recentBeers = (Button) findViewById(R.id.bRecent);
		welcomeText = (TextView) findViewById(R.id.welcome);
		submit = (Button) findViewById(R.id.bSubmit);
		text = (EditText) findViewById(R.id.tSearch);
		
		text.setBackgroundColor(Color.WHITE);
		text.setTextColor(Color.parseColor("#000000"));
		
		
		DbHelper db1 = new DbHelper(this);
		int count = 0;
		
		
		int count2 = 0;
		
		
		try {
			db1.open();
			count = db1.GetCount();
			
			
			queueList = new String[count];
			beerIds = new String[count];
			
			Cursor c = db1.getAllBeers();
			if (c.moveToFirst() && count > 0){
				for (int x=0;x<count;x++){
					queueList[x] = c.getString(0);
					beerIds[x] = c.getString(5);
					c.moveToNext();
					
					
					
				} 
				
			}
			
			db1.close();
			
			
		} catch (java.sql.SQLException e1) {
			e1.printStackTrace();
		}
		
		if(queueList.length > 0){
		//lv.setAdapter(new ArrayAdapter<String>(Queue.this, android.R.layout.simple_list_item_1, queueList));
			setListAdapter(new MyCustomAdapter(Queue.this, R.layout.triedbeerrow, queueList));
		}else{
			
			welcomeText.setText("Your queue is empty!  Search for beers to populate the queue");
		}
		
		
		
		submit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(text.getText().toString().equals("")){
				}else{
				searchString = text.getText().toString();
				
				
				try{
					Class myClass = Class.forName("com.beer.bottles.Search");
					Intent myIntent = new Intent(Queue.this, myClass);
					myIntent.putExtra("searchString", searchString);		
					startActivity(myIntent);
				}catch(ClassNotFoundException e){
					e.printStackTrace();
				}
				
				}
			}
		});
		
		recentBeers.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					Class myClass = Class.forName("com.beer.bottles.RecentBeersTest");
					Intent myIntent = new Intent(Queue.this, myClass);
					startActivity(myIntent);
				}catch(ClassNotFoundException e){
					e.printStackTrace();
				}
				
			}
		});

	}
	//public void DisplayBeer(Cursor c){
		//Toast.makeText(this, "Name: " + c.getString(1) + "\n", Toast.LENGTH_LONG).show();
	//}

	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String Id = beerIds[position];
		try{
			Class myClass = Class.forName("com.beer.bottles.QueueDetails");
			Intent myIntent = new Intent(Queue.this, myClass);
			myIntent.putExtra("beerId", Id);		
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
			Intent myIntent = new Intent(Queue.this, myClass);
				
			startActivity(myIntent);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
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
			label.setText(queueList[position]);
			
			
			return row;
			
			
			
			
		}
		
		
		
	}


	
	
	

}
