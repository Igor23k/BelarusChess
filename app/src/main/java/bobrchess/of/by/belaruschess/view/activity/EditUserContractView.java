package bobrchess.of.by.belaruschess.view.activity;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.RankDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;

public interface EditUserContractView extends MvpView, BaseContractView, ButtonContractView{
    void showIncorrectUserNameText();
    void updateUser();
    void setGenderSpinnerAdapter(List<String> genders);
    void setCountrySpinnerAdapter(List<? extends CountryDTO> countries);
    void setCoachSpinnerAdapter(List<? extends UserDTO> coaches);
    void setRankSpinnerAdapter(List<? extends RankDTO> ranks);
    void startActivity(UserDTO userDTO);
    void userUpdated(UserDTO userDTO);
}
