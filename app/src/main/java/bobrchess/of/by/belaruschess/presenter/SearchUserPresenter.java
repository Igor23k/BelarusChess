package bobrchess.of.by.belaruschess.presenter;

import bobrchess.of.by.belaruschess.view.activity.impl.SearchUserActivity;

/**
 * Created by Igor on 12.04.2018.
 */

public interface SearchUserPresenter {
    void getUsers();

    void attachView(SearchUserActivity activity);

    void detachView();

    void viewIsReady();
}
