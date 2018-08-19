package bobrchess.of.by.belaruschess.presenter;

import bobrchess.of.by.belaruschess.view.activity.AddTournamentContractView;

/**
 * Created by Igor on 12.04.2018.
 */

public interface AddTournamentPresenter extends BasePresenter {
    void addTournament();

    void attachView(AddTournamentContractView activity);
}