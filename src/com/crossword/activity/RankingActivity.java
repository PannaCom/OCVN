package com.crossword.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.crossword.R;
import com.crossword.adapter.RankingAdapter;
import com.crossword.parser.RankingParser;

public class RankingActivity extends CrosswordParentActivity{
	RankingAdapter adapter;
	RankingParser jsonP;
	ListView list;
	public ArrayList<HashMap<String, String>> itemRankList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("displayNewsContent", "ok1");
		setContentView(R.layout.rankingactivity);
		list=(ListView)findViewById(R.id.RankingListView);
		Log.e("displayNewsContent", "ok4");
		Toast.makeText(this, "Đang cập nhật Bảng xếp hạng", Toast.LENGTH_SHORT).show();
	    //Log.e("displayNewsContent", "ok4");
		////////// Viec customize listview duoc thuc hien o LazayAdapter
		new showRankFromUrlTask().execute("http://binhyen.net/ApiServer/getRankingList");		
//		itemRankList = new ArrayList<HashMap<String, String>>();	
//        adapter=new RankingAdapter(this, itemRankList,this.getBaseContext());  
//        Log.e("displayNewsContent", "ok5");
//        list.setAdapter(adapter);
        //Log.e("displayNewsContent", "ok6");
        //Log.e("list.setAdapter(adapter);", "ok2");
	}
	private void showRank(){
		itemRankList = new ArrayList<HashMap<String, String>>();
		itemRankList=jsonP.getItemRankList();
		adapter=new RankingAdapter(this, itemRankList,this.getBaseContext());  
        Log.e("displayNewsContent", "ok5");
        list.setAdapter(adapter);
	}
	private class showRankFromUrlTask extends AsyncTask<String, Void, String> {    	
        @Override
        protected String doInBackground(String... urls) {
              
            // params comes from the execute() call: params[0] is the url.
            try {
            	//Log.e("showNewsFromUrlTask", "1");
            	
            	Log.e("showNewsFromUrlTask", "ok1");
        		jsonP=new RankingParser(urls[0]);
        		Log.e("showNewsFromUrlTask", "ok2");
        		//boolean result=
                return jsonP.fetchJSON();
            } catch (IOException e) {
            	Log.e("showNewsFromUrlTask", "can not download");
//            	tPr.interrupt();
//            	progressBarStatus=-1;            	
//            	dialog.dismiss();
//            	showExtProgram("Internet connection",connecMessage);
            	//ResultDownloadWebpage=0;            	
            	return "0";
            }
        }
        
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //textView.setText(result);
        	//try{
        		//if(dialog!=null) 
//        	isSearching=false;
//        	tPr.interrupt();
//        	progressBarStatus=-1;            	
//        	dialog.dismiss();
        	//}catch(Exception ex){
        		
        	//}
        	if (result.equals("0")){ 
        		Log.e("onPostExecute", "can not download");
        		//showExtProgram("Internet connection",connecMessage);
        	}else{        		
        		Log.e("onPostExecute", "ok");
        		showRank();
        	}
        	//try{
        		//if(dialog!=null) dialog.dismiss();
        	//}catch(Exception ex){
        		
        	//}
       }
    }
}
