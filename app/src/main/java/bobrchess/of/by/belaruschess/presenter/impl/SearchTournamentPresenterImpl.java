package bobrchess.of.by.belaruschess.presenter.impl;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.network.connection.internal.SearchTournamentConnection;
import bobrchess.of.by.belaruschess.presenter.SearchTournamentPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackSearchTournament;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.SearchTournamentContractView;

@InjectViewState
public class SearchTournamentPresenterImpl extends MvpPresenter<SearchTournamentContractView> implements CallBackSearchTournament, SearchTournamentPresenter {

    private SearchTournamentContractView view;
    private SearchTournamentConnection tournamentConnection;
    private Boolean viewIsReady = false;
    private Integer connectivityStatus = 0;

    public SearchTournamentPresenterImpl() {
        tournamentConnection = new SearchTournamentConnection();
        tournamentConnection.attachPresenter(this);
    }

    @Override
    public void loadTournaments() {
        view.showProgress();
        tournamentConnection.getTournaments();
    }

    @Override
    public void loadTournaments(Integer count) {
        view.showProgress();
        tournamentConnection.getTournaments(count);
    }

    @Override
    public void searchTournaments(@NotNull String text) {
        if (viewIsReady) {
            view.showProgress();
            tournamentConnection.searchTournaments(text);
        }
    }

    @Override
    public void attachView(@NotNull SearchTournamentContractView searchTournamentContractView) {
        view = searchTournamentContractView;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void viewIsReady() {
        viewIsReady = true;
    }

    @Override
    public void onResponse(@NotNull List<? extends TournamentDTO> tournamentsDTO) {
        view.showTournaments(tournamentsDTO);
        view.hideProgress();
        view.hideProgress();
    }

    @Override
    public void onFailure(@NotNull ErrorDTO errorDTO) {
        view.hideProgress();
        view.showToast(errorDTO.getError());
    }

    @Override
    public void onServerUnavailable() {
        view.hideProgress();
    }

    @Override
    public void onUnsuccessfulRequest(@Nullable String message) {
        view.hideProgress();
    }

    @Override
    public void setConnectivityStatus(Integer status) {
        this.connectivityStatus = status;
    }

    @Override
    public boolean isConnected(int status) {
        return Util.Companion.isConnected(status);
    }
}
