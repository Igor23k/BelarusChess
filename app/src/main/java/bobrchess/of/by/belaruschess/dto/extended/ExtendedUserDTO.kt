package bobrchess.of.by.belaruschess.dto.extended

import bobrchess.of.by.belaruschess.dto.UserDTO
import java.io.Serializable

class ExtendedUserDTO : UserDTO(), Serializable {

    var reEnterPassword: String? = null
    var selectedRankIndex: Int? = null
    var selectedCountryIndex: Int? = null
    var selectedCoachIndex: Int? = null
    var selectedGenderIndex: Int? = null

}