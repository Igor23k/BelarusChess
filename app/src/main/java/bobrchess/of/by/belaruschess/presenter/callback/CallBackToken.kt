package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.TokenDTO

interface CallBackToken : CallBack {
    fun onResponse(tokenDTO: TokenDTO)
}
