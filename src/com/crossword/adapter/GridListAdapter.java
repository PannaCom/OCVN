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

package com.crossword.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

import com.crossword.R;
import com.crossword.data.Grid;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridListAdapter extends BaseAdapter {

	private HashMap<Integer, View>	views = new HashMap<Integer, View>();
	private ArrayList<Grid>			data = new ArrayList<Grid>();
	private LayoutInflater 			inflater;
	public Typeface typefaceTitle;
	public GridListAdapter(Context c) {
		this.inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		typefaceTitle=Typeface.createFromAsset(c.getAssets(),"fonts/DroidSans-Bold.ttf");
	}
	
	/**
	 * Return list items count, including date separator
	 */
	@Override
	public int getCount() {
		return this.data.size();
	}

	@Override
	public boolean areAllItemsEnabled () {
		return false;
	}

	@Override
	public boolean isEnabled (int position) {
		return !this.data.get(position).isSeparator();
	}
	
	/**
	 * Return grid for this position
	 */
	@Override
	public Object getItem(int position) {
		return this.data.get(position);
	}

	/**
	 * Not applicable
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		//System.out.println("getview: " + position);
		
		View v = this.views.get(position);

		if (this.data.get(position).isSeparator())
		{
			if (v == null)
			{
				v = this.inflater.inflate(R.layout.gridlist_separator, null);
				
				TextView name = (TextView)v.findViewById(R.id.name);
				name.setText(this.data.get(position).getName());

				if (position + 1 < this.getCount() && this.data.get(position + 1).isSeparator()) {
					v.setVisibility(View.GONE);
					name.setVisibility(View.GONE);
					v.setBackgroundDrawable(null);
				}

				this.views.put(position, v);
			}
		}
		else
		{
			if (v == null)
			{
				v = this.inflater.inflate(R.layout.gridlist_item, null);
				
				// Name
				TextView name = (TextView)v.findViewById(R.id.name);
				name.setText(this.data.get(position).getName().toUpperCase());
				name.setTypeface(typefaceTitle);
//				// Author
//				if (this.data.get(position).getAuthor() != null) {
//					TextView author = (TextView)v.findViewById(R.id.author);
//					author.setText(this.data.get(position).getAuthor());
//				}
//				
//				// Date
//				if (this.data.get(position).getDate() != null) {
//					TextView date = (TextView)v.findViewById(R.id.date);
//					DateFormat df = new SimpleDateFormat("d MMMM yyyy");
//					date.setText(df.format(this.data.get(position).getDate()));
//				}
				
				// Difficulty
				ImageView level = (ImageView)v.findViewById(R.id.level);
				switch (this.data.get(position).getLevel()) {
				case 1: level.setImageResource(R.drawable.icon_level_1); break;
				case 2: level.setImageResource(R.drawable.icon_level_2); break;
				case 3: level.setImageResource(R.drawable.icon_level_3); break;
				}
				this.views.put(position, v);
			}

			// Progression
			ImageView imgPercent = (ImageView)v.findViewById(R.id.percent);
			int percent = this.data.get(position).getPercent();
			if (percent == 0)
				imgPercent.setImageResource(R.drawable.progress_0);
			else if (percent <= 20)
				imgPercent.setImageResource(R.drawable.progress_1);
			else if (percent <= 40)
				imgPercent.setImageResource(R.drawable.progress_2);
			else if (percent <= 60)
				imgPercent.setImageResource(R.drawable.progress_3);
			else if (percent <= 80)
				imgPercent.setImageResource(R.drawable.progress_4);
			else if (percent <= 99)
				imgPercent.setImageResource(R.drawable.progress_5);
			else
				imgPercent.setImageResource(R.drawable.progress_6);
		}

		return v;
	}

	/**
	 * Add grid or date separator to the list
	 * 
	 * @param item
	 */
	public void addGrid(Grid item) {
		this.data.add(item);
	}

	/**
	 * Remove all items from the list, including date separator
	 */
	public void clear() {
		this.data.clear();
		this.views.clear();
	}

	/**
	 * Sort by date list datasource
	 */
	public void sort() {
		Collections.sort(this.data);
	}

	/** Add time separator (a week ago, 2 weeks ago, etc)
	 * 
	 * @param name
	 * @param dayAgo
	 */
	public void addSeparator(String name, int dayAgo) {
		Calendar cal = Calendar.getInstance();

		Grid grid = new Grid();
	    grid.isSeparator(true);
	    grid.setName(name);
        cal.setTime(new java.util.Date());
        cal.add(Calendar.DATE, dayAgo);
        grid.setDate(cal.getTime());
        
        this.addGrid(grid);
	}

}
