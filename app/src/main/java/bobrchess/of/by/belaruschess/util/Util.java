package bobrchess.of.by.belaruschess.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;

import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.RankDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;

/**
 * Created by Igor on 03.05.2018.
 */

public class Util {

    public static Integer USER_INFO = 1;
    public static Integer TOURNAMENT_PARTICIPANTS_REQUEST = 2; // вынести все их сюда
    public static Integer TOURNAMENT_TABLE_REQUEST = 3;
    public static final int AUTHORIZATION_REQUEST = 4;
    public static final int ADD_TOURNAMENT_REQUEST = 5;
    public static final int REGISTRATION_REQUEST = 6;
    public static final int TOURNAMENT_REQUEST = 7;

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static String getEncodedPassword(String password) {
        return new String(Hex.encodeHex(DigestUtils.md5(password)));
    }

    public static TournamentDTO getTestTournament() {
        TournamentDTO tournamentDTO = new TournamentDTO();
        tournamentDTO.setPlace(getTestPlace());
        tournamentDTO.setReferee(getTestUser());
        tournamentDTO.setCountPlayersInTeam(1);
        tournamentDTO.setName("Белая ладья - 2018");
        tournamentDTO.setFullDescription("Положение о проведении открытого республиканского шахматного турнира памяти Ю. В.  Кулаги\n" +
                "\n" +
                "Цель турнира\n" +
                "\n" +
                "Ø Установление дружественных связей с шахматистами других регионов\n" +
                "Ø Повышение мастерства шахматистов\n");
        tournamentDTO.setShortDescription("Вам тут понравится!");
        tournamentDTO.setStartDate("456789");
        tournamentDTO.setFinishDate("4567890");
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
        return placeDTO;
    }

    public static CountryDTO getTestCountry() {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setName("Франция");
        countryDTO.setAbbreviation("FRA");
        return countryDTO;
    }

    public static CountryDTO getTestCountry2() {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setName("Франциq");
        countryDTO.setAbbreviation("FRS");
        return countryDTO;
    }

    public static RankDTO getTestRank() {
        RankDTO rankDTO = new RankDTO();
        rankDTO.setAbbreviation("GM");
        rankDTO.setName("Gross");
        return rankDTO;
    }

    public static UserDTO getTestUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("ww@dd.ek");
        userDTO.setCountry(getTestCountry2());
        userDTO.setRank(getTestRank());
        userDTO.setStatus("My best status");
        userDTO.setPhoneNumber("29373692");
        userDTO.setName("Ihar");
        userDTO.setSurname("Kazlou");
        userDTO.setPatronymic("Sergeevich");
        userDTO.setPassword("12345678901234567890123456789012");
        userDTO.setRating(2000);
        userDTO.setBirthday("eeeeeee");
        userDTO.setCoach(null);
        return userDTO;
    }

    public static UserDTO getTestUser2() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("ww@dd.ek1");
        userDTO.setCountry(getTestCountry());
        userDTO.setRank(getTestRank());
        userDTO.setStatus("My best status1");
        userDTO.setPhoneNumber("293736921");
        userDTO.setName("Ihar1");
        userDTO.setSurname("Kazlou1");
        userDTO.setPatronymic("Sergeevich1");
        userDTO.setPassword("12345678901234567890123456789012");
        userDTO.setRating(2000);
        userDTO.setBirthday("eeeeeee1");
        return userDTO;
    }

    private static ProgressDialog mProgressDialog;

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSimpleProgressDialog(Context context) {
        showSimpleProgressDialog(context, null, "Loading...", false);
    }

    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<String> getUsersNames(List<UserDTO> users) {
        List<String> usersNames = new ArrayList<>();
        UserDTO user;
        for (int i = 0; i < users.size(); i++) {
            user = users.get(i);
            usersNames.add(user.getName() + " " + user.getSurname() + " " + user.getPatronymic() + " (" + user.getRank().getAbbreviation() + ")");
        }
        return usersNames;
    }

    public static List<String> getRanksNames(List<RankDTO> ranks) {
        List<String> usersNames = new ArrayList<>();
        for (int i = 0; i < ranks.size(); i++) {
            usersNames.add(ranks.get(i).getName());
        }
        return usersNames;
    }

    public static List<String> getCountriesNames(List<CountryDTO> countries) {
        List<String> usersNames = new ArrayList<>();
        for (int i = 0; i < countries.size(); i++) {
            usersNames.add(countries.get(i).getName());
        }
        return usersNames;
    }

    public static List<String> getPlacesNames(List<PlaceDTO> places) {
        List<String> usersNames = new ArrayList<>();
        for (int i = 0; i < places.size(); i++) {
            usersNames.add(places.get(i).getName());
        }
        return usersNames;
    }

    public static List<String> getGenders() {
        List<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");//bug доставать это из резурсов (с интернационализацией).
        return genders;
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }
}
