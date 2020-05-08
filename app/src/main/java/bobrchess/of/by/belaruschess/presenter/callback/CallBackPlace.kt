package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.PlaceDTO

/**
 * Created by Igor on 11.04.2018.
 */

interface CallBackPlace : CallBack {
    fun onResponse(placeDTO: PlaceDTO)

    fun onResponse(list: List<PlaceDTO>)
}
