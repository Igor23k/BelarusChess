package bobrchess.of.by.belaruschess.view.activity;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;

public interface AddTournamentContractView extends MvpView, BaseContractView, ButtonContractView{
    void showIncorrectTournamentNameText();
    void addTournament();
    void setRefereeSpinnerAdapter(List<? extends UserDTO> referees);
    void setPlaceSpinnerAdapter(List<? extends PlaceDTO> places);
    void startActivity(TournamentDTO tournamentDTO);
    void tournamentAdded(TournamentDTO tournamentDTO);
    void removeTournamentFromLocalStorage(long id);
}
