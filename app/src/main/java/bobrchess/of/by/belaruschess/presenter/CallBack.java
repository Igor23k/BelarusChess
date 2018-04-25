package bobrchess.of.by.belaruschess.presenter;

import bobrchess.of.by.belaruschess.dto.CountryDTO;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Igor on 11.04.2018.
 */

public interface CallBack {
    void onResponse(CountryDTO countryDTO);
    void onFailure(Throwable t);
}
