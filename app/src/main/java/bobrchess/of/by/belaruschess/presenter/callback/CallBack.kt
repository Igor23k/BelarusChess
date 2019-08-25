package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.ErrorDTO

/**
 * Created by Igor on 11.04.2018.
 */

interface CallBack {
    fun onFailure(errorDTO: ErrorDTO)
}
