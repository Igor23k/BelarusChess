package bobrchess.of.by.belaruschess.presenter.impl;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.dto.TournamentResultDTO;
import bobrchess.of.by.belaruschess.network.connection.UserInfoConnection;
import bobrchess.of.by.belaruschess.presenter.UserInfoPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackUserInfo;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.PackageModel;
import bobrchess.of.by.belaruschess.view.activity.UserInfoContractView;

/**
 * Created by Igor on 11.04.2018.
 */

public class UserInfoPresenterImpl implements CallBackUserInfo, UserInfoPresenter {

    private UserInfoContractView contractView;
    private UserInfoConnection userInfoConnection;
    private Boolean viewIsReady = false;
    private Integer connectivityStatus = 0;
    private PackageModel packageModel;

    public UserInfoPresenterImpl() {
        userInfoConnection = new UserInfoConnection();
        userInfoConnection.attachPresenter(this);
    }

    @Override
    public void onResponse(@NonNull List<TournamentResultDTO> tournamentResults) {
        contractView.userTournamentsResultAreLoaded(tournamentResults);
    }

    @Override
    public void onFailure(@NonNull ErrorDTO errorDTO) {
        contractView.hideProgress();
        contractView.showToast(errorDTO.getError());
    }

    @Override
    public void attachView(@NonNull UserInfoContractView contractView) {
        this.contractView = contractView;
    }

    @Override
    public void loadUserTournamentsResults() {
        if (viewIsReady) {
            userInfoConnection.getTournamentsResults();
        }
    }

    public void detachView() {
        contractView = null;
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

    public void setPackageModel(PackageModel packageModel) {
        this.packageModel = packageModel;
    }
}
