package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.dto.extended.ExtendedPlaceDTO
import bobrchess.of.by.belaruschess.view.activity.AddPlaceContractView
import java.io.File

interface AddPlacePresenter : BasePresenter {
    fun removePlace(id: Int?)
    fun addPlace(placeDTO: ExtendedPlaceDTO, userImageUri: File?)
    fun loadCountries()
    fun attachView(activity: AddPlaceContractView)
    fun setSelectedCountryIndex(selectedCountryIndex: Int?)
}