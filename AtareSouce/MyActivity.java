package com.hijiyama_koubou.atare_kun;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.common.api.GoogleApiClient;

import net.nend.android.NendAdView;

import java.util.Locale;
import java.util.Map;

public class MyActivity extends AppCompatActivity {
//implements NavigationView.OnNavigationItemSelectedListener
	private DrawerLayout main_drawer_layout;
	private Fragment calentFragment;
	private WebFtagnemt web_fragment;
	public Toolbar main_tool_bar;						//このアクティビティのtoolBar
	public NavigationView main_navi_view;
	public TextView nvh_main_tv;			//drawerヘッドのメインテキスト
	public TextView nvh_sub_tv;				//drawerヘッドのサブテキスト
	public int dWidth;														//ディスプレイ幅
	public int dHeight;														//ディスプレイ高

	public String fName=null;
	public String MLStr="";
	public String dataURI="";
	public String fType="";
	public String baseUrl="";

	public ActionBarDrawerToggle toggle_wk;
	//プリファレンス設定
	public SharedPreferences sharedPref;
	public SharedPreferences.Editor myEditor;
	public String pref_sonota_vername;				//このアプリのバージョンコード
	public int kurikaesi_val = 100;							//繰り返し判定数
	public boolean prefUseDlog = true;						//ダイアログの使用/未使用	true	false
	public boolean dPadAri = false;							//ダイヤルの有無
	public static String repSyurui = "";					//一発繰り返し区分
	public static String kujiSyurui = "";					//くじの種類
	public static int val_val = 6;							//乱数の個数
	public static int randumStart_val = 1;					//乱数の開始値
	public static int randumEnd_val = 43;					//乱数の終了値
	public static boolean honJuufuku = true;				//本番号の重複
	public static int SPNval_val = 0;						//特番の数
	public static int SPNStart_val = 0;						//特番の開始値
	public static int SPNumEnd_val = 0;						//特番の終了値
	public static boolean juufuku = false;					//特番と本番合との重複
	public int grupe_suu = 0;								//グループ数
	public boolean jorker_use = false;						//ジョーカー使用
	public boolean jyuuhuk_nasi_use = false;				//重複無しの連続使用
	public static int APIL = Build.VERSION.SDK_INT;			//APIレベル
	public int kujiId;
	public int souwa;										//総和
	public int kakidasiSousuu;								//書き出し総数
	public static int grupeEnd_val = 43;					//グループの終了値
	public int nokori = 0;
	public static String shyougouURL;    //照合URL

	public static final int MENU_WQKIT= 900;							//これメニュー
	public static final int MENU_SETTEI = MENU_WQKIT+1;					//設定	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		final String TAG = "onCreate[MA]";
		String dbMsg = "開始";
		try {
			MobileAds.initialize(this,getResources ().getString (R.string.banner_ad_unit_id));
			WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);			// ウィンドウマネージャのインスタンス取得
			Display disp = wm.getDefaultDisplay();										// ディスプレイのインスタンス生成
			dWidth = disp.getWidth();								//ディスプレイ幅
			dHeight = disp.getHeight();							//ディスプレイ高
			dbMsg=dbMsg + ",ディスプレイサイズ="+dWidth+"×"+dHeight;/////////////////////////////////////////////////////////////////////////////////////////////////////////
			// 		Log.i(TAG,dbMsg);
			Bundle extras = getIntent ().getExtras ();
			dataURI = extras.getString ("dataURI");                        //最初に表示するページのパス
			dbMsg = dbMsg+ ",dataURI=" + dataURI;////////////////////////////////////////////////////////////////////////
			String rStr = "file://" + extras.getString ("baseUrl");                //最初に表示するページを受け取る
			dbMsg = dbMsg +  ",baseUrl=" + rStr;////////////////////////////////////////////////////////////////////////
			if(rStr != null){
				baseUrl = rStr;                //最初に表示するページを受け取る
			}
			rStr =  extras.getString ("fType");                            //データタイプ
			dbMsg = dbMsg +  ",fType=" + rStr;////////////////////////////////////////////////////////////////////////
			if(rStr != null){
				fType = rStr;
//				String[] testSrA = dataURI.split (File.separator);
//				fName = testSrA[testSrA.length - 1];
//				dbMsg = dbMsg + ",fType=" + fType + ",fName=" + fName ;////////////////////////////////////////////////////////////////////////
				Log.i (TAG, dbMsg);
			}
			web_fragment = new WebFtagnemt();


			setContentView (R.layout.activity_main);            //このアクティビティのtoolBarを含むので
			main_tool_bar = (Toolbar) findViewById (R.id.main_tool_bar);
			setSupportActionBar (main_tool_bar);
			Bitmap orgBitmap = BitmapFactory.decodeResource (getResources (), R.drawable.atare96);    //DrawableからBitmapインスタンスを取得
			Bitmap resizedBitmap = Bitmap.createScaledBitmap (orgBitmap, 96, 96, false);                // 100x100にリサイズ
			Drawable drawable = new BitmapDrawable (getResources (), resizedBitmap);
			main_tool_bar.setNavigationIcon (drawable);
			main_drawer_layout = (DrawerLayout) findViewById (R.id.main_drawer_layout);
			initDrawer ();

