package com.lingfeishengtian.skymobile.ViewControllers.GradesRelated

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import com.lingfeishengtian.skymobile.ImportantUtilities.UtilsClass.Courses
import com.lingfeishengtian.skymobile.ImportantUtilities.UtilsClass.TermsAvailable
import com.lingfeishengtian.skymobile.R
import kotlinx.android.synthetic.main.progress_report_view.*

class ProgressReportViewController: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_report_view)

        Log.d("Courses Details", Courses.toString())

        val NewArrayDropdownList = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            TermsAvailable)
        NewArrayDropdownList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectable_terms.adapter = NewArrayDropdownList
    }

}