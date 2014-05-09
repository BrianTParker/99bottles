package com.beer.bottles;


import com.beer.bottles.Queue.MyCustomAdapter;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Queue_fragment extends ListFragment{
	
	Button submit;
	EditText text;
	String searchString;
	String[] queueList;
	String[] beerIds;
	Context context = getActivity().getApplicationContext();
	TextView welcomeText;
	Button recentBeers;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		DbHelper db1 = new DbHelper(context);
		int count;
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
				setListAdapter(new MyCustomAdapter(context, R.layout.triedbeerrow, queueList));
			}else{
				
				welcomeText.setText("Your queue is empty!  Search for beers to populate the queue");
			}
		
		
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String Id = beerIds[position];
		try{
			Class myClass = Class.forName("com.beer.bottles.QueueDetails");
			Intent myIntent = new Intent(context, myClass);
			myIntent.putExtra("beerId", Id);		
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
			
			
			LayoutInflater inflater = (LayoutInflater)context.getSystemService
				      (Context.LAYOUT_INFLATER_SERVICE);
			View row = inflater.inflate(R.layout.triedbeerrow, parent, false);
			TextView label = (TextView) row.findViewById(R.id.beerName);
			label.setText(queueList[position]);
			
			
			return row;
			
			
			
			
		}
		
		
		
	}
	
}


