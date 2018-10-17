package bobrchess.of.by.belaruschess.util;

/**
 * Created by Igor on 04.04.2018.
 */

public class Constants {
    public static class URL {
        //public static final String HOST = "http://192.168.100.2:8080";
        public static final String HOST = "http://192.168.43.96:8080";
    }

    public static final String UNSUCCESSFUL_REQUEST = "Unsuccessful request!";
    public static final String SERVER_UNAVAILABLE = "Server is unavailable";
    public static final String ERROR_PARAMETER = "Error";
    //public static final String PLEASE_WAIT = "Please, wait ...";
    public static final String EMPTY_STRING = "";

    public static final String USER_PARAMETER = "user";
    public static final String GAME_PARAMETER = "game";
    public static final String TOURNAMENT_PARAMETER = "tournament";
    public static final String USER_NAME_PARAMETER = "gameRecord";
    public static final String USER_SURNAME_PARAMETER = "surname";
    public static final String USER_RATING_PARAMETER = "rating";
    public static final String USER_EMAIL_PARAMETER = "email";
    public static final String USER_STATUS_PARAMETER = "status";
    public static final String USER_PATRONYMIC_PARAMETER = "patronymic";
    public static final String USER_BIRTHDAY_PARAMETER = "birthday";

    public static final String DATE_PICKER_DIALOG = "Datepickerdialog";
    public static final String TIME_PICKER_DIALOG = "Timepickerdialog";

    public static final String INCORRECT_USER_ID = "Error! User id must not be less than 1!";
    public static final String INCORRECT_USER_NAME = "Error! User gameRecord must contain from 3 to 30 symbols!";
    public static final String INCORRECT_USER_SURNAME = "Error! User surname must contain from 3 to 30 symbols!";
    public static final String INCORRECT_USER_PATRONYMIC = "Error! User patronymic must contain from 3 to 30 symbols!";
    public static final String INCORRECT_USER_BIRTHDAY = "Error! Incorrect user birthday!";
    public static final String INCORRECT_USER_STATUS = "Error! User status must contain from 1 to 50 symbols!";
    public static final String INCORRECT_USER_EMAIL = "Error! Incorrect user email!";
    public static final String INCORRECT_USER_PASSWORD = "Error! User password must contain more than 5 symbols!";
    public static final String INCORRECT_USER_RATING_BIG = "Error! User rating must  be not more than 5000!";
    public static final String INCORRECT_USER_RATING_SMALL = "Error! User rating must be not less than 0!";
    public static final String INCORRECT_USER_PHONE_NUMBER = "Error! User phone number must contain from 5 to 20 symbols!";

    public static final String INCORRECT_COUNTRY_ID = "Error! Country id must not be less than 1!";
    public static final String INCORRECT_COUNTRY_NAME = "Error! Country name must contain from 3 to 50 symbols!";
    public static final String INCORRECT_COUNTRY_ABBREVIATION = "Error! Country abbreviation must contain from 3 to 3 symbols!";

    public static final String INCORRECT_GAME_ID = "Error! Game id must not be less than 1!";
    public static final String INCORRECT_GAME_RECORD = "Error! Game record must contain from 2 to 20000 symbols!";//bug на сервере тоже 20000 сделать
    public static final String INCORRECT_GAME_CHESS_PLAYER = "Error! Incorrect game chess player!";
    public static final String INCORRECT_COUNT_POINTS_OF_PLAYER_SMALL = "Error! Count points of player must not be less than 0!";
    public static final String INCORRECT_COUNT_POINTS_OF_PLAYER_BIG = "Error! Count points of player must not be more than 1!";

    public static final String INCORRECT_MATCH_ID = "Error! Match id must not be less than 1!";
    public static final String INCORRECT_COUNT_POINTS_OF_TEAM = "Error! Count points of a team must not be less than 0!";
    public static final String INCORRECT_MATCH_DATE = "Error! Incorrect match date!";
    public static final String INCORRECT_MATCH_TEAM = "Error! Team must be selected!";
    public static final String INCORRECT_MATCH_TOURNAMENT = "Error! Match tournament must not be null!";

    public static final String INCORRECT_PLACE_ID = "Error! Place id must not be less than 1!";
    public static final String INCORRECT_PLACE_NAME = "Error! Place gameRecord must contain from 1 to 100 symbols!";
    public static final String INCORRECT_PLACE_CITY = "Error! Place city must contain from 3 to 50 symbols!";
    public static final String INCORRECT_PLACE_STREET = "Error! Place street must contain from 3 to 50 symbols!";
    public static final String INCORRECT_PLACE_BUILDING = "Error! Place building must contain from 1 to 10 symbols!";
    public static final String INCORRECT_PLACE_CAPACITY_SMALL = "Error! Place capacity must not be less than 1!";
    public static final String INCORRECT_PLACE_CAPACITY_BIG = "Error! Place capacity must not be more than 10000!";
    public static final String INCORRECT_PLACE_COUNTRY = "Error! Place country must not be null!";

    public static final String INCORRECT_RANK_ID = "Error! Rank id must not be less than 1!";
    public static final String INCORRECT_RANK_NAME = "Error! Rank gameRecord must contain from 3 to 50 symbols!";
    public static final String INCORRECT_RANK_ABBREVIATION = "Error! Rank abbreviation must contain from 3 to 3 symbols!";

    public static final String INCORRECT_TOURNAMENT_ID = "Error! Tournament id must not be less than 1!";
    public static final String INCORRECT_TOURNAMENT_NAME = "Error! Tournament gameRecord must contain from 8 to 50 symbols!";
    public static final String INCORRECT_TOURNAMENT_PLACE = "Error! Tournament place is not selected!";
    public static final String INCORRECT_TOURNAMENT_SHORT_DESCRIPTION = "Error! Tournament short description must contain from 20 to 100 symbols!";
    public static final String INCORRECT_TOURNAMENT_FULL_DESCRIPTION = "Error! Tournament full description must contain from 100 to 20000 symbols!";
    public static final String INCORRECT_TOURNAMENT_START_DATE = "Error! Incorrect tournament start date!";
    public static final String INCORRECT_TOURNAMENT_FINISH_DATE = "Error! Incorrect tournament finish date!";
    public static final String INCORRECT_TOURNAMENT_COUNT_PLAYERS_IN_TEAM = "Error! Count players in team must not be more than 20!";

    public static final String INCORRECT_TEAM_ID = "Error! Team id must not be less than 1!";
    public static final String INCORRECT_TEAM_NAME = "Error! Team gameRecord must contain from 3 to 50 symbols!";

    public static final String INCORRECT_TOURNAMENT_TEAM_ID = "Error! Tournament team id must not be less than 1!";
    public static final String INCORRECT_TOURNAMENT_TEAM_POSITION = "Error! Tournament team position must not be less than 1!";
    public static final String INCORRECT_TOURNAMENT_TEAM_COUNT_POINTS = "Error! Tournament team country points must not be less than 0!";
}
