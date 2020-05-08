package bobrchess.of.by.belaruschess.view.activity;

import android.support.annotation.StringRes;

/**
 * Created by Igor on 04.05.2018.
 */

public interface BaseContractView {
    void showAlertDialog(@StringRes int title, @StringRes int message, @StringRes int buttonText, boolean cancelable);
    void dismissAlertDialog();
    void showSnackbar(Integer resId);
    void showToast(Integer resId);
    void showToast(String message);
    void showProgress();
    void hideProgress();
    void setConnectionStatus(Integer connectivityStatus);
}
