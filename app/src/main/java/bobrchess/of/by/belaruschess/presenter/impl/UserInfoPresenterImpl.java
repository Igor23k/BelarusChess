package bobrchess.of.by.belaruschess.presenter.impl;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.network.connection.UserInfoConnection;
import bobrchess.of.by.belaruschess.presenter.UserInfoPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackUserInfo;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.impl.UserInfoActivity;

/**
 * Created by Igor on 11.04.2018.
 */

public class UserInfoPresenterImpl implements CallBackUserInfo, UserInfoPresenter {

    private UserInfoActivity view;
    private UserInfoConnection userInfoConnection;
    private Boolean viewIsReady = false;
    private Integer connectivityStatus = 0;

    public UserInfoPresenterImpl() {
        userInfoConnection = new UserInfoConnection();
        userInfoConnection.attachPresenter(this);
    }

    @Override
    public void onResponse(@NonNull List<TournamentDTO> tournaments) {
        view.displayUserTournaments(tournaments);
    }

    @Override
    public void onFailure(@NonNull ErrorDTO errorDTO) {
        view.hideProgress();
        view.showToast(errorDTO.getError());
    }

    @Override
    public void attachView(@NonNull UserInfoActivity activity) {
        view = activity;
    }

    @Override
    public void loadUserTournaments() {
        if (viewIsReady) {
            userInfoConnection.getTournaments();
        }
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {
        viewIsReady = true;
    }

    @Override
    public void setConnectivityStatus(Integer status) {
        this.connectivityStatus = status;
    }

    @Override
    public void onServerUnavailable() {

    }

    @Override
    public void onUnsuccessfulRequest(@Nullable String message) {

    }

    @Override
    public boolean isConnected(int status) {
        return Util.Companion.isConnected(status);
    }//todo remove
}
