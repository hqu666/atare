package com.hijiyama_koubou.atare_kun;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import net.nend.android.NendAdInterstitial;
import net.nend.android.NendAdView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//android.view.WindowLeaked: Activity com.hijiyama_koubou.atare_kun.AtarekunnActivity has leaked window com.android.internal.policy.impl.PhoneWindow$DecorView{42924fc0 V.E..... R.....I. 0,0-0,0} that was originally added

public class AtarekunnActivity extends Activity  implements plogTaskCallback{			//, View.OnKeyListener			 implements plogTaskCallback
	// extends Fragment		Activity , View.OnKeyListener		 View.OnClickListener , View.OnLongClickListener ,
	public Spinner maxValSP;			//六合彩用の可変個数スピナー
	public Spinner kujisyuSP;			//くじの種類スピナー
	public Spinner repetSp;				//繰り返し選択スピナー
	public TextView IppatuMainTF;	//一発表示のメイン
	public TextView IppatuSubTF;	//一発表示のサブ
	public LinearLayout kahenHyoujiLL;	//表示個数設定部のリニアレイアウト
	public LinearLayout ruisekiLL;	//累積設定部のリニアレイアウト
	public Button ruisekiBT;
	public TextView ruisekiSuuTF;		//出現回数上限の表示枠
	public ScrollView lank_sv;					//出現状況表示のスクロールビュー
	public TextView lank1data;			//出現状況表示
	public TextView aCount;				//現在のカウント
	public TextView tCount;				//最終カウント
//	public LinearLayout web_aria;			//webキットの読み込み
	public WebView webView;		// Webビュー   ☆ WebFtagnemt webFtagnemt　ではnendViewが二重に読み込まれる
	public FrameLayout frag_aria;	//Fragmentを読み込むFrameLayout
	public String sarchUrlOrg = "https://www.google.co.jp/";

	public long startTime;
	public SimpleDateFormat sdf_mss = new SimpleDateFormat("mm:ss SSS");		//02;03 414=123414

	public int slotBT_ID=1;		//順送ボタン
	public int lank1data_ID=slotBT_ID+1;				//出現回数上限の表示枠

	public String dbMsg="";
	public Resources res;
	public Context rContext;
	public Locale locale;	// アプリで使用されているロケール情報を取得
	public SharedPreferences sharedPref;
	public Editor myEditor ;
	public static int APIL=Build.VERSION.SDK_INT;	//APIレベル
	public int kurikaesi_val=100;													//繰り返し判定数
	public boolean prefUseDlog=true;											//ダイアログの使用/未使用	true	false
 	public boolean dPadAri=false;							//ダイヤルの有無
	public int kujiId;
	public static String repSyurui="";			//一発繰り返し区分
	public static String kujiSyurui="";			//くじの種類	val_val
	public static int val_val=6;					//乱数の個数
	public static int randumStart_val =1;			//乱数の開始値
	public static int randumEnd_val =43;			//乱数の終了値
	public static boolean honJuufuku =true;		//本番号の重複
	public static int SPNval_val =0;				//特番の数
	public static int SPNStart_val =0;			//特番の開始値
	public static int SPNumEnd_val =0;			//特番の終了値
	public static boolean juufuku =false;		//特番と本番合との重複
	public int[] tokubanArray =null;

	public static String spower_ball_URL = "http://www.powerball.com/pb_home.asp";			//Power Ball(米)
	public static String mega_millions_URL = "http://www.megamillions.com/";						//Mega Millions(米)
	public static String euro_mllions_URL= "http://www.euro-millions.com/results.asp";		//Euro Millions
	public static String mar_six_URL= "http://bet.hkjc.com/marksix/default.aspx";				//六合彩（香港;マークシックス）
	public static String loto7_URL= "http://www.takarakujinet.co.jp/loto7/index2.html";			//ロト7<
	public static String loto6_URL="http://www.takarakujinet.co.jp/loto6/index2.html";			//ロト6
	public static String mini_loto_URL= "http://www.takarakujinet.co.jp/miniloto/index2.html";		//ミニロト
	public static String num3_URL= "http://www.takarakujinet.co.jp/numbers3/index2.html";			//ナンバーズ3
	public static String num4_URL= "http://www.takarakujinet.co.jp/numbers4/index2.html";			//ナンバーズ4

	public static String shyougouURL=loto6_URL;	//照合URL

	public static float spower_ball_WHB = 1.0f;			//Power Ball(米)
	public static float mega_millions_WHB = 1.0f;			//Mega Millions(米)
	public static float euro_mllions_WHB = 1.0f;			//Euro Millions
	public static float mar_six_WHB = 1.0f;				//六合彩（香港;マークシックス）
	public static float loto7_WHB = 1.0f;					//ロト7<
	public static float loto6_WHB = 1.0f;					//ロト6
	public static float mini_loto_WHB = 1.0f;				//ミニロト
	public static float num3_WHB = 1.0f;					//ナンバーズ3
	public static float num4_WHB = 1.0f;					//ナンバーズ4

	public static float w_hyouji_bairitu =loto6_WHB;			//表示倍率

	public String rData;
	public int ruisekiMax;				//出現回数最大値
	public int maxVal;					//本数字の最大値
	public int subMaxVal;				//別枠の最大値

	public int kurikaesiCount;				//やり直し総数
	public int dArrayCount;
	public Intent intentWV;
	public ProgressDialog pDialog;
	public ProgressDialog 	m_progressDialog;
	public Activity uiActivity;
	public plogTask pTask;						//プログレス表示
	public String pdTitol;						//ダイアログタイトル
	public String pdMessage;						//その時のメッセージ
	public int pdMaxVal;						//その時のメインバーの最大値
 	public boolean riuseki=false;			//累積表示をする
 	public boolean riusekiCyuu=false;		//累積中；止めると異常終了

	public int nowSelectMenu = 0;							//現在選ばれているメニュ
	public static final int MENU_sousa=10;						//操作メニュー
	public static final int MENU_kekkaCheck=MENU_sousa+1;		//結果確認
	public static final int MENU_ippatu=MENU_kekkaCheck+5;		//一発表示
	public static final int MENU_ruiseki=MENU_ippatu+1;			//累積
	public static final int MENU_SONOTA=MENU_ruiseki+10;			//その他
	public static final int MENU_HELP=MENU_SONOTA+1;				//ヘルプ表示
	public static final int MENU_SETTEI=MENU_HELP+1;				//設定
	public static final int MENU_SETTEI_DEll=MENU_SETTEI+1;		//設定消去
	public static final int dr_cyuukiHyouji = MENU_SETTEI_DEll+10;								//対象国外で警告表示
	public static final int dr_webRecord = dr_cyuukiHyouji+1;			//表示されているwebページの設定保存
	public static final int dr_kujiRecord = dr_webRecord+1;				//表示されているくじの設定保存
	public static final int dr_kujiRecTuika = dr_kujiRecord+1;				//新しいくじ
	public static final int dr_kujiRecKousin = dr_kujiRecTuika+1;			//表示されているくじの設定保存実行
	public static final int dr_kujiSakujyo = dr_kujiRecKousin+1;			//くじを削除
	public static final int dr_otherSentaku = dr_kujiSakujyo+1;			//海外くじの選択

	public static final int MENU_END=dr_otherSentaku+10;				//終了
	public static final String kugiri = ",";
//	private static final String MY_AD_UNIT_ID = null;

	public final int PROGRESS_SPIN=MENU_END+100;
	public final int PROGRESS_HORIZ=PROGRESS_SPIN+1;

/*	Error:Exception in thread "pool-2-thread-3"
	Error:UNEXPECTED TOP-LEVEL ERROR:
	Error:java.lang.OutOfMemoryError: GC overhead limit exceeded
	Error:java.lang.OutOfMemoryError: GC overhead limit exceeded
	:app:transformClassesWithDexForDebug FAILED
	Error:Execution failed for task ':app:transformClassesWithDexForDebug'.
	com.android.build.api.transform.TransformException: com.android.ide.common.process.ProcessException:
	java.util.concurrent.ExecutionException: com.android.ide.common.process.ProcessException: org.gradle.process.internal.ExecException: Process 'command 'C:\Program Files\Java\jdk1.8.0_91\bin\java.exe'' finished with non-zero exit value 3
*/

/// 汎用関数 ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public boolean isNum(String motoStr){		//数字ならtrue
		final String TAG = "isNum";
		String dbMsg = "";//////////////////
		try {
			int a= Integer.parseInt(motoStr);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean isInListInt(int[] groups , int tInt){		//渡された数字が既にリストに登録されていたらtrue
		boolean retBool =false;
		final String TAG = "isInListInt";
		String dbMsg= groups.length + "件目で" + tInt;//////////////////////////////////////////
		try{
			int i=1;
			for(int tName:groups){		//for(i=0;i<pCount;i++){
				if(tInt == tName){
					dbMsg= i +"/" +groups.length+";" + tInt +"/" +tName ;////////////////////////////////////////////////////////
					return true;
				}
			}
//			Log.i(TAG,dbMsg);
//			dbMsg="tStr=" + tStr +"が"+ groups.get(0) + "～" + groups.get(groups.size()-1) + "に有るか；件数="+ groups.size() ;//////////////////////////////////////////
		} catch (Exception e) {
			Log.e(TAG,"[OrgUtil]" +dbMsg+e.toString());
		}
		return retBool;
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////// 汎用関数 ///
//メニューボタンで表示するメニュー///////////////////////////////////////////////////////////////////////////////
	public boolean onCreateOptionsMenu(Menu flMenu) {
	//	//Log.d("onCreateOptionsMenu","NakedFileVeiwActivity;mlMenu="+flMenu);
		getMenuInflater().inflate(R.menu.atarekunn_activity , flMenu);		//メニューリソースの使用
		return super.onCreateOptionsMenu(flMenu);
	}

	public boolean makeOptionsMenu(Menu flMenu) {	//ボタンで表示するメニューの内容
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu flMenu) {			//表示直前に行う非表示や非選択設定
		//	//Log.d("onPrepareOptionsMenu","NakedFileVeiwActivity;mlMenu="+flMenu);
		flMenu.findItem(R.id.menu_sousa_webrecord).setEnabled(true);		//現在のwebページを記録
		flMenu.findItem(R.id.menu_sousa_ippatu).setEnabled(true);		//結果確認	MENU_kekkaCheck
		flMenu.findItem(R.id.menu_sousa_ippatu).setEnabled(true);		//一発表示	MENU_ippatu
		flMenu.findItem(R.id.menu_sousa_ruiseki).setEnabled(true);		//乱数累積	MENU_ruiseki

		flMenu.findItem(R.id.menu_sonota_settei).setVisible(true);		//設定	MENU_SETTEI
		flMenu.findItem(R.id.menu_sonota_settei).setEnabled(true);		//設定	MENU_SETTEI
		flMenu.findItem(R.id.menu_sonota_help).setVisible(true);		//ヘルプ表示	MENU_HELP
		flMenu.findItem(R.id.menu_sonota_syuuryou).setVisible(true);	//終了	MENU_END
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		final String TAG = "onOptionsItemSelected";
		String dbMsg = "";//////////////////
		try{
			dbMsg ="MenuItem"+item.getItemId()+",getItemId"+item.getItemId();////////////////////////////////////////////////////////////////////////////

			nowSelectMenu = item.getItemId();							//現在選ばれているメニュ
			switch (nowSelectMenu) {
			case R.id.menu_sousa_webrecord:				//現在のwebページを記録
				webRecord();			//表示されているwebページの設定保存
				return true;
			case R.id.menu_sousa_ippatu:					//一発表示	MENU_ippatu
	//			wriNum(setNRNum(randumStart_val,randumEnd_val,val_val));				//一発表示
				return true;
			case R.id.menu_sousa_ruiseki:					//乱数累積 MENU_ruiseki
				ruiseki();						//乱数の累積
				return true;
			case R.id.menu_sousa_kekkaCheck:				//果確認	MENU_kekkaCheck
				intentWV = new Intent(AtarekunnActivity.this,wKit.class);			//webで
				intentWV.putExtra("dataURI",shyougouURL);							//照合URL
				startActivity(intentWV);
				return true;
			case R.id.menu_sonota_settei:						//).setVisible(true);		//設定	MENU_SETTEI
				delPrif();		//☆暫定対策；；プリファレンスの内容削除
				Intent intentPRF = new Intent(AtarekunnActivity.this,MyPreferences.class);			//プリファレンス
				dbMsg = "繰り返し= " + kurikaesi_val ;
				intentPRF.putExtra("kurikaesi_val",kurikaesi_val);		//繰り返し		String.valueOf(kurikaesi_val)
				dbMsg = dbMsg +",ダイヤログの利用= " + prefUseDlog ;
				intentPRF.putExtra("prefUseDlog",prefUseDlog);			//ダイヤログの利用
				dbMsg = dbMsg +",ダイヤルキー= " + dPadAri ;
				intentPRF.putExtra("dPadAri",dPadAri);					//ダイヤルキー
				Log.i(TAG,dbMsg);
				startActivityForResult(intentPRF , MENU_SETTEI);
				return true;
			case R.id.menu_sonota_settei_syoukyo:						//, 0,CTM_SETTEI_DEll);		//設定消去";	MENU_SETTEI_DEll
				delPrif();		//プリファレンスの内容削除
				String fn = this.getApplicationContext().getString(R.string.kuji_file);		//kuji.db
				dbMsg = "fn= " + fn ;
				kuji_table = getResources().getString(R.string.kuji_table);				//kuji_table</string>
				dbMsg = dbMsg +  ",テーブル名=" + kuji_table;
				kujiHelper = new KujiHelper(getApplicationContext() , fn);				//計算履歴トヘルパ
				File dbF = getDatabasePath(fn);			//Environment.getExternalStorageDirectory().getPath();		new File(fn);		//cContext.
				dbMsg = dbMsg + ",dbF=" + dbF;
				dbMsg = dbMsg + " , exists=" + dbF.exists() +" , canWrite=" + dbF.canWrite();
				boolean syouhkyo = dbF.delete();
				dbMsg = dbMsg + " , syouhkyo=" + syouhkyo;
				if(syouhkyo){
					Toast.makeText(AtarekunnActivity.this,
							this.getApplicationContext().getString(R.string.menu_item_sonota_syoukyo) , Toast.LENGTH_LONG).show();
				}
	//			Log.i(TAG,dbMsg);
				return true;
			case R.id.menu_sonota_help:						//ヘルプ表示	MENU_HELP
				String helpURL;
				Intent intentWV = new Intent(AtarekunnActivity.this,wKit.class);			//webでヘルプ表示
				if(locale.equals( Locale.JAPAN)){										//日本語の場合のみconstant for ja_JP.
					helpURL = "http://www.geocities.jp/hqu666/atarekunn/";		//日本語ヘルプ
				}else {
					helpURL = "http://www.geocities.jp/hqu666/atarekunn/en/index.html";	//英語ヘルプ
				}
				intentWV.putExtra("dataURI",helpURL);		//"file:///android_asset/index.html"
				startActivity(intentWV);
				return true;
			case R.id.menu_sonota_syuuryou:					//終了	MENU_END
				quitMe();		//このアプリを終了する
				return true;
			}
		return false;
		} catch (Exception e) {
			Log.e(TAG,"エラー発生；"+e);
			return false;
		}
	}

	public void onOptionsMenuClosed(Menu flMenu) {
		//Log.d("onOptionsMenuClosed","NakedFileVeiwActivity;mlMenu="+flMenu);
	}

//コンテキストメニュー//////////////////////////////////////////////////////メニューボタンで表示するメニュー//
	///ContextMenu///http://techbooster.jpn.org/andriod/ui/7490///
		public String contextTitile = null;
		static final int CONTEXT_web_rec = MENU_END+10;					//現在のwebページを記録
		static final int CONTEXT_web_ken = CONTEXT_web_rec+1;			//このくじに関する検索
		static final int CONTEXT_web_Sre = CONTEXT_web_ken+1;			//webのスケールリセット
		static final int CONTEXT_kuji_set = CONTEXT_web_Sre+1;			//このくじの設定変更
		static final int CONTEXT_kuji_sakujyo = CONTEXT_kuji_set+1;		//このくじを削除
		ArrayList<String> otherList;		//その他のくじ

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		final String TAG = "onCreateContextMenu[AtarekunnActivity]";
		String dbMsg="開始";/////////////////////////////////////
		try{
			String contextTitile = kujiSyurui + getResources().getString(R.string.menu_item_sonota_settei);
			dbMsg=contextTitile+";";/////////////////////////////////////
			menu.setHeaderTitle(contextTitile);		//リスト操作;コンテキストメニューの設定
			//Menu.add(int groupId, int itemId, int order, CharSequence title)
			menu.add(0, CONTEXT_web_rec, 0, getResources().getString(R.string.menu_item_sousa_webrecord));		//現在のwebページを記録
			String sarchUrl = webView.getUrl();	                                        	//String sarchUrl = webFtagnemt.getUrl();
			dbMsg= dbMsg + "は" + shyougouURL + " [" + sarchUrl.equals(shyougouURL) + "]と "
			+ sarchUrlOrg + " [" +  sarchUrl.contains(sarchUrlOrg) + "]で "  + shyougouURL+ ">>" + sarchUrl;/////////////////////////////////////
	//		Log.i(TAG,dbMsg);
			if(sarchUrl.equals(shyougouURL) ||
				 sarchUrl.contains(sarchUrlOrg)
					){						//ページが変わっていなければ非表示
				menu.getItem(0).setEnabled(false);
			}
			menu.add(0, CONTEXT_web_ken, 0, kujiSyurui + getResources().getString(R.string.menu_item_sousa_webkennsaku));	//の検索
			menu.add(0, CONTEXT_web_Sre, 0, getResources().getString(R.string.pref_web_zen));	//全画面表示
			menu.add(0, CONTEXT_kuji_set, 0, kujiSyurui + getResources().getString(R.string.menu_item_sonota_settei));	//くじの設定
			menu.add(0, CONTEXT_kuji_sakujyo, 0, kujiSyurui + getResources().getString(R.string.comon_sakujyo));	//くじの削除
		}catch (Exception e) {
			Log.e(TAG,dbMsg +"で"+e.toString());
		}
	}

	public boolean onContextItemSelected(MenuItem item) {
	//	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final String TAG = "onContextItemSelected[AtarekunnActivity]";
		String dbMsg="開始";/////////////////////////////////////
		try{
			dbMsg="item" + item.getItemId() + ")";/////////////////////////////////////
			switch (item.getItemId()) {
			case CONTEXT_web_rec:					//現在のwebページを記録
				webRecord();							//表示されているwebページの設定保存
				return true;
			case CONTEXT_web_ken:					//このくじの検索
				webKensaku();			//くじに関する情報のweb検索
				return true;
			case CONTEXT_web_Sre:					//web全画面表示
		//		webView.zenGamen();//		webFtagnemt.zenGamen();
				return true;
			case CONTEXT_kuji_set:					//くじの設定
				String dTitol = kujiSyurui + getResources().getString(R.string.menu_item_sonota_settei);
				String dMsg = getResources().getString(R.string.kujiset_tuikka_msg);
				kujiRecord(dTitol , dMsg);							//表示されているくじの設定保存
				return true;
			case CONTEXT_kuji_sakujyo:
				kujiSakujyo(  );							//くじを削除
				return true;
			default:
				return super.onContextItemSelected(item);
			}
		}catch (Exception e) {
			Log.e(TAG,dbMsg +"で"+e.toString());
		}
		return true;
	}

///くじ種と繰り返し選択//////////////////////////////////////////////////////コンテキストメニュー//
	public void kujiSentaku(String kujiSyurui) {			///くじ種選択 <onCreate、kujiRecTuika
		final String TAG = "kujiSentaku[AtarekunnActivity]";
		String dbMsg = "";//////////////////
		try{
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Locale kuni = Locale.getDefault ();
			dbMsg = "キー=" + kuni  ;//////////////////
			TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String pCountry = telManager.getSimCountryIso();			 // 取得
			dbMsg = dbMsg + "電キー=" + pCountry;								//////////////////
			dbMsg = dbMsg + "電話回線=" + pCountry + ",くじ=" + kujiSyurui ;//////////////////
		//			Log.i(TAG,dbMsg);
//		http://memorva.jp/tool/app/cctld_country_code.php
			if( pCountry.equals("jp") ){
				adapter.add(getResources().getString(R.string.loto7_name));	// アイテムを追加します
				adapter.add(getResources().getString(R.string.loto6_name));
				adapter.add(getResources().getString(R.string.mini_loto_name));
				adapter.add(getResources().getString(R.string.num4_name));
				adapter.add(getResources().getString(R.string.num3_name));
			}else if( pCountry.equals("us")){
				adapter.add(getResources().getString(R.string.power_ball_name));
				adapter.add(getResources().getString(R.string.mega_millions));
			} else if(pCountry.equals("hk")			//香港
					|| pCountry.equals("cn")				//中国
					|| pCountry.equals("tw")				//台湾
//					|| Locale.TRADITIONAL_CHINESE.equals(kuni)
//					|| Locale.SIMPLIFIED_CHINESE.equals(kuni)
					) {				// HK 	Hong Kong 	>>>cc 	China (中華人民共和国)
				adapter.add(getResources().getString(R.string.mar_six_name));
			} else  if(pCountry.equals("fr")
					|| pCountry.equals("uk")
					|| pCountry.equals("es")		//スペイン Spanish (es_ES)
					|| pCountry.equals("ie")		//アイルランド
					|| pCountry.equals("pt")		//ポルトガル
					|| pCountry.equals("lu")		//ルクセンブルク
					|| pCountry.equals("ch")		//スイス
					|| pCountry.equals("be")		//ベルギー Dutch, Belgium (nl_BE)
					|| pCountry.equals("at")		//オーストリアGerman, Austria (de_AT)
					) {
				adapter.add(getResources().getString(R.string.ruro_mllions_name));
			} else{
				if(Locale.JAPAN.equals(kuni)) {				// ja_JP
					adapter.add(getResources().getString(R.string.loto7_name));	// アイテムを追加します
					adapter.add(getResources().getString(R.string.loto6_name));
					adapter.add(getResources().getString(R.string.mini_loto_name));
					adapter.add(getResources().getString(R.string.num4_name));
					adapter.add(getResources().getString(R.string.num3_name));
				} else if(Locale.US.equals(kuni)) {				//アメリカ
					adapter.add(getResources().getString(R.string.power_ball_name));
					adapter.add(getResources().getString(R.string.mega_millions));
				} else if(Locale.CHINA.equals(kuni)
						|| Locale.TAIWAN.equals(kuni)
						|| Locale.CHINESE.equals(kuni)
						|| Locale.TRADITIONAL_CHINESE.equals(kuni)
						|| Locale.SIMPLIFIED_CHINESE.equals(kuni)
//						|| kokumei.equals("hk")
						) {				// HK 	Hong Kong 	香港>>>cc 	China (中華人民共和国)
					adapter.add(getResources().getString(R.string.mar_six_name));
				} else 	if(Locale.FRANCE.equals(kuni)				//フランス
						|| Locale.UK.equals(kuni)					//イギリス
						|| Locale.ENGLISH.equals(kuni)
//						|| kokumei.equals("sp")					//スペイン Spanish (es_ES)
//						|| kokumei.equals("nik")					//北アイルラン	//アイルランドEnglish, Ireland (en_IE)
//						|| kokumei.equals("ie")						//・アイルランド
//						|| kokumei.equals("po")					//ポルトガル
//						|| kokumei.equals("sz")	//スイス
//						|| kokumei.equals("be")					//ベルギー Dutch, Belgium (nl_BE)
//						|| kokumei.equals("au")					//オーストリアGerman, Austria (de_AT)
						) {		//ルクセンブルグ,ポルトガル？
					adapter.add(getResources().getString(R.string.ruro_mllions_name));
				}
			}
			dbStart();				//データベースの作成・オープン→
			otherList = new ArrayList<String>();		//その他のくじ(国外の登録くじ)
			dbMsg = dbMsg + ",kujimeiList=" +kujimeiList ;//////////////////
			if(kujimeiList != null){
				dbMsg = dbMsg + ",DB内の全くじ=" + kujimeiList.toString() ;//////////////////
				otherList.addAll(kujimeiList);					//登録してあるくじ
			}
			for(int i=0 ;i< adapter.getCount(); i++ ){
				dbMsg = i + ")" ;//////////////////
				String oStr = adapter.getItem(i);
				dbMsg = dbMsg + oStr ;//////////////////
				otherList.remove(oStr);
			}
			dbMsg = dbMsg + ",既存のくじ=" + tourkuKuji.toString() ;/////////////////
			dbMsg = dbMsg + ">国外の登録くじ=" + otherList.toString() ;//////////////////
			ArrayList<String> origList = new ArrayList<String>();		//その他のくじ
			origList.addAll(otherList);
			dbMsg = dbMsg + ",その他のくじ=" + origList.toString() ;//////////////////
	//		Log.i(TAG,dbMsg);
			for(int i=0 ;i< otherList.size(); i++ ){			//オリジナルが混入した国外の登録くじ
				dbMsg = i + ")" ;//////////////////
				String oStr = otherList.get(i);
				dbMsg = dbMsg + oStr ;//////////////////
				for(int k=0 ;k< tourkuKuji.size(); k++ ){
					dbMsg = k + ")" ;//////////////////
					String omStr = tourkuKuji.get(k);
					dbMsg = dbMsg + oStr ;//////////////////
					if(omStr.equals(oStr)){						//既存くじを
						origList.remove(oStr);					//除外
					}
				}
			}
			dbMsg = dbMsg + ">>" + origList.toString() ;//////////////////
	//		Log.i(TAG,dbMsg);
			for(int i=0 ;i< origList.size(); i++ ){			//オリジナルをリストに追記
				dbMsg = i + ")" ;//////////////////
				String oStr = origList.get(i);
				dbMsg = dbMsg + oStr ;//////////////////
				adapter.add(oStr);
				otherList.remove(oStr);		//海外くじからオリジナルを除去
			}
	//		Log.i(TAG,dbMsg);
			kuji_db.close();
			adapter.add(getResources().getString(R.string.sonota));

			kujisyuSP.setAdapter(adapter);	// アダプターを設定します
			if( kujiSyurui != null ){
				kujisyuSP.setSelection(adapter.getPosition(kujiSyurui));
			}

	// スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
			kujisyuSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
					final String TAG = "onItemSelected[kujiSentaku]";
					String dbMsg = "";//////////////////
					try{
						Spinner spinner = (Spinner) parent;
						dbMsg = "spinner=" + spinner;//////////////////
						String kujiSyurui = (String) spinner.getSelectedItem();	// 選択されたアイテムを取得します
		//				Toast.makeText(AtarekunnActivity.this, AtarekunnActivity.this.kujiSyurui , Toast.LENGTH_LONG).show();
//						MyPreferences MP = new MyPreferences();
//						MP.kujiTeisuu(AtarekunnActivity.this.kujiSyurui  , getResources() , AtarekunnActivity.this);						//各くじの定数設定	 , AtarekunnActivity.this
						dbMsg = kujiSyurui ;
						String syougou = getResources().getString(R.string.sonota);
						dbMsg = dbMsg+ "、照合=" + syougou ;
						Log.i(TAG,dbMsg);
						if( kujiSyurui.equals(syougou ) ){
							String[] items = otherList.toArray(new String[0]);
							otherSentaku( getResources().getString(R.string.sonota) , items );			//海外くじの選択
						}else{
							kujiSentakuKekka( kujiSyurui);			//くじ種選択本体
						}
					} catch (Exception e) {
						Log.e(TAG,"エラー発生；"+e);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
				});

		} catch (Exception e) {
			Log.e(TAG,dbMsg+e);
		}
	}

