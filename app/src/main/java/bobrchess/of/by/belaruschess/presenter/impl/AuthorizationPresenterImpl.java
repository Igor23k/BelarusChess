package bobrchess.of.by.belaruschess.presenter.impl;

import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.exception.IncorrectEmailException;
import bobrchess.of.by.belaruschess.exception.IncorrectPasswordException;
import bobrchess.of.by.belaruschess.network.connection.AuthorizationConnection;
import bobrchess.of.by.belaruschess.presenter.AuthorizationPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAuthorization;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.impl.AuthorizationActivity;

/**
 * Created by Igor on 11.04.2018.
 */

public class AuthorizationPresenterImpl implements CallBackAuthorization, AuthorizationPresenter {

    private AuthorizationActivity view;
    private AuthorizationConnection userConnection;
    private Boolean viewIsReady = false;

    public AuthorizationPresenterImpl() {
        userConnection = new AuthorizationConnection();
        userConnection.attachPresenter(this);
    }

    @Override
    public void onResponse(UserDTO userDTO) {
        view.hideProgress();
        view.enableButton();
        view.startActivity(userDTO);
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
        view.enableButton();
        view.showToast(t.getLocalizedMessage());
    }

    @Override
    public void authorization() {
        if(viewIsReady) {
            view.disableButton();
            UserDTO userDTO = view.getUserData();
            try {
                validateUserData(userDTO);
                userDTO.setPassword(Util.getEncodedPassword(userDTO.getPassword()));
                view.disableButton();
                view.showProgress();
                userConnection.authorization(userDTO);
            } catch (IncorrectEmailException e) {
                view.showIncorrectEmailText();
            } catch (IncorrectPasswordException e) {
                view.showIncorrectPasswordText();
            } finally {
                view.hideProgress();
                view.enableButton();
            }
        }
    }

    private void validateUserData(UserDTO userDTO) throws IncorrectEmailException, IncorrectPasswordException {
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();

        if (email == null || email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new IncorrectEmailException();
        }
        if (password == null || password.isEmpty() || password.length() < 4) {
            throw new IncorrectPasswordException();
        }
    }

    public void attachView(AuthorizationActivity activity) {
        view = activity;
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {
        viewIsReady = true;
    }
}
