package com.hijiyama_koubou.atare_kun;

public class AsyncTaskResult<T> {
	/*	AsyncTask#doInBackgroundの戻り値を考える	http://d.hatena.ne.jp/tomorrowkey/20100824/1282655538
    AsyncTaskのdoInBackgroundメソッドからonPostExecuteメソッドへ渡す引数用の独自クラス
    コンストラクタを使用できなくし、インスタンス作成メソッドを用意する
    他のAsyncTaskでも使えるよう、汎用的に作ります
*/
	private T content;		//AsyncTaskで取得したデータ
	private int resId;		//エラーメッセージのリソースID
	private boolean isError;		//エラーならtrueが設定されている

	private AsyncTaskResult(T content, boolean isError, int resId) {	//コンストラクタ;content=AsyncTaskで取得したデータ,isError=エラーならtrueを設定する,resId=エラーメッセージのリソースIDを指定する
		this.content = content;
		this.isError = isError;
		this.resId = resId;
	}

	public T getContent() {		//AsyncTaskで取得したデータを返す@return AsyncTaskで取得したデータ
		return content;
	}

	public boolean isError() {		//エラーならtrueを返す@return エラーならtrueを返す
		return isError;
	}

	public int getResourceId() {		//stringリソースのIDを返す@return stringリソースのIDを返す
		return resId;
	}

	public static <T> AsyncTaskResult<T> createNormalResult(T content) {	//AsyncTaskが正常終了した場合の結果を作る@param <T> @param content,AsyncTaskで取得したデータを指定する, @return AsyncTaskResult
		return new AsyncTaskResult<T>(content, false, 0);
	}

	public static <T> AsyncTaskResult<T> createErrorResult(int resId) {	//AsyncTaskが異常終了した場合の結果を作る @param <T>@param resId=エラーメッセージのリソースIDを指定する,@return AsyncTaskResult
		return new AsyncTaskResult<T>(null, true, resId);
	}
}