package bobrchess.of.by.belaruschess.presenter;

import bobrchess.of.by.belaruschess.view.activity.impl.TournamentActivity;

/**
 * Created by Igor on 12.04.2018.
 */

public interface TournamentPresenter extends BasePresenter{
    void getTournament(Integer id);

    void getTournaments();

    void loadGames();

    void attachView(TournamentActivity activity);
}