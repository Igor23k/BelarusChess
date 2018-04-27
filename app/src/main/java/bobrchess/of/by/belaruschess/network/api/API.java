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
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
    Call<RankDTO> addRank(@Field("name") String name, @Field("abbreviation") String abbreviation);

    //Country
    @GET("/country/{id}")
    Call<CountryDTO> getCountry(@Path("id") int id);

    @GET("/countries")
    Call<List<CountryDTO>> getCountries();

    @POST("/country")
    @FormUrlEncoded
    Call<CountryDTO> addCountry(@Field("name") String name, @Field("abbreviation") String abbreviation);

    //User
    @GET("/users")
    Call<List<UserDTO>> getUsers();

    @GET("/user/{id}")
    Call<UserDTO> getUser(@Path("id") int id);

    @POST("/user")
    @FormUrlEncoded
    Call<UserDTO> authorizate(@Field("email") String email, @Field("password") String password);

    //Tournament
    @GET("/tournament/{id}")
    Call<TournamentDTO> getTournament(@Path("id") int id);

    @GET("/tournaments")
    Call<List<TournamentDTO>> getTournaments();

    @POST("/tournament")
    @FormUrlEncoded
    Call<TournamentDTO> addTournament(@Field("name") String name, @Field("startDate") Date startDate, @Field("finishDate") Date finishDate,
                                   @Field("countPlayersInTeam") Integer countPlayersInTeam, @Field("placeId") Integer placeId,
                                   @Field("refereeId") UserDTO refereeId);

    //Game
    @GET("/game/{id}")
    Call<GameDTO> getGame(@Path("id") int id);

    @GET("/games")
    Call<List<GameDTO>> getGames();

    @POST("/game")
    @FormUrlEncoded
    Call<GameDTO> addGame(@Field("name") String name, @Field("countPointsFirstPlayer") Double countPointsFirstPlayer,
                                      @Field("countPointsSecondPlayer") Double countPointsSecondPlayer,
                                      @Field("matchId") Integer matchId, @Field("firstChessPlayer") UserDTO firstChessPlayer,
                                      @Field("secondChessPlayer") UserDTO secondChessPlayer);

    //Match
    @GET("/match/{id}")
    Call<MatchDTO> getMatch(@Path("id") int id);

    @GET("/matches")
    Call<List<MatchDTO>> getMatches();

    @POST("/match")
    @FormUrlEncoded
    Call<MatchDTO> addMatch(@Field("countPointsFirstTeam") Double countPointsFirstTeam,
                          @Field("countPointsSecondTeam") Double countPointsSecondTeam,
                          @Field("date") Date date, @Field("tournamentId") TournamentDTO tournament,
                          @Field("firstTeamId") TournamentTeamDTO firstTeamId, @Field("secondTeamId") TournamentTeamDTO secondTeamId);
}
