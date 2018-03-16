package bobrchess.of.by.colibritweet.pojo

import bobrchess.of.by.belaruschess.pojo.Country
import bobrchess.of.by.belaruschess.pojo.Rank
import java.util.*

/**
 * Created by Igor on 14.03.2018.
 */
data class Person(var id: Long, val name: String, val surname: String, val patronymic: String, val birthday: Date, val email: String, val password: String, val rank: Rank, val nation: Country)