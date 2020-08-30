package bobrchess.of.by.belaruschess.presenter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.List;

import bobrchess.of.by.belaruschess.adapter.FideApiAdapterContract;
import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.dto.externalFide.TopPlayerWithImageDTO;
import bobrchess.of.by.belaruschess.dto.externalFide.WorldTournamentDataDTO;
import bobrchess.of.by.belaruschess.network.connection.external.FideApiAdapterConnection;
import bobrchess.of.by.belaruschess.presenter.FideApiAdapterPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAdapterFideApi;
import bobrchess.of.by.belaruschess.util.Util;

public class FideApiAdapterPresenterImpl implements CallBackAdapterFideApi, FideApiAdapterPresenter {

    private FideApiAdapterContract adapterContractPresenter;
    private FideApiAdapterConnection fideApiAdapterConnection;
    private Boolean viewIsReady = false;
    private Integer connectivityStatus = 0;

    public FideApiAdapterPresenterImpl() {
        fideApiAdapterConnection = new FideApiAdapterConnection();
        fideApiAdapterConnection.attachPresenter(this);
    }

    @Override
    public void onResponse(@NotNull List<TopPlayerWithImageDTO> topPlayerDTO) {
        if (!CollectionUtils.isEmpty(topPlayerDTO)) {
            adapterContractPresenter.showTopPlayer(topPlayerDTO.get(0));
        }
    }

    @Override
    public void onResponse(@NotNull WorldTournamentDataDTO worldTournamentDTO) {
        adapterContractPresenter.showWorldTournament(worldTournamentDTO.getData());
    }

    @Override
    public void loadTopPlayer(int id) {
        fideApiAdapterConnection.getTopPlayer(id);
    }

    @Override
    public void loadWorldTournament(int id) {
        fideApiAdapterConnection.getWorldTournament(id);
    }

    @Override
    public void attachView(@NotNull FideApiAdapterContract fideApiContractView) {
        adapterContractPresenter = fideApiContractView;
    }

    @Override
    public void detachView() {
        adapterContractPresenter = null;
    }

    @Override
    public void viewIsReady() {
        viewIsReady = true;
    }


    @Override
    public void onFailure(@NotNull ErrorDTO errorDTO) {
        //todo
    }

    @Override
    public void onServerUnavailable() {
//todo
    }

    @Override
    public void onUnsuccessfulRequest(@Nullable String message) {
//todo
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
