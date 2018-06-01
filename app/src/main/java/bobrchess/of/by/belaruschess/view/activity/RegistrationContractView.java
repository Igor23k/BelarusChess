package bobrchess.of.by.belaruschess.view.activity;

import bobrchess.of.by.belaruschess.dto.UserDTO;

/**
 * Created by Igor on 22.04.2018.
 */

public interface RegistrationContractView extends BaseContractView{
    void registration();
    void showProgress();
    void hideProgress();
    void enableButton();
    void disableButton();
    void onRegistrationFailed();
    void onRegistrationSuccess(UserDTO userDTO);
    void onConnectionError();
    void showIncorrectEmailText();
    void showIncorrectPasswordText();
    void showToast(Integer resId);
    void showToast(String message);
    UserDTO getUserData();
}
