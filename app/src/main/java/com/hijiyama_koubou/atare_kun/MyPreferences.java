package com.hijiyama_koubou.atare_kun;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.text.InputType;
import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;

public class MyPreferences extends PreferenceActivity {
	AtarekunnActivity AAA = new AtarekunnActivity();  //オブジェクトNakedFileVeiwActivityの生成
	public String dbMsg;
	public SharedPreferences sharedPref;// = AAA.sharedPref;
	public Editor myEditor;//=AAA.myEditor;
	public boolean dPadAri=false;			//ダイヤルキーは使えない
	public String pref_apiLv;		//APIL
	public int kurikaesi_val;				//繰り返し
//	public String kujiSyurui;		//);	//現在のくじ種類
	public boolean prefUseDlog;			//ダイヤログの利用

	public EditTextPreference shyougouUrlTF;		//照合サイトURL="http://www.takarakujinet.co.jp/loto6/index2.html"
	public CheckBoxPreference prefJufukuCheck;		//"prefJufuku">本番号と予備番号の重複

	//	public PreferenceScreen pref_sonota;					//その他
	public EditTextPreference kurikaesiTF;			//繰り返し判定数
	public CheckBoxPreference prefUseDlogCheck;	//ダイアログの使用/未使用
	public CheckBoxPreference pref_dPadAri;		//ダイヤルキー

	public Map<String, ?> keys;
	public EditTextPreference pEdit;
	public Resources res;
	public Context context;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final String TAG = "onCreate[MyPreferences]";
		String dbMsg = "開始";//////////////////
		try{
		//	res = getResources();
	//		sharedPref = getSharedPreferences(getResources().getString(R.string.pref_main_file),MODE_PRIVATE);		//	getSharedPreferences(prefFname,MODE_PRIVATE);
			sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());			//	this.getSharedPreferences(this, MODE_PRIVATE);		//
			myEditor = sharedPref.edit();
			Locale locale = Locale.getDefault();		// アプリで使用されているロケール情報を取得
			dbMsg=dbMsg+ "locale="+locale;/////////////////////////////////////

			Bundle extras = getIntent().getExtras();
			kurikaesi_val = extras.getInt("kurikaesi_val");
			dbMsg =dbMsg+  ",繰り返し="+kurikaesi_val;		////////////////
//			kurikaesi_val = String.valueOf(kurikaesi_val);	//繰り返し	extras.getInt(
////			String rStr = new String(kurikaesi_val);
//			dbMsg =dbMsg+  ",>String>="+kurikaesi_val;//////////////////
//			if(kurikaesi_val != null){
//				if(kurikaesi_val == "0"){
//					kurikaesi_val ="100";
//				}
//			}
			CharSequence wrVal = String.valueOf(kurikaesi_val);
			dbMsg =dbMsg+  ">CharSequence>"+wrVal;//////////////////
			prefUseDlog = extras.getBoolean("prefUseDlog");			//ダイヤログの利用
			dbMsg =dbMsg+  "、ダイアログの使用="+String.valueOf(prefUseDlog);//////////////////
			dPadAri = extras.getBoolean("dPadAri");			//ダイヤログの利用
			dbMsg =dbMsg+  ",ダイヤルキー="+ dPadAri;//////////////////
////java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String
// Converting to string: TypedValue{t=0x10/d=0x64 a=-1}
//android.content.res.Resources$NotFoundException: String resource ID #0x55

			this.addPreferencesFromResource(R.xml.preferences);
			kurikaesiTF = (EditTextPreference)getPreferenceScreen().findPreference("pref_kurikaesi");					//繰り返し判定数
			kurikaesiTF.setSummary(wrVal);
			kurikaesiTF.setDefaultValue(kurikaesi_val);					//☆数値
			Log.i(TAG,dbMsg);
			kurikaesiTF.setOnPreferenceChangeListener( new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference,Object newValue) {
					final String TAG = "onPreferenceChange[kurikaesiTF]";
					String dbMsg = "newValue=" + newValue;//////////////////
					try{
	//					String i2t = newValue.toString();
//						dbMsg = dbMsg+",書込み="+kakikomi;//////////////////
						if( newValue != null){
							setVals( "pref_kurikaesi" , newValue);
						}
					} catch (Exception e) {
						Log.e(TAG,dbMsg+"；"+e.toString());
					}
					return true;
				}
			});

		//	pEdit = (EditTextPreference)findPreference("kurikaesi_val");

			dbMsg =dbMsg+  "、ダイアログの使用="+String.valueOf(prefUseDlog);//////////////////
			prefUseDlogCheck = new CheckBoxPreference(this);
			prefUseDlogCheck =(CheckBoxPreference) findPreference("prefUseDlog_ch");	//ダイアログの使用/未使用
			prefUseDlogCheck.setChecked(prefUseDlog);
			prefUseDlogCheck.setOnPreferenceChangeListener( new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference,Object newValue) {
					final String TAG = "onPreferenceChange[prefUseDlogCheck]";
					String dbMsg = "";//////////////////
					try{
						String i2t = newValue.toString();
						dbMsg = "newValue="+i2t;//////////////////
						Log.i(TAG,dbMsg);
						setVals( "prefUseDlog_ch" , newValue);
					} catch (Exception e) {
						Log.e(TAG,dbMsg+"；"+e.toString());
					}
					return true;
				}
			});

			dbMsg =dbMsg+  ",ダイヤルキー="+ dPadAri;//////////////////
			pref_dPadAri = new CheckBoxPreference(this);		//ダイヤルキー
			pref_dPadAri =(CheckBoxPreference) findPreference("dPadAri");
			pref_dPadAri.setChecked(dPadAri);

			Log.i(TAG,dbMsg);
	//		viewSakusei( );			//プリファレンスの表示処理
		} catch (Exception e) {
			Log.e(TAG,dbMsg+"；"+e.toString());
		}
	}

