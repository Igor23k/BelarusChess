package bobrchess.of.by.belaruschess.view.activity;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.RankDTO;

public interface PlacePresenterCallBack {
    void placeIsLoaded(PlaceDTO placeDTO);
    void placesAreLoaded(List<? extends PlaceDTO> places);
}
