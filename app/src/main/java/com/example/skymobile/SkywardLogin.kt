package com.example.skymobile

import android.os.Bundle
import android.os.Message
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.*
import kotlinx.android.synthetic.main.activity_login.*
import android.webkit.WebSettings.PluginState
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent





class SkywardLogin : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loadWebView()
    }

    /** Called when the user taps the Send button */
    fun sendMessage(view: View) {
        val studentID = StudentID.text.toString()
        val password = Password.text.toString()
        println(studentID)
        println(password)
    }

    fun loadWebView() {
        Log.d("Debug", "LEADDDDDD")
        var cookieMan = CookieManager.getInstance()
        cookieMan.setAcceptCookie(true)
        val webView: WebView = WebView
        webView.getSettings().setSupportMultipleWindows(true);
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.loadUrl("https://skyward-fbprod.iscorp.com/scripts/wsisa.dll/WService=wsedufortbendtx/seplog01.w")
//        myWebView.evaluateJavascript("login.value = \"(UserName)\"; password.value = \"(Password)\"; bLogin.click();") {
//            val result = it
//            println(result)
//            //shouldOverrideUrlLoading(myWebView, result)
//        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
            ): Boolean {
                val newWebView = WebView(applicationContext)
                newWebView.settings.javaScriptEnabled = true
                newWebView.settings.domStorageEnabled = true
                newWebView.settings.setSupportZoom(true)
                newWebView.settings.builtInZoomControls = true
                newWebView.settings.setSupportMultipleWindows(true)
                view!!.addView(newWebView)
                val transport = resultMsg!!.obj as WebView.WebViewTransport
                transport.webView = newWebView
                resultMsg!!.sendToTarget()

                newWebView.webViewClient = WebViewClient()

                return true
            }
        }
    }
    }