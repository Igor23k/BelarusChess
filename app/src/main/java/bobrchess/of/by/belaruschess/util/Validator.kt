package bobrchess.of.by.belaruschess.util

import bobrchess.of.by.belaruschess.dto.*
import bobrchess.of.by.belaruschess.dto.extended.ExtendedPlaceDTO
import bobrchess.of.by.belaruschess.dto.extended.ExtendedTournamentDTO
import bobrchess.of.by.belaruschess.dto.extended.ExtendedUserDTO
import bobrchess.of.by.belaruschess.exception.IncorrectDataException
import bobrchess.of.by.belaruschess.util.Util.Companion.getInternalizedMessage
import org.springframework.util.StringUtils

/**
 * Created by Igor on 03.09.2018.
 */

object Validator {
    @Throws(IncorrectDataException::class)
    fun validateUserData(userDTO: ExtendedUserDTO?): Boolean {
        val email = userDTO?.email
        val password = userDTO?.password
        val reEnterPassword = userDTO?.reEnterPassword
        val name = userDTO?.name
        val surname = userDTO?.surname
        val patronymic = userDTO?.patronymic
        val rating = userDTO?.rating
        val birthdate = userDTO?.birthday
        val status = userDTO?.status//todo видимо нужно удалить статус
        val phoneNumber = userDTO?.phoneNumber
        val coach = userDTO?.selectedCoachIndex
        val rank = userDTO?.selectedRankIndex
        val country = userDTO?.selectedCountryIndex
        val isMale = userDTO?.selectedGenderIndex

        validateRegistrationPassword(password, reEnterPassword)
        validateEmail(email)

        if (StringUtils.isEmpty(name) || name!!.length < 3 || name.length > 30) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_USER_NAME))
        }
        if (StringUtils.isEmpty(surname) || surname!!.length < 3 || surname.length > 30) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_USER_SURNAME))
        }
        if (StringUtils.isEmpty(patronymic) || patronymic!!.length < 3 || patronymic.length > 30) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_USER_PATRONYMIC))
        }
        if (rating == null || rating < 0) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_USER_RATING_SMALL))
        }
        if (rating > 5000) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_USER_RATING_BIG))
        }
        if (StringUtils.isEmpty(birthdate)) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_USER_BIRTHDAY))
        }
        if (StringUtils.isEmpty(phoneNumber)) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_USER_PHONE_NUMBER))
        }
        if (rank == 0) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_RANK_IS_NOT_SELECTED))
        }
        if (isMale == 0) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_GENDER_IS_NOT_SELECTED))
        }
        if (country == 0) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_COUNTRY_IS_NOT_SELECTED))
        }
        if (coach == 0) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_COACH_IS_NOT_SELECTED))
        }

        return true
    }

    @Throws(IncorrectDataException::class)
    fun validateAuthUserData(userDTO: UserDTO?): Boolean {
        val email = userDTO?.email
        val password = userDTO?.password

        validatePassword(password)
        validateEmail(email)
        return true
    }

    @Throws(IncorrectDataException::class)
    private fun validateEmail(email: String?) {
        if (StringUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_USER_EMAIL))
        }
    }

    @Throws(IncorrectDataException::class)
    private fun validateRegistrationPassword(password: String?, reEnterPassword: String?) {
        validatePassword(password)
        if (!password.equals(reEnterPassword)) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_USER_RE_ENTER_PASSWORD))
        }
    }

    @Throws(IncorrectDataException::class)
    private fun validatePassword(password: String?) {
        if (StringUtils.isEmpty(password) || password?.length!! < 6) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_USER_PASSWORD))
        }
    }


    @Throws(IncorrectDataException::class)
    fun validateTournamentData(tournamentDTO: ExtendedTournamentDTO?): Boolean {
        val name = tournamentDTO?.name
        val toursCount = tournamentDTO?.toursCount
        val shortDescription = tournamentDTO?.shortDescription
        val fullDescription = tournamentDTO?.fullDescription
        val countPlayersInTeam = tournamentDTO?.countPlayersInTeam
        val image = tournamentDTO?.image
        val startDate = tournamentDTO?.startDate
        val finishDate = tournamentDTO?.finishDate
        val referee = tournamentDTO?.selectedRefereeIndex
        val place = tournamentDTO?.selectedPlaceIndex

        if (StringUtils.isEmpty(name) || name!!.length < 8 || name.length > 50) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_TOURNAMENT_NAME))
        }
        if (StringUtils.isEmpty(shortDescription) || shortDescription!!.length < 20 || shortDescription.length > 100) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_TOURNAMENT_SHORT_DESCRIPTION))
        }
        if (StringUtils.isEmpty(fullDescription) || fullDescription!!.length < 2 || fullDescription.length > 20000) {//todo от 100
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_TOURNAMENT_FULL_DESCRIPTION))
        }
        if (countPlayersInTeam == null || countPlayersInTeam > 20) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_TOURNAMENT_COUNT_PLAYERS_IN_TEAM))
        }
        if (toursCount == null || toursCount < 1) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_TOURNAMENT_TOURS_COUNT))
        }
        if (StringUtils.isEmpty(image)) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_TOURNAMENT_IMAGE))
        }
        if (StringUtils.isEmpty(startDate)) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_TOURNAMENT_START_DATE))
        }
        if (StringUtils.isEmpty(finishDate)) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_TOURNAMENT_FINISH_DATE))
        }
        if (referee == 0) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_REFEREE_IS_NOT_SELECTED))
        }
        if (place == 0) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_PLACE_IS_NOT_SELECTED))
        }
        return true
    }

    @Throws(IncorrectDataException::class)
    fun validateGameData(gameDTO: GameDTO?): Boolean {
        val gameRecord = gameDTO?.gameRecord
        val countPointsFirstPlayer = gameDTO?.countPointsFirstPlayer
        val countPointsSecondPlayer = gameDTO?.countPointsSecondPlayer
        val firstChessPlayer = gameDTO?.firstChessPlayer
        val secondChessPlayer = gameDTO?.secondChessPlayer
        val matchDTO = gameDTO?.matchDTO

        if (StringUtils.isEmpty(gameRecord) || gameRecord!!.length < 2 || gameRecord.length > 20000) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_GAME_RECORD))
        }
        if (countPointsFirstPlayer == null || countPointsFirstPlayer < 0 || countPointsSecondPlayer == null || countPointsSecondPlayer < 0) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_COUNT_POINTS_OF_PLAYER_SMALL))
        }
        if (countPointsFirstPlayer > 1 || countPointsSecondPlayer > 1) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_COUNT_POINTS_OF_PLAYER_BIG))
        }
        return true
    }

    @Throws(IncorrectDataException::class)
    fun validateMatchData(matchDTO: MatchDTO?): Boolean {
        val date = matchDTO?.date
        val countPointsFirstTeam = matchDTO?.countPointsFirstTeam
        val countPointsSecondTeam = matchDTO?.countPointsSecondTeam
        val firstTeam = matchDTO?.firstTeam
        val secondTeam = matchDTO?.secondTeam
        val tournament = matchDTO?.tournament

        if (countPointsFirstTeam == null || countPointsFirstTeam < 0 || countPointsSecondTeam == null || countPointsSecondTeam < 0) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_MATCH_COUNT_POINTS_OF_TEAM))
        }
        if (StringUtils.isEmpty(date)) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_MATCH_DATE))
        }
        return true
    }/*турс каунт перенести везде из места в турнир*/

    @Throws(IncorrectDataException::class)
    fun validatePlaceData(placeDTO: ExtendedPlaceDTO?): Boolean {
        val name = placeDTO?.name
        val capacity = placeDTO?.capacity
        val building = placeDTO?.building
        val city = placeDTO?.city
        val street = placeDTO?.street
        val image = placeDTO?.image
        val country = placeDTO?.selectedCountryIndex

        if (StringUtils.isEmpty(name) || name!!.isEmpty() || name.length > 100) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_PLACE_NAME))
        }
        if (StringUtils.isEmpty(city) || city!!.length < 3 || city.length > 50) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_PLACE_CITY))
        }
        if (StringUtils.isEmpty(street) || street!!.length < 3 || street.length > 50) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_PLACE_STREET))
        }
        if (StringUtils.isEmpty(building) || building!!.isEmpty() || building.length > 10) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_PLACE_BUILDING))
        }
        if (capacity == null || capacity < 1) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_PLACE_CAPACITY_SMALL))
        }
        if (capacity > 10000) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_PLACE_CAPACITY_BIG))
        }
        if (country == 0) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_COUNTRY_IS_NOT_SELECTED))
        }
        if (StringUtils.isEmpty(image)) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_PLACE_IMAGE))
        }

        return true
    }

    @Throws(IncorrectDataException::class)
    fun validateRankData(rankDTO: RankDTO?): Boolean {
        val name = rankDTO?.name
        val abbreviation = rankDTO?.abbreviation

        if (StringUtils.isEmpty(name) || name!!.length < 3 || name.length > 50) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_RANK_NAME))
        }
        if (StringUtils.isEmpty(abbreviation) || abbreviation!!.length < 2 || abbreviation.length > 3) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_RANK_ABBREVIATION))
        }

        return true
    }

    @Throws(IncorrectDataException::class)
    fun validateCountryData(countryDTO: CountryDTO?): Boolean {
        val name = countryDTO?.name
        val abbreviation = countryDTO?.abbreviation

        if (StringUtils.isEmpty(name) || name!!.length < 3 || name.length > 50) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_COUNTRY_NAME))
        }
        if (StringUtils.isEmpty(abbreviation) || abbreviation!!.length < 3 || abbreviation.length > 3) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_COUNTRY_ABBREVIATION))
        }

        return true
    }

    @Throws(IncorrectDataException::class)
    fun validateTeamData(teamDTO: TeamDTO?): Boolean {
        val name = teamDTO?.name

        if (StringUtils.isEmpty(name) || name!!.length < 3 || name.length > 50) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_TEAM_NAME))
        }

        return true
    }

    @Throws(IncorrectDataException::class)
    fun validateTournamentTeamRankingData(tournamentTeamRankingDTO: TournamentTeamRankingDTO?): Boolean {
        val points = tournamentTeamRankingDTO?.points
        val position = tournamentTeamRankingDTO?.position

        if (points == null || points < 0) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_TOURNAMENT_TEAM_COUNT_POINTS))
        }
        if (position == null || position > 1) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_TOURNAMENT_TEAM_POSITION))
        }
        return true
    }
}
