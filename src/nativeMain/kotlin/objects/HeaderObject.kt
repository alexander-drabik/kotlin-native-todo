package objects

import ncurses.printw

/* Object used for creating headers for checkboxes (TODOs). */
class HeaderObject {
    var headerTitle = ""
    var listOfTODOs: Array<TODOObject?> = Array(0) { TODOObject() }
    // If expanded is equal to true - TODOs will be visible.
    var expanded: Boolean = true

    fun addTODO(todoObject: TODOObject) {
        val newListOfTODOs: Array<TODOObject?> = listOfTODOs.copyOf(listOfTODOs.size + 1)
        newListOfTODOs[listOfTODOs.size] = todoObject
        listOfTODOs = newListOfTODOs
    }

    fun draw() {
        printw(headerTitle + "\n")
        if(expanded) {
            for(todo in listOfTODOs) {
                todo?.draw()
                printw("\n")
            }
        }
    }
}