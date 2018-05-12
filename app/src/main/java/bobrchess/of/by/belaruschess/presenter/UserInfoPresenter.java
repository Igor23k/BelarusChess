package bobrchess.of.by.belaruschess.presenter;

import bobrchess.of.by.belaruschess.view.activity.impl.UserInfoActivity;

/**
 * Created by Igor on 12.04.2018.
 */

public interface UserInfoPresenter {
    void attachView(UserInfoActivity activity);

    void detachView();

    void viewIsReady();

    void readyToLoadUserTournaments();
}
