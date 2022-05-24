package bobrchess.of.by.belaruschess.util

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.support.v4.content.CursorLoader
import android.util.Base64
import bobrchess.of.by.belaruschess.dto.*
import bobrchess.of.by.belaruschess.handler.BitmapHandler
import bobrchess.of.by.belaruschess.util.Constants.Companion.EMPTY_STRING
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.util.StringUtils
import java.io.*
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*


class Util {

    companion object {
        var TYPE_WIFI = 1
        var TYPE_MOBILE = 2
        var TYPE_NOT_CONNECTED = 0

        private var user: UserDTO? = null

        fun setUser(image: UserDTO?) {
            this.user = image
        }

        fun getUser(): UserDTO? {
            return user
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

        fun getBitMapByByteArr(image: ByteArray?): Bitmap? {
            if (image != null) {
                return BitmapFactory.decodeByteArray(image, 0, image.size)
            }
            return null
        }

        fun getScaledBitMapByByteArr(image: ByteArray?, resources: Resources): Bitmap? {
            if (image != null) {
                val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                if (bitmap != null) {
                    return BitmapHandler.getCircularBitmap(
                            BitmapHandler.getScaledBitmap(
                                    bitmap
                            ), resources
                    )
                }
            }
            return null
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

        fun getMultipartImage(image: File?): MultipartBody.Part? {
            return if (image != null) {
                MultipartBody.Part.createFormData("file", image.name, RequestBody.create(MediaType.parse("image/*"), image))
            } else null
        }

        fun compressImage(file: File?): File? {
            return try {

                // BitmapFactory options to downsize the image
                val o = BitmapFactory.Options()
                o.inJustDecodeBounds = true
                o.inSampleSize = 6
                // factor of downsizing the image
                var inputStream = FileInputStream(file)
                //Bitmap selectedBitmap = null;
                BitmapFactory.decodeStream(inputStream, null, o)
                inputStream.close()

                // The new size we want to scale to
                val REQUIRED_SIZE = 15

                // Find the correct scale value. It should be the power of 2.
                var scale = 1
                /*   while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                           o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                       scale *= 2
                   }*/
                val o2 = BitmapFactory.Options()
                o2.inSampleSize = scale
                inputStream = FileInputStream(file)
                val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
                inputStream.close()

                // here i override the original image file
                file?.createNewFile()
                val outputStream = FileOutputStream(file)
                selectedBitmap?.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                file
            } catch (e: java.lang.Exception) {
                null
            }
        }

        @Throws(IOException::class)
        fun transformUriToFile(context: Context, uri: Uri): File? {
            val inputStream: InputStream? = context.getContentResolver().openInputStream(uri)
            val fileName = getFileName(context, uri)
            val splitName = splitFileName(fileName)
            var tempFile: File = File.createTempFile(splitName[0], splitName[1])
            tempFile = rename(tempFile, fileName)
            tempFile.deleteOnExit()
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(tempFile)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            if (inputStream != null) {
                if (out != null) {
                    copy(inputStream, out)
                }
                inputStream.close()
            }
            if (out != null) {
                out.close()
            }
            return tempFile
        }

        private fun splitFileName(fileName: String?): Array<String?> {
            var name = fileName
            var extension: String? = ""
            val i = fileName!!.lastIndexOf(".")
            if (i != -1) {
                name = fileName.substring(0, i)
                extension = fileName.substring(i)
            }
            return arrayOf(name, extension)
        }

        private fun getFileName(context: Context, uri: Uri): String? {
            var result: String? = null
            if (uri.scheme == "content") {
                val cursor: Cursor? = context.getContentResolver().query(uri, null, null, null, null)
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                } finally {
                    if (cursor != null) {
                        cursor.close()
                    }
                }
            }
            if (result == null) {
                result = uri.path
                val cut: Int = result!!.lastIndexOf(File.separator)
                if (cut != -1) {
                    result = result.substring(cut + 1)
                }
            }
            return result
        }

        private fun rename(file: File, newName: String?): File {
            val newFile = File(file.getParent(), newName)
            if (!newFile.equals(file)) {
                if (newFile.exists() && newFile.delete()) {
                    //  Log.d("FileUtil", "Delete old $newName file")
                }
                if (file.renameTo(newFile)) {
                    //   Log.d("FileUtil", "Rename file to $newName")
                }
            }
            return newFile
        }

        private val EOF = -1

        @Throws(IOException::class)
        private fun copy(input: InputStream, output: OutputStream): Long {
            var count: Long = 0
            var n: Int = 0
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            while (EOF !== input.read(buffer).also({ n = it })) {
                output.write(buffer, 0, n)
                count += n.toLong()
            }
            return count
        }
    }

}
