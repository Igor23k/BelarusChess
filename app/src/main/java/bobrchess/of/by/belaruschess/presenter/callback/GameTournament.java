package bobrchess.of.by.belaruschess.presenter.callback;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.GameDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;

/**
 * Created by Igor on 11.04.2018.
 */

public interface GameTournament extends CallBack {
    void onResponse(GameDTO gameDTO);

    void onResponse(List<GameDTO> list);

    void onFailure(Throwable t);
}
