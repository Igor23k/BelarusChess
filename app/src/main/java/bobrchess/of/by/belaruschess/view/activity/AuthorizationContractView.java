package bobrchess.of.by.belaruschess.view.activity;

import bobrchess.of.by.belaruschess.dto.UserDTO;

/**
 * Created by Igor on 22.04.2018.
 */

public interface AuthorizationContractView extends BaseContractView, ButtonContractView{
    void showIncorrectEmailText();
    void showIncorrectPasswordText();
    UserDTO getUserData();
    void startActivity(UserDTO userDTO);
}
