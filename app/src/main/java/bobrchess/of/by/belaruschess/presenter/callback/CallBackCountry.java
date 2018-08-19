package bobrchess.of.by.belaruschess.presenter.callback;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.CountryDTO;

/**
 * Created by Igor on 11.04.2018.
 */

public interface CallBackCountry extends CallBack {
    void onResponse(CountryDTO countryDTO);

    void onResponse(List<CountryDTO> countryDTO);
}
