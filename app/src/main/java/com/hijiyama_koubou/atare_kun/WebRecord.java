package com.hijiyama_koubou.atare_kun;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class WebRecord  extends DialogFragment {
	public AlertDialog.Builder builder;
	public String dTitol;
	public String dMsg;
	public String nowUrl;
	public String nowTitol;
	public float nowScale ;

//	@Override
	public WebRecord(String dTitol , String dMsg , String nowTitol , String nowUrl , float nowScale) {
			final String TAG = "WebRecord[WebRecord]";
			String dbMsg= "開始;";/////////////////////////////////////
			try{
				WebRecord.this.dTitol = dTitol;
				dbMsg= dbMsg + ",dTitol=" + dTitol;/////////////////////////////////////
				WebRecord.this.dMsg = dMsg;
				dbMsg= dbMsg + ",dTitol=" + dTitol;/////////////////////////////////////
				WebRecord.this.nowTitol = nowTitol;
				dbMsg= dbMsg + ",nowTitol=" + nowTitol;/////////////////////////////////////
				WebRecord.this.nowUrl = nowUrl;
				dbMsg= dbMsg + ",nowUrl=" + nowUrl;/////////////////////////////////////
				WebRecord.this.nowScale = nowScale;
				dbMsg= dbMsg + ",nowScale=" + nowScale;/////////////////////////////////////
				Log.i(TAG,dbMsg);
			}catch (Exception e) {
				Log.e(TAG,dbMsg + "で"+e.toString());
			}
		}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			final String TAG = "onCreate[WebRecord]";
			String dbMsg= "開始;";/////////////////////////////////////
			try{
				Log.i(TAG,dbMsg);
			}catch (Exception e) {
				Log.e(TAG,dbMsg + "で"+e.toString());
			}
		}


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final String TAG = "onCreate[WebRecord]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());		// Use the Builder class for convenient dialog construction
			LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View content = inflater.inflate(R.layout.web_record, null);
//			TextView wr_titoled = (TextView) content.findViewById(R.id.wr_titoled);
//			wr_titoled.setText(nowTitol);
//			TextView wr_urlet = (TextView) content.findViewById(R.id.wr_urlet);
//			wr_urlet.setText(nowUrl);
//			TextView wr_scaled = (TextView) content.findViewById(R.id.wr_scaled);
//			wr_urlet.setText(String.valueOf(nowScale));

			builder.setView(content);
			builder.setTitle(dTitol)
			.setMessage(dMsg)
			.setNegativeButton("閉じる", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {	// User cancelled the dialog
				}
		});	// Create the AlertDialog object and return it
			Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
		return builder.create();
	}

//http://qiita.com/ux_design_tokyo/items/61ca074566d1570b37d3


}