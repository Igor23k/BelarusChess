package bobrchess.of.by.belaruschess.presenter.impl;

import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import bobrchess.of.by.belaruschess.R;
import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.network.connection.internal.SearchUserConnection;
import bobrchess.of.by.belaruschess.presenter.SearchUserPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackSearchUser;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.PackageModel;
import bobrchess.of.by.belaruschess.view.activity.SearchUserContractView;
import butterknife.BindView;

/**
 * Created by Igor on 04.05.2018.
 */

public class SearchUserPresenterImpl implements CallBackSearchUser, SearchUserPresenter {

    private SearchUserContractView view;
    private SearchUserConnection userConnection;
    private Boolean viewIsReady = false;
    private PackageModel packageModel;
    private Integer connectivityStatus = 0;


    @BindView(R.id.t_link_registration)
    TextView registrationLink;

    public SearchUserPresenterImpl() {
        userConnection = new SearchUserConnection();
        userConnection.attachPresenter(this);
    }

    @Override
    public void loadUsers() {
        userConnection.getUsers(10);
    }//todo

    @Override
    public void loadUsers(Integer count) {
        userConnection.getUsers(count);
    }

    @Override
    public void searchUsers(@NotNull String text) {
        if(viewIsReady) {
            userConnection.searchUsers(text);
        }
    }

    @Override
    public void attachView(@NotNull SearchUserContractView searchUserContractView) {
        view = searchUserContractView;
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
    public void onResponse(@NotNull List<? extends UserDTO> users) {
        view.showUsers(users);
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

    public PackageModel getPackageModel() {
        return packageModel;
    }

    public void setPackageModel(PackageModel packageModel) {
        this.packageModel = packageModel;
    }

    @Override
    public boolean isConnected(int status) {
        return Util.Companion.isConnected(status);
    }//todo remove
}
