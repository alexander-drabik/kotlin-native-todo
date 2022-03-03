package objects

import getHeaderIdAt
import getLineTypeAt
import getTodoIdAt
import ncurses.printw

class PointerInfo {
    fun draw(y: Int) {
        val lineType = getLineTypeAt(y)
        val objectID = if (lineType == ObjectType.Todo) getTodoIdAt(y)[0] else getHeaderIdAt(y)
        printw("Pointing on $lineType(ID: $objectID)")
    }
}