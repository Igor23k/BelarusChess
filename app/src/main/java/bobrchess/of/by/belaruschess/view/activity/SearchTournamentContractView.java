package bobrchess.of.by.belaruschess.view.activity;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.TournamentDTO;


public interface SearchTournamentContractView extends MvpView, BaseContractView {
    void showTournaments(List<? extends TournamentDTO> tournaments);
}
