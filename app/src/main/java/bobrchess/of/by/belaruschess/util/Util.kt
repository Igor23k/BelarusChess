package bobrchess.of.by.belaruschess.util

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import bobrchess.of.by.belaruschess.dto.*
import bobrchess.of.by.belaruschess.util.Constants.Companion.EMPTY_STRING
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.util.StringUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Igor on 03.05.2018.
 */

class Util {

    companion object {
        var TYPE_WIFI = 1
        var TYPE_MOBILE = 2
        var TYPE_NOT_CONNECTED = 0

        fun getConnectivityStatus(context: Context): Int {
            val cm = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            if (null != activeNetwork) {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI)
                    return TYPE_WIFI
                if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
                    return TYPE_MOBILE
            }
            return TYPE_NOT_CONNECTED
        }


        var USER_INFO: Int? = 1
        var TOURNAMENT_PARTICIPANTS_REQUEST: Int? = 2
        var TOURNAMENT_TABLE_REQUEST: Int? = 3
        val AUTHORIZATION_REQUEST = 4
        val ADD_TOURNAMENT_REQUEST = 5
        val REGISTRATION_REQUEST = 6
        val TOURNAMENT_REQUEST = 7


        val testTournament: TournamentDTO
            get() {
                val tournamentDTO = TournamentDTO()
                tournamentDTO.place = testPlace
                tournamentDTO.referee = testUser
                tournamentDTO.countPlayersInTeam = 1
                tournamentDTO.toursCount = 9
                tournamentDTO.name = "Белая ладья - 2018"
                tournamentDTO.fullDescription = "Положение о проведении открытого республиканского шахматного турнира памяти Ю. В.  Кулаги\n" +
                        "\n" +
                        "Цель турнира\n" +
                        "\n" +
                        "Ø Установление дружественных связей с шахматистами других регионов\n" +
                        "Ø Повышение мастерства шахматистов\n"
                tournamentDTO.shortDescription = "Вам тут понравится!"
                tournamentDTO.startDate = "456789"
                tournamentDTO.finishDate = "4567890"
                return tournamentDTO
            }

        val testPlace: PlaceDTO
            get() {
                val placeDTO = PlaceDTO()
                placeDTO.name = "Училище 28"
                placeDTO.street = "Первомайская"
                placeDTO.country = testCountry
                placeDTO.building = "43"
                placeDTO.city = "Минск"
                placeDTO.capacity = 100
                return placeDTO
            }

        val testCountry: CountryDTO
            get() {
                val countryDTO = CountryDTO()
                countryDTO.name = "Франция"
                countryDTO.abbreviation = "FRA"
                return countryDTO
            }

        val testCountry2: CountryDTO
            get() {
                val countryDTO = CountryDTO()
                countryDTO.name = "Франциq"
                countryDTO.abbreviation = "FRS"
                return countryDTO
            }

        val testRank: RankDTO
            get() {
                val rankDTO = RankDTO()
                rankDTO.abbreviation = "GM"
                rankDTO.name = "Gross"
                return rankDTO
            }

        val testUser: UserDTO
            get() {
                val userDTO = UserDTO()
                userDTO.email = "ww@dd.ek"
                userDTO.country = testCountry2
                userDTO.rank = testRank
                userDTO.status = "My best status"
                userDTO.phoneNumber = "29373692"
                userDTO.name = "Ihar"
                userDTO.surname = "Kazlou"
                userDTO.patronymic = "Sergeevich"
                userDTO.password = "12345678901234567890123456789012"
                userDTO.rating = 2000
                userDTO.birthday = "eeeeeee"
                userDTO.coach = null
                return userDTO
            }

        val testUser2: UserDTO
            get() {
                val userDTO = UserDTO()
                userDTO.email = "ww@dd.ek1"
                userDTO.country = testCountry
                userDTO.rank = testRank
                userDTO.status = "My best status1"
                userDTO.phoneNumber = "293736921"
                userDTO.name = "Ihar1"
                userDTO.surname = "Kazlou1"
                userDTO.patronymic = "Sergeevich1"
                userDTO.password = "12345678901234567890123456789012"
                userDTO.rating = 2000
                userDTO.birthday = "eeeeeee1"
                return userDTO
            }

        private var mProgressDialog: ProgressDialog? = null

        //bug доставать это из резурсов (с интернационализацией).
        val genders: List<String>
            get() {
                val genders = ArrayList<String>()
                genders.add("Male")
                genders.add("Female")
                return genders
            }

