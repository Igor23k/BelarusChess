package bobrchess.of.by.belaruschess.presenter.impl;

import java.util.Date;

import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.exception.IncorrectEmailException;
import bobrchess.of.by.belaruschess.exception.IncorrectPasswordException;
import bobrchess.of.by.belaruschess.network.connection.UserConnection;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackUser;
import bobrchess.of.by.belaruschess.presenter.RegistrationPresenter;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.impl.RegistrationActivity;

/**
 * Created by Igor on 11.04.2018.
 */

public class RegistrationPresenterImpl implements CallBackUser, RegistrationPresenter {

    private RegistrationActivity view;
    private UserConnection userConnection;

    public RegistrationPresenterImpl() {
        userConnection = new UserConnection();
        userConnection.attachView(this);
    }

    @Override
    public void onResponse(UserDTO userDTO) {
        view.hideProgress();
        view.onLoginSuccess();
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
        view.showToast(t.getMessage());
    }

    @Override
    public void registrate() {
        view.disableButton();
        UserDTO userDTO = view.getUserData();
        //userDTO.setPassword(Util.getEncodedPassword(userDTO.getPassword()));
        try {
            validateUserData(userDTO);
            view.enableButton();
            view.showProgress();
            userConnection.registrate(userDTO);
        } catch (IncorrectEmailException e) {
            view.showIncorrectEmailText();
            view.onLoginFailed();
        } catch (IncorrectPasswordException e) {
            view.showIncorrectPasswordText();
            view.onLoginFailed();
        }
    }

    private boolean validateUserData(UserDTO userDTO) throws IncorrectEmailException, IncorrectPasswordException {
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        String name = userDTO.getName();
        String surname = userDTO.getSurname();
        String patronymic = userDTO.getPatronymic();
        Integer rating = userDTO.getRating();
      //  Date birthdate = userDTO.getBirthday();

        if (email == null || email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new IncorrectEmailException("Incorrect email!");
        }
        if (password == null || password.isEmpty() || password.length() < 4 || password.length() > 10) {
            throw new IncorrectPasswordException("Incorrect password!");
        }

        return true;
    }

    public void attachView(RegistrationActivity activity) {
        view = activity;
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {

    }
}
