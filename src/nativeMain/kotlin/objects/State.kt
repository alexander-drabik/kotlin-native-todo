package objects

enum class State {
    TODO, DOING, DONE;
}

fun stringToState(string: String): State {
    return when(string) {
        "[ ]" -> State.TODO
        "[-]" -> State.DOING
        "[X]" -> State.DONE
        else -> State.TODO
    }
}