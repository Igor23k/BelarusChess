package bobrchess.of.by.belaruschess.presenter;

import bobrchess.of.by.belaruschess.view.activity.impl.GamesListActivity;

/**
 * Created by Igor on 12.04.2018.
 */

public interface GamesListPresenter extends BasePresenter {
    void loadGames();

    void loadGames(Integer count);

    void searchGames();

    void attachView(GamesListActivity activity);
}
