package com.beer.bottles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;





import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Search extends ListActivity{
	
	ListView lv;
	TextView noResultsText;
	
	Request request = new Request(this);
	JSONObject list = new JSONObject();
	boolean noResults = false;
	boolean tooManyResults = false;
	
	String key = "0dcbd00f34baec00a0f14e0b7d667be0";
	
	String type = "beer";
	String charset = "UTF-8";

	
	String[][] beerList;
	BeerObject[] beerObjectList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		String q = getIntent().getStringExtra("searchString");
		q = q.replace(" ", "%20");
		lv = getListView();
		noResultsText = (TextView) findViewById(R.id.tvNoResults);
		String url = "http://api.brewerydb.com/v2/search/?key=" + key + "&q=" + q + "&type=" + type;
		
		//setListAdapter(new ArrayAdapter<String>(Search.this, android.R.layout.simple_list_item_1, beerList));
		//System.out.println(url);
		System.out.println("We're about to call the request");
		request.execute(url);
		
		
	}
//	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String beerName = beerObjectList[position].getName();
		String beerId = beerObjectList[position].getId();
		String beerIbu = beerObjectList[position].getIbu();
		String beerAbv = beerObjectList[position].getAbv();
		String beerStyle = beerObjectList[position].getStyle();
		String beerDescription = beerObjectList[position].getDescription();
		String[] beerArray = {beerId, beerName, beerDescription,beerIbu, beerAbv, beerStyle };
		try{
			Class myClass = Class.forName("com.beer.bottles.Details");
			Intent myIntent = new Intent(Search.this, myClass);
			myIntent.putExtra("beerArray", beerArray);		
			startActivity(myIntent);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		
	}
//	
//	
//	
//
//
//
public class Request extends AsyncTask<String, String, BeerObject[]>{
		private final Context _context;
		private ProgressDialog progressDialog;
		
		
		
		public Request(Context context){
			_context = context;
		}
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(this._context);
			progressDialog.setMessage("Searching...");
			progressDialog.setCancelable(false);
	        progressDialog.setIndeterminate(true);

			progressDialog.show();
			
			
		}


		protected void onProgressUpdate(String... beers){
			
			progressDialog.cancel();	
			if (noResults == false && tooManyResults == false){
			lv.setAdapter(new ArrayAdapter<String>(Search.this, android.R.layout.simple_list_item_1, beers));
			}else if (noResults == true){
				noResultsText.setText("Sorry, we couldn't find any beers with that name.  Make sure you spelled it correctly");
			}else if (tooManyResults == true){
				noResultsText.setText("That search is too broad.  Please be more specific.");
			}
				
		}
			
			
		
		
		@Override
		protected BeerObject[] doInBackground(String... urls){
			JSONObject newList = new JSONObject();
			BeerObject[] beerObjectArray = null;
			String[] beers = null;
			String[] additionalUrls = null;
			String additionalUrl = null;
			int x = 0;
			
			
			
			try{
				for (int i=0; i<urls.length; i++){
					
					URL json = new URL(urls[i]);
					additionalUrl = urls[i];
					URLConnection tc = json.openConnection();
					tc.setRequestProperty("Accept-Charset", charset);
					BufferedReader in = new BufferedReader(new InputStreamReader(tc.getInputStream()));
					//InputStream in = tc.getInputStream();
					//JSONObject list = new JSONObject(in.toString());
					
					String line;
					while((line = in.readLine()) != null){
						list = new JSONObject(line);
					}
					
					if(list.length() >= 4){
					additionalUrls = new String[list.getInt("numberOfPages")];
					for(int b=0; b<list.getInt("numberOfPages");b++){
						additionalUrls[b] = additionalUrl + "&p=" + (b+1);
					}
					int totalLength = list.getJSONArray("data").length() * list.getInt("numberOfPages");
					if (totalLength > 50){
						tooManyResults = true;
					}
					//used for pushing beer data to the details activity
					beerObjectArray = new BeerObject[totalLength];
					for(int p = 0; p < totalLength; p++){
						beerObjectArray[p] = new BeerObject();
					}
					
					//list of names used to populate the beer list on progress update
					beers = new String[totalLength];
					}else{
						
						noResults = true;
					}
				}
				
				
				
				if(list.length() >= 4 && tooManyResults == false){
				for (int y=0;y<additionalUrls.length;y++){
					
					URL newJson = new URL(additionalUrls[y]);
					
					URLConnection con = newJson.openConnection();
					con.setRequestProperty("Accept-Charset", charset);
					BufferedReader newIn = new BufferedReader(new InputStreamReader(con.getInputStream()));
					//InputStream in = tc.getInputStream();
					//JSONObject list = new JSONObject(in.toString());
					
					String newLine;
					while((newLine = newIn.readLine()) != null){
						newList = new JSONObject(newLine);
						
					}
				
				if (newList != null){
					
					
					
					for (int z=0; z<newList.getJSONArray("data").length();z++){
						
						
						beers[x] = newList.getJSONArray("data").getJSONObject(z).getString("name").toString();
						try{
							Object styleObject = newList.getJSONArray("data").getJSONObject(1).get("style");
							JSONObject jsonStyle = new JSONObject(styleObject.toString());
							
							beerObjectArray[x].setStyle(jsonStyle.getString("name").toString());
							
						}catch(JSONException e){
							
							beerObjectArray[x].setStyle("No style information available");
						}
						
						beerObjectArray[x].setId(newList.getJSONArray("data").getJSONObject(z).getString("id").toString());
						
						beerObjectArray[x].setName(newList.getJSONArray("data").getJSONObject(z).getString("name").toString());
						
						try{
						beerObjectArray[x].setIbu(newList.getJSONArray("data").getJSONObject(z).getString("ibu").toString());
						}catch(JSONException e){
							beerObjectArray[x].setIbu("N/A");
						}
						try{
							beerObjectArray[x].setAbv(newList.getJSONArray("data").getJSONObject(z).getString("abv").toString());
						}catch(JSONException e){
							beerObjectArray[x].setAbv("N/A");
						}
						
						
						try{
							beerObjectArray[x].setDescription(newList.getJSONArray("data").getJSONObject(z).getString("description").toString());
						}catch(JSONException e){
							beerObjectArray[x].setDescription("No Description Available");
						}
						
						x++;
					}
				}}}
					publishProgress(beers);
				
				
			
			}catch (MalformedURLException e){
				e.printStackTrace();
			}catch (IOException e){
				e.printStackTrace();
			}catch (JSONException e) {
				e.printStackTrace();
			}
			
				
			return beerObjectArray;
				
		}
				




		@Override
		protected void onPostExecute(BeerObject[] result) {
			
			// TODO Auto-generated method stub
			Search search = (Search) _context;
			search.beerObjectList = result;
			super.onPostExecute(result);
			
			
			


			
		}

		
	
	
	}
	

}
