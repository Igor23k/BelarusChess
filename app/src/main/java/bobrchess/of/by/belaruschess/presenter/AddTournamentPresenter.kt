package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.dto.PlaceDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.dto.extended.ExtendedTournamentDTO
import bobrchess.of.by.belaruschess.view.activity.AddTournamentContractView
import java.io.File

interface AddTournamentPresenter : BasePresenter {
    fun removeTournament(id: Long?)
    fun addTournament(tournamentDTO: ExtendedTournamentDTO, tournamentImageFile: File?)
    fun loadPlaces()
    fun loadReferees()
    fun attachView(activity: AddTournamentContractView)
    fun setSelectedPlaceIndex(selectedPlaceIndex: Int?)
    fun setSelectedRefereeIndex(selectedRefereeIndex: Int?)
    fun savePlacesIndexes(places: List<PlaceDTO?>?)
    fun saveRefereesIndexes(referees: List<UserDTO?>?)
}