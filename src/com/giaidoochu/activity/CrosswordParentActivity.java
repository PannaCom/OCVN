package com.giaidoochu.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import com.giaidoochu.Crossword;
import com.giaidoochu.R;

public class CrosswordParentActivity extends Activity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_main_preferences:
        	startActivityForResult(new Intent(this, PeferencesActivity.class), Crossword.REQUEST_PREFERENCES);
        	return true;
        case R.id.menu_feedback:
        	startActivity(new Intent(this, FeedbackActivity.class));
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
