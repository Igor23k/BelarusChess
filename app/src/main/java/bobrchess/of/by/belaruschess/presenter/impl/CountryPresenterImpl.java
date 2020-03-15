package bobrchess.of.by.belaruschess.presenter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.network.connection.CountryConnection;
import bobrchess.of.by.belaruschess.presenter.CountryPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackCountry;
import bobrchess.of.by.belaruschess.view.activity.CountryPresenterCallBack;

/**
 * Created by Igor on 11.04.2018.
 */

public class CountryPresenterImpl implements CallBackCountry, CountryPresenter {

    private CountryConnection countryConnection;
    private CountryPresenterCallBack countryPresenterCallBack;

    public CountryPresenterImpl() {
        countryConnection = new CountryConnection();
        countryConnection.attachPresenter(this);
    }

    @Override
    public void onResponse(@NotNull CountryDTO countryDTO) {
        countryPresenterCallBack.countryIsLoaded(countryDTO);
    }

    @Override
    public void onResponse(@NotNull List<CountryDTO> list) {
        countryPresenterCallBack.countriesAreLoaded(list);
    }

    @Override
    public void onFailure(@NotNull ErrorDTO errorDTO) {//todo
    }

    @Override
    public void getCountry(Integer id) {
        countryConnection.getCountry(id);
    }

    @Override
    public void getCountries() {
        countryConnection.getCountries();
    }

    @Override
    public void onServerUnavailable() {

    }

    @Override
    public void onUnsuccessfulRequest(@Nullable String message) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void viewIsReady() {

    }

    @Override
    public void setConnectivityStatus(@Nullable Integer status) {

    }

    @Override
    public boolean isConnected(int status) {
        return false;
    }

    @Override
    public void attachView(@NotNull CountryPresenterCallBack countryPresenterCallBack) {
        this.countryPresenterCallBack = countryPresenterCallBack;
    }
}
