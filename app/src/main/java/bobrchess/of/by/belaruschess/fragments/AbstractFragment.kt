package bobrchess.of.by.belaruschess.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.Gravity
import android.widget.Toast
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.view.activity.BaseContractView
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import bobrchess.of.by.belaruschess.view.activity.impl.ConfirmationDialog
import bobrchess.of.by.belaruschess.view.activity.impl.ConfirmationListener
import org.springframework.util.StringUtils


/**
 * EventInstanceFragment abstract base class for all fragments which edit/add an instance of EventDate
 */
abstract class AbstractFragment : Fragment(), BaseContractView, ConfirmationListener {

    protected var packageModel: PackageModel? = null
    protected var connectivityStatus: Int = 0
    protected var dialog: ConfirmationDialog? = null

    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            connectivityStatus = Util.getConnectivityStatus(context)
            setConnectionStatus(connectivityStatus)
        }
    }

    override fun dismissAlertDialog() {
        dialog?.alertDialog?.dismiss()
    }

    public override fun onResume() {
        super.onResume()
        registerInternetCheckReceiver()
    }

    public override fun onPause() {
        super.onPause()
        this.activity?.unregisterReceiver(broadcastReceiver)
    }

    protected fun registerInternetCheckReceiver() {
        val internetFilter = IntentFilter()
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        this.activity?.registerReceiver(broadcastReceiver, internetFilter)
    }

    override fun showAlertDialog(@StringRes title: Int, @StringRes message: Int, @StringRes buttonText: Int, cancelable: Boolean) {
        if (dialog != null) {
            dialog?.dismiss()
        }
        dialog = ConfirmationDialog()
        dialog!!
                .setData(title, message, buttonText, cancelable)
                .show(activity?.supportFragmentManager, "ConfirmationDialogFragmentTag")

    }

    override fun showToast(resId: Int?) {
        val toast = Toast.makeText(this.activity, resId!!, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    override fun showToast(message: String?) {
        if (!StringUtils.isEmpty(message)) {
            val toast = Toast.makeText(this.activity, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    override fun showSnackbar(resId: Int?) {
        Snackbar
                .make(
                        view!!,
                        resId!!,
                        Snackbar.LENGTH_LONG
                ).show()
    }

}