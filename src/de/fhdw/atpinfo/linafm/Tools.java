package de.fhdw.atpinfo.linafm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Tools {
	public static void ShowErrorMessage(String title, String message, Context context)
	   {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {

		   }
		});
		alertDialog.setIconAttribute(android.R.attr.alertDialogIcon);
		alertDialog.show();
	   }
}
