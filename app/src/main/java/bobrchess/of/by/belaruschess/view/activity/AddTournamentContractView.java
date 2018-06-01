package bobrchess.of.by.belaruschess.view.activity;

import bobrchess.of.by.belaruschess.dto.TournamentDTO;

/**
 * Created by Igor on 22.04.2018.
 */

public interface AddTournamentContractView extends BaseContractView{
    void showProgress();
    void hideProgress();
    void enableButton();
    void disableButton();
    void onAddTournamentFailed();
    void onAddTournamentSuccess(TournamentDTO tournamentDTO);
    void onConnectionError();
    void showIncorrectTournamentNameText();
    void showToast(Integer resId);
    void showToast(String message);
    void addTournament();
    TournamentDTO getTournamentData();
}