	public void kujiSentakuKekka(String kujiSyurui) {			///くじ種選択本体
		final String TAG = "kujiSentaku[kujiSentakuKekka]";
		String dbMsg = "";//////////////////
		try{
			dbYomidasi(kujiSyurui );					//データベースへの書き込み
			dbMsg = dbMsg + ",乱数の個数=" + val_val;
			dbMsg = dbMsg + "[" + randumStart_val;
			dbMsg = dbMsg + "～" + randumEnd_val;
			dbMsg = dbMsg + "]" + shyougouURL + "[" + w_hyouji_bairitu + "]";
			reWriteAllVal();			//変数全設定
			wriAllPrif();				//プリファレンス援項目の読込み
//			readPrifMini();				//webなど
			dbMsg = dbMsg + "]" + shyougouURL + "[" + w_hyouji_bairitu + "]";
		} catch (Exception e) {
			Log.e(TAG,dbMsg+e);
		}
	}

	public void repetSentaku() {			///繰り返し選択
		final String TAG = "repetSentaku[AtarekunnActivity]";
		String dbMsg = "";//////////////////
		try{
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			adapter.add(getResources().getString(R.string.menu_item_sousa_ruisek));		//乱数累積
			adapter.add(getResources().getString(R.string.menu_item_sousa_ippatu));		//一発表示</string>

			repetSp.setAdapter(adapter);	// アダプターを設定します
			repetSp.setSelection(adapter.getPosition(repSyurui));			//一発繰り返し区分

	// スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
			repetSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
					final String TAG = "onItemSelected[repetSentaku]";
					String dbMsg = "";//////////////////
					try{
						Spinner spinner = (Spinner) parent;
						dbMsg = "spinner=" + spinner;//////////////////
						repSyurui = (String) spinner.getSelectedItem();		//一発繰り返し区分
						dbMsg =  "区分=" + repSyurui;
						if( ! AtarekunnActivity.this.riuseki ){			//累積表示しない
							dbMsg= dbMsg + ",ruisekiLL=" + ruisekiLL;/////////////////////////////////////
							AtarekunnActivity.this.ruisekiLL.setVisibility(View.GONE);
							dbMsg= dbMsg + ",lank_sv=" + lank_sv;/////////////////////////////////////
							AtarekunnActivity.this.lank_sv.setVisibility(View.GONE);						//出現状況表示のスクロールビュー
						}else{
							AtarekunnActivity.this.ruisekiLL.setVisibility(View.VISIBLE);											//累積設定部のリニアレイアウト
						}
						if( repSyurui.equals(getResources().getString(R.string.menu_item_sousa_ippatu)) ){
			//				lank1data.setText(getResources().getString(R.string.main_tf_syoki));															//出現状況表示
					//		IppatuMainTF.setText(getResources().getString(R.string.gr1_name));		//乱数一発！速攻表示		slotBT
							IppatuMainTF.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
						//			dbMsg ="slotBTのonClick";////////////////////////////////////////////////////////////////////////////
							//		setNRNum(randumStart_val,randumEnd_val,val_val);						//乱数を指定した個数、表示する
									String dbMsg = randumStart_val + "～" + randumEnd_val +"を" + val_val + "個";////////////////////
									int[] mainGr =setNRNum(randumStart_val,randumEnd_val,val_val);
									Log.d("Ippatu",dbMsg);
										wriNum( mainGr , IppatuMainTF);			//一発表示
										dbMsg = dbMsg +"と"+ SPNStart_val + "～" + SPNumEnd_val +"を" + SPNval_val + "個";//////////////////////////////
										if(SPNval_val>0){
											wriNum(setSubNRNum(SPNStart_val , SPNumEnd_val , SPNval_val , mainGr) , IppatuSubTF);		//特番の終了値
										}
									}
								});
						}else{
							dbMsg= dbMsg + ",累積表示=" + riuseki;/////////////////////////////////////
			//				IppatuMainTF.setText(getResources().getString(R.string.gr2_name));		//乱数累積
							IppatuMainTF.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									final String TAG = "onClick[累積]";
									String dbMsg = "";//////////////////
									try{
										dbMsg ="累積中" + riusekiCyuu;//////累積中；止めると異常終了///////
										if(! riusekiCyuu){
											ruiseki();
										}
						//				Log.i(TAG,dbMsg);
									} catch (Exception e) {
										Log.e(TAG,"エラー発生；"+e);
									}
									}
								});
						}
						setTitolComent(); 																//ウィンドウタイトルの書き換え
			//			Log.i(TAG,dbMsg);
			//			reWriteAllVal();//変数全設定
						wriAllPrif();		//プリファレンス援項目の読込み
					} catch (Exception e) {
						Log.e(TAG,"エラー発生；"+e);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
				});
		} catch (Exception e) {
			Log.e(TAG,"エラー発生；"+e);
		}
	}

///////////////////////////////////////////////////////くじ種選択

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final String TAG = "onCreate[AtarekunnActivity]";
		String dbMsg = "";//////////////////
		try{
			ruisekiMax = 0;				//出現回数最大値
			maxVal = 0;					//本数字の最大値
			subMaxVal = 0;				//別枠の最大値
			repSyurui=getResources().getString(R.string.menu_item_sousa_ruisek);		//乱数累積;	一発繰り返し区分
			locale = Locale.getDefault();	// アプリで使用されているロケール情報を取得
			rContext =this;
			res = getResources();
	//		sharedPref = getSharedPreferences(getResources().getString(R.string.pref_f_name),MODE_PRIVATE);		//	getSharedPreferences(prefFname,MODE_PRIVATE);
			sharedPref = PreferenceManager.getDefaultSharedPreferences(this);					//this.getSharedPreferences(this, MODE_PRIVATE);		//
			myEditor = sharedPref.edit();
			readPrif();					//プリファレンスの読込み
			tourkuKuji = new ArrayList<String>();		//既存のくじ
			tourkuKuji.add(getResources().getString(R.string.power_ball_name));
			tourkuKuji.add(getResources().getString(R.string.mega_millions));
			tourkuKuji.add(getResources().getString(R.string.ruro_mllions_name));
			tourkuKuji.add(getResources().getString(R.string.mar_six_name));
			tourkuKuji.add(getResources().getString(R.string.loto7_name));
			tourkuKuji.add(getResources().getString(R.string.loto6_name));
			tourkuKuji.add(getResources().getString(R.string.mini_loto_name));
			tourkuKuji.add(getResources().getString(R.string.num4_name));
			tourkuKuji.add(getResources().getString(R.string.num3_name));

			dbMsg = "前回=" + kujiSyurui;//////////////////
			setContentView(R.layout.main);
			kahenHyoujiLL = (LinearLayout)findViewById(R.id.kahen_hyouji);	//表示個数設定部のリニアレイアウト
			IppatuMainTF = (TextView)findViewById(R.id.f1mainTF);	//一発表示のメイン
	//		registerForContextMenu(IppatuMainTF);			//Viewに追加する場合、registerForContextMenu(View);が必要
			IppatuSubTF = (TextView)findViewById(R.id.f1SubTF);	//一発表示のサブ
	//		slotBT = (Button) this.findViewById(R.id.ippatuBtn);		//一発表示
	//		IppatuMainTF.setId(slotBT_ID);
			ruisekiLL = (LinearLayout)findViewById(R.id.ruisekiLL);	//累積設定部のリニアレイアウト

			if(subMaxVal == 0){			//別枠の最大値が設定されていなければ
				IppatuSubTF.setVisibility(View.GONE);		//6桁目の表示枠
	//			RuisekiSubTF.setVisibility(View.GONE);		//累積6桁目の表示枠
				/*0:view.VISIBLE・・・表示
				 *1; view.INVISIBLE・・・非表示（非表示にしたスペースは詰めない）
				 *8; view.GONE・・・非表示（非表示にしたスペースを詰める）
				 */
			}

			ruisekiSuuTF = (TextView)findViewById(R.id.ruisekiSuuTF);			//出現回数上限の表示枠
			aCount = (TextView)findViewById(R.id.aCount);						//現在のカウント
			aCount.setText(String.valueOf(0));
			tCount = (TextView)findViewById(R.id.tCount);						//最終カウント
			lank_sv = (ScrollView)findViewById(R.id.lank_sv);					//出現状況表示のスクロールビュー
			lank1data = (TextView)findViewById(R.id.lank1data);					//出現状況表示
	//		lank1data.setId(lank1data_ID);
			maxValSP = (Spinner)findViewById(R.id.maxValSP);	//六合彩用の可変個数スピナー
			setMaxSP(6 , 15);		//maxValSPのアイテム設定
			maxValSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		//		@Override
				public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
					Spinner spinner = (Spinner) parent;
					String item = (String) spinner.getSelectedItem();	// 選択されたアイテムを取得します
					val_val=Integer.valueOf(item);				//乱数の個数
				}
		//		@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});

            kujisyuSP = (Spinner)findViewById(R.id.kujisyuSP);	//くじの種類スピナー
			kujiSentaku( kujiSyurui );										///くじ種選択
	//		kujisyuSP.setOnKeyListener(this);
			repetSp = (Spinner)findViewById(R.id.repetSp);	//繰り返し選択スピナー
			repetSentaku();												///繰り返し選択
			riuseki = false;
			dbMsg= dbMsg + ",累積表示=" + riuseki;/////////////////////////////////////
			if( ! riuseki ){			//累積表示しない
				dbMsg= dbMsg + ",ruisekiLL=" + ruisekiLL;/////////////////////////////////////
				ruisekiLL.setVisibility(View.GONE);
				dbMsg= dbMsg + ",lank_sv=" + lank_sv;/////////////////////////////////////
				lank_sv.setVisibility(View.GONE);						//出現状況表示のスクロールビュー
			}
//			frag_aria = (FrameLayout)findViewById(R.id.frag_aria);	//Fragmentを読み込むFrameLayout
//			FragmentManager fragmentManager = getFragmentManager();
//			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			webView = (WebView)findViewById(R.id.webView);    //		webFtagnemt = new WebFtagnemt();
			WebSettings settings = webView.getSettings();
			settings.setSupportMultipleWindows(true);
			settings.setLoadsImagesAutomatically(true);
			settings.setBuiltInZoomControls(true);						//ズームコントロールを表示し
			settings.setSupportZoom(true);								//ピンチ操作を有効化
			settings.setLightTouchEnabled(true);
			settings.setJavaScriptEnabled(true);						//JavaScriptを有効化
			settings.setUseWideViewPort(true);							//読み込んだコンテンツの幅に表示倍率を自動調整
			settings.setLoadWithOverviewMode(true);						//☆setUseWideViewPortに続けて記載必要
			dbMsg = dbMsg +",照合URL="+shyougouURL ;////////////////////////////////////////////////////////////////////////////
//          webFtagnemt.init(shyougouURL);								//準備したFtagnemtをFrameLayoutに流し込む
//			fragmentTransaction.replace(frag_aria.getId() , webFtagnemt); 	//	☆.replace(android.R.id.content, では全画面が切り替わる
//			fragmentTransaction.commit();
//			registerForContextMenu(frag_aria);			//Viewに追加する場合、registerForContextMenu(View);が必要
////		checkServiceAvailable();			// Google Play Services APK がインストールされているかチェックするhttp://dev.classmethod.jp/smartphone/android/android-google-play-services-2/
////			nend_aria = (LinearLayout) findViewById(R.id.nend_aria);				//nendの読み込み範囲
////			nenvNow = false;
////			nend_aria.setVisibility(View.GONE);
			nendAdView = (NendAdView) findViewById(R.id.nend);								//nend広告表示エリアを
			nendAdView.setVisibility(View.GONE);											//非表示
			mAdView = (AdView) findViewById(R.id.adView);									//Google広告表示エリアを
 			mAdView.setVisibility(View.GONE);												//非表示
	//		Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG, dbMsg + "で"+e);
		}
	}

//	public CustomView(Context context) {	//コンストラクタ内で何か処理を入れている場合isInEditMode()で判定してやらないと上記ログが表示される
//		super(context);
//		if (!isInEditMode()) {		// レイアウトエディターでisInEditMode()で判定しないとエディター内でExceptionが発生する
//			init();			// カスタムユーザーエージェントをセットする
//		}
//	}

	View currentFo;
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {		//①ⅱヘッドのイメージは実際にローディンされた時点で設定表示と同時にウィジェットの高さや幅を取得したいときは大抵ここで取る。
		if (hasFocus) {
			final String TAG = "onWindowFocusChanged[AtarekunnActivity]";
			String dbMsg= "開始;";/////////////////////////////////////
			try{
				nendAdView.setVisibility(View.GONE);
				if( Locale.getDefault().equals( Locale.JAPAN)){										//日本語の場合のみconstant for ja_JP.
					nendAdView.setVisibility(View.VISIBLE);
				}else {
					adMobLoad();													//Google AdMobの広告設定
				}

				dbMsg= dbMsg + ",累積表示=" + riuseki;/////////////////////////////////////
				if( ! riuseki ){			//累積表示しない
					dbMsg= dbMsg + ",ruisekiLL=" + ruisekiLL;/////////////////////////////////////
					ruisekiLL.setVisibility(View.GONE);
					dbMsg= dbMsg + ",lank_sv=" + lank_sv;/////////////////////////////////////
					lank_sv.setVisibility(View.GONE);						//出現状況表示のスクロールビュー
				}
				currentFo = AtarekunnActivity.this.getCurrentFocus();
				dbMsg= dbMsg + ",currentFo=" + currentFo;/////////////////////////////////////
	//			Log.i(TAG,dbMsg);
			}catch (Exception e) {
				Log.e(TAG,dbMsg + "で"+e.toString());
			}
		 }
		 super.onWindowFocusChanged(hasFocus);
	 }

//	public LinearLayout nend_aria;			//nendの読み込み範囲
	public NendAdView nendAdView;			//nend広告表示エリア
	boolean nenvNow;

//	public void nendLoad(){		//nendの広告設定
//		final String TAG = "nendLoad[AtarekunnActivity]";
//		String dbMsg = "";//////////////////
//		try{
//			String nend_apiKey = getResources().getString(R.string.nend_apiKey);	//9f317f03b6d2b69e104dc43950fb6190eb39b451
//			int nend_spotID = Integer.parseInt(getResources().getString(R.string.nend_spotID));	//458687
//			nend_aria.setVisibility(View.VISIBLE);
//			nendAdView = new NendAdView(getApplicationContext(), nend_spotID, nend_apiKey);			// 1 NendAdView をインスタンス化
//
//			nendAdView.setListener(new NendAdListener() {
//		//	    @Override
//				public void onCompletion(NendAdInterstitialStatusCode status) {
//					final String TAG = "onCompletion[nendLoad]";
//					String dbMsg = "";//////////////////
//					try{
//						dbMsg = "NendAdInterstitialStatusCode="+status;//////////////////
//						switch (status) {
//						case SUCCESS:			// 成功
//							dbMsg = dbMsg + "成功";//////////////////
//							nenvNow = true;
//							if(dPadAri){
//								nendKeys();		//nebdの広告用のキー設定
//							}
//							break;
//						case INVALID_RESPONSE_TYPE:			// 不明な広告タイプ
//							dbMsg = dbMsg + "不明な広告タイプ";//////////////////
//							break;
//						case FAILED_AD_REQUEST:		// 広告取得失敗
//							dbMsg = dbMsg + "広告取得失敗";//////////////////
//							nenvNow = false;
//							nend_aria.setVisibility(View.GONE);
//							break;
//						case FAILED_AD_INCOMPLETE:		// 広告取得未完了
//							dbMsg = dbMsg + "広告取得未完了";//////////////////
//							break;
//						case FAILED_AD_DOWNLOAD:		// 広告画像取得失敗
//							dbMsg = dbMsg + "広告画像取得失敗";//////////////////
//							nenvNow = false;
//							nend_aria.setVisibility(View.GONE);
//							break;
//						default:
//							break;
//						}
//		//				Log.i(TAG,dbMsg);
//					} catch (Exception e) {
//						Log.e(TAG,dbMsg+"で"+e);
//					}
//				}
//
//				@Override
//				public void onClick(NendAdView arg0) {	// クリック通知
//					final String TAG = "onClick[nendLoad]";
//					String dbMsg = "";//////////////////
//					try{
//				//		Toast.makeText(getApplicationContext(), "onClick", Toast.LENGTH_LONG).show();
//						Log.i(TAG,dbMsg);
//					} catch (Exception e) {
//						Log.e(TAG,dbMsg+"で"+e);
//					}
//				}
//
//				@Override
//				public void onDismissScreen(NendAdView arg0) {	// 復帰通知
//					final String TAG = "onDismissScreen[nendLoad]";
//					String dbMsg = "";//////////////////
//					try{
//						dbMsg = "NendAdView = " + arg0;//////////////////
////						Toast.makeText(getApplicationContext(), "onDismissScreen", Toast.LENGTH_LONG).show();
//				//		nendLoad();		//nendの広告設定
//						Log.i(TAG,dbMsg);
//					} catch (Exception e) {
//						Log.e(TAG,dbMsg+"で"+e);
//					}
//				}
//
//				@Override
//				public void onFailedToReceiveAd(NendAdView arg0) {	// 受信エラー通知
//					final String TAG = "onFailedToReceiveAd[nendLoad]";
//					String dbMsg = "";//////////////////
//					try{
//						dbMsg = "NendAdView = " + arg0;//////////////////
////			//			Toast.makeText(getApplicationContext(), "onFailedToReceiveAd", Toast.LENGTH_LONG).show();
//						nenvNow = false;
//						nend_aria.setVisibility(View.GONE);
//						Log.i(TAG,dbMsg);
//						if(! adMobNow){
//							adMobLoad();													//Google AdMobの広告設定
//						}
//					} catch (Exception e) {
//						Log.e(TAG,dbMsg+"で"+e);
//					}
//				}
//
//				@Override
//				public void onReceiveAd(NendAdView arg0) {	// 受信成功通知
//					final String TAG = "onReceiveAd[nendLoad]";
//					String dbMsg = "";//////////////////
//					try{
//						dbMsg = "NendAdView = " + arg0;//////////////////
//			//			Toast.makeText(getApplicationContext(), "onReceiveAd", Toast.LENGTH_LONG).show();
//						dbMsg = dbMsg + ",dPadAri = " + dPadAri;//////////////////
//						if(dPadAri){
//							nendKeys();		//nebdの広告用のキー設定
//						}
//			//			Log.i(TAG,dbMsg);
//					} catch (Exception e) {
//						Log.e(TAG,dbMsg+"で"+e);
//					}
//
//				}
//			});
//
//			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//
////			nend_aria.setVisibility(View.VISIBLE);
////			nend_aria.addView(nendAdView, params);
////			nendAdView.loadAd();
//			nenvNow = true;
//
//			Log.i(TAG,dbMsg);
//	//		syoliSentaku( IppatuMainTF );										//Viewの初期選択		kotae_tf
//		} catch (Exception e) {
//			Log.e(TAG,dbMsg+"で"+e);
//		}
//	}

	private AdView mAdView;					//Google広告表示エリア
	private AdRequest adRequest;			// 一般的なリクエストを行う
	boolean adMobNow;
	public View adViewC;
