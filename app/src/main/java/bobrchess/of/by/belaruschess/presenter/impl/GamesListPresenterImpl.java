package bobrchess.of.by.belaruschess.presenter.impl;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.GameDTO;
import bobrchess.of.by.belaruschess.network.connection.GamesListConnection;
import bobrchess.of.by.belaruschess.presenter.GamesListPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackGamesList;
import bobrchess.of.by.belaruschess.view.activity.impl.GamesListActivity;

/**
 * Created by Igor on 04.05.2018.
 */

public class GamesListPresenterImpl implements CallBackGamesList, GamesListPresenter {

    private GamesListActivity view;
    private GamesListConnection gameslistConnection;
    private Boolean viewIsReady = false;

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
        String text = view.getSearchText();
        gameslistConnection.searchGames(text);
    }

    @Override
    public void attachView(GamesListActivity activity) {
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
    public void onResponse(List<GameDTO> gamesDTO) {
        view.showGames(gamesDTO);
    }

    @Override
    public void onConnectionError(Throwable t) {
        view.hideProgress();
        view.onConnectionError();
    }
}
