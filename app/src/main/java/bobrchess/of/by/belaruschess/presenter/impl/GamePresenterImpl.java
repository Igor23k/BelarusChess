package bobrchess.of.by.belaruschess.presenter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.dto.GameDTO;
import bobrchess.of.by.belaruschess.network.connection.GameConnection;
import bobrchess.of.by.belaruschess.presenter.GamePresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackGame;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.PackageModel;
import bobrchess.of.by.belaruschess.view.activity.impl.GameActivity;

/**
 * Created by Igor on 11.04.2018.
 */

public class GamePresenterImpl implements CallBackGame, GamePresenter {

    private GameActivity view;
    private GameConnection gameConnection;
    private Boolean viewIsReady = false;
    private PackageModel packageModel;
    private Integer connectivityStatus = 0;

    public GamePresenterImpl() {
        gameConnection = new GameConnection();
        gameConnection.attachPresenter(this);
    }

    @Override
    public void onResponse(@NotNull GameDTO gameDto) {
        view.hideProgress();
    }

    @Override
    public void onResponse(@NotNull List<GameDTO> list) {
        view.hideProgress();
    }

    @Override
    public void onFailure(@NotNull ErrorDTO errorDTO) {
        view.hideProgress();
        view.showToast(errorDTO.getError());
    }

    @Override
    public void getGame(Integer id) {
        if (viewIsReady) {
            view.showProgress();
            gameConnection.getGame(id);
        }
    }

    @Override
    public void getGames() {
        if (viewIsReady) {
            view.showProgress();
            gameConnection.getGames();
        }
    }

    @Override
    public void attachView(@NotNull GameActivity activity) {
        view = activity;
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {
        viewIsReady = true;
    }

    @Override
    public void onServerUnavailable() {

    }

    @Override
    public void onUnsuccessfulRequest(@Nullable String message) {

    }

    @Override
    public void setConnectivityStatus(Integer status) {
        this.connectivityStatus = status;
    }

    @Override
    public boolean isConnected(int status) {
        return Util.Companion.isConnected(status);
    }//todo remove
}
