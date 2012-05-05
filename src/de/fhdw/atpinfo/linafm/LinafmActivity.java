package de.fhdw.atpinfo.linafm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * linafm - LÜK Is Not A French Man
 * LÜK-Clone for Android 3.1
 * 
 * https://github.com/Brueggus/linafm
 * 
 * @author Esther Hentrich, Daniel Philipp, Alexander Brügmann
 * @version 0.1
 *
 */
public class LinafmActivity extends Activity implements OnClickListener {
	
	private Context context;
	private Button mBtnPlay, mBtnEnd;
	
	// Optionsmenü Credits
	static final int DIALOG_CREDITS_ID = 0;
	static final int DIALOG_HELP_ID = 1; 

    /**
     * Hier fängt alles an...
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		context = LinafmActivity.this;    	
		// Levelliste einlesen		
        try {
        	LevelHandler.refreshLevelCaptions(context);
        }
        catch (Exception ex) {
        	Tools.ShowErrorMessage(getString(android.R.string.dialog_alert_title), 
        			getString(R.string.error_reading_level_list), context);
        }
        setContentView(R.layout.main);
        
        mBtnPlay = (Button)findViewById(R.id.play);
        mBtnEnd = (Button)findViewById(R.id.end);
        
        //[DEBUG] Button farbig anzeigen fuer Positionierung
        //mBtnEnd.setBackgroundColor(Color.GREEN);
        //mBtnPlay.setBackgroundColor(Color.GREEN);
        
        mBtnPlay.setOnClickListener(this);
        mBtnEnd.setOnClickListener(this);
    }
    
	/**
	 * Acitivity kommt wieder in den Vordergrund
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// Levelliste aktualisieren
        try {
        	LevelHandler.refreshLevelCaptions(context);
        }
        catch (Exception ex) {
        	Tools.ShowErrorMessage(getString(android.R.string.dialog_alert_title), 
        			getString(R.string.error_reading_level_list), context);
        }
	}

	/**
	 * Klick auf einen der Buttons im Hauptmenü
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View button) {
		switch (button.getId()) {
		case R.id.play:
			openLevelChooserDialog();
			break;
		case R.id.end:
			finish();
			break;
		}
	}

	
	/**
	 * Dialog zur Levelauswahl wird geöffnet
	 */
	private void openLevelChooserDialog() {
        // Neuen Dialog initialisieren
		final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.levelchooser);
        dialog.setTitle(R.string.choose_level);
        dialog.setCancelable(true);
        
        // ListView des Dialogs mit den Namen der Level füllen
        ListView lv = (ListView) dialog.findViewById(R.id.lvLvl);
        String[] levelNames;
        try {
        	levelNames = LevelHandler.getLevelCaptionsForMenu(context);
        }
        catch (Exception ex) {
        	Tools.ShowErrorMessage(getString(android.R.string.dialog_alert_title), 
        			getString(R.string.error_reading_level_list), context);
        	return;
        }
		ArrayAdapter<String> menuItems = new ArrayAdapter<String>(this, R.layout.menu_item, levelNames);
        lv.setAdapter(menuItems);

        // Bei Auswahl eines Levels wird das jeweilige Level gestartet und der Dialog geschlossen
        lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
        	public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
              newGame(position);
              dialog.dismiss();
            }
          });
        
        // Abbrechen-Button
        Button button = (Button) dialog.findViewById(R.id.btnCancel);
        button.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
   
        dialog.show();
	}
	
	
	/**
	 * Neues Spiel starten
	 * 
	 * @param levelId ID des gewählten Levels
	 */
	private void newGame(int levelId) {
		// TODO
		//Toast.makeText(context, "Neues Spiel wird gestartet, Level-ID: " + levelId, Toast.LENGTH_SHORT).show();
		Intent i = new Intent(context, Spiel.class);
		
		// Wir geben der neuen Activity die Level-ID mit
		i.putExtra("levelId", levelId);

		startActivity(i);
	}
	
	
	/**
	 * Optionsmenü erzeugen
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.optionmenu, menu);
		return true;
	}

	/**
	 * Erlaubt uns die Items des Optionsmenüs auszuwählen
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Abhandeln des Item-Auswahl (derzeit nur ein Item vorhanden)
		switch(item.getItemId()) {
		case R.id.credits:
			createOptionDialog(DIALOG_CREDITS_ID);
			return true;
		case R.id.help: 
			createOptionDialog(DIALOG_HELP_ID);
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public Dialog createOptionDialog(int id) {
		AlertDialog dialog = null;
		switch(id) {
		case DIALOG_CREDITS_ID:
			LayoutInflater li = LayoutInflater.from(context);
			View view = li.inflate(R.layout.simpledialog, null);

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(getString(R.string.credits));
			builder.setView(view);
			builder.setMessage(Html.fromHtml(getString(R.string.credits_text)));
			builder.show();
			break;
		case DIALOG_HELP_ID:
			// NYI , aber analog zum Credits AlertDialog, Text entwerfen, anpassen
			break;
		default: 
			dialog = null;
		}
		return dialog;
	}

}
