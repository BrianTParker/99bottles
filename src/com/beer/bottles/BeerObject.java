package com.beer.bottles;

public class BeerObject {
	
	
	
	private String name;
	private String id;
	private String ibu;
	private String abv;
	private String style;
	private String description;
	
	
	BeerObject(){
		name = "";
		id = "";
		ibu = "";
		abv = "";
		style = "";
		description = "";
		
	}
	BeerObject(String inName, String inId, String inIbu, String inAbv, String inStyle, String inDescription){
		
		name = inName;
		id = inId;
		ibu = inIbu;
		abv = inAbv;
		style = inStyle;
		description = inDescription;
		
	}
	
	
	//get methods
	String getName(){
		return name;
	}
	
	String getId(){
		return id;
	}
	
	String getIbu(){
		
		return ibu;
	}
	
	String getAbv(){
		return abv;
	}
	
	String getStyle(){
		return style;
	}
	
	String getDescription(){
		return description;
	}
	
	//set methods
	void setName(String inName){
		name = inName;
	}
	
	void setId(String inId){
		id = inId;
	}
	
	void setIbu(String inIbu){
		ibu = inIbu;
	}
	
	void setAbv(String inAbv){
		abv = inAbv;
	}
	
	void setStyle(String inStyle){
		style = inStyle;
	}
	
	void setDescription(String inDescription){
		description = inDescription;
	}

}
