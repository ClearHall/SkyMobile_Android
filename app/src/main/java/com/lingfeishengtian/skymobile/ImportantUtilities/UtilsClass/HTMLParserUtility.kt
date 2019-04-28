package com.lingfeishengtian.skymobile.ImportantUtilities.UtilsClass

import org.jsoup.Jsoup

/**
 * A very *important* utility class.
 *
 * This class is a utility class used for parsing HTML and returning grades.
 * This class is very similar to the iOS version, in fact it is built from it!
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
        var NewCourses = mutableListOf<Course>()
        var Classes = mutableListOf<String>()
        val MainCSSSelectorCode = "tr[group-parent]"
        val FinalHTML = html.replace("\\u003C", "<")

        /**
         * HTML Parsing
         */
        val DocumentParsed = Jsoup.parse(FinalHTML)
        val ParentRowsFromParsedDocument = DocumentParsed.select(MainCSSSelectorCode)

        /**
         * Get Classes from parsed HTML
         */
        for(element in ParentRowsFromParsedDocument){
            val elementText = element.text()
            if(elementText.contains("Period")){
                Classes.add(elementText)
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
        NewCourses = splitClasses(Classes)
        for (elementIndex in 0 until NewCourses.size){
            val GradeElem = ParentRowsFromParsedDocument.get(elementIndex).select("td")

            var ind = 0
            for (tempElem in GradeElem){
                NewCourses.get(elementIndex).termGrades.put(gradesOut.get(ind), tempElem.text())
                ind++
            }
        }

        return NewCourses
    }

    /**
     * Helper function to split the class description.
     *
     * @param classArr Array of Class Descriptions
     */
    private fun splitClasses(classArr: MutableList<String>): MutableList<Course>{
        var FinalClasses = mutableListOf<Course>()

        for (ClassDesc in classArr){
            val SplitClassDesc = ClassDesc.split("\\n Period")
            val Period = SplitClassDesc[1].split(")\\n ")[0].split("(")[0]
            val ClassDescription = SplitClassDesc[0].split("\\n \\n \\n")[1]
            var Teacher = ""

            if (SplitClassDesc[1].split(")\\n").size <= 1){
                Teacher = SplitClassDesc[1].split("(")[0].split("\\n\\n\\n")[0]
            }else {
                Teacher = SplitClassDesc[1].split(")\\n ")[1].split("\\n\\n\\n")[0]
            }

            val CurrentCourse = Course(
                ClassDescription,
                mutableListOf(),
                Period,
                Teacher,
                1.0,
                mutableMapOf()
            )

            FinalClasses.add(CurrentCourse)
        }

        return FinalClasses
    }

/**
 *  Will use "iOS Ambiguous Assignments Scraper"
 *
 *  @param html Assignment popup html code.
 */
fun retrieveAssignmentsFromHtml(html: String){
}