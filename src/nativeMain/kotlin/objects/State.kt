package objects

enum class State {
    TODO, DOING, DONE;
}

fun stringToState(string: String): State {
    return when (string) {
        "[ ]", " " -> State.TODO
        "[-]", "-" -> State.DOING
        "[X]", "X", "x" -> State.DONE
        else -> State.TODO
    }
}