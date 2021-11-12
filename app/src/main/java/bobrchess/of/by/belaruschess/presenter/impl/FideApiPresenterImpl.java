package bobrchess.of.by.belaruschess.presenter.impl;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.dto.externalFide.TopPlayerDTO;
import bobrchess.of.by.belaruschess.dto.externalFide.TopPlayersDTO;
import bobrchess.of.by.belaruschess.dto.externalFide.WorldTournamentDTO;
import bobrchess.of.by.belaruschess.dto.externalFide.WorldTournamentsDataDTO;
import bobrchess.of.by.belaruschess.network.connection.external.FideApiConnection;
import bobrchess.of.by.belaruschess.adapter.FideApiAdapterContract;
import bobrchess.of.by.belaruschess.presenter.FideApiPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackFideApi;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.FideApiContractView;

@InjectViewState
public class FideApiPresenterImpl extends MvpPresenter<FideApiContractView> implements CallBackFideApi, FideApiPresenter {

    private FideApiContractView view;
    private FideApiConnection fideApiConnection;
    private Boolean viewIsReady = false;
    private Integer connectivityStatus = 0;

    public FideApiPresenterImpl() {
        fideApiConnection = new FideApiConnection();
        fideApiConnection.attachPresenter(this);
    }

    @Override
    public void onResponse(@NotNull TopPlayersDTO topPlayersDTO) {
        view.showTopPlayers(topPlayersDTO);
    }

    @Override
    public void onResponse(@NotNull WorldTournamentsDataDTO tournamentsData) {
        view.showWorldTournaments(tournamentsData);
    }

    @Override
    public void loadTopPlayers() {
        fideApiConnection.getTopPlayers();
    }

    @Override
    public void loadWorldTournaments(int id, boolean worldChampion, boolean closestEvents, @NotNull String category, int dateStartMonth) {
        fideApiConnection.getWorldTournaments(id, worldChampion, closestEvents, category, dateStartMonth);
    }

    @Override
    public void attachView(@NotNull FideApiContractView fideApiContractView) {
        view = fideApiContractView;
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
