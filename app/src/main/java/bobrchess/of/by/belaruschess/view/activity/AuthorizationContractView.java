package bobrchess.of.by.belaruschess.view.activity;

import bobrchess.of.by.belaruschess.dto.UserDTO;

/**
 * Created by Igor on 22.04.2018.
 */

public interface AuthorizationContractView extends BaseContractView{
    void showProgress();
    void hideProgress();
    void enableButton();
    void disableButton();
    void showIncorrectEmailText();
    void showIncorrectPasswordText();
    void onLoginFailed();
    void onLoginSuccess();
    void onConnectionError();
    void showToast(Integer resId);
    void showToast(String message);
    UserDTO getUserData();
}
