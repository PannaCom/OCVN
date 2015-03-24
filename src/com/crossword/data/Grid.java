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

package com.crossword.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Data class for grid meta information
 * 
 * @author alex
 */
public class Grid implements Parcelable, Comparable<Grid> {
	private String	fileName;
	private String	name;
	private String	description;
	private int		percent;
	private int		level;
	private Date	date;
	private String	author;
	private int 	width;
	private int 	height;
	
	public Grid() {
	}
	
	public Grid(Parcel in) {
		fileName = in.readString();
		name = in.readString();
		description = in.readString();
		percent = in.readInt();
		level = in.readInt();
		author = in.readString();
		width = in.readInt();
		height = in.readInt();
		rawDate = in.readString();
		try {
			date = new SimpleDateFormat("dd/MM/yyyy").parse(rawDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(fileName);
		dest.writeString(name);
		dest.writeString(description);
		dest.writeInt(percent);
		dest.writeInt(level);
		dest.writeString(author);
		dest.writeInt(width);
		dest.writeInt(height);
		dest.writeString(rawDate);
	}

	public static final Parcelable.Creator<Grid> CREATOR = new Parcelable.Creator<Grid>() {
		@Override
		public Grid createFromParcel(Parcel source)
		{
			return new Grid(source);
		}

		@Override
		public Grid[] newArray(int size)
		{
			return new Grid[size];
		}
	};
	
	// This is a little hack, if isSeparator is true, element will be displayed
	// with "date separator" layout (a week ago, a month ago, etc).
	private boolean	isSeparator;
	private String rawDate;

	public boolean isSeparator() {
		return this.isSeparator;
	}
	
	public void isSeparator(boolean value) {
		this.isSeparator = value;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getPercent() {
		return percent;
	}
	
	public void setPercent(int percent) {
		this.percent = percent;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setWidth(int value) {
		this.width = value;
	}

	public void setHeight(int value) {
		this.height = value;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	@Override
	public int compareTo(Grid arg) {
		if (arg.getDate() == null)
			return -1;
		if (this.date == null)
			return 1;
			
		return this.date.before(arg.getDate()) ? 1 : -1;
	}

	public void setRawDate(String value) {
		this.rawDate = value;
	}
}
