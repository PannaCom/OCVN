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

package com.crossword;

@SuppressWarnings("serial")
public class CrosswordException extends Exception {

	public static enum ExceptionType {FILE_NOT_FOUND, GRID_NOT_FOUND, SAV_NOT_FOUND, NETWORK};
	public ExceptionType type;
	
	public CrosswordException(ExceptionType t) {
		this.type = t;
	}

	@Override
	public String getMessage() {
		switch (this.type) {
		case FILE_NOT_FOUND:
			return Crossword.getAppContext().getString(R.string.exception_file_not_found);
		case GRID_NOT_FOUND:
			return Crossword.getAppContext().getString(R.string.exception_grid_not_found);
		case SAV_NOT_FOUND:
			return Crossword.getAppContext().getString(R.string.exception_sav_not_found);
		case NETWORK:
			return Crossword.getAppContext().getString(R.string.exception_network);
		}
		return Crossword.getAppContext().getString(R.string.exception_unknow);
	}
}
