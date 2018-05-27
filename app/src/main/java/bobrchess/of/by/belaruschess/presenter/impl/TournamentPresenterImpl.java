package bobrchess.of.by.belaruschess.presenter.impl;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.network.connection.TournamentConnection;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackTournament;
import bobrchess.of.by.belaruschess.presenter.TournamentPresenter;
import bobrchess.of.by.belaruschess.view.activity.impl.TournamentActivity;

/**
 * Created by Igor on 11.04.2018.
 */

public class TournamentPresenterImpl implements CallBackTournament, TournamentPresenter {

    private TournamentActivity view;
    private TournamentConnection tournamentConnection;

    public TournamentPresenterImpl() {
        tournamentConnection = new TournamentConnection();
        tournamentConnection.attachPresenter(this);
    }

    @Override
    public void onResponse(TournamentDTO tournamentDTO) {
        view.hideProgress();
    }

    @Override
    public void onResponse(List<TournamentDTO> list) {
        view.hideProgress();
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
    }

    @Override
    public void getTournament(Integer id) {
        view.disableButton();
        view.showProgress();
        tournamentConnection.getTournament(id);
    }

    @Override
    public void getTournaments() {
        view.disableButton();
        view.showProgress();
        tournamentConnection.getTournaments();
    }

    @Override
    public void attachView(TournamentActivity activity) {
        view = activity;
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {

    }
}
