package com.lingfeishengtian.skymobile.ImportantUtilities.UtilsClass

import org.jsoup.Jsoup

/**
 * A very *important* utility class.
 *
 * This class is a utility class used for parsing HTML and returning grades.
 * This class is very similar to the iOS version; in fact, it is modeled off it!
 */

   /**
     * Parse HTML and return the Courses scraped.
     *
    * Uses "iOS Terms And Progress Assumption"
    *
     * @param html The HTML scraped from the website.
     * @param gradesOut The terms our parameter.
     * @return Returns the courses from the HTML.
     */
    fun parseHtmlToRetrieveGrades(html: String, termsHTML: String, gradesOut:MutableList<String>): MutableList<Course> {
        /**
         * Pre-Variable Init
         */
        var newCourses: MutableList<Course>
        var classStoreArray = mutableListOf<String>()
        val mainCssCode = "tr[group-parent]"
        val modifiedHtmlCode = html.replace("\\u003C", "<")

        /**
         * HTML Parsing
         */
        val parsedDocument = Jsoup.parse(modifiedHtmlCode)
        val courseElementsByCssCode = parsedDocument.select(mainCssCode)

        /**
         * Get Classes from parsed HTML
         */
        for(element in courseElementsByCssCode){
            val elementText = element.text()
            if(elementText.contains("Period")){
                classStoreArray.add(elementText)
            }
        }

        /**
         * PreInit Grades and set terms correctly.
         */
        for (elem in termsHTML.split("@SWIFT_TERM_SEPARATOR@")){
            if (elem.isNotEmpty() && !elem.contains("'") && !elem.contains("\"")){
                gradesOut.add(elem)
            }
        }

        /**
         * Final init Grades and initialize Courses
         */
        newCourses = splitClasses(classStoreArray)
        for (elementIndex in 0 until newCourses.size){
            val gradeElement = courseElementsByCssCode[elementIndex].select("td")

            for ((ind, tempElem) in gradeElement.withIndex()){
                newCourses[elementIndex].termGrades[gradesOut[ind]] = tempElem.text()
            }
        }

        return newCourses
    }

    /**
     * Helper function to split the class description.
     *
     * @param classArr Array of Class Descriptions
     */
    private fun splitClasses(classArr: MutableList<String>): MutableList<Course>{
        var finalClassesList = mutableListOf<Course>()

        for (ClassDesc in classArr){
            val splitClassName = ClassDesc.split("\\n Period")
            val period = splitClassName[1].split(")\\n ")[0].split("(")[0]
            val className = splitClassName[0].split("\\n \\n \\n")[1]
            var teacher: String

            teacher = if (splitClassName[1].split(")\\n").size <= 1){
                splitClassName[1].split("(")[0].split("\\n\\n\\n")[0]
            }else {
                splitClassName[1].split(")\\n ")[1].split("\\n\\n\\n")[0]
            }

            val course = Course(
                className,
                mutableListOf(),
                period,
                teacher,
                1.0,
                mutableMapOf()
            )

            finalClassesList.add(course)
        }

        return finalClassesList
    }

/**
 *  Will use "iOS Ambiguous Assignments Scraper"
 *
 *  @param html Assignment popup html code.
 */
fun retrieveAssignmentsFromHtml(html: String) : AssignmentBlock{
    val finAssignmentBlock = AssignmentBlock(mutableListOf())

    val modifiedHtmlCode = html.replace("\\u003C", "<").replace("\\\\","")
    val parsedDocument = Jsoup.parseBodyFragment(modifiedHtmlCode)
    val finalGradesElements = parsedDocument.select("tbody").first().select("tbody").last()
    val combined = finalGradesElements.select("tr")

    var stillScrapingSections = false
    for(assignment in combined){
        var assignmentDescription = assignment.text()
        val separatedTdValues = assignment.select("td")

        if(assignment.outerHtml().contains("sf_Section")){
            var weightAttempt: String? = null
            var grade: String? = null

            if(separatedTdValues[1].text().contains("weighted at ")){
                weightAttempt = separatedTdValues[1].text().split("weighted at ").last().replace("%","").replace(")","")
                assignmentDescription = separatedTdValues[1].text().split("weighted at ").first().replace(" (","").trimEnd(' ')
            }

            for (elem in separatedTdValues){
                if (elem.text().toDoubleOrNull() != null){
                    grade = elem.text()
                }
            }

            if (!stillScrapingSections){
                finAssignmentBlock.AssignmentSections.add(MainAssignmentSection(assignmentDescription, weightAttempt, mutableListOf(), mutableListOf(), grade))
                stillScrapingSections = true
            }else{
                finAssignmentBlock.GetLatestMainSection().addMinorSection(MinorAssignmentSection(assignmentDescription, weightAttempt!!, grade!!))
            }
        }else{
            stillScrapingSections = false

            var grade = -1000.0
            var assignmentDescription = ""
            var finalAssignment: Assignment

            if(separatedTdValues.size <= 1){
                finalAssignment = Assignment("No grades here...", 0.0, AssignmentTag.IsInfoMarker)
            }else{
                for(elem in separatedTdValues){
                    if(elem.text().trimEnd(' ').toDoubleOrNull() != null){
                        grade = elem.text().trimEnd(' ').toDouble()
                    }
                }
                assignmentDescription = separatedTdValues[1].text()
                finalAssignment = Assignment(assignmentDescription, grade)
            }

            finAssignmentBlock.PutAssignmentInLastMainAssignmentSection(finalAssignment)
        }
    }

    return finAssignmentBlock
}