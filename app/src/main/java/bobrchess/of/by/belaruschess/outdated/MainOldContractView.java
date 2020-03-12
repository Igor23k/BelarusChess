package bobrchess.of.by.belaruschess.outdated;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.view.activity.BaseContractView;

/**
 * Created by Igor on 22.04.2018.
 */

public interface MainOldContractView extends BaseContractView {
    void displayUserTournaments(List<TournamentDTO> tournaments);
}
