package com.tkbaze.raspberrypiledsample

import android.app.Activity
import android.app.ProgressDialog
import android.os.AsyncTask
import android.util.Log
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class RaspberryPiLedHttpGetTask(private val parentActivity: Activity) :
    AsyncTask<Int, Void, Void>() {

    private var _dialog: ProgressDialog? = null
    private val dialog:ProgressDialog get()=_dialog!!

    private val DEFAULTURL = "http://192.168.144.59/~pi/ledtest.php?"
    private var uri: String = ""

    override fun doInBackground(vararg p0: Int?): Void? {
        uri=DEFAULTURL+String.format("num=%d&stat=%d",p0[0],p0[1])
        Log.d("pasPiLED",uri)
        exec_get()
        return null
    }

    override fun onPreExecute() {
        _dialog = ProgressDialog(parentActivity)
        dialog.setMessage("Sending...")
        dialog.show()
    }

    override fun onPostExecute(result: Void?) {
        dialog.dismiss()
    }

    private fun exec_get():String{
        var http:HttpURLConnection?=null
        var inputStream:InputStream?=null
        var src:String=""

        try {
            val url: URL =URL(uri)
            http=url.openConnection()as HttpURLConnection
            http.requestMethod="GET"
            http.connect()

            inputStream=http.inputStream
            var line=ByteArray(1024)
            var size:Int;
            while (true){
                size=inputStream.read(line)
                if(size<=0)break
                src+=line.toString()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            try {
                http?.disconnect()
                inputStream?.close()
            }catch (e:Exception){

            }
        }
        return src
    }
}