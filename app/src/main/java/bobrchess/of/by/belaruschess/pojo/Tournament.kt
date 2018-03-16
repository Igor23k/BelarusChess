package bobrchess.of.by.belaruschess.pojo

import bobrchess.of.by.colibritweet.pojo.Person
import java.util.*

/**
 * Created by Igor on 16.03.2018.
 */
data class Tournament(val id: Long, val name: String, val start: Date, val end: Date, val countPlayersInTeam: Byte, val place: Place, val referee: Person)