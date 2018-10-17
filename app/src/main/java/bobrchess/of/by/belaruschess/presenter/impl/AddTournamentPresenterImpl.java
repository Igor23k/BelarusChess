package bobrchess.of.by.belaruschess.presenter.impl;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.exception.IncorrectDataException;
import bobrchess.of.by.belaruschess.network.connection.AddTournamentConnection;
import bobrchess.of.by.belaruschess.presenter.AddTournamentPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAddTournament;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.util.Validator;
import bobrchess.of.by.belaruschess.view.activity.AddTournamentContractView;

@InjectViewState
public class AddTournamentPresenterImpl extends MvpPresenter<AddTournamentContractView> implements CallBackAddTournament, AddTournamentPresenter {

    private AddTournamentContractView view;
    private AddTournamentConnection addTournamentConnection;
    private Boolean viewIsReady = false;
    private Integer selectedPlaceIndex;
    private Integer selectedRefereeIndex;
    private Byte countSuccessfulSpinnerResponses = 0;
    private Map<Integer, PlaceDTO> placesIndexes = new HashMap<>();
    private Map<Integer, UserDTO> refereesIndexes = new HashMap<>();


    public AddTournamentPresenterImpl() {
    }

    public AddTournamentPresenterImpl(AddTournamentContractView view) {
        addTournamentConnection = new AddTournamentConnection();
        addTournamentConnection.attachPresenter(this);
        attachView(view);
        view.showProgress();
    }

    @Override
    public void onResponse(@NonNull TournamentDTO tournamentDTO) {
        view.hideProgress();
        view.enableButton();
        view.startActivity(tournamentDTO);
    }

    @Override
    public void onRefereeResponse(@NonNull List<UserDTO> referees) {
        saveRefereesIndexes(referees);
        view.setRefereeSpinnerAdapter(Util.getUsersNames(referees));
        checkIsViewReady();
    }

    @Override
    public void onPlaceResponse(@NonNull List<PlaceDTO> places) {
        savePlacesIndexes(places);
        view.setPlaceSpinnerAdapter(Util.getPlacesNames(places));
        checkIsViewReady();
    }

    @Override
    public void onFailure(@NonNull Throwable t) {
        view.showToast(t.getLocalizedMessage());
    }

    @Override
    public void addTournament(TournamentDTO tournamentDTO) {
        if (viewIsReady) {
            view.disableButton();
            tournamentDTO.setPlace(placesIndexes.get(selectedPlaceIndex));
            tournamentDTO.setReferee(refereesIndexes.get(selectedRefereeIndex));
            try {
                Validator.INSTANCE.validateTournamentData(tournamentDTO);
                view.disableButton();
                view.showProgress();
                addTournamentConnection.addTournament(tournamentDTO);
            } catch (IncorrectDataException e) {
                view.showIncorrectTournamentNameText();
            } finally {
                view.hideProgress();
                view.enableButton();
            }
        }
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
            placesIndexes.put(i, places.get(i));
        }
    }

    private void saveRefereesIndexes(List<UserDTO> referees) {
        for (int i = 0; i < referees.size(); i++) {
            refereesIndexes.put(i, referees.get(i));
        }
    }

    synchronized private void checkIsViewReady() {
        countSuccessfulSpinnerResponses++;
        if (countSuccessfulSpinnerResponses == 2) {
            viewIsReady();
        }
    }
}
