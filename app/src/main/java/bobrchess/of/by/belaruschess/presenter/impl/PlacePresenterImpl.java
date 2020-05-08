package bobrchess.of.by.belaruschess.presenter.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.RankDTO;
import bobrchess.of.by.belaruschess.network.connection.PlaceConnection;
import bobrchess.of.by.belaruschess.presenter.PlacePresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackPlace;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackRank;
import bobrchess.of.by.belaruschess.view.activity.PlacePresenterCallBack;
import bobrchess.of.by.belaruschess.view.activity.RankPresenterCallBack;

public class PlacePresenterImpl implements CallBackPlace, PlacePresenter {

    private PlaceConnection placeConnection;
    private PlacePresenterCallBack placePresenterCallBack;

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
    public void onFailure(@NotNull ErrorDTO errorDTO) {//todo
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
    public void attachView(@NotNull PlacePresenterCallBack placePresenterCallBack) {
        this.placePresenterCallBack = placePresenterCallBack;
    }


}
