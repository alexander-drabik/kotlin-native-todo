
import kotlinx.cinterop.memScoped
import objects.HeaderObject
import objects.TODOObject
import objects.stringToState
import platform.posix.fclose
import platform.posix.fgetc
import platform.posix.fopen
import platform.posix.fputs

fun saveToFile() {
    // Open save file in write mode
    val file = fopen("/home/alex/.todo/todo", "w")

    // Construct output string
    val output = StringBuilder()
    for(header in headerObjects) {
        // Write header title in format: #title
        output.append('#' + header!!.headerTitle + '\n')

        // Write TODOs in format: [stateStatus]title
        for(todo in header.listOfTODOs) {
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
    val file = fopen("/home/alex/.todo/todo", "r")

    var input = StringBuilder()

    // Manual controlling of memory
    memScoped {
        var headerObject: HeaderObject ?= null
        do {
            // Get single char from save file
            val c = fgetc(file)

            // New line operations
            if(c == 10) {
                // Remove whitespaces from the beginning and end
                val line = StringBuilder()
                line.append(input.trim().toString())
                input = line

                if(input.isNotEmpty()) {
                    when(input[0]) {
                        // Line is header object's title
                        '#' -> {
                            // Add current header object to array if exists
                            if(headerObject != null)
                                createHeaderObject(headerObject)

                            // Create new header object
                            headerObject = HeaderObject()

                            // Set header's title to value after # character
                            for(i in 1 until input.length) {
                                headerObject.headerTitle += input[i]
                            }
                        }
                        '[' -> {
                            // Create new todoObject
                            val todoObject = TODOObject()

                            // Get state
                            var todoState = ""
                            todoState += input[0]
                            todoState += input[1]
                            todoState += input[2]
                            todoObject.state = stringToState(todoState)

                            // Set title
                            for(i in 3 until input.length) {
                                todoObject.text += input[i]
                            }

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