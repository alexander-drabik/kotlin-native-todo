import objects.ObjectType

// Gets if line is either HeaderObject, TodoObject or empty
fun getLineTypeAt(linePosition: Int): ObjectType? {
    var line = 0
    for (headerObject in headerObjects) {
        if (line == linePosition) return ObjectType.Header
        if (headerObject.expanded) {
            line += headerObject.listOfTODOs.size
            if (line >= linePosition) return ObjectType.Todo
        }
        line++
    }
    return null
}

// Gets HeaderObject's ID at line
fun getHeaderIdAt(linePosition: Int): Int {
    var line = 0
    for ((i, headerObject) in headerObjects.withIndex()) {
        if (line == linePosition) return i
        if (headerObject.expanded) line += headerObject.listOfTODOs.size
        line++
    }
    return 0
}

// Gets TodoObject's ID at
fun getTodoIdAt(linePosition: Int): IntArray {
    var line = 1
    for ((i, headerObject) in headerObjects.withIndex()) {
        if (headerObject.expanded) {
            if (line + headerObject.listOfTODOs.size >= linePosition) return intArrayOf(y-line, i)
            line += headerObject.listOfTODOs.size
        }
        line++
    }
    return intArrayOf(0, 0)
}