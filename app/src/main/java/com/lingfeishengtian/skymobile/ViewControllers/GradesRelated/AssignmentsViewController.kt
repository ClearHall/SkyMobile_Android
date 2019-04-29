package com.lingfeishengtian.skymobile.ViewControllers.GradesRelated

import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.RelativeLayout
import android.widget.TableRow
import android.widget.TextView
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

        for (Assignment in CurrentAssignmentBlocks!!.AssignmentSections) {
            var Grade = ""
            if (Assignment.grade != null){
                Grade = Assignment.grade!!
            }
            val display = getWindowManager().getDefaultDisplay()
            val size = Point()
            display.getSize(size)
            val Row = makeNewRowFromNameAndGrade(Assignment.name, Grade)
            Row.minimumHeight = size.y / 14
            AssignmentsTableView.addView(Row)
            Row.gravity = Gravity.CENTER_VERTICAL

            for (MainSections in Assignment.MinorSections){
                val GapRow = TableRow(this)
                GapRow.minimumHeight = 10

                AssignmentsTableView.addView(makeNewRowFromNameAndGrade(MainSections.name, MainSections.grade))
            }

            for (AssignmentSection in Assignment.AssignmentList){
                val GapRow = TableRow(this)
                GapRow.minimumHeight = 10

                AssignmentsTableView.addView(makeNewRowFromNameAndGrade(AssignmentSection.name, AssignmentSection.grade.toString()))
            }
        }
    }

    private fun makeNewRowFromNameAndGrade(name: String, grade: String): TableRow{
        var NeueRow = TableRow(this)
        NeueRow.setBackgroundResource(R.drawable.rounded_row)
        NeueRow.minimumHeight = root_view.height/2

        val AssignmentName = TextView(this)
        val Grade = TextView(this)

        val display = getWindowManager().getDefaultDisplay()
        val size = Point()
        display.getSize(size)

        val ArrOfViewsInTableRow = mutableListOf(AssignmentName, Grade)
        for (View in ArrOfViewsInTableRow){
            View.setTextColor(Color.BLACK)
            View.minimumHeight = size.y / 20
            View.textSize = 20F
            View.includeFontPadding = false
            View.setPadding(10,0,15,0)

            NeueRow.addView(View)
        }

        AssignmentName.text = (name)
        Grade.text = grade
        AssignmentName.gravity = Gravity.CENTER_VERTICAL
        Grade.gravity = Gravity.CENTER_VERTICAL or Gravity.END

        return (NeueRow)
    }

}