package bobrchess.of.by.belaruschess.presenter.impl;

import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.exception.IncorrectEmailException;
import bobrchess.of.by.belaruschess.exception.IncorrectPasswordException;
import bobrchess.of.by.belaruschess.network.connection.RegistrationConnection;
import bobrchess.of.by.belaruschess.presenter.RegistrationPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackRegistration;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.RegistrationContractView;
import bobrchess.of.by.belaruschess.view.activity.impl.RegistrationActivity;

import static bobrchess.of.by.belaruschess.util.Constants.*;

/**
 * Created by Igor on 11.04.2018.
 */

public class RegistrationPresenterImpl implements CallBackRegistration, RegistrationPresenter {

    private RegistrationContractView view;
    private RegistrationConnection userConnection;
    private Boolean viewIsReady = false;

    public RegistrationPresenterImpl() {
        userConnection = new RegistrationConnection();
        userConnection.attachPresenter(this);
    }

    @Override
    public void onResponse(UserDTO userDTO) {
        view.hideProgress();
        view.onRegistrationSuccess(userDTO);
    }

    @Override
    public void onRegistrationFailure(Throwable t) {
        view.hideProgress();
        view.showToast(t.getLocalizedMessage());
        view.onRegistrationFailed();
    }

    @Override
    public void onConnectionError(Throwable t) {
        view.hideProgress();
        view.showToast(t.getMessage());
    }

    @Override
    public void registration() {
        view.disableButton();
        UserDTO userDTO = view.getUserData();
        try {
            validateUserData(userDTO);
            userDTO.setPassword(Util.getEncodedPassword(userDTO.getPassword()));
            view.enableButton();
            view.showProgress();
            userConnection.registration(userDTO);
        } catch (IncorrectEmailException e) {
            view.showIncorrectEmailText();
            view.onRegistrationFailed();
        } catch (IncorrectPasswordException e) {
            view.showIncorrectPasswordText();
            view.onRegistrationFailed();
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
            throw new IncorrectEmailException(INCORRECT_EMAIL);
        }
        if (password == null || password.isEmpty() || password.length() < 4) {
            throw new IncorrectPasswordException(INCORRECT_PASSWORD);
        }

        return true;
    }

    public void attachView(RegistrationContractView activity) {
        view = activity;
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {
        viewIsReady = true;
    }
}
