
import ncurses.*
import objects.HeaderObject

// Root array of header objects, can be accessed from everywhere via 'import headerObjects'
var headerObjects: Array<HeaderObject?> =  Array(0) { HeaderObject() }

var running: Boolean = true

var x: Int = 0
var y: Int = 0

fun main() {
    loadConfig()
    loadFromFile()
    val keyboard = KeyboardEvents()

    initscr()
    raw()
    keypad(stdscr, true)
    noecho()

    while(running) {
        erase()

        for(headerObject in headerObjects) {
            headerObject?.draw()
        }

        move(y, x)

        refresh()
        keyboard.typeEvent(getch())
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

fun verificationAsk(text: String): Boolean {
    while(true) {
        erase()

        printw(text)

        refresh()

        val input = getch()
        when(input.toChar()) {
            'y' -> return true
            'n' -> return false
        }
    }
}