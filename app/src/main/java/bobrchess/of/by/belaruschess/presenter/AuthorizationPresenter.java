package bobrchess.of.by.belaruschess.presenter;

import bobrchess.of.by.belaruschess.view.activity.impl.AuthorizationActivity;

/**
 * Created by Igor on 12.04.2018.
 */

public interface AuthorizationPresenter extends BasePresenter{
    void authorization();

    void attachView(AuthorizationActivity activity);
}
