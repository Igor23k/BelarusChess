package bobrchess.of.by.belaruschess.view.activity;

import com.arellomobile.mvp.MvpView;

import bobrchess.of.by.belaruschess.dto.externalFide.TopPlayersDTO;
import bobrchess.of.by.belaruschess.dto.externalFide.WorldTournamentsDataDTO;


public interface FideApiContractView extends MvpView, BaseContractView {
    void showWorldTournaments(WorldTournamentsDataDTO tournamentsData);
    void showTopPlayers(TopPlayersDTO topPlayersDTO);
}
