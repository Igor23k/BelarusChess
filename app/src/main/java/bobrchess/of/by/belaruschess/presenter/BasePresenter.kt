package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.util.Util

/**
 * Created by Igor on 19.08.2018.
 */

interface BasePresenter {
    fun detachView()
    fun viewIsReady()
    fun onServerUnavailable()
    fun onUnsuccessfulRequest(message: String?)
    fun setConnectivityStatus(status: Int?)
    fun isConnected(status: Int): Boolean {
        return Util.isConnected(status)
    }
}