			//メインコンテンツとの切替
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			Bundle bundle = new Bundle();
			bundle.putInt ("dWidth", dWidth);												//ディスプレイ幅
			bundle.putInt ("dHeight", dHeight);
			//ヘルプwebの場合
			bundle.putString ("dataURI", dataURI);
			bundle.putString ("baseUrl", baseUrl);
			bundle.putString ("fType", fType);
			bundle.putString ("fName", fName);
			calentFragment = web_fragment;
			mainFragmentLoad(ft , web_fragment , R.menu.wk_menu , bundle);			//ヘルプ様にwebFlagment読み込み

//			////////////////////////////////////////////////
			Log.i (TAG, dbMsg);
		} catch (Exception e) {
			Log.e (TAG, dbMsg + "で" + e.toString ());
		}
	}

		@Override
	public void onWindowFocusChanged(boolean hasFocus) {		//①ⅱヘッドのイメージは実際にローディンされた時点で設定表示と同時にウィジェットの高さや幅を取得したいときは大抵ここで取る。
		if (hasFocus) {
			final String TAG = "oWFC[MA]";
			String dbMsg= "開始;";/////////////////////////////////////
			try{
				dbMsg= dbMsg + "ディスプレイ["+dWidth+" × "+ dHeight +"]" ;								//[1080 × 1776]
//				wk_koukoku_ll = (LinearLayout) findViewById(R.id.wk_koukoku_ll);
//				wk_nend = (NendAdView) findViewById(R.id.wk_nend);
//				mAdView = (AdView) findViewById(R.id.wk_adView);
				adMobLoad();													//Google AdMobの広告設定
				if( wk_koukoku_ll != null ){
					dbMsg= dbMsg + "wk_koukoku_ll="+wk_koukoku_ll.getId () ;								//[1080 × 1776]

				}
//				koukokuH =wk_koukoku_ll.getHeight();
//				dbMsg= dbMsg + ",nend["+dWidth+" × "+ koukokuH +"]" ;								//[1080 × 1776]
//				if( Locale.getDefault().equals( Locale.JAPAN)){										//日本語の場合のみconstant for ja_JP.
//				}else {
//					wk_nend.setVisibility(View.GONE);
//				}
//				adMobLoad();													//Google AdMobの広告設定
		//				Log.i(TAG,dbMsg);
			}catch (Exception e) {
				Log.e(TAG,dbMsg + "で"+e.toString());
			}
		}
			super.onWindowFocusChanged(hasFocus);
	}

	////////////////////////////////////////////////////////
	private LinearLayout wk_koukoku_ll;
	private NendAdView wk_nend;
	private boolean nenvNow =false;
	private AdView mAdView;							//広告表示エリア
	private AdRequest adRequest;			// 一般的なリクエストを行う
	boolean adMobNow = false;
	public View adViewC;

	@SuppressLint ("NewApi")
