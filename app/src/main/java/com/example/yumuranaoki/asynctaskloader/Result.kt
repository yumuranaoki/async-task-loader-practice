package com.example.yumuranaoki.asynctaskloader

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class ResultLoader(context: Context): AsyncTaskLoader<String>(context) {
    override fun loadInBackground(): String? {
        val response = httpGet("http://localhost:3000")
        if (response != null) {
            // 取得に成功したら、パースして返す
            val jsonResponse: JSONObject? = parseJson(response)
            if (jsonResponse != null) {
                val publicKey = jsonResponse.getString("publicKey") ?: "no keys found"
                return publicKey
            }
        }

        return null
    }

    override fun onStartLoading() {
        forceLoad()
    }

    override fun onStopLoading() {
        cancelLoad()
    }

    override fun onReset() {
        super.onReset()
        onStopLoading()
    }
}

fun httpGet(url: String) : InputStream? {
    // 通信接続用のオブジェクトを作る
    val con = URL(url).openConnection() as HttpsURLConnection

    // 接続の設定を行う
    con.apply {
        requestMethod = "GET"           // メソッド
        connectTimeout = 3000           // 接続のタイムアウト（ミリ秒）
        readTimeout = 5000              // 読み込みのタイムアウト（ミリ秒）
        instanceFollowRedirects = true  // リダイレクト許可
    }

    // 接続する
    con.connect()

    // ステータスコードの確認
    if (con.responseCode in 200..299) {
        // 成功したら、レスポンスの入力ストリームを、BufferedInputStreamとして返す
        return BufferedInputStream(con.inputStream)
    }

    // 失敗
    return null
}

fun parseJson(inputStream: InputStream): JSONObject? {
    val response = BufferedReader(InputStreamReader(inputStream))
    var result: String? = null
    try {
        result = response.readLine()
        inputStream.close()
    } catch (ex: Exception) {

    }

    if (result != null) {
        return JSONObject(result)
    }

    return null
}

