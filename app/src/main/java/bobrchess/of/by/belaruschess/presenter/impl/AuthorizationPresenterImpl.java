package bobrchess.of.by.belaruschess.presenter.impl;

import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.exception.IncorrectEmailException;
import bobrchess.of.by.belaruschess.exception.IncorrectPasswordException;
import bobrchess.of.by.belaruschess.network.Network;
import bobrchess.of.by.belaruschess.presenter.AuthorizationPresenter;
import bobrchess.of.by.belaruschess.presenter.CallBack;
import bobrchess.of.by.belaruschess.view.activity.impl.AuthorizationActivity;

/**
 * Created by Igor on 11.04.2018.
 */

public class AuthorizationPresenterImpl implements CallBack, AuthorizationPresenter {

    private AuthorizationActivity view;
    private Network network;

    public AuthorizationPresenterImpl() {
        network = new Network();
        network.attachView(this);
    }

    @Override
    public void onResponse(CountryDTO countryDTO) {
        view.hideProgress();
        view.onLoginSuccess();
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
        view.onLoginFailed();
    }

    @Override
    public void authorizate() {
        UserDTO userDTO = view.getUserData();
        try {
            validateUserData(userDTO);
            view.showProgress();
            network.getCountry();
        } catch (IncorrectEmailException e) {
            view.showIncorrectEmailText();
            view.onLoginFailed();
        } catch (IncorrectPasswordException e) {
            view.showIncorrectPasswordText();
            view.onLoginFailed();
        }
    }

    /*private class UserAuthorizationTask extends AsyncTask<Void,Void, UserDTO> {
        @Override
        protected UserDTO doInBackground(Void... voids) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            return template.getForObject(Constants.URL.USER_AUTHORIZATION, UserDTO.class);
        }

        @Override
        protected void onPostExecute(UserDTO personDTO) {

            System.out.println("ekk");
        }
    }*/
    private boolean validateUserData(UserDTO userDTO) throws IncorrectEmailException, IncorrectPasswordException {
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new IncorrectEmailException("Incorrect email!");
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
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
