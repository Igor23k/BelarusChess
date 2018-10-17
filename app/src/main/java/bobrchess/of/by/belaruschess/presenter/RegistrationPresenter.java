package bobrchess.of.by.belaruschess.presenter;

import android.view.View;

import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.view.activity.RegistrationContractView;

public interface RegistrationPresenter extends BasePresenter{
    void registration(UserDTO userDTO);
    void loadCoaches();
    void loadRanks();
    void loadCountries();
    void attachView(RegistrationContractView activity);
    void attachViewComponent(View view);
    void setConnectivityStatus(Integer status);
    void setSelectedRankIndex(Integer selectedRankIndex);
    void setSelectedCountryIndex(Integer selectedCountryIndex);
    void setSelectedCoachIndex(Integer selectedCoachIndex);
    void setSelectedGenderIndex(Integer selectedCoachIndex);
    void loadSpinnersData();
}