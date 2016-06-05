package com.hijiyama_koubou.atare_kun;

import java.io.File;
import java.util.Locale;

import net.nend.android.NendAdListener;
import net.nend.android.NendAdView;
import net.nend.android.NendAdInterstitial.NendAdInterstitialStatusCode;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
//import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Build;
//import android.graphics.Picture;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
//import android.view.View;
//import android.view.Window;				//タイトルバーに文字列を設定
//import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
//import android.webkit.WebView.PictureListener;
import android.webkit.WebViewClient;
//インターネットに出るにはAndroidManifest.xmlを開きandroid.permission.INTERNET
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class wKit extends Activity {			//ActionBarActivityではクラッシュした

	public Toolbar tool_bar;		//Toolbar
	public WebView webView;
	public WebSettings settings;
	public String dbMsg="";
	public String fName=null;
	public String MLStr="";
	public String dataURI="";
	public String fType="";
	public String baseUrl="";
	public boolean En_ZUP=true;			//ズームアップメニュー有効
	public boolean En_ZDW=true;			//ズームアップメニュー無効
	public boolean En_FOR=false;			//1ページ進む";
	public boolean En_BAC=false;			//1ページ戻る";
	public int dWidth;														//ディスプレイ幅
	public int dHeight;														//ディスプレイ高

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
	protected void onCreate(Bundle savedInstanceState) {		//org;publicvoid
		super.onCreate(savedInstanceState);
		final String TAG = "onCreate[wKit]";
		String dbMsg="開始";
		try{
			Bundle extras = getIntent().getExtras();
			dataURI = extras.getString("dataURI");						//最初に表示するページのパス
			baseUrl = "file://"+extras.getString("baseUrl");				//最初に表示するページを受け取る
			fType = extras.getString("fType");							//データタイプ
			String[] testSrA=dataURI.split(File.separator);
			fName=testSrA[testSrA.length-1];
			dbMsg = "dataURI="+dataURI+",fType="+fType+",fName="+fName+",baseUrl="+baseUrl;////////////////////////////////////////////////////////////////////////
			Log.d(TAG,dbMsg);
			//		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 		//ローディングをタイトルバーのアイコンとして表示☆リソースを読み込む前にセットする
			WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);			// ウィンドウマネージャのインスタンス取得
			Display disp = wm.getDefaultDisplay();										// ディスプレイのインスタンス生成
			dWidth = disp.getWidth();								//ディスプレイ幅
			dHeight = disp.getHeight();							//ディスプレイ高
			dbMsg=dbMsg + ",ディスプレイサイズ="+dWidth+"×"+dHeight;/////////////////////////////////////////////////////////////////////////////////////////////////////////
			setContentView(R.layout.wk_view);
			webView = (WebView) findViewById(R.id.webview);		// Webビューの作成
			webView.setVerticalScrollbarOverlay(true);					//縦スクロール有効
//			setProgressBarIndeterminateVisibility(true);

			settings = webView.getSettings();
			settings.setSupportMultipleWindows(true);
			settings.setLoadsImagesAutomatically(true);
			settings.setBuiltInZoomControls(true);						//ズームコントロールを表示し
			settings.setSupportZoom(true);								//ピンチ操作を有効化
			settings.setLightTouchEnabled(true);
			settings.setJavaScriptEnabled(true);						//JavaScriptを有効化
			settings.setUseWideViewPort(true);							//読み込んだコンテンツの幅に表示倍率を自動調整
			settings.setLoadWithOverviewMode(true);						//☆setUseWideViewPortに続けて記載必要

			MLStr=dataURI;
			dbMsg = fType+"をMLStr="+MLStr;////////////////////////////////////////////////////////////////////////
//				Log.d("onCreate",dbMsg);
			webView.loadUrl(MLStr);


//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);		//タスクバーを 非表示
//			requestWindowFeature(Window.FEATURE_NO_TITLE); 							//タイトルバーを非表示

			webView.setWebViewClient(new WebViewClient() {		//リンク先もこのWebViewで表示させる；端末のブラウザを起動させない
				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon) {
					super.onPageStarted(view, url, favicon);
					setProgressBarIndeterminateVisibility(true);
					setTitle(url); 	//タイトルバーに文字列を設定
				}
				@Override
				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(view, url);
					final String TAG = "onCreate[wKit]";
					String dbMsg="開始";
					if(fName==null){
						String tStr="";
						tStr=webView.getTitle();
						dbMsg = "tStr="+tStr;////////////////////////////////////////////////////////////////////////
//						Log.d("onPageFinished","wKit；"+dbMsg);
						//		Toast.makeText(webView.getContext(), webView.getTitle(), Toast.LENGTH_LONG).show();
						setTitle(webView.getTitle()); 	//タイトルバーに文字列を設定
					}
					setProgressBarIndeterminateVisibility(false);
				}

//				PictureListener picture = new PictureListener(){
//					public void onNewPicture (WebView view, Picture picture){
//						Object loading;
//						if (((Object) loading).isShowing()) {
//							loading.dismiss();
//						}
//					}
//				}

			});