//	private OnSharedPreferenceChangeListener onPreferenceChangeListenter = new OnSharedPreferenceChangeListener() {
//		@Override
//		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		public void setVals( String key , Object newValue) {
			final String TAG = "setVals[MyPreferences]";
			String dbMsg = "";//////////////////
			try{
				dbMsg = "key="+key.toString();//////////////////
				String i2t = String.valueOf(newValue);
				dbMsg = "newValue="+i2t;//////////////////
				Log.i(TAG,dbMsg);
				if (key.equals("pref_kurikaesi")) {
					CharSequence wrVal = String.valueOf(newValue);
					kurikaesi_val = Integer.parseInt(i2t);
					dbMsg = dbMsg+",値="+ kurikaesi_val;//////////////////
					kurikaesiTF.setSummary( wrVal);		//異常終了；wrVal(終了後)
//kurikaesi_val　で　android.content.res.Resources$NotFoundException: String resource ID #0x2c
//i2t　で	java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String

					Log.i(TAG,dbMsg);
				//	kurikaesiTF.setDefaultValue(wrVal);		//☆java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String
					Log.i(TAG,dbMsg);
					myEditor.putString("pref_kurikaesi",String.valueOf(newValue));	//繰り返し☆文字で書き込む
				} else if (key.equals("prefUseDlog_ch")) {
					CheckBoxPreference pref = (CheckBoxPreference)findPreference(key);
					prefUseDlog = pref.isChecked();
					dbMsg = dbMsg+",prefUseDlog="+prefUseDlog;//////////////////
					pref.setChecked(pref.isChecked());
					myEditor.putBoolean("prefUseDlog_ch", pref.isChecked());			//ダイアログの使用/未使用	true	false
				}
				boolean kakikomi =myEditor.commit();
				dbMsg = dbMsg+",書込み="+kakikomi;//////////////////
				Log.i(TAG,dbMsg);
			} catch (Exception e) {
				Log.e(TAG,dbMsg+"で"+e.toString());
			}
		}
//	};

	protected boolean isEditOBJ(String testStr){			//EditTextPreferenceで設定された文字項目☆項目追加時はここに記載
		boolean retB=false;
		if(testStr .equals("shyougouURL")					//照合サイトURL
		){
			return true;
		}
		return retB;
	}

	protected boolean isIntOBJ(String testStr){			//EditTextPreferenceで設定された項目☆項目追加時はここに記載
		boolean retB=false;
		if(testStr .equals("randumStart_val")				//乱数の開始値
				|| testStr .equals("randumEnd_val")			//乱数の終了値
				|| testStr .equals("val_val")				//乱数の個数
				|| testStr .equals("kurikaesi_val")			//繰り返し判定数
				|| testStr .equals("SPNval_val")			//特番の数
				|| testStr .equals("SPNStart_val")			//特番の開始値
				|| testStr .equals("SPNumEnd_val")			//特番の終了値
		){
			return true;
		}
		return retB;
	}

	protected boolean isListOBJ(String testStr){			//ListPreferenceで設定された項目☆項目追加時はここに記載
		boolean retB=false;
		if(testStr .equals("kujiSyurui")				//くじの種類
//			|| testStr .equals("prefsSortOder")				//ソーﾄ
		){
			return true;
		}
		return retB;
	}

	protected boolean isCheckOBJ(String testStr){			//CheckBoxtPreferenceで設定された項目☆項目追加時はここに記載
		boolean retB=false;
		if(testStr .equals("prefUseDlog_ch")			//ダイアログの使用/未使用
			|| testStr .equals("prefJufuku_ch")			//重複チャック
			|| testStr .equals("dPadAri")				//キー
		){
			return true;
		}
		return retB;
	}


	// ここで summary を動的に変更
