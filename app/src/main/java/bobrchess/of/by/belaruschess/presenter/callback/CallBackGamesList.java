package bobrchess.of.by.belaruschess.presenter.callback;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.GameDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;

/**
 * Created by Igor on 11.04.2018.
 */

public interface CallBackGamesList extends CallBack {
    void onResponse(List<GameDTO> games);

    void onConnectionError(Throwable t);
}
