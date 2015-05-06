package com.giaidoochu;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
public class AdvManager {
	private static InterstitialAd adView;
	private static final String AD_UNIT_ID = "ca-app-pub-6575239088642506/7112181273";
	public static void init(Context thisa){
		adView = new InterstitialAd(thisa);
		//adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(AD_UNIT_ID);
		adView.setAdListener(new AdListener() {
		      @Override
		      public void onAdLoaded() {
		        //Log.e("onAdLoadedonAdLoadedonAdLoaded", "onAdLoaded");
		      
		      }

		      @Override
		      public void onAdFailedToLoad(int errorCode) {
		        String message = String.format("onAdFailedToLoad (%s)", getErrorReason(errorCode));
		        //Log.e("onAdFailedToLoadonAdFailedToLoad", message);
		      }
		    });
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}
	  private static String getErrorReason(int errorCode) {
		    String errorReason = "";
		    switch(errorCode) {
		      case AdRequest.ERROR_CODE_INTERNAL_ERROR:
		        errorReason = "Internal error";
		        break;
		      case AdRequest.ERROR_CODE_INVALID_REQUEST:
		        errorReason = "Invalid request";
		        break;
		      case AdRequest.ERROR_CODE_NETWORK_ERROR:
		        errorReason = "Network Error";
		        break;
		      case AdRequest.ERROR_CODE_NO_FILL:
		        errorReason = "No fill";
		        break;
		    }
		    return errorReason;
		  }
		// Invoke displayInterstitial() when you are ready to display an interstitial.
		public static void displayInterstitial() {
			Log.e("displayInterstitial", "ok");
		   //if (adView.isLoaded()) {
			   Log.e("displayInterstitial", "ok1");
		    	adView.show();
		    	Log.e("displayInterstitial", "ok2");
		   //}
		}
}
