package bobrchess.of.by.belaruschess.network.api

import bobrchess.of.by.belaruschess.dto.externalFide.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ExternalFideServerApi {

    @get:GET("/api/v1/client/players")
    val topPlayersRating: Call<TopPlayersDTO>

    @GET("/api/v1/client/players/{search}")
    fun searchTopPlayers(@Path("text") text: String): Call<TopPlayersDTO>

    @GET("/api/v1/client/players/{id}")
    fun topPlayer(@Path("id") id: Int): Call<List<TopPlayerWithImageDTO>>

    @GET("/api/v1/client/events/{id}")
    fun worldTournament(@Path("id") id: Int): Call<WorldTournamentDataDTO>

    @GET("/api/v1/client/events")
    fun worldTournaments(@Query("filter[date_start_years]") id: Int, @Query("filter[world_champion]") worldChampion: Boolean, @Query("filter[closest_events]") closestEvents: Boolean, @Query("filter[category]") category: String, @Query("filter[date_start_month]") dateStartMonth: Int): Call<WorldTournamentsDataDTO>
}
