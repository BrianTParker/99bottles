package com.beer.bottles;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;



public class HomeScreen extends FragmentActivity{
	
	Button submit;
	EditText text;
	String searchString;
	
	PagerAdapter mPagerAdapter;
	TextView welcomeText;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homescreen);
		
		welcomeText = (TextView) findViewById(R.id.welcome);
		submit = (Button) findViewById(R.id.bSubmit);
		text = (EditText) findViewById(R.id.tSearch);
		
		text.setBackgroundColor(Color.WHITE);
		text.setTextColor(Color.parseColor("#000000"));
		// TODO Auto-generated method stub
		
		
		
		
		submit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(text.getText().toString().equals("")){
				}else{
				searchString = text.getText().toString();
				
				
				try{
					Class myClass = Class.forName("com.beer.bottles.Search");
					Intent myIntent = new Intent(HomeScreen.this, myClass);
					myIntent.putExtra("searchString", searchString);		
					startActivity(myIntent);
				}catch(ClassNotFoundException e){
					e.printStackTrace();
				}
				
				}
			}
		});
		
		
		
		this.initialisePaging();
		
	}
	
	
	private void initialisePaging() {

		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, QueueFragment.class.getName()));
		fragments.add(Fragment.instantiate(this, TriedBeersFragment.class.getName()));
		this.mPagerAdapter  = new CustomPagerAdapter(super.getSupportFragmentManager(), fragments);
		//
		ViewPager pager = (ViewPager)super.findViewById(R.id.viewpager);
		pager.setAdapter(this.mPagerAdapter);
		
	}
	
	


//public void DisplayBeer(Cursor c){
	//Toast.makeText(this, "Name: " + c.getString(1) + "\n", Toast.LENGTH_LONG).show();
//}

	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		try{
			Class myClass = Class.forName("com.beer.bottles.Queue");
			Intent myIntent = new Intent(HomeScreen.this, myClass);
				
			startActivity(myIntent);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	
	
	
}
