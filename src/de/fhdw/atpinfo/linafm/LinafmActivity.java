package de.fhdw.atpinfo.linafm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class LinafmActivity extends Activity implements OnClickListener {
	
	private Context context;
	private Button mBtnPlay, mBtnEnd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mBtnPlay = (Button)findViewById(R.id.play);
        mBtnEnd = (Button)findViewById(R.id.end);
        mBtnPlay.setOnClickListener(this);
        mBtnEnd.setOnClickListener(this);
    }
    
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

	private void openLevelChooserDialog() {
		context = LinafmActivity.this;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.levelchooser);
        dialog.setTitle(R.string.choose_level);
        dialog.setCancelable(true);
        
        ListView lv = (ListView) dialog.findViewById(R.id.lvLvl);
        String[] levelNames;
        try {
        	levelNames = LevelHandler.getLevelCaptionsForMenu(context);
        }
        catch (Exception ex) {
        	Tools.ShowErrorMessage(getString(android.R.string.dialog_alert_title), 
        			"Es ist ein Fehler beim Einlesen der Levelliste aufgetreten.", context);
        	return;
        }
		ArrayAdapter<String> menuItems = new ArrayAdapter<String>(this, R.layout.menu_item, levelNames);
        lv.setAdapter(menuItems);
             
        lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
        	public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
              newGame(position);
              dialog.dismiss();
            }
          });
        

        Button button = (Button) dialog.findViewById(R.id.btnCancel);
        button.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
   
        dialog.show();
	}
	
	private void newGame(int levelId) {
		// TODO
		Toast.makeText(context, "Neues Spiel wird gestartet, Level-ID: " + levelId, Toast.LENGTH_SHORT).show();
	}
}