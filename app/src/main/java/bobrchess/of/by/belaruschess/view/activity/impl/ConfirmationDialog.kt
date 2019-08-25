package bobrchess.of.by.belaruschess.view.activity.impl

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.content.DialogInterface
import android.view.View


class ConfirmationDialog : DialogFragment() {

    var alertDialog: AlertDialog? = null
    private lateinit var listener: ConfirmationListener
    private var title: Int = 0
    private var message: Int = 0
    private var buttonText: Int = 0

    //private var isCancelable: Boolean = false


    fun setData(@StringRes title: Int, @StringRes message: Int, @StringRes buttonText: Int, cancelable: Boolean): DialogFragment {
        this.title = title
        this.message = message
        this.buttonText = buttonText
        this.isCancelable = cancelable
        return this
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listener = activity as ConfirmationListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement ConfirmationListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        alertDialog = AlertDialog.Builder(context!!)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonText) { _, _ ->
                    listener.dialogConfirmButtonClicked()
                }
                .create()

        alertDialog!!.setOnShowListener { dialog ->
            val button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                listener.dialogConfirmButtonClicked()
            }
        }
        return alertDialog!!
    }
}