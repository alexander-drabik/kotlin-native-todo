
import ncurses.KEY_DOWN
import ncurses.KEY_LEFT
import ncurses.KEY_RIGHT
import ncurses.KEY_UP
import kotlin.math.max

class KeyboardEvents {
    fun typeEvent(keycode: Int) {
        when(keycode) {
            Keyboard.E.keycode -> {
                close()
            }
            KEY_UP   -> y = max(y-1, 0)
            KEY_DOWN -> y++
            KEY_LEFT -> x = max(x-1, 0)
            KEY_RIGHT-> x++
        }
    }
}

// Enum for keycodes that ncurses doesn't offer
enum class Keyboard(val keycode: Int) {
    E(101)
}