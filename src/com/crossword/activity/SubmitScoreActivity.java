package com.crossword.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.crossword.Crossword;
import com.crossword.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SubmitScoreActivity extends CrosswordParentActivity implements OnClickListener{
	private String error;
	private AlertDialog alertName;
	private AlertDialog.Builder alertDialogBuilder;
	private List<NameValuePair>nameValuePairs = new ArrayList<NameValuePair>(2);
	public void onCreate(Bundle savedInstanceState) {
		Log.e("SubmitScoreActivity", "1111");
	    super.onCreate(savedInstanceState);
	    Log.e("SubmitScoreActivity", "2222");
	    setContentView(R.layout.submitscore);
	    Button feedSend = (Button)findViewById(R.id.feed_send);
	    feedSend.setOnClickListener(this);
	    Log.e("SubmitScoreActivity", "3333");
	    final EditText inputname = new EditText(this.getApplicationContext());
	    Log.e("SubmitScoreActivity", "44444");
	    alertDialogBuilder = new AlertDialog.Builder(this);
		// set title
		alertDialogBuilder.setTitle("Nhập tên người chơi");
		Log.e("SubmitScoreActivity", "55555");
		// set dialog message
		alertDialogBuilder
			.setMessage("")
			.setCancelable(false)
			.setPositiveButton("Gửi điểm",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					nameValuePairs.add(
			                new BasicNameValuePair("name", inputname.getText().toString())
			                );
					nameValuePairs.add(
			                new BasicNameValuePair("number","0909009090")
			                );
					nameValuePairs.add(
			                new BasicNameValuePair("lon","105.803189")
			                );
					nameValuePairs.add(
			                new BasicNameValuePair("lat","20.991513")
			                );
					startSend();
				}
			  })
			  .setNeutralButton("Bỏ qua",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
	        		
				}
			  });
	}
	public String getDeviceName() {
		   String manufacturer = Build.MANUFACTURER;
		   String model = Build.MODEL;
		   if (model.startsWith(manufacturer)) {
		      return capitalize(model);
		   } else {
		      return capitalize(manufacturer) + " " + model;
		   }
	}
	private String capitalize(String s) {
	    if (s == null || s.length() == 0) {
	        return "";
	    }
	    char first = s.charAt(0);
	    if (Character.isUpperCase(first)) {
	        return s;
	    } else {
	        return Character.toUpperCase(first) + s.substring(1);
	    }
}
	public void postMessage() {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    String url="http://tingting.vn/map/local?";//name=hlhlk&number=0909009090&lon=105.803189&lat=20.991513
	    HttpPost httppost = new HttpPost(url);
	    try {
	        //EditText feedMessage = (EditText)findViewById(R.id.feed_message);

	        // Add your data
//	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//	        nameValuePairs.add(new BasicNameValuePair("from", android.os.Build.MODEL + " (" + android.os.Build.VERSION.RELEASE + ")"));
//	        nameValuePairs.add(new BasicNameValuePair("message", feedMessage.getText().toString()));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        if (response.getStatusLine().getStatusCode() == 200) {
    		    runOnUiThread(new Runnable() {
    		        public void run() {
    		        	Toast.makeText(SubmitScoreActivity.this, R.string.feedback_send_success, Toast.LENGTH_SHORT).show();
    		        }
    		    });
	        	finish();
	        	return;
	        }
	        
	        String line;
	        StringBuilder total = new StringBuilder();
	        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        while ((line = rd.readLine()) != null) { 
	            total.append(line); 
	        }
	        error = Html.fromHtml(total.toString()).toString();
	    } catch (ClientProtocolException e) {
	    	error = getResources().getString(R.string.exception_network);
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	error = getResources().getString(R.string.exception_network);
	    	e.printStackTrace();
	    }
	}
	public void startSend() {
		
	        final ProgressDialog progress = ProgressDialog.show(SubmitScoreActivity.this, "", "Đang cập nhật..", true);
	        new Thread((new Runnable() {
	            @Override
	            public void run() {
	    			postMessage();
	    			progress.dismiss();
	    		    runOnUiThread(new Runnable() {
	    		        public void run() {
	    		        	new AlertDialog.Builder(SubmitScoreActivity.this).setMessage(SubmitScoreActivity.this.error)
	    		 	       .setCancelable(false)
	    		 	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	    		 	           public void onClick(DialogInterface dialog, int id) {
	    		 	                dialog.cancel();
	    		 	           }
	    		 	       }).create().show();
	    		        }
	    		    });
	            }
	        })).start();
		
	} 
	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
		case R.id.feed_send:
	        final ProgressDialog progress = ProgressDialog.show(SubmitScoreActivity.this, "", getResources().getString(R.string.feedback_sending), true);
	        new Thread((new Runnable() {
	            @Override
	            public void run() {
	    			postMessage();
	    			progress.dismiss();
	    		    runOnUiThread(new Runnable() {
	    		        public void run() {
	    		        	new AlertDialog.Builder(SubmitScoreActivity.this).setMessage(SubmitScoreActivity.this.error)
	    		 	       .setCancelable(false)
	    		 	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	    		 	           public void onClick(DialogInterface dialog, int id) {
	    		 	                dialog.cancel();
	    		 	           }
	    		 	       }).create().show();
	    		        }
	    		    });
	            }
	        })).start();
		}
	} 
}
