package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.ErrorDTO

interface CallBack {
    fun onFailure(errorDTO: ErrorDTO)
}
