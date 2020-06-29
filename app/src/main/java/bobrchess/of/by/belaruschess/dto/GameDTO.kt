package bobrchess.of.by.belaruschess.dto

import java.io.Serializable


class GameDTO : Serializable {

    var id: Int? = null
    var gameRecord: String? = null
    var countPointsFirstPlayer: Double? = null
    var countPointsSecondPlayer: Double? = null
    var matchDTO: MatchDTO? = null
    var firstChessPlayer: UserDTO? = null
    var secondChessPlayer: UserDTO? = null

    constructor(id: Int?, name: String, countPointsFirstPlayer: Double?, countPointsSecondPlayer: Double?, matchDTO: MatchDTO, firstChessPlayer: UserDTO, secondChessPlayer: UserDTO) {
        this.id = id
        this.gameRecord = name
        this.countPointsFirstPlayer = countPointsFirstPlayer
        this.countPointsSecondPlayer = countPointsSecondPlayer
        this.matchDTO = matchDTO
        this.firstChessPlayer = firstChessPlayer
        this.secondChessPlayer = secondChessPlayer
    }

    constructor() {}
}
