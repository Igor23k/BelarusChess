package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.view.activity.AddTournamentContractView

/**
 * Created by Igor on 12.04.2018.
 */

interface AddTournamentPresenter : BasePresenter {
    fun addTournament(tournamentDTO: TournamentDTO)
    fun loadPlaces()
    fun loadReferees()
    fun attachView(activity: AddTournamentContractView)
    fun setSelectedPlaceIndex(selectedPlaceIndex: Int?)
    fun setSelectedRefereeIndex(selectedRefereeIndex: Int?)
}