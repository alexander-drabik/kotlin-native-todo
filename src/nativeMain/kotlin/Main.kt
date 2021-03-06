import ncurses.*
import objects.HeaderObject
import objects.PointerInfo

// Root array of header objects, can be accessed from everywhere via 'import headerObjects'
var headerObjects: Array<HeaderObject> =  Array(0) { HeaderObject() }

var running: Boolean = true

var x: Int = 0
var y: Int = 0
var drawLines: Int = 0

var screenY: Int = 0
var scroll: Int = 0

fun main() {
    loadConfig()
    loadFromFile()
    val keyboard = KeyboardEvents()

    initscr()
    raw()
    keypad(stdscr, true)
    noecho()

    val pointerInfo = PointerInfo()
    while (running) {
        erase()

        screenY = getmaxy(stdscr)
        if (y >= screenY + scroll) {
            scroll++
        } else if (y < scroll) {
            scroll--
        }

        draw(pointerInfo)

        move(y - scroll, x)

        refresh()
        keyboard.typeEvent(getch())
    }

    endwin()
}

fun draw(pointerInfo: PointerInfo) {
    drawLines = 0
    for (headerObject in headerObjects)
        headerObject.draw()
    if (pointer)
        pointerInfo.draw(y)
}

fun createHeaderObject(headerObject: HeaderObject) {
    val newHeaderObjects: Array<HeaderObject?> = headerObjects.copyOf(headerObjects.size+1)
    newHeaderObjects[headerObjects.size] = headerObject

    headerObjects = newHeaderObjects.filterNotNull().toTypedArray()
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