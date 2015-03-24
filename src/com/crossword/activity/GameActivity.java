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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.xml.sax.helpers.DefaultHandler;

import com.crossword.Crossword;
import com.crossword.CrosswordException;
import com.crossword.R;
import com.crossword.SAXFileHandler;
import com.crossword.keyboard.KeyboardView;
import com.crossword.keyboard.KeyboardViewInterface;
import com.crossword.parser.CrosswordParser;
import com.crossword.parser.GridParser;
import com.crossword.adapter.GameGridAdapter;
import com.crossword.data.Grid;
import com.crossword.data.Word;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends CrosswordParentActivity implements OnTouchListener, KeyboardViewInterface {

	public enum GRID_MODE {NORMAL, CHECK, SOLVE};
	public GRID_MODE currentMode = GRID_MODE.NORMAL;
	
	private GridView 		gridView;
	private KeyboardView 	keyboardView;
	private GameGridAdapter gridAdapter;
	private TextView 		txtDescription;
	private TextView 		txtUserinfo;
	private TextView 		keyboardOverlay;

	private Grid			grid;
	private ArrayList<Word> entries;		// Liste des mots
	private ArrayList<View>	selectedArea = new ArrayList<View>(); // Liste des cases selectionnées

	private boolean			downIsPlayable;	// false si le joueur à appuyé sur une case noire 
	private int 			downPos;		// Position ou le joueur à appuyé
    private int 			downX;			// Ligne ou le joueur à appuyé
    private int 			downY;			// Colonne ou le joueur à appuyé
	private int 			currentPos;		// Position actuelle du curseur
	private int 			currentX;		// Colonne actuelle du curseur
	private int 			currentY;		// Ligne actuelle du curseur
	private Word			currentWord;	// Mot actuellement selectionné
	private boolean 		horizontal;		// Sens de la selection

	private String 			filename;		// Nom de la grille

	private boolean 		solidSelection;	// PREFERENCES: Selection persistante
	private boolean			gridIsLower;	// PREFERENCES: Grille en minuscule
	
	private int width;
	private int height;

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.crossword, menu);
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	MenuItem itemCheck = menu.findItem(R.id.menu_check);
    	MenuItem itemSolve = menu.findItem(R.id.menu_solve);
    	itemCheck.setIcon(preferences.getBoolean("grid_check", false) ? R.drawable.ic_menu_check_enable : R.drawable.ic_menu_check);
    	itemSolve.setIcon(currentMode == GRID_MODE.SOLVE ? R.drawable.ic_menu_solve_enable : R.drawable.ic_menu_solve);
		return true;
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        switch (item.getItemId()) {
        case R.id.menu_check:
    		boolean checked = !preferences.getBoolean("grid_check", false);
    		preferences.edit().putBoolean("grid_check", checked).commit();
        	if (currentMode != GRID_MODE.SOLVE) {
        		currentMode = checked ? GRID_MODE.CHECK : GRID_MODE.NORMAL;
        	}
        	this.gridAdapter.notifyDataSetChanged();
        	return true;
        case R.id.menu_solve:
        	if (currentMode == GRID_MODE.SOLVE)
        		currentMode = (preferences.getBoolean("grid_check", false) ? GRID_MODE.CHECK : GRID_MODE.NORMAL);
        	else
        		currentMode = GRID_MODE.SOLVE;
        	this.gridAdapter.notifyDataSetChanged();
        	return true;
        case R.id.menu_grid:
        	Intent intent = new Intent(this, GridActivity.class);
    		intent.putExtra("grid", grid);
        	startActivity(intent);
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	@Override
	public void onPause()
	{
		save();
		super.onPause();
	}
 
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	switch (requestCode) {
    	case Crossword.REQUEST_PREFERENCES:
    		if (Crossword.DEBUG) Toast.makeText(this, "PREFERENCES_UPDATED", Toast.LENGTH_SHORT).show();
    		readPreferences();
    		this.gridAdapter.setLower(this.gridIsLower);
    		this.gridAdapter.notifyDataSetChanged();
        	break;
    	}
	}

	private void readPreferences() {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		this.solidSelection = preferences.getBoolean("solid_selection", false);
		this.gridIsLower = preferences.getBoolean("grid_is_lower", false);
		if (currentMode != GRID_MODE.SOLVE)
			currentMode = preferences.getBoolean("grid_check", false) ? GRID_MODE.CHECK : GRID_MODE.NORMAL;
		
	}

	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.game);
	    
		readPreferences();
	    
	    try {
		    this.filename = getIntent().getExtras().getString("filename");
			File file = new File(String.format(Crossword.GRID_LOCAL_PATH, this.filename));
			if (file.exists())
			{
				// Get grid meta informations (name, author, date, level)
				GridParser gridParser = new GridParser();
				SAXFileHandler.read((DefaultHandler)gridParser, String.format(Crossword.GRID_LOCAL_PATH, this.filename));
				this.grid = gridParser.getData();
			    if (this.grid == null) {
			    	finish();
			    	return;
			    }

			    // Get words information (word, tmp and description)
			    CrosswordParser crosswordParser = new CrosswordParser();
				SAXFileHandler.read((DefaultHandler)crosswordParser, String.format(Crossword.GRID_LOCAL_PATH, this.filename));
				this.entries = crosswordParser.getData();
			    if (this.entries == null) {
			    	finish();
			    	return;
			    }
			}
			else
			{
		    	finish();
		    	return;
			}
		} catch (CrosswordException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	    
	    this.width = this.grid.getWidth();
	    this.height = this.grid.getHeight();

	    Display display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int keyboardHeight = (int)(height / 4.4);
		
		this.txtDescription = (TextView)findViewById(R.id.description);
		this.txtUserinfo = (TextView)findViewById(R.id.userinfo);
        this.gridView = (GridView)findViewById(R.id.grid);
        this.gridView.setOnTouchListener(this);
        this.gridView.setNumColumns(this.width);
        android.view.ViewGroup.LayoutParams gridParams = this.gridView.getLayoutParams();
        gridParams.height = height - keyboardHeight - this.txtDescription.getLayoutParams().height;
        this.gridView.setLayoutParams(gridParams);
        this.gridView.setVerticalScrollBarEnabled(false);
		this.gridAdapter = new GameGridAdapter(this, this.entries, this.width, this.height);
		this.gridView.setAdapter(this.gridAdapter);

        this.keyboardView = (KeyboardView)findViewById(R.id.keyboard);
        this.keyboardView.setDelegate(this);
        android.view.ViewGroup.LayoutParams KeyboardParams = this.keyboardView.getLayoutParams();
        KeyboardParams.height = keyboardHeight;
        this.keyboardView.setLayoutParams(KeyboardParams);

        this.keyboardOverlay = (TextView)findViewById(R.id.keyboard_overlay);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
            	int position = this.gridView.pointToPosition((int)event.getX(), (int)event.getY());
            	View child = this.gridView.getChildAt(position);

            	// Si pas de mot sur cette case (= case noire), aucun traitement
            	if (child == null || child.getTag().equals(GameGridAdapter.AREA_BLOCK)) {
            		if (this.solidSelection == false) {
                        clearSelection();
                    	this.gridAdapter.notifyDataSetChanged();
            		}
            			
            		this.downIsPlayable = false;
            		return true;
            	}
        		this.downIsPlayable = true;

            	// Stocke les coordonnees d'appuie sur l'ecran
            	this.downPos = position;
                this.downX = this.downPos % this.width;
                this.downY = this.downPos / this.width;
                System.out.println("ACTION_DOWN, x:" + this.downX + ", y:" + this.downY + ", position: " + this.downPos);

                clearSelection();
                
            	// Colore la case en bleu
            	child.setBackgroundResource(R.drawable.area_selected);
            	selectedArea.add(child);

            	this.gridAdapter.notifyDataSetChanged();
        		break;
            }

            case MotionEvent.ACTION_UP:
            {
            	// Si le joueur à appuyé sur une case noire, aucun traitement 
            	if (this.downIsPlayable == false)
            		return true;
            	
                int position = this.gridView.pointToPosition((int)event.getX(), (int)event.getY());
                int x = position % this.width;
                int y = position / this.width;
                System.out.println("ACTION_DOWN, x:" + x + ", y:" + y + ", position: " + position);

            	// Si clique sur la case, inversion horizontale <> verticale
                // Si clique sur une autre case (= mouvement) calcul en fonction de la gesture
            	if (this.downPos == position && this.currentPos == position)
            	{
            		this.horizontal = !this.horizontal;
            	}
            	else if (this.downPos != position) 
            	{
            		this.horizontal = (Math.abs(this.downX - x) > Math.abs(this.downY - y));
            	}

            	// Test si un mot se trouve sur cette case
                this.currentWord = getWord(this.downX, this.downY, this.horizontal);
        	    if (this.currentWord == null)
        	    	break;
        	    
        	    // Force la direction a etre dans le meme sens que le mot
        	    this.horizontal = this.currentWord.getHorizontal();
                
            	// Si clique sur la case, place le curseur sur le mot
                // Sinon place le curseur au debut du mot
            	if (this.downPos == position)
            	{
            	    this.currentX = this.downX;
                    this.currentY = this.downY;
                	this.currentPos = position;
            	}
            	else
            	{
            	    this.currentX = this.currentWord.getX();
                    this.currentY = this.currentWord.getY();
                	this.currentPos = this.currentY * this.width + this.currentX;
            	}

            	this.txtDescription.setText(this.currentWord.getDescription());
            	//int tempx=this.currentX;
            	//int tempy= this.currentY;
            	//if (this.horizontal) tempx=0; else tempy=0;
            	//String tempcurrentword=this.gridAdapter.getWord(tempx, tempy, this.currentWord.getText().length(), this.horizontal);
//            	if (gridAdapter.isTheSame(this.currentX, this.currentY,this.horizontal)){
//            		this.txtUserinfo.setText("Giong nhau "+gridAdapter.getTempWord1()+"="+gridAdapter.getTempWord2());
//            	}else{
//            		this.txtUserinfo.setText("khac nhau "+gridAdapter.getTempWord1()+"!="+gridAdapter.getTempWord2());
//            	}
            	
        	    // Set background color
        	    boolean horizontal = this.currentWord.getHorizontal();
        	    for (int l = 0; l < this.currentWord.getLength(); l++) {
        	    	int index = this.currentWord.getY() * this.width + this.currentWord.getX() + (l * (horizontal ? 1 : this.width));
        	    	View currentChild = this.gridView.getChildAt(index);
        	    	if (currentChild != null) {
        	    		currentChild.setBackgroundResource(index == this.currentPos ? R.drawable.area_current : R.drawable.area_selected);
        	    		selectedArea.add(currentChild);
        	    	}
        	    }

        	    this.gridAdapter.notifyDataSetChanged();
        	    break;
            }
        }
        // if you return false, these actions will not be recorded
        return true;
	}
	
	// Remet les anciennes case selectionnees dans leur etat normal
    private void clearSelection() {
    	for (View selected: selectedArea)
    		selected.setBackgroundResource(R.drawable.area_empty);
    	selectedArea.clear();
	}

	private Word getWord(int x, int y, boolean horizontal)
    {
        Word horizontalWord = null;
        Word verticalWord = null;
	    for (Word entry: this.entries) {
	    	if (x >= entry.getX() && x <= entry.getXMax())
	    		if (y >= entry.getY() && y <= entry.getYMax()) {
        	    	if (entry.getHorizontal())
        	    		horizontalWord = entry;
        	    	else
        	    		verticalWord = entry;
	    		}
	    }
	    
	    if (horizontal)
	    	return (horizontalWord != null) ? horizontalWord : verticalWord;
	    else
	    	return (verticalWord != null) ? verticalWord : horizontalWord;
	}

	@Override
	public void onKeyDown(String value, int location[], int width) {
		System.out.println("onKeyDown: " + value + ", insert in: " + currentX + "x" + currentY);

		// Deplace l'overlay du clavier
		if (value.equals(" ") == false) {
			int offsetX = (this.keyboardOverlay.getWidth() - width) / 2;
			int offsetY = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Crossword.KEYBOARD_OVERLAY_OFFSET, getResources().getDisplayMetrics());
			FrameLayout.LayoutParams lp = (LayoutParams)this.keyboardOverlay.getLayoutParams();
			lp.leftMargin = location[0] - offsetX;
			lp.topMargin = location[1] - offsetY;
			this.keyboardOverlay.setLayoutParams(lp);
			this.keyboardOverlay.setText(value);
			this.keyboardOverlay.clearAnimation();
			this.keyboardOverlay.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onKeyUp(String value) {
		System.out.println("onKeyUp: " + value + ", insert in: " + currentX + "x" + currentY);

		// Efface l'overlay du clavier
		if (value.equals(" ") == false) {
			this.keyboardOverlay.setAnimation(AnimationUtils.loadAnimation(this, R.anim.keyboard_overlay_fade_out));
			this.keyboardOverlay.setVisibility(View.INVISIBLE);
		}

		// Si aucun mot selectionne, retour
		if (this.currentWord == null)
			return;

		// Case actuelle
		int x = this.currentX;
		int y = this.currentY;

		// Si la case est noire => retour
		if (this.gridAdapter.isBlock(x, y))
			return;
		
		// Ecrit la lettre sur le "curseur"
		this.gridAdapter.setValue(x, y, value);
		this.gridAdapter.notifyDataSetChanged();
		
		// Deplace sur le "curseur" sur la case precendante (effacer), ou suivante (lettres)
		if (value.equals(" ")) {
			x = (this.horizontal ? x - 1 : x);
			y = (this.horizontal ? y: y - 1);
		}
		else
		{
			x = (this.horizontal ? x + 1 : x);
			y = (this.horizontal ? y: y + 1);
		}
		if (gridAdapter.isTheSame(this.currentX, this.currentY,this.horizontal)){
    		this.txtUserinfo.setText("Giong nhau "+gridAdapter.getTempWord1()+"="+gridAdapter.getTempWord2());
    	}else{
    		this.txtUserinfo.setText("khac nhau "+gridAdapter.getTempWord1()+"!="+gridAdapter.getTempWord2());
    	}
		// Si la case suivante est disponible, met la case en jaune, remet l'ancienne en bleu, et set la nouvelle position
		if (x >= 0 && x < this.width
				&& y >= 0 && y < this.height
				&& this.gridAdapter.isBlock(x, y) == false) {
			this.gridView.getChildAt(y * this.width + x).setBackgroundResource(R.drawable.area_current);
			this.gridView.getChildAt(this.currentY * this.width + this.currentX).setBackgroundResource(R.drawable.area_selected);
			this.currentX = x;
			this.currentY = y;
		}
	}
	
    private void save() {
		// writre new XML file

    	StringBuffer wordHorizontal = new StringBuffer();
    	StringBuffer wordVertical = new StringBuffer();
	    for (Word entry: this.entries) {
	    	int x = entry.getX();
	    	int y = entry.getY();
    	    String word = String.format(
    	    		"<word x=\"%d\" y=\"%d\" description=\"%s\" tmp=\"%s\">%s</word>\n",
    	    		x,
    	    		y,
    	    		entry.getDescription(),
    	    		this.gridAdapter.getWord(x, y, entry.getLength(), entry.getHorizontal()),
    	    		entry.getText());
    	    if (entry.getHorizontal())
    	    	wordHorizontal.append(word);
    	    else
    	    	wordVertical.append(word);
	    }

    	StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		sb.append("<grid>\n");
		sb.append("<name>" + grid.getName() + "</name>\n");
		sb.append("<description>" + grid.getDescription() + "</description>\n");
		if (this.grid.getDate() != null)
			sb.append("<date>" + new SimpleDateFormat("dd/MM/yyyy").format(grid.getDate()) + "</date>\n");
		sb.append("<author>" + grid.getAuthor() + "</author>\n");
		sb.append("<level>" + grid.getLevel() + "</level>\n");
		sb.append("<percent>" + gridAdapter.getPercent()+"</percent>\n");
		sb.append("<width>" + this.width + "</width>\n");
		sb.append("<height>" + this.height + "</height>\n");
		sb.append("<horizontal>\n");
		sb.append(wordHorizontal);
		sb.append("</horizontal>\n");
		sb.append("<vertical>\n");
		sb.append(wordVertical);
		sb.append("</vertical>\n");
		sb.append("</grid>\n");
		
		// Make directory if not exists
		File directory = new File(Crossword.GRID_DIRECTORY);
		if (directory.exists() == false)
			directory.mkdir();
		
		// Write XML
		FileWriter file;
		try {
			file = new FileWriter(String.format(Crossword.GRID_LOCAL_PATH, this.filename));
			file.write(sb.toString());
			file.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		System.out.println(sb);
	}

	@Override
	public void setDraft(boolean value) {
		this.gridAdapter.setDraft(value);
	}

}
