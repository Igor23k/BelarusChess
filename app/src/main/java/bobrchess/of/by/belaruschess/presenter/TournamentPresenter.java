package bobrchess.of.by.belaruschess.presenter;

import bobrchess.of.by.belaruschess.view.activity.impl.TournamentActivity;

/**
 * Created by Igor on 12.04.2018.
 */

public interface TournamentPresenter {
    void getTournament(Integer id);

    void getTournaments();

    void attachView(TournamentActivity activity);

    void detachView();

    void viewIsReady();
}