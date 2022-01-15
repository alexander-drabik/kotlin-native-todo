
import ncurses.*
import objects.HeaderObject
import objects.State
import objects.TODOObject
import kotlin.math.max

class KeyboardEvents {
    fun typeEvent(keycode: Int) {
        when(keycode) {
            Keyboard.Q.keycode -> {
                close()
            }
            Keyboard.TAB.keycode, Keyboard.ENTER.keycode -> {
                var line = 0
                loop@ for(headerObject in headerObjects) {
                    if(line == y) {
                        headerObject?.expanded = !headerObject?.expanded!!
                        break
                    }
                    line++
                    if(headerObject!!.expanded) {
                        for(todo in headerObject.listOfTODOs) {
                            line++
                        }
                    }
                }
            }
            Keyboard.E.keycode -> {
                var line = 0
                loop@ for(headerObject in headerObjects) {
                    if(line == y){
                        headerObject!!.headerTitle = editText(headerObject.headerTitle)
                        saveToFile()
                        break
                    }

                    line++
                    if(headerObject!!.expanded) {
                        for(todo in headerObject.listOfTODOs) {
                            if(line == y) {
                                todo!!.text = editText(todo.text)
                                saveToFile()
                                break@loop
                            }
                            line++
                        }
                    }
                }
            }

            Keyboard.INSERT.keycode -> {
                var line = 0
                for(headerObject in headerObjects) {
                    if(line == y) {
                        val newTodo = TODOObject()
                        newTodo.text = editText("")
                        newTodo.state = State.TODO
                        headerObject?.addTODO(newTodo)
                    }
                    line++
                    if(headerObject!!.expanded) {
                        for(todo in headerObject.listOfTODOs) {
                            if(line == y) {
                                val newTodo = TODOObject()
                                newTodo.text = editText("")
                                newTodo.state = State.TODO
                                headerObject.addTODO(newTodo)
                            }
                            line++
                        }
                    }
                }
                val newHeaderObject = HeaderObject()
                newHeaderObject.headerTitle = editText("")
                createHeaderObject(newHeaderObject)
            }
            KEY_BACKSPACE, Keyboard.R.keycode -> {
                var line = 0
                loop@ for((x, headerObject) in headerObjects.withIndex()) {
                    if(line == y) {
                        val newList: Array<HeaderObject?> = Array(headerObjects.size-1) { HeaderObject() }
                        var a = 0
                        for(l in headerObjects.indices) {
                            val newHeaderObject = headerObjects[l]
                            if(l == x){
                                saveToFile()
                                continue
                            }
                            newList[a] = newHeaderObject
                            a++
                        }
                        headerObjects = newList
                    }

                    line++
                    if(headerObject!!.expanded) {
                        for(i in 0 until headerObject.listOfTODOs.size) {
                            if(line == y) {
                                val newList: Array<TODOObject?> = Array(headerObject.listOfTODOs.size-1) { TODOObject() }
                                var k =0
                                for(j in 0 until headerObject.listOfTODOs.size) {
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
            Keyboard.ENTER.keycode -> {
                var line = 0
                loop@ for(headerObject in headerObjects) {
                    line++
                    if(headerObject!!.expanded) {
                        for(todo in headerObject.listOfTODOs) {
                            if(line == y) {
                                when(todo?.state) {
                                    State.TODO  -> todo.state = State.DOING
                                    State.DOING -> todo.state = State.DONE
                                    State.DONE  -> todo.state = State.TODO
                                }
                                saveToFile()

                                break@loop
                            }
                            line++
                        }
                    }
                }
            }
        }

        when(keycode) {
            KEY_UP   -> y = max(y-1, 0)
            KEY_DOWN -> y++
            KEY_LEFT -> x = max(x-1, 0)
            KEY_RIGHT -> x++
        }
    }

    private fun editText(text: String): String {
        var output = text
        while (true) {
            initscr()

            erase()
            move(0, 0)

            printw(output)

            refresh()
            val input = getch()
            if(input == Keyboard.ENTER.keycode || input == Keyboard.TAB.keycode) {
                if(output.isNotEmpty()) {
                    return output
                }
            }
            if(input == KEY_BACKSPACE) {
                output = output.dropLast(1)
            } else {
                output += input.toChar()
            }
        }
    }
}

// Enum for keycodes that ncurses doesn't offer
enum class Keyboard(val keycode: Int) {
    E(101), TAB(9), ENTER(10), Q(113), INSERT(331), R(114)
}