//			webView.loadUrl(requestToken);
//			tool_bar = (Toolbar) findViewById(R.id.wk_tool_bar);		//Toolbar
//			dbMsg=dbMsg + ",tool_bar=" + tool_bar;/////////////////////////////////////////////////////////////////////////////////////////////////////////
//			setSupportActionBar(tool_bar);	//java.lang.IllegalStateException: This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.

			mAdView = (AdView) findViewById(R.id.wk_adView);				//広告表示エリア
//			mAdView.setAdSize(AdSize.SMART_BANNER);
//			mAdView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));

			wk_koukoku_ll = (LinearLayout) findViewById(R.id.wk_koukoku_ll);				//
			wk_nend = (NendAdView) findViewById(R.id.wk_nend);				//
			mAdView = (AdView) findViewById(R.id.wk_adView);
		} catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {		//①ⅱヘッドのイメージは実際にローディンされた時点で設定表示と同時にウィジェットの高さや幅を取得したいときは大抵ここで取る。
		if (hasFocus) {
			final String TAG = "onWindowFocusChanged[wKit]";
			String dbMsg= "開始;";/////////////////////////////////////
			try{
				dbMsg= dbMsg + "ディスプレイ["+dWidth+" × "+ dHeight +"]" ;								//[1080 × 1776]
				koukokuH =wk_koukoku_ll.getHeight();
				dbMsg= dbMsg + ",nend["+dWidth+" × "+ koukokuH +"]" ;								//[1080 × 1776]
				if( Locale.getDefault().equals( Locale.JAPAN)){										//日本語の場合のみconstant for ja_JP.
				}else {
					wk_nend.setVisibility(View.GONE);
				}
				adMobLoad();													//Google AdMobの広告設定
				//		Log.i(TAG,dbMsg);
			}catch (Exception e) {
				Log.e(TAG,dbMsg + "で"+e.toString());
			}
		}
		super.onWindowFocusChanged(hasFocus);
	}

	////////////////////////////////////////////////////////
	public float koukokuScale = 0.5f;
	private LinearLayout wk_koukoku_ll;
	private NendAdView wk_nend;
	private boolean nenvNow =false;
	public int nendW;
	public int nendH;
	public int koukokuH;

	public void nendLoad(){		//nendの広告設定
		final String TAG = "nendLoad[wKit]";
		String dbMsg = "";//////////////////
		try{
			String nend_apiKey = getResources().getString(R.string.nend_apiKey);	//9f317f03b6d2b69e104dc43950fb6190eb39b451
			int nend_spotID = Integer.parseInt(getResources().getString(R.string.nend_spotID));	//458687
			//	mAdView.setVisibility(View.GONE);
			wk_nend.setVisibility(View.VISIBLE);
			wk_nend = new NendAdView(getApplicationContext(), nend_spotID, nend_apiKey);			// 1 NendAdView をインスタンス化

			wk_nend.setListener(new NendAdListener() {
				//	    @Override
				public void onCompletion(NendAdInterstitialStatusCode status) {
					final String TAG = "onCompletion[nendLoad]";
					String dbMsg = "";//////////////////
					try{
						dbMsg = "NendAdInterstitialStatusCode="+status;//////////////////
						switch (status) {
							case SUCCESS:			// 成功
								dbMsg = dbMsg + "成功";//////////////////
								nenvNow = true;
								break;
							case INVALID_RESPONSE_TYPE:			// 不明な広告タイプ
								dbMsg = dbMsg + "不明な広告タイプ";//////////////////
								break;
							case FAILED_AD_REQUEST:		// 広告取得失敗
								dbMsg = dbMsg + "広告取得失敗";//////////////////
								nenvNow = false;
								//			nend_aria.setVisibility(View.GONE);
								break;
							case FAILED_AD_INCOMPLETE:		// 広告取得未完了
								dbMsg = dbMsg + "広告取得未完了";//////////////////
								break;
							case FAILED_AD_DOWNLOAD:		// 広告画像取得失敗
								dbMsg = dbMsg + "広告画像取得失敗";//////////////////
								nenvNow = false;
								//					nend_aria.setVisibility(View.GONE);
								break;
							default:
								break;
						}
						//				Log.i(TAG,dbMsg);
					} catch (Exception e) {
						Log.e(TAG,dbMsg+"で"+e);
					}
				}

				@Override
				public void onClick(NendAdView arg0) {	// クリック通知
					final String TAG = "onClick[nendLoad]";
					String dbMsg = "";//////////////////
					try{
						//		Toast.makeText(getApplicationContext(), "onClick", Toast.LENGTH_LONG).show();
						Log.i(TAG,dbMsg);
					} catch (Exception e) {
						Log.e(TAG,dbMsg+"で"+e);
					}
				}

				@Override
				public void onDismissScreen(NendAdView arg0) {	// 復帰通知
					final String TAG = "onDismissScreen[nendLoad]";
					String dbMsg = "";//////////////////
					try{
						dbMsg = "NendAdView = " + arg0;//////////////////
//						Toast.makeText(getApplicationContext(), "onDismissScreen", Toast.LENGTH_LONG).show();
						nendLoad();		//nendの広告設定
						Log.i(TAG,dbMsg);
					} catch (Exception e) {
						Log.e(TAG,dbMsg+"で"+e);
					}
				}

				@Override
				public void onFailedToReceiveAd(NendAdView arg0) {	// 受信エラー通知
					final String TAG = "onFailedToReceiveAd[nendLoad]";
					String dbMsg = "";//////////////////
					try{
						dbMsg = "NendAdView = " + arg0;//////////////////
//			//			Toast.makeText(getApplicationContext(), "onFailedToReceiveAd", Toast.LENGTH_LONG).show();
						nenvNow = false;
						//				nend_aria.setVisibility(View.GONE);
						Log.i(TAG,dbMsg);
						//		if(! adMobNow){
						//				adMobLoad();													//Google AdMobの広告設定
						//	}
					} catch (Exception e) {
						Log.e(TAG,dbMsg+"で"+e);
					}
				}

				@Override
				public void onReceiveAd(NendAdView arg0) {	// 受信成功通知
					final String TAG = "onReceiveAd[nendLoad]";
					String dbMsg = "";//////////////////
					try{
						dbMsg = "NendAdView = " + arg0;//////////////////
						//			Toast.makeText(getApplicationContext(), "onReceiveAd", Toast.LENGTH_LONG).show();
						//			Log.i(TAG,dbMsg);
					} catch (Exception e) {
						Log.e(TAG,dbMsg+"で"+e);
					}

				}
			});

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

//			nend_aria.setVisibility(View.VISIBLE);
//			nend_aria.addView(nendAdView, params);
//			nendAdView.loadAd();
			//	nenvNow = true;

			if( ! nenvNow ){
				//		 nendLoad();		//nendの広告設定
			}
			Log.i(TAG,dbMsg);
			//		syoliSentaku( IppatuMainTF );										//Viewの初期選択		kotae_tf
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"で"+e);
		}
	}

	private AdView mAdView;							//広告表示エリア
	private AdRequest adRequest;			// 一般的なリクエストを行う
	boolean adMobNow = false;
	public View adViewC;

	@SuppressLint("NewApi")
	public void adMobLoad(){		//Google AdMobの広告設定
		final String TAG = "adMobLoad[wKit]";
		String dbMsg = "";//////////////////
		try{
			//	wk_nend.setVisibility(View.GONE);
			dbMsg = "mAdView=" + mAdView;//	https://developers.google.com/mobile-ads-sdk/docs/admob/android/quick-start#faq
			mAdView.setVisibility(View.VISIBLE);
			//テスト		https://developers.google.com/mobile-ads-sdk/docs/admob/intermediate?hl=ja
			//未登録機はlogcatで03-21 21:21:41.232: I/Ads(10844): Use AdRequest.Builder.addTestDevice("EF6049FA0F4D49D1A08E68C5037D6302") to get test ads on this device.	を検索
			adRequest = new AdRequest.Builder()
					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)				// すべてのエミュレータ
					.addTestDevice("EF6049FA0F4D49D1A08E68C5037D6302")	//Xpelia Z5 (Android5.1.1)
					.addTestDevice("F3B787B1C99665529E01E5BB0647FD8D")	//	304SH(Android4.4.2)
					.addTestDevice("772C6F3DB402CD1F9D8A66E1555E54C5")	//SH08E(Android4.2.2/7インチタブレット)
					.addTestDevice("B339C45F7878E57784B1940379760332")		//	206SH(Android4.2.2)
					.addTestDevice("EFF070C53D3F43AF29325F8E5529D704")	//	203SH(Android4.1.2)
					.addTestDevice("2CCFE123DEF10276C319F12B66D744FA")	//	iS15SH(Android4.0.3)Ads: Use AdRequest.Builder.addTestDevice("") to get test ads on this device.で取得
//				.tagForChildDirectedTreatment(true)									//児童向けで
					.build();
			dbMsg = dbMsg + ",　request=" + adRequest;
			mAdView.loadAd(adRequest);
			mAdView.setAdListener(new AdListener() {
				@Override
				public void onAdOpened() {					// 広告オーバーレイに移動する前にアプリの状態を保存する
					final String TAG = "onAdOpened[adMobLoad]";
					String dbMsg = "広告からオーバーレイを開いて画面全体が覆われた";//////////////////
					try{
						dbMsg = dbMsg+"、mAdView=" + mAdView;
						Log.i(TAG,dbMsg);
					} catch (Exception e) {
						Log.e(TAG,dbMsg+"で"+e);
					}
				}

				@Override
				public void onAdLoaded() {
					final String TAG = "onAdLoaded[adMobLoad]";
					String dbMsg = "広告が表示された";//////////////////
					try{
						dbMsg = dbMsg+"、mAdView=" + mAdView;			//.AdView@41b75f70
						dbMsg = dbMsg+ ",getChildCount=" + mAdView.getChildCount();						//1
						adMobNow = true;
						if(0<mAdView.getChildCount()){
							adViewC = mAdView.getChildAt(0);
							//				adViewC.setOnClickListener(AtarekunnActivity.this);
							//				adViewC.setOnKeyListener( AtarekunnActivity.this);			//	, View.OnKeyListener	使用時のみ
//							if(dPadAri){
//								adMobKeys();		//Google AdMobの広告用のキー設定
//							}
						}
						dbMsg = dbMsg+",getChildAt=" + adViewC;
						dbMsg = dbMsg+",ClassName=" + mAdView.getMediationAdapterClassName();		//null
						//errer発生	getTransitionName	getOutlineProvider	getOverlay
						//				Log.i(TAG,dbMsg);
					} catch (Exception e) {
						Log.e(TAG,dbMsg+"で"+e);
					}
				}

				@SuppressLint("NewApi")
				@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
				@Override
				public void onAdClosed() {
					final String TAG = "onAdClosed[adMobLoad]";
					String dbMsg = "ユーザーが広告をクリックし、アプリケーションに戻ろうとした";//////////////////
					try{
						dbMsg = ",mAdView=" + mAdView;
						Log.i(TAG,dbMsg);
					} catch (Exception e) {
						Log.e(TAG,dbMsg+"で"+e);
					}
				}

				@Override
				public void onAdLeftApplication() {
					final String TAG = "onAdLeftApplication[adMobLoad]";
					String dbMsg = "広告からアプリケーションを終了した場合";//////////////////
					try{
						dbMsg = dbMsg+"、mAdView=" + mAdView;
						Log.i(TAG,dbMsg);
					} catch (Exception e) {
						Log.e(TAG,dbMsg+"で"+e);
					}
				}

				@Override
				public void onAdFailedToLoad(int errorCode) {
					final String TAG = "onAdFailedToLoad[adMobLoad]";
					String dbMsg = "広告リクエストが失敗した";//////////////////
					try{
						dbMsg = dbMsg + ":errorCode=" + errorCode;
						switch(errorCode) {
							case AdRequest.ERROR_CODE_INTERNAL_ERROR:
								dbMsg = dbMsg + ":ERROR_CODE_INTERNAL_ERROR";
								break;
							case AdRequest.ERROR_CODE_INVALID_REQUEST:
								dbMsg = dbMsg + ":ERROR_CODE_INVALID_REQUEST";
								break;
							case AdRequest.ERROR_CODE_NETWORK_ERROR:
								dbMsg = dbMsg + ":ERROR_CODE_NETWORK_ERROR";
								break;
							case AdRequest.ERROR_CODE_NO_FILL:
								dbMsg = dbMsg + ":広告が来ない";
								adMobNow = false;
//							mAdView.setVisibility(View.GONE);
//							artist_tv.setNextFocusUpId(ppPBT.getId());
//							ppPBT.setNextFocusDownId(artist_tv.getId());
//							stopPButton.setNextFocusDownId(artist_tv.getId());		//タイトル
								break;
						}
						Log.i(TAG,dbMsg);
					} catch (Exception e) {
						Log.e(TAG,dbMsg+"で"+e);
					}
				}
			});
			dbMsg =dbMsg+ "、getAdUnitId=" + mAdView.getAdUnitId();//ca-app-pub-3146425308522831/2530772303
			dbMsg =dbMsg+  "[" + mAdView.getHeight() + "×" + mAdView.getWidth() + "]";///is15[75×480]
			//mAdView=com.google.android.gms.ads.AdView@4182e0d8,　
			//request=com.google.android.gms.ads.AdRequest@41b02b48

//2015/09/15；https://www.youtube.com/watch?v=8YHc8DUpYlMを参照にマージン5ポイント設けました。
			//		syoliSentaku( IppatuMainTF );										//Viewの初期選択		kotae_tf
			if(  Locale.getDefault().equals( Locale.JAPAN) ){
				mAdView.setScaleX(koukokuScale);
				mAdView.setScaleY(koukokuScale);
				int mAdView_L = mAdView.getLeft();
				int mAdView_T = mAdView.getTop();
				dbMsg= dbMsg + "mAdView("+ mAdView_L + " , "+ mAdView_T +")" ;								//[1080 × 1776]
				int mAdView_W = mAdView.getWidth();
				int mAdView_H = mAdView.getHeight();
				dbMsg= dbMsg + "["+mAdView_W+" × "+ mAdView_H +"]" ;								//[1080 × 1776]
				//		wk_koukoku_ll.setGravity(Gravity.LEFT|Gravity.BOTTOM);
				mAdView.setX(mAdView_L-mAdView_W/4 + dWidth/30);
				mAdView.setY(mAdView_T+mAdView_H/4);
				wk_nend.setScaleX(koukokuScale);
				wk_nend.setScaleY(koukokuScale);
				wk_nend.setX( dWidth/4 + dWidth/20);					//dWidth/2
				wk_nend.setY(mAdView_T+mAdView_H/4);
//				ViewGroup.LayoutParams params = wk_koukoku_ll.getLayoutParams();
//				params.height = koukokuH/2 + koukokuH/4;
//				wk_koukoku_ll.setLayoutParams(params);

				int nend_L = wk_nend.getLeft();
				int nend_T = wk_nend.getTop();
				dbMsg= dbMsg + "wk_nend("+ nend_L +" , "+ nend_T +")" ;								//[1080 × 1776]
				int nend_W = wk_nend.getWidth();
				int nend_H = wk_nend.getHeight();
				dbMsg= dbMsg + "["+nend_W+" × "+ nend_H +"]" ;								//[1080 × 1776]

//E/Ads: JS: Uncaught ReferenceError: AFMA_ReceiveMessage is not defined (:1)
// 		nendLoad();		//nendの広告設定
//			}else{
//				mAdView.setScaleX(0.7f);
//				mAdView.setScaleY(0.7f);
			}
			//		Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"で"+e);
		}
	}

	////////////////////////////////////////////////////////
	public String retML(String dataStr){		//受け取ったデータによってHTMLを変える
		String retStr = null;
		try{
			dbMsg = "dataStr="+dataStr;////////////////////////////////////////////////////////////////////////

		} catch (Exception e) {
			Log.e("retML",dbMsg+"；"+e.toString());
		}
		return retStr;
	}

	public void quitMe(){			//このActivtyの終了
		final String TAG = "quitMe[wKit]";
		String dbMsg="開始" ;
		try{
			Intent intent = new Intent();
			//		intent.putExtra("item", item);
			setResult(RESULT_OK, intent);
			wKit.this.finish();
			Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG, dbMsg + "で"+e.toString());
		}
	}

	public boolean wZoomUp() {				//ズームアップして上限に達すればfalse
		try{
			En_ZUP=webView.zoomIn();			//ズームアップメニューのフラグ設定
		}catch (Exception e) {
			Log.e("wZoomUp",e.toString());
			return false;
		}
		return En_ZUP;
	}

	public boolean wZoomDown() {				//ズームダウンして下限に達すればfalse
		try{
			En_ZDW=webView.zoomOut();			//ズームダウンのフラグ設定
		}catch (Exception e) {
			Log.e("wZoomDown",e.toString());
			return false;
		}
		return En_ZDW;
	}

	public void wForward() {					//ページ履歴で1つ後のページに移動する
		try{
			webView.goForward();				//ページ履歴で1つ後のページに移動する
		}catch (Exception e) {
			Log.e("wForward",e.toString());
		}
	}

	public void wGoBack() {					//ページ履歴で1つ前のページに移動する
		try{
			dbMsg="canGoBack="+webView.canGoBack();//+",getDisplayLabel="+String.valueOf(event.getDisplayLabel())+",getAction="+event.getAction();////////////////////////////////
			//		Log.d("wGoBack",dbMsg);
			if(webView.canGoBack()){		//戻るページがあれば
				webView.goBack();					//ページ履歴で1つ前のページに移動する
			}else{							//無ければ終了
				wKit.this.finish();
			}
		}catch (Exception e) {
			Log.e("wGoBack",e.toString());
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try{
			dbMsg="keyCode="+keyCode;//+",getDisplayLabel="+String.valueOf(event.getDisplayLabel())+",getAction="+event.getAction();////////////////////////////////
			//		Log.d("onKeyDown","[wKit]"+dbMsg);
//		dbMsg="ppBtnID="+myNFV_S_Pref.getBoolean("prefKouseiD_PadUMU", false);///////////////////////////////////////////////////////////////////
//			Log.d("onKeyDown","[wKit]"+dbMsg);
			dbMsg="サイドボリュームとディスプレイ下のキー；canGoBack="+webView.canGoBack();///////////////////////////////////////////////////////////////////
			switch (keyCode) {	//キーにデフォルト以外の動作を与えるもののみを記述★KEYCODE_MENUをここに書くとメニュー表示されない
				case KeyEvent.KEYCODE_DPAD_UP:		//マルチガイド上；19
					//	wZoomUp();						//ズームアップして上限に達すればfalse
					if(! myNFV_S_Pref.getBoolean("prefKouseiD_PadUMU", false)){		//キーの利用が無効になっていたら
						pNFVeditor.putBoolean("prefKouseiD_PadUMU", true);			//キーの利用を有効にして
					}
					return true;
				case KeyEvent.KEYCODE_DPAD_DOWN:	//マルチガイド下；20
					//	wZoomDown();					//ズームダウンして下限に達すればfalse
					if(! myNFV_S_Pref.getBoolean("prefKouseiD_PadUMU", false)){		//キーの利用が無効になっていたら
						pNFVeditor.putBoolean("prefKouseiD_PadUMU", true);			//キーの利用を有効にして
					}
					return true;
				case KeyEvent.KEYCODE_DPAD_LEFT:	//マルチガイド左；21
					wForward();						//ページ履歴で1つ後のページに移動する					return true;
					if(! myNFV_S_Pref.getBoolean("prefKouseiD_PadUMU", false)){		//キーの利用が無効になっていたら
						pNFVeditor.putBoolean("prefKouseiD_PadUMU", true);			//キーの利用を有効にして
					}
					return true;
				case KeyEvent.KEYCODE_DPAD_RIGHT:	//マルチガイド右；22
					wGoBack();					//ページ履歴で1つ前のページに移動する
					if(! myNFV_S_Pref.getBoolean("prefKouseiD_PadUMU", false)){		//キーの利用が無効になっていたら
						pNFVeditor.putBoolean("prefKouseiD_PadUMU", true);			//キーの利用を有効にして
					}
					return true;
				case KeyEvent.KEYCODE_VOLUME_UP:	//24
					wZoomUp();						//ズームアップして上限に達すればfalse
					return true;
				case KeyEvent.KEYCODE_VOLUME_DOWN:	//25
					wZoomDown();					//ズームダウンして下限に達すればfalse
					return true;
				case KeyEvent.KEYCODE_BACK:			//4KEYCODE_BACK :keyCode；09SH: keyCode；4,event=KeyEvent{action=0 code=4 repeat=0 meta=0 scancode=158 mFlags=72}
					wGoBack();					//ページ履歴で1つ前のページに移動する;
					return true;
				default:
					return false;
			}
		} catch (Exception e) {
			Log.e("onKeyDown",dbMsg+"；"+e.toString());
			return false;
		}
	}

	//メニューボタンで表示するメニュー///////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean onCreateOptionsMenu(Menu wkMenu) {
		//	Log.d("onCreateOptionsMenu","NakedFileVeiwActivity;mlMenu="+wkMenu);
		makeOptionsMenu(wkMenu);	//ボタンで表示するメニューの内容の実記述
		return super.onCreateOptionsMenu(wkMenu);
	}

	public boolean makeOptionsMenu(Menu wkMenu) {	//ボタンで表示するメニューの内容
		dbMsg ="MenuItem"+wkMenu.toString();////////////////////////////////////////////////////////////////////////////
//		Log.d("makeOptionsMenu",dbMsg);
//			wkMenu.add(0, MENU_kore, 0, "これ");	//メニューそのもので起動するパターン
		SubMenu koreMenu = wkMenu.addSubMenu("操作");
		koreMenu.add(MENU_WQKIT, MENU_WQKIT_ZUP, 0, CTM_WQKIT_ZUP);				//ズームアップ";
		koreMenu.add(MENU_WQKIT, MENU_WQKIT_ZDW, 0,CTM_WQKIT_ZDW);				//ズームダウン";
		koreMenu.add(MENU_WQKIT, MENU_WQKIT_FOR, 0, CTM_WQKIT_FOR);				//1ページ進む";
		koreMenu.add(MENU_WQKIT, MENU_WQKIT_BAC, 0,CTM_WQKIT_BAC);				//1ページ戻る";
		koreMenu.add(MENU_WQKIT, MENU_WQKIT_END, 0,CTM_WQKIT_END);		// = "終了";
		return true;
		//	return super.onCreateOptionsMenu(wkMenu);			//102SHでメニューが消えなかった
	}
	//
	@Override
	public boolean onPrepareOptionsMenu(Menu wkMenu) {			//表示直前に行う非表示や非選択設定
		dbMsg ="MenuItem"+wkMenu.toString()+",進み"+webView.canGoForward()+",戻り"+webView.canGoBack();////////////////////////////////////////////////////////////////////////////
		Log.d("onPrepareOptionsMenu",dbMsg);
		//戻るページがあれば
//1ページ進むを表示
		En_FOR = webView.canGoForward();
		//戻るページがあれば
//1ページ戻るを表示
		En_BAC = webView.canGoBack();
		wkMenu.findItem(MENU_WQKIT_ZUP).setEnabled(En_ZUP);		//ズームアップ";
		wkMenu.findItem(MENU_WQKIT_ZDW).setEnabled(En_ZDW);		//ズームダウン";
		wkMenu.findItem(MENU_WQKIT_FOR).setEnabled(En_FOR);		//1ページ進む";
		wkMenu.findItem(MENU_WQKIT_BAC).setEnabled(En_BAC);		//1ページ戻る";
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		try{
			dbMsg ="MenuItem"+item.getItemId()+"を操作";////////////////////////////////////////////////////////////////////////////
			//			Log.d("onOptionsItemSelected",dbMsg);
			switch (item.getItemId()) {
				case MENU_WQKIT_ZUP:						//ズームアップ";
					wZoomUp();			//ズームアップして上限に達すればfalse
					return true;
				case MENU_WQKIT_ZDW:				//ズームダウン";
					wZoomDown();					//ズームダウンして下限に達すればfalse
					return true;
				case MENU_WQKIT_FOR:				//1ページ進む";
					wForward();						//ページ履歴で1つ後のページに移動する
					return true;
				case MENU_WQKIT_BAC:				//1ページ戻る";
					wGoBack();						//ページ履歴で1つ前のページに移動する
					return true;

				case MENU_WQKIT_END:						//終了";
					quitMe();			//このActivtyの終了
					return true;
			}
			return false;
		} catch (Exception e) {
			Log.e("onOptionsItemSelected","エラー発生；"+e);
			return false;
		}
	}

	@Override
	public void onOptionsMenuClosed(Menu wkMenu) {
		Log.d("onOptionsMenuClosed","NakedFileVeiwActivity;mlMenu="+wkMenu);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		final String TAG = "dispatchKeyEvent[wKit]";
		int keyCode = event.getKeyCode();
		String dbMsg="keyCode="+ String.valueOf(keyCode) ;
		boolean retBool =false;
		try{
			dbMsg=dbMsg+",getAction="+event.getAction();////////////////////////////////
			dbMsg=dbMsg+",初押時間="+event.getDownTime();////////////////////////////////
			dbMsg=dbMsg+",イベント発生時間="+event.getEventTime();////////////////////////////////
			int renzoku = event.getRepeatCount();
			dbMsg=dbMsg+",連続入力回数="+ renzoku ;////////////////////////////////
			dbMsg=dbMsg+",スキャンコード="+event.getScanCode();////////////////////////////////
			dbMsg=dbMsg+",同時押しとか="+event.getMetaState();////////////////////////////////
			int focusItemID = 0;
			View currentFo = this.getCurrentFocus();
			if(currentFo != null){
				focusItemID = currentFo.getId();
				dbMsg= dbMsg+"が="+focusItemID+"で発生";
			}
			//		if (event.getAction()==KeyEvent.ACTION_UP) {		//0;ACTION_DOWN
			switch (event.getKeyCode()) {
				case KeyEvent.KEYCODE_BACK:
					wKit.this.finish();
					quitMe();			//このActivtyの終了
					return true;		// 親クラスのdispatchKeyEvent()を呼び出さずにtrueを返す
			}
			//		}
//				//		http://techbooster.jpn.org/andriod/ui/4109/
		} catch (Exception e) {
			Log.e(TAG, dbMsg +"で"+e.toString());
		}
		dbMsg= dbMsg+",retBool="+retBool;
		Log.i(TAG,dbMsg);
		if(retBool ){
			return true;		//Activityを終了せずに何もしないようにするには親クラスのdispatchKeyEvent()を呼び出さずにtrueを返すようにします。
		} else {
			return super.dispatchKeyEvent(event);
		}
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
		try{
			dbMsg ="onDestroy発生";//////////////拡張子=.m4a,ファイルタイプ=audio/*,フルパス=/mnt/sdcard/Music/AC DC/Blow Up Your Video/03 Meanstreak.m4a
			setVisible(false);		//エラー対策		wKit has leaked window android.widget.ZoomButtonsController$Container{444ff948 V.E..... ......I. 0,0-1080,146} that was originally added here
		}catch (Exception e) {
			Log.e("onDestroy","[wKit]"+"で"+e.toString());
		}
	}

}
//AndroidProtocolHandler(26741): Unable to open asset URL: file:///android_asset/css/reset.css
