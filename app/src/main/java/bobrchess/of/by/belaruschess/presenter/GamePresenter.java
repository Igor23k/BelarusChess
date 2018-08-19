package bobrchess.of.by.belaruschess.presenter;

import bobrchess.of.by.belaruschess.view.activity.impl.GameActivity;

/**
 * Created by Igor on 12.04.2018.
 */

public interface GamePresenter extends BasePresenter{
    void getGame(Integer id);

    void getGames();

    void attachView(GameActivity activity);
}