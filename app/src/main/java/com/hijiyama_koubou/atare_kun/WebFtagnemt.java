package com.hijiyama_koubou.atare_kun;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebFtagnemt extends Fragment {
	private String currentURL;
	public WebView webView;
    public WebSettings settings;
    public String dbBlock="";
    public String fName=null;
    public String MLStr="";
    public String dataURI="";
    public String fType="";
    public String baseUrl="";
    public boolean En_ZUP=true;			//ズームアップメニュー有効
    public boolean En_ZDW=true;			//ズームアップメニュー無効
    public boolean En_FOR=false;			//1ページ進む";
    public boolean En_BAC=false;			//1ページ戻る";

	//プリファレンス設定
	SharedPreferences myNFV_S_Pref;
	Editor pNFVeditor ;

	public static final int MENU_WQKIT=800;							//これメニュー
	public static final int MENU_WQKIT_ZUP = MENU_WQKIT+1;			//ズームアップ
	public static final int MENU_WQKIT_ZDW = MENU_WQKIT_ZUP+1;		//ズームダウン
	public static final int MENU_WQKIT_FOR = MENU_WQKIT_ZDW+1;		//1ページ進む
	public static final int MENU_WQKIT_BAC = MENU_WQKIT_FOR+1;		//1ページ戻る
	public static final int MENU_WQKIT_END = MENU_WQKIT_BAC+10;		//webkit終了

	public final CharSequence CTM_WQKIT_ZUP = "ズームアップ";
	public final CharSequence CTM_WQKIT_ZDW = "ズームダウン";
	public final CharSequence CTM_WQKIT_FOR  = "1ページ進む";
	public final CharSequence CTM_WQKIT_BAC = "1ページ戻る";
	public final CharSequence CTM_WQKIT_END = "表示終了";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final String TAG = "onCreate[WebFtagnemt]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
	//		Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

	public void init(String url) {
		final String TAG = "init[WebFtagnemt]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			dbMsg= "currentURL=" + url;/////////////////////////////////////
			currentURL = url;
			dbMsg= dbMsg + "webView=" + webView;/////////////////////////////////////
			if(webView != null){
				webView.loadUrl(url);
			}
	//		Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final String TAG = "onActivityCreated[WebFtagnemt]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
	//		Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = null;
		final String TAG = "onCreateView[WebFtagnemt]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			dbMsg= dbMsg + "inflater=" + inflater;
			dbMsg= dbMsg + ",container=" + container;
	//		setVerticalScrollbarOverlay(true); // 右端の余白を削除　☆Flagumentで使えない
			v = inflater.inflate(R.layout.wk_view, container, false);
			webView = (WebView) v.findViewById(R.id.webview);
			webView.setWebViewClient(new WebViewClient());
			settings = webView.getSettings();
			settings.setSupportZoom(true);								//ピンチ操作を有効化
			settings.setBuiltInZoomControls(true);						//ズームコントロールを表示し
			settings.setLightTouchEnabled(true);
			settings.setJavaScriptEnabled(true);						//JavaScriptを有効化

//			settings.setSupportMultipleWindows(true);
//			settings.setLoadsImagesAutomatically(true);
			settings.setUseWideViewPort(true);							// ワイドビューポート
			settings.setLoadWithOverviewMode(true);						// ズームアウト	ページ全体を読み込む
	//		webView.setWebViewClient(new SwAWebClient());

			dbMsg= dbMsg + "url=" + currentURL;
			if (currentURL != null) {
	//			Log.d("SwA", "Current URL  1["+currentURL+"]");
				updateUrl(currentURL , 1.0f);
			}
			dbMsg= dbMsg + ",inflater=" + v;

			dbMsg= dbMsg + ",settings=" + settings;/////////////////////////////////////
			dbMsg= dbMsg + ",getTitle=" + webView.getTitle();
			dbMsg= dbMsg + ",getUrl=" + webView.getUrl();
			dbMsg= dbMsg + ",getScale=" + webView.getScale();
			String userAgent = webView.getSettings().getUserAgentString();
	//		webView.getSettings().setUserAgentString(userAgent + "独自に指定したいユーザーエージェント");
			dbMsg= dbMsg + ",userAgent=" + userAgent;
	//		Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
		return v;
	}

	public void updateUrl(String url , float bairitu) {
		final String TAG = "updateUrl[WebFtagnemt]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			dbMsg=  "指定="+url+"] - View ["+getView()+"]";/////////////////////////////////////
			currentURL = url;
			dbMsg= dbMsg + ",指定倍率=" + bairitu;/////////////////////////////////////
			dbMsg= dbMsg + "webView=" + webView;/////////////////////////////////////
			if(webView != null){
				webView.clearCache(true);
				webView.loadUrl(url);
				dbMsg= dbMsg + "倍率=" + webView.getScale();/////////////////////////////////////
				dbMsg= dbMsg + ",指定=" + bairitu;/////////////////////////////////////
				if(bairitu != 1.0){
					zenGamen();				//全画面表示
				}
				dbMsg= dbMsg + "、倍率=" + webView.getScale();/////////////////////////////////////
			}
	//		Log.i(TAG, dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

	public void zenGamen() {				//全画面表示
		final String TAG = "zenGamen[WebFtagnemt]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			dbMsg= "倍率=" + webView.getScale();/////////////////////////////////////
			while( webView.zoomOut()){				//1<webView.getScale()
				dbMsg= dbMsg + ">>" + webView.getScale();/////////////////////////////////////
			}
			webView.setScaleX(1.0f);
			webView.setScaleY(1.0f);
			dbMsg= dbMsg + ">>" + webView.getScale();/////////////////////////////////////
			Log.i(TAG, dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

	public void wZUp() {				//webビューの拡大
		final String TAG = "wZUp[WebFtagnemt]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			dbMsg= "倍率=" + webView.getScale();/////////////////////////////////////
			webView.zoomIn();
			dbMsg= dbMsg + ">>" + webView.getScale();///////////////////////////////
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

	public void wZDown() {				//webビューの縮小
		final String TAG = "wZDown[WebFtagnemt]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			dbMsg= "倍率=" + webView.getScale();/////////////////////////////////////
			webView.zoomOut();
			dbMsg= dbMsg + ">>" + webView.getScale();///////////////////////////////
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

	public String getUrl() {
		String retStr = null;
		final String TAG = "getUrl[WebFtagnemt]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			dbMsg= dbMsg + ",getTitle=" + webView.getTitle();
			retStr =  webView.getUrl();
			dbMsg= dbMsg + ",getUrl=" + retStr;
	//		Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
		return retStr;
	}

	public String getTitle() {
		String retStr = null;
		final String TAG = "getTitle[WebFtagnemt]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			retStr =  webView.getTitle();
			dbMsg= dbMsg + ",getUrl=" + retStr;
			Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
		return retStr;
	}


	@SuppressWarnings("deprecation")
	public float getScale() {
		float retfl = 0;
		final String TAG = "getScale[WebFtagnemt]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			retfl = webView.getScale();
			dbMsg= dbMsg + ",getScale=" + retfl;
			Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
		return retfl;
	}


	//@Override
	public void onWindowFocusChanged(boolean hasFocus) {		//①ⅱヘッドのイメージは実際にローディンされた時点で設定表示と同時にウィジェットの高さや幅を取得したいときは大抵ここで取る。
		if (hasFocus) {
			final String TAG = "onWindowFocusChanged[WebFtagnemt]";
			String dbMsg= "開始;";/////////////////////////////////////
			try{
				dbMsg= dbMsg + ",getScale=" + webView.getScale();
				dbMsg= dbMsg + ",getTitle=" + webView.getTitle();
				dbMsg= dbMsg + ",getUrl=" + webView.getUrl();
				Log.i(TAG,dbMsg);
			}catch (Exception e) {
				Log.e(TAG,dbMsg + "で"+e.toString());
			}
		 }
	//	 super.onWindowFocusChanged(hasFocus);
	 }


	private DisplayMetrics getDisplayMetrics(Context context) {
		DisplayMetrics metrics = null;
		final String TAG = "updateUrl[WebFtagnemt]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			metrics = new DisplayMetrics();
			WindowManager windowManager = (WindowManager) context.getSystemService(Activity.WINDOW_SERVICE);
			Display display = windowManager.getDefaultDisplay();
			display.getMetrics(metrics);
			dbMsg= dbMsg + ",density=" + metrics.density;/////////////////////////////////////
			dbMsg= dbMsg + ",densityDpi=" + metrics.densityDpi;/////////////////////////////////////
			dbMsg= dbMsg + ",metrics=" + metrics;/////////////////////////////////////
			Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
		return metrics;
}

	private class SwAWebClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			final String TAG = "shouldOverrideUrlLoading[WebFtagnemt.SwAWebClient]";
			String dbMsg= "開始;";/////////////////////////////////////
			try{
				dbMsg= "WebView=" + view;
				dbMsg= dbMsg + "url=" + url;
				Log.i(TAG,dbMsg);
	//			view.resetZoom();
			}catch (Exception e) {
				Log.e(TAG,dbMsg + "で"+e.toString());
			}
			return false;
		}
	}
//http://www.survivingwithandroid.com/2013/03/android-fragment-tutorial-webview-example.html
}