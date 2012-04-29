package de.fhdw.atpinfo.linafm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * "Toolbox" mit nützlichen Methoden
 * 
 * @author Esther Hentrich, Daniel Philipp, Alexander Brügmann
 * @version 0.1
 * 
 */
public class Tools {
	/**
	 * Ausgabe einer Fehlermeldung (Dialogfeld)
	 * 
	 * @param title Titel
	 * @param message Nachricht
	 * @param context der jeweilige Context
	 */
	public static void ShowErrorMessage(String title, String message, Context context)
	   {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
			   // hier passiert (noch?) nix...
		   }
		});
		alertDialog.setIconAttribute(android.R.attr.alertDialogIcon);
		alertDialog.show();
	   }
}
