package com.beer.bottles;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchFields extends Activity{
	Button submit;
	EditText nameField;
	EditText ibuField;
	EditText abvField;
	String searchString;
	String nameString = "";
	String ibuString = "";
	String abvString = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		submit = (Button) findViewById(R.id.bSubmit);
		nameField = (EditText) findViewById(R.id.nameSearch);
		ibuField = (EditText) findViewById(R.id.ibuSearch);
		abvField = (EditText) findViewById(R.id.abvSearch);
		nameField.setBackgroundColor(Color.WHITE);
		nameField.setTextColor(Color.parseColor("#000000"));
		ibuField.setBackgroundColor(Color.WHITE);
		ibuField.setTextColor(Color.parseColor("#000000"));
		abvField.setBackgroundColor(Color.WHITE);
		abvField.setTextColor(Color.parseColor("#000000"));
		submit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(nameField.getText().toString().equals("") && ibuField.getText().toString().equals("") && abvField.getText().toString().equals("")){
					
				}else{
					if(!nameField.getText().toString().equals("")){
						nameString = nameField.getText().toString();
					}
					if(!ibuField.getText().toString().equals("")){
						ibuString = ibuField.getText().toString();
					}
					if(!abvField.getText().toString().equals("")){
						abvString = abvField.getText().toString();
					}
					String[] extraArray = new String[3];
					extraArray[0] = nameString;
					extraArray[1] = ibuString;
					extraArray[2] = abvString;
					
					try{
						Class myClass = Class.forName("com.beer.bottles.Search");
						Intent myIntent = new Intent(SearchFields.this, myClass);
						myIntent.putExtra("searchArray", extraArray);		
						startActivity(myIntent);
					}catch(ClassNotFoundException e){
						e.printStackTrace();
					}
				}
			}

		});
		
	}

}
