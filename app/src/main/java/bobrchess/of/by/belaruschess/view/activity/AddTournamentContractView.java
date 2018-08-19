package bobrchess.of.by.belaruschess.view.activity;

import bobrchess.of.by.belaruschess.dto.TournamentDTO;

/**
 * Created by Igor on 22.04.2018.
 */

public interface AddTournamentContractView extends BaseContractView, ButtonContractView{
    void showIncorrectTournamentNameText();
    void addTournament();
    TournamentDTO getTournamentData();
    void startActivity(TournamentDTO tournamentDTO);
}
