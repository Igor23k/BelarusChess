package bobrchess.of.by.belaruschess.view.activity;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.TournamentResultDTO;
import bobrchess.of.by.belaruschess.presenter.BasePresenter;

public interface TournamentsResultContractView extends BasePresenter {
    void userTournamentsResultAreLoaded(List<TournamentResultDTO> tournamentsResults);
}
