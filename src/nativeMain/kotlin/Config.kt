import ncurses.*

var spaces: Int = 1
var indentSpaces: Int = 3
var up: Int = KEY_UP
var down: Int = KEY_DOWN
var left: Int = KEY_LEFT
var right: Int = KEY_RIGHT
var use: Int = Keyboard.ENTER.keycode
var new: Int = Keyboard.INSERT.keycode
var edit: Int = Keyboard.E.keycode
var remove: Int = KEY_BACKSPACE
var exit: Int = Keyboard.Q.keycode
var headerExtendInfo: String = "[...]"
var pointer = false

fun toKey(value: String?): Int {
    when (value) {
        "TAB" -> return Keyboard.TAB.keycode
        "ENTER" -> return Keyboard.ENTER.keycode
        "BACKSPACE" -> return KEY_BACKSPACE
        "SPACE" -> return 32
        "INSERT" -> return Keyboard.INSERT.keycode
    }
    return value!![0].lowercaseChar().code
}