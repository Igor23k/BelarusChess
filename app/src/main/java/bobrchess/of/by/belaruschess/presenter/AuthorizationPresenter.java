package bobrchess.of.by.belaruschess.presenter;

import android.view.View;

import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.view.activity.AuthorizationContractView;

/**
 * Created by Igor on 12.04.2018.
 */

public interface AuthorizationPresenter extends BasePresenter {
    void authorization(UserDTO userDTO);
    void attachView(AuthorizationContractView activity);
    void setConnectivityStatus(Integer status);
    void attachViewComponent(View view);
}
