package bobrchess.of.by.belaruschess.util;

/**
 * Created by Igor on 04.04.2018.
 */

public class Constants {
    public static class URL {
        public static final String HOST = "http://192.168.0.104:8080";
       // public static final String HOST = "http://192.168.43.96:8080";
        //public static final String HOST = "https://api.github.com";
        public static final String GET_COUNTRIES = HOST + "country";
        public static final String GET_COUNTRY = HOST + "country/1";
        public static final String GET_USERS = HOST + "user";
        public static final String GET_USER = HOST + "user/1";
        public static final String USER_REGISTRATION = HOST + "user/1";
        public static final String USER_AUTHORIZATION = HOST + "user/1";
    }

    public static final String UNSUCCESSFUL_REQUEST = "Unsuccessful request!";
    public static final String ERROR_PARAMETER = "Error";
    public static final String EMPTY_STRING = "";
    public static final String INCORRECT_EMAIL = "Incorrect email!";
    public static final String INCORRECT_PASSWORD = "Incorrect password!";
    public static final String INCORRECT_NAME = "Incorrect name!";
    public static final String INCORRECT_SURNAME = "Incorrect surname!";
    public static final String INCORRECT_PATRONYMIC = "Incorrect patronymic!";
    public static final String INCORRECT_RATING = "Incorrect rating!";

    public static final String USER_PARAMETER = "user";
    public static final String USER_NAME_PARAMETER = "name";
    public static final String USER_SURNAME_PARAMETER = "surname";
    public static final String USER_RATING_PARAMETER = "rating";
    public static final String USER_EMAIL_PARAMETER = "email";
    public static final String USER_STATUS_PARAMETER = "status";
    public static final String USER_PATRONYMIC_PARAMETER = "patronymic";
    public static final String USER_BIRTHDAY_PARAMETER = "birthday";

}