//	private SharedPreferences.OnSharedPreferenceChangeListener listener =new SharedPreferences.OnSharedPreferenceChangeListener() {
//		public void onSharedPreferenceChanged(SharedPreferences onSharedPreferenceChanged, String key) {
//			String valStr;
//			final String TAG = "onSharedPreferenceChanged[MyPreferences]";
//			String dbMsg = "";//////////////////
//			try{
//				dbMsg = "key="+key.toString();//////////////////
//				Log.i(TAG,dbMsg);
//				if(isCheckOBJ(key)){
//					if(key.equals("prefUseDlog_ch")){
//						prefUseDlog = prefUseDlogCheck.isChecked();			//	Boolean.valueOf("valStr");
//						valStr = String.valueOf(prefUseDlogCheck.isChecked());
//					}else if(key.equals("prefJufuku_ch")){
//						AtarekunnActivity.juufuku = prefJufukuCheck.isChecked();			//	Boolean.valueOf("valStr");
//						valStr = String.valueOf(prefJufukuCheck.isChecked());
//					}else if(key.equals("dPadAri")){
//			//			AtarekunnActivity.keyAri = dPadCheck.isChecked();			//	Boolean.valueOf("valStr");
//						valStr = String.valueOf(pref_dPadAri.isChecked());
//					}
//				}else{
//					pEdit = (EditTextPreference)findPreference(key);
//					valStr = pEdit.getText();
//					if(key.equals("kurikaesi_val")){									//=100;繰り返し判定数
//						kurikaesi_val=Integer.valueOf(valStr);
//						if(AAA.isNum(valStr)){
//							valStr=String.valueOf(valStr);
//						}
//						dbMsg = dbMsg+"="+valStr;//////////////////
//						Log.i(TAG,dbMsg);
//						pEdit.setSummary(pEdit.getText());
//					}
//				}
//			} catch (Exception e) {
//				Log.e(TAG,dbMsg+"で"+e.toString());
//			}
//		}	//onSharedPreferenceChanged(SharedPreferences onSharedPreferenceChanged, String key) {
//	};

//	public void viewSakusei( ) {				//プリファレンスの表示処理
//		//<<onSharedPreferenceChanged , prefItemuKakikomi , onWindowFocusChanged , viewSakusei
//		final String TAG = "viewSakusei[MyPreferences]";
//		String dbMsg="開始";/////////////////////////////////////
//		try{
//		//	String pref_sonota;
//		//	dbMsg = "その他" + pref_sonota;//////////////////pTF_pref_gyapless
//			String wrStr= null;
//			String playerMsg =null;	//プレイヤー設定//////////////////////////////////////////////////////////
//			if(0 < kurikaesi_val ){
//				dbMsg = "繰り返し回数=" + kurikaesi_val;//////////////////pTF_pref_gyapless
//				playerMsg =  getResources().getString(R.string.kurikaesi_val_titol) + kurikaesi_val +  getResources().getString(R.string.main_tf_kaimaderuiseki)  + "\n" ;
//			}
//			playerMsg = playerMsg + getResources().getString(R.string.prefUseDlogTitol) + kurikaesi_val +  getResources().getString(R.string.main_tf_kaimaderuiseki)  + "\n" ;
//			dbMsg = dbMsg + "ダイアログ=" + prefUseDlogCheck.getSummary();//////////////////pTF_pref_gyapless
//			if(Boolean.valueOf((String) prefUseDlogCheck.getSummary() )){
//				playerMsg = playerMsg + getResources().getString(R.string.comon_tukau)  + "\n" ;
//			}else{
//				playerMsg = playerMsg + getResources().getString(R.string.comon_tukawanai)  + "\n" ;
//
//			}
//			dbMsg = dbMsg + "ダイヤルキー=" + pref_dPadAri.getSummary();//////////////////pTF_pref_gyapless
//		//	boolean hantei= .getSummary();
//			if(Boolean.valueOf((String) pref_dPadAri.getSummary() )){
//				playerMsg = playerMsg + getResources().getString(R.string.comon_tukau)  + "\n" ;
//			}else{
//				playerMsg = playerMsg + getResources().getString(R.string.comon_tukawanai)  + "\n" ;
//			}
//			Log.i(TAG,dbMsg);
//		}catch (Exception e) {
//			Log.e(TAG,dbMsg + "で"+e.toString());
//		}
//	}


	@Override
	protected void onResume() {
		super.onResume();
//		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyPreferences.this.finish();
	//	//Log.d("onDestroy","onDestroyが発生");
	//	AAA.readPrif();		//プリファレンスの読込み
	//	clPref();	//プリファレンス設定状況読み込み
	}
}