        fun getEncodedPassword(password: String): String {
            return String(Hex.encodeHex(DigestUtils.md5(password)))
        }

        @JvmOverloads
        fun showSimpleProgressDialog(context: Context, title: String? = null,
                                     msg: String = "Loading...", isCancelable: Boolean = false) {
            try {
                if (mProgressDialog == null) {
                    mProgressDialog = ProgressDialog.show(context, title, msg)
                    mProgressDialog!!.setCancelable(isCancelable)
                }

                if (!mProgressDialog!!.isShowing) {
                    mProgressDialog!!.show()
                }

            } catch (ie: IllegalArgumentException) {
                ie.printStackTrace()
            } catch (re: RuntimeException) {
                re.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun removeSimpleProgressDialog() {
            try {
                if (mProgressDialog != null) {
                    if (mProgressDialog!!.isShowing) {
                        mProgressDialog!!.dismiss()
                        mProgressDialog = null
                    }
                }
            } catch (ie: IllegalArgumentException) {
                ie.printStackTrace()

            } catch (re: RuntimeException) {
                re.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun isNetworkAvailable(context: Context): Boolean {
            val connectivity = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity == null) {
                return false
            } else {
                val info = connectivity.allNetworkInfo
                if (info != null) {
                    for (i in info.indices) {
                        if (info[i].state == NetworkInfo.State.CONNECTED) {
                            return true
                        }
                    }
                }
            }
            return false
        }

        fun getCountriesBasicData(list: MutableList<out CountryDTO>?): MutableList<String> {
            val usersNames = ArrayList<String>()
            var country: CountryDTO
            for (i in list!!.indices) {
                usersNames.add(list[i].name!!)
            }
            return usersNames
        }

        fun getUsersBasicData(users: MutableList<out UserDTO>?): MutableList<String> {
            val usersNames = ArrayList<String>()
            var user: UserDTO
            for (i in users!!.indices) {
                user = users[i]
                var rank = user.rank?.abbreviation
                rank = if (!StringUtils.isEmpty(rank)) " ($rank)" else ""
                usersNames.add(user.name + " " + user.surname + " " + user.patronymic + rank)
            }
            return usersNames
        }

        fun getRanksNames(ranks: List<RankDTO>): MutableList<String> {
            val usersNames = ArrayList<String>(ranks.size)
            for (i in ranks.indices) {
                ranks[i].name?.let { usersNames.add(it) }
            }
            return usersNames
        }

        fun getCountriesNames(countries: MutableList<CountryDTO>): MutableList<String> {
            val usersNames = ArrayList<String>()
            for (i in countries.indices) {
                countries[i].name?.let { usersNames.add(it) }
            }
            return usersNames
        }

        fun getPlacesNames(places: MutableList<out PlaceDTO>): MutableList<String> {
            val placeNames = ArrayList<String>()
            for (i in places.indices) {
                places[i].name?.let { placeNames.add(it) }
            }
            return placeNames
        }

        fun buildErrorJsonResponse(errorString: String): String {
            return try {
                (JsonParser().parse(errorString) as JsonObject).toString()
            } catch (e: Exception) {
                EMPTY_STRING
            }
        }

        fun buildErrorDto(errorMessage: String): ErrorDTO {
            if (!StringUtils.isEmpty(errorMessage)) {
                try {
                    return Gson().fromJson(errorMessage, ErrorDTO::class.java)
                } catch (e: Exception) {
                    //todo log
                }
            }
            return ErrorDTO()
        }

        fun buildOnFailureResponse(): ErrorDTO {
            return buildErrorDto(buildErrorJsonResponse(getInternalizedMessage(Constants.KEY_SERVER_UNAVAILABLE_JSON_FORMAT)))
        }

        fun getInternalizedMessage(key: String): String {
            return try {
                val errors = ResourceBundle.getBundle("messages", Locale.getDefault())
                errors.getString(key)
            } catch (e: Exception) {
                ""
            }
        }

        fun isConnected(status: Int?): Boolean {
            return status != TYPE_NOT_CONNECTED
        }

        fun transformDate(dateString: String?): String? {
            return try {
                val bdFormat = SimpleDateFormat("dd/mm/yyyy", Locale.getDefault())
                val newFormat = SimpleDateFormat("dd.mm.yyyy", Locale.getDefault())
                val date = bdFormat.parse(dateString)
                newFormat.format(date)
            } catch (e: Exception) {
                null
            }
        }
    }
}
