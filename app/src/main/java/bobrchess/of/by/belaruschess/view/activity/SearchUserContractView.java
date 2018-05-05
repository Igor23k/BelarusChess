package bobrchess.of.by.belaruschess.view.activity;

/**
 * Created by Igor on 22.04.2018.
 */

public interface SearchUserContractView extends BaseContractView {
    void showToast(Integer resId);
    void showProgress();
    void hideProgress();
    void onConnectionError();
    void showToast(String message);
}
