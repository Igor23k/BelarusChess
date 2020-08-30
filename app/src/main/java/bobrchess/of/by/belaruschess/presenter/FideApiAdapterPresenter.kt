package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.adapter.FideApiAdapterContract


interface FideApiAdapterPresenter : BasePresenter {
    fun loadWorldTournament(id: Int)
    fun loadTopPlayer(id:Int)
    fun attachView(fideApiContractView: FideApiAdapterContract)
}
