package com.example.skymobile

class Course constructor(className: String, period: Int, teacher: String){
    val ClassName = className
    var Grades = mutableMapOf<String, String>()
    val Period = period
    val Teacher = teacher
    var Credits = 1.0
}

class Assignment constructor(className: String, assignmentName: String, grade: Double){
    val Class = className
    val Assignment = assignmentName
    var Grade = grade
}

class Account constructor(nick: String, user: String, password: String){
    var Nickname = nick
    val Username = user
    val Password = password
}