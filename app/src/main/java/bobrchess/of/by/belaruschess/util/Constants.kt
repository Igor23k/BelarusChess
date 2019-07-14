package bobrchess.of.by.belaruschess.util

/**
 * Created by Igor on 04.04.2018.
 */

class Constants {

    companion object {


        val UNSUCCESSFUL_REQUEST = "Unsuccessful request!"
        val SERVER_UNAVAILABLE = "Server is unavailable"
        val TOKEN_IS_EXPIRED = "Token is expired"
        val ERROR_PARAMETER = "Error"
        //public static final String PLEASE_WAIT = "Please, wait ...";
        val EMPTY_STRING = ""

        val USER_PARAMETER = "user"
        val GAME_PARAMETER = "game"
        val TOURNAMENT_PARAMETER = "tournament"
        val USER_NAME_PARAMETER = "gameRecord"
        val USER_SURNAME_PARAMETER = "surname"
        val USER_RATING_PARAMETER = "rating"
        val USER_EMAIL_PARAMETER = "email"
        val USER_STATUS_PARAMETER = "status"
        val USER_PATRONYMIC_PARAMETER = "patronymic"
        val USER_BIRTHDAY_PARAMETER = "birthday"

        val DATE_PICKER_DIALOG = "Datepickerdialog"
        val TIME_PICKER_DIALOG = "Timepickerdialog"

        val INCORRECT_USER_ID = "Error! User id must not be less than 1!"
        val INCORRECT_USER_NAME = "Error! User token must contain from 3 to 30 symbols!"
        val INCORRECT_USER_SURNAME = "Error! User surname must contain from 3 to 30 symbols!"
        val INCORRECT_USER_PATRONYMIC = "Error! User patronymic must contain from 3 to 30 symbols!"
        val INCORRECT_USER_BIRTHDAY = "Error! Incorrect user birthday!"
        val INCORRECT_USER_STATUS = "Error! User status must contain from 1 to 50 symbols!"
        val INCORRECT_USER_EMAIL = "Error! Incorrect user email!"
        val INCORRECT_USER_PASSWORD = "Error! User password must contain more than 5 symbols!"
        val INCORRECT_USER_RATING_BIG = "Error! User rating must  be not more than 5000!"
        val INCORRECT_USER_RATING_SMALL = "Error! User rating must be not less than 0!"
        val INCORRECT_USER_PHONE_NUMBER = "Error! User phone number must contain from 5 to 20 symbols!"

        val INCORRECT_COUNTRY_ID = "Error! Country id must not be less than 1!"
        val INCORRECT_COUNTRY_NAME = "Error! Country token must contain from 3 to 50 symbols!"
        val INCORRECT_COUNTRY_ABBREVIATION = "Error! Country abbreviation must contain from 3 to 3 symbols!"

        val INCORRECT_GAME_ID = "Error! Game id must not be less than 1!"
        val INCORRECT_GAME_RECORD = "Error! Game record must contain from 2 to 20000 symbols!"//bug на сервере тоже 20000 сделать
        val INCORRECT_GAME_CHESS_PLAYER = "Error! Incorrect game chess player!"
        val INCORRECT_COUNT_POINTS_OF_PLAYER_SMALL = "Error! Count points of player must not be less than 0!"
        val INCORRECT_COUNT_POINTS_OF_PLAYER_BIG = "Error! Count points of player must not be more than 1!"

        val INCORRECT_MATCH_ID = "Error! Match id must not be less than 1!"
        val INCORRECT_COUNT_POINTS_OF_TEAM = "Error! Count points of a team must not be less than 0!"
        val INCORRECT_MATCH_DATE = "Error! Incorrect match date!"
        val INCORRECT_MATCH_TEAM = "Error! Team must be selected!"
        val INCORRECT_MATCH_TOURNAMENT = "Error! Match tournament must not be null!"

        val INCORRECT_PLACE_ID = "Error! Place id must not be less than 1!"
        val INCORRECT_PLACE_NAME = "Error! Place gameRecord must contain from 1 to 100 symbols!"
        val INCORRECT_PLACE_CITY = "Error! Place city must contain from 3 to 50 symbols!"
        val INCORRECT_PLACE_STREET = "Error! Place street must contain from 3 to 50 symbols!"
        val INCORRECT_PLACE_BUILDING = "Error! Place building must contain from 1 to 10 symbols!"
        val INCORRECT_PLACE_CAPACITY_SMALL = "Error! Place capacity must not be less than 1!"
        val INCORRECT_PLACE_CAPACITY_BIG = "Error! Place capacity must not be more than 10000!"
        val INCORRECT_PLACE_COUNTRY = "Error! Place country must not be null!"

        val INCORRECT_RANK_ID = "Error! Rank id must not be less than 1!"
        val INCORRECT_RANK_NAME = "Error! Rank token must contain from 3 to 50 symbols!"
        val INCORRECT_RANK_ABBREVIATION = "Error! Rank abbreviation must contain from 3 to 3 symbols!"

        val INCORRECT_TOURNAMENT_ID = "Error! Tournament id must not be less than 1!"
        val INCORRECT_TOURNAMENT_NAME = "Error! Tournament gameRecord must contain from 8 to 50 symbols!"
        val INCORRECT_TOURNAMENT_PLACE = "Error! Tournament place is not selected!"
        val INCORRECT_TOURNAMENT_SHORT_DESCRIPTION = "Error! Tournament short description must contain from 20 to 100 symbols!"
        val INCORRECT_TOURNAMENT_FULL_DESCRIPTION = "Error! Tournament full description must contain from 100 to 20000 symbols!"
        val INCORRECT_TOURNAMENT_START_DATE = "Error! Incorrect tournament start date!"
        val INCORRECT_TOURNAMENT_FINISH_DATE = "Error! Incorrect tournament finish date!"
        val INCORRECT_TOURNAMENT_COUNT_PLAYERS_IN_TEAM = "Error! Count players in team must not be more than 20!"

        val INCORRECT_TEAM_ID = "Error! Team id must not be less than 1!"
        val INCORRECT_TEAM_NAME = "Error! Team gameRecord must contain from 3 to 50 symbols!"

        val INCORRECT_TOURNAMENT_TEAM_ID = "Error! Tournament team id must not be less than 1!"
        val INCORRECT_TOURNAMENT_TEAM_POSITION = "Error! Tournament team position must not be less than 1!"
        val INCORRECT_TOURNAMENT_TEAM_COUNT_POINTS = "Error! Tournament team country points must not be less than 0!"


        val HOST = "http://192.168.100.2:8080"
        //val HOST = "http://192.168.43.96:8080"


        val REFRESH_TOKEN_DEFAULT = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZWN1cml0eUBnbWFpbC5jb20iLCJzY29wZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfUFJFTUlVTV9NRU1CRVIiLCJST0xFX1JFRlJFU0hfVE9LRU4iXSwiaXNzIjoiaHR0cDovL3N2bGFkYS5jb20iLCJpYXQiOjE0NzIzOTAwNjUsImV4cCI6MTk3MjM5MDk2NX0.q9H20pGFLegFH2LjiYBNTm7u9i3PWGZh8rTx3A3nrXnFVg5_fOiSDxYQuodkt_S9gFNjJCI8ap-dvogTgwCf5Q"
        val REFRESH_TOKEN = "refreshToken"
        val TOKEN = "token"
        val TOKEN_DEFAULT = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZWN1cml0eUBnbWFpbC5jb20iLCJzY29wZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfUFJFTUlVTV9NRU1CRVIiLCJST0xFX1JFRlJFU0hfVE9LRU4iXSwiaXNzIjoiaHR0cDovL3N2bGFkYS5jb20iLCJpYXQiOjE0NzIzOTAwNjUsImV4cCI6MTk3MjM5MDk2NX0.q9H20pGFLegFH2LjiYBNTm7u9i3PWGZh8rTx3A3nrXnFVg5_fOiSDxYQuodkt_S9gFNjJCI8ap-dvogTgwCf5Q"

    }
}
