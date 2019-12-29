package bobrchess.of.by.belaruschess.view.activity;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.TournamentDTO;

/**
 * Created by Igor on 22.04.2018.
 */

public interface AddTournamentContractView extends MvpView, BaseContractView, ButtonContractView{
    void showIncorrectTournamentNameText();
    void addTournament();
    void setRefereeSpinnerAdapter(List<String> refereeNames);
    void setPlaceSpinnerAdapter(List<String> placeNames);
    void startActivity(TournamentDTO tournamentDTO);
    void tournamentAdded(TournamentDTO tournamentDTO);
}
