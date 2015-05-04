package com.crossword.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crossword.R;
import com.crossword.activity.GameActivity.GRID_MODE;
import com.crossword.adapter.RankingAdapter;
import com.crossword.parser.RankingParser;

public class RankingActivity extends CrosswordParentActivity{
	RankingAdapter adapter;
	RankingParser jsonP,jsonR;
	ListView list;
	public ArrayList<HashMap<String, String>> itemRankList;
	private ProgressDialog dialog;
    private Handler progressBarHandler = new Handler();
    private int progressBarStatus = 0;
    private Thread tPr;
    private int levels,totallaurel;
    private String username;
    private String iddevice;
    TextView rankpos,pointpos;
    public Typeface  typefaceTitle,typefaceInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.e("displayNewsContent", "ok1");
		setContentView(R.layout.rankingactivity);
		list=(ListView)findViewById(R.id.RankingListView);
		//Log.e("displayNewsContent", "ok4");
		readPreferences();
		//UI
		typefaceTitle=Typeface.createFromAsset(this.getAssets(),"fonts/Roboto-Bold.ttf");
		TextView username = (TextView)findViewById(R.id.username); 
		TextView point = (TextView)findViewById(R.id.point); 
		TextView level = (TextView)findViewById(R.id.level); 
		TextView ranktitle = (TextView)findViewById(R.id.ranktitle); 
		//TextView rankpos = (TextView)findViewById(R.id.rankpos); 
		//TextView pointpos = (TextView)findViewById(R.id.pointpos); 
		username.setTypeface(typefaceTitle);
		point.setTypeface(typefaceTitle);
		level.setTypeface(typefaceTitle);
		ranktitle.setTypeface(typefaceTitle);
		ranktitle.setShadowLayer(12, -4, -4, Color.YELLOW);
		username.setShadowLayer(12, -4, -4, Color.BLACK);
		point.setShadowLayer(12, -4, -4, Color.BLACK);
		level.setShadowLayer(12, -4, -4, Color.BLACK);
		
		Toast.makeText(this, "Đang cập nhật Bảng xếp hạng", Toast.LENGTH_SHORT).show();
		dialog=new ProgressDialog(this);
		dialog.setMessage("Đang cập nhật bảng xếp hạng! Xin đợi...");
		dialog.setCancelable(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setProgress(0);
		dialog.setMax(100);
		

	   dialog.show();
	   final int totalProgressTime = 100;

	   tPr = new Thread(){

	   @Override
	   public void run(){
	 
	      int jumpTime = 0;
	      while(jumpTime < totalProgressTime && progressBarStatus!=-1){
	         try {
	            sleep(1000);
	            jumpTime += 5;
	            progressBarStatus = jumpTime;
	            //dialog.setProgress(jumpTime);
	            // Update the progress bar
				  progressBarHandler.post(new Runnable() {
					public void run() {
						dialog.setProgress(progressBarStatus);
					}
				  });
	         } catch (InterruptedException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	         }

	      }

	   }
	   };
	   tPr.start();
	   
	    //Log.e("displayNewsContent", "ok4");
		////////// Viec customize listview duoc thuc hien o LazayAdapter
		new showRankFromUrlTask().execute("http://ochu.binhyen.net/Home/getRankingList");		
//		itemRankList = new ArrayList<HashMap<String, String>>();	
//        adapter=new RankingAdapter(this, itemRankList,this.getBaseContext());  
//        Log.e("displayNewsContent", "ok5");
//        list.setAdapter(adapter);
        //Log.e("displayNewsContent", "ok6");
        //Log.e("list.setAdapter(adapter);", "ok2");
	}
	private void readPreferences() {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		this.totallaurel=preferences.getInt("totallaurel", 0);
		
		this.levels=preferences.getInt("levels", 0);
		this.username=preferences.getString("username", "");	
		this.iddevice=preferences.getString("iddevice", "");	
		
		
	}
	private void showRank(){
		itemRankList = new ArrayList<HashMap<String, String>>();
		itemRankList=jsonP.getItemRankList();
		adapter=new RankingAdapter(this, itemRankList,this.getBaseContext(),this.username,this.totallaurel);  
        //Log.e("displayNewsContent", "ok5");
        list.setAdapter(adapter);
        rankpos=(TextView)findViewById(R.id.rankpos);
        pointpos=(TextView)findViewById(R.id.pointpos);
        rankpos.setTypeface(typefaceTitle);
		pointpos.setTypeface(typefaceTitle);
        pointpos.setText(String.valueOf(this.totallaurel));
        rankpos.setText(jsonR.getRank());//jsonR.getRank()
        rankpos.setShadowLayer(12, -4, -4, Color.BLUE);
        pointpos.setShadowLayer(12, -4, -4, Color.BLACK);
	}
	private class showRankFromUrlTask extends AsyncTask<String, Void, String> {    	
        @Override
        protected String doInBackground(String... urls) {
              
            // params comes from the execute() call: params[0] is the url.
            try {
            	//Log.e("showNewsFromUrlTask", "1");
            	
            	//Log.e("showNewsFromUrlTask", "ok1");
        		jsonP=new RankingParser(urls[0]);
        		jsonR=new RankingParser("http://ochu.binhyen.net/Home/getRankingPos?point="+totallaurel+"&levels="+levels+"&iddevice="+iddevice);
        		jsonR.fetchRaw();
        		//Log.e("showNewsFromUrlTask", "ok2");
        		//boolean result=
                return jsonP.fetchJSON();
            } catch (IOException e) {
            	//Log.e("showNewsFromUrlTask", "can not download");
            	tPr.interrupt();
            	progressBarStatus=-1;            	
            	dialog.dismiss();
            	Toast.makeText(RankingActivity.this, "Đường truyền không ổn định. Xin quay lại sau!", Toast.LENGTH_SHORT).show();
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
        	tPr.interrupt();
        	progressBarStatus=-1;            	
        	dialog.dismiss();
        	//}catch(Exception ex){
        		
        	//}
        	if (result.equals("0")){ 
        		//Log.e("onPostExecute", "can not download");
        		Toast.makeText(RankingActivity.this, "Đường truyền không ổn định. Xin quay lại sau!", Toast.LENGTH_SHORT).show();
        		//showExtProgram("Internet connection",connecMessage);
        	}else{        		
        		//Log.e("onPostExecute", "ok");
        		showRank();
        	}
        	//try{
        		//if(dialog!=null) dialog.dismiss();
        	//}catch(Exception ex){
        		
        	//}
       }
    }
}
