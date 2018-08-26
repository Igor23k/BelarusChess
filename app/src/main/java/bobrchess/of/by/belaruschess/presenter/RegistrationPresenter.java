package bobrchess.of.by.belaruschess.presenter;

import bobrchess.of.by.belaruschess.view.activity.RegistrationContractView;

/**
 * Created by Igor on 12.04.2018.
 */

public interface RegistrationPresenter extends BasePresenter{
    void registration();
    void loadCoaches();
    void loadRanks();
    void loadCountries();
    void attachView(RegistrationContractView activity);
    void setSelectedRankIndex(Integer selectedRankIndex);
    void setSelectedCountryIndex(Integer selectedCountryIndex);
    void setSelectedCoachIndex(Integer selectedCoachIndex);
}