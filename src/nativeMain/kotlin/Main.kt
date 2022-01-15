
import ncurses.*
import objects.HeaderObject

// Root array of header objects, can be accessed from everywhere via 'import headerObjects'
var headerObjects: Array<HeaderObject?> =  Array(0) { HeaderObject() }

var running: Boolean = true

fun main() {
    initscr()

    while(running) {
        clear()

        refresh()

        val input = getch()
        if(input == Keyboard.ESCAPE.keycode) {
          close()
        }
    }

    endwin()
}

fun createHeaderObject(headerObject: HeaderObject) {
    val newHeaderObjects: Array<HeaderObject?> = headerObjects.copyOf(headerObjects.size+1)
    newHeaderObjects[headerObjects.size] = headerObject

    headerObjects = newHeaderObjects
}

fun close() {
    running = false
}