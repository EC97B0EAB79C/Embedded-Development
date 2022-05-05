package com.example.websample

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.graphics.Color
import android.os.AsyncTask
import android.os.Parcel
import android.os.Parcelable
import android.os.SystemClock
import android.os.SystemClock.sleep
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class HttpGetTask(private val parentActivity: Activity, private val textView: TextView) :
    ViewModel() {
    companion object {
        private val uri: String = "https://www.yamagiwalab.jp/~yama/KPK/Hello.html"
        //private val uri: String = "https://www.google.com"
    }

    private var src: String = ""
    private lateinit var mDialog: ProgressDialog

    fun getWeb() {
        viewModelScope.launch {
            /*
            mDialog = ProgressDialog(parentActivity)
            mDialog.setMessage("")
            mDialog.show()
            textView.text = getHttp()
            mDialog.dismiss()

             */

            val progressBar = ProgressBar(parentActivity)
            progressBar.isIndeterminate = true
            progressBar.setPadding(32)

            val builder: AlertDialog.Builder = AlertDialog.Builder(parentActivity)
            builder.setView(progressBar)

            val dialog: AlertDialog = builder.create()
            dialog.show()
            dialog.window?.setLayout(300,300)
            dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            parentActivity.window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            textView.text = getHttp()
            dialog.hide()
            parentActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    private suspend fun getHttp(): String {
        var src: String = ""
        withContext(Dispatchers.IO) {
            sleep(5000)
            try {
                var http: HttpURLConnection? = null
                var inputStream: InputStream? = null

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
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return src
    }

}

/*
public class HttpGetTask(private val parentActivity: Activity, private val textView: TextView) :
    AsyncTask<Void, Void, String>() {

    private val uri: String = "https://www.yamagiwalab.jp/~yama/KPK/Hello.html"
    private lateinit var mDialog: ProgressDialog

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
*/
