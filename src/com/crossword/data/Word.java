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

public class Word {
	private int		x;
	private int		y;
	private int		length;
	private String	tmp;
	private String	text;
	private String	description;
	private boolean	horizontal = true;
	
	public void		setText(String value) { this.text = value; this.length = value.length(); }
	public String	getText() { return this.text; }
	
	public void		setTmp(String value) { this.tmp = value; }
	public String	getTmp() { return this.tmp; }
	
	public void		setDescription(String value) { this.description= value; }
	public String	getDescription() { return this.description; }
	
	public boolean	getHorizontal() { return this.horizontal; }
	public void		setHorizontal(boolean value) { this.horizontal = value; }

	public void		setX(int value) { this.x = value; }
	public int		getX() { return this.x; }
	public int 		getXMax() { return this.horizontal ? this.x + this.length - 1: this.x; }
	
	public void		setY(int value) { this.y = value; }
	public int		getY() { return this.y; }
	public int 		getYMax() { return this.horizontal ? this.y : this.y + this.length - 1; }

	public int 		getLength() { return this.length; }

}
