package com.lingfeishengtian.skymobile.ViewControllers.GradesRelated

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Toast
import com.lingfeishengtian.skymobile.ImportantUtilities.UtilsClass.*
import com.lingfeishengtian.skymobile.R
import kotlinx.android.synthetic.main.activity_login.*

class SkywardLogin : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        basicAlert()

        loadWebView()
    }

    /** Called when the user taps the Send button */
    fun sendMessage(view: View) {
        val studentID = StudentID.text.toString()
        val password = Password.text.toString()

        webView.evaluateJavascript("""
                 login.value = "$studentID"; password.value = "$password"; bLogin.click();
            """.trimIndent()){
                print(it)
            }
    }

    fun basicAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Hey There!")
        builder.setMessage("This is an ALPHA version of SkyMobile, right now the application is just a demo to show what is to expect in the future. There may be bugs present and it DOES NOT look that good. But enjoy the demo!")

        builder.setPositiveButton(android.R.string.ok) { dialog, which ->
            Toast.makeText(applicationContext,
                android.R.string.yes, Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

    fun loadWebView() {
        var cookieMan = CookieManager.getInstance()
        cookieMan.setAcceptCookie(true)
        webView.getSettings().setSupportMultipleWindows(true);
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.loadUrl("https://skyward-fbprod.iscorp.com/scripts/wsisa.dll/WService=wsedufortbendtx/seplog01.w")
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
                val transport = resultMsg!!.obj as WebView.WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()

                newWebView.webViewClient = object : WebViewClient(){
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)

                        HandleNewWebpageReached(view!!, url!!)
                    }
                }

                SkywardWebpage = newWebView

                return true
            }
        }
    }

    fun HandleNewWebpageReached(webView: WebView, url: String){
        if (url.contains("sfhome01.w"))
            webView.evaluateJavascript("document.querySelector('a[data-nav=\\\"sfgradebook001.w\\\"]').click()", null)
        else if (url.contains("sfgradebook001.w")){
            val ScrapingJavascript = """
            function PrintTerms(){
             var FinalString = ""
             if (typeof sf_StudentList !== "undefined"){
            FinalString = FinalString + sf_StudentList.innerHTML
             }else{
             FinalString = FinalString + "@SWIFT_VALUE_DOES_NOT_EXIST"
             }
            FinalString = FinalString + "\n\n\n@SWIFT_DETERMINE_IF_STUDENT_LIST_EXIST\n\n\n"
            if (document.querySelector("div[id^=\"grid_stuGradesGrid\"]") != null){
            FinalString = FinalString + document.querySelector("div[id^=\"grid_stuGradesGrid\"]").innerHTML + "@SWIFT_HTML&TERMS_SEPARATION@"
            let elems = document.querySelector("div[id^=\"grid_stuGradesGrid\"]").querySelector("table[id*=\"grid_stuGradesGrid\"]").querySelectorAll("th")
            for(var i = 0; i < elems.length; i++){
                FinalString = FinalString + elems[i].textContent + "@SWIFT_TERM_SEPARATOR@"
            }
            }
            return FinalString
            }
            PrintTerms()
            """.trimIndent()
            webView.evaluateJavascript(ScrapingJavascript){
                HandleGradeBookHTML(it)
                val ProgressReportViewIntent = Intent(this, ProgressReportViewController::class.java)
                startActivity(ProgressReportViewIntent)
            }

        }
    }

    fun HandleGradeBookHTML(html: String){
        val SplitString = html.split("@SWIFT_DETERMINE_IF_STUDENT_LIST_EXIST")
        val returnedResults = SplitString.last()
        val ListedStudents = SplitString.first()
        val SeperatedTerms = returnedResults.split("@SWIFT_HTML&TERMS_SEPARATION@")
        val HTML = SeperatedTerms.first()
        val Terms = SeperatedTerms.last()
        var GradingTerms = mutableListOf<String>()

        Courses = ParseGradesHTMLToRetrieveGrades(HTML, Terms, GradingTerms)
        TermsAvailable = GradingTerms

        PrintCoursesToLog("Courses Details", Courses)
    }

    fun PrintCoursesToLog(tag: String, courses: MutableList<Course>){
        for (course in courses){
            Log.d(tag, "${course.name} is initialized as ${course.period} period and has a teacher ${course.teacher} and has grades of ${course.termGrades}")
        }
    }
}

