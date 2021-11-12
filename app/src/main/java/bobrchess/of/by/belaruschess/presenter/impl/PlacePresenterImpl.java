package bobrchess.of.by.belaruschess.presenter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.network.connection.internal.PlaceConnection;
import bobrchess.of.by.belaruschess.presenter.PlacePresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackPlace;
import bobrchess.of.by.belaruschess.view.activity.PlacePresenterCallBack;

public class PlacePresenterImpl implements CallBackPlace, PlacePresenter {

    private PlaceConnection placeConnection;
    private PlacePresenterCallBack placePresenterCallBack;
    private Boolean viewIsReady = false;

    public PlacePresenterImpl() {
        placeConnection = new PlaceConnection();
        placeConnection.attachPresenter(this);
    }

    @Override
    public void onResponse(@NotNull PlaceDTO placeDTO) {
        placePresenterCallBack.placeIsLoaded(placeDTO);
    }

    @Override
    public void onResponse(@NotNull List<? extends PlaceDTO> list) {
        placePresenterCallBack.placesAreLoaded(list);
    }

    @Override
    public void onFailure(@NotNull ErrorDTO errorDTO) {
        placePresenterCallBack.placesAreLoaded(new ArrayList<>());
    }

    @Override
    public void getPlace(Integer id) {
        placeConnection.getPlace(id);
    }

    @Override
    public void getPlaces() {
        placeConnection.getPlaces();
    }

    @Override
    public void onServerUnavailable() {
        placePresenterCallBack.placesAreLoaded(new ArrayList<>());
    }

    @Override
    public void onUnsuccessfulRequest(@Nullable String message) {
        placePresenterCallBack.placesAreLoaded(new ArrayList<>());
    }

    @Override
    public void detachView() {

    }

    @Override
    public void viewIsReady() {
        viewIsReady = true;
    }

    @Override
    public void setConnectivityStatus(@Nullable Integer status) {

    }

    @Override
    public boolean isConnected(int status) {
        return false;
    }

    @Override
    public void attachView(@NotNull PlacePresenterCallBack placePresenterCallBack) {
        this.placePresenterCallBack = placePresenterCallBack;
    }


}
