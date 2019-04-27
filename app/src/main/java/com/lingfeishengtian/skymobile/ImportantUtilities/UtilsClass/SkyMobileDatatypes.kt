package com.lingfeishengtian.skymobile.ImportantUtilities.UtilsClass

var Courses = mutableListOf<Course>()
var TermsAvailable = mutableListOf<String>()

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
    val name: String,
    var grade: Double
)

data class District(
    val name: String,
    val link: String,
    val GPACalculatorSupport: GPACalcSupport
)

enum class GPACalcSupport{
    HundredPoint, FourPoint, NoSupport
}