//	public void adMobLoad(){		//Google AdMobの広告設定
//		final String TAG = "adMobLoad[MA]";
//		String dbMsg = "";//////////////////
//		try{
//			wk_nend.setVisibility(View.GONE);
//			dbMsg = "mAdView=" + mAdView;//	https://developers.google.com/mobile-ads-sdk/docs/admob/android/quick-start#faq
//			mAdView.setVisibility(View.VISIBLE);
//			//テスト		https://developers.google.com/mobile-ads-sdk/docs/admob/intermediate?hl=ja
//			//未登録機はlogcatで03-21 21:21:41.232: I/Ads(10844): Use AdRequest.Builder.addTestDevice("EF6049FA0F4D49D1A08E68C5037D6302") to get test ads on this device.	を検索
//			adRequest = new AdRequest.Builder()
//					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)				// すべてのエミュレータ
//					.addTestDevice("EF6049FA0F4D49D1A08E68C5037D6302")	//Xpelia Z5 (Android5.1.1)
//					.addTestDevice("F3B787B1C99665529E01E5BB0647FD8D")	//	304SH(Android4.4.2)
//					.addTestDevice("772C6F3DB402CD1F9D8A66E1555E54C5")	//SH08E(Android4.2.2/7インチタブレット)
//					.addTestDevice("B339C45F7878E57784B1940379760332")		//	206SH(Android4.2.2)
//					.addTestDevice("EFF070C53D3F43AF29325F8E5529D704")	//	203SH(Android4.1.2)
//					.addTestDevice("2CCFE123DEF10276C319F12B66D744FA")	//	iS15SH(Android4.0.3)Ads: Use AdRequest.Builder.addTestDevice("") to get test ads on this device.で取得
//					//				.tagForChildDirectedTreatment(true)									//児童向けで
//					.build();
//			dbMsg = dbMsg + ",　request=" + adRequest;
//			mAdView.loadAd(adRequest);
//			mAdView.setAdListener(new AdListener () {
//				@Override
//				public void onAdOpened() {					// 広告オーバーレイに移動する前にアプリの状態を保存する
//					final String TAG = "onAdOpened[adMobLoad]";
//					String dbMsg = "広告からオーバーレイを開いて画面全体が覆われた";//////////////////
//					try{
//						dbMsg = dbMsg+"、mAdView=" + mAdView;
//						Log.i(TAG,dbMsg);
//					} catch (Exception e) {
//						Log.e(TAG,dbMsg+"で"+e);
//					}
//				}
//
//				@Override
//				public void onAdLoaded() {
//					final String TAG = "onAdLoaded[adMobLoad]";
//					String dbMsg = "広告が表示された";//////////////////
//					try{
//						dbMsg = dbMsg+"、mAdView=" + mAdView;			//.AdView@41b75f70
//						dbMsg = dbMsg+ ",getChildCount=" + mAdView.getChildCount();						//1
//						adMobNow = true;
//						if(0<mAdView.getChildCount()){
//							adViewC = mAdView.getChildAt(0);
//							//				adViewC.setOnClickListener(AtarekunnActivity.this);
//							//				adViewC.setOnKeyListener( AtarekunnActivity.this);			//	, View.OnKeyListener	使用時のみ
//							//							if(dPadAri){
//							//								adMobKeys();		//Google AdMobの広告用のキー設定
//							//							}
//						}
//						dbMsg = dbMsg+",getChildAt=" + adViewC;
//						dbMsg = dbMsg+",ClassName=" + mAdView.getMediationAdapterClassName();		//null
//						//errer発生	getTransitionName	getOutlineProvider	getOverlay
//						//				Log.i(TAG,dbMsg);
//					} catch (Exception e) {
//						Log.e(TAG,dbMsg+"で"+e);
//					}
//				}
//
//				@SuppressLint("NewApi")
//				@TargetApi (Build.VERSION_CODES.JELLY_BEAN_MR2)
//				@Override
//				public void onAdClosed() {
//					final String TAG = "onAdClosed[adMobLoad]";
//					String dbMsg = "ユーザーが広告をクリックし、アプリケーションに戻ろうとした";//////////////////
//					try{
//						dbMsg = ",mAdView=" + mAdView;
//						Log.i(TAG,dbMsg);
//					} catch (Exception e) {
//						Log.e(TAG,dbMsg+"で"+e);
//					}
//				}
//
//				@Override
//				public void onAdLeftApplication() {
//					final String TAG = "onAdLeftApplication[adMobLoad]";
//					String dbMsg = "広告からアプリケーションを終了した場合";//////////////////
//					try{
//						dbMsg = dbMsg+"、mAdView=" + mAdView;
//						Log.i(TAG,dbMsg);
//					} catch (Exception e) {
//						Log.e(TAG,dbMsg+"で"+e);
//					}
//				}
//
//				@Override
//				public void onAdFailedToLoad(int errorCode) {
//					final String TAG = "onAdFailedToLoad[adMobLoad]";
//					String dbMsg = "広告リクエストが失敗した";//////////////////
//					try{
//						dbMsg = dbMsg + ":errorCode=" + errorCode;
//						switch(errorCode) {
//							case AdRequest.ERROR_CODE_INTERNAL_ERROR:
//								dbMsg = dbMsg + ":ERROR_CODE_INTERNAL_ERROR";
//								break;
//							case AdRequest.ERROR_CODE_INVALID_REQUEST:
//								dbMsg = dbMsg + ":ERROR_CODE_INVALID_REQUEST";
//								break;
//							case AdRequest.ERROR_CODE_NETWORK_ERROR:
//								dbMsg = dbMsg + ":ERROR_CODE_NETWORK_ERROR";
//								break;
//							case AdRequest.ERROR_CODE_NO_FILL:
//								dbMsg = dbMsg + ":広告が来ない";
//								adMobNow = false;
//								//							mAdView.setVisibility(View.GONE);
//								//							artist_tv.setNextFocusUpId(ppPBT.getId());
//								//							ppPBT.setNextFocusDownId(artist_tv.getId());
//								//							stopPButton.setNextFocusDownId(artist_tv.getId());		//タイトル
//								break;
//						}
//						Log.i(TAG,dbMsg);
//					} catch (Exception e) {
//						Log.e(TAG,dbMsg+"で"+e);
//					}
//				}
//			});
//			dbMsg =dbMsg+ "、getAdUnitId=" + mAdView.getAdUnitId();//ca-app-pub-3146425308522831/2530772303
//			dbMsg =dbMsg+  "[" + mAdView.getHeight() + "×" + mAdView.getWidth() + "]";///is15[75×480]
//			//mAdView=com.google.android.gms.ads.AdView@4182e0d8,　
//			//request=com.google.android.gms.ads.AdRequest@41b02b48
//
//			//2015/09/15；https://www.youtube.com/watch?v=8YHc8DUpYlMを参照にマージン5ポイント設けました。
//			//		syoliSentaku( IppatuMainTF );										//Viewの初期選択		kotae_tf
//			if(  Locale.getDefault().equals( Locale.JAPAN) ){
//				int koukokuH = wk_koukoku_ll.getHeight ();
//				dbMsg= dbMsg + "広告枠["+wk_koukoku_ll.getWidth ()+" × "+ koukokuH  ;
//				dbMsg= dbMsg + "("+wk_koukoku_ll.getX ()+" , "+ wk_koukoku_ll.getWidth () +")" ;
//				float motoX = mAdView.getX ();
//				float motoY = mAdView.getY ();
//				ViewGroup.LayoutParams params = wk_koukoku_ll.getLayoutParams();
//				dbMsg= dbMsg + "params="+params;
//
//				wk_koukoku_ll.setScaleX(0.5f);									//☆中身は縮小されるが
//				wk_koukoku_ll.setScaleY(0.5f);									//外形は変わらず
//				params.width = dWidth*2; 				//params.width = wk_koukoku_ll.getHeight();//				wk_koukoku_ll.setMinimumWidth (dWidth * 2);
//				wk_koukoku_ll.setLayoutParams(params);
//				wk_koukoku_ll.setX (-dWidth/2);
//		//		wk_koukoku_ll.setY (dHeight - 50);
//				dbMsg= dbMsg + ">>["+wk_koukoku_ll.getWidth ()+" × "+ wk_koukoku_ll.getHeight () + "("+wk_koukoku_ll.getX ()+" , "+ wk_koukoku_ll.getWidth () +")" ;
//				dbMsg= dbMsg + "ad("+motoX+" × "+" , "+ motoY +")" ;
//				float shiftX = mAdView.getX ();
//				float shiftY = mAdView.getY ();
//				dbMsg= dbMsg + "shift("+shiftX+" × "+" , "+ shiftY +")" ;
//
//				wk_nend.setVisibility(View.VISIBLE);
//				int nend_L = wk_nend.getLeft();
//				int nend_T = wk_nend.getTop();
//				dbMsg= dbMsg + "wk_nend("+ nend_L +" , "+ nend_T +")" ;								//[1080 × 1776]
//				wk_nend.setLeft (dWidth/2);
////				int koukokuH = wk_koukoku_ll.getHeight ();
////				dbMsg= dbMsg + "広告枠["+wk_koukoku_ll.getWidth ()+" × "+ koukokuH + "]" ;
////				FrameLayout main_content_frame = (FrameLayout) findViewById (R.id.main_content_frame);
////				int hyoujiwakuH  = main_content_frame.getHeight ();
////				dbMsg= dbMsg + "表示枠["+main_content_frame.getWidth ()+" × "+ hyoujiwakuH + "]" ;
////				main_content_frame.setMinimumHeight ( hyoujiwakuH + koukokuH/2 );
//			}
//					Log.i(TAG,dbMsg);
//		} catch (Exception e) {
//			Log.e(TAG,dbMsg+"で"+e);
//		}
//	}
/*
* Ads: JS: Uncaught ReferenceError: AFMA_ReceiveMessage is not defined
*app.jsに	var TiAdmob = require('ti.admob');	を付けると大丈夫らしい。
* */
	////////////////////////////////////////////////////////

	/**
		 * NaviViewの初期設定
		 * 開閉のイベント設定
		 **/
	private void initDrawer () {			//http://qiita.com/androhi/items/f12b566730d9f951b8ec
		final String TAG = "initDrawer[MA]";
		String dbMsg = "";
		try {
			toggle_wk = new ActionBarDrawerToggle (this, main_drawer_layout, main_tool_bar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
				public void onDrawerClosed (View view) {
					final String TAG = "onDrawerClosed[MyActivity.initDrawer]";
					String dbMsg="開始";
					try{
					} catch (Exception e) {
						Log.e(TAG,dbMsg + "で"+e.toString());
					}
				}                ///Called when a drawer has settled in a completely closed state.

				public void onDrawerOpened (View drawerView) {
					final String TAG = "onDrawerOpened[MyActivity.initDrawer]";
					String dbMsg="開始";
					try{
						if(calentFragment == web_fragment){
							boolean En_ZUP=web_fragment.En_ZUP;				//ズームアップメニュー有効
							boolean En_ZDW=web_fragment.En_ZDW;		//ズームアップメニュー無効
							boolean En_FOR=web_fragment.En_FOR;			//1ページ進む";
							boolean En_BAC=web_fragment.En_BAC;			//1ページ戻る";
							if( web_fragment.webView != null ){
								En_FOR = web_fragment.webView.canGoForward();						//戻るページがあれば1ページ進むを表示
								En_BAC = web_fragment.webView.canGoBack();							//進めるページがあれば1ページ戻るを表示
								nvh_main_tv = (TextView) main_navi_view.findViewById(R.id.nvh_main_tv);			//drawerヘッドのメインテキスト
								nvh_sub_tv = (TextView) main_navi_view.findViewById(R.id.nvh_sub_tv);				//drawerヘッドのサブテキスト
								dbMsg="url=" + web_fragment.dataURI + ",Titol=" + web_fragment.pageTitol;
								nvh_main_tv.setText (web_fragment.pageTitol); 		//drawerヘッドのメインテキスト
								nvh_sub_tv.setText (web_fragment.dataURI);				//drawerヘッドのサブテキスト
							}
							dbMsg= dbMsg + "En_ZUP=" + En_ZUP + ",En_ZDW=" + En_ZDW + ",En_FOR=" + En_FOR + ",En_BAC=" + En_BAC;
							Menu dMenu = main_navi_view.getMenu ();
							dMenu.findItem(R.id.wk_menu_zu).setEnabled(En_ZUP);		//ズームアップ";
							dMenu.findItem(R.id.wk_menu_zd).setEnabled(En_ZDW);		//ズームダウン";
							dMenu.findItem(R.id.wk_menu_for).setEnabled(En_FOR);		//1ページ進む";
							dMenu.findItem(R.id.wk_menu_bck).setEnabled(En_BAC);		//1ページ戻る";
						}
						Log.i (TAG, dbMsg);
					} catch (Exception e) {
						Log.e(TAG,dbMsg + "で"+e.toString());
					}
				}                // Called when a drawer has settled in a completely open state.
			};					//Drawer開閉イベント

			toggle_wk.setDrawerIndicatorEnabled(true);
			main_drawer_layout.setDrawerListener (toggle_wk);	//Attempt to invoke virtual method 'void android.support.v4.widget.DrawerLayout.setDrawerListener(android.support.v4.widget.DrawerLayout$DrawerListener)' on a null object reference
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);			//左矢印←アイコンになる
			getSupportActionBar().setDisplayShowHomeEnabled(true);

			main_navi_view = (NavigationView)findViewById(R.id.drawer_navigation);		//			wk_navigation
			main_navi_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(MenuItem menuItem) {
					final String TAG = "onNavigationItemSelected[MyActivity.initDrawer]";
					String dbMsg ="MenuItem"+menuItem.toString();/////////////////////////////////////////////////
					boolean retBool = false;
					try{
						retBool = funcSelected(menuItem);
						MyActivity.this.main_drawer_layout.closeDrawers ();
					} catch (Exception e) {
						Log.e(TAG,dbMsg + "エラー発生；"+e);
						return false;
					}
					return retBool;
				}
			});
		} catch (Exception e) {
			Log.e (TAG, dbMsg + "で" + e);
		}
	}																	//NaviViewの初期設定

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		final String TAG = "onPostCreate[wKit]";
		String dbMsg = "";
		try {
			toggle_wk.syncState();						//NaviIconの回転アニメーションなど
			Log.i (TAG, dbMsg);
		} catch (Exception e) {
			Log.e (TAG, dbMsg + "で" + e);
		}
	}

	@SuppressLint ( "LongLogTag" )
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		final String TAG = "onConfigurationChanged[wKit]";
		String dbMsg = "";
		try {
			toggle_wk.onConfigurationChanged(newConfig);
			Log.i (TAG, dbMsg);
		} catch (Exception e) {
			Log.e (TAG, dbMsg + "で" + e);
		}
	}

