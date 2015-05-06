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

package com.giaidoochu;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

import com.giaidoochu.CrosswordException.ExceptionType;


public class DownloadManager {

	private static void download(String in, String out) throws CrosswordException {
		InputStream input = null;
	    FileOutputStream writeFile = null;
	    Log.e("Download file: " + in,"aaaaaaaaaa");
	    // Telechargement du fichier
	    try
	    {
	        URL url = new URL(in);
	        System.out.println("Download file: " + url.toString());
	        URLConnection connection = url.openConnection();
	        int fileLength = connection.getContentLength();
	
	        if (fileLength == -1)
	        {
	            System.out.println("Invalide URL or file.");
	            throw new CrosswordException(ExceptionType.NETWORK);
	        }
	
	        input = connection.getInputStream();
	        writeFile = new FileOutputStream(out);
	        int read;
	        byte[] buffer = new byte[1024];
	        while ((read = input.read(buffer)) > 0)
	            writeFile.write(buffer, 0, read);
	        writeFile.flush();
	    }
	    catch (IOException e)
	    {
	        System.out.println("Error while trying to download the file.");
	        e.printStackTrace();
	    }
	    finally
	    {
	        try
	        {
	        	if (writeFile != null)
	        		writeFile.close();
	        	if (input != null)
	        		input.close();
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	    }
	}
	
	public static void downloadGrid(String filename) throws CrosswordException {
		DownloadManager.download(String.format(Crossword.GRID_URL, filename), String.format(Crossword.GRID_LOCAL_PATH, filename));
	}

	public static void downloadListGrid() throws CrosswordException {
		DownloadManager.download(Crossword.GRIDLIST_URL, Crossword.GRIDLIST_LOCAL_PATH);
	}
}
