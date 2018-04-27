package bobrchess.of.by.belaruschess.presenter;

import bobrchess.of.by.belaruschess.view.activity.impl.RegistrationActivity;

/**
 * Created by Igor on 12.04.2018.
 */

public interface RegistrationPresenter {
    void registrate();

    void attachView(RegistrationActivity activity);

    void detachView();

    void viewIsReady();
}