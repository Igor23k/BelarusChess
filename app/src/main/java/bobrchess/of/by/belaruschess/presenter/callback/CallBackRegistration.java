package bobrchess.of.by.belaruschess.presenter.callback;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.RankDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;

/**
 * Created by Igor on 11.04.2018.
 */

public interface CallBackRegistration extends CallBack {
    void onResponse(UserDTO userDTO);
    void onCoachResponse(List<UserDTO> coaches);
    void onRankResponse(List<RankDTO> ranks);
    void onCountryResponse(List<CountryDTO> countries);
}
