package bobrchess.of.by.belaruschess.view.activity;

import android.view.View;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.UserDTO;

/**
 * Created by Igor on 22.04.2018.
 */

public interface RegistrationContractView extends MvpView, BaseContractView, ButtonContractView{
    void registration();
    void showIncorrectEmailText();
    void showIncorrectPasswordText();
    void showNoConnectionAlertDialog(Integer title, Integer message, Integer buttonText, Boolean cancelable);
    void showServerInUnavailableAlertDialog(Integer title, Integer message, Integer buttonText, Boolean cancelable);
    void showSnackBar(View componentView, Integer text, Integer buttonText);
    void showSnackBar(View componentView, String message, Integer buttonText);
    void startActivity(UserDTO userDTO);
    void setGenderSpinnerAdapter(List<String> userNames);
    void setCoachSpinnerAdapter(List<String> userNames);
    void setRankSpinnerAdapter(List<String> rankNames);
    void setCountrySpinnerAdapter(List<String> countryNames);
    void loadSpinnersData();
}
