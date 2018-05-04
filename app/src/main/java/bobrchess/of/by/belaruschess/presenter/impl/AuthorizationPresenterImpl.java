package bobrchess.of.by.belaruschess.presenter.impl;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.exception.IncorrectEmailException;
import bobrchess.of.by.belaruschess.exception.IncorrectPasswordException;
import bobrchess.of.by.belaruschess.network.connection.UserConnection;
import bobrchess.of.by.belaruschess.presenter.AuthorizationPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackUser;
import bobrchess.of.by.belaruschess.view.activity.impl.AuthorizationActivity;

/**
 * Created by Igor on 11.04.2018.
 */

public class AuthorizationPresenterImpl implements CallBackUser, AuthorizationPresenter {

    private AuthorizationActivity view;
    private UserConnection userConnection;

    public AuthorizationPresenterImpl() {
        userConnection = new UserConnection();
        userConnection.attachView(this);
    }

    @Override
    public void onResponse(UserDTO userDTO) {
        view.hideProgress();
        view.onLoginSuccess();
    }

    @Override
    public void onResponse(List<UserDTO> usersDTO) {

    }

    @Override
    public void onAuthorizationFailure(Throwable t) {
        view.hideProgress();
        view.showToast(t.getLocalizedMessage());
        view.onLoginFailed();
    }

    @Override
    public void onConnectionError(Throwable t) {
        view.hideProgress();
        view.onConnectionError();
    }

    @Override
    public void authorizate() {
        view.disableButton();
        UserDTO userDTO = view.getUserData();
        try {
            validateUserData(userDTO);
            view.disableButton();
            view.showProgress();
            userConnection.authorizate(userDTO);
        } catch (IncorrectEmailException e) {
            view.showIncorrectEmailText();
            view.onLoginFailed();
        } catch (IncorrectPasswordException e) {
            view.showIncorrectPasswordText();
            view.onLoginFailed();
        }
    }

    private void validateUserData(UserDTO userDTO) throws IncorrectEmailException, IncorrectPasswordException {
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();

        if (email == null || email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new IncorrectEmailException("Incorrect email!");
        }
        if (password == null || password.isEmpty() || password.length() < 4 || password.length() > 10) {
            throw new IncorrectPasswordException("Incorrect password!");
        }
    }

    public void attachView(AuthorizationActivity activity) {
        view = activity;
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {

    }
}
