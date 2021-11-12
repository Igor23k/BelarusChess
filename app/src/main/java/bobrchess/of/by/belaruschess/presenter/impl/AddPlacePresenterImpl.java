package bobrchess.of.by.belaruschess.presenter.impl;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.extended.ExtendedPlaceDTO;
import bobrchess.of.by.belaruschess.exception.IncorrectDataException;
import bobrchess.of.by.belaruschess.network.connection.internal.AddPlaceConnection;
import bobrchess.of.by.belaruschess.presenter.AddPlacePresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAddPlace;
import bobrchess.of.by.belaruschess.util.Constants;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.util.Validator;
import bobrchess.of.by.belaruschess.view.activity.AddPlaceContractView;
import bobrchess.of.by.belaruschess.view.activity.PackageModel;

import static bobrchess.of.by.belaruschess.util.Constants.TOKEN;
import static javax.xml.bind.JAXBIntrospector.getValue;

@InjectViewState
public class AddPlacePresenterImpl extends MvpPresenter<AddPlaceContractView> implements CallBackAddPlace, AddPlacePresenter {

    private AddPlaceContractView view;
    private AddPlaceConnection addPlaceConnection;
    private Boolean viewIsReady = false;
    List<String> a = new ArrayList<>();
    private Integer selectedCountryIndex = 0;
    private Map<Integer, CountryDTO> countriesIndexes = new HashMap<>();
    private PackageModel packageModel;
    private Integer connectivityStatus = 0;

    public AddPlacePresenterImpl() {
    }

    public AddPlacePresenterImpl(AddPlaceContractView view) {
        addPlaceConnection = new AddPlaceConnection();
        addPlaceConnection.attachPresenter(this);
        attachView(view);
        view.showProgress();
    }

    @Override
    public void onResponse(@NonNull PlaceDTO placeDTO) {
        view.hideProgress();
        view.enableButton();
        view.placeAdded(placeDTO);
    }

    @Override
    public void onCountryResponse(@NonNull List<CountryDTO> countries) {
        saveCountriesIndexes(countries);
        view.setCountrySpinnerAdapter(countries);
        viewIsReady();
    }

    @Override
    public void onResponse(int removedPlaceId) {
        if (removedPlaceId != 0) {
            view.placeRemoved(removedPlaceId);
        } else {
            view.showToast(Constants.Companion.getINTERNAL_SERVER_ERROR());
        }
    }

    @Override
    public void onFailure(@NonNull ErrorDTO errorDTO) {
        view.showToast(errorDTO.getError());
    }

    @Override
    public void addPlace(@NonNull ExtendedPlaceDTO placeDTO) {
        if (viewIsReady) {
            view.disableButton();
            try {
                placeDTO.setSelectedCountryIndex(selectedCountryIndex);
                Validator.INSTANCE.validatePlaceData(placeDTO);
                placeDTO.setCountry(countriesIndexes.get(selectedCountryIndex - 1));
                view.disableButton();
                view.showProgress();
                addPlaceConnection.addPlace(new PlaceDTO(placeDTO), packageModel.getValue(TOKEN));
            } catch (IncorrectDataException e) {
                view.showToast(e.getLocalizedMessage());
            } finally {
                view.hideProgress();
                view.enableButton();
            }
        }
    }

    public void attachView(@NonNull AddPlaceContractView activity) {
        view = activity;
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {
        viewIsReady = true;
    }

    @Override
    public void removePlace(Integer id) {
        if (viewIsReady) {
            view.showProgress();
            addPlaceConnection.removePlace(id, packageModel.getValue(TOKEN));
        }
    }

    @Override
    public void loadCountries() {
        addPlaceConnection.getCountries();
    }

    public void setSelectedCountryIndex(Integer selectedCountryIndex) {
        this.selectedCountryIndex = selectedCountryIndex;
    }

    private void saveCountriesIndexes(List<CountryDTO> countries) {
        for (int i = 0; i < countries.size(); i++) {
            countriesIndexes.put(i, countries.get(i));
        }
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

    public void setPackageModel(PackageModel packageModel) {
        this.packageModel = packageModel;
    }

    @Override
    public boolean isConnected(int status) {
        return Util.Companion.isConnected(status);
    }
}
