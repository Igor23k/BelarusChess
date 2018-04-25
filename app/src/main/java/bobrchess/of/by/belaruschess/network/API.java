package bobrchess.of.by.belaruschess.network;

import java.util.List;

import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Igor on 10.04.2018.
 */

public interface API {

    @GET("/users")
    Call<List<UserDTO>> getUsers();

    @GET("/user/1")
    Call<UserDTO> getUser();

    @GET("/country/1")
    Call<CountryDTO> getCountry();
}
