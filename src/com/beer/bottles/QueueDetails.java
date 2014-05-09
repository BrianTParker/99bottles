package com.beer.bottles;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class QueueDetails extends Activity{
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		try{
			Class myClass = Class.forName("com.beer.bottles.HomeScreen");
			Intent myIntent = new Intent(QueueDetails.this, myClass);
				
			startActivity(myIntent);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}


	TextView nameDisplay;
	TextView descriptionDisplay;
	TextView ibuDisplay;
	TextView abvDisplay;
	TextView styleDisplay;
	Button triedIt;
	Button deleteBeer;
	Button moveToTop;
	ImageButton trash;
	//String beerId;
	int rowId;
	int beerPosition;
	String[] updateQueue;
	int[] Ids;
	int[] positions;
	AlertDialog.Builder builder;
	AlertDialog.Builder builder2;
	AlertDialog.Builder deleteBuilder;
	int wouldDrinkAgain = 0;
	String comment = "";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		builder = new AlertDialog.Builder(this);
		builder2 = new AlertDialog.Builder(this);
		deleteBuilder = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		
		
		
		
		
		final String beerId = getIntent().getStringExtra("beerId");
		String[] beerInfo = new String[5];
		setContentView(R.layout.queuedetails);
		trash = (ImageButton) findViewById(R.id.trashcan);
		//deleteBeer = (Button) findViewById(R.id.bDelete);
		triedIt = (Button) findViewById(R.id.bTriedIt);
		nameDisplay = (TextView) findViewById(R.id.tvQueueName);
		ibuDisplay = (TextView) findViewById(R.id.tvQueueIbu);
		descriptionDisplay = (TextView) findViewById(R.id.tvQueueDescription);
		abvDisplay = (TextView) findViewById(R.id.tvQueueAbv);
		styleDisplay = (TextView) findViewById(R.id.tvQueueStyle);
		moveToTop = (Button) findViewById(R.id.bMoveToTop);
		trash.setBackgroundColor(color.transparent);
		
		
		
		final DbHelper db1 = new DbHelper(this);
		final DrunkDb db2 = new DrunkDb(this);
		
		try {
			db1.open();
			
			
			Cursor c = db1.getBeerWithId(beerId);
			if (c.moveToFirst()){
				for (int x=0;x<1;x++){
					beerInfo[0] = c.getString(0);//name
					beerInfo[1] = c.getString(1);//Description
					beerInfo[2] = c.getString(2);//Style
					beerInfo[3] = c.getString(3);//IBU
					beerInfo[4] = c.getString(4);//ABV
					
					beerPosition = c.getInt(5);//queue position
					rowId = c.getInt(6);//row id
									
				} 
				
			}
			
			db1.close();
			
			
		} catch (java.sql.SQLException e1) {
			e1.printStackTrace();
		}
		
		final String beerName = beerInfo[0];
		final String beerDescription = beerInfo[1];
		final String beerStyle = beerInfo[2];
		final String beerIBU = beerInfo[3];
		final String beerABV = beerInfo[4];
		
		
		
		nameDisplay.setText(beerInfo[0]);
		descriptionDisplay.setText(beerInfo[1]);
		styleDisplay.setText(beerInfo[2]);
		ibuDisplay.setText("IBU: " + beerInfo[3]);
		abvDisplay.setText("ABV: " + beerInfo[4]);
		
		moveToTop.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				int count2 = 0;
				
				try{
					db1.open();
					count2 = db1.GetCount();
					if(count2 > 0){
						Cursor c = db1.getAllBeers();
						Ids = new int[count2];
						positions = new int[count2];
						if (c.moveToFirst()){
							
							for (int x=0;x<count2;x++){
								
								Ids[x] = c.getInt(6);// row id
								positions[x] = c.getInt(7);//position
								db1.increaseByOne(Ids[x], positions[x]);
								
								c.moveToNext();
								}
								
								
								
								
							} 
							
						}
						
					
					db1.updateBeerPos(rowId, 1);
				}catch (java.sql.SQLException e) {
					
					e.printStackTrace();
				}
				try{
					Class myClass = Class.forName("com.beer.bottles.HomeScreen");
					Intent myIntent = new Intent(QueueDetails.this, myClass);
						
					startActivity(myIntent);
				}catch(ClassNotFoundException e){
					e.printStackTrace();
				}
				
			}
		});
		
		
		triedIt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				builder.setMessage("What did you think?")
				   //.setCancelable(false)
				   .setPositiveButton("Would Drink Again", new DialogInterface.OnClickListener() {
					   public void onClick(DialogInterface dialog, int id){
						   wouldDrinkAgain = 1;
						   AlertDialog alert2 = builder2.create();
						   alert2.show();
						   dialog.cancel();
					   }
				   })
				   .setNegativeButton("Didn't care for it", new DialogInterface.OnClickListener() {
					   public void onClick(DialogInterface dialog, int id){
						   AlertDialog alert2 = builder2.create();
						   alert2.show();
						   dialog.cancel();
					   }
				   });
				
				builder2.setMessage("Would you like to add a comment?")
				   .setView(input)
				   //.setCancelable(false)
				   .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					   public void onClick(DialogInterface dialog, int id){
						   
						   comment = input.getText().toString();
						   
						   try{
								db2.open();
								
								
								long insert = db2.insertBeer(beerName, beerDescription, beerIBU, beerABV, beerStyle, beerId,wouldDrinkAgain, comment);
								
								db2.close();
								db1.open();
								db1.deleteBeerWithId(beerId);
								db1.close();
								}catch (java.sql.SQLException e) {					
								e.printStackTrace();
								}
						   dialog.cancel();
						   try{
								Class myClass = Class.forName("com.beer.bottles.HomeScreen");
								Intent myIntent = new Intent(QueueDetails.this, myClass);
									
								startActivity(myIntent);
							}catch(ClassNotFoundException e){
								e.printStackTrace();
							}
					   }
				   })
				   .setNegativeButton("No", new DialogInterface.OnClickListener() {
					   public void onClick(DialogInterface dialog, int id){
						   
						   
						   try{
								db2.open();
								
								
								long insert = db2.insertBeer(beerName, beerDescription, beerIBU, beerABV, beerStyle, beerId,wouldDrinkAgain, comment);
								
								db2.close();
								db1.open();
								db1.deleteBeerWithId(beerId);
								db1.close();
								}catch (java.sql.SQLException e) {					
								e.printStackTrace();
								}
						   dialog.cancel();
						   try{
								Class myClass = Class.forName("com.beer.bottles.HomeScreen");
								Intent myIntent = new Intent(QueueDetails.this, myClass);
									
								startActivity(myIntent);
							}catch(ClassNotFoundException e){
								e.printStackTrace();
							}
					   }
				   });
				
				AlertDialog alert = builder.create();			
				alert.show();	
				}
		});
		
		trash.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				

				deleteBuilder.setMessage("Are you sure you want to remove this beer without storing feedback?")
				   //.setCancelable(false)
				   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					   public void onClick(DialogInterface dialog, int id){
						   int count = 0;
							
							try {
								db1.open();
								db1.deleteBeerWithId(beerId);
								count = db1.GetCount();
								
								if (count > 0){
									Ids = new int[count];
									positions = new int[count];
									Cursor c = db1.getAllBeers();
									if (c.moveToFirst()){
										
										for (int x=0;x<count;x++){
											
											Ids[x] = c.getInt(6);// row id
											positions[x] = c.getInt(7);//position
											if(positions[x] > beerPosition){
												
												db1.updateAllPos(Ids[x], positions[x]);
											}
											c.moveToNext();
											
											
											
										} 
										
									}
									}
								db1.close();
							} catch (java.sql.SQLException e) {
								
								e.printStackTrace();
							}
							
							try{
								Class myClass = Class.forName("com.beer.bottles.HomeScreen");
								Intent myIntent = new Intent(QueueDetails.this, myClass);
									
								startActivity(myIntent);
							}catch(ClassNotFoundException e){
								e.printStackTrace();
							}
						   
					   }
				   })
				   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					   public void onClick(DialogInterface dialog, int id){
						   dialog.cancel();
					   }
				   });
				
				AlertDialog alert = deleteBuilder.create();			
				alert.show();
				
			}
			
			
				
			});
		
		
	}
	
	

}
