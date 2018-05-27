package bobrchess.of.by.belaruschess.network.api;

import java.util.Date;
import java.util.List;

import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.GameDTO;
import bobrchess.of.by.belaruschess.dto.MatchDTO;
import bobrchess.of.by.belaruschess.dto.RankDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.dto.TournamentTeamDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Igor on 10.04.2018.
 */

public interface API {
    //Rank
    @GET("/rank/{id}")
    Call<RankDTO> getRank(@Path("id") int id);

    @GET("/ranks")
    Call<List<RankDTO>> getRanks();

    @POST("/rank")
    @FormUrlEncoded
    Call<RankDTO> addRank(@Body RankDTO rankDTO);

    //Country
    @GET("/countryDTO/{id}")
    Call<CountryDTO> getCountry(@Path("id") int id);

    @GET("/countries")
    Call<List<CountryDTO>> getCountries();

    @POST("/countryDTO")
    Call<CountryDTO> addCountry(@Body CountryDTO countryDTO);

    //User

    @GET("/allUsers")
    Call<List<UserDTO>> getUsers();

    @GET("/users")
    Call<List<UserDTO>> getUsers(@Query("count") int count);

    @GET("/searchUsers")
    Call<List<UserDTO>> searchUsers(@Query("text") String text);

    @GET("/user/{id}")
    Call<UserDTO> getUser(@Path("id") int id);

    @POST("/user")
    Call<UserDTO> authorization(@Body UserDTO userDTO);

    @POST("/addUser")
    Call<UserDTO> registration(@Body UserDTO userDTO);

    //Tournament
    @GET("/tournament/{id}")
    Call<TournamentDTO> getTournament(@Path("id") int id);

    @GET("/tournaments")
    Call<List<TournamentDTO>> getTournaments();

    @GET("/searchUsers")
    Call<List<TournamentDTO>> searchTournaments(@Query("text") String text);

    @POST("/tournament")
    Call<TournamentDTO> addTournament(@Body TournamentDTO tournamentDTO);

    //Game
    @GET("/game/{id}")
    Call<GameDTO> getGame(@Path("id") int id);

    @GET("/games")
    Call<List<GameDTO>> getGames();

    @GET("/games")
    Call<List<GameDTO>> getGames(@Query("count") int count);

    @GET("/searchGames")
    Call<List<GameDTO>> searchGames(@Query("text") String text);

    @POST("/game")
    @FormUrlEncoded
    Call<GameDTO> addGame(@Body GameDTO gameDTO);

    //Match
    @GET("/match/{id}")
    Call<MatchDTO> getMatch(@Path("id") int id);

    @GET("/matches")
    Call<List<MatchDTO>> getMatches();

    @POST("/match")
    @FormUrlEncoded
    Call<MatchDTO> addMatch(@Body MatchDTO matchDTO);
}
