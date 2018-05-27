package bobrchess.of.by.belaruschess.presenter.impl;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.GameDTO;
import bobrchess.of.by.belaruschess.network.connection.GameConnection;
import bobrchess.of.by.belaruschess.presenter.GamePresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackGame;
import bobrchess.of.by.belaruschess.view.activity.impl.GameActivity;

/**
 * Created by Igor on 11.04.2018.
 */

public class GamePresenterImpl implements CallBackGame, GamePresenter {

    private GameActivity view;
    private GameConnection gameConnection;

    public GamePresenterImpl() {
        gameConnection = new GameConnection();
        gameConnection.attachPresenter(this);
    }

    @Override
    public void onResponse(GameDTO gameDTO) {
        view.hideProgress();
    }

    @Override
    public void onResponse(List<GameDTO> list) {
        view.hideProgress();
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
    }

    @Override
    public void getGame(Integer id) {
        view.disableButton();
        view.showProgress();
        gameConnection.getGame(id);
    }

    @Override
    public void getGames() {
        view.disableButton();
        view.showProgress();
        gameConnection.getGames();
    }

    @Override
    public void attachView(GameActivity activity) {
        view = activity;
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {

    }
}
