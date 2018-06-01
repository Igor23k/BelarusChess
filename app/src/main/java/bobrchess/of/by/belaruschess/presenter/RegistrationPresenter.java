package bobrchess.of.by.belaruschess.presenter;

import bobrchess.of.by.belaruschess.view.activity.RegistrationContractView;

/**
 * Created by Igor on 12.04.2018.
 */

public interface RegistrationPresenter {
    void registration();

    void attachView(RegistrationContractView activity);

    void detachView();

    void viewIsReady();
}