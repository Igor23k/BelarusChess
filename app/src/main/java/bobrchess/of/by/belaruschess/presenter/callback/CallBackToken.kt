package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.TokenDTO

/**
 * Created by Igor on 11.04.2018.
 */

interface CallBackToken : CallBack {
    fun onResponse(tokenDTO: TokenDTO)
}
