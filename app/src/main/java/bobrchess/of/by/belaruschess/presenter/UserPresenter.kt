package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.dto.CountryDTO
import bobrchess.of.by.belaruschess.dto.RankDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.dto.extended.ExtendedUserDTO
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import bobrchess.of.by.belaruschess.view.activity.UserContractView
import java.io.File

interface UserPresenter : BasePresenter {
    fun updateUser(user: ExtendedUserDTO, userImageFile: File?)
    fun loadUserById(id: Int)
    fun loadReferees()
    fun loadCoaches()
    fun loadUsers(count: Int?)
    fun searchUsers(text: String)
    fun attachView(userContractView: UserContractView)
    fun setPackageModel(packageModel: PackageModel?)
    fun setSelectedRankIndex(selectedRankIndex: Int)
    fun setSelectedCountryIndex(selectedCountryIndex: Int)
    fun setSelectedCoachIndex(selectedCoachIndex: Int)
    fun setSelectedGenderIndex(selectedGenderIndex: Int)
    fun saveRanksIndexes(ranks: List<RankDTO?>?)
    fun saveCoachesIndexes(coaches: List<UserDTO?>?)
    fun saveCountriesIndexes(coaches: List<CountryDTO?>?)
}
