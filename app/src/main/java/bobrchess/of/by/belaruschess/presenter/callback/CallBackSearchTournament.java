package bobrchess.of.by.belaruschess.presenter.callback;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;

/**
 * Created by Igor on 11.04.2018.
 */

public interface CallBackSearchTournament extends CallBack {
    void onResponse(List<TournamentDTO> tournamentDTO);

    void onConnectionError(Throwable t);
}
