package bobrchess.of.by.belaruschess.view.activity;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.RankDTO;

public interface CountryPresenterCallBack {
    void countryIsLoaded(CountryDTO countryDTO);
    void countriesAreLoaded(List<CountryDTO> list);
}
