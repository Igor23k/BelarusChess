package bobrchess.of.by.belaruschess.view.activity.impl

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.annotation.StringRes
import android.view.Gravity
import android.widget.Toast
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.view.activity.BaseContractView
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import com.arellomobile.mvp.MvpAppCompatActivity
import org.springframework.util.StringUtils

abstract class AbstractActivity : MvpAppCompatActivity(), BaseContractView, ConfirmationListener {

    protected var packageModel: PackageModel = PackageModel(this)
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
        unregisterReceiver(broadcastReceiver)
    }

    protected fun registerInternetCheckReceiver() {
        val internetFilter = IntentFilter()
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(broadcastReceiver, internetFilter)
    }

    override fun showAlertDialog(@StringRes title: Int, @StringRes message: Int, @StringRes buttonText: Int, cancelable: Boolean) {
        if (dialog != null) {
            dialog?.dismiss()
        }
        dialog = ConfirmationDialog()
        dialog!!
                .setData(title, message, buttonText, cancelable)
                .show(supportFragmentManager, "ConfirmationDialogFragmentTag")

    }

    override fun showToast(resId: Int?) {
        val toast = Toast.makeText(this, resId!!, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    override fun showToast(message: String?) {
        if (!StringUtils.isEmpty(message)) {
            val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
}
