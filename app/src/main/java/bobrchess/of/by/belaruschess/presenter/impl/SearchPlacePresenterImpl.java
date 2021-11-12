package bobrchess.of.by.belaruschess.presenter.impl;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.network.connection.internal.SearchPlaceConnection;
import bobrchess.of.by.belaruschess.presenter.SearchPlacePresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackSearchPlace;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.SearchPlaceContractView;

/**
 * Created by Igor on 04.05.2018.
 */

@InjectViewState
public class SearchPlacePresenterImpl extends MvpPresenter<SearchPlaceContractView> implements CallBackSearchPlace, SearchPlacePresenter {

    private SearchPlaceContractView view;
    private SearchPlaceConnection placeConnection;
    private Boolean viewIsReady = false;
    private Integer connectivityStatus = 0;

    public SearchPlacePresenterImpl() {
        placeConnection = new SearchPlaceConnection();
        placeConnection.attachPresenter(this);
    }

    @Override
    public void loadPlaces() {
        view.showProgress();
        placeConnection.getPlaces();
    }

    @Override
    public void searchPlaces(@NotNull String text) {
        if (viewIsReady) {
            placeConnection.searchPlaces(text);
        }
    }

    @Override
    public void attachView(@NotNull SearchPlaceContractView searchPlaceContractView) {
        view = searchPlaceContractView;
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
    public void onResponse(@NotNull List<? extends PlaceDTO> placesDTO) {
        view.showPlaces(placesDTO);
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


    @Override
    public boolean isConnected(int status) {
        return Util.Companion.isConnected(status);
    }
}
