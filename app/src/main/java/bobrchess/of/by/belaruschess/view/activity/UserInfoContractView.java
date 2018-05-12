package bobrchess.of.by.belaruschess.view.activity;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.TournamentDTO;

/**
 * Created by Igor on 22.04.2018.
 */

public interface UserInfoContractView extends BaseContractView {
    void showToast(Integer resId);
    void showProgress();
    void hideProgress();
    void onConnectionError();
    void showToast(String message);
    void displayUserTournaments(List<TournamentDTO> tournaments);

}
