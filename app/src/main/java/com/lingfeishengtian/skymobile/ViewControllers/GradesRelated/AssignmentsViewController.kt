package com.lingfeishengtian.skymobile.ViewControllers.GradesRelated

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView
import com.lingfeishengtian.skymobile.ImportantUtilities.UtilsClass.ColorGeneratingAlgorithmFromGrade
import com.lingfeishengtian.skymobile.ImportantUtilities.UtilsClass.CurrentAssignmentBlocks
import com.lingfeishengtian.skymobile.R
import kotlinx.android.synthetic.main.assignments_view.*

class AssignmentsViewController : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.assignments_view)

        TableRefreshOrInit()
    }

    private fun TableRefreshOrInit(){
        AssignmentsTableView.removeAllViews()
        AssignmentsTableView.setPadding(15,10,15,10)
        AssignmentsTableView.setColumnStretchable(1, true)
        AssignmentsTableView.setColumnStretchable(0, true)

        for (Assignment in CurrentAssignmentBlocks!!.AssignmentSections) {
            var Grade = ""
            if (Assignment.grade != null){
                Grade = Assignment.grade!!
            }
            val display = getWindowManager().getDefaultDisplay()
            val size = Point()
            display.getSize(size)
            val Row = makeNewRowFromNameAndGrade(Assignment.name, Grade, Assignment.weightIfApplicable)
            Row.minimumHeight = size.y / 12
            AssignmentsTableView.addView(Row)
            Row.gravity = Gravity.CENTER_VERTICAL

            for (MinorSection in Assignment.MinorSections){
                val GapRow = TableRow(this)
                GapRow.minimumHeight = 10

                val temp = (makeNewRowFromNameAndGrade(MinorSection.name, MinorSection.grade, MinorSection.weight))
                temp.minimumHeight = size.y/16
                temp.gravity = Gravity.CENTER_VERTICAL
                AssignmentsTableView.addView(temp)
            }

            for (AssignmentSection in Assignment.AssignmentList){

                AssignmentsTableView.addView(makeNewRowFromNameAndGrade(AssignmentSection.name, AssignmentSection.grade.toString()))
            }
        }
    }

    private fun makeNewRowFromNameAndGrade(name: String, grade: String, weight: String? = null): TableRow{
        var NeueRow = TableRow(this)
        NeueRow.setBackgroundResource(R.drawable.rounded_row)

        val AssignmentName = TextView(this)
        val Grade = TextView(this)

        val display = getWindowManager().getDefaultDisplay()
        val size = Point()
        display.getSize(size)

        val ArrOfViewsInTableRow = mutableListOf(AssignmentName, Grade)
        for (View in ArrOfViewsInTableRow){
            View.setTextColor(Color.BLACK)
            View.minimumHeight = size.y / 20
            View.maxHeight = size.y / 18
            View.setSingleLine()
            View.textSize = 20F
            View.includeFontPadding = false
            View.setPadding(10,0,15,0)

            NeueRow.addView(View)
        }
        val DrawableBackground = NeueRow.background as GradientDrawable
        val GradeValueAsInt = grade!!.toDoubleOrNull()
        if (GradeValueAsInt != null && GradeValueAsInt != -1000.0){
            val FinalColor = ColorGeneratingAlgorithmFromGrade(GradeValueAsInt.toDouble())
            DrawableBackground.setColor(FinalColor)
        }else{
            DrawableBackground.setColor(Color.argb(255,102, 153, 255))
        }

        AssignmentName.text = (name)
        if (!grade.contains("-1000")) {
            Grade.text = grade
        }
        if (weight != null){
            AssignmentName.text = "${AssignmentName.text} weighted at $weight%"
        }
        AssignmentName.maxWidth = size.x - 230
        AssignmentName.gravity = Gravity.CENTER_VERTICAL
        Grade.gravity = Gravity.CENTER_VERTICAL or Gravity.END

        val GapRow = TableRow(this)
        GapRow.minimumHeight = 10

        AssignmentsTableView.addView(GapRow)
        return (NeueRow)
    }

}