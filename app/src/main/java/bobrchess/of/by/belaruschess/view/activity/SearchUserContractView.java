package bobrchess.of.by.belaruschess.view.activity;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.UserDTO;

/**
 * Created by Igor on 22.04.2018.
 */

public interface SearchUserContractView extends BaseContractView {
    void showUsers(List<? extends UserDTO> users);
}
