package com.example.websample

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Parcel
import android.os.Parcelable
import android.widget.TextView
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

public class HttpGetTask(private val parentActivity: Activity, private val textView: TextView) :
    AsyncTask<Void, Void, String>() {

    //    private val uri: String = "https://www.google.com"
    private val uri: String = "https://www.yamagiwalab.jp/~yama/KPK/Hello.html"
    private lateinit var mDialog: ProgressDialog

    /*
    private lateinit var textView: TextView
    private lateinit var parentActivity: Activity

    fun HttpGetTask(_parentActivity: Activity, _textView: TextView) {
        parentActivity = _parentActivity
        textView = _textView
    }
    */
    override fun onPreExecute() {
        super.onPreExecute()
        mDialog = ProgressDialog(parentActivity)
        mDialog.setMessage("")
        mDialog.show()
    }

    override fun doInBackground(vararg p0: Void?): String {
        return exec_get()
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        mDialog.dismiss()
        textView.text = result
    }

    private fun exec_get(): String {
        var http: HttpURLConnection? = null
        var inputStream: InputStream? = null
        var src: String = ""

        try {
            val url: URL = URL(uri)
            http = url.openConnection() as HttpURLConnection?
            http!!.requestMethod = "GET"
            http.connect()

            src = http.inputStream.bufferedReader().readText()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                http?.disconnect()
                inputStream?.close()
            } catch (ignored: Exception) {

            }
        }
        return src
    }
}