//	@SuppressLint ("NewApi")
	public void adMobLoad(){		//Google AdMobの広告設定
		final String TAG = "adMobLoad[AtarekunnActivity]";
		String dbMsg = "";//////////////////
		try{
			dbMsg = "mAdView=" + mAdView;//	https://developers.google.com/mobile-ads-sdk/docs/admob/android/quick-start#faq
			nendAdView.setVisibility(View.GONE);						//nebdを消して
			mAdView.setVisibility(View.VISIBLE);						//adを表示
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
							if(dPadAri){
								adMobKeys();		//Google AdMobの広告用のキー設定
							}
						}
						dbMsg = dbMsg+",getChildAt=" + adViewC;
						dbMsg = dbMsg+",ClassName=" + mAdView.getMediationAdapterClassName();		//null
						//errer発生	getTransitionName	getOutlineProvider	getOverlay
		//				Log.i(TAG,dbMsg);
					} catch (Exception e) {
						Log.e(TAG,dbMsg+"で"+e);
					}
				}
		//		@SuppressLint("NewApi")
		//		@TargetApi (Build.VERSION_CODES.JELLY_BEAN_MR2)
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
							mAdView.setVisibility(View.GONE);
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
//表示されたバナーに "Adview missing required XML attribute "adsize"
	//	Log.i(TAG,dbMsg);
////Ads: JS: Uncaught ReferenceError: AFMA_ReceiveMessage is not defined (:1)
//app.jsにvar TiAdmob = require('ti.admob'); を付ける?

        } catch (Exception e) {
			Log.e(TAG,dbMsg+"で"+e);
		}
	}
//http://pentan.info/android/app/multi_thread.html
	//http://outcesticide.hatenablog.com/entry/using_runOnUiThread
	public void adMobKeys(){		//Google AdMobの広告用のキー設定
		final String TAG = "adMobKeys[AtarekunnActivity]";
		String dbMsg = "";//////////////////
		try{
			dbMsg = "kujisyuSP" + kujisyuSP ;
			kujisyuSP.setNextFocusUpId(adViewC.getId());			//くじの種類スピナー
			dbMsg = dbMsg + "repetSp" + repetSp ;
			repetSp.setNextFocusUpId(adViewC.getId());				//繰り返し選択スピナー
			dbMsg = dbMsg + "adViewC" + adViewC ;
		//	adViewC.setNextFocusUpId(webFtagnemt.getId());		// Webビュー
			adViewC.setNextFocusDownId(kujisyuSP.getId());		// Webビュー
			Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"で"+e);
		}
	}

//	public void nendKeys(){		//nebdの広告用のキー設定
//		final String TAG = "adMobLoad[AtarekunnActivity]";
//		String dbMsg = "";//////////////////
//		try{
//			dbMsg = "kujisyuSP" + kujisyuSP ;
//			kujisyuSP.setNextFocusUpId(nendAdView.getId());			//くじの種類スピナー
//			dbMsg = dbMsg + ",repetSp" + repetSp ;
//			repetSp.setNextFocusUpId(nendAdView.getId());				//繰り返し選択スピナー
//			dbMsg = dbMsg + ",nendAdView=" + nendAdView ;
//			nendAdView.setNextFocusUpId(frag_aria.getId());		// Webビュー
//			nendAdView.setNextFocusDownId(kujisyuSP.getId());		// Webビュー
//			Log.i(TAG,dbMsg);
//		} catch (Exception e) {
//			Log.e(TAG,dbMsg+"で"+e);
//		}
//	}
//言語を切り替えるとクラッシュ/Ads: JS: Uncaught ReferenceError: AFMA_ReceiveMessage is not defined (:1
	//app.jsに	var TiAdmob = require('ti.admob');	を付けると大丈夫らしい。    http://se-suganuma.blogspot.jp/2016/05/titaniumlistviewadmob.html

	private void checkServiceAvailable() {
	final String TAG = "adMobLoad[AtarekunnActivity]";
	String dbMsg = "";//////////////////
	try{
	int iRes = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);			// Google Play Servicesを利用可能か
	 dbMsg = "iRes=" + iRes;//////////////////
		if (iRes == ConnectionResult.SUCCESS) {		// 利用可能な場合
		}else if (GooglePlayServicesUtil.isUserRecoverableError(iRes)) {			// ユーザが対応可能なエラー
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(iRes, this, 0);
			if (dialog != null) {
				GooglePlayServicesUtil.getErrorDialog(iRes, this, 1, new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						finish();
					}
				}).show();
			}
		} else {			// ユーザでは対応不可
			Toast.makeText(this, "Google Play Services is not available.", Toast.LENGTH_LONG).show();
			finish();
		}
		Log.i(TAG,dbMsg);
	} catch (Exception e) {
		Log.e(TAG,"エラー発生；"+e);
	}
	//http://seesaawiki.jp/w/moonlight_aska/d/Google%20Play%20Services%A4%AC%CD%AD%B8%FA%A4%AB%B3%CE%C7%A7%A4%B9%A4%EB
//	http://dev.classmethod.jp/smartphone/android/android-google-play-services-2/
 }

	public void setMaxSP(int sita , int ue){		//maxValSPのアイテム設定
		final String TAG = "setMaxSP";
		String dbMsg = "";//////////////////
		try{
			String[] listEnt = new String[(ue-sita+1)];
			for(int i=0 ;i< listEnt.length; i++ ){
				listEnt[i]= String.valueOf(sita+i);
			}
			dbMsg = listEnt[0] +"～"+ listEnt[listEnt.length-1];//////////////////
	//		Log.i(TAG,dbMsg);
			 ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listEnt);
			 arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			maxValSP.setAdapter(arrayAdapter);								// アダプターを設定します
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"で"+e);
		}
	}

	public void showField(){				//指定した枠数を示する
//		try{
//			dbMsg ="kujiSyurui="+kujiSyurui+",val_val="+val_val;////////////////////////////////////////////////////////////////////////////
//
//		//	dbMsg ="INVISIBLE;4="+String.valueOf(f4TF.getVisibility())+",5="+String.valueOf(f5TF.getVisibility())+",6="+String.valueOf(f6TF.getVisibility());////////////////////////////////////////////////////////////////////////////
//					//Log.d("showField",dbMsg);
//			if(subMaxVal == 0){			//別枠の最大値が設定されていなければ
//
//				f6TF.setVisibility(View.INVISIBLE);	 					//6桁目の表示枠
//				S6TF.setVisibility(View.INVISIBLE);	 					//累積6桁目の表示枠
//			}
//		} catch (Exception e) {
//			Log.e("showField",dbMsg+"で"+e);
//		}

	}

	public void reWriteAllVal(){ //変数全設定	★wriAllPrifに続けてreadPrifを呼び出しても更新がされていない
		final String TAG = "reWriteAllVal";
		String dbMsg = "";//////////////////
		try{
			dbMsg = "くじの種類"+kujiSyurui +"[" + randumStart_val + "～"+randumEnd_val + "]"+val_val +"個";////////////////////////////////////////////////////////////////////////////
	//		Log.d("reWriteAllVal",dbMsg);
			if(randumEnd_val==0){			//乱数の終了値
				randumEnd_val=43;
			}
			if(val_val==0){					//乱数の個数
				val_val=6;
			}
			if(kurikaesi_val==0){			//繰り返し判定数
				kurikaesi_val=100;
			}
			if(shyougouURL==null){			//照合web
				shyougouURL="http://www.takarakujinet.co.jp/loto6/index2.html";
			}
			if(kujiSyurui==null || kujiSyurui.equals("")){			//くじの種類
				kujiSyurui=getResources().getString(R.string.loto6_name);;
			}

			if( ! riuseki ){											//累積表示しない
				ruisekiLL.setVisibility(View.GONE);
				lank_sv.setVisibility(View.GONE);						//出現状況表示のスクロールビュー
			}else{
				ruisekiLL.setVisibility(View.VISIBLE);
				lank_sv.setVisibility(View.VISIBLE);						//出現状況表示のスクロールビュー
				ruisekiSuuTF.setText(String.valueOf(kurikaesi_val));						//繰り返し設定の表示
				tCount.setText(String.valueOf(kurikaesi_val));								//累積最大値
				aCount.setText(String.valueOf(0));											//現在のカウント
				String rString= getResources().getString(R.string.main_tf_syoki);						//">Atare君は「予想」なんてできません。
				rString=rString+getResources().getString(R.string.main_tf_syoki1)+kujiSyurui+"\n";							//"くじの種類は";
				rString=rString+getResources().getString(R.string.randumStart_val_titol) +" = "+ randumStart_val+"\n";		//"開始値
				rString=rString+getResources().getString(R.string.randumEnd_val_titol) +" = "+ randumEnd_val+"\n";			//終了値
				rString=rString+getResources().getString(R.string.val_val_titol) +" = "+val_val+"\n";						//乱数の個数
				rString=rString+getResources().getString(R.string.kurikaesi_val_titol) +" = "+kurikaesi_val+"\n";		//繰り返し
				rString=rString+getResources().getString(R.string.shyougouURL_titol) +" = "+ shyougouURL+"\n";			//参照webページ
				rString=rString+getResources().getString(R.string.prefUseDlogTitol) +" = "+ prefUseDlog+"\n";			//プログレスダイアログの表示
				lank1data.setText(rString);
			}
			setTitolComent(); 																//ウィンドウタイトルの書き換え
			if(kujiSyurui.equals(getResources().getString(R.string.mar_six_name))){
				kahenHyoujiLL.setVisibility(View.VISIBLE);	//表示個数設定部のリニアレイアウト
		//		maxValSP.setVisibility(View.VISIBLE);	//六合彩用の可変個数スピナー
			}else{
				kahenHyoujiLL.setVisibility(View.GONE);	//表示個数設定部のリニアレイアウト
//				maxValSP.setVisibility(View.GONE);	//六合彩用の可変個数スピナー
			}
			dbMsg =  dbMsg + randumStart_val  + "～"+randumEnd_val + "を"+val_val;////////////////////////////////////////////////////////////////////////////
			String wStr ="";
			for(int i =0 ; i < val_val ; i++){
				if(wStr.length() == 0){
					wStr = String.valueOf(randumEnd_val-val_val+1);
				}else {
					wStr = wStr + kugiri + String.valueOf(randumEnd_val -val_val +i +1);
				}
			}
			IppatuMainTF.setHint(wStr);					//一発表示のメイン
			wStr ="";
			for(int i =0 ; i < SPNval_val ; i++){
				if(wStr.length() == 0){
					wStr = String.valueOf(SPNumEnd_val-SPNval_val+1);
				}else {
					wStr = wStr + kugiri + String.valueOf(SPNumEnd_val -SPNval_val +i+1);
				}
			}
			if(wStr.length() > 0){
				dbMsg =  dbMsg +"と"+ SPNStart_val  + "～"+SPNumEnd_val + "を"+SPNval_val + ";表示"+wStr;////////////////////////////////////////////////////////////////////////////
				IppatuSubTF.setVisibility(View.VISIBLE);		//一発表示のサブ
				IppatuSubTF.setHint(wStr);
			}else{
				IppatuSubTF.setVisibility(View.GONE);		//一発表示のサブ
			}
			dbMsg = dbMsg +",照合URL="+shyougouURL ;////////////////////////////////////////////////////////////////////////////
			webView.loadUrl (shyougouURL);  //		webFtagnemt.updateUrl(shyougouURL , w_hyouji_bairitu);
	//		Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"で"+e);
		}
	}

	public void setTitolComent(){ 				//ウィンドウタイトルとボタンの書き換え
		final String TAG = "setTitolComent[AtarekunnActivity]";
		String dbMsg = "";//////////////////
		try{
			String tString=kujiSyurui + " ; " + randumStart_val + ">>>"+randumEnd_val + " * "+val_val ;
			if(SPNval_val>0){
				tString = tString +" ("+SPNStart_val + ">>>"+SPNumEnd_val + " : "+SPNval_val  +")";
			}
			if(honJuufuku){		//	public static boolean  =true;		//本番号の重複
				tString = tString +getResources().getString(R.string.kujiset_jyuuhuku);
			}
			if(repSyurui.equals(getResources().getString(R.string.gr2_name))){							//繰り返し
				tString = tString +" ["+getResources().getString(R.string.comon_ruiseki) + kurikaesi_val + "]";
			}
			dbMsg = dbMsg + tString;////////////////////////////////////////////////////////////////////////////
			setTitle(tString);

			if( AtarekunnActivity.this.repSyurui.equals(getResources().getString(R.string.menu_item_sousa_ippatu)) ){
//				lank1data.setText(getResources().getString(R.string.main_tf_syoki));															//出現状況表示
				IppatuMainTF.setText(getResources().getString(R.string.gr1_name));		//乱数一発！速攻表示		slotBT
			}else{
				dbMsg= dbMsg + ",累積表示=" + riuseki;/////////////////////////////////////
				IppatuMainTF.setText(getResources().getString(R.string.gr2_name));		//乱数累積
			}
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"で"+e);
		}
	}

	public void readPrif(){		//プリファレンスの読込み
		final String TAG = "readPrif[AtarekunnActivity]";
		String dbMsg = "";//////////////////
		try{
			sharedPref = PreferenceManager.getDefaultSharedPreferences(this);					//this.getSharedPreferences(this, MODE_PRIVATE);		//
			Map<String, ?> keys = sharedPref.getAll();
			dbMsg = "読み込み開始"+keys.size()+"項目;sharedPref="+sharedPref;////////////////////////////////////////////////////////////////////////////
			if (keys.size() == 0) {			//初回起動時は			//初回起動時は
				myEditor = sharedPref.edit();
				myEditor.putInt("pref_kurikaesi",100);				//繰り返し判定数		putString
				myEditor.putBoolean("prefUseDlog_ch", true);			//ダイアログの使用/未使用
				myEditor.putBoolean("dPadAri", dPadAri);			//ダイヤルキー
				boolean kakikomi =myEditor.commit();
				dbMsg = dbMsg+",書込み="+kakikomi;//////////////////
				keys = sharedPref.getAll();
				dbMsg = "読み込み開始"+keys.size()+"項目;sharedPref="+sharedPref;////////////////////////////////////////////////////////////////////////////
			}
			int i=0;
			for (String key : keys.keySet()) {
				i++;
				dbMsg = dbMsg+"\n"+ i+"/"+keys.size()+")　"+key+"は" + keys.get(key);///////////////+","+(keys.get(key) instanceof String)+",instanceof Boolean="+(keys.get(key) instanceof Boolean);////////////////////////////////////////////////////////////////////////////
				try{
					if(key.equals("prefUseDlog_ch")){
						prefUseDlog=Boolean.valueOf(keys.get(key).toString());						//ダイアログの使用/未使用	true	false
						dbMsg = dbMsg+ ",ダイアログの使用＝"+prefUseDlog;///////////////+","+(keys.get(key) instanceof String)+",instanceof Boolean="+(keys.get(key) instanceof Boolean);////////////////////////////////////////////////////////////////////////////
					}else if(key.equals("pref_kurikaesi")){
						 Object rStr = keys.get(key);
						if(rStr == null){
							rStr ="100";
						}
						kurikaesi_val=Integer.valueOf(String.valueOf(keys.get(key)));		//繰り返し判定数
			//			kurikaesi_val=Integer.valueOf(keys.get(key).toString());		//繰り返し判定数
						dbMsg = dbMsg+ ",繰り返し＝"+kurikaesi_val;///////////////+","+(keys.get(key) instanceof String)+",instanceof Boolean="+(keys.get(key) instanceof Boolean);////////////////////////////////////////////////////////////////////////////
					}else if(key.equals("dPadAri")){
						dPadAri=Boolean.valueOf(keys.get(key).toString());						//ダイアログの使用/未使用	true	false
						dbMsg = dbMsg+ ",ダイヤルキーは"+dPadAri;///////////////+","+(keys.get(key) instanceof String)+",instanceof Boolean="+(keys.get(key) instanceof Boolean);////////////////////////////////////////////////////////////////////////////
					}
				} catch (Exception e) {
					Log.e(TAG,dbMsg+"；"+e);
				}
			}			//	for (String key : keys.keySet()) {
			dbMsg = dbMsg + randumStart_val + "～"+randumEnd_val + "を"+val_val +"個"+"；くじの種類"+kujiSyurui  + ",繰り返し判定数"+kurikaesi_val + ",照合URL"+shyougouURL;////////////////////////////////////////////////////////////////////////////
	//		Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"；"+e);
		}
	}

	public void wriAllPrif(){		//プリファレンス全項目書込み
		final String TAG = "wriAllPrif";
		String dbMsg = "";//////////////////
		try{
			delPrif();		//プリファレンスの内容削除
			dbMsg = kujiSyurui ;
			myEditor.putString("kujiSyurui",kujiSyurui);					//くじの種類
			dbMsg = dbMsg+"["+randumStart_val ;
			myEditor.putInt("randumStart_val",randumStart_val);				//乱数の開始値
			dbMsg = dbMsg+"～"+randumEnd_val ;
			myEditor.putInt("randumEnd_val",randumEnd_val);					//乱数の終了値
			dbMsg = dbMsg+"]"+val_val ;
			myEditor.putInt("val_val",val_val);								//乱数の個数
			dbMsg = dbMsg+"回;照合web="+shyougouURL;////////////////////////////////////////////////////////////////////////////
			myEditor.putString("shyougouURL",shyougouURL);					//照合web
			dbMsg = dbMsg+"繰り返し="+kurikaesi_val;////////////////////////////////////////////////////////////////////////////
			myEditor.putInt("pref_kurikaesi",kurikaesi_val);					//繰り返し判定数
			dbMsg = dbMsg+"特番["+SPNStart_val;////////////////////////////////////////////////////////////////////////////
			myEditor.putInt("SPNStart_val",SPNStart_val);				//特番の開始値
			dbMsg = dbMsg+"～"+SPNumEnd_val;////////////////////////////////////////////////////////////////////////////
			myEditor.putInt("SPNumEnd_val",SPNumEnd_val);				//特番の終了値
			dbMsg = dbMsg+"]"+SPNval_val;////////////////////////////////////////////////////////////////////////////
			myEditor.putInt("SPNval_val",SPNval_val);					//特番の数
			dbMsg = dbMsg+","+repSyurui;////////////////////////////////////////////////////////////////////////////
			myEditor.putString("repSyurui",repSyurui);						//一発繰り返し区分
			dbMsg = dbMsg+"個,ダイアログの使用="+prefUseDlog;////////////////////////////////////////////////////////////////////////////
			myEditor.putBoolean("prefUseDlog_ch", prefUseDlog);			//ダイアログの使用/未使用	true	false
	//		Log.i(TAG,dbMsg);
			myEditor.commit();
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"；"+e);
		}
	}
//
	public void delPrif(){		//プリファレンスの内容削除
	//	sharedPref = getSharedPreferences(getResources().getString(R.string.pref_f_name),MODE_PRIVATE);		//	getSharedPreferences(prefFname,MODE_PRIVATE);
		myEditor = sharedPref.edit();

		final String TAG = "delPrif";
		String dbMsg = "";//////////////////
		try{
			Map<String, ?> keys = sharedPref.getAll();
			int i=0;
			if (keys.size() > 0) {
				for (String key : keys.keySet()) {
					i++;
					dbMsg = i+"/"+keys.size()+")　"+key+"　の値は　"+(keys.get(key)).toString();////////////////////////////////////////////////////////////////////////////
					myEditor.remove(key);
					//Log.d("delPrif",dbMsg);
				}
	//			PreferenceManager.setDefaultValues(this, R.xml.prefes, true);		//デフォルト値を書き出す
				myEditor.commit();
			}
		} catch (Exception e) {
			Log.e(TAG,"エラー発生；"+e);
		}
	}

