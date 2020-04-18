package bobrchess.of.by.belaruschess.view.activity;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.TournamentResultDTO;

/**
 * Created by Igor on 22.04.2018.
 */

public interface TournamentsResultContractView {
    void userTournamentsResultAreLoaded(List<TournamentResultDTO> tournamentsResults);
}
