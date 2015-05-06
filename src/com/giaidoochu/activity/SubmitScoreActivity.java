package com.giaidoochu.activity;

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

import com.giaidoochu.Crossword;
import com.giaidoochu.R;
import com.giaidoochu.activity.GameActivity.GRID_MODE;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
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
	private AlertDialog alertDialog;
	private List<NameValuePair>nameValuePairs = new ArrayList<NameValuePair>(2);
	private int totallaurel,levels;
	private String username;
	private String iddevice;
	//private EditText inputname;
	public void onCreate(Bundle savedInstanceState) {
		//Log.e("SubmitScoreActivity", "1111");
	    super.onCreate(savedInstanceState);
	    //Log.e("SubmitScoreActivity", "2222");
	    //setContentView(R.layout.submitscore);
	    //Button feedSend = (Button)findViewById(R.id.feed_send);
	    //feedSend.setOnClickListener(this);
	    totallaurel=0;
	    readPreferences();
	    //Log.e("SubmitScoreActivity", "3333");
	    final EditText inputname = new EditText(this);//.getApplicationContext()
	    inputname.setText(this.username);
	    //levels=totallaurel/60;
	    //Log.e("SubmitScoreActivity", "44444");
	    alertDialogBuilder = new AlertDialog.Builder(this);
		// set title
		alertDialogBuilder.setTitle("Nhập tên người chơi(không quá dài, chỉ bao gồm chữ cái, số)");
		//Log.e("SubmitScoreActivity", "55555");
		//alertDialog
		// set dialog message
		alertDialogBuilder
			.setMessage("")
			.setView(inputname)
			.setCancelable(false)
			.setPositiveButton("Gửi điểm",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					String username = inputname.getText().toString(); 
					if (username.trim().equals("")){
						Toast.makeText(SubmitScoreActivity.this.getBaseContext(), "Nhập tên user", Toast.LENGTH_SHORT).show();
						SubmitScoreActivity.this.finish();
						return;
						//alertDialog = alertDialogBuilder.create();
						//alertDialog.show();
					}
					if (!username.trim().equals("") && username.trim().length()>16){
						Toast.makeText(SubmitScoreActivity.this.getBaseContext(), "Tên user không dài quá 16 ký tự!", Toast.LENGTH_SHORT).show();
						SubmitScoreActivity.this.finish();
						return;
						//alertDialog = alertDialogBuilder.create();
						//alertDialog.show();
					}
					
//					nameValuePairs.add(
//			                new BasicNameValuePair("iddevice",getPhoneNumber())
//			                );
//					nameValuePairs.add(
//			                new BasicNameValuePair("namedevice",getDeviceName())
//			                );
//					nameValuePairs.add(
//			                new BasicNameValuePair("username", inputname.getText().toString())
//			                );
//					nameValuePairs.add(
//			                new BasicNameValuePair("point",String.valueOf(totallaurel))
//			                );
//					nameValuePairs.add(
//			                new BasicNameValuePair("levels",String.valueOf(levels))
//			                );
					if (!username.trim().equals("")) startSend(username);
				}
			  })
			  .setNeutralButton("Bỏ qua",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					SubmitScoreActivity.this.finish();
	        		System.exit(0);
				}
			  });
		// create alert dialog
		
		alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
	private void readPreferences() {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		this.totallaurel=preferences.getInt("totallaurel", 0);
		this.levels=preferences.getInt("level", 0);
		this.username=preferences.getString("username", "");		
	}
	private void setUsernamePreferences(String username){
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

	    SharedPreferences.Editor editor = preferences.edit();
	    editor.putString("username", username);
	    editor.putString("iddevice", this.iddevice);
	    editor.commit();
		//preferences.
		
	}
	private String getPhoneNumber(){
		try{
			TelephonyManager telemamanger = (TelephonyManager) getSystemService(this.getApplicationContext().TELEPHONY_SERVICE);
			String getSimSerialNumber = telemamanger.getSimSerialNumber();
			String getSimNumber = telemamanger.getLine1Number();
			if (getSimNumber=="" || getSimNumber.equals("")) 
				{
				getSimNumber=telemamanger.getDeviceId();
			}
			if (getSimNumber==null || getSimNumber=="") return "testDevice";
			return getSimNumber;
		}catch(Exception ex){
			return "testDevice";
		}
	}
	public String getDeviceName() {
		   String manufacturer = Build.MANUFACTURER.replaceAll(" ","%20");
		   String model = Build.MODEL.replaceAll(" ","%20");
		   if (model.startsWith(manufacturer)) {
		      return capitalize(model);
		   } else {
		      return capitalize(manufacturer) + "_" + model;
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
	public void postMessage(String username) {
	    // Create a new HttpClient and Post Header
		
	    HttpClient httpclient = new DefaultHttpClient();
	    String getPhoneNumber=getPhoneNumber();
	    //Log.e("runOnUiThreadrunOnUiThread333", getPhoneNumber);
	    String getDeviceName=getDeviceName();
	    Log.e("runOnUiThreadrunOnUiThread444", getDeviceName);
	    this.iddevice=getPhoneNumber;
	    setUsernamePreferences(username);
	    String url="http://ochu.binhyen.net/Home/updateRanking?iddevice="+getPhoneNumber+"&namedevice="+getDeviceName+"&username="+username+"&point="+totallaurel+"&levels="+levels;//name=hlhlk&number=0909009090&lon=105.803189&lat=20.991513
	    //Log.e("runOnUiThreadrunOnUiThread111", url);
	    HttpPost httppost = new HttpPost(url);
	    //Log.e("runOnUiThreadrunOnUiThread222", url);
	    try {
	        //EditText feedMessage = (EditText)findViewById(R.id.feed_message);

	        // Add your data
//	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//	        nameValuePairs.add(new BasicNameValuePair("from", android.os.Build.MODEL + " (" + android.os.Build.VERSION.RELEASE + ")"));
//	        nameValuePairs.add(new BasicNameValuePair("message", feedMessage.getText().toString()));
	        //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        if (response.getStatusLine().getStatusCode() == 200) {
    		    runOnUiThread(new Runnable() {
    		        public void run() {
    		        	//Log.e("runOnUiThreadrunOnUiThread", "111");
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
	        //Log.e("runOnUiThreadrunOnUiThread", "2222");
	        error = Html.fromHtml(total.toString()).toString();
	    } catch (ClientProtocolException e) {
	    	error = getResources().getString(R.string.exception_network);
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	error = getResources().getString(R.string.exception_network);
	    	e.printStackTrace();
	    }
	}
	public void startSend(final String username) {
		//Log.e("startSend", "111");	
	        final ProgressDialog progress = ProgressDialog.show(SubmitScoreActivity.this, "", "Đang cập nhật.", true);
	        new Thread((new Runnable() {
	            @Override
	            public void run() {
	            	//Log.e("startSend", "2222");	
	    			postMessage(username);
	    			//Log.e("startSend", "33333");	
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
	    			postMessage("abc");
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
