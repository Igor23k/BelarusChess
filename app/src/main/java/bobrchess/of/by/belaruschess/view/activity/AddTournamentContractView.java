package bobrchess.of.by.belaruschess.view.activity;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;

/**
 * Created by Igor on 22.04.2018.
 */

public interface AddTournamentContractView extends MvpView, BaseContractView, ButtonContractView{
    void showIncorrectTournamentNameText();
    void addTournament();
    void setRefereeSpinnerAdapter(List<UserDTO> referees);
    void setPlaceSpinnerAdapter(List<PlaceDTO> places);
    void startActivity(TournamentDTO tournamentDTO);
    void tournamentAdded(TournamentDTO tournamentDTO);
    void removeTournamentFromLocalStorage(long id);
}
