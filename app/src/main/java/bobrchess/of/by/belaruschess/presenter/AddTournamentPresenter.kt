package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.dto.PlaceDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.dto.extended.ExtendedTournamentDTO
import bobrchess.of.by.belaruschess.view.activity.AddTournamentContractView

interface AddTournamentPresenter : BasePresenter {
    fun removeTournament(id: Long?)
    fun addTournament(tournamentDTO: ExtendedTournamentDTO, tournamentImageUri: String?)
    fun loadPlaces()
    fun loadReferees()
    fun attachView(activity: AddTournamentContractView)
    fun setSelectedPlaceIndex(selectedPlaceIndex: Int?)
    fun setSelectedRefereeIndex(selectedRefereeIndex: Int?)
    fun savePlacesIndexes(places: List<PlaceDTO?>?)
    fun saveRefereesIndexes(referees: List<UserDTO?>?)
}