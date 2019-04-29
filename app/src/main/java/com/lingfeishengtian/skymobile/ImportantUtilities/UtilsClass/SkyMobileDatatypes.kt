package com.lingfeishengtian.skymobile.ImportantUtilities.UtilsClass

import android.webkit.WebView

var Courses = mutableListOf<Course>()
var TermsAvailable = mutableListOf<String>()
var SkywardWebpage: WebView? = null
var CurrentAssignmentBlocks: AssignmentBlock? = null

data class Account(
    var nickname: String,
    val username: String,
    val password: String
)

data class Course(
    val name: String,
    var assignments: MutableList<Assignment>,
    val period: String,
    val teacher: String,
    var credits: Double = 1.0,
    var termGrades: MutableMap<String, String>
)

data class Assignment(
    var name: String,
    var grade: Double,
    var assignmentTag: AssignmentTag = AssignmentTag.IsAssignment
){
    override fun toString(): String {
        return " Assignment: $name Grade: $grade"
    }
}

data class AssignmentBlock(
    val AssignmentSections: MutableList<MainAssignmentSection>
){
    fun PutAssignmentInLastMainAssignmentSection(assignment: Assignment){
        AssignmentSections.last().AssignmentList.add(assignment)
    }

    fun GetLatestMainSection() : MainAssignmentSection{
        return AssignmentSections.last()
    }

    fun GetWeightInfo() : MutableMap<String, String>{
        val finMap = mutableMapOf<String,String>()
        for (assignmentSection in AssignmentSections){
            if(assignmentSection.weightIfApplicable != null){
                finMap[assignmentSection.name] = assignmentSection.weightIfApplicable!!
            }else{
                for(minorSection in assignmentSection.MinorSections){
                    finMap["${assignmentSection.name} : ${minorSection.weight}"]
                }
            }
        }
        return finMap
    }

    override fun toString(): String {
        var tempString = ""
        for (assignmentSec in AssignmentSections) tempString += " $assignmentSec"
        return tempString
    }
}

/**
 * ALl assignments will be put under the last MAIN section created.
 */
data class MainAssignmentSection(
    val name: String,
    val weightIfApplicable: String?,
    val MinorSections: MutableList<MinorAssignmentSection>,
    val AssignmentList: MutableList<Assignment>,
    val grade: String?
){
    fun addMinorSection(minorAssignmentSection: MinorAssignmentSection){
        MinorSections.add(minorAssignmentSection)
    }

    override fun toString(): String {
        var tempString = "MAIN: $name $weightIfApplicable $grade "
        for (minorSec in MinorSections) tempString += " $minorSec"
        for (aS in AssignmentList) tempString += " $aS"
        return tempString
    }
}

data class MinorAssignmentSection(
    val name: String,
    val weight: String,
    val grade: String
){
    override fun toString(): String {
        return " Minor: $name $weight $grade"
    }
}

data class District(
    val name: String,
    val link: String,
    val GPACalculatorSupport: GPACalcSupport
)

enum class GPACalcSupport{
    HundredPoint, FourPoint, NoSupport
}

enum class AssignmentTag{
    IsAssignment, IsInfoMarker
}