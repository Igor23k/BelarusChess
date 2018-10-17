package bobrchess.of.by.belaruschess.presenter;

import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.view.activity.AddTournamentContractView;

/**
 * Created by Igor on 12.04.2018.
 */

public interface AddTournamentPresenter extends BasePresenter {
    void addTournament(TournamentDTO tournamentDTO);
    void loadPlaces();
    void loadReferees();
    void attachView(AddTournamentContractView activity);
    void setSelectedPlaceIndex(Integer selectedPlaceIndex);
    void setSelectedRefereeIndex(Integer selectedRefereeIndex);
}