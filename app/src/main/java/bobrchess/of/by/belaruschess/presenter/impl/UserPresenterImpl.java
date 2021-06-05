package bobrchess.of.by.belaruschess.presenter.impl;

import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bobrchess.of.by.belaruschess.R;
import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.dto.RankDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.dto.extended.ExtendedUserDTO;
import bobrchess.of.by.belaruschess.exception.IncorrectDataException;
import bobrchess.of.by.belaruschess.network.connection.internal.SearchUserConnection;
import bobrchess.of.by.belaruschess.presenter.UserPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackSearchUser;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.util.Validator;
import bobrchess.of.by.belaruschess.view.activity.PackageModel;
import bobrchess.of.by.belaruschess.view.activity.UserContractView;
import butterknife.BindView;

import static bobrchess.of.by.belaruschess.util.Constants.TOKEN;

public class UserPresenterImpl implements CallBackSearchUser, UserPresenter {

    private UserContractView view;
    private SearchUserConnection userConnection;
    private Boolean viewIsReady = false;
    private PackageModel packageModel;
    private Integer connectivityStatus = 0;
    private Integer selectedRankIndex = 0;
    private Integer selectedCountryIndex = 0;
    private Integer selectedCoachIndex = 0;
    private Integer selectedGenderIndex = 0;
    private Map<Integer, RankDTO> ranksIndexes = new HashMap<>();
    private Map<Integer, CountryDTO> countriesIndexes = new HashMap<>();
    private Map<Integer, UserDTO> coachesIndexes = new HashMap<>();


    @BindView(R.id.t_link_registration)
    TextView registrationLink;

    public UserPresenterImpl() {
        userConnection = new SearchUserConnection();
        userConnection.attachPresenter(this);
    }

    @Override
    public void loadUserById(int id) {
        view.showProgress();
        userConnection.getUserById(packageModel.getValue(TOKEN), id);
    }

    @Override
    public void loadReferees() {
        view.showProgress();
        userConnection.getReferees(packageModel.getValue(TOKEN));
    }

    @Override
    public void loadCoaches() {
        view.showProgress();
        userConnection.getCoaches(packageModel.getValue(TOKEN));
    }

    @Override
    public void loadUsers() {
        view.showProgress();
        userConnection.getUsers(10);
    }//todo

    @Override
    public void loadUsers(Integer count) {
        view.showProgress();
        userConnection.getUsers(count);
    }

    @Override
    public void searchUsers(@NotNull String text) {
        if (viewIsReady) {
            view.showProgress();
            userConnection.searchUsers(text);
        }
    }

    @Override
    public void updateUser(@NotNull ExtendedUserDTO user) {
        if (viewIsReady) {
            //view.disableButton();
            try {
                user.setSelectedCoachIndex(selectedCoachIndex - 1);
                user.setSelectedCountryIndex(selectedCountryIndex);
                user.setSelectedRankIndex(selectedRankIndex);
                user.setSelectedGenderIndex(selectedGenderIndex);
                Validator.INSTANCE.validateUserData(user, false);
                user.setCoach(coachesIndexes.get(selectedCoachIndex - 1));
                user.setCountry(countriesIndexes.get(selectedCountryIndex));
                user.setRank(ranksIndexes.get(selectedRankIndex));
                user.setBeMale(selectedGenderIndex == 0);
                //user.setCoach(null);
                //   view.disableButton();
                view.showProgress();
                userConnection.updateUser(new UserDTO(user), packageModel.getValue(TOKEN));
            } catch (IncorrectDataException e) {
                view.showToast(e.getLocalizedMessage());//todo проверить что норм показывается и остальные так же плэйс и тд
                view.hideProgress();
            //    view.enableButton();
            }
        }
    }

    @Override
    public void attachView(@NotNull UserContractView userContractView) {
        view = userContractView;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void viewIsReady() {
        viewIsReady = true;
    }

    @Override
    public void onResponse(@NotNull List<? extends UserDTO> users) {
        view.showUsers(users);
        view.hideProgress();
    }

    @Override
    public void onResponse(@NotNull UserDTO user) {
        view.showUser(user);
        view.hideProgress();
    }

    @Override
    public void onFailure(@NotNull ErrorDTO errorDTO) {
        view.hideProgress();
        view.showToast(errorDTO.getError());
    }

    @Override
    public void onServerUnavailable() {
        view.hideProgress();
    }

    @Override
    public void onUnsuccessfulRequest(@Nullable String message) {
        view.hideProgress();
    }

    @Override
    public void setConnectivityStatus(Integer status) {
        this.connectivityStatus = status;
    }

    public PackageModel getPackageModel() {
        return packageModel;
    }

    public void setPackageModel(PackageModel packageModel) {
        this.packageModel = packageModel;
    }

    @Override
    public boolean isConnected(int status) {
        return Util.Companion.isConnected(status);
    }

    @Override
    public void setSelectedRankIndex(int selectedRankIndex) {
        this.selectedRankIndex = selectedRankIndex;
    }

    @Override
    public void setSelectedCountryIndex(int selectedCountryIndex) {
        this.selectedCountryIndex = selectedCountryIndex;
    }

    @Override
    public void setSelectedCoachIndex(int selectedCoachIndex) {
        this.selectedCoachIndex = selectedCoachIndex;
    }

    @Override
    public void setSelectedGenderIndex(int selectedGenderIndex) {
        this.selectedGenderIndex = selectedGenderIndex;
    }

    @Override
    public void saveRanksIndexes(@Nullable List<RankDTO> ranks) {
        if (ranks != null) {
            for (int i = 0; i < ranks.size(); i++) {
                ranksIndexes.put(i, ranks.get(i));
            }
        }
    }

    @Override
    public void saveCoachesIndexes(@Nullable List<? extends UserDTO> coaches) {
        if (coaches != null) {
            for (int i = 0; i < coaches.size(); i++) {
                coachesIndexes.put(i, coaches.get(i));
            }
        }
    }

    @Override
    public void saveCountriesIndexes(List<CountryDTO> countries) {
        if (countries != null) {
            for (int i = 0; i < countries.size(); i++) {
                countriesIndexes.put(i, countries.get(i));
            }
        }
    }

}
