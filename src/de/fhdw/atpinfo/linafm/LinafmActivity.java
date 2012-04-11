package de.fhdw.atpinfo.linafm;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class LinafmActivity extends Activity implements OnClickListener {
	
	private Button play;
	private Button end;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        play = (Button)findViewById(R.id.play);
        end = (Button)findViewById(R.id.end);
        play.setOnClickListener(this);
        end.setOnClickListener(this);
    }
    
	@Override
	public void onClick(View button) {
		switch (button.getId()) {
		case R.id.play:
			// Hier passiert etwas...
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, "klick, klack... nichts passiert.", Toast.LENGTH_SHORT);
			toast.show();			
			break;
		case R.id.end:
			finish();
			break;
		}
	}
}