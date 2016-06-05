package com.hijiyama_koubou.atare_kun;

public interface plogTaskCallback {
	void onSuccessplogTask(int[] retArry );	//成功した時に呼ばれるメソッド
	void onFailedDownloadImage(int resId);	//失敗した時に呼ばれるメソッド  @param resId;エラーメッセージのリソースID
}
//http://android.keicode.com/basics/async-asynctask.php