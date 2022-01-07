package objects

/* Object used for creating headers for checkboxes (TODOs). */
class HeaderObject {
    var headerTitle = "test"
    var listOfTODOs: Array<TODOObject> = Array(0) { TODOObject() }
    // If expanded is equal to true - TODOs will be visible.
    var expanded: Boolean = false
}