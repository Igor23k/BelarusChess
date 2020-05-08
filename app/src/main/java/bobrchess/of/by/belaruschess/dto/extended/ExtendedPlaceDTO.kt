package bobrchess.of.by.belaruschess.dto.extended

import bobrchess.of.by.belaruschess.dto.PlaceDTO
import java.io.Serializable

class ExtendedPlaceDTO : PlaceDTO(), Serializable {

    var selectedCountryIndex: Int? = null

}