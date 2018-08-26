package bobrchess.of.by.belaruschess.presenter.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.RankDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.exception.IncorrectEmailException;
import bobrchess.of.by.belaruschess.exception.IncorrectPasswordException;
import bobrchess.of.by.belaruschess.network.connection.RegistrationConnection;
import bobrchess.of.by.belaruschess.presenter.RegistrationPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackRegistration;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.RegistrationContractView;

/**
 * Created by Igor on 11.04.2018.
 */

public class RegistrationPresenterImpl implements CallBackRegistration, RegistrationPresenter {

    private RegistrationContractView view;
    private RegistrationConnection userConnection;
    private Boolean viewIsReady = false;
    private List<RankDTO> ranks;
    private List<CountryDTO> countries;
    private List<UserDTO> coaches;
    private Integer selectedRankIndex;
    private Integer selectedCountryIndex;
    private Integer selectedCoachIndex;
    private Map<Integer, RankDTO> ranksIndexes = new HashMap<>();
    private Map<Integer, CountryDTO> countriesIndexes = new HashMap<>();
    private Map<Integer, UserDTO> coachesIndexes = new HashMap<>();

    public RegistrationPresenterImpl() {
        userConnection = new RegistrationConnection();
        userConnection.attachPresenter(this);
    }

    @Override
    public void onResponse(UserDTO userDTO) {
        view.hideProgress();
        view.enableButton();
        view.startActivity(userDTO);
    }

    @Override
    public void onCoachResponse(List<UserDTO> coaches) {
        this.coaches = coaches;
        saveCoachesIndexes(coaches);
        view.hideProgress();
        view.enableButton();
        view.setCoachSpinnerAdapter(Util.getUsersNames(coaches));
    }

    @Override
    public void onRankResponse(List<RankDTO> ranks) {
        this.ranks = ranks;
        saveRanksIndexes(ranks);
        view.hideProgress();
        view.enableButton();
        view.setRankSpinnerAdapter(Util.getRanksNames(ranks));
    }

    @Override
    public void onCountryResponse(List<CountryDTO> countries) {
        this.countries = countries;
        saveCountriesIndexes(countries);
        view.hideProgress();
        view.enableButton();
        view.setCountrySpinnerAdapter(Util.getCountriesNames(countries));
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
        view.enableButton();
        view.showToast(t.getLocalizedMessage());
    }

    @Override
    public void registration() {
        if (viewIsReady) {
            view.disableButton();
            UserDTO userDTO = view.getUserData();
            userDTO.setRank(ranksIndexes.get(selectedRankIndex));
            userDTO.setCountry(countriesIndexes.get(selectedCountryIndex));
            userDTO.setCoach(coachesIndexes.get(selectedCoachIndex));
            userDTO.setBirthday("ДОБАВИТЬ"); // bug добавить выбор даты рождения и 2 строки ниже тоже
            userDTO.setBeCoach(true);
            userDTO.setBeOrganizer(true);
            try {
                validateUserData(userDTO);
                userDTO.setPassword(Util.getEncodedPassword(userDTO.getPassword()));
                view.enableButton();
                view.showProgress();
                userConnection.registration(userDTO);
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

    @Override
    public void loadCoaches() {
        userConnection.getCoaches();
    }

    @Override
    public void loadRanks() {
        userConnection.getRanks();
    }

    @Override
    public void loadCountries() {
        userConnection.getCountries();
    }

    private boolean validateUserData(UserDTO userDTO) throws IncorrectEmailException, IncorrectPasswordException {
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        String name = userDTO.getName();
        String surname = userDTO.getSurname();
        String patronymic = userDTO.getPatronymic();
        Integer rating = userDTO.getRating();
        //  Date birthdate = userDTO.getBirthday();

     /*   if (email == null || email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new IncorrectEmailException(INCORRECT_EMAIL);
        }
        if (password == null || password.isEmpty() || password.length() < 4) {
            throw new IncorrectPasswordException(INCORRECT_PASSWORD);
        }*/

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

    public void setSelectedRankIndex(Integer selectedRankIndex) {
        this.selectedRankIndex = selectedRankIndex;
    }

    public void setSelectedCountryIndex(Integer selectedCountryIndex) {
        this.selectedCountryIndex = selectedCountryIndex;
    }

    public void setSelectedCoachIndex(Integer selectedCoachIndex) {
        this.selectedCoachIndex = selectedCoachIndex;
    }

    private void saveRanksIndexes(List<RankDTO> ranks) {
        for (int i = 0; i < ranks.size(); i++) {
            ranksIndexes.put(i,ranks.get(i));
        }
    }

    private void saveCountriesIndexes(List<CountryDTO> countries) {
        for (int i = 0; i < countries.size(); i++) {
            countriesIndexes.put(i,countries.get(i));
        }
    }

    private void saveCoachesIndexes(List<UserDTO> coaches) {
        for (int i = 0; i < coaches.size(); i++) {
            coachesIndexes.put(i,coaches.get(i));
        }
    }
}
