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

package com.giaidoochu.parser;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.giaidoochu.data.Word;
public class CrosswordParser extends DefaultHandler {
	
	private ArrayList<Word>	entries;
	private Word 			currentFeed;
	private StringBuffer 	buffer;
	private boolean			inHorizontal;

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		super.processingInstruction(target, data);
	}
	public CrosswordParser() {
		super();
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		entries = new ArrayList<Word>();
	}

	@Override
	public void startElement(String uri, String localName, String name,	Attributes attributes) throws SAXException {
		buffer = new StringBuffer();

		if (localName.equalsIgnoreCase("horizontal")) {
			this.inHorizontal = true;
		}

		if (localName.equalsIgnoreCase("vertical")) {
			this.inHorizontal = false;
		}
		
		if (localName.equalsIgnoreCase("word")) {
			this.currentFeed = new Word();
			this.currentFeed.setX(Integer.parseInt(attributes.getValue("x")));
			this.currentFeed.setY(Integer.parseInt(attributes.getValue("y")));
			this.currentFeed.setTmp(attributes.getValue("tmp"));
			this.currentFeed.setDescription(attributes.getValue("description"));
			this.currentFeed.setHorizontal(this.inHorizontal);
		}
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		if (localName.equalsIgnoreCase("word")) {
			this.currentFeed.setText(buffer.toString());
			entries.add(this.currentFeed);
			System.out.println("Word, text: " + this.currentFeed.getText() + ", tmp: " + this.currentFeed.getTmp() + ", x: " + this.currentFeed.getX() + ", y: " + this.currentFeed.getY());
			buffer = null;
		}
	}

	public void characters(char[] ch,int start, int length)	throws SAXException{
		String lecture = new String(ch,start,length);
		if(buffer != null) buffer.append(lecture);
	}

	public ArrayList<Word> getData() { return entries; }
}
