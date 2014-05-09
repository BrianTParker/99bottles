package com.beer.bottles;






import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Details extends Activity{
	
	TextView nameDisplay;
	TextView descriptionDisplay;
	TextView ibuDisplay;
	TextView abvDisplay;
	TextView styleDisplay;
	Button addBeer;
	String beerID;
	String beerName = "";
	String beerDescription;
	String beerIbu;
	String beerAbv;
	String beerStyle;
	AlertDialog alertDialog;
	AlertDialog.Builder builder;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		builder = new AlertDialog.Builder(this);
		builder.setMessage("This beer is already in your queue")
			   .setCancelable(false)
			   .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int id){
					   dialog.cancel();
				   }
			   });
		String[] beerArray = getIntent().getStringArrayExtra("beerArray");
		beerID = beerArray[0];
		beerName = beerArray[1];
		beerDescription = beerArray[2];
		beerIbu = beerArray[3];
		beerAbv = beerArray[4];
		beerStyle = beerArray[5];
		setContentView(R.layout.details);
		addBeer = (Button) findViewById(R.id.bAdd);
		nameDisplay = (TextView) findViewById(R.id.tvName);
		descriptionDisplay = (TextView) findViewById(R.id.tvDescription);
		ibuDisplay = (TextView) findViewById(R.id.tvIbu);
		abvDisplay = (TextView) findViewById(R.id.tvAbv);
		styleDisplay = (TextView) findViewById(R.id.tvStyle);
		
		nameDisplay.setText(beerName);
		descriptionDisplay.setText(beerDescription);
		ibuDisplay.setText("IBU: " + beerIbu);
		abvDisplay.setText("ABV: " + beerAbv);
		styleDisplay.setText(beerStyle);
		
		
		
		
		
		addBeer.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				DbHelper db = new DbHelper(Details.this);
				if(!beerName.equals("")){				
					try {
						db.open();
						
						int count = db.CheckForBeer(beerID);
						if(count > 0){
							
							AlertDialog alert = builder.create();
							alert.show();
							db.close();
							
						}else{
						int position = db.getMaxPosition();
						if(!(position > 0)){
							position = 1;
						}else{
							position = position + 1;
						}
						long id = db.insertBeer(beerName, beerDescription, beerIbu, beerAbv, beerStyle, beerID,position);
						
						
						db.close();
						try{
							Class myClass = Class.forName("com.beer.bottles.HomeScreen");
							Intent myIntent = new Intent(Details.this, myClass);
								
							startActivity(myIntent);
						}catch(ClassNotFoundException e){
							e.printStackTrace();
						}}
							
					} catch (java.sql.SQLException e) {
						
						e.printStackTrace();
					}
				}
				
							
				
			}
		});
	}
	

}
