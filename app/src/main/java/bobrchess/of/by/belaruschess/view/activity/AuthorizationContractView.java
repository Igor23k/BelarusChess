package bobrchess.of.by.belaruschess.view.activity;

/**
 * Created by Igor on 22.04.2018.
 */

public interface AuthorizationContractView {
    void showToast(int resId);
    void showProgress();
    void hideProgress();
    void showIncorrectEmailText();
    void showIncorrectPasswordText();
}