//	public int kurikaesiCount=0;				//やり直し総数

	public int[] setNRNum(int StVal,int endVal,int vCount){				//乱数を指定した個数、重複を無くしてintArrayに格納してソート
		int[] retArray=null;
		final String TAG = "setNRNum";
		String dbMsg = "";//////////////////
		try{
			dbMsg=StVal+"～"+endVal+"を"+vCount+"個 ";///////////////////////////
			dbMsg= dbMsg +" ,honJuufuku= "+ honJuufuku;///////////////////////////
			String retStr = "";
			int addCount = 0;
			retArray =new int[vCount];	//objUser.makeIntArray(vCount);		//	intArray = new int[val_val];							//一発表示のデータ乱
			do{
				int tInt=rNum(StVal,endVal);
				dbMsg= dbMsg +" , "+ tInt;///////////////////////////
				if(retStr.length() == 0){
					retStr = String.valueOf(tInt);
					retArray[0]= tInt;
				}else{
					if(honJuufuku){			//	public static boolean  =true;	//本番合の重複
						retStr = retStr + kugiri + String.valueOf(tInt);
						retArray[addCount]= tInt;
						addCount ++ ;
					}else{
						if(isInListInt(retArray, tInt)){
						}else{
							retStr = retStr + kugiri + String.valueOf(tInt);
							retArray[addCount]= tInt;
							addCount ++ ;
						}
					}
				}
			}while(addCount < vCount);
			java.util. Arrays.sort (retArray);											//ソート関数
			dbMsg= dbMsg +retArray[0] +"～"+ retArray[addCount -1] +";"+ retArray.length +"個";///////////////////////////
	//		Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"；"+e);
		}
		return retArray;
	}

	public int[] setSubNRNum(int StVal,int endVal,int vCount , int[] mainGr){				//乱数を指定した個数、重複を無くしてintArrayに格納してソート
		int[] retArray= new int[vCount];
		final String TAG = "setSubNRNum";
		String dbMsg = "";//////////////////
		try{
			int addCount = 0;
			dbMsg=StVal+"～"+endVal+"を"+vCount+"個 , " + mainGr[0] +"～"+ mainGr[mainGr.length-1];///////////////////////////
			retArray =new int[vCount];	//objUser.makeIntArray(vCount);		//	intArray = new int[val_val];							//一発表示のデータ乱
			String retStr = "";
			do{
				int tInt=rNum(StVal,endVal);
				boolean kakikomi =true;
				if(juufuku){							//特番と本番合との重複が許可され
					if(isInListInt(retArray , tInt) ){	//特番の配列に重複があれば
						kakikomi =false;				//書き込まない
					}
				}else{
					if(isInListInt(mainGr , tInt) ){		//渡された数字が既にリストに登録されていたらtrue)
						kakikomi =false;
					}else if(isInListInt(retArray, tInt)){	//本数字に重複がなくても特番の配列に重複があれば
						kakikomi =false;				//書き込まない
					}
				}

				if(kakikomi){		//判定がすべて通れば書き込み処理
					if(isInListInt(retArray, tInt)){
					}else{
						if(retStr.length() == 0){
							retStr = String.valueOf(tInt);
							retArray[0]= tInt;
						}else{
							retStr = retStr + kugiri + String.valueOf(tInt);
							retArray[addCount]= tInt;
							addCount ++ ;
						}
					}
				}
			}while(addCount < vCount);
			java.util. Arrays.sort (retArray);											//ソート関数
			dbMsg=retArray[0] +"～"+ retArray[addCount-1] +";"+ retArray.length +"個";///////////////////////////
	//		Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"；"+e);
		}
		return retArray;
	}

	public int rNum(int kagen, int jyougenn){				//1～指定範囲の乱数を発生させる
		final String TAG = "rNum";
		int retInt =0;
		String dbMsg = "開始";//////////////////
		try{
			dbMsg = kagen +"～"+ jyougenn ;//////////////////
			float retFl =(float) (Math.floor(Math.random() * (jyougenn - kagen + 1)) + kagen);
			dbMsg = dbMsg +">>>"+ retFl ;//////////////////
			retInt = (int) (retFl+0.5);
			dbMsg = dbMsg +">>>"+ retInt ;//////////////////
//			Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"；"+e);
		}
		return retInt;
	}

	public boolean dousuuKakuninn(int[] checArray,int StVal,int endVal,int vCount){				//同じ数値が無い事を確認する
		boolean retBool=false;
		final String TAG = "dousuuKakuninn";
		String dbMsg = "";//////////////////
		try{

			dbMsg=checArray[0]+",";			//dbMsg=objUser.getIntArray(0)+",";
			if(vCount>=2){
				dbMsg=dbMsg+checArray[1];///////////////////////////
				if(checArray[0] == checArray[1]){
//					Log.d("dousuuKakuninn",dbMsg);
					return retBool;
				}
			}
			if(vCount>=3){
				dbMsg=dbMsg+","+checArray[2];///////////////////////////
				if(checArray[1] == checArray[2]){
//					Log.d("dousuuKakuninn",dbMsg);
					return retBool;
				}
			}
			if(vCount>=4){
				dbMsg=dbMsg+","+checArray[3];///////////////////////////
				if(checArray[2] == checArray[3]){
//					Log.d("dousuuKakuninn",dbMsg);
					return retBool;
					}
			}
			if(vCount>=5){
				dbMsg=dbMsg+","+checArray[4];///////////////////////////
				if(checArray[3] == checArray[4]){
//					Log.d("dousuuKakuninn",dbMsg);
					return retBool;
					}
			}
			if(vCount>=6){
				dbMsg=dbMsg+","+checArray[5];///////////////////////////
				if(checArray[4] == checArray[5] ){
//					Log.d("dousuuKakuninn",dbMsg);
//					setNRNum(StVal, endVal, vCount);						//再び乱数を指定した個数、表示する
					return retBool;
				}else{
					retBool=true;
				}
			}
//			Log.d("dousuuKakuninn",dbMsg);
			retBool=true;
		} catch (Exception e) {
			Log.e(TAG,"エラー発生；"+e);
		}
		return retBool;
	}

	public void wriNum(int[] wrArray ,TextView kakikomuTF){				//一発表示
		final String TAG = "wriNum";
		String dbMsg = "";//////////////////
		try{
	//		int wrCount=wrArray.length;
			dbMsg=wrArray.length+"個"+wrArray[0];///////////////////////////

			String wStr =null;
			int i = 0;
			for(int wInt : wrArray){
				i++;
				dbMsg=i +"/"+ wrArray.length +":"+wInt;///////////////////////////
				if( wStr == null){
					wStr = String.valueOf(wInt);
				}else{
					wStr = wStr + kugiri +String.valueOf(wInt);
				}
				dbMsg=dbMsg+">>" + wStr;///////////////////////////
			}
			kakikomuTF.setText(wStr);	//一発表示のメイン
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"；"+e);
		}
	}

	public void ruiseki(){					//乱数の累積
		ruisekiMax = 0;				//出現回数最大値
		final String TAG = "ruiseki";
		String dbMsg = "";//////////////////
		try{
			//http://se-suganuma.blogspot.jp/2010/02/androidjava.html
			this.kurikaesiCount=0;
			int[] hyoujiArray=null;	//メイン配列
			int[] ruisekiArray=null;
			ruisekiArray = new int[randumEnd_val+1];				//出現回数累積値の配列は乱数の終了値+繰返し総数の記録領域
			dbMsg=ruisekiMax+"/"+kurikaesi_val+",randumEnd_val="+randumEnd_val+",val_val="+val_val;///////////////////////////
			if( ! riuseki ){											//累積表示しない
			}else{
				ruisekiSuuTF.setText(String.valueOf(kurikaesi_val));			 	//最終カウント
				aCount.setText(String.valueOf(0));
				tCount.setText(String.valueOf(kurikaesi_val));			 	//最終カウント
			}
			startTime = System.currentTimeMillis();		// 総集計開始時刻の取得

			dbMsg="kurikaesi_val="+kurikaesi_val;///////////////////////////
				pdTitol=kujiSyurui+";"+randumStart_val+"～"+randumEnd_val+"("+val_val+")";						//ダイアログタイトル
			pdMessage=res.getString(R.string.ruisekiProgMsg1) +kurikaesi_val +res.getString(R.string.ruisekiProgMsg2) ;						//その時のメッセージ	"回出るまで繰り返し"
			pdMaxVal=kurikaesi_val;					//その時のメインバーの最大値
			dbMsg="pdTitol="+pdTitol+",pdMessage="+pdMessage+",pdMaxVal="+pdMaxVal;///////////////////////////
			dbMsg=dbMsg+",ダイアログの使用="+riusekiCyuu;///////////////////////////
			Log.i(TAG,dbMsg);
			if(prefUseDlog){		//ダイアログの使用/未使用
				riusekiCyuu = true;
				dbMsg ="累積中" + riusekiCyuu;//////累積中；止めると異常終了///////
				new plogTask(this, this).execute(pdTitol,pdMessage,pdMaxVal,randumStart_val,randumEnd_val,val_val,ruisekiArray);		//new Object[]{}
			}else{
				for(int j=0;j<kurikaesi_val ; j++){
			//		while(ruisekiMax < kurikaesi_val){
						ruisekiArray=ruisekiBody(ruisekiArray);				//		間に合わせ			//乱数の累積のループ内
						ruisekiMax=ruisekiArray.length;
						dbMsg= j +"/"+ kurikaesi_val+"回目"+ruisekiArray[0]+"～"+ruisekiArray[ruisekiArray.length-1];//////////////////////////////////////////////////////////////////////////////////
						String arrayMoniter="";
						for(int i=1;i<ruisekiArray.length;i++){
							if(ruisekiMax<=ruisekiArray[i]){
								ruisekiMax=ruisekiArray[i];
							}
							arrayMoniter=arrayMoniter+(i)+"に"+ruisekiArray[i]+",";
						}
						dbMsg=dbMsg+ruisekiMax+"/"+kurikaesi_val+";;;"+arrayMoniter;//////////////////////////////////////////////////////////////////////////////////
						Log.i(TAG,dbMsg);
			//		}
				}
				onSuccessplogTask( ruisekiArray );
//				hyoujiArray=ruisekiHyouji(ruisekiArray);		//乱数の累積表示
			}
			//plogTask未使用のテストブロック//////////////////////////////////////////////
			//////////////////////////////////////////////plogTask未使用のテストブロック//
			dbMsg = dbMsg +"と"+ SPNStart_val + "～" + SPNumEnd_val +"を" + SPNval_val + "個";////////////////////////////////////////////////////////////////////////////
			Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"；"+e);
		}
	}
