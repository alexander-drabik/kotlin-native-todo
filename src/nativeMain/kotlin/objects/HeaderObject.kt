package objects

import drawLines
import ncurses.printw
import screenY

/* Object used for creating headers for checkboxes (TODOs). */
class HeaderObject {
    var headerTitle = ""
    var listOfTODOs: Array<TODOObject?> = Array(0) { TODOObject() }
    // If expanded is equal to true - TODOs will be visible.
    var expanded: Boolean = false

    fun addTODO(todoObject: TODOObject) {
        val newListOfTODOs: Array<TODOObject?> = listOfTODOs.copyOf(listOfTODOs.size + 1)
        newListOfTODOs[listOfTODOs.size] = todoObject
        listOfTODOs = newListOfTODOs
    }

    fun draw() {
        if (drawLines >= screenY-1) return
        printw(headerTitle)
        if(expanded) {
            printw("\n")
            drawLines++
            for(todo in listOfTODOs) {
                if (drawLines >= screenY-1) return
                todo?.draw()
                printw("\n")
                drawLines++
            }
        } else {
            if (drawLines >= screenY-1) return
            if(listOfTODOs.isNotEmpty()) {
                printw(" [...]")
            }
            printw("\n")
            drawLines++
        }
    }
}