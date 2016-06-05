package com.hijiyama_koubou.atare_kun;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class plogTask extends AsyncTask<Object, Integer, AsyncTaskResult<int[]>> {				//元はk<Object, Integer, Boolean>
//第一引数;タスク開始時:doInBackground()に渡す引数の型　,　第二引数;進捗率を表示させるとき:onProgressUpdate()に使う型　,　第三引数;タスク終了時のdoInBackground()の返り値の型
	private plogTaskCallback callback;

	AtarekunnActivity AaA = new AtarekunnActivity();  //オブジェクトNakedFileVeiwActivityの生成
	public Context cContext = null;	//
	public ProgressDialog pDialog = null;	// 処理中ダイアログ
	public CharSequence pdTitol=AaA.pdTitol;			//ProgressDialog のタイトルを設定
	public CharSequence pdMessage=AaA.pdMessage;			//ProgressDialog のメッセージを設定
	public int pdMaxVal=AaA.pdMaxVal;					//ProgressDialog の最大値を設定 (水平の時)
	public int pdStartVal=0;					//ProgressDialog の初期値を設定 (水平の時)
	public int pdCoundtVal=0;					//ProgressDialog表示値
//			public int pdSecundVal;					//ProgressDialog のセカンダリ値を設定 (水平の時)
//			public int pdSecundVal2;
	public Boolean preExecuteFiniSh=false;	//ProgressDialog生成終了
//	public String dbBlock="";
	public Bundle extras;

	final String TAG = "plogTask";

	public plogTask(Context cContext,plogTaskCallback callback){
		try{
			String dbBlock = "cContext="+cContext.getPackageName();///////////////////////////
	//		Log.d("plogTask",dbBlock);
			this.cContext = cContext;
			this.callback = callback;			//AsyncTaskからAcitivtyへの情報の伝達はコールバックを使う

			Log.d("onPreExecute", dbBlock);
	//		if(pDialog == null){
				pDialog = new ProgressDialog(cContext);            // 進捗ダイアログ表示	AtarekunnActivity.this		view.getContext()
	//		}
			pDialog.setTitle(pdTitol);
			pDialog.setMessage(pdMessage);
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pDialog.setProgress(pdStartVal);
				pDialog.setTitle(String.valueOf(pdTitol));
				pDialog.setMessage(String.valueOf(pdMessage));
				pDialog.setMax(pdMaxVal);
			pDialog.setProgress(pdStartVal);
			pDialog.setMax(pdMaxVal);
////			pDialog.setSecondaryProgress(pdSecundVal);
		} catch (Exception e) {
			Log.e("plogTask","でエラー発生；"+e.toString());
		}
	}

	@Override
	protected void onPreExecute() {        // タスク開始前処理：仮でダイアログを作成
		String dbBlock="";
		try {
			dbBlock = "pdTitol="+pdTitol+",pdMessage="+pdMessage+",pdMaxVal="+pdMaxVal;///////////////////////////
			pDialog.show();
			preExecuteFiniSh=true;	//ProgressDialog生成終了
		} catch (Exception e) {
			Log.e("onPreExecute","でエラー発生；"+e.toString());
		}
	}


	@Override
	public AsyncTaskResult<int[]> doInBackground(Object... params) {        //続けて呼ばれる処理；第一引数が反映されるのはここなのでここからダイアログ更新 バックスレッドで実行する処理;getProgress=0で呼ばれている
		int[] ruisekiArray=null;
		final String TAG = "doInBackground";
		String dbBlock="";
		try {
			pdTitol=(CharSequence) params[0];			//ProgressDialog のタイトルを設定
			pdMessage=(CharSequence) params[1];			//ProgressDialog のメッセージを設定
			pdMaxVal=(Integer) params[2];					//ProgressDialog の最大値を設定 (水平の時)
			int randumStart=(Integer) params[3];													//乱数の開始値
			int randumEnd=(Integer) params[4];													//乱数の終了値
			int val_val=(Integer) params[5];															//乱数の個数
			ruisekiArray= (int[]) params[6];
			dbBlock= "pdTitol="+pdTitol+",pdMessage="+pdMessage+",pdMaxVal="+pdMaxVal+";"+randumStart+"～"+randumEnd+"の乱数を"+val_val+"個";///////////////////////////+params[0]
	//		Log.d("doInBackground", dbBlock);
			pDialog.setTitle(pdTitol);
			pDialog.setMessage(pdMessage);
			pDialog.setMax(pdMaxVal);
//			pDialog.setSecondaryProgress(pdSecundVal);
			dbBlock = dbBlock+"getMax="+pDialog.getMax();///////////////////////////
			dbBlock= dbBlock+"、params="+params[0].toString()+"("+params.length+")";///////////////////////////+params[0]
			Log.i(TAG, dbBlock);
			while(pDialog.getProgress()<pDialog.getMax()){
				if(pDialog.getProgress()==0){								//カウント指定が無ければ
					dbBlock= "pDialog.getProgress()="+pDialog.getProgress();///////////////////////////
					publishProgress(pDialog.getProgress()+1);	//カウント指定が無ければ既存値のカウントアップ
				}else{
					dbBlock= "ダイアログを表示しながら";///////////////////////////
					ruisekiArray=AaA.ruisekiBody(ruisekiArray);
					for(int i=1;i<ruisekiArray.length;i++){			//最大値を探す		//for(int i=0;i<ruisekiTBL.length;i++){
						if(pdCoundtVal<= ruisekiArray[i]){					//if(ruisekiMax<= ruisekiTBL[i]){
							pdCoundtVal=ruisekiArray[i];								//					ruisekiMax=objUser.getRuisekiTBL(i);								//uisekiMax=ruisekiTBL[i];
							dbBlock=dbBlock+(i+1)+";"+ruisekiArray[i]+",";
						}
					}
					dbBlock= "[最大="+pdCoundtVal+"]"+dbBlock;///////////////////////////
		//			Log.d("doInBackground",dbBlock);
					if(pdCoundtVal==0){
						break;
					}
					onProgressUpdate(pdCoundtVal);				//指定した処理からの指定カウント受け取り
					publishProgress(pdCoundtVal);				//指定した処理からの指定カウント受け取り
				}
			}
	//		Log.d("doInBackground",dbBlock);
			return AsyncTaskResult.createNormalResult(ruisekiArray);
		} catch (Exception e) {
			Log.e(TAG,dbBlock+"；"+e.toString());
		}
		return null;
	}
	// プログレスバー更新処理： UIスレッドで実行される
	@Override
	protected void onProgressUpdate(Integer... values) {
		String dbBlock="";
		try{
			dbBlock= "values="+values[0]+"⇒"+pDialog.getProgress()+"/"+pDialog.getMax();///////////////////////////
	//		Log.d("onProgressUpdate",dbBlock);
			pDialog.setProgress(values[0]);			//values[0
//			pDialog.incrementSecondaryProgressBy(pSecundVal); 		// セカンダリプログレスバーのProgressを指定値分増加させます。
			//         Log.d("onPreExecute", "values= " + values[0]);
		} catch (Exception e) {
			Log.e("onProgressUpdate",dbBlock+"；"+e.toString());
		}
	}


	//@Override
	public void onPostExecute(AsyncTaskResult<int[]> result)  {	// タスク終了後処理：UIスレッドで実行される
		String dbBlock="";
		try{
			dbBlock=pDialog.getProgress()+"/"+pDialog.getMax()+";";///////////////////////////
			Log.d("onPostExecute", dbBlock);
			pDialog.dismiss();							// 進捗ダイアログをクローズ
			pDialog=null;
			preExecuteFiniSh=false;						//ProgressDialog破棄
			if (result.isError()) {
				callback.onFailedDownloadImage(result.getResourceId());		//エラーをコールバックで返す
			} else {
				callback.onSuccessplogTask(result.getContent());		 // ダウンロードした画像コールバックでを返す
			}

		} catch (Exception e) {
			Log.e("onPostExecute","でエラー発生；"+e.toString());
		}
	}

	@Override
	protected void onCancelled() {
		Log.d("onCancelled", "onCancelled");
		pDialog.dismiss();
	}

}
