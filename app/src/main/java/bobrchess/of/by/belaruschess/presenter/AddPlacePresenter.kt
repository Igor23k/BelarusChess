package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.dto.extended.ExtendedPlaceDTO
import bobrchess.of.by.belaruschess.view.activity.AddPlaceContractView

interface AddPlacePresenter : BasePresenter {
    fun removePlace(id: Int?)
    fun addPlace(placeDTO: ExtendedPlaceDTO)
    fun loadCountries()
    fun attachView(activity: AddPlaceContractView)
    fun setSelectedCountryIndex(selectedCountryIndex: Int?)
}