package bobrchess.of.by.belaruschess.view.activity;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.UserDTO;

/**
 * Created by Igor on 22.04.2018.
 */

public interface RegistrationContractView extends BaseContractView, ButtonContractView{
    void registration();
    void showIncorrectEmailText();
    void showIncorrectPasswordText();
    UserDTO getUserData();
    void startActivity(UserDTO userDTO);
    void setCoachSpinnerAdapter(List<String> userNames);
    void setRankSpinnerAdapter(List<String> rankNames);
    void setCountrySpinnerAdapter(List<String> countryNames);
}
