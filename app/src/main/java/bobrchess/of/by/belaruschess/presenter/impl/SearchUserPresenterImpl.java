package bobrchess.of.by.belaruschess.presenter.impl;

import android.widget.TextView;

import java.util.List;

import bobrchess.of.by.belaruschess.R;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.network.connection.SearchUserConnection;
import bobrchess.of.by.belaruschess.presenter.SearchUserPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackSearchUser;
import bobrchess.of.by.belaruschess.view.activity.impl.SearchUserActivity;
import butterknife.BindView;

/**
 * Created by Igor on 04.05.2018.
 */

public class SearchUserPresenterImpl implements CallBackSearchUser, SearchUserPresenter {

    private SearchUserActivity view;
    private SearchUserConnection userConnection;
    private Boolean viewIsReady = false;

    @BindView(R.id.t_link_registration)
    TextView registrationLink;

    public SearchUserPresenterImpl() {
        userConnection = new SearchUserConnection();
        userConnection.attachView(this);
    }

    @Override
    public void loadUsers() {
        userConnection.getUsers();
    }

    @Override
    public void loadUsers(Integer count) {
        userConnection.getUsers(count);
    }

    @Override
    public void searchUsers() {
        String text = view.getSearchText();
        userConnection.searchUsers(text);
    }

    @Override
    public void attachView(SearchUserActivity activity) {
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
    public void onResponse(List<UserDTO> usersDTO) {
        view.showUsers(usersDTO);
    }

    @Override
    public void onConnectionError(Throwable t) {
        view.hideProgress();
        view.onConnectionError();
    }
}
