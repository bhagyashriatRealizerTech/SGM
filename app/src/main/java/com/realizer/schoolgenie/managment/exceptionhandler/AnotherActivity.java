package com.realizer.schoolgenie.managment.exceptionhandler;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.realizer.schoolgenie.managment.R;
import com.realizer.schoolgenie.managment.utils.Config;

public class AnotherActivity extends Activity {

	TextView error;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.activity_excption);
		Config.alertDialog(AnotherActivity.this, "Sorry", "");


		AlertDialog.Builder	adbdialog = new AlertDialog.Builder(AnotherActivity.this);
		adbdialog.setTitle("Sorry");
		adbdialog.setMessage("The application School Genie Management has stopped unexpectedly. Please try again.");
		adbdialog.setIcon(android.R.drawable.ic_dialog_alert);
		adbdialog.setCancelable(false);
		adbdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(10);

			}
		});


		adbdialog.setNegativeButton("Report", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(10);
			}
		});
		adbdialog.show();
	}
}
