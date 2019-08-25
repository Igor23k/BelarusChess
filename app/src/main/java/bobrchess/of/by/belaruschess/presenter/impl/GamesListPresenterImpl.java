package bobrchess.of.by.belaruschess.presenter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.dto.GameDTO;
import bobrchess.of.by.belaruschess.network.connection.GamesListConnection;
import bobrchess.of.by.belaruschess.presenter.GamesListPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackGamesList;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.PackageModel;
import bobrchess.of.by.belaruschess.view.activity.impl.GamesListActivity;

/**
 * Created by Igor on 04.05.2018.
 */

public class GamesListPresenterImpl implements CallBackGamesList, GamesListPresenter {

    private GamesListActivity view;
    private GamesListConnection gameslistConnection;
    private Boolean viewIsReady = false;
    private PackageModel packageModel;
    private Integer connectivityStatus = 0;

    public GamesListPresenterImpl() {
        gameslistConnection = new GamesListConnection();
        gameslistConnection.attachPresenter(this);
    }

    @Override
    public void loadGames() {
        gameslistConnection.getGames();
    }

    @Override
    public void loadGames(Integer count) {
        gameslistConnection.getGames(count);
    }

    @Override
    public void searchGames() {
        if (viewIsReady) {
            String text = view.getSearchText();
            gameslistConnection.searchGames(text);
        }
    }

    @Override
    public void attachView(@NotNull GamesListActivity activity) {
        view = activity;
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
    public void onResponse(@NotNull List<GameDTO> gamesDTO) {
        view.showGames(gamesDTO);
    }

    @Override
    public void onFailure(@NotNull ErrorDTO errorDTO) {
        view.hideProgress();
        view.showToast(errorDTO.getError());
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
