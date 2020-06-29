package bobrchess.of.by.belaruschess.presenter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.dto.RankDTO;
import bobrchess.of.by.belaruschess.network.connection.internal.RankConnection;
import bobrchess.of.by.belaruschess.presenter.RankPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackRank;
import bobrchess.of.by.belaruschess.view.activity.RankPresenterCallBack;

/**
 * Created by Igor on 11.04.2018.
 */

public class RankPresenterImpl implements CallBackRank, RankPresenter {

    private RankConnection rankConnection;
    private RankPresenterCallBack rankPresenterCallBack;

    public RankPresenterImpl() {
        rankConnection = new RankConnection();
        rankConnection.attachPresenter(this);
    }

    @Override
    public void onResponse(@NotNull RankDTO rankDto) {
        rankPresenterCallBack.rankIsLoaded(rankDto);
    }

    @Override
    public void onResponse(@NotNull List<RankDTO> list) {
        rankPresenterCallBack.ranksAreLoaded(list);
    }

    @Override
    public void onFailure(@NotNull ErrorDTO errorDTO) {//todo
    }

    @Override
    public void getRank(Integer id) {
        rankConnection.getRank(id);
    }

    @Override
    public void getRanks() {
        rankConnection.getRanks();
    }

    @Override
    public void onServerUnavailable() {

    }

    @Override
    public void onUnsuccessfulRequest(@Nullable String message) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void viewIsReady() {

    }

    @Override
    public void setConnectivityStatus(@Nullable Integer status) {

    }

    @Override
    public boolean isConnected(int status) {
        return false;
    }

    @Override
    public void attachView(@NotNull RankPresenterCallBack rankPresenterCallBack) {
        this.rankPresenterCallBack = rankPresenterCallBack;
    }


}
