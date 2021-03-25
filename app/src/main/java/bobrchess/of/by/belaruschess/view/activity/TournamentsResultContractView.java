package bobrchess.of.by.belaruschess.view.activity;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.TournamentResultDTO;

public interface TournamentsResultContractView {
    void userTournamentsResultAreLoaded(List<TournamentResultDTO> tournamentsResults);
}
