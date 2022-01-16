package objects

import indentSpaces
import ncurses.printw
import spaces

class TODOObject {
    var text = ""
    var state: State = State.TODO
    fun stateToString(): String {
        return when(state) {
            State.TODO  -> "[ ]"
            State.DOING -> "[-]"
            State.DONE  -> "[X]"
        }
    }

    fun draw() {
        for(i in 0 until indentSpaces) {
            printw(" ")
        }
        printw(stateToString())
        for(i in 0 until spaces) {
            printw(" ")
        }
        printw(text)
    }
}