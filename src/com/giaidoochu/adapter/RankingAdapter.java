package com.giaidoochu.adapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.giaidoochu.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RankingAdapter extends BaseAdapter{
	 private Activity activity;
	    private ArrayList<HashMap<String, String>> data;
	    private static LayoutInflater inflater=null;
	   
	    public int totalNumber=0;
	    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	    //public String currentDateandTime = sdf.format(new Date());
	    
	    public static String Main_Domain="http://binhyen.net";
	    public Context context;
	    public Typeface typefaceTitle,typefaceInfo;
	    private String username="";
	    private int totallaurel;
	   
	    public RankingAdapter(Activity a, ArrayList<HashMap<String, String>> d,Context context,String username,int totallaurel) {
	    	//.context=context;
	        activity = a;
	        data=d;
	        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        this.username=username;
	        this.totallaurel=totallaurel;
	        //currentDateandTime = sdf.format(new Date(ts));
	        typefaceTitle=Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Bold.ttf");
	        typefaceInfo=Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Medium.ttf");
	        //Log.e("LazyAdapter", "ok2");
	    }
	// day se la so luong hien thi len listview
	    public int getCount() {
	        return data.size();
	    }

	    // lay 1 item tu vi tri cua no
	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }
	    
	    // day la noi gan len listview
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	totalNumber++;
	        View vi=convertView;
	        if(convertView==null)
	            vi = inflater.inflate(R.layout.ranking, null);
	        TextView no = (TextView)vi.findViewById(R.id.no); // title
	        TextView username = (TextView)vi.findViewById(R.id.username); // title
	        TextView point = (TextView)vi.findViewById(R.id.point); // source
	        TextView level = (TextView)vi.findViewById(R.id.level); // date
	       
	        
	        HashMap<String, String> news = new HashMap<String, String>();
	        news = data.get(position);
	        //AssetManager am=getContext(;
	        // Setting all values in listview
	        
	        username.setTypeface(typefaceTitle);
	        username.setShadowLayer(12, -4, -4, Color.BLUE);
	        point.setTypeface(typefaceInfo);
	        level.setTypeface(typefaceInfo);
	        no.setTypeface(typefaceInfo);
	        username.setText(Html.fromHtml(news.get("username").toLowerCase()));
	       
	               
	        point.setText(news.get("point"));
	        no.setText(String.valueOf(position+1));
	        level.setText(news.get("levels"));
	        if (news.get("username").trim().equalsIgnoreCase(this.username.trim()) && news.get("point").equalsIgnoreCase(String.valueOf(this.totallaurel))){
	        	vi.setBackgroundColor(Color.YELLOW);
	        }
	        return vi;
	    }
	    
}
