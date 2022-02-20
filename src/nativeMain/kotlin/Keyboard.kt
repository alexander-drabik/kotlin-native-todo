import ncurses.*
import objects.HeaderObject
import objects.ObjectType
import objects.State
import objects.TODOObject
import kotlin.math.max

class KeyboardEvents {
    fun typeEvent(keycode: Int) {
        when(keycode) {
            exit -> {
                close()
                saveToFile()
            }
            use -> {
                when (getLineType(y)) {
                    ObjectType.Header -> {
                        val id = getHeaderID(y)
                        headerObjects[id!!]?.expanded = !headerObjects[id]!!.expanded
                    }
                    ObjectType.Todo -> {
                        val id = getTodoID(y)
                        headerObjects[id!![1]]!!.listOfTODOs[id[0]]!!.next()
                    }
                    else -> {} // Compiler is angry at me when I don't do that, just ignore that
                }
            }
            edit -> {
                var line = 0
                loop@ for (headerObject in headerObjects) {
                    if (line == y){
                        headerObject!!.headerTitle = editText(headerObject.headerTitle, "Editing '${headerObject.headerTitle}': ")
                        saveToFile()
                        break
                    }

                    line++
                    if (headerObject!!.expanded) {
                        for (todo in headerObject.listOfTODOs) {
                            if (line == y) {
                                todo!!.text = editText(todo.text, "Editing '${todo.text}': ")
                                saveToFile()
                                break@loop
                            }
                            line++
                        }
                    }
                }
            }

            new -> {
                var line = 0
                loop@ for (headerObject in headerObjects) {
                    if (line == y) {
                        val newTodo = TODOObject()
                        newTodo.text = editText("", "Creating new todo checkbox: ")
                        newTodo.state = State.TODO
                        headerObject?.addTODO(newTodo)
                        saveToFile()
                        return
                    }
                    line++
                    if (headerObject!!.expanded) {
                        for (todo in headerObject.listOfTODOs) {
                            if (line == y) {
                                val newTodo = TODOObject()
                                newTodo.text = editText("", "Creating new todo checkbox: ")
                                newTodo.state = State.TODO
                                headerObject.addTODO(newTodo)
                                saveToFile()
                                return
                            }
                            line++
                        }
                    }
                }
                val newHeaderObject = HeaderObject()
                newHeaderObject.headerTitle = editText("", "Creating new todo checkbox: ")
                createHeaderObject(newHeaderObject)
            }
            remove -> {
                var line = 0
                loop@ for ((x, headerObject) in headerObjects.withIndex()) {
                    if(line == y && verificationAsk("Are you sure you want to delete `${headerObject?.headerTitle}`? y/n")) {
                        val newList: Array<HeaderObject?> = Array(headerObjects.size-1) { HeaderObject() }
                        var a = 0
                        for (l in headerObjects.indices) {
                            val newHeaderObject = headerObjects[l]
                            if (l == x) {
                                saveToFile()
                                continue
                            }
                            newList[a] = newHeaderObject
                            a++
                        }
                        headerObjects = newList
                    }

                    line++
                    if (headerObject!!.expanded) {
                        for (i in 0 until headerObject.listOfTODOs.size) {
                            if (line == y && verificationAsk("Are you sure you want to delete `${headerObject.listOfTODOs[i]?.text}`? y/n")) {
                                val newList: Array<TODOObject?> = Array(headerObject.listOfTODOs.size-1) { TODOObject() }
                                var k =0
                                for (j in 0 until headerObject.listOfTODOs.size) {
                                    val newTodo = headerObject.listOfTODOs[j]
                                    if(j == i) {
                                        saveToFile()
                                        continue
                                    }
                                    newList[k] = newTodo
                                    k++
                                }
                                headerObject.listOfTODOs = newList
                                break@loop
                            }
                            line++
                        }
                    }
                }
            }
        }

        when(keycode) {
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
            if(input == Keyboard.ENTER.keycode) {
                if(output.isNotEmpty()) {
                    return output
                }
                continue
            }
            if(input == KEY_BACKSPACE) {
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