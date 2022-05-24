package bobrchess.of.by.belaruschess.util

import bobrchess.of.by.belaruschess.dto.*
import bobrchess.of.by.belaruschess.dto.extended.ExtendedPlaceDTO
import bobrchess.of.by.belaruschess.dto.extended.ExtendedTournamentDTO
import bobrchess.of.by.belaruschess.dto.extended.ExtendedUserDTO
import bobrchess.of.by.belaruschess.exception.IncorrectDataException
import bobrchess.of.by.belaruschess.util.Util.Companion.getInternalizedMessage
import org.springframework.util.StringUtils

object Validator {
    @Throws(IncorrectDataException::class)
    fun validateUserData(userDTO: ExtendedUserDTO?, checkIndexes: Boolean): Boolean {
        val email = userDTO?.email
        val password = userDTO?.password
        val reEnterPassword = userDTO?.reEnterPassword
        val name = userDTO?.name
        val surname = userDTO?.surname
        val patronymic = userDTO?.patronymic
        val rating = userDTO?.rating
        val birthday = userDTO?.birthday
        val rank = userDTO?.selectedRankIndex
        val country = userDTO?.selectedCountryIndex
        val isMale = userDTO?.selectedGenderIndex

        validateRegistrationPassword(password, reEnterPassword)
        validateEmail(email)

        if (StringUtils.isEmpty(name) || name!!.length < 2 || name.length > 30) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_USER_NAME))
        }
        if (StringUtils.isEmpty(surname) || surname!!.length < 2 || surname.length > 30) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_USER_SURNAME))
        }
        if (StringUtils.isEmpty(patronymic) || patronymic!!.length < 2 || patronymic.length > 30) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_USER_PATRONYMIC))
        }
        if (rating == null || rating < 0) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_USER_RATING_SMALL))
        }
        if (rating > 5000) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_USER_RATING_BIG))
        }
        if (StringUtils.isEmpty(birthday)) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_USER_BIRTHDAY))
        }

        if (checkIndexes) {
            if (rank == 0) {
                throw IncorrectDataException(getInternalizedMessage(Constants.KEY_RANK_IS_NOT_SELECTED))
            }
            if (isMale == 0) {
                throw IncorrectDataException(getInternalizedMessage(Constants.KEY_GENDER_IS_NOT_SELECTED))
            }
            if (country == 0) {
                throw IncorrectDataException(getInternalizedMessage(Constants.KEY_COUNTRY_IS_NOT_SELECTED))
            }
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
    fun validateEmail(email: String?) {
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
        val fullDescription = tournamentDTO?.fullDescription
        val startDate = tournamentDTO?.startDate
        val finishDate = tournamentDTO?.finishDate
        val referee = tournamentDTO?.selectedRefereeIndex
        val place = tournamentDTO?.selectedPlaceIndex

        if (StringUtils.isEmpty(name) || name!!.length < 8 || name.length > 100) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_TOURNAMENT_NAME))
        }
        if (StringUtils.isEmpty(fullDescription) || fullDescription!!.length < 100 || fullDescription.length > 10000) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_TOURNAMENT_FULL_DESCRIPTION))
        }
        if (toursCount == null || toursCount < 1) {
            throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_TOURNAMENT_TOURS_COUNT))
        }
        /* if (StringUtils.isEmpty(tournamentImageUri)) {
             throw IncorrectDataException(getInternalizedMessage(Constants.KEY_INCORRECT_TOURNAMENT_IMAGE))
         }*/
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
    }

    @Throws(IncorrectDataException::class)
    fun validatePlaceData(placeDTO: ExtendedPlaceDTO?): Boolean {
        val name = placeDTO?.name
        val capacity = placeDTO?.capacity
        val building = placeDTO?.building
        val city = placeDTO?.city
        val street = placeDTO?.street
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
