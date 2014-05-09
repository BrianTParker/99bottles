package com.beer.bottles;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class RecentDetails extends Activity{
	
	TextView nameDisplay;
	TextView descriptionDisplay;
	TextView ibuDisplay;
	TextView abvDisplay;
	TextView styleDisplay;
	TextView wouldDrink;
	TextView comment;
	String[] beerInfo;
	int wouldDrinkInt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recentdetails);
		nameDisplay = (TextView) findViewById(R.id.tvQueueName);
		ibuDisplay = (TextView) findViewById(R.id.tvQueueIbu);
		descriptionDisplay = (TextView) findViewById(R.id.tvQueueDescription);
		abvDisplay = (TextView) findViewById(R.id.tvQueueAbv);
		styleDisplay = (TextView) findViewById(R.id.tvQueueStyle);
		wouldDrink = (TextView) findViewById(R.id.tvWouldDrink);
		comment = (TextView) findViewById(R.id.tvComment);
		
		final String beerId = getIntent().getStringExtra("beerId");
		
		final DrunkDb db = new DrunkDb(this);
		
		try{
			db.open();
			
			beerInfo = new String[7];
			Cursor c = db.getBeerWithId(beerId);
			if (c.moveToFirst()){
				for (int x=0;x<1;x++){
					beerInfo[0] = c.getString(0);//name
					beerInfo[1] = c.getString(1);//Description
					beerInfo[2] = c.getString(2);//Style
					beerInfo[3] = c.getString(3);//IBU
					beerInfo[4] = c.getString(4);//ABV
					
					beerInfo[6] = c.getString(6); //comment
					wouldDrinkInt = c.getInt(5);
					
					
									
				} 
				
			}
			
			db.close();
			
		}catch (java.sql.SQLException e) {
			
			e.printStackTrace();
		}
		System.out.println("Would drink " + wouldDrinkInt);
		nameDisplay.setText(beerInfo[0]);
		descriptionDisplay.setText(beerInfo[1]);
		styleDisplay.setText(beerInfo[2]);
		ibuDisplay.setText(beerInfo[3]);
		abvDisplay.setText(beerInfo[4]);
		if(wouldDrinkInt == 1){
			wouldDrink.setText("Would Drink again!");
			
		}else{
			wouldDrink.setText("I did not care for this beer");
			
		}
		if (!beerInfo[6].equals("")){
			comment.setText("Personal Comments:\n" + beerInfo[6]);
		}
	}

}
