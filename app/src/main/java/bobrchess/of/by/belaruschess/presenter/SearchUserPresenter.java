package bobrchess.of.by.belaruschess.presenter;

import bobrchess.of.by.belaruschess.presenter.callback.CallBack;
import bobrchess.of.by.belaruschess.view.activity.impl.SearchUserActivity;

/**
 * Created by Igor on 12.04.2018.
 */

public interface SearchUserPresenter extends CallBack{
    void loadUsers();

    void loadUsers(Integer count);

    void searchUsers();

    void attachView(SearchUserActivity activity);

    void detachView();

    void viewIsReady();
}
