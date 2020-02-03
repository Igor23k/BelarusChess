package bobrchess.of.by.belaruschess.view.activity;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.TournamentDTO;

/**
 * Created by Igor on 22.04.2018.
 */

public interface SearchTournamentContractView extends MvpView, BaseContractView {
    void showTournaments(List<TournamentDTO> tournaments);
}
