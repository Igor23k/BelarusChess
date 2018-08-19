package bobrchess.of.by.belaruschess.presenter.callback;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.TournamentDTO;

/**
 * Created by Igor on 11.04.2018.
 */

public interface CallBackUserInfo extends CallBack {
    void onResponse(List<TournamentDTO> tournaments);
}
