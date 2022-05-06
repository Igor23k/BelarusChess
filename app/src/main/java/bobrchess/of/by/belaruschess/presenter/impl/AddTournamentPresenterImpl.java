package bobrchess.of.by.belaruschess.presenter.impl;

import static bobrchess.of.by.belaruschess.util.Constants.TOKEN;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bobrchess.of.by.belaruschess.R;
import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.dto.extended.ExtendedTournamentDTO;
import bobrchess.of.by.belaruschess.exception.IncorrectDataException;
import bobrchess.of.by.belaruschess.network.connection.internal.AddTournamentConnection;
import bobrchess.of.by.belaruschess.presenter.AddTournamentPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAddTournament;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.util.Validator;
import bobrchess.of.by.belaruschess.view.activity.AddTournamentContractView;
import bobrchess.of.by.belaruschess.view.activity.PackageModel;

@InjectViewState
public class AddTournamentPresenterImpl extends MvpPresenter<AddTournamentContractView> implements CallBackAddTournament, AddTournamentPresenter {

    private AddTournamentContractView view;
    private AddTournamentConnection addTournamentConnection;
    private Boolean viewIsReady = false;
    private Integer selectedPlaceIndex = 0;
    private Integer selectedRefereeIndex = 0;
    private Byte countSuccessfulSpinnerResponses = 0;
    private Map<Integer, PlaceDTO> placesIndexes = new HashMap<>();
    private Map<Integer, UserDTO> refereesIndexes = new HashMap<>();
    private PackageModel packageModel;
    private Integer connectivityStatus = 0;

    public AddTournamentPresenterImpl() {
    }

    public AddTournamentPresenterImpl(AddTournamentContractView view) {
        addTournamentConnection = new AddTournamentConnection();
        addTournamentConnection.attachPresenter(this);
        attachView(view);
    }

    @Override
    public void onResponse(@NonNull TournamentDTO tournamentDTO) {
        view.hideProgress();
        view.enableButton();
        view.tournamentAdded(tournamentDTO);
    }

    @Override
    public void onRefereeResponse(@NonNull List<? extends UserDTO> referees) {
        saveRefereesIndexes(referees);
        view.setRefereeSpinnerAdapter(referees);
        checkIsViewReady();
    }

    @Override
    public void onPlaceResponse(@NonNull List<? extends PlaceDTO> places) {
        savePlacesIndexes(places);
        view.setPlaceSpinnerAdapter(places);
        checkIsViewReady();
    }

    @Override
    public void onResponse(long removedTournamentId) {
        if (removedTournamentId != 0) {
            view.removeTournamentFromLocalStorage(removedTournamentId);
            view.showSnackbar(R.string.tournament_deleted_notification);
        }
        view.hideProgress();
    }

    @Override
    public void onFailure(@NonNull ErrorDTO errorDTO) {
        view.showToast(errorDTO.getError());
        view.hideProgress();
    }

    @Override
    public void addTournament(@NonNull ExtendedTournamentDTO tournamentDTO, String tournamentImageUri) {
        if (viewIsReady) {
            view.disableButton();
            try {
                tournamentDTO.setSelectedPlaceIndex(selectedPlaceIndex);
                tournamentDTO.setSelectedRefereeIndex(selectedRefereeIndex);
                Validator.INSTANCE.validateTournamentData(tournamentDTO);
                tournamentDTO.setPlace(placesIndexes.get(selectedPlaceIndex - 1));
                tournamentDTO.setReferee(refereesIndexes.get(selectedRefereeIndex - 1));
                removeRedundantImages(tournamentDTO);
                view.disableButton();
                view.showProgress();
                addTournamentConnection.addTournament(new TournamentDTO(tournamentDTO), Util.Companion.compressImage(tournamentImageUri), packageModel.getValue(TOKEN));
            } catch (IncorrectDataException e) {
                view.showToast(e.getLocalizedMessage());
                view.hideProgress();
                view.enableButton();
            }
        }
    }

    private void removeRedundantImages(ExtendedTournamentDTO tournamentDTO) {
        if (tournamentDTO.getPlace() != null) {
            tournamentDTO.getPlace().setImage(null);
        }
        if (tournamentDTO.getReferee() != null) {
            tournamentDTO.getReferee().setImage(null);
        }
    }


    public void attachView(@NonNull AddTournamentContractView activity) {
        view = activity;
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {
        viewIsReady = true;
    }

    @Override
    public void removeTournament(Long id) {
        if (viewIsReady) {
            view.showProgress();
            addTournamentConnection.removeTournament(id);
        }
    }

    @Override
    public void loadReferees() {
        addTournamentConnection.getReferees(packageModel.getValue(TOKEN));
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

    public void savePlacesIndexes(List<? extends PlaceDTO> places) {
        for (int i = 0; i < places.size(); i++) {
            placesIndexes.put(i, places.get(i));
        }
    }

    public void saveRefereesIndexes(List<? extends UserDTO> referees) {
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

    @Override
    public void onServerUnavailable() {
        view.hideProgress();
    }

    @Override
    public void onUnsuccessfulRequest(@Nullable String message) {
        view.hideProgress();
    }

    @Override
    public void setConnectivityStatus(Integer status) {
        this.connectivityStatus = status;
    }

    public void setPackageModel(PackageModel packageModel) {
        this.packageModel = packageModel;
    }

    @Override
    public boolean isConnected(int status) {
        return Util.Companion.isConnected(status);
    }
}
