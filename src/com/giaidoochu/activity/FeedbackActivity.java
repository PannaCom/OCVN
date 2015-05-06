/*
 * Copyright 2011 Alexis Lauper <alexis.lauper@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of 
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends CrosswordParentActivity implements OnClickListener {
	
	private String error;

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.feedback);

	    Button feedSend = (Button)findViewById(R.id.feed_send);
	    feedSend.setOnClickListener(this);
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
	public void postMessage() {
	    // Create a new HttpClient and Post Header
		EditText feedMessage = (EditText)findViewById(R.id.feed_message);
		String fromContent="Xin chào bạn nhé";//getPhoneNumber()+"%20"+android.os.Build.MODEL + "%20" + android.os.Build.VERSION.RELEASE + "%20"+feedMessage.getText().toString();
		
	    HttpClient httpclient = new DefaultHttpClient();
	    String url="http://ochu.binhyen.net/Home/addFeedback";//?content="+fromContent;
	    Log.e("Gá»­i lĂªn", url);
	    HttpPost httppost = new HttpPost(url);
	    try {
	        
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        //nameValuePairs.add(new BasicNameValuePair("from", android.os.Build.MODEL + " (" + android.os.Build.VERSION.RELEASE + ")"));
	        nameValuePairs.add(new BasicNameValuePair("content", getPhoneNumber()+", "+android.os.Build.MODEL + ", " + android.os.Build.VERSION.RELEASE + ", "+feedMessage.getText().toString()));
	        
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
	        
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        if (response.getStatusLine().getStatusCode() == 200) {
    		    runOnUiThread(new Runnable() {
    		        public void run() {
    		        	Toast.makeText(FeedbackActivity.this, R.string.feedback_send_success, Toast.LENGTH_SHORT).show();
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

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
		case R.id.feed_send:
	        final ProgressDialog progress = ProgressDialog.show(FeedbackActivity.this, "", getResources().getString(R.string.feedback_sending), true);
	        new Thread((new Runnable() {
	            @Override
	            public void run() {
	    			postMessage();
	    			progress.dismiss();
	    		    runOnUiThread(new Runnable() {
	    		        public void run() {
	    		        	new AlertDialog.Builder(FeedbackActivity.this).setMessage(FeedbackActivity.this.error)
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