//http://smartphone-zine.com/dokuwiki/android/100_tips/progressdialog.thml	onCreateDialog;多分これがProgressDialogの正しい実装方法
	public int[] ruisekiBody(int[] ruisekiArray){					//乱数の累積のループ内
		final String TAG = "ruisekiBody";
		String dbMsg = "";//////////////////
		try{
			int[] ranArray=null;
			ranArray=new int[val_val];			//retArray=new int[ruisekiTBL.length];
			ranArray=setNRNum(randumStart_val,randumEnd_val,val_val);							//乱数を指定した個数、表示する
	//		Log.d(TAG , dbMsg);
				dbMsg = dbMsg +"と"+ SPNStart_val + "～" + SPNumEnd_val +"を" + SPNval_val + "個";//////////////////////////////
			dbMsg=ruisekiArray.length-1+"までの"+ranArray.length+"個の乱数"+ranArray[0]+"…"+ranArray[ranArray.length-1]+"の出現回数を加算  ";/////////////////////////////////////////////////////
	//		Log.i(TAG,dbMsg);
			for(int i=0;i<ranArray.length;i++){			//出現回数の累積をintArrayに格納		for(int i=0;i<ranArray.length;i++){
				dbMsg =dbMsg+"("+i+")";/////////////////////////////////////////
				int cyuusennBangou=ranArray[i];				//[0]を避けてカウンタ読み出しint cyuusennBangou=ranArray[i]-1;
				int val=ruisekiArray[cyuusennBangou]+1;
				dbMsg =dbMsg+cyuusennBangou+"に;"+val+" , ";/////////////////////////////////////////
				ruisekiArray[cyuusennBangou]=val;		//		ruisekiTBL[(intArray[i]-1)]++;
			}			//出現回数の累積をintArrayに格納
			ruisekiArray[0]=kurikaesiCount;				//繰返し総数を累積配列の先頭に格納
			dbMsg = dbMsg +ruisekiArray[0]+"回目";
	//		Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"；"+e);
		}
		return ruisekiArray;
	}

	public int[] ruisekiHyouji(int[] ruisekiArray){					//乱数の累積表示配列作成
		int[] hyoujiArray=null;	//表示用配列
		final String TAG = "ruisekiHyouji";
		String dbMsg = "";//////////////////
		try{
			dbMsg="開始；val_val="+val_val;/////////////////////////////////////////////////////
			//		jyunMaxArray(ruisekiTBL);	//大きい方からインデックスを返す
			maxVal=0;

			dbMsg="最大値を探す　";
			ruisekiMax=0;
			for(int i=1;i<ruisekiArray.length;i++){			//最大値を探す		//for(int i=0;i<ruisekiTBL.length;i++){
				if(ruisekiMax<= ruisekiArray[i]){					//if(ruisekiMax<= ruisekiTBL[i]){
					ruisekiMax=ruisekiArray[i];								//					ruisekiMax=objUser.getRuisekiTBL(i);								//uisekiMax=ruisekiTBL[i];
				}
				dbMsg=dbMsg+(i+1)+";"+ruisekiArray[i]+",";
			}
			Log.d("ruisekiHyouji","["+ruisekiMax+";"+kurikaesi_val+"]"+dbMsg);
			kurikaesiCount=ruisekiArray[0];				//繰返し総数を累積配列の先頭に格納
			dbMsg="["+ res.getString(R.string.kurikaesisousuu) +this.kurikaesiCount+ res.getString(R.string.kaicyuu) +"]";		//objUser.getkurikaesiCount()
			dArrayCount=0;
			int[] retArray=null;
			retArray=new int[ruisekiArray.length];			//retArray=new int[ruisekiTBL.length];
			hyoujiArray = new int[val_val];	//表示用配列
			int hyoujiArrayCount=0;
			rData=dbMsg+"\n";
			for(maxVal=ruisekiMax;maxVal>=0;maxVal--){		//出現回数が最大値から0になるまで減算
				String cyuusennBangou="";
				for(int i=1;i<ruisekiArray.length;i++){			//抽選番号をすべて読み取りfor(int i=0;i<randumEnd_val;i++){
					if(maxVal== ruisekiArray[i]){			//最大値が見つかれば			//maxVal<= dArray[i] && dArray[i]>0//if(maxVal== objUser.getRuisekiTBL(i)){	//maxVal<= dArray[i] && dArray[i]>0
//						if(dArrayCount>=randumEnd_val){
//							//Log.d("jyunMaxArray","["+maxVal+";"+ruisekiMax+"]"+dbMsg+"("+dArrayCount+")");
//							break;
//						}
						retArray[dArrayCount]=i;			//retArrayにその抽選番号を記入★j++;が無くても加算している
						cyuusennBangou =cyuusennBangou+retArray[dArrayCount]+",";////////////////////////////////////////////////////////////////////////////
						if(hyoujiArrayCount<hyoujiArray.length){
							dbMsg=dArrayCount+"位；"+(hyoujiArrayCount+1)+"枠に"+retArray[dArrayCount]+",";
							hyoujiArray[hyoujiArrayCount]=i;
							hyoujiArrayCount++;
						}
						dArrayCount++;						//順位加算
					}
				}
				if(SPNval_val>0){
					setSubNRNum(SPNStart_val , SPNumEnd_val , SPNval_val , hyoujiArray);		//特番の終了値
	//				wriNum(setSubNRNum(SPNStart_val , SPNumEnd_val , SPNval_val , hyoujiArray) , RuisekiSubTF);		//特番の終了値
				}
				if(! cyuusennBangou.equals("")){			//現在の出現回数に該当する抽選番号があれば
					rData=rData+dArrayCount+"; "+cyuusennBangou+"["+maxVal+"]\n";		//出現状況表示に追記
				}
			}	//出現回数が最大値から0になるまで減算
			////////////////////////////////////////////////////////////////////////////////////////////////////大きい方からインデックスを返す///
//			Log.d("ruisekiBody",dbMsg);
			java.util.Arrays.sort(hyoujiArray);		//昇順ソート		//hyoujiArray

			long endTime= System.currentTimeMillis()-startTime;		//所要時間syoyoujikan
			rData=rData+res.getString(R.string.syoyoujikan)+sdf_mss.format(endTime);
	//		Log.d("ruisekiHyouji",dbMsg+"所要時間　"+sdf_mss.format(endTime));
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"；"+e);
		}
		return hyoujiArray;
	}

	public void ruisekiWrite(int[] ruisekiArray ){					//乱数の累積表示
		final String TAG = "ruisekiWrite";
		String dbMsg = "";//////////////////
		try{
			dbMsg="最大出現回数は"+ruisekiMax+rData+"回、";
			dbMsg=dbMsg+ruisekiArray[0]+"～"+ruisekiArray[ruisekiArray.length-1]+"を"+ruisekiArray.length+"桁表示";
			Log.d("ruisekiWrite",dbMsg);
			String wStr =null;
			int i = 0;
			for(int wInt : ruisekiArray){
				i++;
				dbMsg=i +"/"+ ruisekiArray.length +":"+wInt;///////////////////////////
				if( wStr == null){
					wStr = String.valueOf(wInt);
				}else{
					wStr = wStr + kugiri +String.valueOf(wInt);
				}
				dbMsg=dbMsg+">>" + wStr;///////////////////////////
			}
			IppatuMainTF.setText(wStr);			//累積表示のメイン
			if( ! riuseki ){			//累積表示しない
				ruisekiLL.setVisibility(View.GONE);
				lank_sv.setVisibility(View.GONE);						//出現状況表示のスクロールビュー
			}else{
				lank1data.setText(rData);			 		//出現状況表示
				aCount.setText(String.valueOf(ruisekiMax));			 		//現在のカウント
			}

			if(SPNval_val>0){
				tokubanArray=null;
				tokubanArray=new int[SPNval_val];			//retArray=new int[ruisekiTBL.length];
				tokubanArray = setSubNRNum(SPNStart_val , SPNumEnd_val , SPNval_val , ruisekiArray);
				dbMsg =tokubanArray[0]+"～"+tokubanArray[tokubanArray.length-1];/////////////////////////////////////////
				Log.d(TAG , dbMsg);
				wStr =null;
				for(int wInt : tokubanArray){
					if( wStr == null){
						wStr = String.valueOf(wInt);
					}else{
						wStr = wStr + kugiri +String.valueOf(wInt);
					}
				}
				IppatuSubTF.setText(wStr);				//サブの累積表示
			}
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"；"+e);
		}
	}

	// @Override
	public void onSuccessplogTask(int[] retArry ) {	//成功した時に呼ばれるメソッド//
		final String TAG = "onSuccessplogTask";
		String dbMsg = "";///////////////
// /E/WindowManager: android.view.WindowLeaked: Activity com.hijiyama_koubou.atare_kun.Alart3BT has leaked window com.android.internal.policy.impl.PhoneWindow$DecorView{1ec0411e V.E..... R....... 0,0-1026,711} that was originally added here

        try{
			dbMsg=retArry.length+"個の配列を"+this.kurikaesiCount+"回繰り返し";
			riusekiCyuu = false;
			dbMsg ="累積中" + riusekiCyuu;//////累積中；止めると異常終了///////
			Log.i(TAG,dbMsg);
			int[] hyoujiArray=ruisekiHyouji(retArry);		//乱数の累積表示
			ruisekiWrite(hyoujiArray);		//メインの累積表示

		} catch (Exception e) {
			Log.e(TAG,dbMsg+"；"+e);
		}
	  }

	//  @Override
	  public void onFailedDownloadImage(int resId) {
	// エラーの内容をトーストに表示する
	Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
	  }

	public void cyuukiHyouji(){								//対象国外で警告表示
		final String TAG = "cyuukiHyouji";
		String dbMsg = "";//////////////////
		try{
			boolean hyouj = false;
			Locale kuni = Locale.getDefault ();
			dbMsg = "国=" + kuni + ",くじ=" + kujiSyurui ;//////////////////
			TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String pCountry = telManager.getSimCountryIso();			 // 取得
			dbMsg = dbMsg + "電キー=" + pCountry;								//////////////////
			dbMsg = dbMsg + "電話回線=" + pCountry + ",くじ=" + kujiSyurui ;//////////////////
		//			Log.i(TAG,dbMsg);
//http://memorva.jp/tool/app/cctld_country_code.php
			if( pCountry.equals("jp") ){
				if(kujiSyurui.equals(res.getString(R.string.loto7_name))
						||kujiSyurui.equals(res.getString(R.string.loto6_name))
						||kujiSyurui.equals(res.getString(R.string.mini_loto_name))		//ミニロト</string>
						||kujiSyurui.equals(res.getString(R.string.num3_name))			//ナンバーズ3</string>
						||kujiSyurui.equals(res.getString(R.string.num4_name))			//ナンバーズ4</string>
						){}else{
							hyouj = true;
		//					Log.i(TAG,dbMsg);
						}
			}else if( pCountry.equals("us")){
				if(kujiSyurui.equals(res.getString(R.string.power_ball_name))				//<string name="">Power Ball(米)</string>
						||kujiSyurui.equals(res.getString(R.string.mega_millions))		//<string name="">Mega Millions(米)</string>
						){}else{
							hyouj = true;
						}
			} else if(pCountry.equals("hk")
					|| pCountry.equals("cn")
					|| pCountry.equals("tw")
//					|| Locale.TRADITIONAL_CHINESE.equals(kuni)
//					|| Locale.SIMPLIFIED_CHINESE.equals(kuni)
					) {				// HK 	Hong Kong 	香港>>>cc 	China (中華人民共和国)
				if(kujiSyurui.equals(res.getString(R.string.mar_six_name))				//"">六合彩（香港;マークシックス）	</string>
						){}else{
							hyouj = true;
						}
			} else  if(pCountry.equals("fr")
					|| pCountry.equals("uk")
					|| pCountry.equals("es")					//スペイン Spanish (es_ES)
					|| pCountry.equals("ie")						//アイルランド
					|| pCountry.equals("pt")						//ポルトガル
					|| pCountry.equals("lu")		//ルクセンブルク
					|| pCountry.equals("ch")		//スイス
					|| pCountry.equals("be")						//ベルギー Dutch, Belgium (nl_BE)
					|| pCountry.equals("at")						//オーストリアGerman, Austria (de_AT)
					) {
				if(kujiSyurui.equals(res.getString(R.string.ruro_mllions_name))				//<string name="">Euro Millions</string>
						){}else{
							hyouj = true;
						}
			} else{
				if(Locale.JAPAN.equals(kuni)) {				// ja_JP
					if(kujiSyurui.equals(res.getString(R.string.loto7_name))
						||kujiSyurui.equals(res.getString(R.string.loto6_name))
						||kujiSyurui.equals(res.getString(R.string.mini_loto_name))		//ミニロト</string>
						||kujiSyurui.equals(res.getString(R.string.num3_name))			//ナンバーズ3</string>
						||kujiSyurui.equals(res.getString(R.string.num4_name))			//ナンバーズ4</string>
						){}else{
							hyouj = true;
		//					Log.i(TAG,dbMsg);
						}
					} else 	if(Locale.CHINA.equals(kuni)
							|| Locale.TAIWAN.equals(kuni)
							|| Locale.CHINESE.equals(kuni)
							|| Locale.TRADITIONAL_CHINESE.equals(kuni)
							|| Locale.SIMPLIFIED_CHINESE.equals(kuni)
//							|| kokumei.equals("hk")
							) {				// HK 	Hong Kong 	香港>>>cc 	China (中華人民共和国)
						if(kujiSyurui.equals(res.getString(R.string.mar_six_name))				//"">六合彩（香港;マークシックス）	</string>
								){}else{
									hyouj = true;
								}
					} else 	if(Locale.US.equals(kuni)) {
						if(kujiSyurui.equals(res.getString(R.string.power_ball_name))				//<string name="">Power Ball(米)</string>
								||kujiSyurui.equals(res.getString(R.string.mega_millions))		//<string name="">Mega Millions(米)</string>
								){}else{
									hyouj = true;
								}
					} else 	if(Locale.FRANCE.equals(kuni)				//フランス
							|| Locale.FRENCH.equals(kuni)
							|| Locale.UK.equals(kuni)					//イギリス
							|| Locale.ENGLISH.equals(kuni)
//							|| kokumei.equals("sp")						//スペイン
//							|| kokumei.equals("nik")				//北アイルランド
//							|| kokumei.equals("ie")				//・アイルランド
//							|| kokumei.equals("po")		//ポルトガル
//							|| kokumei.equals("sz")	//スイス
//							|| kokumei.equals("be")				////ベルギー
//							|| kokumei.equals("au")				//オーストリア
							) {		//ルクセンブルグ？
						if(kujiSyurui.equals(res.getString(R.string.ruro_mllions_name))				//<string name="">Euro Millions</string>
								){}else{
									hyouj = true;
								}
					}else{
						hyouj = true;
					}
			}
            if(hyouj){
				dTitol = res.getString(R.string.comon_tukamaru);			//ダイアログタイトル ; お願い
				dMsg = res.getString(R.string.kounyuu_cyuui);				//アラート文 ; 日本国内で海外の宝くじは購入できません。\n雰囲気だけお楽しみください。
				dPbtn = res.getString(R.string.comon_ok);	            	//ボタン1のキーフェイス ; OK
				dNtn = null;
	            dPRet = dr_cyuukiHyouji;
	            dNRet = 0;
				showDialog(1);

//	            AlertDialog.Builder builder = new AlertDialog.Builder(this);		// Use the Builder class for convenient dialog construction
//                builder.setTitle(res.getString(R.string.comon_tukamaru));	                   		//ダイアログタイトル ; お願い
//                builder.setMessage(res.getString(R.string.kounyuu_cyuui));	                        //アラート文 ; 日本国内で海外の宝くじは購入できません。\n雰囲気だけお楽しみください。
//                builder.setPositiveButton(res.getString(R.string.comon_ok), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {		//ボタン1のキーフェイス ; OK
//                        final String TAG = "setPositiveButton[webRecord]";
//                        String dbMsg= "開始;";/////////////////////////////////////
//                        try{
//                         }catch (Exception e) {
//                            Log.e(TAG,dbMsg + "で"+e.toString());
//                        }
//                    }
//                });
////                builder.setNegativeButton(getResources().getString(R.string.comon_cyuusi), new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog, int id) {	// User cancelled the dialog
////                        //☆記載の必要無し；クリックすればダイアログを閉じる「
////                    }
////                });
//	            //     builder.show();  ではリークするので
			}
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"で"+ e);
		}
	}


	public String nowUrl;
	public float nowScale;
	public void webRecord() {			//表示されているwebページの設定保存
		final String TAG = "webRecord[AtarekunnActivity]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			String dTitol = kujiSyurui + getResources().getString(R.string.menu_item_sonota_settei);
//			dbMsg= dbMsg + ",dTitol=" + dTitol;/////////////////////////////////////
			String dMsg = kujiSyurui + getResources().getString(R.string.wrd_msg);
		//	dbMsg= dbMsg + ",dMsg=" + dMsg;/////////////////////////////////////
//			DialogFragment newFragment = new WebRecord( dTitol ,dMsg , nowTitol , nowUrl , nowScale);
//			newFragment.show(getFragmentManager(), "test");
			LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View content = inflater.inflate(R.layout.web_record, null);

			TextView wr_titoled = (TextView) content.findViewById(R.id.wr_titoled);
			String nowTitol = webView.getTitle();           //String nowTitol = webFtagnemt.getTitle();
//			dbMsg= dbMsg + ",nowTitol=" + nowTitol;/////////////////////////////////////
			wr_titoled.setText(String.valueOf(nowTitol));

			TextView wr_urlet = (TextView) content.findViewById(R.id.wr_urlet);
			AtarekunnActivity.this.nowUrl = webView.getUrl();           // = webFtagnemt.getUrl();
			dbMsg= dbMsg + ",nowUrl=" + AtarekunnActivity.this.nowUrl;/////////////////////////////////////
			wr_urlet.setText(String.valueOf(nowUrl));

			TextView wr_scaled = (TextView) content.findViewById(R.id.wr_scaled);
			AtarekunnActivity.this.nowScale = webView.getScale();       // = webFtagnemt.getScale();
			dbMsg= dbMsg + ",nowScale=" + AtarekunnActivity.this.nowScale;/////////////////////////////////////
			wr_scaled.setText(String.valueOf(nowScale));

			String msg2 = getResources().getString(R.string.wrd_msg2) +";" + shyougouURL + "\n" +
							getResources().getString(R.string.wrd_scale) +";" + w_hyouji_bairitu;
			TextView wr_message_tf = (TextView) content.findViewById(R.id.wr_message_tf);
			wr_message_tf.setText(msg2);

//			dTitol = getResources().getString(R.string.comon_tukamaru);			//ダイアログタイトル ; お願い
//			dMsg = getResources().getString(R.string.kounyuu_cyuui);				//アラート文 ; 日本国内で海外の宝くじは購入できません。\n雰囲気だけお楽しみください。
			dPbtn = getResources().getString(R.string.comon_jikkou);	            	//ボタン1のキーフェイス ; OK
			dNtn = null;
			dPRet = dr_webRecord;
			dNRet = 0;
			showDialog(1);


//			AlertDialog.Builder builder = new AlertDialog.Builder(this);		// Use the Builder class for convenient dialog construction
//			builder.setView(content);
//			builder.setTitle(dTitol);
//			builder.setMessage(dMsg);
//			builder.setPositiveButton(getResources().getString(R.string.comon_jikkou), new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {	// User cancelled the dialog
//					final String TAG = "setPositiveButton[webRecord]";
//					String dbMsg= "開始;";/////////////////////////////////////
//					try{
//						webRecordCore();			//表示されているwebページの設定保存実行
//					}catch (Exception e) {
//						Log.e(TAG,dbMsg + "で"+e.toString());
//					}
//				}
//				});
//			builder.setNegativeButton(getResources().getString(R.string.comon_cyuusi), new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {	// User cancelled the dialog
//					//☆記載の必要無し；クリックすればダイアログを閉じる「
//				}
//				});
//			builder.show();
			Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

		public void webRecordCore() {			//表示されているwebページの設定保存実行
		final String TAG = "webRecordCore[AtarekunnActivity]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			dbStart();				//データベースの作成・オープン
			ContentValues cv = new ContentValues();
			String where = "_id = " + String.valueOf(kujiId) ;
			dbMsg= dbMsg + ",where= " + where;/////////////////////////////////////
			String[] selectionArgs = null;
			dbMsg= dbMsg + "Url=" + shyougouURL;/////////////////////////////////////
			dbMsg= dbMsg + ">>" + nowUrl;/////////////////////////////////////
			dbMsg= dbMsg + "[変化無し=" + nowUrl.equals(shyougouURL) + "]";/////////////////////////////////////
			if(! nowUrl.equals(shyougouURL)){	//照合URLnowUrl	//
				cv.put("SHOU_URL", nowUrl);
				dbMsg= dbMsg + ",cv= " + cv;/////////////////////////////////////
			}
			dbMsg= dbMsg + "、倍率=" + w_hyouji_bairitu;/////////////////////////////////////
			dbMsg= dbMsg + ">>" + nowScale;/////////////////////////////////////
			if(nowScale != w_hyouji_bairitu){	//照合URLnowUrl	//
				cv.put("WH_BAI", nowScale);
				dbMsg= dbMsg + ",cv= " + cv;/////////////////////////////////////
			}
			if( cv != null){
				kuji_db.update(kuji_table, cv, "_id = " + String.valueOf(kujiId) , null);
			}
			Log.i(TAG,dbMsg);
			kuji_db.close();
			Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

	public void webKensaku() {			//くじに関する情報のweb検索
		final String TAG = "webKensaku[AtarekunnActivity]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			String sarchUrl = sarchUrlOrg + "#q=" +kujiSyurui;
//			String nowUrl = "https://www.google.co.jp/\" target=\"_blank\";
			dbMsg= dbMsg + ",sarchUrl=" + sarchUrl;///////%E3%83%AD%E3%83%887//////////////////////////////
			webView.loadUrl (sarchUrl);        //webFtagnemt.updateUrl(sarchUrl , w_hyouji_bairitu);
			Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

	public String dTitol;
	public String dMsg;
	public String dPbtn;
	public String dNtn;
	public int dPRet;
	public int dNRet;
	public String[] dItems;
	public String r_kujiSyurui;
	public String r_randumStart_val;
	public String r_randumEnd_val;
	public String r_val_val;
	public boolean r_honJuufuku;		//本番号の重複
	public String r_SPNStart_val;
	public String r_SPNumEnd_val;
	public String r_SPNval_val;
	public boolean r_juufuku;		//本番号と特番の重複
	public String r_shyougouURL;
	public String[] cNames ={ "KUJISYRUI" , "START_VAL" ,"END_VAL" , "VAL_VAL" , "HON_JUFUKU" ,
								"SPN_START" , "SPN_END" , "SPN_VAL" , "JUFUKU" , "SHOU_URL" };
	public List<Map<String, Object>> kujiSL;							//変更リスト
	public Map<String, Object> kujiMap;				//計算履歴リスト用
	public TextView kujisyurui;
	public TextView start_val;
	public TextView end_val;
	public TextView val_valtf;
	public CheckBox honjyuuhuku;
	public TextView spn_start;
	public TextView spn_end;
	public TextView spn_val;
	public CheckBox toku_jyuuhuku;
	public TextView kujiurl;

	public void kujiRecord( String dTitol , String dMsg) {			//表示されているくじの設定保存
		final String TAG = "kujiRecord[AtarekunnActivity]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			dbMsg= dbMsg + ",dTitol=" + dTitol;/////////////////////////////////////
			dbMsg= dbMsg + ",dMsg=" + dMsg;/////////////////////////////////////

			dTitol = dTitol;
			dMsg = dMsg;
			dPbtn = getResources().getString(R.string.comon_jikkou);
			dNtn = getResources().getString(R.string.comon_cyuusi);
			dPRet = dr_kujiRecord;
			dNRet = 0;
			showDialog(1);



//			LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			View content = inflater.inflate(R.layout.kuji_record, null);
//
//			kujisyurui = (TextView) content.findViewById(R.id.kujisyurui);
//			dbMsg= dbMsg + ",kujiSyurui=" + kujiSyurui;
//			kujisyurui.setText(String.valueOf(kujiSyurui));
//
//			start_val = (TextView) content.findViewById(R.id.start_val);
//			dbMsg= dbMsg + ",乱数の開始値=" + randumStart_val;
//			start_val.setText(String.valueOf(randumStart_val));
//
//			end_val = (TextView) content.findViewById(R.id.end_val);
//			dbMsg= dbMsg + "～=" + randumEnd_val;
//			end_val.setText(String.valueOf(randumEnd_val));
//
//			val_valtf = (TextView) content.findViewById(R.id.val_val);
//			dbMsg= dbMsg + "×" + val_val;								//
//			val_valtf.setText(String.valueOf(val_val));
//
//			honjyuuhuku = (CheckBox) content.findViewById(R.id.honjyuuhuku);
//			dbMsg= dbMsg + ",乱数の重複=" + honJuufuku;
//			honjyuuhuku.setChecked(honJuufuku);
//			honjyuuhuku.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {		// チェックボックスがクリックされた時に呼び出されます
//					final String TAG = "kujiRecord[AtarekunnActivity]";
//					String dbMsg= "開始;";/////////////////////////////////////
//					try{
//						CheckBox checkBox = (CheckBox) v;
//						AtarekunnActivity.this.r_honJuufuku = checkBox.isChecked();		// チェックボックスのチェック状態を取得します
//						Log.i(TAG,dbMsg);
//					}catch (Exception e) {
//						Log.e(TAG,dbMsg + "で"+e.toString());
//					}
//				}
//			});
//
//			spn_start = (TextView) content.findViewById(R.id.spn_start);
//			dbMsg= dbMsg + ",特番の開始値=" + SPNStart_val;
//			spn_start.setText(String.valueOf(SPNStart_val));
//
//			spn_end = (TextView) content.findViewById(R.id.spn_end);
//			dbMsg= dbMsg + ",特番の終了値=" + SPNumEnd_val;
//			spn_end.setText(String.valueOf(SPNumEnd_val));
//
//			spn_val = (TextView) content.findViewById(R.id.spn_val);
//			dbMsg= dbMsg + ",特番の数=" + SPNval_val;
//			spn_val.setText(String.valueOf(SPNval_val));
//
//			toku_jyuuhuku = (CheckBox) content.findViewById(R.id.toku_jyuuhuku);
//			dbMsg= dbMsg + ",特番と本番合との重複=" + juufuku;
//			toku_jyuuhuku.setChecked(juufuku);
//			toku_jyuuhuku.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {		// チェックボックスがクリックされた時に呼び出されます
//					final String TAG = "kujiRecord[AtarekunnActivity]";
//					String dbMsg= "開始;";/////////////////////////////////////
//					try{
//						CheckBox checkBox = (CheckBox) v;
//						AtarekunnActivity.this.r_juufuku = checkBox.isChecked();		// チェックボックスのチェック状態を取得します
//						Log.i(TAG,dbMsg);
//					}catch (Exception e) {
//						Log.e(TAG,dbMsg + "で"+e.toString());
//					}
//				}
//			});
//
//			kujiurl = (TextView) content.findViewById(R.id.kujiurl);
//			dbMsg= dbMsg + ",照合URL=" + shyougouURL;
//			kujiurl.setText(String.valueOf(shyougouURL));
////			String msg2 = getResources().getString(R.string.wrd_msg2) +";" + shyougouURL + "\n" +
////							getResources().getString(R.string.wrd_scale) +";" + w_hyouji_bairitu;
////			TextView wr_message_tf = (TextView) content.findViewById(R.id.wr_message_tf);
////			wr_message_tf.setText(msg2);
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);		// Use the Builder class for convenient dialog construction
//			builder.setView(content);
//			builder.setTitle(dTitol);
//			builder.setMessage(dMsg);
//			builder.setPositiveButton(getResources().getString(R.string.comon_jikkou), new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {	// User cancelled the dialog
//					final String TAG = "setPositiveButton[webRecord]";
//					String dbMsg= "開始;";/////////////////////////////////////
//					try{
//						boolean sinki =false;
//						int hennkouNasi = 0;
//
//						kujiSL =  new ArrayList<Map<String, Object>>();				//変更リスト
//						dbMsg = dbMsg + ",kujiSL=" +  kujiSL;
//
//						r_kujiSyurui = String.valueOf(AtarekunnActivity.this.kujisyurui.getText());
//						if( ! kujiSyurui.equals(r_kujiSyurui)){
//							dbMsg= cNames[0]+"くじ名称 =" + kujiSyurui;
//							dbMsg= dbMsg + ">>" + r_kujiSyurui;
//							kujiMap = new HashMap<String, Object>();
//							kujiMap.put("koumokumei" , getResources().getString(R.string.kujiSyuruiTitol) );
//							kujiMap.put("cName" ,cNames[0] );
//							kujiMap.put("henkou" ,String.valueOf(r_kujiSyurui) );
//							kujiMap.put("moto" ,kujiSyurui );
//							kujiSL.add( kujiMap);		//kujiSL.size(),
//							sinki = true;
//							hennkouNasi++;
//						}
//
//						r_randumStart_val = String.valueOf(AtarekunnActivity.this.start_val.getText());
//						if(! r_randumStart_val.equals(String.valueOf(randumStart_val)) ){
//							dbMsg= dbMsg + ","+cNames[1]+ ",乱数の開始値=" + randumStart_val;
//							dbMsg= dbMsg + ">>" + r_randumStart_val;
//							kujiMap = new HashMap<String, Object>();
//							kujiMap.put("koumokumei" , getResources().getString(R.string.randumStart_val_titol) );
//							kujiMap.put("cName" ,cNames[1] );
//							kujiMap.put("henkou" ,String.valueOf(r_randumStart_val ));
//							kujiMap.put("moto" , String.valueOf(randumStart_val ));
//							kujiSL.add(kujiMap);
//							hennkouNasi++;
//						}
//
//						r_randumEnd_val = String.valueOf(AtarekunnActivity.this.end_val.getText());
//						if(! r_randumEnd_val.equals(String.valueOf(randumEnd_val)) ){
//							dbMsg= dbMsg+ "," + cNames[2]+ ",乱数の終了値=" + randumEnd_val;
//							dbMsg= dbMsg + ">>" + r_randumEnd_val;
//							kujiMap = new HashMap<String, Object>();
//							kujiMap.put("koumokumei" , getResources().getString(R.string.randumEnd_val_titol) );
//							kujiMap.put("cName" ,cNames[2] );
//							kujiMap.put("henkou" ,String.valueOf(r_randumEnd_val) );
//							kujiMap.put("moto" ,randumEnd_val );
//							kujiSL.add( kujiMap);
//							hennkouNasi++;
//						}
//
//						r_val_val = String.valueOf(AtarekunnActivity.this.val_valtf.getText());
//						if(! r_val_val.equals(String.valueOf(val_val)) ){
//							dbMsg= dbMsg+ "," + cNames[3]+  ",個数=" + val_val;								//
//							dbMsg= dbMsg + ">>" + r_val_val;
//							kujiMap = new HashMap<String, Object>();
//							kujiMap.put("koumokumei" , getResources().getString(R.string.val_val_titol) );
//							kujiMap.put("cName" ,cNames[3] );
//							kujiMap.put("henkou" ,String.valueOf(r_val_val) );
//							kujiMap.put("moto" ,val_val );
//							kujiSL.add( kujiMap);
//							hennkouNasi++;
//						}
//
//						if(AtarekunnActivity.this.honJuufuku != AtarekunnActivity.this.r_honJuufuku){
//							dbMsg= dbMsg + ","+ cNames[4]+  ",重複=" + honJuufuku;
//							dbMsg= dbMsg + ">>" + r_honJuufuku;
//							kujiMap = new HashMap<String, Object>();
//							kujiMap.put("koumokumei" , getResources().getString(R.string.kujiset_jyuuhuku) );
//							kujiMap.put("cName" ,cNames[4] );
//							kujiMap.put("henkou" ,String.valueOf(r_honJuufuku));
//							kujiMap.put("moto" ,honJuufuku );
//							kujiSL.add( kujiMap);
//							hennkouNasi++;
//						}
//
//						r_SPNStart_val = String.valueOf(AtarekunnActivity.this.spn_start.getText());
//						if(! r_SPNStart_val.equals(String.valueOf(SPNStart_val)) ){
//							dbMsg= dbMsg+ "," + cNames[5]+ ",特番の開始値=" + SPNStart_val;
//							dbMsg= dbMsg + ">>" + r_SPNStart_val;
//							kujiMap = new HashMap<String, Object>();
//							kujiMap.put("koumokumei" , getResources().getString(R.string.kujiset_tokubann) +
//														getResources().getString(R.string.randumStart_val_titol) );
//							kujiMap.put("cName" ,cNames[5] );
//							kujiMap.put("henkou" ,String.valueOf(r_SPNStart_val));
//							kujiMap.put("moto" ,SPNStart_val );
//							kujiSL.add( kujiMap);
//							hennkouNasi++;
//						}
//
//						r_SPNumEnd_val = String.valueOf(AtarekunnActivity.this.spn_end.getText());
//						if(! r_SPNumEnd_val.equals(String.valueOf(SPNumEnd_val)) ){
//							dbMsg= dbMsg+ "," + cNames[6]+ ",終了値=" + SPNumEnd_val;
//							dbMsg= dbMsg + ">>" + r_SPNumEnd_val;
//							kujiMap = new HashMap<String, Object>();
//							kujiMap.put("koumokumei" , getResources().getString(R.string.kujiset_tokubann) +
//														getResources().getString(R.string.randumEnd_val_titol) );
//							kujiMap.put("cName" ,cNames[6] );
//							kujiMap.put("henkou" ,String.valueOf(r_SPNumEnd_val));
//							kujiMap.put("moto" ,SPNumEnd_val );
//							kujiSL.add( kujiMap);
//							hennkouNasi++;
//						}
//
//						r_SPNval_val = String.valueOf(AtarekunnActivity.this.spn_val.getText());
//						if(! r_SPNval_val.equals(String.valueOf(SPNval_val)) ){
//							dbMsg= dbMsg+ "," + cNames[7]+  ",特番の数=" + SPNval_val;
//							dbMsg= dbMsg + ">>" + r_SPNval_val;
//							kujiMap = new HashMap<String, Object>();
//							kujiMap.put("koumokumei" , getResources().getString(R.string.kujiset_tokubann) +
//														getResources().getString(R.string.SPN_val_titol) );
//							kujiMap.put("cName" ,cNames[7] );
//							kujiMap.put("henkou" ,String.valueOf(r_SPNval_val));
//							kujiMap.put("moto" ,SPNval_val );
//							kujiSL.add( kujiMap);
//							hennkouNasi++;
//						}
//
//						if(AtarekunnActivity.this.juufuku != AtarekunnActivity.this.r_juufuku){
//							dbMsg= dbMsg+ "," + cNames[8]+ ",特番と本番合との重複=" + juufuku;
//							dbMsg= dbMsg + ">>" + r_juufuku;
//							kujiMap = new HashMap<String, Object>();
//							kujiMap.put("koumokumei" , getResources().getString(R.string.kujiset_tokubann) +
//														getResources().getString(R.string.kujiset_jyuuhuku) );
//							kujiMap.put("cName" ,cNames[8] );
//							kujiMap.put("henkou" ,String.valueOf(r_juufuku));
//							kujiMap.put("moto" ,juufuku );
//							kujiSL.add( kujiMap);
//							hennkouNasi++;
//						}
//
//						r_shyougouURL = String.valueOf(AtarekunnActivity.this.kujiurl.getText());
//						if( ! shyougouURL.equals(r_shyougouURL)){
//							dbMsg= dbMsg+ "," + cNames[9]+",照合URL=" + shyougouURL;
//							dbMsg= dbMsg + ">>" + r_shyougouURL;
//							kujiMap = new HashMap<String, Object>();
//							kujiMap.put("koumokumei" , getResources().getString(R.string.shyougouURL_titol) );
//							kujiMap.put("cName" ,cNames[9] );
//							kujiMap.put("henkou" ,String.valueOf(r_shyougouURL));
//							kujiMap.put("moto" ,shyougouURL );
//							kujiSL.add( kujiMap);
//							hennkouNasi++;
//						}
//						dbMsg= dbMsg + ">hennkouNasi=" + hennkouNasi;
//						dbMsg= dbMsg + ">kujiSL=" + kujiSL.size() + "件";
//						String dMsg = "";
//						for(int i=0 ;i< kujiSL.size(); i++ ){
//							Map<String, Object> kujiMap = kujiSL.get( i );
//							dMsg = dMsg+String.valueOf(kujiMap.get("koumokumei"));
//							if(sinki){
//								dMsg = dMsg+" = "+ String.valueOf(kujiMap.get("henkou")) + "\n";
//							}else{
//								dMsg = dMsg+";"+ String.valueOf(kujiMap.get("moto"));
//								dMsg = dMsg+">>"+ String.valueOf(kujiMap.get("henkou")) + "\n";
//							}
//						}
//						dbMsg= dbMsg + ">>=" +dMsg;
//						Log.i(TAG,dbMsg);
//
//						if( sinki ){	//名称を変えて新しいくじとして登録
//							String dTitol = r_kujiSyurui + getResources().getString(R.string.menu_item_sonota_settei);
//							dMsg = dMsg + r_kujiSyurui + getResources().getString(R.string.kujiset_tuikka_msg2);			//を新しいくじとして登録します。</string>
//							kujiRecTuika( dTitol , dMsg );			//新しいくじ
//						}else if( 0 < hennkouNasi ){
//							String dTitol = kujiSyurui + getResources().getString(R.string.comon_hennkou);
//							dMsg =dMsg + "\n" + kujiSL.size() + getResources().getString(R.string.comon_koumoku);
//							kujiRecKousin( dTitol, dMsg);			//表示されているくじの設定保存実行
//						}
//					}catch (Exception e) {
//						Log.e(TAG,dbMsg + "で"+e.toString());
//					}
//				}
//				});
//			builder.setNegativeButton(getResources().getString(R.string.comon_cyuusi), new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {	// User cancelled the dialog
//					//☆記載の必要無し；クリックすればダイアログを閉じる「
//				}
//				});
//			builder.show();
			Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

	public void kujiRecTuika( String dTitol , String dMsg ) {			//新しいくじ
		final String TAG = "kujiRecTuika[AtarekunnActivity]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			dbMsg= "くじ名称 =" + r_kujiSyurui+ ",乱数=" + r_randumStart_val+ "～" + r_randumEnd_val+ "*" + r_val_val+ "重複" + honJuufuku;
			dbMsg= dbMsg+",特番=" + r_SPNStart_val+ "～" + r_SPNumEnd_val+ "*" + r_SPNval_val+ "重複" + r_juufuku+ ",URL" + r_shyougouURL;

			dTitol = dTitol;
			dMsg = dMsg;
			dPbtn = getResources().getString(R.string.comon_jikkou);
			dNtn = getResources().getString(R.string.comon_cyuusi);
			dPRet = dr_kujiRecTuika;
			dNRet = 0;
			showDialog(1);


//			AlertDialog.Builder builder = new AlertDialog.Builder(this);		// Use the Builder class for convenient dialog construction
//			builder.setTitle(dTitol);
//			builder.setMessage(dMsg);
//			builder.setPositiveButton(getResources().getString(R.string.comon_jikkou), new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {	// User cancelled the dialog
//					final String TAG = "setPositiveButton[kujiRecTuika]";
//					String dbMsg= "開始;";/////////////////////////////////////
//					try{
//						String fn = AtarekunnActivity.this.getApplicationContext().getString(R.string.kuji_file);		//kuji.db
//						dbMsg = "fn= " + fn ;
//						kuji_table = getResources().getString(R.string.kuji_table);				//kuji_table</string>
//						dbMsg = dbMsg +  ",テーブル名=" + kuji_table;
//						kujiHelper = new KujiHelper(getApplicationContext() , fn);				//計算履歴トヘルパ
//						File dbF = getDatabasePath(fn);			//Environment.getExternalStorageDirectory().getPath();		new File(fn);		//cContext.
//						if(! kuji_db.isOpen()){
//							dbMsg = dbMsg + ">>delF=" + dbF.getPath();
//							kuji_db = kujiHelper.getWritableDatabase();		//アーティスト名のえリストファイルを書き込みモードで開く
//							dbMsg = dbMsg + ">isOpen>" + kuji_db.isOpen();		//03-28java.lang.IllegalArgumentException:  contains a path separator
//						}
//						dbMsg = dbMsg + ">getPageSize>" + kuji_db.getPageSize();		//03-28java.lang.IllegalArgumentException:  contains a path separator
//						stmt = kuji_db.compileStatement("insert into " + kuji_table +
//								"(KUJISYRUI,VAL_VAL,START_VAL,END_VAL,HON_JUFUKU,SPN_VAL,SPN_START,SPN_END,JUFUKU,SHOU_URL,WH_BAI) values (?,?,?,?,?,?,?,?,?,?,?);");
//						//くじの種類,乱数の個数,乱数の開始値,乱数の終了値,本番合の重複,特番の数,特番の開始値,特番の終了値,特番と本番合との重複,照合URL,表示倍率
//						dbMsg = dbMsg + ",stmt=" + stmt;
//						String s_honJuufuku= String.valueOf( AtarekunnActivity.this.r_honJuufuku);
//						String sjuufuku= String.valueOf( AtarekunnActivity.this.r_juufuku);
//
//
//
//						new ContentValues();
//						dbKakikomi( stmt , AtarekunnActivity.this.r_kujiSyurui ,AtarekunnActivity.this.r_val_val,
//								AtarekunnActivity.this.r_randumStart_val , AtarekunnActivity.this.r_randumEnd_val , s_honJuufuku ,
//								AtarekunnActivity.this.r_SPNval_val,AtarekunnActivity.this.r_SPNStart_val , AtarekunnActivity.this.r_SPNumEnd_val ,
//								sjuufuku ,AtarekunnActivity.this.r_shyougouURL ,  "1.0f" );
//						kuji_db.close();
//						kujimeiList=null;			//DB内のくじリストをクリアにしてkujiSentaku＞dbStartで再リストアップ
//						kujiSentaku( kujiSyurui);
//						kujiSentakuKekka( kujiSyurui);			//くじ種選択本体
//						Log.i(TAG,dbMsg);
//					}catch (Exception e) {
//						Log.e(TAG,dbMsg + "で"+e.toString());
//					}
//				}
//				});
//			builder.setNegativeButton(getResources().getString(R.string.comon_cyuusi), new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {	// User cancelled the dialog
//					//☆記載の必要無し；クリックすればダイアログを閉じる「
//				}
//				});
//			builder.show();
			Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

	public void kujiRecTuika_pb(){           //新しいくじ
		final String TAG = "kujiRecTuika_pb[AtarekunnActivity]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			String fn = AtarekunnActivity.this.getApplicationContext().getString(R.string.kuji_file);		//kuji.db
			dbMsg = "fn= " + fn ;
			String kuji_table = getResources().getString(R.string.kuji_table);				//kuji_table</string>
			dbMsg = dbMsg +  ",テーブル名=" + kuji_table;
			kujiHelper = new KujiHelper(getApplicationContext() , fn);				//計算履歴トヘルパ
			File dbF = getDatabasePath(fn);			//Environment.getExternalStorageDirectory().getPath();		new File(fn);		//cContext.
			if(! AtarekunnActivity.this.kuji_db.isOpen()){
				dbMsg = dbMsg + ">>delF=" + dbF.getPath();
				kuji_db = kujiHelper.getWritableDatabase();		//アーティスト名のえリストファイルを書き込みモードで開く
				dbMsg = dbMsg + ">isOpen>" + AtarekunnActivity.this.kuji_db.isOpen();		//03-28java.lang.IllegalArgumentException:  contains a path separator
			}
			dbMsg = dbMsg + ">getPageSize>" + AtarekunnActivity.this.kuji_db.getPageSize();		//03-28java.lang.IllegalArgumentException:  contains a path separator
			AtarekunnActivity.this.stmt = AtarekunnActivity.this.kuji_db.compileStatement("insert into " + kuji_table +
					"(KUJISYRUI,VAL_VAL,START_VAL,END_VAL,HON_JUFUKU,SPN_VAL,SPN_START,SPN_END,JUFUKU,SHOU_URL,WH_BAI) values (?,?,?,?,?,?,?,?,?,?,?);");
			//くじの種類,乱数の個数,乱数の開始値,乱数の終了値,本番合の重複,特番の数,特番の開始値,特番の終了値,特番と本番合との重複,照合URL,表示倍率
			dbMsg = dbMsg + ",stmt=" + AtarekunnActivity.this.stmt;
			String s_honJuufuku= String.valueOf( AtarekunnActivity.this.r_honJuufuku);
			String sjuufuku= String.valueOf( AtarekunnActivity.this.r_juufuku);

			new ContentValues();
			dbKakikomi( AtarekunnActivity.this.stmt , AtarekunnActivity.this.r_kujiSyurui ,AtarekunnActivity.this.r_val_val,
					AtarekunnActivity.this.r_randumStart_val , AtarekunnActivity.this.r_randumEnd_val , s_honJuufuku ,
					AtarekunnActivity.this.r_SPNval_val,AtarekunnActivity.this.r_SPNStart_val , AtarekunnActivity.this.r_SPNumEnd_val ,
					sjuufuku ,AtarekunnActivity.this.r_shyougouURL ,  "1.0f" );
			AtarekunnActivity.this.kuji_db.close();
			kujimeiList=null;			//DB内のくじリストをクリアにしてkujiSentaku＞dbStartで再リストアップ
			kujiSentaku( kujiSyurui);
			kujiSentakuKekka( kujiSyurui);			//くじ種選択本体
			Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

		public void kujiRecKousin( String dTitol , String dMsg ) {			//表示されているくじの設定保存実行
		final String TAG = "kujiRecKousin[AtarekunnActivity]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			dbMsg= "Titol=" + dTitol;/////////////////////////////////////
			dbMsg= dbMsg + "dMsg=" + dMsg;/////////////////////////////////////
			dbMsg= dbMsg + "[" + kujiSL + "件]";/////////////////////////////////////
			//			dTitol = getResources().getString(R.string.comon_tukamaru);			//ダイアログタイトル ; お願い
			//			dMsg = getResources().getString(R.string.kounyuu_cyuui);				//アラート文 ; 日本国内で海外の宝くじは購入できません。\n雰囲気だけお楽しみください。
			dPbtn = getResources().getString(R.string.comon_jikkou);	            	//ボタン1のキーフェイス ; OK
			dNtn = getResources().getString(R.string.comon_cyuusi);
			dPRet = dr_kujiRecKousin;
			dNRet = 0;
			showDialog(1);

//			AlertDialog.Builder builder = new AlertDialog.Builder(this);		// Use the Builder class for convenient dialog construction
//			builder.setTitle(dTitol);
//			builder.setMessage(dMsg);
//			Log.i(TAG,dbMsg);
//
//			builder.setPositiveButton(getResources().getString(R.string.comon_jikkou), new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {	// User cancelled the dialog
//					final String TAG = "setPositiveButton[kujiRecTuika]";
//					String dbMsg= "開始;";/////////////////////////////////////
//					try{
//						dbStart();				//データベースの作成・オープン
//						ContentValues cv = new ContentValues();
//						String where = "_id = " + String.valueOf(kujiId) ;
//						dbMsg= dbMsg + ",where= " + where;/////////////////////////////////////
//						String[] selectionArgs = null;
//						for(int i=0 ;i< kujiSL.size(); i++ ){
//							Map<String, Object> kujiMap = kujiSL.get( i );
//							dbMsg= i + ")" ;/////////////////////////////////////
//							String cName = String.valueOf(kujiMap.get("cName"));
//							dbMsg= dbMsg + cName ;/////////////////////////////////////
//							String henkou = String.valueOf(kujiMap.get("henkou"));
//							dbMsg= dbMsg + ">>" + henkou ;/////////////////////////////////////
//							cv.put(cName, henkou);
//							dbMsg= dbMsg + ",cv= " + cv;/////////////////////////////////////
//							Log.i(TAG,dbMsg);
//						}
//						if( cv != null){
//							kuji_db.update(kuji_table, cv, "_id = " + String.valueOf(kujiId) , null);
//						}
//						Log.i(TAG,dbMsg);
//						kuji_db.close();
//						kujiSentakuKekka( kujiSyurui);			//くじ種選択本体
//						Log.i(TAG,dbMsg);
//					}catch (Exception e) {
//						Log.e(TAG,dbMsg + "で"+e.toString());
//					}
//				}
//			});
//			builder.setNegativeButton(getResources().getString(R.string.comon_cyuusi), new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {	// User cancelled the dialog
//					//☆記載の必要無し；クリックすればダイアログを閉じる「
//				}
//				});
//			builder.show();
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

	public void kujiRecKousin_pb(){            //表示されているくじの設定保存実行
		final String TAG = "kujiRecKousin_pb[AtarekunnActivity]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			dbStart();				//データベースの作成・オープン
			ContentValues cv = new ContentValues();
			String where = "_id = " + String.valueOf(kujiId) ;
			dbMsg= dbMsg + ",where= " + where;/////////////////////////////////////
			String[] selectionArgs = null;
			for(int i=0 ;i< kujiSL.size(); i++ ){
				Map<String, Object> kujiMap = kujiSL.get( i );
				dbMsg= i + ")" ;/////////////////////////////////////
				String cName = String.valueOf(kujiMap.get("cName"));
				dbMsg= dbMsg + cName ;/////////////////////////////////////
				String henkou = String.valueOf(kujiMap.get("henkou"));
				dbMsg= dbMsg + ">>" + henkou ;/////////////////////////////////////
				cv.put(cName, henkou);
				dbMsg= dbMsg + ",cv= " + cv;/////////////////////////////////////
				Log.i(TAG,dbMsg);
			}
			if( cv != null){
				kuji_db.update(kuji_table, cv, "_id = " + String.valueOf(kujiId) , null);
			}
			Log.i(TAG,dbMsg);
			kuji_db.close();
			kujiSentakuKekka( kujiSyurui);			//くじ種選択本体
			Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}


	public void kujiSakujyo(  ) {			//くじを削除
		final String TAG = "kujiSakujyo[AtarekunnActivity]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			dTitol = kujiSyurui + getResources().getString(R.string.comon_sakujyo);
			dbMsg = dTitol;
			dMsg = getResources().getString(R.string.comon_kamaimasennka);
			//			dTitol = getResources().getString(R.string.comon_tukamaru);			//ダイアログタイトル ; お願い
			//			dMsg = getResources().getString(R.string.kounyuu_cyuui);				//アラート文 ; 日本国内で海外の宝くじは購入できません。\n雰囲気だけお楽しみください。
			dPbtn = getResources().getString(R.string.comon_jikkou);	            	//ボタン1のキーフェイス ; OK
			dNtn = getResources().getString(R.string.comon_cyuusi);
			dPRet = dr_kujiSakujyo;
			dNRet = 0;
			showDialog(1);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);		// Use the Builder class for convenient dialog construction
			builder.setTitle(dTitol);
			builder.setMessage(dMsg);
			Log.i(TAG,dbMsg);

//			builder.setPositiveButton(getResources().getString(R.string.comon_jikkou), new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {	// User cancelled the dialog
//					final String TAG = "setPositiveButton[kujiSakujyo]";
//					String dbMsg= "開始;";/////////////////////////////////////
//					try{
//						dbStart();				//データベースの作成・オープン
//						ContentValues cv = new ContentValues();
//						String where = "_id = " + String.valueOf(kujiId) ;
//						dbMsg= dbMsg + ",where= " + where;/////////////////////////////////////
//						String[] selectionArgs = null;
//						if( cv != null){
//							int retI =kuji_db.delete(kuji_table, "_id = " + String.valueOf(kujiId) , selectionArgs);
//							dbMsg= dbMsg + ",削除結果= " + retI;/////////////////////////////////////
//						}
//						Log.i(TAG,dbMsg);
//						kuji_db.close();
//						kujimeiList=null;			//DB内のくじリストをクリアにしてkujiSentaku＞dbStartで再リストアップ
//						kujiSentaku( kujiSyurui);
//						kujiSentakuKekka( kujiSyurui);			//くじ種選択本体
//					}catch (Exception e) {
//						Log.e(TAG,dbMsg + "で"+e.toString());
//					}
//				}
//			});
//			builder.setNegativeButton(getResources().getString(R.string.comon_cyuusi), new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {	// User cancelled the dialog
//					//☆記載の必要無し；クリックすればダイアログを閉じる「
//				}
//				});
//			builder.show();
			Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

	public void kujiSakujyo_pb() {            //くじを削除
		final String TAG = "kujiSakujyo_pb[AtarekunnActivity]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			dbStart();				//データベースの作成・オープン
			ContentValues cv = new ContentValues();
			String where = "_id = " + String.valueOf(kujiId) ;
			dbMsg= dbMsg + ",where= " + where;/////////////////////////////////////
			String[] selectionArgs = null;
			if( cv != null){
				int retI =kuji_db.delete(kuji_table, "_id = " + String.valueOf(kujiId) , selectionArgs);
				dbMsg= dbMsg + ",削除結果= " + retI;/////////////////////////////////////
			}
			Log.i(TAG,dbMsg);
			kuji_db.close();
			kujimeiList=null;			//DB内のくじリストをクリアにしてkujiSentaku＞dbStartで再リストアップ
			kujiSentaku( kujiSyurui);
			kujiSentakuKekka( kujiSyurui);			//くじ種選択本体
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

	public void otherSentaku( String dTitol , String[] items ) {			//海外くじの選択
	final String TAG = "otherSentaku[AtarekunnActivity]";
	String dbMsg= "開始;";/////////////////////////////////////
	try{
		dTitol = dTitol;			//ダイアログタイトル ; お願い
		dItems = items;
	//	dMsg = getResources().getString(R.string.kounyuu_cyuui);				//アラート文 ; 日本国内で海外の宝くじは購入できません。\n雰囲気だけお楽しみください。
		dPbtn = getResources().getString(R.string.comon_jikkou);	            	//ボタン1のキーフェイス ; OK
		dNtn = getResources().getString(R.string.comon_cyuusi);
		dPRet = dr_otherSentaku;
		dNRet = 0;
		showDialog(dr_otherSentaku);

//			AlertDialog.Builder builder = new AlertDialog.Builder(this);		// Use the Builder class for convenient dialog construction
//			builder.setTitle(dTitol);
//			builder.setItems(items, new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {		// リスト選択時の処理
//					final String TAG = "onClick[otherSentaku]";
//					String dbMsg= "which=" + which;						// which は、選択されたアイテムのインデックス
//					try{
//						kujiSyurui = otherList.get(which);
//						kujiSentakuKekka( kujiSyurui);			//くじ種選択本体
//						Log.i(TAG,dbMsg);
//					}catch (Exception e) {
//						Log.e(TAG,dbMsg + "で"+e.toString());
//					}
//				}
//			});
//			builder.create().show();		// 表示
			Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

	public List<String> kujimeiList = null;			//登録されているくじ名
	public Cursor cursor;
	public SQLiteDatabase kuji_db;						//計算履歴ファイル
	public String kuji_table;											//計算履歴テーブル名
	public KujiHelper kujiHelper = null;				//計算履歴トヘルパー
	public  SQLiteStatement stmt = null ;			//6；SQLiteStatement
	public List<Map<String, Object>> kujiList;			//DB登録されているくじ名
	public ArrayList<String> tourkuKuji = null;		//既存のくじ

	public void dbStart(){					//データベースの作成・オープン
		String dbMsg = "";
		String TAG="dbStart[AtarekunnActivity]";
		try{
			String fn = this.getApplicationContext().getString(R.string.kuji_file);		//kuji.db
			dbMsg = "fn= " + fn ;
			kuji_table = getResources().getString(R.string.kuji_table);				//kuji_table</string>
			dbMsg = dbMsg +  ",テーブル名=" + kuji_table;
			kujiHelper = new KujiHelper(getApplicationContext() , fn);				//計算履歴トヘルパ
			File dbF = getDatabasePath(fn);			//Environment.getExternalStorageDirectory().getPath();		new File(fn);		//cContext.
			dbMsg = dbMsg + ",dbF=" + dbF;
			dbMsg = dbMsg + " , exists=" + dbF.exists() +" , canWrite=" + dbF.canWrite();
//			Log.i(TAG,dbMsg);
			if(dbF.exists() ){						//ファイルは有る
				if( kuji_db == null){			//開いていなければ
					kuji_db = kujiHelper.getReadableDatabase();		//アーティスト名のえリストファイルを読み書きモードで開く
				}
				dbMsg = dbMsg + ">isOpen>" + kuji_db.isOpen();		//03-28java.lang.IllegalArgumentException:  contains a path separator
				if(! kuji_db.isOpen()){
					dbMsg = dbMsg + ">>delF=" + dbF.getPath();
					kuji_db = kujiHelper.getReadableDatabase();		//アーティスト名のえリストファイルを読み書きモードで開く
					dbMsg = dbMsg + ">isOpen>" + kuji_db.isOpen();		//03-28java.lang.IllegalArgumentException:  contains a path separator
				}
				dbMsg = dbMsg + ">getPageSize>" + kuji_db.getPageSize();		//03-28java.lang.IllegalArgumentException:  contains a path separator
				if(kujimeiList == null){
					kujimeiList = new ArrayList<String>();
					String[] columns =null;					//{  "ALBUM_ARTIST" , "ARTIST"};				//検索結果に含める列名を指定します。nullを指定すると全列の値が含まれます。
					String selections = null;	;				//"ALBUM_ARTIST <> ? AND ALBUM_ARTIST <> ? AND ALBUM_ARTIST <> ?";			//+ comp ;		//MediaStore.Audio.Media.ARTIST +" <> " + comp;			//2.projection  A list of which columns to return. Passing null will return all columns, which is inefficient.
					String[] c_selectionArgs= null;
					String groupBy = null;					//"ALBUM_ARTIST";			//;					//groupBy句を指定します。
					String having =null;					//having句を指定します。
					String orderBy  ="_id";				//
					String limit = null;					//検索結果の上限レコードを数を指定します。
					Cursor cursor = kuji_db.query( kuji_table ,columns, selections,  c_selectionArgs,  groupBy,  having,  orderBy,  limit) ;
					dbMsg= dbMsg + "," + cursor.getCount()+ "×" + cursor.getColumnCount() ;/////////////////////////////////////
					if( cursor.moveToFirst() ){
						do{
							String rStr = cursor.getString(cursor.getColumnIndex("KUJISYRUI"));		//くじの種類
							dbMsg= dbMsg + "くじの種類は" +rStr;/////////////////////////////////////
							kujimeiList.add(rStr);
						}while(cursor.moveToNext());
						dbMsg= dbMsg + kujimeiList.size()+"件";/////////////////////////////////////
					}
					cursor.close();
				}
			} else {
				kuji_db = this.getApplicationContext().openOrCreateDatabase(fn, this.getApplicationContext().MODE_PRIVATE, null);	//String path, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler				//アーティスト名のえリストファイルを読み書きモードで開く
				kuji_db.close();
				dbMsg = dbMsg + ">作り直し>" + this.getApplicationContext().getDatabasePath(fn).getPath();	///data/data/com.hijiyam_koubou.marasongs/databases/artist.db
				kuji_db = kujiHelper.getWritableDatabase();			// データベースをオープン
				kuji_db.beginTransaction();
				stmt = null;
				stmt = kuji_db.compileStatement("insert into " + kuji_table +
						"(KUJISYRUI,VAL_VAL,START_VAL,END_VAL,HON_JUFUKU,SPN_VAL,SPN_START,SPN_END,JUFUKU,SHOU_URL,WH_BAI) values (?,?,?,?,?,?,?,?,?,?,?);");
				//くじの種類,乱数の個数,乱数の開始値,乱数の終了値,本番合の重複,特番の数,特番の開始値,特番の終了値,特番と本番合との重複,照合URL,表示倍率
/*stmt = Zenkyoku_db.compileStatement("insert into " + zenkyokuTName +
		"(ARTIST,ALBUM_ARTIST,ALBUM,TRACK,TITLE,DURATION,YEAR,DATA,MODIFIED,COMPOSER,LAST_YEAR) values (?,?,?,?,?,?,?,?,?,?,?);");
*/
				dbMsg = dbMsg + ",stmt=" + stmt;
				new ContentValues();
//初期値設定///////////////////////////////////////////////////////////////////////////////////
				dbKakikomi( stmt , getResources().getString(R.string.power_ball_name) ,		//Power Ball(米)
						 "5" , "1" ,  "59" ,  "false" ,				//乱数の個数,乱数の開始値,乱数の終了値,						1～59までの数字の中から5つと、
						 "1" , "1" ,  "39" ,  "false" , 	//特番の数,特番の開始値,特番の終了値,特番と本番合との重複	1～39までのパワーボールの中から1つ
						 "http://www.powerball.com/pb_home.asp" ,  "1.0f" );	//,照合URL,表示倍率
				dbKakikomi( stmt , getResources().getString(R.string.mega_millions) ,		//Mega Millions(米)　1～56までの数字を5つと
						 "5" , "1" ,  "56" , "false" ,						//乱数の個数,乱数の開始値,乱数の終了値,
						 "1" , "1" ,  "46" ,  "true" , 				//1～46までのメガボールと呼ばれる数字を1つ
						 "http://www.megamillions.com/" , "1.0f" );	//,照合URL,表示倍率
				dbKakikomi( stmt , getResources().getString(R.string.ruro_mllions_name) ,	//Euro Millions　1～50までの数字を5つと
						 "5" , "1" ,  "50" , "false" ,				//乱数の個数,乱数の開始値,乱数の終了値,
						 "2" , "1" ,  "9" ,  "true" , 	//特番の数,特番の開始値,特番の終了値,特番と本番合との重複	1～9までのラッキースターナンバーを2つ
						 "http://www.euro-millions.com/results.asp" , "1.0f" );	//,照合URL,表示倍率
				dbKakikomi( stmt , getResources().getString(R.string.mar_six_name) ,	////マークシックス（六合彩）香港	1～47までの数字を6つ選択し、全て当たれば1等となる。
						 "7" , "1" ,  "37" , "false" ,				//乱数の個数,乱数の開始値,乱数の終了値,
						 "" , "" ,  "" ,  "" , 	//特番の数,特番の開始値,特番の終了値,特番と本番合との重複	ちなみに数字を15個まで選択する事が可能
						 "http://bet.hkjc.com/marksix/default.aspx" , "1.0f" );	//,照合URL,表示倍率
				dbKakikomi( stmt , getResources().getString(R.string.loto7_name) ,									//ロト7  2013年4月1日 月曜日から
						 "7" , "1" ,  "37" , "false" ,				//乱数の個数,乱数の開始値,乱数の終了値,
						 "" , "" ,  "" ,  "" , 	//特番の数,特番の開始値,特番の終了値,特番と本番合との重複
						 "http://www.takarakujinet.co.jp/loto7/index2.html" , "1.0f" );	//,照合URL,表示倍率
				dbKakikomi( stmt , getResources().getString(R.string.loto6_name) ,									//ロト6
						 "6" , "1" ,  "43" , "false" ,				//乱数の個数,乱数の開始値,乱数の終了値,
						 "" , "" ,  "" ,  "" , 	//特番の数,特番の開始値,特番の終了値,特番と本番合との重複
						 "http://www.takarakujinet.co.jp/loto6/index2.html" , "1.0f" );	//,照合URL,表示倍率
				dbKakikomi( stmt , getResources().getString(R.string.mini_loto_name) ,								//ミニロト
						 "5" , "1" ,  "31" , "false" ,				//乱数の個数,乱数の開始値,乱数の終了値,
						 "" , "" ,  "" ,  "" , 	//特番の数,特番の開始値,特番の終了値,特番と本番合との重複
						 "http://www.takarakujinet.co.jp/miniloto/index2.html" , "1.0f" );	//,照合URL,表示倍率
				dbKakikomi( stmt , getResources().getString(R.string.num4_name) ,									//ナンバーズ4
						 "4" , "1" ,  "9" , "true" ,				//乱数の個数,乱数の開始値,乱数の終了値,
						 "" , "" ,  "" ,  "" , 	//特番の数,特番の開始値,特番の終了値,特番と本番合との重複
						 "http://www.takarakujinet.co.jp/numbers4/index2.html" , "1.0f" );	//,照合URL,表示倍率
				dbKakikomi( stmt , getResources().getString(R.string.num3_name) ,	//ナンバーズ3
						 "3" , "1" ,  "9" , "true" , 				//乱数の個数,乱数の開始値,乱数の終了値,
						 "" , "" ,  "" ,  "" , 	//特番の数,特番の開始値,特番の終了値,特番と本番合との重複
						 "http://www.takarakujinet.co.jp/numbers3/index2.html" , "1.0f" );
//////////////////////////////////////////////////////////////////////////////////初期値設定///
				try{
					dbMsg= "sql_db = " + kuji_db;//////
					kuji_db.setTransactionSuccessful();
				} finally {
					kuji_db.endTransaction();
				}
			}
		//	kuji_db.close();
	//		Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"で"+e);
		}
	}

	public void dbKakikomi(SQLiteStatement stmt , String KUJISYRUI , String VAL_VAL , String START_VAL , String END_VAL , String HON_JUFUKU ,
			String SPN_VAL , String SPN_START , String SPN_END , String JUFUKU , String SHOU_URL , String WH_BAI ) throws IOException {					//データベースへの書き込み
		String dbMsg = "";
		String TAG="dbKakikomi[AtarekunnActivity]";
		try{
			dbMsg = "くじの種類="+ KUJISYRUI;
			if(KUJISYRUI == null){
				stmt.bindString(1, "");
				dbMsg =  dbMsg +">空白文字"  ;
			}else{
				stmt.bindString(1, String.valueOf(KUJISYRUI));
			}

			dbMsg = dbMsg + ",乱数の個数="+ VAL_VAL;
			if(VAL_VAL == null){
				stmt.bindString(2, "");
				dbMsg =  dbMsg +">空白文字"  ;
			}else{
				stmt.bindString(2, String.valueOf(VAL_VAL));
			}
			dbMsg = dbMsg + ",乱数の開始値="+ START_VAL;
			if(START_VAL == null){
				stmt.bindString(3, "");
				dbMsg =  dbMsg +">空白文字"  ;
			}else{
				stmt.bindString(3, String.valueOf(START_VAL));
			}

			dbMsg = dbMsg + ",乱数の終了値="+ END_VAL;
			if(END_VAL == null){
				stmt.bindString(4, "");
				dbMsg =  dbMsg +">空白文字"  ;
			}else{
				stmt.bindString(4, String.valueOf(END_VAL));
			}
			dbMsg = dbMsg + ",本番合の重複="+ HON_JUFUKU;
			if(HON_JUFUKU == null){
				stmt.bindString(5, "");
				dbMsg =  dbMsg +">空白文字"  ;
			}else{
				stmt.bindString(5, String.valueOf(HON_JUFUKU));
			}

			dbMsg = dbMsg + ",特番の数="+ SPN_VAL;
			if(SPN_VAL == null){
				stmt.bindString(6, "");
				dbMsg =  dbMsg +">空白文字"  ;
			}else{
				stmt.bindString(6, String.valueOf(SPN_VAL));
			}
			dbMsg = dbMsg + ",特番の開始値="+ SPN_START;
			if(SPN_START == null){
				stmt.bindString(7, "");
				dbMsg =  dbMsg +">空白文字"  ;
			}else{
				stmt.bindString(7, String.valueOf(SPN_START));
			}
			dbMsg = dbMsg + ",特番の終了値="+ SPN_END;
			if(SPN_END == null){
				stmt.bindString(8, "");
				dbMsg =  dbMsg +">空白文字"  ;
			}else{
				stmt.bindString(8, String.valueOf(SPN_END));
			}
			dbMsg = dbMsg + ",特番と本番合との重複="+ JUFUKU;						//☆更新時間 String.valueOf(System.currentTimeMillis());
			if(JUFUKU == null){
				stmt.bindString(9, "");
				dbMsg =  dbMsg +">空白文字"  ;
			}else{
				stmt.bindString(9, String.valueOf(JUFUKU));
			}
			dbMsg = dbMsg + ",照合URL="+ SHOU_URL;
			if(SHOU_URL == null){
				stmt.bindString(10, "");
				dbMsg =  dbMsg +">空白文字"  ;
			}else{
				stmt.bindString(10, String.valueOf(SHOU_URL));
			}
			dbMsg = dbMsg + ",表示倍率"+ WH_BAI;
			if(WH_BAI == null){
				stmt.bindString(11, "");
				dbMsg =  dbMsg +">空白文字"  ;
			}else{
				stmt.bindString(11, String.valueOf(WH_BAI));
			}
			long id = 0;
			id = stmt.executeInsert();
			dbMsg = dbMsg + "[" + id +"]に追加";///////////////////		T2cActivity.this.
	//		Log.i(TAG,dbMsg);
		}catch(IllegalArgumentException e){
			Log.e(TAG,dbMsg +"で"+e.toString());
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"で"+e);
		}
	}

	public void dbYomidasi( String kujiSyurui ){					//データベースへの書き込み
		String dbMsg = "";
		String TAG="dbYomidasi[AtarekunnActivity]";
		try{
			dbStart();				//データベースの作成・オープン
			dbMsg = "くじの種類="+ kujiSyurui;
			String[] columns =null;					//{  "ALBUM_ARTIST" , "ARTIST"};				//検索結果に含める列名を指定します。nullを指定すると全列の値が含まれます。
			String selections = "KUJISYRUI = ?";	;				//"ALBUM_ARTIST <> ? AND ALBUM_ARTIST <> ? AND ALBUM_ARTIST <> ?";			//+ comp ;		//MediaStore.Audio.Media.ARTIST +" <> " + comp;			//2.projection  A list of which columns to return. Passing null will return all columns, which is inefficient.
			String[] c_selectionArgs= {kujiSyurui};
			String groupBy = null;					//"ALBUM_ARTIST";			//;					//groupBy句を指定します。
			String having =null;					//having句を指定します。
			String orderBy  =null;				//
			String limit = null;					//検索結果の上限レコードを数を指定します。
			Cursor cursor = kuji_db.query( kuji_table ,columns, selections,  c_selectionArgs,  groupBy,  having,  orderBy,  limit) ;
			dbMsg= dbMsg + "," + cursor.getCount()+ "×" + cursor.getColumnCount() ;/////////////////////////////////////
			if( cursor.moveToFirst() ){
				kujiId = setIntItem( cursor , "_id");
		//		kujiId = cursor.getString(cursor.getColumnIndex("_id"));
				dbMsg= dbMsg + "[" + kujiId +"]";/////////////////////////////////////
				AtarekunnActivity.this.kujiSyurui = cursor.getString(cursor.getColumnIndex("KUJISYRUI"));		//くじの種類
				dbMsg= dbMsg + "くじの種類は" + AtarekunnActivity.this.kujiSyurui;/////////////////////////////////////
				val_val = setIntItem( cursor , "VAL_VAL");
				dbMsg= dbMsg + ",乱数の個数は" + val_val;/////////////////////////////////////
				randumStart_val = setIntItem( cursor , "START_VAL");
				dbMsg= dbMsg + "[" + randumStart_val;/////////////////////////////////////
				randumEnd_val = setIntItem( cursor , "END_VAL");
				dbMsg= dbMsg + "～" + randumEnd_val;/////////////////////////////////////
				honJuufuku = Boolean.valueOf(cursor.getString(cursor.getColumnIndex("HON_JUFUKU")));			//特番と本番合との重複,
				dbMsg= dbMsg + ",本番号重複" + honJuufuku;/////////////////////////////////////
				SPNval_val = setIntItem( cursor , "SPN_VAL");
				dbMsg= dbMsg + "]特番" + SPNval_val;/////////////////////////////////////
				SPNStart_val = setIntItem( cursor , "SPN_START");
				dbMsg= dbMsg + "[" + SPNStart_val;/////////////////////////////////////
				SPNumEnd_val = setIntItem( cursor , "SPN_END");
				dbMsg= dbMsg + "～" + SPNumEnd_val;/////////////////////////////////////
				SPNumEnd_val = setIntItem( cursor , "SPN_END");
				juufuku = Boolean.valueOf(cursor.getString(cursor.getColumnIndex("JUFUKU")));			//特番と本番合との重複,
				dbMsg= dbMsg + "]重複" + juufuku;/////////////////////////////////////
				shyougouURL = cursor.getString(cursor.getColumnIndex("SHOU_URL"));			//=loto6_URL;	//照合URL
				dbMsg= dbMsg + ",url=" + shyougouURL;/////////////////////////////////////
				w_hyouji_bairitu = Float.valueOf(cursor.getString(cursor.getColumnIndex("WH_BAI")));			//表示倍率
				dbMsg= dbMsg + ",倍率=" + w_hyouji_bairitu;/////////////////////////////////////
			}
			cursor.close();
			kuji_db.close();
			cyuukiHyouji();			//対象国外で警告表示
			//		Log.i(TAG,dbMsg);
		}catch(IllegalArgumentException e){
			Log.e(TAG,dbMsg +"で"+e.toString());
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"で"+e);
		}
	}

	public int setIntItem(Cursor cursor , String cName){		//データベースから整数項目の転記
		int retInt = 0;
		String dbMsg = "";
		String TAG="setIntItem[AtarekunnActivity]";
		try{
			dbMsg= cName;/////////////////////////////////////
			String rStr = String.valueOf(cursor.getString(cursor.getColumnIndex(cName)));
			dbMsg= dbMsg + "=" + rStr;/////////////////////////////////////
			if(rStr != null){
				if(! rStr.equals("")){
					retInt = Integer.parseInt(rStr);
				}
			}
			dbMsg= dbMsg + ">>" + retInt;/////////////////////////////////////
	//		Log.i(TAG,dbMsg);
		}catch(IllegalArgumentException e){
			Log.e(TAG,dbMsg +"で"+e.toString());
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"で"+e);
		}
		return retInt;
	}


	public void quitMe() {				//このクラスを破棄
		final String TAG = "quitMe";
		String dbMsg = "";//////////////////
		try{
			NendAdInterstitial.dismissAd();
	//		NendAdInterstitial.showFinishAd(this);			// アプリ内で使用するインタースティシャル広告の枠が一つの場合はこちらをお使いください
	//		NendAdInterstitial.showFinishAd (this,458687);		// 広告枠指定ありの場合
			AtarekunnActivity.this.finish();
		} catch (Exception e) {
			Log.d(TAG,"エラー発生；"+e);
		}
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected Dialog onCreateDialog(int id ) {      //builder.show();  ではリークするので
		final String TAG = "onCreateDialog[AtarekunnActivity]";
		String dbMsg = "";//////////////////
		Dialog dlog = null;
		try {
			AlertDialog.Builder builder;
			switch (id) {
				case 1:
					builder = new AlertDialog.Builder (this);        // Use the Builder class for convenient dialog construction
					builder.setTitle (AtarekunnActivity.this.dTitol);                            //ダイアログタイトル ; お願い
					builder.setMessage (AtarekunnActivity.this.dMsg);                            //アラート文 ; 日本国内で海外の宝くじは購入できません。\n雰囲気だけお楽しみください。
					builder.setPositiveButton (AtarekunnActivity.this.dPbtn, new DialogInterface.OnClickListener () {
						public void onClick (DialogInterface dialog, int id) {        //ボタン1のキーフェイス ; OK
							final String TAG = "onCreateDialog[webRecord]";
							String dbMsg = "開始;";/////////////////////////////////////
							try {
								switch (AtarekunnActivity.this.dPRet) {
									case dr_cyuukiHyouji:                //対象国外で警告表示
										break;
									case dr_webRecord:                    //表示されているwebページの設定保存
										webRecordCore ();            //表示されているwebページの設定保存実行
										break;
									case dr_kujiRecTuika:                //新しいくじ
										kujiRecTuika_pb ();
										break;
									case dr_kujiRecKousin:                //表示されているくじの設定保存実行
										kujiRecKousin_pb ();
										break;
									case dr_kujiSakujyo:                //くじを削除
										kujiSakujyo_pb ();
										break;
									}
								} catch (Exception e) {
									Log.e (TAG, dbMsg + "で" + e.toString ());
								}
							}
						});
					if (AtarekunnActivity.this.dNtn != null) {
						builder.setNegativeButton (AtarekunnActivity.this.dNtn, new DialogInterface.OnClickListener () {
							public void onClick (DialogInterface dialog, int id) {    // User cancelled the dialog
								//☆記載の必要無し；クリックすればダイアログを閉じる「
							}
						});
					}
					dlog = builder.create ();
					break;
				case dr_kujiRecord: 				//表示されているくじの設定保存
					LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View content = inflater.inflate(R.layout.kuji_record, null);

					kujisyurui = (TextView) content.findViewById(R.id.kujisyurui);
					dbMsg= dbMsg + ",kujiSyurui=" + kujiSyurui;
					kujisyurui.setText(String.valueOf(kujiSyurui));

					start_val = (TextView) content.findViewById(R.id.start_val);
					dbMsg= dbMsg + ",乱数の開始値=" + randumStart_val;
					start_val.setText(String.valueOf(randumStart_val));

					end_val = (TextView) content.findViewById(R.id.end_val);
					dbMsg= dbMsg + "～=" + randumEnd_val;
					end_val.setText(String.valueOf(randumEnd_val));

					val_valtf = (TextView) content.findViewById(R.id.val_val);
					dbMsg= dbMsg + "×" + val_val;								//
					val_valtf.setText(String.valueOf(val_val));

					honjyuuhuku = (CheckBox) content.findViewById(R.id.honjyuuhuku);
					dbMsg= dbMsg + ",乱数の重複=" + honJuufuku;
					honjyuuhuku.setChecked(honJuufuku);
					honjyuuhuku.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {		// チェックボックスがクリックされた時に呼び出されます
							final String TAG = "kujiRecord[AtarekunnActivity]";
							String dbMsg= "開始;";/////////////////////////////////////
							try{
								CheckBox checkBox = (CheckBox) v;
								AtarekunnActivity.this.r_honJuufuku = checkBox.isChecked();		// チェックボックスのチェック状態を取得します
								Log.i(TAG,dbMsg);
							}catch (Exception e) {
								Log.e(TAG,dbMsg + "で"+e.toString());
							}
						}
					});

					spn_start = (TextView) content.findViewById(R.id.spn_start);
					dbMsg= dbMsg + ",特番の開始値=" + SPNStart_val;
					spn_start.setText(String.valueOf(SPNStart_val));

					spn_end = (TextView) content.findViewById(R.id.spn_end);
					dbMsg= dbMsg + ",特番の終了値=" + SPNumEnd_val;
					spn_end.setText(String.valueOf(SPNumEnd_val));

					spn_val = (TextView) content.findViewById(R.id.spn_val);
					dbMsg= dbMsg + ",特番の数=" + SPNval_val;
					spn_val.setText(String.valueOf(SPNval_val));

					toku_jyuuhuku = (CheckBox) content.findViewById(R.id.toku_jyuuhuku);
					dbMsg= dbMsg + ",特番と本番合との重複=" + juufuku;
					toku_jyuuhuku.setChecked(juufuku);
					toku_jyuuhuku.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {		// チェックボックスがクリックされた時に呼び出されます
							final String TAG = "kujiRecord[AtarekunnActivity]";
							String dbMsg= "開始;";/////////////////////////////////////
							try{
								CheckBox checkBox = (CheckBox) v;
								AtarekunnActivity.this.r_juufuku = checkBox.isChecked();		// チェックボックスのチェック状態を取得します
								Log.i(TAG,dbMsg);
							}catch (Exception e) {
								Log.e(TAG,dbMsg + "で"+e.toString());
							}
						}
					});

					kujiurl = (TextView) content.findViewById(R.id.kujiurl);
					dbMsg= dbMsg + ",照合URL=" + shyougouURL;
					kujiurl.setText(String.valueOf(shyougouURL));
					//			String msg2 = getResources().getString(R.string.wrd_msg2) +";" + shyougouURL + "\n" +
					//							getResources().getString(R.string.wrd_scale) +";" + w_hyouji_bairitu;
					//			TextView wr_message_tf = (TextView) content.findViewById(R.id.wr_message_tf);
					//			wr_message_tf.setText(msg2);
					builder = new AlertDialog.Builder(this);		// Use the Builder class for convenient dialog construction
					builder.setView(content);
					builder.setTitle(dTitol);
					builder.setMessage(dMsg);
					builder.setPositiveButton(getResources().getString(R.string.comon_jikkou), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {	// User cancelled the dialog
							final String TAG = "setPositiveButton[webRecord]";
							String dbMsg= "開始;";/////////////////////////////////////
							try{
								boolean sinki =false;
								int hennkouNasi = 0;

								kujiSL =  new ArrayList<Map<String, Object>>();				//変更リスト
								dbMsg = dbMsg + ",kujiSL=" +  kujiSL;

								r_kujiSyurui = String.valueOf(AtarekunnActivity.this.kujisyurui.getText());
								if( ! kujiSyurui.equals(r_kujiSyurui)){
									dbMsg= cNames[0]+"くじ名称 =" + kujiSyurui;
									dbMsg= dbMsg + ">>" + r_kujiSyurui;
									kujiMap = new HashMap<String, Object> ();
									kujiMap.put("koumokumei" , getResources().getString(R.string.kujiSyuruiTitol) );
									kujiMap.put("cName" ,cNames[0] );
									kujiMap.put("henkou" ,String.valueOf(r_kujiSyurui) );
									kujiMap.put("moto" ,kujiSyurui );
									kujiSL.add( kujiMap);		//kujiSL.size(),
									sinki = true;
									hennkouNasi++;
								}

								r_randumStart_val = String.valueOf(AtarekunnActivity.this.start_val.getText());
								if(! r_randumStart_val.equals(String.valueOf(randumStart_val)) ){
									dbMsg= dbMsg + ","+cNames[1]+ ",乱数の開始値=" + randumStart_val;
									dbMsg= dbMsg + ">>" + r_randumStart_val;
									kujiMap = new HashMap<String, Object>();
									kujiMap.put("koumokumei" , getResources().getString(R.string.randumStart_val_titol) );
									kujiMap.put("cName" ,cNames[1] );
									kujiMap.put("henkou" ,String.valueOf(r_randumStart_val ));
									kujiMap.put("moto" , String.valueOf(randumStart_val ));
									kujiSL.add(kujiMap);
									hennkouNasi++;
								}

								r_randumEnd_val = String.valueOf(AtarekunnActivity.this.end_val.getText());
								if(! r_randumEnd_val.equals(String.valueOf(randumEnd_val)) ){
									dbMsg= dbMsg+ "," + cNames[2]+ ",乱数の終了値=" + randumEnd_val;
									dbMsg= dbMsg + ">>" + r_randumEnd_val;
									kujiMap = new HashMap<String, Object>();
									kujiMap.put("koumokumei" , getResources().getString(R.string.randumEnd_val_titol) );
									kujiMap.put("cName" ,cNames[2] );
									kujiMap.put("henkou" ,String.valueOf(r_randumEnd_val) );
									kujiMap.put("moto" ,randumEnd_val );
									kujiSL.add( kujiMap);
									hennkouNasi++;
								}

								r_val_val = String.valueOf(AtarekunnActivity.this.val_valtf.getText());
								if(! r_val_val.equals(String.valueOf(val_val)) ){
									dbMsg= dbMsg+ "," + cNames[3]+  ",個数=" + val_val;								//
									dbMsg= dbMsg + ">>" + r_val_val;
									kujiMap = new HashMap<String, Object>();
									kujiMap.put("koumokumei" , getResources().getString(R.string.val_val_titol) );
									kujiMap.put("cName" ,cNames[3] );
									kujiMap.put("henkou" ,String.valueOf(r_val_val) );
									kujiMap.put("moto" ,val_val );
									kujiSL.add( kujiMap);
									hennkouNasi++;
								}

								if(AtarekunnActivity.this.honJuufuku != AtarekunnActivity.this.r_honJuufuku){
									dbMsg= dbMsg + ","+ cNames[4]+  ",重複=" + honJuufuku;
									dbMsg= dbMsg + ">>" + r_honJuufuku;
									kujiMap = new HashMap<String, Object>();
									kujiMap.put("koumokumei" , getResources().getString(R.string.kujiset_jyuuhuku) );
									kujiMap.put("cName" ,cNames[4] );
									kujiMap.put("henkou" ,String.valueOf(r_honJuufuku));
									kujiMap.put("moto" ,honJuufuku );
									kujiSL.add( kujiMap);
									hennkouNasi++;
								}

								r_SPNStart_val = String.valueOf(AtarekunnActivity.this.spn_start.getText());
								if(! r_SPNStart_val.equals(String.valueOf(SPNStart_val)) ){
									dbMsg= dbMsg+ "," + cNames[5]+ ",特番の開始値=" + SPNStart_val;
									dbMsg= dbMsg + ">>" + r_SPNStart_val;
									kujiMap = new HashMap<String, Object>();
									kujiMap.put("koumokumei" , getResources().getString(R.string.kujiset_tokubann) +
											getResources().getString(R.string.randumStart_val_titol) );
									kujiMap.put("cName" ,cNames[5] );
									kujiMap.put("henkou" ,String.valueOf(r_SPNStart_val));
									kujiMap.put("moto" ,SPNStart_val );
									kujiSL.add( kujiMap);
									hennkouNasi++;
								}

								r_SPNumEnd_val = String.valueOf(AtarekunnActivity.this.spn_end.getText());
								if(! r_SPNumEnd_val.equals(String.valueOf(SPNumEnd_val)) ){
									dbMsg= dbMsg+ "," + cNames[6]+ ",終了値=" + SPNumEnd_val;
									dbMsg= dbMsg + ">>" + r_SPNumEnd_val;
									kujiMap = new HashMap<String, Object>();
									kujiMap.put("koumokumei" , getResources().getString(R.string.kujiset_tokubann) +
											getResources().getString(R.string.randumEnd_val_titol) );
									kujiMap.put("cName" ,cNames[6] );
									kujiMap.put("henkou" ,String.valueOf(r_SPNumEnd_val));
									kujiMap.put("moto" ,SPNumEnd_val );
									kujiSL.add( kujiMap);
									hennkouNasi++;
								}

								r_SPNval_val = String.valueOf(AtarekunnActivity.this.spn_val.getText());
								if(! r_SPNval_val.equals(String.valueOf(SPNval_val)) ){
									dbMsg= dbMsg+ "," + cNames[7]+  ",特番の数=" + SPNval_val;
									dbMsg= dbMsg + ">>" + r_SPNval_val;
									kujiMap = new HashMap<String, Object>();
									kujiMap.put("koumokumei" , getResources().getString(R.string.kujiset_tokubann) +
											getResources().getString(R.string.SPN_val_titol) );
									kujiMap.put("cName" ,cNames[7] );
									kujiMap.put("henkou" ,String.valueOf(r_SPNval_val));
									kujiMap.put("moto" ,SPNval_val );
									kujiSL.add( kujiMap);
									hennkouNasi++;
								}

								if(AtarekunnActivity.this.juufuku != AtarekunnActivity.this.r_juufuku){
									dbMsg= dbMsg+ "," + cNames[8]+ ",特番と本番合との重複=" + juufuku;
									dbMsg= dbMsg + ">>" + r_juufuku;
									kujiMap = new HashMap<String, Object>();
									kujiMap.put("koumokumei" , getResources().getString(R.string.kujiset_tokubann) +
											getResources().getString(R.string.kujiset_jyuuhuku) );
									kujiMap.put("cName" ,cNames[8] );
									kujiMap.put("henkou" ,String.valueOf(r_juufuku));
									kujiMap.put("moto" ,juufuku );
									kujiSL.add( kujiMap);
									hennkouNasi++;
								}

								r_shyougouURL = String.valueOf(AtarekunnActivity.this.kujiurl.getText());
								if( ! shyougouURL.equals(r_shyougouURL)){
									dbMsg= dbMsg+ "," + cNames[9]+",照合URL=" + shyougouURL;
									dbMsg= dbMsg + ">>" + r_shyougouURL;
									kujiMap = new HashMap<String, Object>();
									kujiMap.put("koumokumei" , getResources().getString(R.string.shyougouURL_titol) );
									kujiMap.put("cName" ,cNames[9] );
									kujiMap.put("henkou" ,String.valueOf(r_shyougouURL));
									kujiMap.put("moto" ,shyougouURL );
									kujiSL.add( kujiMap);
									hennkouNasi++;
								}
								dbMsg= dbMsg + ">hennkouNasi=" + hennkouNasi;
								dbMsg= dbMsg + ">kujiSL=" + kujiSL.size() + "件";
								String dMsg = "";
								for(int i=0 ;i< kujiSL.size(); i++ ){
									Map<String, Object> kujiMap = kujiSL.get( i );
									dMsg = dMsg+String.valueOf(kujiMap.get("koumokumei"));
									if(sinki){
										dMsg = dMsg+" = "+ String.valueOf(kujiMap.get("henkou")) + "\n";
									}else{
										dMsg = dMsg+";"+ String.valueOf(kujiMap.get("moto"));
										dMsg = dMsg+">>"+ String.valueOf(kujiMap.get("henkou")) + "\n";
									}
								}
								dbMsg= dbMsg + ">>=" +dMsg;
								Log.i(TAG,dbMsg);

								if( sinki ){	//名称を変えて新しいくじとして登録
									String dTitol = r_kujiSyurui + getResources().getString(R.string.menu_item_sonota_settei);
									dMsg = dMsg + r_kujiSyurui + getResources().getString(R.string.kujiset_tuikka_msg2);			//を新しいくじとして登録します。</string>
									kujiRecTuika( dTitol , dMsg );			//新しいくじ
								}else if( 0 < hennkouNasi ){
									String dTitol = kujiSyurui + getResources().getString(R.string.comon_hennkou);
									dMsg =dMsg + "\n" + kujiSL.size() + getResources().getString(R.string.comon_koumoku);
									kujiRecKousin( dTitol, dMsg);			//表示されているくじの設定保存実行
								}
							}catch (Exception e) {
								Log.e(TAG,dbMsg + "で"+e.toString());
							}
						}
					});
					builder.setNegativeButton(getResources().getString(R.string.comon_cyuusi), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {	// User cancelled the dialog
							//☆記載の必要無し；クリックすればダイアログを閉じる「
						}
					});
					dlog = builder.create();		// 表示
					break;
				case dr_otherSentaku:                //海外くじの選択
					builder = new AlertDialog.Builder(this);		// Use the Builder class for convenient dialog construction
					builder.setTitle(AtarekunnActivity.this.dTitol);
					builder.setItems(AtarekunnActivity.this.dItems, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {		// リスト選択時の処理
							final String TAG = "onClick[otherSentaku]";
							String dbMsg= "which=" + which;						// which は、選択されたアイテムのインデックス
							try{
								kujiSyurui = otherList.get(which);
								kujiSentakuKekka( kujiSyurui);			//くじ種選択本体
								Log.i(TAG,dbMsg);
							}catch (Exception e) {
								Log.e(TAG,dbMsg + "で"+e.toString());
							}
						}
					});
					dlog = builder.create();		// 表示
					break;
				default:
			//		throw new IllegalArgumentException ("unknown dialog id " + id + ".");
					break;
			}
		} catch (Exception e) {
			Log.e (TAG, dbMsg + "で" + e);
		}
		return dlog;
	}

	//キー対応////////////////////////////////////////////////////////メニューボタンで表示するメニュー//
	public boolean keyAri=false;					//d-pad有りか
	public int nowPosition=0;

	/*判定時間は500msがデフォルトのようです。
		ViewConfiguration.DEFAULT_LONG_PRESS_TIMEOUT = 500
		各コンポーネントからは同クラスのgetLongPressTimeout()で判定されます。*/
	private Boolean mLongPressed = false; // 長押し判定フラグ
	public View nextFV;

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		//☆他のActivityで発生したdispatchKeyEventもここにハンドルされる
		final String TAG = "dispatchKeyEvent[AtarekunnActivity]";
		int keyCode = event.getKeyCode();
		String dbMsg="keyCode="+ String.valueOf(keyCode) ;
		try{
			dbMsg=dbMsg+",getAction="+event.getAction();////////////////////////////////
			dbMsg=dbMsg+",初押時間="+event.getDownTime();////////////////////////////////
			dbMsg=dbMsg+",イベント発生時間="+event.getEventTime();////////////////////////////////
			int renzoku = event.getRepeatCount();
			dbMsg=dbMsg+",連続入力回数="+ renzoku ;////////////////////////////////
			dbMsg=dbMsg+",スキャンコード="+event.getScanCode();////////////////////////////////
			dbMsg=dbMsg+",同時押しとか="+event.getMetaState();////////////////////////////////
			View currentFo = AtarekunnActivity.this.getCurrentFocus();
			Log.i(TAG,dbMsg);
			switch (keyCode) {	//キーにデフォルト以外の動作を与えるもののみを記述★KEYCODE_MENUをここに書くとメニュー表示されない
				case KeyEvent.KEYCODE_DPAD_DOWN:
				case KeyEvent.KEYCODE_DPAD_UP:
				case KeyEvent.KEYCODE_DPAD_LEFT:			//21
				case KeyEvent.KEYCODE_DPAD_RIGHT:		//22
		//		case KeyEvent.KEYCODE_DPAD_CENTER:		//23
					dbMsg=dbMsg+",dPadAri="+dPadAri;////////////////////////////////
					if(! dPadAri){
						setKeyAri();
					}
					if(event.getAction()  == KeyEvent.ACTION_DOWN){			//ACTION_UP
						dPadSousa( keyCode , event);
					}
					break;
				case KeyEvent.KEYCODE_BACK:					//4("BACK", 4)アクティビティを閉じる
					if(dPadAri){
						syoliSentaku( IppatuMainTF );		//Viewの初期選択に戻して
						return true;						//イベント吸収
					}
					break;
//				default:
//					break;
			}
	//		Log.i(TAG,dbMsg);
		} catch (NullPointerException e) {
			Log.e(TAG, dbMsg +"で"+e.toString());
		} catch (Exception e) {
			Log.e(TAG, dbMsg +"で"+e.toString());
		}
		return super.dispatchKeyEvent(event);
	}

