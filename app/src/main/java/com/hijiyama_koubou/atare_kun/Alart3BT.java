package com.hijiyama_koubou.atare_kun;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Alart3BT extends Activity {

	private AlertDialog alertDialog;
	private static CharSequence dTitol = null;		//ダイアログタイトル
	private static CharSequence dMessage = null;		//アラート文
	private static CharSequence Msg1 = null;			//ボタン1のキーフェイス
	private static CharSequence Msg2 = null;			//ボタン2のキーフェイス
	private static CharSequence Msg3 = null;			//ボタン3のキーフェイス
	private int retInt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final String TAG = "onCreate";
		String dbBlock="";
		try{
			dbBlock="(MyPlogres)スタート";/////////////////////////////////////////////////////////////////////////////////////
			Log.d(TAG,dbBlock);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);		// アラートダイアログのタイトルを設定します
			final Intent rData = new Intent();
			Bundle extras = getIntent().getExtras();
			dTitol=extras.getString("dTitol");				//ダイアログタイトル
			dMessage=extras.getString("dMessage");			//アラート文
			Msg1=extras.getString("Msg1");					//ボタン1のキーフェイス
			Msg2=extras.getString("Msg2");					//ボタン2のキーフェイス
			Msg3=extras.getString("Msg3");					//ボタン3のキーフェイス
			rData.putExtras(extras);
			dbBlock="(MyPlogres)dTitol="+dTitol+",dMessage="+dMessage+",Msg1="+Msg1+",Msg2="+Msg2+",Msg3="+Msg3;/////////////////////////////////////////////////////////////////////////////////////
			Log.d(TAG,dbBlock);
			alertDialogBuilder.setTitle(dTitol)
				.setMessage(dMessage)        // アラートダイアログのメッセージを設定します
				.setPositiveButton(Msg1,new DialogInterface.OnClickListener() {	// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
					//	@Override
						public void onClick(DialogInterface dialog, int which) {
							retInt = 1;
							setResult(RESULT_OK, rData);		//RESULT_OK=1 (0xffffffff)
							closeMe();				//ダイアログとこのクラスを破棄
							return;
						}
					});       // アラートダイアログの中立ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
			if(null != Msg2){
				alertDialogBuilder.setNeutralButton(Msg2,new DialogInterface.OnClickListener() {
					//	@Override
						public void onClick(DialogInterface dialog, int which) {
							retInt = 9;
							setResult(9, rData);
							closeMe();				//ダイアログとこのクラスを破棄
							return;
						}
					});        // アラートダイアログの否定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
			}
			if(null != Msg3){
				alertDialogBuilder.setNegativeButton(Msg3,new DialogInterface.OnClickListener() {
				//	@Override
					public void onClick(DialogInterface dialog, int which) {
						retInt = 3;
						setResult(RESULT_CANCELED, rData);		//RESULT_CANCELED=0 (0x00000000)
						closeMe();				//ダイアログとこのクラスを破棄
						return;
					}
				});
			}
			alertDialogBuilder.setCancelable(true)        // アラートダイアログのキャンセルが可能かどうかを設定します
				.show();
			alertDialog = alertDialogBuilder.create();        // アラートダイアログを表示します
			alertDialog.show();
		} catch (Exception e) {
			Log.e("onCreate","[Alart3BT]エラー発生；"+e);
		}
	}

	public void closeMe() {				//ダイアログとこのクラスを破棄
		Log.d("closeMe","[Alart3BT]でコールされました");
		try{
			alertDialog.dismiss();
			Alart3BT.this.finish();
		} catch (Exception e) {
			Log.e("closeMe","[Alart3BT]エラー発生；"+e);
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d("onRestart","onRestartが[Alart3BT]で発生");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("onResume","onResumeが[Alart3BT]で発生");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d("onStart","onStartが[Alart3BT]で発生");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("onPause","onPauseが[Alart3BT]で発生");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("onStop","onStopが[Alart3BT]で発生");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("onDestroy","onDestroyが[Alart3BT]で発生");
		closeMe();				//ダイアログとこのクラスを破棄
	}
}
