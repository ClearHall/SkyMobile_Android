package com.example.skymobile

data class Account(
    var nickname: String,
    val username: String,
    val password: String
)

data class Course(
    val name: String,
    var assignments: List<Assignment>,
    val period: Int,
    val teacher: String,
    var credits: Double = 1.0
)

data class Assignment(
    val name: String,
    var grade: Double
)