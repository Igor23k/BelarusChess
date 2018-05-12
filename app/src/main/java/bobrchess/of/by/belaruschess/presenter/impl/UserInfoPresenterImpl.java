package bobrchess.of.by.belaruschess.presenter.impl;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.network.connection.SearchUserConnection;
import bobrchess.of.by.belaruschess.network.connection.UserInfoConnection;
import bobrchess.of.by.belaruschess.presenter.UserInfoPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackUserInfo;
import bobrchess.of.by.belaruschess.view.activity.impl.UserInfoActivity;

/**
 * Created by Igor on 11.04.2018.
 */

public class UserInfoPresenterImpl implements CallBackUserInfo, UserInfoPresenter {

    private UserInfoActivity view;
    private UserInfoConnection userInfoConnection;
    private Boolean viewIsReady = false;

    public UserInfoPresenterImpl() {
        userInfoConnection = new UserInfoConnection();
        userInfoConnection.attachPresenter(this);
    }

    @Override
    public void onResponse(List<TournamentDTO> tournaments) {
        view.displayUserTournaments(tournaments);
    }

    @Override
    public void onConnectionError(Throwable t) {
        view.hideProgress();
        view.onConnectionError();
    }

    @Override
    public void attachView(UserInfoActivity activity) {
        view = activity;
    }

    @Override
    public void readyToLoadUserTournaments() {
        userInfoConnection.getTournaments();
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {
        viewIsReady = true;
    }
}
