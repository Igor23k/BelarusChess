package bobrchess.of.by.belaruschess.presenter.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.exception.IncorrectTournamentNameException;
import bobrchess.of.by.belaruschess.network.connection.AddTournamentConnection;
import bobrchess.of.by.belaruschess.presenter.AddTournamentPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAddTournament;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.AddTournamentContractView;

import static bobrchess.of.by.belaruschess.util.Constants.INCORRECT_TOURNAMENT_NAME;

/**
 * Created by Igor on 11.04.2018.
 */

public class AddTournamentPresenterImpl implements CallBackAddTournament, AddTournamentPresenter {

    private AddTournamentContractView view;
    private AddTournamentConnection addTournamentConnection;
    private Boolean viewIsReady = false;
    private List<PlaceDTO> places;
    private List<UserDTO> referees;
    private Integer selectedPlaceIndex;
    private Integer selectedRefereeIndex;
    private Map<Integer, PlaceDTO> placesIndexes = new HashMap<>();
    private Map<Integer, UserDTO> refereesIndexes = new HashMap<>();

    public AddTournamentPresenterImpl() {
        addTournamentConnection = new AddTournamentConnection();
        addTournamentConnection.attachPresenter(this);
    }

    @Override
    public void onResponse(TournamentDTO tournamentDTO) {
        view.hideProgress();
        view.enableButton();
        view.startActivity(tournamentDTO);
    }

    @Override
    public void onRefereeResponse(List<UserDTO> referees) {
        this.referees = referees;
        saveRefereesIndexes(referees);
        view.hideProgress();
        view.enableButton();
        view.setRefereeSpinnerAdapter(Util.getUsersNames(referees));
    }

    @Override
    public void onPlaceResponse(List<PlaceDTO> places) {
        this.places = places;
        savePlacesIndexes(places);
        view.hideProgress();
        view.enableButton();
        view.setCountrySpinnerAdapter(Util.getPlacesNames(places));// bug почему называется country spinner?
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
        view.enableButton();
        view.showToast(t.getLocalizedMessage());
    }

    @Override
    public void addTournament() {
        if (viewIsReady) {
            view.disableButton();
            TournamentDTO tournamentDTO = view.getTournamentData();
            tournamentDTO.setPlace(placesIndexes.get(selectedPlaceIndex));
            tournamentDTO.setReferee(refereesIndexes.get(selectedRefereeIndex));
            try {
                validateTournamentData(tournamentDTO);
                view.disableButton();
                view.showProgress();
                addTournamentConnection.addTournament(tournamentDTO);
            } catch (IncorrectTournamentNameException e) {
                view.showIncorrectTournamentNameText();
                view.hideProgress();
                view.enableButton();
            }
        }
    }

    private boolean validateTournamentData(TournamentDTO tournamentDTO) throws IncorrectTournamentNameException {
        //TODO
        String name = tournamentDTO.getName();

        if (name == null || name.isEmpty()) {
            throw new IncorrectTournamentNameException(INCORRECT_TOURNAMENT_NAME);
        }

        return true;
    }

    public void attachView(AddTournamentContractView activity) {
        view = activity;
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {
        viewIsReady = true;
    }

    @Override
    public void loadReferees() {
        addTournamentConnection.getReferees();
    }

    @Override
    public void loadPlaces() {
        addTournamentConnection.getPlaces();
    }

    @Override
    public void setSelectedPlaceIndex(Integer selectedPlaceIndex) {
        this.selectedPlaceIndex = selectedPlaceIndex;
    }

    @Override
    public void setSelectedRefereeIndex(Integer selectedRefereeIndex) {
        this.selectedRefereeIndex = selectedRefereeIndex;
    }

    private void savePlacesIndexes(List<PlaceDTO> places) {
        for (int i = 0; i < places.size(); i++) {
            placesIndexes.put(i,places.get(i));
        }
    }

    private void saveRefereesIndexes(List<UserDTO> referees) {
        for (int i = 0; i < referees.size(); i++) {
            refereesIndexes.put(i,referees.get(i));
        }
    }
}
