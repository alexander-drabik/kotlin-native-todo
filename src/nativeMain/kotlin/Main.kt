import objects.HeaderObject

// Root array of header objects, can be accessed from everywhere via 'import headerObjects'
var headerObjects: Array<HeaderObject?> =  Array(0) { HeaderObject() }

fun main() {
    //TODO: printing
    loadFromFile()
}

fun createHeaderObject(headerObject: HeaderObject) {
    val newHeaderObjects: Array<HeaderObject?> = headerObjects.copyOf(headerObjects.size+1)
    newHeaderObjects[headerObjects.size] = headerObject

    headerObjects = newHeaderObjects
}