package bobrchess.of.by.belaruschess.dto

import java.io.Serializable

/**
 * Created by Igor on 05.04.2018.
 */

class RegistrationUserDTO : UserDTO(), Serializable {

    var reEnterPassword: String? = null
    var selectedRankIndex: Int? = null
    var selectedCountryIndex: Int? = null
    var selectedCoachIndex: Int? = null
    var selectedGenderIndex: Int? = null

}