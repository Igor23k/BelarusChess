package bobrchess.of.by.belaruschess.view.activity;

import android.view.View;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.UserDTO;

/**
 * Created by Igor on 22.04.2018.
 */

public interface AuthorizationContractView extends MvpView, BaseContractView, ButtonContractView{
    void showIncorrectEmailText();
    void showIncorrectPasswordText();
    void startActivity(UserDTO userDTO);
    void showNoConnectionAlertDialog(Integer title, Integer message, Integer buttonText, Boolean cancelable);
    void showSnackBar(View componentView, String message, Integer buttonText);
    void showSnackBar(View componentView, Integer message, Integer buttonText);
    void authorization();
}
