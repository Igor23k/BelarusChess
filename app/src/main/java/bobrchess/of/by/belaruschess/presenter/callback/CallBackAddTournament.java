package bobrchess.of.by.belaruschess.presenter.callback;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.RankDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;

/**
 * Created by Igor on 11.04.2018.
 */

public interface CallBackAddTournament extends CallBack {
    void onResponse(TournamentDTO tournamentDTO);
    void onRefereeResponse(List<UserDTO> referees);
    void onPlaceResponse(List<PlaceDTO> places);
}
