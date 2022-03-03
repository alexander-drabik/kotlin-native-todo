import ncurses.*
import objects.HeaderObject
import objects.ObjectType
import objects.State
import objects.TODOObject
import kotlin.math.max

class KeyboardEvents {
    fun typeEvent(keycode: Int) {
        when(keycode) {
            exit -> { // Exit the application
                close()
                saveToFile()
            }
            use -> { // Interact with selected line
                when (getLineTypeAt(y)) {
                    ObjectType.Header -> { // Expand Headers
                        val id = getHeaderIdAt(y)
                        headerObjects[id].expanded = !headerObjects[id].expanded
                        saveToFile()
                    }
                    ObjectType.Todo -> { // Change state od TodoObjects
                        val todoID = getTodoIdAt(y)[0]
                        val headerID = getTodoIdAt(y)[1]
                        headerObjects[headerID].listOfTODOs[todoID]?.next()
                        saveToFile()
                    }
                    else -> {} // Compiler is angry at me when I don't do that, just ignore this line
                }
            }
            edit -> { // Edit text at selected line
                when (getLineTypeAt(y)) {
                    ObjectType.Header -> {
                        val id = getHeaderIdAt(y)
                        headerObjects[id].headerTitle = editText(headerObjects[id].headerTitle, "Editing '${headerObjects[id].headerTitle}'")
                    }
                    ObjectType.Todo -> {
                        val todoID = getTodoIdAt(y)[0]
                        val headerID = getTodoIdAt(y)[1]
                        val text = headerObjects[headerID].listOfTODOs[todoID]!!.text
                        headerObjects[headerID].listOfTODOs[todoID]!!.text = editText(text, "Editing '${text}'")
                    }
                    else -> {}
                }
            }

            new -> { // Create new object
                when (getLineTypeAt(y)) {
                    ObjectType.Header, ObjectType.Todo -> { // If HeaderObject or TodoObject is selected create new TodoObject (might change in future when adding children todos)
                        val newTodo = TODOObject()
                        newTodo.text = editText("", "Creating new todo checkbox: ")
                        newTodo.state = State.TODO
                        headerObjects[getHeaderIdAt(y)].addTODO(newTodo)
                        saveToFile()
                    }
                    else -> { // If empty line is selected create new HeaderObject
                        val newHeaderObject = HeaderObject()
                        newHeaderObject.headerTitle = editText("", "Creating new todo checkbox: ")
                        createHeaderObject(newHeaderObject)
                    }
                }
            }

            remove -> {
                when (getLineTypeAt(y)) {
                    ObjectType.Header -> {
                        val headerID = getHeaderIdAt(y)
                        if(verificationAsk("Are you sure you want to delete '${headerObjects[headerID].headerTitle}'? y/n")) {
                            val newList: Array<HeaderObject?> = Array(headerObjects.size-1) { HeaderObject() }
                            var a = 0
                            for (l in headerObjects.indices) {
                                val newHeaderObject = headerObjects[l]
                                if (l == headerID) {
                                    saveToFile()
                                    continue
                                }
                                newList[a] = newHeaderObject
                                a++
                            }
                            headerObjects = newList.filterNotNull().toTypedArray()
                        }
                    }
                    ObjectType.Todo -> {
                        val todoID = getTodoIdAt(y)[0]
                        val headerID = getTodoIdAt(y)[1]
                        if (verificationAsk("Are you sure you want to delete `${headerObjects[headerID].listOfTODOs[todoID]?.text}`? y/n")) {
                            val newList: Array<TODOObject?> = Array(headerObjects[headerID].listOfTODOs.size-1) { TODOObject() }
                            var k =0
                            for (j in 0 until headerObjects[headerID].listOfTODOs.size) {
                                val newTodo = headerObjects[headerID].listOfTODOs[j]
                                if(j == todoID) {
                                    saveToFile()
                                    continue
                                }
                                newList[k] = newTodo
                                k++
                            }
                            headerObjects[headerID].listOfTODOs = newList
                        }
                    }
                    else -> {}
                }
            }
        }

        when (keycode) {
            up   -> y = max(y-1, 0)
            down -> y++
            left -> x = max(x-1, 0)
            right-> x++
        }
    }

    private fun editText(text: String, message: String): String {
        var output = text
        while (true) {
            initscr()

            erase()
            move(0, 0)

            printw(message)
            printw("\n")
            printw(output)

            refresh()
            val input = getch()
            if (input == Keyboard.ENTER.keycode) {
                if(output.isNotEmpty()) {
                    return output
                }
                continue
            }
            if (input == KEY_BACKSPACE) {
                output = output.dropLast(1)
            } else {
                if(input in 32..122) {
                    output += input.toChar()
                }
            }
        }
    }
}

// Enum for keycodes that ncurses doesn't offer
enum class Keyboard(val keycode: Int) {
    E(101), TAB(9), ENTER(10), Q(113), INSERT(331)
}