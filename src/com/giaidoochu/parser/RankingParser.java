package com.giaidoochu.parser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.JsonReader;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class RankingParser {
	
	   private String id = "id";
	   private String iddevice = "iddevice";
	   private String namedevice = "namedevice";
	   private String username = "username";
	   private String point = "point";
	   private String levels = "levels";
	   public ArrayList<HashMap<String, String>> itemRankList;
	   private String rank;
	   public String urlString;
	   
	   public RankingParser(String url){
	      this.urlString = url;
	   }
	   public String getUsername(){
	      return username;
	   }
	   public String getRank(){
		   return rank;
	   } 
	   public String getPoint(){
	      return point;
	   }
	   public ArrayList<HashMap<String, String>> getItemRankList(){
		   return itemRankList;
	   }
	  
	   
	   @SuppressLint("NewApi")
	   public void readAndParseJSON(String in) {
		  JSONArray items = null; 
		  //String preTitle="";
	      try {
	    	  JSONObject jsonObj = new JSONObject(in);
              
              // Getting JSON Array node
	    	  items = jsonObj.getJSONArray("news");	    	  
	    	 
	    	  itemRankList = new ArrayList<HashMap<String, String>>();
	    	  
	    	  //Log.e("________", String.valueOf(items.length()));
	    	  
	    	
              for (int i = 0; i < items.length(); i++) {
                  
                  try{
                	  JSONObject c = items.getJSONObject(i);
                      if (c.isNull("id")) continue;
                   
                      username=  c.getString("username"); 
                      point = c.getString("point");
                      levels=c.getString("levels"); 
                      HashMap<String, String> itemDetail = new HashMap<String, String>();
                    
	                  itemDetail.put("username", username);
	                  itemDetail.put("point", point);
	                  itemDetail.put("levels", levels);
	                  
	                  itemRankList.add(itemDetail);
                  }catch(Exception ex)
                  {
                	  //Log.e("ExceptionExceptionExceptionExceptionExceptionException", ex.toString());
                  }
              }
	         


	        } catch (Exception e) {
	           // TODO Auto-generated catch block
	        	//Log.e("ExceptionExceptionExceptionExceptionExceptionException", e.toString());
	           e.printStackTrace();
	        }

	   }
	   public String fetchJSON() throws IOException{
	      
	         try {
	            URL url = new URL(urlString);
	            //Log.e("fetchJSON", urlString);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setReadTimeout(10000 /* milliseconds */);
	            conn.setConnectTimeout(15000 /* milliseconds */);
	            conn.setRequestMethod("GET");
	            conn.setDoInput(true);
	            //conn.setRequestProperty(field, newValue)
	            // Starts the query
	            conn.connect();
	            //Log.e("fetchJSON", "ok1");
		         //InputStream stream = conn.getInputStream();
	            InputStreamReader reader = null;
		         StringBuilder builder = new StringBuilder();
	
		         try {
		             // ...
		             reader = new InputStreamReader(conn.getInputStream(), "UTF-8");
		             char[] buffer = new char[8192];
	
		             for (int length = 0; (length = reader.read(buffer)) > 0;) {
		                 builder.append(buffer, 0, length);
		                 
		             }
		         } finally {
		             if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}
		         }
	
		         String html = builder.toString();
		         
		         String data = html;//convertStreamToString(stream);
	      		 data="{\"news\":"+data+"}";
	      		 //Log.e("fetchJSON", data);
	      		 readAndParseJSON(data);
	      		 //stream.close();
	      		 return data;
	         } catch (Exception e) {
	            e.printStackTrace();
	            return "0";
	         }	         
	      		
	   }
	   public String fetchRaw() throws IOException{
		      
	         try {
	            URL url = new URL(urlString);
	            //Log.e("fetchJSON", urlString);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setReadTimeout(10000 /* milliseconds */);
	            conn.setConnectTimeout(15000 /* milliseconds */);
	            conn.setRequestMethod("GET");
	            conn.setDoInput(true);
	            //conn.setRequestProperty(field, newValue)
	            // Starts the query
	            conn.connect();
	            //Log.e("fetchJSON", "ok1");
		         //InputStream stream = conn.getInputStream();
	            InputStreamReader reader = null;
		         StringBuilder builder = new StringBuilder();
	
		         try {
		             // ...
		             reader = new InputStreamReader(conn.getInputStream(), "UTF-8");
		             char[] buffer = new char[8192];
	
		             for (int length = 0; (length = reader.read(buffer)) > 0;) {
		                 builder.append(buffer, 0, length);
		                 
		             }
		         } finally {
		             if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}
		         }
	
		         String html = builder.toString();		         
		         String data = html;//convertStreamToString(stream);
		         rank=data;
	      		 return data;
	         } catch (Exception e) {
	            e.printStackTrace();
	            return "0";
	         }	         
	      		
	   }
	   static String convertStreamToString(java.io.InputStream is) {
	      java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	      return s.hasNext() ? s.next() : "";
	   }
}