//	@Override
//	public boolean onKey(View v, int keyCode, KeyEvent event) {
//		final String TAG = "onKey[AtarekunnActivity]";
//		String dbMsg="";/////////////////////////////////////
//		try{
//			int focusItemID = 0;
//			dbMsg= dbMsg + " , keyCode=" +keyCode;
//			dbMsg= dbMsg+"("+event.getKeyCode();
//			dbMsg= dbMsg+")Action="+event.getAction();
//			View currentFo = this.getCurrentFocus();
//			Log.i(TAG,dbMsg);
//			switch (keyCode) {	//キーにデフォルト以外の動作を与えるもののみを記述★KEYCODE_MENUをここに書くとメニュー表示されない
//			case KeyEvent.KEYCODE_DPAD_DOWN:
//			case KeyEvent.KEYCODE_DPAD_UP:
//			case KeyEvent.KEYCODE_DPAD_LEFT:			//21
//			case KeyEvent.KEYCODE_DPAD_RIGHT:		//22
//			case KeyEvent.KEYCODE_BACK:					//4("BACK", 4)アクティビティを閉じる
//				if(dPadAri){
//					setKeyAri();
//				}
//				dPadSousa( keyCode , event);
//				return true;
//			default:
//				break;
//			}
//
//		} catch (NullPointerException e) {
//			Log.e(TAG,dbMsg +"で"+e.toString());
//		} catch (Exception e) {
//			Log.e(TAG,dbMsg +"で"+e.toString());
//		}
//		return false;
//	}

	public View selBtn;
	public int renzoku;
	public View b_lBtn;
	public void dPadSousa(int keyCode, KeyEvent event) {
		final String TAG = "dPadSousa[AtarekunnActivity]";
		String dbMsg="";
		try{
			dbMsg=dbMsg + "keyCode="+keyCode;////////////////////////////////
			dbMsg=dbMsg+",getAction="+event.getAction();////////////////////////////////
			dbMsg=dbMsg+",初押時間="+event.getDownTime();////////////////////////////////
			dbMsg=dbMsg+",イベント発生時間="+event.getEventTime();////////////////////////////////
			dbMsg=dbMsg+",連続入力回数="+ event.getRepeatCount() ;////////////////////////////////
			dbMsg=dbMsg+",スキャンコード="+event.getScanCode();////////////////////////////////
			dbMsg=dbMsg+",同時押しとか="+event.getMetaState();////////////////////////////////
	//		String motStr = mainText_tv.getText().toString();
//			Editable editable = mainText_tv.getText();
//			String motStr = editable.toString();
			selBtn = null;
	//		Log.i(TAG,dbMsg);
			View currentFo = this.getCurrentFocus();
			int focusItemID= 0 ;
			if(currentFo != null){
				focusItemID=currentFo.getId();
				dbMsg= "focusItemID="+focusItemID;///////////////////////////////////////////////////////////////////
				kujisyuSP.setBackgroundColor(Color.rgb(0, 0, 0));		//くじの種類スピナー
				repetSp.setBackgroundColor(Color.rgb(0, 0, 0));			//繰り返し選択スピナー
				IppatuMainTF.setBackgroundColor(getResources().getColor(R.color.red2));	//一発表示のメイン
				frag_aria.setBackgroundColor(Color.rgb(0, 0, 0));	//情報表示エリア
		//		nend_aria.setBackgroundColor(Color.rgb(0, 0, 0));	//情報表示エリア
		//		mAdView.setBackgroundColor(Color.rgb(0, 0, 0));	//Google AdMob
				currentFo.setBackgroundColor( getResources().getColor(R.color.orenge_select) );					//Color.rgb(246, 152, 32)
				if (focusItemID== kujisyuSP.getId()){
					dbMsg= "くじの種類スピナー="+kujisyuSP.getId() ;///////////////////////////
					dbMsg= dbMsg +", getKeyCode="+event.getKeyCode()  ;///////////////////////////
					switch (keyCode) {	//キーにデフォルト以外の動作を与えるもののみを記述★KEYCODE_MENUをここに書くとメニュー表示されない
					case KeyEvent.KEYCODE_DPAD_UP:
						if (adMobNow){
				//			syoliSentaku( adViewC );
				//		}else if( nenvNow ){
				//			syoliSentaku( nend_aria );
						}
						break;
					case KeyEvent.KEYCODE_DPAD_DOWN:
//						dbMsg= dbMsg +", getAction="+event.getAction() ;///////////////////////////
//						if(event.getAction() == KeyEvent.ACTION_UP){					//ACTION_UP =1
////							mAdView.setFocusable(true);					// ;			//出現状況表示
////							mAdView.setFocusableInTouchMode(true);
////							mAdView.requestFocus();
//						}
						syoliSentaku( IppatuMainTF );										//Viewの初期選択		kotae_tf
						break;
					case KeyEvent.KEYCODE_DPAD_LEFT:			//21
						syoliSentaku( repetSp );
						break;
					case KeyEvent.KEYCODE_DPAD_RIGHT:		//22
						syoliSentaku( repetSp );
						break;
					}
					kujisyuSP.setBackgroundColor(Color.rgb(0, 0, 0));		//くじの種類スピナー
				} else if (focusItemID== repetSp.getId()){
					dbMsg= "繰り返し選択スピナー="+repetSp.getId() ;///////////////////////////
					switch (keyCode) {	//キーにデフォルト以外の動作を与えるもののみを記述★KEYCODE_MENUをここに書くとメニュー表示されない
					case KeyEvent.KEYCODE_DPAD_UP:
			//			if (adMobNow){
			//				syoliSentaku( adViewC );
			//			}else if( nenvNow ){
			//				syoliSentaku( nend_aria );
			//			}
						break;
					case KeyEvent.KEYCODE_DPAD_DOWN:
						syoliSentaku( IppatuMainTF );										//Viewの初期選択		kotae_tf
						break;
					case KeyEvent.KEYCODE_DPAD_LEFT:			//21
						syoliSentaku( kujisyuSP );										//Viewの初期選択		kotae_tf
						break;
					case KeyEvent.KEYCODE_DPAD_RIGHT:		//22
						syoliSentaku( kujisyuSP );										//Viewの初期選択		kotae_tf
						break;
					}
					repetSp.setBackgroundColor(Color.rgb(0, 0, 0));			//繰り返し選択スピナー
				} else if (focusItemID== IppatuMainTF.getId()){
					dbMsg= "一発表示のメイン="+IppatuMainTF.getId() ;///////////////////////////
					switch (keyCode) {	//キーにデフォルト以外の動作を与えるもののみを記述★KEYCODE_MENUをここに書くとメニュー表示されない
					case KeyEvent.KEYCODE_DPAD_UP:
						syoliSentaku( kujisyuSP );										//Viewの初期選択		kotae_tf
						break;
					case KeyEvent.KEYCODE_DPAD_DOWN:
						syoliSentaku( frag_aria );										//Viewの初期選択		kotae_tf
						break;
					}
					IppatuMainTF.setBackgroundColor(getResources().getColor(R.color.red2));	//一発表示のメイン
				} else if (focusItemID== frag_aria.getId()){
					dbMsg= "webキットの読み込み="+frag_aria.getId() ;///////////////////////////
					dbMsg= dbMsg +", getKeyCode="+event.getKeyCode()  ;///////////////////////////
					switch (keyCode) {	//キーにデフォルト以外の動作を与えるもののみを記述★KEYCODE_MENUをここに書くとメニュー表示されない
					case KeyEvent.KEYCODE_DPAD_UP:
						syoliSentaku( IppatuMainTF );										//Viewの初期選択		kotae_tf
						break;
					case KeyEvent.KEYCODE_DPAD_DOWN:
						dbMsg= dbMsg +",adMobNow="+adMobNow  +", nenvNow="+nenvNow ;///////////////////////////
//						if(adMobNow){
//							if (adViewC != null){
//								syoliSentaku( adViewC );
//							} else {
//								syoliSentaku( kujisyuSP );
//							}
//						} else if(nenvNow){
//							if (nend_aria != null){
//								syoliSentaku( nend_aria );
//							} else {
//								syoliSentaku( kujisyuSP );
//							}
//						} else {
//							syoliSentaku( kujisyuSP );
//						}
						break;
					case KeyEvent.KEYCODE_DPAD_LEFT:			//21
						webView.zoomOut ();                 //webFtagnemt.wZDown();
						break;
					case KeyEvent.KEYCODE_DPAD_RIGHT:		//22
						webView.zoomIn();                         //webFtagnemt.wZUp();
						break;
					}
					frag_aria.setBackgroundColor(Color.rgb(0, 0, 0));			//繰り返し選択スピナー
//				} else if ( nend_aria != null){
//					if (focusItemID== nend_aria.getId()){
//						dbMsg= "nend表示エリア="+nend_aria.getId() ;
//						switch (keyCode) {	//キーにデフォルト以外の動作を与えるもののみを記述★KEYCODE_MENUをここに書くとメニュー表示されない
//						case KeyEvent.KEYCODE_DPAD_UP:
//							syoliSentaku( frag_aria );										//Viewの初期選択		kotae_tf
//							break;
//						case KeyEvent.KEYCODE_DPAD_DOWN:
//							syoliSentaku( kujisyuSP );										//Viewの初期選択		kotae_tf
//							break;
//						}
//						nendAdView.setBackgroundColor(Color.rgb(0, 0, 0));			//繰り返し選択スピナー
//					} else if (adViewC != null){
//						if (focusItemID== adViewC.getId()){
//							dbMsg= "Google表示エリア="+adViewC.getId() ;
//							switch (keyCode) {	//キーにデフォルト以外の動作を与えるもののみを記述★KEYCODE_MENUをここに書くとメニュー表示されない
//							case KeyEvent.KEYCODE_DPAD_UP:
//								syoliSentaku( frag_aria );										//Viewの初期選択		kotae_tf
//								break;
//							case KeyEvent.KEYCODE_DPAD_DOWN:
//								syoliSentaku( kujisyuSP );										//Viewの初期選択		kotae_tf
//								break;
//							}
//							adViewC.setBackgroundColor(Color.rgb(0, 0, 0));			//繰り返し選択スピナー
//						}
//					}
				}
			}
			if(focusItemID == -1){
				syoliSentaku( IppatuMainTF );
			}
			Log.i(TAG,dbMsg);
		} catch (NullPointerException e) {
			Log.e(TAG, dbMsg +"で"+e.toString());
		} catch (Exception e) {
			Log.e(TAG, dbMsg +"で"+e.toString());
		}
	}


	public void setKeyAri() {									//d-pad対応
		final String TAG = "setKeyAri[AtarekunnActivity]";
		String dbMsg=TAG+"開始";
		try{
			dPadAri=true;					//d-pad有りか
			dbMsg="dPadAri=" +dPadAri ;
			myEditor.putBoolean("dPad_ari", dPadAri);
			boolean kakikomi = myEditor.commit();
			dbMsg= dbMsg +",kakikomi=" +kakikomi ;

			kujisyuSP.setNextFocusDownId(IppatuMainTF.getId());		//くじの種類スピナー
			repetSp.setNextFocusDownId(IppatuMainTF.getId());		//繰り返し選択スピナー
			IppatuMainTF.setNextFocusUpId(kujisyuSP.getId());		//一発表示のメイン
			IppatuMainTF.setNextFocusDownId(frag_aria.getId());		//
			frag_aria.setNextFocusUpId(IppatuMainTF.getId());		//情報エリア
//
			dbMsg= dbMsg +",nenvNow=" +nenvNow ;
			dbMsg= dbMsg +",adMobNow=" +adMobNow ;
//			if(adMobNow){
//				if (adViewC != null){
//
//					syoliSentaku( adViewC );
//				} else {
//					syoliSentaku( kujisyuSP );
//				}
//			} else if(nenvNow){
//				if (nend_aria != null){
//					syoliSentaku( nend_aria );
//				} else {
//					syoliSentaku( kujisyuSP );
//				}
//			} else {
//				syoliSentaku( kujisyuSP );
//			}
			syoliSentaku( IppatuMainTF );							//一発表示のメイン
			Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"で"+e);
		}
	}

	public void syoliSentaku( View sentaku ) {										//Viewの初期選択
		final String TAG = "syoliSentaku[AtarekunnActivity]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			View currentFo = this.getCurrentFocus();
//			if( currentFo != null ){
//				currentFo.refreshDrawableState();
//			}
//			dbMsg = "currentFo=" + currentFo;
//			dbMsg = "＞＞sentaku=" + sentaku;
//			sentaku.setSelected(true);								//a_clear_btn
//			sentaku.setFocusable(true);
//			sentaku.setFocusableInTouchMode(true);
//			sentaku.requestFocus();
//			sentaku.setBackgroundDrawable( getResources().getDrawable(R.drawable.i_selecter));
	//		Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent rData) { // startActivityForResult で起動させたアクティビティがfinish() により破棄されたときにコールされる
	// requestCode : startActivityForResult の第二引数で指定した値が渡される
	// resultCode : 起動先のActivity.setResult の第一引数が渡される
	// Intent data : 起動先Activityから送られてくる Intent
		super.onActivityResult(requestCode, resultCode, rData);
		final String TAG = "onActivityResult";
		String dbMsg = "";//////////////////
		try{
			dbMsg = "requestCode="+requestCode+",resultCode="+resultCode+",rData="+rData;//////////////////
			Log.d(TAG,dbMsg);
			switch(requestCode) {
			case MENU_SETTEI:				//エラー発生時
				readPrif();		//プリファレンスの読込み
				setTitolComent(); 				//ウィンドウタイトルとボタンの書き換え
				break;
			case MENU_ruiseki:			//511;//1.MediaStoreをSQLiteに読み込む
				ruiseki();
				break;
			}
		}catch (Exception e) {
			Log.e(TAG,"で"+e.toString());
		}
	}		//http://fernweh.jp/b/startactivityforresult/


	@Override
	protected void onRestart() {
		super.onRestart();
		final String TAG = "onRestart[AtarekunnActivity]";
		String dbMsg = "";//////////////////
		try{
			Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"；"+e);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		final String TAG = "onResume[AtarekunnActivity]";
		String dbMsg = "";//////////////////E/ActivityThread: Performing stop of activity that is not resumed: {com.hijiyama_koubou.atare_kun/com.hijiyama_koubou.atare_kun.AtarekunnActivity
		try{
			Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"；"+e);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		final String TAG = "onStart[AtarekunnActivity]";
		String dbMsg = "";//////////////////
		try{
			Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"；"+e);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		final String TAG = "onPause[AtarekunnActivity]";
		String dbMsg = "";//////////////////
		try{
			dbMsg = "adView="+ mAdView;//////////////////
			mAdView.pause();
			dbMsg = ">>"+ mAdView;//////////////////
			Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e("onPause",dbMsg+"；"+e);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		final String TAG = "onStart[AtarekunnActivity]";
		String dbMsg = "";//////////////////
		try{
			Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"；"+e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		final String TAG = "onDestroy[AtarekunnActivity]";
		String dbMsg = "";//////////////////
		try{
			dbMsg = "adView="+ mAdView;//////////////////
			mAdView.destroy();
			dbMsg = ">>"+ mAdView;//////////////////

			dbMsg = "adView="+ mAdView;//////////////////
			mAdView.destroy();
			dbMsg = ">>"+ mAdView;//////////////////


			Log.i(TAG,dbMsg);
			wriAllPrif();		//プリファレンス全項目書込み
			quitMe();			//このクラスを破棄
			Log.i(TAG,dbMsg);
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"；"+e);
		}
	}


}
//収益化
//importsdk/extras/google/google_play_services/libproject/google-play-services_lib/
//https://developers.google.com/mobile-ads-sdk/docs/?hl=ja
//https://developers.google.com/admob/android/eclipse?hl=ja#download_the_google_play_services_sdk
//http://kino2718.blogspot.jp/2014/03/androidadmob-sdkgoogle-play-sdk.html
/*Error:(31, 1) Could not compile build file 'I:\an\workspace\atarekunn\app\build.gradle'.
> startup failed:
  build file 'I:\an\workspace\atarekunn\app\build.gradle': 31: expecting '}', found '' @ line 31, column 2.
     }
      ^
  1 error*/