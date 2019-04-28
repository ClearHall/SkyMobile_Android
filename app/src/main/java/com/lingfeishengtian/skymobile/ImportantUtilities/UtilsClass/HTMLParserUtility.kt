package com.lingfeishengtian.skymobile.ImportantUtilities.UtilsClass

import org.jsoup.Jsoup

/**
 * A very important utility class.
 *
 * This class is a utility class used for parsing HTML and returning grades.
 * This class is very similar to the iOS version; in fact, it is modeled off it!
 *
 */

   /**
     * Parse HTML and return the Courses scraped.
     *
     * @param html The HTML scraped from the website.
     * @param gradesOut The terms our parameter.
     * @return Returns the courses from the HTML.
     */
    fun retrieveGrades(html: String, termsHTML: String, gradesOut:MutableList<String>): MutableList<Course> {
       /** Pre-Variable Init */
        val newCourses: MutableList<Course>
        val classes = mutableListOf<String>()
        val cssSelectorCode = "tr[group-parent]"
        val finalHTML = html.replace("\\u003C", "<")

        /** HTML Parsing */
        val document = Jsoup.parse(finalHTML)
        val parentRows = document.select(cssSelectorCode)

        /** Get Classes from parsed HTML */
        for (element in parentRows){
            val elementText = element.text()
            if(elementText.contains("Period")){
                classes.add(elementText)
            }
        }

        /** PreInit Grades and set terms correctly. */
        for (elem in termsHTML.split("@SWIFT_TERM_SEPARATOR@")){
            if (elem.isNotEmpty() && !elem.contains("'") && !elem.contains("\"")){
                gradesOut.add(elem)
            }
        }

        /** Final init Grades and initialize Courses */
        newCourses = splitClassDescriptions(classes)
        for (elementIndex in 0 until newCourses.size){
            val gradeElem = parentRows.get(elementIndex).select("td")

            for ((ind, tempElem) in gradeElem.withIndex())
                newCourses[elementIndex].termGrades[gradesOut[ind]] = tempElem.text()
        }

        return newCourses
    }

    /**
     * Helper function to split the class description.
     *
     * @param classArr Array of Class Descriptions
     */
    private fun splitClassDescriptions(classArr: MutableList<String>): MutableList<Course>{
        val finalClasses = mutableListOf<Course>()

        for (ClassDesc in classArr){
            val splitClassDesc = ClassDesc.split("\\n Period")
            val period = splitClassDesc[1].split(")\\n ")[0].split("(")[0]
            val description = splitClassDesc[0].split("\\n \\n \\n")[1]
            val teacher = if (splitClassDesc[1].split(")\\n").size <= 1)
                splitClassDesc[1].split("(")[0].split("\\n\\n\\n")[0]
            else
                splitClassDesc[1].split(")\\n ")[1].split("\\n\\n\\n")[0]

            val course = Course(
                description,
                mutableListOf(),
                period,
                teacher,
                1.0,
                mutableMapOf()
            )

            finalClasses.add(course)
        }

        return finalClasses
    }
