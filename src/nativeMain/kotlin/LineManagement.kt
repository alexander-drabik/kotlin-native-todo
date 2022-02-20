
import objects.ObjectType

fun getLineType(y: Int): ObjectType? {
    var line = 0
    for (headerObject in headerObjects) {
        if (line == y) return ObjectType.Header
        if (headerObject!!.expanded) {
            line += headerObject.listOfTODOs.size
            if (line > y) return ObjectType.Todo
        }
        line++
    }
    return null
}

fun getHeaderID(y: Int): Int? {
    var line = 0
    for ((i, headerObject) in headerObjects.withIndex()) {
        if (line == y) return i
        if (headerObject!!.expanded) line += headerObject.listOfTODOs.size
        line++
    }
    return null
}

fun getTodoID(y: Int): IntArray? {
    var line = 0
    for ((i, headerObject) in headerObjects.withIndex()) {
        if (headerObject!!.expanded) {
            if (line + headerObject.listOfTODOs.size > y) return intArrayOf(y-line-1, i)
            line += headerObject.listOfTODOs.size
        }
        line++
    }
    return null
}