//	@Override
//	public void onBackPressed () {
//		DrawerLayout drawer = (DrawerLayout) findViewById (R.id.drawer_layout);
//		if (drawer.isDrawerOpen (GravityCompat.START)) {
//			drawer.closeDrawer (GravityCompat.START);
//		} else {
//			super.onBackPressed ();
//		}
//	}

//ヘルプweb//////////////////////////////////////////////////////////////////
public void mainFragmentLoad(FragmentTransaction ft , Fragment fragment , int intmenuRes , Bundle bundle){			//ヘルプ様にwebFlagment読み込み
	final String TAG = "mainFragmentLoad[MA]";
	String dbMsg="開始" ;
	try{
		dbMsg="fragment=" + fragment ;
//		Bundle bundle = new Bundle();
//		bundle.putString ("dataURI", dataURI);
//		bundle.putString ("baseUrl", baseUrl);
//		bundle.putString ("fType", fType);
//		bundle.putString ("fName", fName);
//		bundle.putInt ("dWidth", dWidth);												//ディスプレイ幅
//		bundle.putInt ("dHeight", dHeight);
		fragment.setArguments(bundle);																//bundleのデータをfragmentに渡して
		ft.replace(R.id.main_content_frame, fragment).commit();										//DrawerLayout中のFrameLayoutをfragmentに置き換える

		Menu navi_menu = main_navi_view.getMenu ();													//NavigationViewのapp:menuを取得
		dbMsg= dbMsg + ",navi_menu=" + navi_menu ;
//		MenuItem tItem = navi_menu.findItem (R.id.menu_sonota_help);
//		int subMenu = tItem.getGroupId();			//getItemId ();                //ヘルプ表示	navi_menu.getItem (1).getItemId ()=2131624248	getGroupId=2131624257
//		dbMsg= dbMsg + ",id=" + subMenu+ ",findItem=" + navi_menu.findItem (subMenu) ;
	//	navi_menu.inflate(R.menu.wk_menu, subMenu);
		getMenuInflater ().inflate (intmenuRes, navi_menu);											//読み込んだfragment用メニュー
//		navi_menu.add  (subMenu ,R.id.wk_menu_zu, 0 , getResources ().getString (R.string.wk_menu_zu));				//ズームアップ";		 , 1 ,R.id.menu_sonota_help
//		navi_menu.add (subMenu, R.id.wk_menu_zd, 0 , getResources ().getString (R.string.wk_menu_zd));				//ズームダウン";
//		navi_menu.add (subMenu, R.id.wk_menu_for, 0 , getResources ().getString (R.string.wk_menu_for));				//1ページ進む";
//		navi_menu.add (subMenu, R.id.wk_menu_bck, Menu.NONE , getResources ().getString (R.string.wk_menu_bck));				//1ページ戻る";
		getMenuInflater ().inflate (R.menu.sonota_menu, navi_menu);									//共通メニュー
	//	Log.i(TAG,dbMsg);
	}catch (Exception e) {
		Log.e(TAG, dbMsg + "で"+e.toString());
	}
}							//ヘルプ様にwebFlagment読み込み
	//共通機能///////////////////////////////////////////////////////////////////
	public void quitMe(){			//このActivtyの終了
		final String TAG = "quitMe[MA]";
		String dbMsg="開始" ;
		try{
			if(calentFragment == web_fragment){
				web_fragment.quitMe ();
			}
			Intent intent = new Intent();
			//		intent.putExtra("item", item);
			setResult(RESULT_OK, intent);
			MyActivity.this.finish();
			ViewGroup parent = (ViewGroup)web_fragment.webView.getParent();
			if (parent != null)
			{
				parent.removeView(web_fragment.webView);
			}
			Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG, dbMsg + "で"+e.toString());
		}
	}																		//このActivtyの終了

	public void prefarenceHyouji () {
		final String TAG = "prefarenceHyouji[MA]";
		String dbMsg = "";//////////////////
		try {
			//		delPrif ();        //☆暫定対策；；プリファレンスの内容削除
			Intent intentPRF = new android.content.Intent(MyActivity.this, MyPreferences.class);
			//	Intent intentPRF = new Intent (MyActivity.this, MyPreferences.class);            //プリファレンス
			dbMsg = "くじの種類= " + kujiSyurui;
			intentPRF.putExtra ("kujiSyurui", kujiSyurui);        //繰り返し		String.valueOf(kurikaesi_val)
			dbMsg = "繰り返し= " + kurikaesi_val;
			intentPRF.putExtra ("kurikaesi_val", kurikaesi_val);        //繰り返し		String.valueOf(kurikaesi_val)
			dbMsg = dbMsg + ",ダイヤログの利用= " + prefUseDlog;
			intentPRF.putExtra ("prefUseDlog", prefUseDlog);            //ダイヤログの利用
			dbMsg = dbMsg + ",ダイヤルキー= " + dPadAri;
			intentPRF.putExtra ("dPadAri", dPadAri);                    //ダイヤルキー
			//	Log.i (TAG, dbMsg);
			startActivityForResult (intentPRF, MENU_SETTEI);
		} catch (Exception e) {
			Log.e (TAG, "エラー発生；" + e);
		}
	}

	public void prefarenceSyoukyo () {
		final String TAG = "prefarenceSyoukyo[MA]";
		String dbMsg = "";//////////////////
		try {
			delPrif ();        //プリファレンスの内容削除
//			String fn = getApplicationContext ().getString (R.string.kuji_file);        //kuji.db
//			dbMsg = "fn= " + fn;
//			kuji_table = getResources ().getString (R.string.kuji_table);                //kuji_table</string>
//			dbMsg = dbMsg + ",テーブル名=" + kuji_table;
//			kujiHelper = new KujiHelper (getApplicationContext (), fn);                //計算履歴トヘルパ
//			File dbF = getDatabasePath (fn);            //Environment.getExternalStorageDirectory().getPath();		new File(fn);		//cContext.
//			dbMsg = dbMsg + ",dbF=" + dbF;
//			dbMsg = dbMsg + " , exists=" + dbF.exists () + " , canWrite=" + dbF.canWrite ();
//			boolean syouhkyo = dbF.delete ();
//			dbMsg = dbMsg + " , syouhkyo=" + syouhkyo;
//			if (syouhkyo) {
//				Toast.makeText (MyActivity.this, this.getApplicationContext ().getString (R.string.menu_item_sonota_syoukyo), Toast.LENGTH_LONG).show ();
//			}
			Log.i (TAG, dbMsg);
		} catch (Exception e) {
			Log.e (TAG, "エラー発生；" + e);
		}
	}
	public void readPrif () {        //プリファレンスの読込み
		final String TAG = "readPrif[MA]";
		String dbMsg = "";//////////////////
		try {
		//	dbStart ();                //データベースの作成・オープン
			sharedPref = PreferenceManager.getDefaultSharedPreferences (this);                    //this.getSharedPreferences(this, MODE_PRIVATE);		//
			Map<String, ?> keys = sharedPref.getAll ();
			dbMsg = "読み込み開始" + keys.size () + "項目;sharedPref=" + sharedPref;////////////////////////////////////////////////////////////////////////////
			if (keys.size () == 0) {            //初回起動時は			//初回起動時は
				myEditor = sharedPref.edit ();
				myEditor.putString ("pref_kurikaesi", "100");                //繰り返し判定数		putString
				myEditor.putBoolean ("prefUseDlog_ch", true);            //ダイアログの使用/未使用
				myEditor.putBoolean ("dPadAri", dPadAri);            //ダイヤルキー
				boolean kakikomi = myEditor.commit ();
				dbMsg = dbMsg + ",書込み=" + kakikomi;//////////////////
				keys = sharedPref.getAll ();
				dbMsg = "読み込み開始" + keys.size () + "項目;sharedPref=" + sharedPref;////////////////////////////////////////////////////////////////////////////
			}
			int i = 0;
			for (String key : keys.keySet ()) {
				i++;
				dbMsg = dbMsg + "\n" + i + "/" + keys.size () + ")　" + key + "は" + keys.get (key);///////////////+","+(keys.get(key) instanceof String)+",instanceof Boolean="+(keys.get(key) instanceof Boolean);////////////////////////////////////////////////////////////////////////////
				try {
					if (key.equals ("prefUseDlog_ch")) {
						prefUseDlog = Boolean.valueOf (keys.get (key).toString ());                        //ダイアログの使用/未使用	true	false
						dbMsg = dbMsg + ",ダイアログの使用＝" + prefUseDlog;///////////////+","+(keys.get(key) instanceof String)+",instanceof Boolean="+(keys.get(key) instanceof Boolean);////////////////////////////////////////////////////////////////////////////
					} else if (key.equals ("kujiSyurui")) {
						kujiSyurui = keys.get (key).toString ();
					} else if (key.equals ("pref_kurikaesi")) {
						Object rStr = keys.get (key);
						if (rStr == null) {
							rStr = "100";
						}
						kurikaesi_val = Integer.valueOf (String.valueOf (keys.get (key)));        //繰り返し判定数
						//			kurikaesi_val=Integer.valueOf(keys.get(key).toString());		//繰り返し判定数
						dbMsg = dbMsg + ",繰り返し＝" + kurikaesi_val;///////////////+","+(keys.get(key) instanceof String)+",instanceof Boolean="+(keys.get(key) instanceof Boolean);////////////////////////////////////////////////////////////////////////////
					} else if (key.equals ("repSyurui")) {
						dbMsg = dbMsg + ",繰り返し＝" + repSyurui;///////////////+","+(keys.get(key) instanceof String)+",instanceof Boolean="+(keys.get(key) instanceof Boolean);////////////////////////////////////////////////////////////////////////////
						repSyurui = String.valueOf (keys.get (key));
					} else if (key.equals ("dPadAri")) {
						dPadAri = Boolean.valueOf (keys.get (key).toString ());                        //ダイアログの使用/未使用	true	false
						dbMsg = dbMsg + ",ダイヤルキーは" + dPadAri;///////////////+","+(keys.get(key) instanceof String)+",instanceof Boolean="+(keys.get(key) instanceof Boolean);////////////////////////////////////////////////////////////////////////////
					}
				} catch (Exception e) {
					Log.e (TAG, dbMsg + "；" + e);
				}
			}            //	for (String key : keys.keySet()) {
			dbMsg = "このアプリのバージョンコード" + pref_sonota_vername;////////////////////////////////////////////////////////////////////////////
			if(pref_sonota_vername != null){
				if( Integer.valueOf (pref_sonota_vername) < 1034140 ){
					Log.i (TAG, dbMsg);
					delPrif ();        //プリファレンスの内容削除
				}
			} else {
				pref_sonota_vername = String.valueOf (getPackageManager().getPackageInfo(getPackageName(), getPackageManager().GET_META_DATA).versionCode);			//.versionNameは1.3.2など
			}
			dbMsg = dbMsg + randumStart_val + "～" + randumEnd_val + "を" + val_val + "個" + "；くじの種類" + kujiSyurui + ",繰り返し判定数" + kurikaesi_val + ",照合URL" + shyougouURL;////////////////////////////////////////////////////////////////////////////
			Log.i (TAG, dbMsg);
		} catch (Exception e) {
			Log.e (TAG, dbMsg + "；" + e);
		}
	}

	public void wriAllPrif () {        //プリファレンス全項目書込み
		final String TAG = "wriAllPrif";
		String dbMsg = "";//////////////////
		try {
			//		delPrif ();        //プリファレンスの内容削除
			dbMsg = kujiSyurui;
			myEditor.putString ("kujiSyurui", kujiSyurui);                    //くじの種類
			dbMsg = dbMsg + "[" + randumStart_val;
			myEditor.putString ("randumStart_val", String.valueOf (randumStart_val));                //乱数の開始値
			if( 0 < grupe_suu || jyuuhuk_nasi_use) {            //グループ数,重複無しの連続使用
				dbMsg = dbMsg + "～" + grupeEnd_val;
				myEditor.putString ("randumEnd_val", String.valueOf (grupeEnd_val));                    //乱数の終了値
			}else{
				dbMsg = dbMsg + "～" + randumEnd_val;
				myEditor.putString ("randumEnd_val", String.valueOf (randumEnd_val));                    //乱数の終了値
			}
			dbMsg = dbMsg + "]" + val_val;
			myEditor.putString ("val_val", String.valueOf ( val_val ));                                //乱数の個数
			dbMsg = dbMsg + "回;照合web=" + shyougouURL;////////////////////////////////////////////////////////////////////////////
			myEditor.putString ("shyougouURL", shyougouURL);                    //照合web
			dbMsg = dbMsg + "繰り返し=" + kurikaesi_val;////////////////////////////////////////////////////////////////////////////
			myEditor.putString ("pref_kurikaesi", String.valueOf ( kurikaesi_val ));                    //繰り返し判定数
			dbMsg = dbMsg + "特番[" + SPNStart_val;////////////////////////////////////////////////////////////////////////////
			myEditor.putString ("SPNStart_val", String.valueOf ( SPNStart_val ));                //特番の開始値
			dbMsg = dbMsg + "～" + SPNumEnd_val;////////////////////////////////////////////////////////////////////////////
			myEditor.putString ("SPNumEnd_val", String.valueOf ( SPNumEnd_val ));                //特番の終了値
			dbMsg = dbMsg + "]" + SPNval_val;////////////////////////////////////////////////////////////////////////////
			myEditor.putString ("SPNval_val", String.valueOf ( SPNval_val ));                    //特番の数
			dbMsg = dbMsg + "," + repSyurui;////////////////////////////////////////////////////////////////////////////
			myEditor.putString ("repSyurui", repSyurui);                        //一発繰り返し区分

			dbMsg = dbMsg + ",グループ数=" + grupe_suu;////////////////////////////////////////////////////////////////////////////
			myEditor.putString ("grupe_suu", String.valueOf ( grupe_suu ));
			dbMsg = dbMsg + ",重複無しの連続使用" + jyuuhuk_nasi_use;////////////////////////////////////////////////////////////////////////////
			myEditor.putBoolean ("jyuuhuk_nasi_use", jyuuhuk_nasi_use);                        //一発繰り返し区分
			dbMsg = dbMsg + ",本番号の重複" + honJuufuku;////////////////////////////////////////////////////////////////////////////
			myEditor.putBoolean ("honJuufuku", honJuufuku);

			dbMsg = dbMsg + "個,ダイアログの使用=" + prefUseDlog;////////////////////////////////////////////////////////////////////////////
			myEditor.putBoolean ("prefUseDlog_ch", prefUseDlog);            //ダイアログの使用/未使用	true	false
			pref_sonota_vername = String.valueOf (getPackageManager().getPackageInfo(getPackageName(), getPackageManager().GET_META_DATA).versionCode);			//.versionNameは1.3.2など
			dbMsg = dbMsg + "このアプリのバージョンコード=" + pref_sonota_vername;////////////////////////////////////////////////////////////////////////////
			myEditor.putString ("pref_sonota_vername", pref_sonota_vername);
			//	Log.i (TAG, dbMsg);
			myEditor.commit ();
		} catch (Exception e) {
			Log.e (TAG, dbMsg + "；" + e);
		}
	}

	public void delPrif () {        //プリファレンスの内容削除
		myEditor = sharedPref.edit ();
		final String TAG = "delPrif";
		String dbMsg = "";//////////////////
		try {
			Map<String, ?> keys = sharedPref.getAll ();
			int i = 0;
			if (keys.size () > 0) {
				for (String key : keys.keySet ()) {
					i++;
					dbMsg = i + "/" + keys.size () + ")　" + key + "　の値は　" + (keys.get (key)).toString ();////////////////////////////////////////////////////////////////////////////
					myEditor.remove (key);
					//Log.d("delPrif",dbMsg);
				}
				//			PreferenceManager.setDefaultValues(this, R.xml.prefes, true);		//デフォルト値を書き出す
				myEditor.commit ();
			}
			pref_sonota_vername = String.valueOf (getPackageManager().getPackageInfo(getPackageName(), getPackageManager().GET_META_DATA).versionCode);			//.versionNameは1.3.2など

//			String fn = this.getApplicationContext ().getString (R.string.kuji_file);        //kuji.db
//			dbMsg = "fn= " + fn;
//			kuji_table = getResources ().getString (R.string.kuji_table);                //kuji_table</string>
//			dbMsg = dbMsg + ",テーブル名=" + kuji_table;
//			kujiHelper = new KujiHelper (getApplicationContext (), fn);                //計算履歴トヘルパ
//			File dbF = getDatabasePath (fn);            //Environment.getExternalStorageDirectory().getPath();		new File(fn);		//cContext.
//			dbMsg = dbMsg + ",dbF=" + dbF;
//			dbMsg = dbMsg + " , exists=" + dbF.exists () + " , canWrite=" + dbF.canWrite ();
//			dbF.delete ();
//			dbStart ();                  //データベースの作成・オープン
			Log.i (TAG, dbMsg);
		} catch (Exception e) {
			Log.e (TAG, "エラー発生；" + e);
		}
	}
	//toolbarに表示するメニュー////////////////////////////////////////////////////////////////共通機能///
		@Override
	public boolean onCreateOptionsMenu(Menu wkMenu) {
		final String TAG = "makeOptionsMenu[MA]";
		String dbMsg  ="NakedFileVeiwActivity;mlMenu="+wkMenu;
		//	boolean retBool = true;
			try{
				if(calentFragment == web_fragment){
					getMenuInflater ().inflate (R.menu.wk_menu, wkMenu);										//読み込んだFragmento用のメニュー
				}
				getMenuInflater ().inflate (R.menu.sonota_menu, wkMenu);									//共通メニュー
				Log.i(TAG,dbMsg);
			} catch (Exception e) {
				Log.e(TAG ,"エラー発生；"+e);
				return false;
			}
		return super.onCreateOptionsMenu(wkMenu);
	}												//toolBarもしくはボタンで表示するメニューの内容

	@Override
	public boolean onPrepareOptionsMenu(Menu wkMenu) {			//表示直前に行う非表示や非選択設定
		final String TAG = "onPrepareOptionsMenu[MA]";
		String dbMsg ="Menu"+wkMenu.toString();////////////////////////////////////////////////////////////////////////////
		boolean retBool = true;
		try{
			if(calentFragment == web_fragment){
				web_fragment.webPrepareOptionsMenu(wkMenu);		//表示直前に行う非表示や非選択設定
			}
			Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG ,"エラー発生；"+e);
			return false;
		}
		return retBool;
	}											//表示直前に行う非表示や非選択設定

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final String TAG = "onOptionsItemSelected[MA]";
		String dbMsg ="MenuItem"+item.toString();/////////////////////////////////////////////////
		boolean retBool = false;
		try{
			retBool = funcSelected(item);
		} catch (Exception e) {
			Log.e(TAG ,"エラー発生；"+e);
			return false;
		}
		return retBool;
	}

	public boolean funcSelected(MenuItem item) {
		final String TAG = "funcSelected[MA]";
		String dbMsg ="MenuItem"+item.toString();/////////////////////////////////////////////////
		try{
			Log.i(TAG,dbMsg);
			if(calentFragment == web_fragment){
				web_fragment.webOptionsItemSelected(item);		//表示直前に行う非表示や非選択設定
			}

			switch (item.getItemId()) {
				case R.id.menu_sonota_settei:				//設定
					prefarenceHyouji ();
					return true;
				case R.id.menu_sonota_settei_syoukyo:                        //, 0,CTM_SETTEI_DEll);		//設定消去";	MENU_SETTEI_DEll
					prefarenceSyoukyo ();
					return true;
				case  R.id.menu_sonota_syuuryou:						//終了";
					quitMe();			//このActivtyの終了
					return true;
			}
			return false;
		} catch (Exception e) {
			Log.e(TAG,"エラー発生；"+e);
			return false;
		}
	}

	@Override
	public void onOptionsMenuClosed(Menu wkMenu) {
	}
	///////////////////////////////////////////////////////////////////////////////toolbarに表示するメニュー//
