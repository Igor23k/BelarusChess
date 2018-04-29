package bobrchess.of.by.belaruschess.presenter.impl;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.exception.IncorrectEmailException;
import bobrchess.of.by.belaruschess.exception.IncorrectPasswordException;
import bobrchess.of.by.belaruschess.network.connection.CountryConnection;
import bobrchess.of.by.belaruschess.presenter.CountryPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackCountry;
import bobrchess.of.by.belaruschess.view.activity.impl.AuthorizationActivity;

/**
 * Created by Igor on 11.04.2018.
 */

public class CountryPresenterImpl implements CallBackCountry, CountryPresenter {

    private AuthorizationActivity view;
    private CountryConnection countryConnection;

    public CountryPresenterImpl() {
        countryConnection = new CountryConnection();
        countryConnection.attachView(this);
    }

    @Override
    public void onResponse(CountryDTO countryDTO) {
        view.hideProgress();
        view.onLoginSuccess();
    }

    @Override
    public void onResponse(List<CountryDTO> countryDTO) {
        view.hideProgress();
        view.onLoginSuccess();
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
        view.onLoginFailed();
    }

    @Override
    public void getCountry(Integer id) {
        view.disableButton();
        view.showProgress();
        countryConnection.getCountry(id);

    }

    private boolean validateUserData(UserDTO userDTO) throws IncorrectEmailException, IncorrectPasswordException {
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();

        if (email == null || email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new IncorrectEmailException("Incorrect email!");
        }
        if (password == null || password.isEmpty() || password.length() < 4 || password.length() > 10) {
            throw new IncorrectPasswordException("Incorrect password!");
        }

        return true;
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
