package bobrchess.of.by.belaruschess.util

import android.app.ProgressDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Base64
import bobrchess.of.by.belaruschess.dto.*
import bobrchess.of.by.belaruschess.handler.BitmapHandler
import bobrchess.of.by.belaruschess.util.Constants.Companion.EMPTY_STRING
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.util.StringUtils
import java.text.SimpleDateFormat
import java.util.*

class Util {

    companion object {
        var TYPE_WIFI = 1
        var TYPE_MOBILE = 2
        var TYPE_NOT_CONNECTED = 0

        private var userImage: String? = null

        fun setUserImage(image: String?) {
            this.userImage = image
        }

        fun getUserImage(): String? {
            return userImage
        }

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

        private var mProgressDialog: ProgressDialog? = null

        val genders: MutableList<String>
            get() {
                val genders = ArrayList<String>()
                genders.add(getInternalizedMessage(Constants.KEY_GENDER_MALE))
                genders.add(getInternalizedMessage(Constants.KEY_GENDER_FEMALE))
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

            if (list != null) {
                for (i in list.indices) {
                    usersNames.add(list[i].name!!)
                }
            }
            return usersNames
        }

        fun getUsersBasicData(users: MutableList<out UserDTO>?): MutableList<String> {
            val usersNames = ArrayList<String>()
            var user: UserDTO

            if (users != null) {
                for (i in users.indices) {
                    user = users[i]
                    var rank = user.rank?.abbreviation
                    rank = if (!StringUtils.isEmpty(rank)) " ($rank)" else ""
                    usersNames.add(user.name + " " + user.surname + " " + user.patronymic + rank)
                }
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
                }
            }
            return ErrorDTO()
        }

        fun buildOnFailureResponse(): ErrorDTO {
            return buildErrorDto(buildErrorJsonResponse(getInternalizedMessage(Constants.KEY_SERVER_UNAVAILABLE_JSON_FORMAT)))
        }

        fun getInternalizedMessage(key: String): String {
            return try {
                val messages = ResourceBundle.getBundle("messages", Locale.getDefault())
                messages.getString(key)
            } catch (e: Exception) {
                ""
            }
        }

        fun isConnected(status: Int?): Boolean {
            return status != TYPE_NOT_CONNECTED
        }

        fun transformDate(format: String, dateString: String?): String? {
            return try {
                val bdFormat = SimpleDateFormat(format, Locale.getDefault())
                val newFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val date = bdFormat.parse(dateString)
                newFormat.format(date)
            } catch (e: Exception) {
                "01.01.1990"
            }
        }

        fun getBitMapByBase64(decodedImage: String?): Bitmap? {
            if (!StringUtils.isEmpty(decodedImage)) {
                val strings = decodedImage!!.split(",").toTypedArray()
                if (strings.size > 1) {
                    val decodedString = Base64.decode(strings[1], Base64.DEFAULT)
                    val decodedImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    return decodedImage
                }
            }
            return null
        }

        fun getScaledBitMapByBase64(decodedImage: String?, resources: Resources): Bitmap? {
            var bitmap = getBitMapByBase64(decodedImage)
            if (bitmap != null) {
                bitmap = BitmapHandler.getCircularBitmap(
                        BitmapHandler.getScaledBitmap(
                                bitmap
                        ), resources
                )
            }
            return bitmap
        }

        /**
         * REQUEST_IMAGE_GET is an intent code used for open the photo gallery
         */
        private val REQUEST_IMAGE_GET = 1

    }

}
