package bobrchess.of.by.belaruschess.network.api

import bobrchess.of.by.belaruschess.dto.*
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Igor on 10.04.2018.
 */

interface API {

    @get:GET("/places")
    val places: Call<List<PlaceDTO>>

    @get:GET("/ranks")
    val ranks: Call<List<RankDTO>>

    @get:GET("/countries")
    val countries: Call<List<CountryDTO>>

    //User

    @GET("/api/allUsers")
    fun users(@Header("Authorization") authorization: String): Call<List<UserDTO>>

    @get:GET("/coaches")
    val coaches: Call<List<UserDTO>>

    @get:GET("/tournaments")
    val tournaments: Call<List<TournamentDTO>>

    @GET("/tournamentsResultByUser/{userId}/{limit}")
    fun getTournamentsResultByUser(@Path("userId") id: Int, @Path("limit") limit: Int): Call<List<TournamentResultDTO>>

    @GET("/tournamentsResultByUser/{userId}")
    fun getTournamentsResultByUser(@Path("userId") id: Int): Call<List<TournamentResultDTO>>

    @get:GET("/games")
    val games: Call<List<GameDTO>>

    @get:GET("/matches")
    val matches: Call<List<MatchDTO>>

    //Place
    @GET("/place/{id}")
    fun getPlace(@Path("id") id: Int): Call<PlaceDTO>

    @POST("/place")
    fun addPlace(@Body placeDTO: PlaceDTO): Call<PlaceDTO>

    //Rank
    @GET("/rank/{id}")
    fun getRank(@Path("id") id: Int): Call<RankDTO>

    @POST("/rank")
    fun addRank(@Body rankDTO: RankDTO): Call<RankDTO>

    //Country
    @GET("/countryDTO/{id}")
    fun getCountry(@Path("id") id: Int): Call<CountryDTO>

    @POST("/countryDTO")
    fun addCountry(@Body countryDTO: CountryDTO): Call<CountryDTO>

    @GET("/users")
    fun getUsers(@Query("count") count: Int): Call<List<UserDTO>>

    @GET("/searchUsers")
    fun searchUsers(@Query("text") text: String): Call<List<UserDTO>>

    @GET("/user/{id}")
    fun getUser(@Path("id") id: Int): Call<UserDTO>

    @GET("/api/me")
    fun getUser(@Header("Authorization") authorization: String): Call<UserDTO>

    @POST("/api/auth/login")
    fun authorization(@Body userDTO: UserDTO): Call<UserContextDTO>

    @GET("/api/auth/token")
    fun refreshToken(@Header("Authorization") authorization: String): Call<TokenDTO>

    @POST("/register")
    fun registration(@Body userDTO: UserDTO): Call<UserContextDTO>

    //Tournament
    @GET("/tournament/{id}")
    fun getTournament(@Path("id") id: Int): Call<TournamentDTO>

    //Tournament
    @DELETE("/tournament/{id}")
    fun removeTournament(@Path("id") id: Long) : Call<Long>

    @GET("/searchUsers")
    fun searchTournaments(@Query("text") text: String): Call<List<TournamentDTO>>

    @POST("/tournament")
    fun addTournament(@Body tournamentDTO: TournamentDTO): Call<TournamentDTO>

    //Game
    @GET("/game/{id}")
    fun getGame(@Path("id") id: Int): Call<GameDTO>

    @GET("/games")
    fun getGames(@Query("count") count: Int): Call<List<GameDTO>>

    @GET("/searchGames")
    fun searchGames(@Query("text") text: String): Call<List<GameDTO>>

    @POST("/game")
    @FormUrlEncoded
    fun addGame(@Body gameDTO: GameDTO): Call<GameDTO>

    //Match
    @GET("/match/{id}")
    fun getMatch(@Path("id") id: Int): Call<MatchDTO>

    @POST("/match")
    @FormUrlEncoded
    fun addMatch(@Body matchDTO: MatchDTO): Call<MatchDTO>
}
