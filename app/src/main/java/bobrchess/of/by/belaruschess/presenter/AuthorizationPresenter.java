package bobrchess.of.by.belaruschess.presenter;

import bobrchess.of.by.belaruschess.view.activity.impl.AuthorizationActivity;

/**
 * Created by Igor on 12.04.2018.
 */

public interface AuthorizationPresenter {
    void authorization();

    void attachView(AuthorizationActivity activity);

    void detachView();

    void viewIsReady();
}
