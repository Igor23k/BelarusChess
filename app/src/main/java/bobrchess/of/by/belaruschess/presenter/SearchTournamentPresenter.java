package bobrchess.of.by.belaruschess.presenter;

import bobrchess.of.by.belaruschess.presenter.callback.CallBack;
import bobrchess.of.by.belaruschess.view.activity.impl.SearchTournamentActivity;

/**
 * Created by Igor on 12.04.2018.
 */

public interface SearchTournamentPresenter extends CallBack {
    void loadTournaments();

    void loadTournaments(Integer count);

    void searchTournaments();

    void attachView(SearchTournamentActivity activity);

    void detachView();

    void viewIsReady();
}
