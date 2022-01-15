package objects

import ncurses.printw

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
        printw(stateToString() + text)
    }
}