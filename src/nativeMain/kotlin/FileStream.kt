
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fputs

fun saveToFile() {
    // Open save file
    val file = fopen("/home/alex/.todo/todo", "w")

    // Construct output string
    var output = StringBuilder()
    for(header in headerObjects) {
        // Write header title in format: #title
        output.append('#' + header.headerTitle + '\n')

        // Write TODOs in format: [stateStatus]title
        for(todo in header.listOfTODOs) {
            output.append(9.toChar() + todo.stateToString() + todo.text + '\n')
        }

        // Separate headers with new lines
        output.append('\n')
    }

    // Write output string to file
    fputs(output.toString(), file)

    // Close file
    fclose(file)
}