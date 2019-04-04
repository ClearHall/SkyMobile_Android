package com.example.skymobile

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_login.*

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
        val myWebView: WebView = WebView
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.setSupportMultipleWindows(true)
        myWebView.settings.javaScriptCanOpenWindowsAutomatically = true
        myWebView.loadUrl("https://skyward-fbprod.iscorp.com/scripts/wsisa.dll/WService=wsedufortbendtx/seplog01.w")
        myWebView.evaluateJavascript("login.value = \"(UserName)\"; password.value = \"(Password)\"; bLogin.click();") {
            val result = it
            println(result)
            //shouldOverrideUrlLoading(myWebView, result)
        }
        fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }

}