package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.PlaceDTO

interface CallBackPlace : CallBack {
    fun onResponse(placeDTO: PlaceDTO)

    fun onResponse(list: List<PlaceDTO>)
}
