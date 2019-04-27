package com.lingfeishengtian.skymobile.ImportantUtilities.UtilsClass

import android.graphics.Color

/**
 * *Color Generating for Better Visibility*
 *
 * This algorithm generates a color from your grade.
 * A - Green
 * B - Yellow
 * C - Orange Yellow
 * F - Red
 * Below - *RED*
 *
 * @param grade The grade you want the algorithm to generate the grade from.
 */
fun ColorGeneratingAlgorithmFromGrade(grade: Double): Int{
    val GreenColor = grade * 2.55/255
    val RedColor = (1-GreenColor) * 6

    return Color.argb(255, (RedColor * 255).toInt(), (GreenColor*255).toInt(), 0)
}