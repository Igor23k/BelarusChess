package bobrchess.of.by.belaruschess.presenter.callback;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.presenter.callback.CallBack;

/**
 * Created by Igor on 11.04.2018.
 */

public interface CallBackUser extends CallBack {
    void onResponse(UserDTO userDTO);

    void onResponse(List<UserDTO> usersDTO);
}
