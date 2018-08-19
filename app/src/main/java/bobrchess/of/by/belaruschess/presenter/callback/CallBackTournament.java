package bobrchess.of.by.belaruschess.presenter.callback;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.GameDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;

/**
 * Created by Igor on 11.04.2018.
 */

public interface CallBackTournament extends CallBack {
    void onResponse(TournamentDTO tournamentDTO);

    void onResponse(List list);
}