//	@Override
//	protected void onPause () {
//		super.onPause ();
//		final String TAG = "onPause[MA]";
//		String dbMsg = "";//////////////////
//		try {
//			dbMsg = "adView=" + mAdView;//////////////////
//			if( mAdView != null){
//				mAdView.pause ();
//			}
//			dbMsg = ">>" + mAdView;//////////////////
//			//		Log.i (TAG, dbMsg);
//		} catch (Exception e) {
//			Log.e ("onPause", dbMsg + "；" + e);
//		}
//	}

	@Override
	public void onResume() {
		super.onResume();
		final String TAG = "onResume[MA]";
		String dbMsg = "";//////////////////
		try {
		if( mAdView != null){
			mAdView.resume();
		}
			//		Log.i (TAG, dbMsg);
		} catch (Exception e) {
			Log.e ("onPause", dbMsg + "；" + e);
		}
	}

	@Override
	protected void onDestroy () {
		super.onDestroy ();
		final String TAG = "onDestroy[MA]";
		String dbMsg = "";//////////////////
		try {
			dbMsg = "adView=" + mAdView;//////////////////
			mAdView.destroy ();
			dbMsg = ">>" + mAdView;//////////////////
			setVisible (false);							//android.view.WindowLeaked: Activity 対策
	//		Log.i (TAG, dbMsg);
		} catch (Exception e) {
			Log.e (TAG, dbMsg + "；" + e);
		}
	}
}
