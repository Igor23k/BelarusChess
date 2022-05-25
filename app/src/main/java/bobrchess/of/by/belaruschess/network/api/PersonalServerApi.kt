package bobrchess.of.by.belaruschess.network.api

import bobrchess.of.by.belaruschess.dto.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File


interface PersonalServerApi {

    // Lite
    @GET("/tournaments-lite")
    fun tournamentsLite(@Query("upcomingOnly") upcomingOnly: Boolean): Call<List<TournamentDTO>>

    @get:GET("/places-lite")
    val placesLite: Call<List<PlaceDTO>>

    @GET("/api/allUsers-lite")
    fun usersLite(@Header("Authorization") authorization: String): Call<List<UserDTO>>

    @get:GET("/places")
    val places: Call<List<PlaceDTO>>

    @get:GET("/ranks")
    val ranks: Call<List<RankDTO>>

    @get:GET("/countries")
    val countries: Call<List<CountryDTO>>

    //User

    @GET("/api/allUsers")
    fun users(@Header("Authorization") authorization: String): Call<List<UserDTO>>

    @GET("/coaches")
    fun coaches(): Call<List<UserDTO>>

    @GET("/api/referees")
    fun referees(@Header("Authorization") authorization: String): Call<List<UserDTO>>

    @GET("/tournaments")
    fun tournaments(@Query("upcomingOnly") upcomingOnly: Boolean): Call<List<TournamentDTO>>

    @GET("/tournaments/{count}")
    fun tournaments(@Path("count") count: Int, @Query("upcomingOnly") upcomingOnly: Boolean): Call<List<TournamentDTO>>

    @GET("/user/{id}")
    fun userById(@Header("Authorization") authorization: String, @Path("id") id: Int): Call<UserDTO>

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
    @Multipart
    fun addPlace(@Header("Authorization") authorization: String, @Part("place") placeDTO: PlaceDTO, @Part file: MultipartBody.Part?, @Part("isImageUpdated") isImageUpdated: Boolean): Call<PlaceDTO>

    @DELETE("/place/{id}")
    fun removePlace(@Header("Authorization") authorization: String, @Path("id") id: Int): Call<Int>

    @GET("/searchPlaces")
    fun searchPlaces(@Query("text") text: String): Call<List<PlaceDTO>>

    //Rank
    @GET("/rank/{id}")
    fun getRank(@Path("id") id: Int): Call<RankDTO>

    @POST("/rank")
    fun addRank(@Header("Authorization") authorization: String, @Body rankDTO: RankDTO): Call<RankDTO>

    //Country
    @GET("/countryDTO/{id}")
    fun getCountry(@Path("id") id: Int): Call<CountryDTO>

    @POST("/countryDTO")
    fun addCountry(@Header("Authorization") authorization: String, @Body countryDTO: CountryDTO): Call<CountryDTO>

    @GET("/users")
    fun getUsers(@Header("Authorization") authorization: String, @Query("count") count: Int): Call<List<UserDTO>>

    @GET("/searchUsers")
    fun searchUsers(@Header("Authorization") authorization: String, @Query("text") text: String): Call<List<UserDTO>>

    @GET("/api/me")
    fun authorization(@Header("Authorization") authorization: String): Call<UserDTO>

    @POST("/api/auth/login")
    fun authorization(@Body userDTO: UserDTO): Call<UserContextDTO>

    @POST("/resetPassword")
    fun passwordReset(@Body email: String): Call<Boolean>

    @GET("/api/auth/token")
    fun refreshToken(@Header("Authorization") authorization: String): Call<TokenDTO>

    @POST("/register")
    @Multipart
    fun registration(@Part("user") userDTO: UserDTO, @Part file: MultipartBody.Part?): Call<UserContextDTO>

    //Tournament
    @GET("/tournament/{id}")
    fun getTournament(@Path("id") id: Int): Call<TournamentDTO>

    //Tournament
    @DELETE("/tournament/{id}")
    fun removeTournament(@Path("id") id: Long): Call<Long>

    @GET("/searchUsers")
    fun searchTournaments(@Query("text") text: String): Call<List<TournamentDTO>>

    @POST("/tournament")
    @Multipart
    fun addTournament(@Header("Authorization") authorization: String, @Part("tournament") tournamentDTO: TournamentDTO, @Part file: MultipartBody.Part?, @Part("isImageUpdated") isImageUpdated: Boolean): Call<TournamentDTO>

    @POST("/api/updateUser")
    @Multipart
    fun updateUser(@Header("Authorization") authorization: String, @Part("user") userDTO: UserDTO, @Part file: MultipartBody.Part?): Call<UserDTO>

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
