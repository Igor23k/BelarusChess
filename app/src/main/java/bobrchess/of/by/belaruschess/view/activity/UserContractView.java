package bobrchess.of.by.belaruschess.view.activity;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.UserDTO;

public interface UserContractView extends BaseContractView {
    void showUsers(List<? extends UserDTO> users);
    void showUser(UserDTO user);
}
