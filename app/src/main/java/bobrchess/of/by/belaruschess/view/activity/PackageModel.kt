package bobrchess.of.by.belaruschess.view.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.Gravity
import android.widget.Toast
import bobrchess.of.by.belaruschess.util.Constants.Companion.USER_PREFERENCES

class PackageModel(private val context: Context) {

    fun addSharePref(tokenMap: Map<String, String>?) {
        val editor = context.getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE).edit()
        tokenMap?.entries?.forEach { entry -> editor.putString(entry.key, entry.value) }
        editor.apply()
    }

    fun addSharePref(propertyName: String, propertyValue: String?) {
        val editor = context.getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE).edit()
        editor.putString(propertyName, propertyValue)
        editor.apply()
    }

    fun clearSharePref() {
        val editor = context.getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }

    fun getSharePrefValue(propertyName: String): String {
        val value = context.getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE).getString(propertyName, "")!!
        return if (value.isEmpty()) {
            value
        } else {
            "Bearer $value"
        }
    }

    fun showToast(resId: Int?) {
        val toast = Toast.makeText(context, resId!!, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun showToast(message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }


    class ConfirmationDialogFragment : DialogFragment() {

        interface ConfirmationListener {
            fun confirmButtonClicked()
            fun cancelButtonClicked()
        }

        private lateinit var listener: ConfirmationListener

        override fun onAttach(context: Context?) {
            super.onAttach(context)

            try {
                // Instantiate the ConfirmationListener so we can send events to the host
                listener = activity as ConfirmationListener
            } catch (e: ClassCastException) {
                // The activity doesn't implement the interface, throw exception
                throw ClassCastException(activity.toString() + " must implement ConfirmationListener")
            }
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return AlertDialog.Builder(context!!)
                    .setMessage("Please, confirm the action")
                    .setPositiveButton("Confirm") { _, _ ->
                        listener.confirmButtonClicked()
                    }
                    .setNegativeButton("Cancel") { _, _ ->
                        listener.cancelButtonClicked()
                    }
                    .create()
        }
    }
}
