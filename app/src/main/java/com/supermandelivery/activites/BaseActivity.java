package com.supermandelivery.activites;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

	private static ProgressDialog pd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}


	/** show the progress dialog*/
	public static void showProgress(Context mContext,String message) {
		pd = new ProgressDialog(mContext);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage(message);
		pd.setCancelable(false);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
	}
	
	/** show the progress update */
	public static void showProgressUpdate(String message){
		if(pd != null){
			pd.setMessage(message);			
		}
	}

	/**hide progress dialog*/
	public static void hideProgress() {
		if(pd != null){
			pd.dismiss();			
		}
	}

	//alert for leave album
	public void showOkAlert(String msg) {
		
		//final exit of application 
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg);

		builder.setPositiveButton(getString(android.R.string.ok),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}
}
