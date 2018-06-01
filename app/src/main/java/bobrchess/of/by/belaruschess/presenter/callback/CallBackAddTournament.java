package bobrchess.of.by.belaruschess.presenter.callback;

import bobrchess.of.by.belaruschess.dto.TournamentDTO;

/**
 * Created by Igor on 11.04.2018.
 */

public interface CallBackAddTournament extends CallBack {
    void onResponse(TournamentDTO tournamentDTO);

    void onAddTournamentFailure(Throwable t);

    void onConnectionError(Throwable t);
}
