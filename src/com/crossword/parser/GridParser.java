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

package com.crossword.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.crossword.Crossword;
import com.crossword.data.Grid;

public class GridParser extends DefaultHandler {
	
	private Grid			grid = new Grid();
	private StringBuffer	buffer;

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		super.processingInstruction(target, data);
	}
	public GridParser() {
		super();
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String name,	Attributes attributes) throws SAXException {
		buffer = new StringBuffer();
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		if (localName.equalsIgnoreCase("name")) {
			this.grid.setName(buffer.toString());
		}

		else if (localName.equalsIgnoreCase("description")) {
			this.grid.setDescription(buffer.toString());
		}

		else if (localName.equalsIgnoreCase("level")) {
			this.grid.setLevel(Integer.parseInt(buffer.toString()));
		}

		else if (localName.equalsIgnoreCase("width")) {
			this.grid.setWidth(Integer.parseInt(buffer.toString()));
		}

		else if (localName.equalsIgnoreCase("height")) {
			this.grid.setHeight(Integer.parseInt(buffer.toString()));
		}

		else if (localName.equalsIgnoreCase("percent")) {
			this.grid.setPercent(Integer.parseInt(buffer.toString()));
		}

		else if (localName.equalsIgnoreCase("date")) {
			System.out.println(buffer.toString());
			try {
				this.grid.setRawDate(buffer.toString());
				this.grid.setDate((new SimpleDateFormat("dd/MM/yyyy")).parse(buffer.toString()));
			} catch (ParseException e) {
				Log.w(Crossword.NAME, "GridParser: Unable to parse grid date");
			}
		}

		else if (localName.equalsIgnoreCase("author")) {
			this.grid.setAuthor(buffer.toString());
		}
	}

	public void characters(char[] ch,int start, int length)	throws SAXException{
		String lecture = new String(ch,start,length);
		if(buffer != null) buffer.append(lecture);
	}

	public Grid getData() {
		return this.grid;
	}
	
	public void setFileName(String name) {
		this.grid.setFileName(name);
	}
	
}
