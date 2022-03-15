package objects

import drawLines
import headerExtendInfo
import ncurses.printw
import screenY
import scroll

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
        if (drawLines >= screenY-1 + scroll) return
        if (drawLines >= scroll) {
            printw(headerTitle)
        }
        if(expanded) {
            if (drawLines >= scroll) {
                printw("\n")
            }
            drawLines++
            for(todo in listOfTODOs) {
                if (drawLines >= screenY-1 + scroll) return
                if (drawLines >= scroll) {
                    todo?.draw()
                    printw("\n")
                }
                drawLines++
            }
        } else {
            if (drawLines >= screenY-1 + scroll) return
            if (drawLines >= scroll) {
                if (listOfTODOs.isNotEmpty()) {
                    var newHeaderInfo = ""
                    var commandValue = ""
                    var command = false
                    var last = 'o'
                    for (i in headerExtendInfo.indices) {
                        if (headerExtendInfo[i] == '/' && last == '<') {
                            newHeaderInfo = newHeaderInfo.dropLast(1)
                            command = true
                        } else if (headerExtendInfo[i] == '>' && last == '/') {
                            command = false
                            commandValue = commandValue.dropLast(1)
                            commandValue = commandValue.drop(1)
                            when (commandValue) {
                                "todo" -> {
                                    newHeaderInfo += listOfTODOs.size.toString()
                                }
                                "done" -> {
                                    var n = 0
                                    for (todo in listOfTODOs) {
                                        if (todo?.state == State.DONE) {
                                            n++
                                        }
                                    }
                                    newHeaderInfo += n.toString()
                                }
                                "doing" -> {
                                    var n = 0
                                    for (todo in listOfTODOs) {
                                        if (todo?.state == State.DOING) {
                                            n++
                                        }
                                    }
                                    newHeaderInfo += n.toString()
                                }
                                "space" -> {
                                    newHeaderInfo += " "
                                }
                            }
                            commandValue = ""
                            continue
                        }

                        if (!command) {
                            newHeaderInfo += headerExtendInfo[i]
                        } else {
                            commandValue += headerExtendInfo[i]
                        }
                        last = headerExtendInfo[i]
                    }
                    printw(newHeaderInfo)
                }
                printw("\n")
            }
            drawLines++
        }
    }
}