package bobrchess.of.by.belaruschess.view.activity;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;

/**
 * Created by Igor on 22.04.2018.
 */

public interface AddPlaceContractView extends MvpView, BaseContractView, ButtonContractView{
    void showIncorrectPlaceNameText();
    void addPlace();
    void setCountrySpinnerAdapter(List<CountryDTO> countries);
    void startActivity(PlaceDTO placeDTO);
    void placeAdded(PlaceDTO placeDTO);
    void placeRemoved(int removedPlaceId);
    void removePlaceFromLocalStorage(long id);
}
