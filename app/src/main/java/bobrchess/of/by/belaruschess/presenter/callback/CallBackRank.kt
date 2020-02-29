package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.RankDTO

/**
 * Created by Igor on 11.04.2018.
 */

interface CallBackRank : CallBack {
    fun onResponse(rankDTO: RankDTO)

    fun onResponse(list: List<RankDTO>)
}
