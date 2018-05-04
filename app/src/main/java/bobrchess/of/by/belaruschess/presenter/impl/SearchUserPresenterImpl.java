package bobrchess.of.by.belaruschess.presenter.impl;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.network.connection.UserConnection;
import bobrchess.of.by.belaruschess.presenter.SearchUserPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackUser;
import bobrchess.of.by.belaruschess.view.activity.impl.SearchUserActivity;

/**
 * Created by Igor on 04.05.2018.
 */

public class SearchUserPresenterImpl implements CallBackUser, SearchUserPresenter {

    private SearchUserActivity view;
    private UserConnection userConnection;

    public SearchUserPresenterImpl() {
        userConnection = new UserConnection();
        userConnection.attachView(this);
    }

    @Override
    public void getUsers() {
        userConnection.getUsers();
    }

    @Override
    public void attachView(SearchUserActivity activity) {
        view = activity;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void viewIsReady() {

    }

    @Override
    public void onResponse(UserDTO userDTO) {

    }

    @Override
    public void onResponse(List<UserDTO> usersDTO) {
        view.showUsers(usersDTO);
    }

    @Override
    public void onAuthorizationFailure(Throwable t) {

    }

    @Override
    public void onConnectionError(Throwable t) {

    }
}
