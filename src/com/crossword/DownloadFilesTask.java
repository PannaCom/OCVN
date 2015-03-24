/*
 * Copyright 2011 Nguyen Hoang Ton, a.k.a Ton Nguyen
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

package com.crossword;

import android.os.AsyncTask;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import org.xml.sax.helpers.DefaultHandler;

import com.crossword.parser.GridListParser;

import android.content.Context;

/**
 * A async task to download data file from Internet. This task can run in background, so the download process
 * will be executed even if user has leave the application
 * @author 
 */
public class DownloadFilesTask extends AsyncTask<String, Integer, String> {
	private boolean isSucceed = false;
	private String errorMessage = "";
        
	private HttpURLConnection urlConnection;
	private DownloadFilesInterface delegate;
	private Context context;
	private boolean completed;
	private String status;
	private int progress;
        
	public DownloadFilesTask(DownloadFilesInterface activity, Context context) {
		this.delegate = activity;
		this.context = context;
		this.status = context.getResources().getString(R.string.notification_update_grids_status);
	}
        
	/**
	 * Android will invoke this method when this async task was started, or when user came back to our application.
	 */
	protected void onPreExecute() {
		super.onPreExecute();
    }
        
	/**
	 * Will be invoked when calling execute(). Everything the task need to do, will be implement here
	 */
	protected String doInBackground(String... params) {
		ArrayList<String> gridList = null;
		GridListParser listGridParser = new GridListParser();
		
		try {
			// Get and parse grid index
			DownloadManager.downloadListGrid();
			SAXFileHandler.read((DefaultHandler)listGridParser, Crossword.GRIDLIST_LOCAL_PATH);
			gridList = listGridParser.getList();

			// Check new grids
			int totalFiles = 0;
			for (String filename: gridList) {
				File file = new File(String.format(Crossword.GRID_LOCAL_PATH, filename));
				if (file.exists() == false)
					++totalFiles;
			}

			// Download new grids
			if (totalFiles != 0)
			{
				delegate.onDownloadTaskStarted();
				for (String filename: gridList) {
					File file = new File(String.format(Crossword.GRID_LOCAL_PATH, filename));
					if (file.exists() == false) {
						DownloadManager.downloadGrid(filename);
					}
					publishProgress(++this.progress, totalFiles);
				}
			}
		} catch (CrosswordException e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
			isSucceed = false;
			return null;
		}
        isSucceed = true;
		return null;
	}

    /**
     * This method will be invoke be UI thread. The purpose is to update UI
     */
    protected void onProgressUpdate(Integer... args) {
    	delegate.onDownloadUpdateProgressStatus(String.format(status, args[0], args[1]));
    }
        
    public void setActivity(DownloadFilesInterface delegate) {
    	this.delegate = delegate;
    	if (completed) {
    		notifyActivityTaskCompleted();
    	}
    }

    protected void onCancelled() {
    	urlConnection.disconnect();
    }
    
    /**
     * After doInBackground has been completed, this method will be called, by UI thread
     */
    protected void onPostExecute(String unused) {
    	completed = true;
    	notifyActivityTaskCompleted();
    }
    
    /**
     * Helper method to notify the activity that this task was completed.
     */
    private void notifyActivityTaskCompleted() {
    	if (delegate == null)
    		return;
    	
    	delegate.onDownloadUpdateProgressStatus(context.getResources().getString(R.string.notification_update_grids_complete));
    	delegate.onDownloadTaskCompleted(isSucceed, progress, errorMessage);
    }
}
