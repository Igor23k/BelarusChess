package bobrchess.of.by.belaruschess.presenter;

import bobrchess.of.by.belaruschess.presenter.callback.CallBack;
import bobrchess.of.by.belaruschess.view.activity.impl.GamesListActivity;
import bobrchess.of.by.belaruschess.view.activity.impl.SearchUserActivity;

/**
 * Created by Igor on 12.04.2018.
 */

public interface GamesListPresenter extends CallBack{
    void loadGames();

    void loadGames(Integer count);

    void searchGames();

    void attachView(GamesListActivity activity);

    void detachView();

    void viewIsReady();
}
