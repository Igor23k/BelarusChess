package bobrchess.of.by.belaruschess.dto.externalFide

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class WorldTournamentsDTO : Serializable {

    @SerializedName("American Events")
    var americanTournaments: List<WorldTournamentDTO>? = null
    @SerializedName("European Events")
    var europeanTournaments: List<WorldTournamentDTO>? = null
    @SerializedName("African Events")
    var africanTournaments: List<WorldTournamentDTO>? = null
    @SerializedName("World Events")
    var worldTournaments: List<WorldTournamentDTO>? = null
    @SerializedName("Asian Events")
    var asianTournaments: List<WorldTournamentDTO>? = null
    @SerializedName("Presidential Board / Congress")
    var presidentialBoardAndCongressTournaments: List<WorldTournamentDTO>? = null
    @SerializedName("Other Official Events")
    var otherOfficialTournaments: List<WorldTournamentDTO>? = null
    @SerializedName("Affiliated Associations Events")
    var affiliatedAssociationsTournaments: List<WorldTournamentDTO>? = null

    constructor(americanTournaments: List<WorldTournamentDTO>?, europeanTournaments: List<WorldTournamentDTO>?, africanTournaments: List<WorldTournamentDTO>?, worldTournaments: List<WorldTournamentDTO>?, asianTournaments: List<WorldTournamentDTO>?, presidentialBoardAndCongressTournaments: List<WorldTournamentDTO>?, otherOfficialTournaments: List<WorldTournamentDTO>?, affiliatedAssociationsTournaments: List<WorldTournamentDTO>?) {
        this.americanTournaments = americanTournaments
        this.europeanTournaments = europeanTournaments
        this.africanTournaments = africanTournaments
        this.worldTournaments = worldTournaments
        this.asianTournaments = asianTournaments
        this.presidentialBoardAndCongressTournaments = presidentialBoardAndCongressTournaments
        this.otherOfficialTournaments = otherOfficialTournaments
        this.affiliatedAssociationsTournaments = affiliatedAssociationsTournaments
    }

    constructor()
}
