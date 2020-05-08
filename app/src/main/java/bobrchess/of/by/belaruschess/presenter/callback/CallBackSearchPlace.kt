package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.PlaceDTO

interface CallBackSearchPlace : CallBack {
    fun onResponse(placesDTO: List<PlaceDTO>)
}
