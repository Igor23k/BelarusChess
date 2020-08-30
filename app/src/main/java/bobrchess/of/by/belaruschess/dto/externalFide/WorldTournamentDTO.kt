package bobrchess.of.by.belaruschess.dto.externalFide

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class WorldTournamentDTO : Serializable {

    var id: Int? = null
    var name: String? = null
    var preview: String? = null
    var category: CategoryDTO? = null
    @SerializedName("date_start")
    var dateStart: String? = null
    @SerializedName("date_end")
    var dateEnd: String? = null
    @SerializedName("time_control_typ")
    var timeControlTyp: String? = null
    @SerializedName("num_round")
    var numRound: String? = null
    var organizer: String? = null
    @SerializedName("chief_arbiter")
    var chiefArbiter: String? = null
    @SerializedName("chief_organizer")
    var chiefOrganizer: String? = null
    var crosstable: String? = null
    var arbiters: String? = null
    var email: String? = null
    var fax: String? = null
    var tel: String? = null
    var pfund: String? = null
    var darbiter: String? = null
    var place: String? = null
    var city: String? = null
    var country: String? = null
    @SerializedName("number_of_players")
    var numberOfPlayers: String? = null
    var type: String? = null
    @SerializedName("time_control")
    var timeControl: String? = null
    @SerializedName("timecontrol_description")
    var timeControlDescription: String? = null
    var zone: String? = null
    var address: String? = null
    @SerializedName("address_coordinates")
    var addressCoordinates: String? = null
    var remarks: String? = null
    var website: String? = null
    @SerializedName("event_type")
    var eventType: String? = null

    constructor(id: Int?, name: String?, preview: String?, category: CategoryDTO?, dateStart: String?, dateEnd: String?, timeControlTyp: String?, numRound: String?, organizer: String?, chiefArbiter: String?, chiefOrganizer: String?, crosstable: String?, arbiters: String?, email: String?, fax: String?, tel: String?, pfund: String?, darbiter: String?, place: String?, city: String?, country: String?, numberOfPlayers: String?, type: String?, timeControl: String?, timeControlDescription: String?, zone: String?, address: String?, addressCoordinates: String?, remarks: String?, website: String?, eventType: String?) {
        this.id = id
        this.name = name
        this.preview = preview
        this.category = category
        this.dateStart = dateStart
        this.dateEnd = dateEnd
        this.timeControlTyp = timeControlTyp
        this.numRound = numRound
        this.organizer = organizer
        this.chiefArbiter = chiefArbiter
        this.chiefOrganizer = chiefOrganizer
        this.crosstable = crosstable
        this.arbiters = arbiters
        this.email = email
        this.fax = fax
        this.tel = tel
        this.pfund = pfund
        this.darbiter = darbiter
        this.place = place
        this.city = city
        this.country = country
        this.numberOfPlayers = numberOfPlayers
        this.type = type
        this.timeControl = timeControl
        this.timeControlDescription = timeControlDescription
        this.zone = zone
        this.address = address
        this.addressCoordinates = addressCoordinates
        this.remarks = remarks
        this.website = website
        this.eventType = eventType
    }

    constructor()
}
