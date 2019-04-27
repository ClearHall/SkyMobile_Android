package com.lingfeishengtian.skymobile.ViewControllers.GradesRelated

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TableRow
import com.lingfeishengtian.skymobile.ImportantUtilities.UtilsClass.Courses
import com.lingfeishengtian.skymobile.ImportantUtilities.UtilsClass.TermsAvailable
import com.lingfeishengtian.skymobile.R
import kotlinx.android.synthetic.main.progress_report_view.*
import android.widget.TextView

class ProgressReportViewController: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_report_view)

        TermsDropdownListInit()
        TableRefreshOrInit()
    }

    fun TermsDropdownListInit() {
        val NewArrayDropdownList = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            TermsAvailable
        )
        NewArrayDropdownList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectable_terms.adapter = NewArrayDropdownList

        selectable_terms.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                TableRefreshOrInit()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }
        }
    }

    fun TableRefreshOrInit(){
        var NeueRow: TableRow
        var Period: TextView
        var ClassDesc: TextView
        var Grade: TextView

        GradesTable.removeAllViews()

        for (Course in Courses) {
            NeueRow = TableRow(this)
            NeueRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT)
            NeueRow.setBackgroundResource(R.drawable.rounded_row)
            NeueRow.minimumHeight = 45
            Period = TextView(this)
            ClassDesc = TextView(this)
            Grade = TextView(this)
            Period.setText(Course.period)
            Period.setTextColor(Color.BLACK)
            ClassDesc.setText(Course.name)
            Grade.setText(Course.termGrades.get(selectable_terms.selectedItem.toString()))

            NeueRow.addView(Period)
            NeueRow.addView(ClassDesc)
            NeueRow.addView(Grade)
            GradesTable.addView(NeueRow)
        }
    }

}