package de.fhdw.atpinfo.linafm;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LinafmActivity extends Activity implements OnClickListener {
	
	private Button play;
	private Button end;
    /** Called when the activity is first created. */
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
		if (button.toString() == "end") {
			
		}
		
	}
}