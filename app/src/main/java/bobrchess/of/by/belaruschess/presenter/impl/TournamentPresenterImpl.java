package bobrchess.of.by.belaruschess.presenter.impl;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.GameDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.network.connection.TournamentConnection;
import bobrchess.of.by.belaruschess.presenter.TournamentPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackTournament;
import bobrchess.of.by.belaruschess.view.activity.impl.TournamentActivity;

/**
 * Created by Igor on 11.04.2018.
 */

public class TournamentPresenterImpl implements CallBackTournament, TournamentPresenter {

    private TournamentActivity view;
    private TournamentConnection tournamentConnection;
    private Boolean viewIsReady = false;

    public TournamentPresenterImpl() {
        tournamentConnection = new TournamentConnection();
        tournamentConnection.attachPresenter(this);
    }

    @Override
    public void onResponse(TournamentDTO tournamentDTO) {
        view.hideProgress();
    }

    @Override
    public void onResponse(List list) {
        if (list.get(0).getClass().equals(GameDTO.class)) {
            view.showGames(list);
        }
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
        view.showToast(t.getLocalizedMessage());
    }

    @Override
    public void getTournament(Integer id) {
        if (viewIsReady) {
            view.showProgress();
            tournamentConnection.getTournament(id);
        }
    }

    @Override
    public void getTournaments() {
        if (viewIsReady) {
            view.showProgress();
            tournamentConnection.getTournaments();
        }
    }

    @Override
    public void loadGames() {
        tournamentConnection.getGames();
    }

    @Override
    public void attachView(TournamentActivity activity) {
        view = activity;
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {
        viewIsReady = true;
    }
}
