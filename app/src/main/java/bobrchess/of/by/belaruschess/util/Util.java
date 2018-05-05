package bobrchess.of.by.belaruschess.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.RankDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;

/**
 * Created by Igor on 03.05.2018.
 */

public class Util {
    public static String getEncodedPassword(String password) {
        return new String(Hex.encodeHex(DigestUtils.md5(password)));
    }

    UserDTO getTestUser() {
        UserDTO userDTO = new UserDTO();
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setName("BELAR");
        countryDTO.setAbbreviation("BLR");
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
