package bobrchess.of.by.belaruschess.presenter.callback;

import bobrchess.of.by.belaruschess.dto.UserDTO;

/**
 * Created by Igor on 11.04.2018.
 */

public interface CallBackRegistration extends CallBack {
    void onResponse(UserDTO userDTO);

    void onRegistrationFailure(Throwable t);

    void onConnectionError(Throwable t);
}
