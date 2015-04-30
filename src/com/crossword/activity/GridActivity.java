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

package com.crossword.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.crossword.R;
import com.crossword.data.Grid;
import android.os.Bundle;
import android.widget.TextView;

public class GridActivity extends CrosswordParentActivity {
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.grid);
	    
        Grid grid = getIntent().getExtras().getParcelable("grid");

		TextView name = (TextView)findViewById(R.id.name);
		name.setText(grid.getName());

		TextView description = (TextView)findViewById(R.id.description);
		description.setText(grid.getDescription());

		TextView author = (TextView)findViewById(R.id.author);
		author.setText(grid.getAuthor());

		TextView date = (TextView)findViewById(R.id.date);
		DateFormat df = new SimpleDateFormat("dd MM yyyy");
		date.setText(df.format(grid.getDate()));
	}
	
}
