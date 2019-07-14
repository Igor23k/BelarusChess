package bobrchess.of.by.belaruschess.dto

class ErrorDTO {
    var timestamp : String? = null
    var status : Int? = null
    var error : String? = null
    var message : String? = null
    var path : String? = null

    constructor(timestamp: String?, status: Int?, error: String?, message: String?, path: String?) {
        this.timestamp = timestamp
        this.status = status
        this.error = error
        this.message = message
        this.path = path
    }
}