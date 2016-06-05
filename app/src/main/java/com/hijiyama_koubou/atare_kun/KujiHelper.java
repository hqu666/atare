package com.hijiyama_koubou.atare_kun;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;
import android.util.Log;

public class KujiHelper extends SQLiteOpenHelper {
	final static private int DB_VERSION = 1;
	private Context rContext;	//読出し元
	public String dbName;		//第２引数は、データベースファイルの名前です。この引数にnullを指定すると、データベースはメモリー上に作られます。

	public KujiHelper(Context context , String dFn) {					//数式の記録
		super(context, dFn, null, DB_VERSION);
		final String TAG = "KujiHelper[KujiHelper]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			dbMsg="getPackageCodePath="+context.getPackageCodePath();/////////////////////////////////////
			rContext = context;						//第１引数; context ;読出し元;データベースを所有するコンテキストオブジェクトを指定します。
			dbName = dFn;							//第２引数; fileName ;データベースファイルの名前です。この引数にnullを指定すると、データベースはメモリー上に作られます。
			dbMsg = dbMsg + " , db=" + dbName;
			dbMsg=dbMsg + ",バージョン="+DB_VERSION;	//第4引数; version ;データベースのバージョンを指定します。
	//		Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {		// table create
		final String TAG = "onCreate[KujiHelper]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			String tName = rContext.getResources().getString(R.string.kuji_table);
			dbMsg="テーブル名= "+ tName;/////////////////////////////////////
			String tSet = "create table " + tName +" (" +							//テーブル名；artist_rw_table
					"_id integer primary key autoincrement not null, "+ 			//作成したリストの連番
					"	KUJISYRUI text," +				// ="";			//くじの種類	val_val
					"	VAL_VAL text, " +			// =6;				//乱数の個数
					"	START_VAL text, " +			// =1;		//乱数の開始値
					"	END_VAL text, " +					// =43;		//乱数の終了値
					"	HON_JUFUKU text, " +				//本番合の重複
					"	SPN_VAL text, " +					//  =0;			//特番の数
					"	SPN_START text, " +				// =0;			//特番の開始値
					"	SPN_END text, " +					// =0;			//特番の終了値
					"	JUFUKU text, " +				//boolean  =false;		//特番と本番合との重複
					"	SHOU_URL text, " +			//=loto6_URL;	//照合URL
					"	WH_BAI text " +			// =loto6_WHB;			//表示倍率
						");";

			dbMsg=dbMsg +",tSet= "+tSet;/////////////////////////////////////
			db.execSQL(tSet);
	//		Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		final String TAG = "onUpgrade[KujiHelper]";
		String dbMsg= "開始;";/////////////////////////////////////
		try{
			Log.i(TAG,dbMsg);
		}catch (Exception e) {
			Log.e(TAG,dbMsg + "で"+e.toString());
		}
	}
}

//http://ichitcltk.hustle.ne.jp/gudon/modules/pico_rd/index.php?content_id=74