package bobrchess.of.by.belaruschess.view.activity;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.PlaceDTO;

/**
 * Created by Igor on 22.04.2018.
 */

public interface SearchPlaceContractView extends MvpView, BaseContractView {
    void showPlaces(List<? extends PlaceDTO> places);
}
