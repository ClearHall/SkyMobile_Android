package com.lingfeishengtian.skymobile.ViewControllers.GradesRelated

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.progress_report_view.*
import android.widget.RelativeLayout
import com.lingfeishengtian.skymobile.ImportantUtilities.UtilsClass.*
import android.os.Handler
import android.util.Log
import com.lingfeishengtian.skymobile.R

class ProgressReportViewController: AppCompatActivity() {
    private val AssignmentVisibleCheckerInterval = 500
    private var AssignmentVisibleChecker: Handler? = null

    private var ClickedCourse: Course? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_report_view)

        TermsDropdownListInit()
        TableRefreshOrInit()

        AssignmentVisibleChecker = Handler()
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
//        //GradesTable.isStretchAllColumns = true
//        GradesTable.setColumnStretchable(2, true)
//        GradesTable.setColumnStretchable(1, false)
//        GradesTable.setColumnStretchable(0, false)

        for (Course in Courses) {
            NeueRow = TableRow(this)
            //NeueRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
            NeueRow.setBackgroundResource(R.drawable.rounded_row)
            NeueRow.minimumHeight = 45

            val LayoutOfTableRow = RelativeLayout(this)
            LayoutOfTableRow.minimumHeight = root_view.height/16
            LayoutOfTableRow.setBackgroundResource(R.drawable.rounded_row)
            LayoutOfTableRow.setPadding(15,10,15,10)
            /**
             * Required to init
             */
            Period = TextView(this)
            ClassDesc = TextView(this)
            Grade = TextView(this)

            val ArrOfViewsInTableRow = mutableListOf(Period, ClassDesc, Grade)
            for (View in ArrOfViewsInTableRow){
                View.maxLines = 1
                View.setTextColor(Color.BLACK)
                View.minimumHeight = root_view.height/16
                View.gravity = Gravity.FILL_VERTICAL or Gravity.CENTER_VERTICAL
                View.includeFontPadding = false
                View.setPadding(10,0,15,0)

                View.layoutParams
                LayoutOfTableRow.addView(View)
                (View.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.CENTER_IN_PARENT)
            }
            /**
             * Extra make look better stuffs
             */
            ClassDesc.width = GradesTable.width - (100 * 2)

            (ClassDesc.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.RIGHT_OF, Period.id)
            (ClassDesc.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.LEFT_OF, Grade.id)
            (ClassDesc.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.CENTER_IN_PARENT)
            (Period.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.ALIGN_PARENT_LEFT, ClassDesc.id)
            (Grade.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.ALIGN_PARENT_RIGHT, ClassDesc.id)
            (Grade.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.CENTER_VERTICAL, ClassDesc.id)
            (Grade.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.TEXT_ALIGNMENT_CENTER, ClassDesc.id)

            Period.width = 60
            Grade.width = 150

            Grade.gravity = Gravity.END or Gravity.CENTER_VERTICAL
            GapRow = TableRow(this)
            GapRow.minimumHeight = 10

            val GradeValue = Course.termGrades.get(selectable_terms.selectedItem.toString())

            val DrawableBackground = LayoutOfTableRow.background as GradientDrawable
            val GradeValueAsInt = GradeValue!!.toIntOrNull()
            if (GradeValueAsInt != null){
                val FinalColor = ColorGeneratingAlgorithmFromGrade(GradeValueAsInt.toDouble())
                DrawableBackground.setColor(FinalColor)
            }else{
                DrawableBackground.setColor(Color.argb(255,102, 153, 255))
            }
            NeueRow.setPadding(10,10,10,10)

            Period.setText(Course.period)
            ClassDesc.setText(Course.name)
            Grade.setText(GradeValue)

            LayoutOfTableRow.setOnClickListener {
                /**
                 * "Click it until you fucking make it" algorithm.
                 */
                Log.d("Course Details", "Clicked")
                if(GradeValueAsInt != null){
                    ClickProgressReportToShowGrades(Course, selectable_terms.selectedItem.toString())
                    //TODO: Loading icon...
                    ClickedCourse = Course
                    startRepeatingTask()
                }
            }

            GradesTable.addView(LayoutOfTableRow)
            GradesTable.addView(GapRow)
        }
    }
    private fun ClickProgressReportToShowGrades(course: Course, term: String){
        Log.d("Course Details", term)
        SkywardWebpage!!.evaluateJavascript("document.querySelectorAll(\"tr[group-parent]\")[${Courses.indexOf(course)}].querySelector(\"a[data-lit=\\\"$term\\\"]\").click();",null)
    }

    var AssignmentStatCheck: Runnable = object : Runnable {
        override fun run() {
            try {
                updateStatus()
            } finally {
                AssignmentVisibleChecker!!.postDelayed(this, AssignmentVisibleCheckerInterval.toLong())
            }
        }
    }

    fun updateStatus(){
        val JS = """
                        document.querySelector("#gradeInfoDialog").outerHTML
                    """.trimIndent()
        SkywardWebpage!!.evaluateJavascript(JS){
            if(it != "null") {
                Log.d("Course Details", it)
                stopRepeatingTask()
            }
        }
    }

    fun startRepeatingTask() {
        AssignmentStatCheck.run()
    }

    fun stopRepeatingTask() {
        AssignmentVisibleChecker!!.removeCallbacks(AssignmentStatCheck)
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