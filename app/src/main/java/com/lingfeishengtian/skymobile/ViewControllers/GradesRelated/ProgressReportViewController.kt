package com.lingfeishengtian.skymobile.ViewControllers.GradesRelated

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.LayoutDirection
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.lingfeishengtian.skymobile.ImportantUtilities.UtilsClass.Courses
import com.lingfeishengtian.skymobile.ImportantUtilities.UtilsClass.TermsAvailable
import kotlinx.android.synthetic.main.progress_report_view.*
import com.lingfeishengtian.skymobile.ImportantUtilities.UtilsClass.ColorGeneratingAlgorithmFromGrade
import com.lingfeishengtian.skymobile.R

class ProgressReportViewController: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_report_view)

        TermsDropdownListInit()
        TableRefreshOrInit()
    }

    private fun TermsDropdownListInit() {
        val NewArrayDropdownList = TermsArrayAdapter(this)
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

    private fun TableRefreshOrInit(){
        var NeueRow: TableRow
        var Period: TextView
        var ClassDesc: TextView
        var Grade: TextView
        var GapRow: TableRow

        GradesTable.removeAllViews()
        GradesTable.setPadding(15,10,15,10)
        //GradesTable.isStretchAllColumns = true
        GradesTable.setColumnStretchable(2, true)
        GradesTable.setColumnStretchable(1, true)
        GradesTable.setColumnStretchable(0, false)

        for (Course in Courses) {
            NeueRow = TableRow(this)
            NeueRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT)
            NeueRow.setBackgroundResource(R.drawable.rounded_row)
            NeueRow.minimumHeight = 45

            /**
             * Required to init
             */
            Period = TextView(this)
            ClassDesc = TextView(this)
            Grade = TextView(this)

            val ArrOfViewsInTableRow = mutableListOf<TextView>(Period, ClassDesc, Grade)
            for (View in ArrOfViewsInTableRow){
                View.setTextColor(Color.BLACK)
                View.height = 70
                View.gravity = Gravity.CENTER or Gravity.LEFT
                View.setPadding(5,0,0,0)

                NeueRow.addView(View)
            }

            /**
             * Extra make look better stuffs
             */
            //Grade.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)

            Grade.textAlignment = TextView.TEXT_ALIGNMENT_VIEW_END
            Grade.gravity = Gravity.END
            GapRow = TableRow(this)
            GapRow.minimumHeight = 10

            val GradeValue = Course.termGrades.get(selectable_terms.selectedItem.toString())

            val DrawableBackground = NeueRow.background as GradientDrawable
            val GradeValueAsInt = GradeValue!!.toIntOrNull()
            if (GradeValueAsInt != null){
                DrawableBackground.setColor(ColorGeneratingAlgorithmFromGrade(GradeValueAsInt.toDouble()))
            }else{
                DrawableBackground.setColor(Color.argb(255,102, 153, 255))
            }
            NeueRow.setPadding(10,10,10,10)

            Period.setText(Course.period)
            ClassDesc.setText(Course.name)
            Grade.setText(GradeValue)

            GradesTable.addView(NeueRow)
            GradesTable.addView(GapRow)
        }
    }

}

private class TermsArrayAdapter(context: Context) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, TermsAvailable){
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)

        view.setBackgroundResource(R.drawable.rounded_row)
        view.setBackgroundColor(Color.BLACK)
        (view as TextView).setTextColor(Color.WHITE)
        view.gravity = Gravity.CENTER

        return view
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)

        (view as TextView).textSize = 20f
        view.setTextColor(Color.WHITE)

        return view
    }
}