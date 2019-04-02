package com.example.skymobile

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_login.*

const val EXTRA_MESSAGE = "com.example.SkyMobile.MESSAGE"

class SkywardLogin : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    /** Called when the user taps the Send button */
    fun sendMessage(view: View) {
        val studentID = StudentID.text.toString()
        println(studentID)
    }

}