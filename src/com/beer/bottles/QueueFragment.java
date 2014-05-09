package com.beer.bottles;



//import com.beer.bottles.RecentBeers.MyCustomAdapter;


import com.beer.bottles.Queue.MyCustomAdapter;

import android.support.v4.app.ListFragment;
import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;





public class QueueFragment extends ListFragment {
	
	
	String searchString;
	String[] queueList;
	String[] beerIds;
	TextView emptyText;
	
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		this.setListAdapter(new MyCustomAdapter(getActivity(), R.layout.triedbeerrow, queueList));
		
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		
	   return inflater.inflate(R.layout.queuetest, container, false);
		
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//lv = getListView();
		
		
		//getActivity().setContentView(R.layout.queuetest);
		
		DbHelper db1 = new DbHelper(getActivity());
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
		
		//mAdapter = new MyCustomAdapter(getActivity(), R.layout.triedbeerrow, queueList);
		/*if(queueList.length > 0){
		//lv.setAdapter(new ArrayAdapter<String>(Queue.this, android.R.layout.simple_list_item_1, queueList));
			setListAdapter(new MyCustomAdapter(getActivity(), R.layout.triedbeerrow, queueList));
		}else{
			queueList = new String[1];
			queueList[0] = "There are no beers in your queue.  Search to find beers.";
			setListAdapter(new MyCustomAdapter(getActivity(), R.layout.triedbeerrow, queueList));
		}*/
		
		
		
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String Id = beerIds[position];
		try{
			Class myClass = Class.forName("com.beer.bottles.QueueDetails");
			Intent myIntent = new Intent(getActivity(), myClass);
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
			
			
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View row = inflater.inflate(R.layout.triedbeerrow, parent, false);
			TextView label = (TextView) row.findViewById(R.id.beerName);
			label.setText(queueList[position]);
			
			
			return row;
			
			
			
			
		}
		
		
		
	}


}