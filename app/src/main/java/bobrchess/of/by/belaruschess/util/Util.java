package bobrchess.of.by.belaruschess.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.RankDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;

/**
 * Created by Igor on 03.05.2018.
 */

public class Util {
    public static String getEncodedPassword(String password) {
        return new String(Hex.encodeHex(DigestUtils.md5(password)));
    }

    public static TournamentDTO getTestTournament(){
        TournamentDTO tournamentDTO = new TournamentDTO();
        tournamentDTO.setPlace(getTestPlace());
        tournamentDTO.setReferee(getTestUser());
        tournamentDTO.setCountPlayersInTeam(1);
        tournamentDTO.setName("Белая ладья - 2018");
        tournamentDTO.setFullDescription("Полное описание");
        tournamentDTO.setShortDescription("Краткое описание");
        tournamentDTO.setId(8);
        return tournamentDTO;
    }

    public static PlaceDTO getTestPlace() {
        PlaceDTO placeDTO = new PlaceDTO();
        placeDTO.setName("Училище 28");
        placeDTO.setStreet("Первомайская");
        placeDTO.setCountry(getTestCountry());
        placeDTO.setBuilding("43");
        placeDTO.setCity("Минск");
        placeDTO.setCapacity(100);
        placeDTO.setId(5);
        return placeDTO;
    }

    public static CountryDTO getTestCountry(){
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setName("France");
        countryDTO.setAbbreviation("FRA");
        return countryDTO;
    }

    public static RankDTO getTestRank(){
        RankDTO rankDTO = new RankDTO();
        rankDTO.setAbbreviation("GM");
        rankDTO.setName("Gross");
        return rankDTO;
    }

    public static UserDTO getTestUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("ww@dd.ek");
        userDTO.setCountry(getTestCountry());
        userDTO.setRank(getTestRank());
        userDTO.setName("Ihar");
        userDTO.setSurname("Kazlou");
        userDTO.setPatronymic("Sergeevich");
        userDTO.setPassword("qwerty");
        userDTO.setRating(2000);
        //  userDTO.setBirthday(new Date(System.currentTimeMillis()));
        return userDTO;
    }
}
