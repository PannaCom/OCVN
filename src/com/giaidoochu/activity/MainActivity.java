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

import com.giaidoochu.AdvManager;
import com.giaidoochu.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends CrosswordParentActivity implements OnClickListener {
	public Typeface  typefaceTitle,typefaceInfo;
//	private AdView adView;
//	private String MY_AD_UNIT_ID="ca-app-pub-6575239088642506/9007716873";
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
        findViewById(R.id.button_last).setOnClickListener(this);
        findViewById(R.id.button_list).setOnClickListener(this);
        findViewById(R.id.button_category).setOnClickListener(this);
        findViewById(R.id.button_search).setOnClickListener(this);
//        String last = PreferenceManager.getDefaultSharedPreferences(this).getString("last_grid", null);
//		if (last != null) {
//			Intent intent = new Intent(this, GameActivity.class);
//			intent.putExtra("filename", last);
//			startActivity(intent);
//		}
        typefaceTitle=Typeface.createFromAsset(this.getAssets(),"fonts/Roboto-Bold.ttf");
        Button buttonLast=(Button)findViewById(R.id.button_last);
        buttonLast.setTypeface(typefaceTitle);
        buttonLast.setShadowLayer(12, -4, -4, Color.BLUE);
        Button buttonList=(Button)findViewById(R.id.button_list);
        buttonList.setTypeface(typefaceTitle);
        buttonList.setShadowLayer(12, -4, -4, Color.BLUE);
        Button buttonCat=(Button)findViewById(R.id.button_category);
        buttonCat.setTypeface(typefaceTitle);
        buttonCat.setShadowLayer(12, -4, -4, Color.BLUE);
        Button buttonSearch=(Button)findViewById(R.id.button_search);
        buttonSearch.setTypeface(typefaceTitle);
        buttonSearch.setShadowLayer(12, -4, -4, Color.BLUE);
        AdvManager.init(this.getBaseContext());
        AdvManager.displayInterstitial();
        //showAd();        
    }
    public void showAd(){
//	    adView = new AdView(this);
//	    adView.setAdSize(AdSize.BANNER);	   
//	    adView.setAdUnitId(MY_AD_UNIT_ID);
//	    //LinearLayout rl = new LinearLayout(this);
//	    LinearLayout.LayoutParams lay = new LinearLayout.LayoutParams(
//	    		LinearLayout.LayoutParams.FILL_PARENT, 
//	    		LinearLayout.LayoutParams.WRAP_CONTENT);
//	    //lay.addRule(LinearLayout.);	    
//	    LinearLayout layout = (LinearLayout) findViewById(R.id.mainGame);
//	    layout.addView(adView,lay);
//	    //rl.setGravity(Gravity.BOTTOM);
//	    AdRequest adRequest = new AdRequest.Builder().build();
//	    adView.loadAd(adRequest);
    
	}
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.button_last: {
				String last = PreferenceManager.getDefaultSharedPreferences(this).getString("last_grid", null);
				if (last != null) {
					Intent intent = new Intent(this, GameActivity.class);
					intent.putExtra("filename", last);
					startActivity(intent);
				}
				break;
			}
			case R.id.button_list: {
				Intent intent = new Intent(this, GridListActivity.class);
				startActivity(intent);
				break;
			}
			case R.id.button_category: {
				Intent intent = new Intent(this, RankingActivity.class);
				startActivity(intent);
				break;
				
			}
			case R.id.button_search: {
				Intent intent = new Intent(this, SubmitScoreActivity.class);
				startActivity(intent);
				break;
			}
		}
	}
    
}
