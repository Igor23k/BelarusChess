package bobrchess.of.by.belaruschess.view.activity;

/**
 * Created by Igor on 22.04.2018.
 */

public interface AuthContractView extends BaseContractView{
    void showProgress();
    void hideProgress();
    void enableButton();
    void disableButton();
    void showIncorrectEmailText();
    void showIncorrectPasswordText();
}
