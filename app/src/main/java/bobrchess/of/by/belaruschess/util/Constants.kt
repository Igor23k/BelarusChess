package bobrchess.of.by.belaruschess.util

/**
 * Created by Igor on 04.04.2018.
 */

class Constants {

    companion object {



        val KEY_INVALID_EMAIL_OR_PASSWORD = "invalid_email_or_password"
        val KEY_UNSUCCESSFUL_REQUEST = "unsuccessful_request"
        val SERVER_UNAVAILABLE = "Server is unavailable"
        val KEY_SERVER_UNAVAILABLE = "server_is_unavailable"
        val INTERNAL_SERVER_ERROR = "Internal Server Error"
        val KEY_SERVER_UNAVAILABLE_JSON_FORMAT = "server_unavailable_json_format"
        val KEY_TOKEN_IS_EXPIRED = "token_is_expired"
        val UNAUTHORIZED = "Unauthorized"
        //public static final String PLEASE_WAIT = "Please, wait ...";
        val EMPTY_STRING = ""

        val USER_PARAMETER = "user"//todo не выбрать ничего из дропдаунов и ничего не выводит из ошибок
        val GAME_PARAMETER = "game"
        val TOURNAMENT_PARAMETER = "tournament"
        val USER_NAME_PARAMETER = "gameRecord"
        val USER_SURNAME_PARAMETER = "shortDescription"
        val USER_RATING_PARAMETER = "rating"
        val USER_EMAIL_PARAMETER = "email"
        val USER_STATUS_PARAMETER = "status"
        val USER_PATRONYMIC_PARAMETER = "patronymic"
        val USER_BIRTHDAY_PARAMETER = "birthday"

        val DATE_PICKER_DIALOG = "Datepickerdialog"
        val TIME_PICKER_DIALOG = "Timepickerdialog"

        val KEY_INCORRECT_USER_ID = "incorrect_user_id"
        val KEY_INCORRECT_USER_NAME = "incorrect_user_name"
        val KEY_INCORRECT_USER_SURNAME = "incorrect_user_surname"
        val KEY_INCORRECT_USER_PATRONYMIC = "incorrect_user_patronymic"
        val KEY_INCORRECT_USER_BIRTHDAY = "incorrect_user_birthday"
        val KEY_INCORRECT_USER_STATUS = "incorrect_user_status"
        val KEY_INCORRECT_USER_EMAIL = "incorrect_user_email"
        val KEY_INCORRECT_USER_PASSWORD = "incorrect_user_password"
        val KEY_INCORRECT_USER_RE_ENTER_PASSWORD = "incorrect_user_re_enter_password"
        val KEY_INCORRECT_USER_RATING_BIG = "incorrect_user_rating_big"
        val KEY_INCORRECT_USER_RATING_SMALL = "incorrect_user_rating_small"
        val KEY_INCORRECT_USER_PHONE_NUMBER = "incorrect_user_phone_number"
        val KEY_RANK_IS_NOT_SELECTED = "rank_is_not_selected"
        val KEY_GENDER_IS_NOT_SELECTED = "gender_is_not_selected"
        val KEY_COUNTRY_IS_NOT_SELECTED = "country_is_not_selected"
        val KEY_COACH_IS_NOT_SELECTED = "coach_is_not_selected"

        val KEY_INCORRECT_COUNTRY_ID = "incorrect_country_id"
        val KEY_INCORRECT_COUNTRY_NAME = "incorrect_country_name"
        val KEY_INCORRECT_COUNTRY_ABBREVIATION = "Eincorrect_country_abbreviation"

        val KEY_INCORRECT_GAME_ID = "incorrect_game_id"
        val KEY_INCORRECT_GAME_RECORD = "incorrect_game_record"//bug на сервере тоже 20000 сделать
        val KEY_INCORRECT_GAME_CHESS_PLAYER = "incorrect_game_chess_player"
        val KEY_INCORRECT_COUNT_POINTS_OF_PLAYER_SMALL = "incorrect_game_points_of_player_small"
        val KEY_INCORRECT_COUNT_POINTS_OF_PLAYER_BIG = "incorrect_game_points_of_player_big"

        val KEY_INCORRECT_MATCH_ID = "incorrect_match_id"
        val KEY_INCORRECT_MATCH_COUNT_POINTS_OF_TEAM = "incorrect_match_count_points_of_team"
        val KEY_INCORRECT_MATCH_DATE = "incorrect_match_date"
        val KEY_INCORRECT_MATCH_TEAM = "incorrect_match_team"
        val KEY_INCORRECT_MATCH_TOURNAMENT = "incorrect_match_tournament"

        val KEY_INCORRECT_PLACE_ID = "incorrect_place_id"
        val KEY_INCORRECT_PLACE_NAME = "incorrect_place_name"
        val KEY_INCORRECT_PLACE_CITY = "incorrect_place_city"
        val KEY_INCORRECT_PLACE_STREET = "incorrect_place_street"
        val KEY_INCORRECT_PLACE_BUILDING = "incorrect_place_building"
        val KEY_INCORRECT_PLACE_CAPACITY_SMALL = "incorrect_place_capacity_small"
        val KEY_INCORRECT_PLACE_CAPACITY_BIG = "incorrect_place_capacity_big"
        val KEY_INCORRECT_PLACE_COUNTRY = "incorrect_place_country"

        val KEY_INCORRECT_RANK_ID = "incorrect_rank_id"
        val KEY_INCORRECT_RANK_NAME = "incorrect_rank_name"
        val KEY_INCORRECT_RANK_ABBREVIATION = "incorrect_rank_abbreviation"

        val KEY_INCORRECT_TOURNAMENT_ID = "incorrect_tournament_id"
        val KEY_INCORRECT_TOURNAMENT_NAME = "incorrect_tournament_name"
        val KEY_INCORRECT_TOURNAMENT_PLACE = "incorrect_tournament_place"
        val KEY_INCORRECT_TOURNAMENT_SHORT_DESCRIPTION = "incorrect_tournament_short_description"
        val KEY_INCORRECT_TOURNAMENT_FULL_DESCRIPTION = "incorrect_tournament_full_description"
        val KEY_INCORRECT_TOURNAMENT_START_DATE = "incorrect_tournament_start_date"
        val KEY_INCORRECT_TOURNAMENT_FINISH_DATE = "incorrect_tournament_finish_date"
        val KEY_INCORRECT_TOURNAMENT_COUNT_PLAYERS_IN_TEAM = "incorrect_tournament_count_players_in_team"

        val KEY_INCORRECT_TEAM_ID = "incorrect_team_id"
        val KEY_INCORRECT_TEAM_NAME = "incorrect_team_name"

        val KEY_INCORRECT_TOURNAMENT_TEAM_ID = "incorrect_tournament_team_id"
        val KEY_INCORRECT_TOURNAMENT_TEAM_POSITION = "incorrect_tournament_team_position"
        val KEY_INCORRECT_TOURNAMENT_TEAM_COUNT_POINTS = "incorrect_tournament_team_count_points"


        //val HOST = "http://192.168.100.12:8080"
        val HOST = "http://192.168.43.96:8080"


        @JvmField
        val REFRESH_TOKEN_DEFAULT = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZWN1cml0eUBnbWFpbC5jb20iLCJzY29wZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfUFJFTUlVTV9NRU1CRVIiLCJST0xFX1JFRlJFU0hfVE9LRU4iXSwiaXNzIjoiaHR0cDovL3N2bGFkYS5jb20iLCJpYXQiOjE0NzIzOTAwNjUsImV4cCI6MTk3MjM5MDk2NX0.q9H20pGFLegFH2LjiYBNTm7u9i3PWGZh8rTx3A3nrXnFVg5_fOiSDxYQuodkt_S9gFNjJCI8ap-dvogTgwCf5Q"
        @JvmField
        val REFRESH_TOKEN = "refreshToken"
        @JvmField
        val TOKEN = "token"
        @JvmField
        val TOKEN_DEFAULT = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZWN1cml0eUBnbWFpbC5jb20iLCJzY29wZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfUFJFTUlVTV9NRU1CRVIiLCJST0xFX1JFRlJFU0hfVE9LRU4iXSwiaXNzIjoiaHR0cDovL3N2bGFkYS5jb20iLCJpYXQiOjE0NzIzOTAwNjUsImV4cCI6MTk3MjM5MDk2NX0.q9H20pGFLegFH2LjiYBNTm7u9i3PWGZh8rTx3A3nrXnFVg5_fOiSDxYQuodkt_S9gFNjJCI8ap-dvogTgwCf5Q"
        @JvmField
        val USER_PREFERENCES = "userPreferences"

        val REQUEST_CODE = "requestCode"
    }

}

