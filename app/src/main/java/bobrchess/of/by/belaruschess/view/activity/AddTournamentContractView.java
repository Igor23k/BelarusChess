package bobrchess.of.by.belaruschess.view.activity;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.TournamentDTO;

/**
 * Created by Igor on 22.04.2018.
 */

public interface AddTournamentContractView extends BaseContractView, ButtonContractView{
    void showIncorrectTournamentNameText();
    void addTournament();
    TournamentDTO getTournamentData();
    void setRefereeSpinnerAdapter(List<String> refereeNames);
    void setCountrySpinnerAdapter(List<String> countryNames);
    void startActivity(TournamentDTO tournamentDTO);
}
