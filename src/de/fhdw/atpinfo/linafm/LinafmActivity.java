package de.fhdw.atpinfo.linafm;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
        String[] levelNames = LevelHandler.getLevelCaptionsForMenu();
        ArrayAdapter<String> menuItems = new ArrayAdapter<String>(this, R.layout.menu_item, levelNames);
        lv.setAdapter(menuItems);

        Button button = (Button) dialog.findViewById(R.id.btnCancel);
        button.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
   
        dialog.show();
	}
}