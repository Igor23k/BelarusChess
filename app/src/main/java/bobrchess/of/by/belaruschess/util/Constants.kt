package bobrchess.of.by.belaruschess.util

class Constants {

    companion object {

        const val NOT_SELECTED_INDEX = 0
        const val ABSENCE_INDEX = 1

        val KEY_INVALID_EMAIL_OR_PASSWORD = "invalid_email_or_password"
        val KEY_UNSUCCESSFUL_REQUEST = "unsuccessful_request"
        val SERVER_UNAVAILABLE = "Server is unavailable"
        val KEY_SERVER_UNAVAILABLE = "server_is_unavailable"
        val INTERNAL_SERVER_ERROR = "Internal Server Error"
        val KEY_INTERNAL_SERVER_ERROR = "internal_server_error"
        val KEY_SERVER_UNAVAILABLE_JSON_FORMAT = "server_unavailable_json_format"
        val KEY_GENDER_MALE = "male"
        val KEY_GENDER_FEMALE = "female"
        val TOKEN_IS_EXPIRED_MESSAGE = "Token is expired"
        val UNAUTHORIZED = "Unauthorized"
        //public static final String PLEASE_WAIT = "Please, wait ...";
        val EMPTY_STRING = ""

        val USER = "user"
        val TOP_PLAYER = "top_player"
        val WORLD_TOURNAMENT = "world_tournament"
        val TOURNAMENT = "tournament"
        val PLACE = "place"

        val USER_PARAMETER = "user"//todo не выбрать ничего из дропдаунов и ничего не выводит из ошибок В РЕГИСТРАЦИИ ПРОВЕРИТЬ

        val PLACES = "places"
        val RANKS = "ranks"
        val COUNTRIES = "countries"
        val REFEREES = "referees"
        val REFEREES_TOURNAMENT_TEXT = "referees_tournament_text"
        val USERS = "users"
        val COACH = "coach"
        val TOURNAMENTS_RESULT = "tournamentsResult"

        val KEY_RATING_OPEN = "key_rating_open"
        val KEY_WOMEN = "key_rating_women"
        val KEY_JUNIORS = "key_rating_juniors"
        val KEY_GIRLS = "key_rating_girls"

        val DATE_PICKER_DIALOG = "Datepickerdialog"

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
        val KEY_REFEREE_IS_NOT_SELECTED = "referee_is_not_selected"
        val KEY_PLACE_IS_NOT_SELECTED = "place_is_not_selected"
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
        val KEY_INCORRECT_PLACE_IMAGE = "incorrect_place_image"

        val KEY_INCORRECT_RANK_ID = "incorrect_rank_id"
        val KEY_INCORRECT_RANK_NAME = "incorrect_rank_name"
        val KEY_INCORRECT_RANK_ABBREVIATION = "incorrect_rank_abbreviation"

        val KEY_INCORRECT_TOURNAMENT_ID = "incorrect_tournament_id"
        val KEY_INCORRECT_TOURNAMENT_NAME = "incorrect_tournament_name"
        val KEY_INCORRECT_TOURNAMENT_PLACE = "incorrect_tournament_place"
        val KEY_INCORRECT_TOURNAMENT_TOURS_COUNT = "incorrect_tournament_tours_count"
        val KEY_INCORRECT_TOURNAMENT_SHORT_DESCRIPTION = "incorrect_tournament_short_description"
        val KEY_INCORRECT_TOURNAMENT_FULL_DESCRIPTION = "incorrect_tournament_full_description"
        val KEY_INCORRECT_TOURNAMENT_IMAGE = "incorrect_tournament_image"
        val KEY_INCORRECT_TOURNAMENT_START_DATE = "incorrect_tournament_start_date"
        val KEY_INCORRECT_TOURNAMENT_FINISH_DATE = "incorrect_tournament_finish_date"
        val KEY_INCORRECT_TOURNAMENT_COUNT_PLAYERS_IN_TEAM = "incorrect_tournament_count_players_in_team"

        val KEY_INCORRECT_TEAM_ID = "incorrect_team_id"
        val KEY_INCORRECT_TEAM_NAME = "incorrect_team_name"

        val KEY_INCORRECT_TOURNAMENT_TEAM_ID = "incorrect_tournament_team_id"
        val KEY_INCORRECT_TOURNAMENT_TEAM_POSITION = "incorrect_tournament_team_position"
        val KEY_INCORRECT_TOURNAMENT_TEAM_COUNT_POINTS = "incorrect_tournament_team_count_points"

        val KEY_NUMBER_OF_PLAYERS = "number_of_players_text"
        val KEY_TIME_CONTROL = "time_control_text"
        val KEY_TIME_CONTROL_DESCRIPTION = "time_control_description_text"
        val KEY_TIME_CONTROL_TYPE = "time_control_type_text"
        val KEY_NUMBER_OF_ROUNDS = "number_of_rounds_text"
        val KEY_ORGANIZER = "organizer_text"
        val KEY_ARBITER = "arbiter_text"
        val KEY_EMAIL = "email_text"
        val KEY_WEBSITE = "website_text"
        val KEY_PHONE = "phone_text"

        val KEY_TIME_CONTROL_STANDARD_TYPE = "time_control_standard_type"
        val KEY_TIME_CONTROL_RAPID_TYPE = "time_control_rapid_type"
        val KEY_TIME_CONTROL_BLITZ_TYPE = "time_control_blitz_type"




        //val PERSONAL_SERVER_HOST = "https://popular-ape-32.loca.lt1"
        val PERSONAL_SERVER_HOST = "http://192.168.0.103:8080"
        //val PERSONAL_SERVER_HOST = "http://192.168.43.96:8080"
        val EXTERNAL_FIDE_API_SERVER_HOST = "https://app.fide.com"
        val EXTERNAL_FIDE_API_RELATIVE_PATH = ""


        @JvmField//todo
        val REFRESH_TOKEN_DEFAULT = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZWN1cml0eUBnbWFpbC5jb20iLCJzY29wZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfUFJFTUlVTV9NRU1CRVIiLCJST0xFX1JFRlJFU0hfVE9LRU4iXSwiaXNzIjoiaHR0cDovL3N2bGFkYS5jb20iLCJpYXQiOjE0NzIzOTAwNjUsImV4cCI6MTk3MjM5MDk2NX0.q9H20pGFLegFH2LjiYBNTm7u9i3PWGZh8rTx3A3nrXnFVg5_fOiSDxYQuodkt_S9gFNjJCI8ap-dvogTgwCf5Q"
        @JvmField
        val REFRESH_TOKEN = "refreshToken"
        @JvmField
        val TOKEN = "token"
        @JvmField//todo
        val TOKEN_DEFAULT = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZWN1cml0eUBnbWFpbC5jb20iLCJzY29wZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfUFJFTUlVTV9NRU1CRVIiLCJST0xFX1JFRlJFU0hfVE9LRU4iXSwiaXNzIjoiaHR0cDovL3N2bGFkYS5jb20iLCJpYXQiOjE0NzIzOTAwNjUsImV4cCI6MTk3MjM5MDk2NX0.q9H20pGFLegFH2LjiYBNTm7u9i3PWGZh8rTx3A3nrXnFVg5_fOiSDxYQuodkt_S9gFNjJCI8ap-dvogTgwCf5Q"
        @JvmField
        val USER_PREFERENCES = "userPreferences"

        val REQUEST_CODE = "requestCode"
        val USER_BIRTHDAY_FORMAT = "dd/MM/yyyy"
    }

}

