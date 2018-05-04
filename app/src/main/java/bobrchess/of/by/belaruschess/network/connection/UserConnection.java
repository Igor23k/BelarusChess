package bobrchess.of.by.belaruschess.network.connection;

import java.util.List;

import bobrchess.of.by.belaruschess.App;
import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.RankDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Igor on 11.04.2018.
 */

public class UserConnection {

    private CallBackUser callBack;

    public void getUser(Integer id) {
        App.getAPI().getUser(id).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    callBack.onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                callBack.onAuthorizationFailure(t);
            }
        });
    }

    public void getUsers() {
        App.getAPI().getUsers().enqueue(new Callback<List<UserDTO>>() {
            @Override
            public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                if (response.isSuccessful()) {
                    callBack.onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<UserDTO>> call, Throwable t) {
                callBack.onAuthorizationFailure(t);
            }
        });
    }

    public void authorizate(UserDTO userDTO) {
        App.getAPI().authorizate(userDTO).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    if (response.raw().code() == 200 && response.body() != null) {
                        callBack.onResponse(response.body());
                    }else {
                        callBack.onAuthorizationFailure(new Throwable(response.raw().header("Error message")));
                    }
                } else {
                    callBack.onAuthorizationFailure(new Throwable());
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                callBack.onConnectionError(t);
            }
        });
    }

    public void registrate(UserDTO userDTO) {
        App.getAPI().registrate(userDTO).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    if (response.raw().code() == 200 && response.body() != null) {
                        callBack.onResponse(response.body());
                    }else {
                        callBack.onAuthorizationFailure(new Throwable(response.raw().header("Error message")));
                    }
                } else {
                    callBack.onAuthorizationFailure(new Throwable());
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                callBack.onConnectionError(t);
            }
        });
    }

    public void attachView(CallBackUser callBack) {
        this.callBack = callBack;
    }

    UserDTO getTestUser() {
        UserDTO userDTO = new UserDTO();
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setName("BELAR");
        countryDTO.setAbbreviation("blr");
        RankDTO rankDTO = new RankDTO();
        rankDTO.setAbbreviation("kek");
        rankDTO.setName("KEKER");
        userDTO.setEmail("ww@dd.ek");
        userDTO.setCountry(countryDTO);
        userDTO.setRank(rankDTO);
        userDTO.setName("Ihar");
        userDTO.setSurname("Kazlou");
        userDTO.setPatronymic("Sergeevich");
        userDTO.setPassword("qwerty");
        userDTO.setRating(2000);
        //  userDTO.setBirthday(new Date(System.currentTimeMillis()));
        return userDTO;
    }
}
