package bobrchess.of.by.belaruschess.exception

class IncorrectDataException : Exception {

    constructor() {}

    constructor(e: Exception) : super(e) {}

    constructor(message: String) : super(message) {}

    constructor(message: String, e: Exception) : super(message, e) {}

    companion object {
        private val serialVersionUID = 1L
    }
}
