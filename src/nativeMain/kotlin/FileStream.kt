import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import objects.HeaderObject
import objects.TODOObject
import objects.stringToState
import platform.posix.*

fun saveToFile() {
    // Open save file in write mode
    val file = fopen("${getenv("HOME")?.toKString()}/.todo/save", "w")

    // Construct output string
    val output = StringBuilder()
    for (header in headerObjects) {
        // Write header title in format: #title
        output.append('#' + header.headerTitle + '\n')

        // Write TODOs in format: [stateStatus]title
        for (todo in header.listOfTODOs) {
            output.append(9.toChar() + todo!!.stateToString() + todo.text + '\n')
        }

        // Separate headers with new lines
        output.append('\n')
    }

    // Write output string to file
    fputs(output.toString(), file)

    // Close file
    fclose(file)
}

fun loadFromFile() {
    // Open save file in read mode
    val file = fopen("${getenv("HOME")?.toKString()}/.todo/save", "r")

    var input = StringBuilder()

    // Manual controlling of memory
    memScoped {
        var headerObject: HeaderObject ?= null
        do {
            // Get single char from save file
            val c = fgetc(file)

            // New line operations
            if (c == 10) {
                // Remove whitespaces from the beginning and end
                val line = StringBuilder()
                line.append(input.trim().toString())
                input = line

                if (input.isNotEmpty()) {
                    when (input[0]) {
                        // Line is header object's title
                        '#' -> {
                            // Add current header object to array if exists
                            if (headerObject != null)
                                createHeaderObject(headerObject)

                            // Create new header object
                            headerObject = HeaderObject()

                            val regex = """^#\s*(.+)$""".toRegex()
                            val title = regex.find(input.toString())?.groups?.get(1)!!.value

                            headerObject.headerTitle = title
                        }
                        '[' -> {
                            // Create new todoObject
                            val todoObject = TODOObject()

                            // Get state
                            val regex = """^\[([ \-xX])]\s*(.+?)$""".toRegex()
                            val todoState = regex.find(input.toString())?.groups?.get(1)!!.value

                            todoObject.state = stringToState(todoState)

                            // Set title
                            todoObject.text = regex.find(input.toString())?.groups?.get(2)!!.value 

                            headerObject!!.addTODO(todoObject)
                        }
                    }
                }

                // Make input empty
                input = StringBuilder()
            } else {
                // Add character to input
                input.append(c.toChar())
            }

        } while(c >= 0)
        if (headerObject != null)
            createHeaderObject(headerObject)
    }
}

fun loadConfig() {
    // Open config file in read mode
    val file = fopen("${getenv("HOME")?.toKString()}/.config/todo/config", "r")

    var input = StringBuilder()

    // Manual controlling of memory
    memScoped {
        do {
            val c = fgetc(file)

            if (c == 10) {
                // Remove whitespaces from the beginning and end
                val line = StringBuilder()
                line.append(input.trim().toString())
                input = line

                val commandRegex = """([^\s]+)\s(.+)""".toRegex()
                val value = commandRegex.find(input.toString())?.groups?.get(2)?.value

                when (commandRegex.find(input.toString())?.groups?.get(1)?.value) {
                    "spaces" -> spaces = value?.toInt()!!
                    "indent_spaces" -> indentSpaces = value?.toInt()!!
                    "pointer" -> pointer = value.toBoolean()
                    "header_extend_info" -> headerExtendInfo = value.toString()

                    "up"   -> up    = toKey(value)
                    "down" -> down  = toKey(value)
                    "left" -> left  = toKey(value)
                    "right"-> right = toKey(value)
                    "use"  -> use   = toKey(value)
                    "new"  -> new   = toKey(value)
                }

                input = StringBuilder()
            } else {
                input.append(c.toChar())
            }

        } while (c >= 0)
    }
}