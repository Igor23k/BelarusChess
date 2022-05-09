package bobrchess.of.by.belaruschess.presenter.impl;

import static bobrchess.of.by.belaruschess.util.Constants.TOKEN;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bobrchess.of.by.belaruschess.R;
import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.TokenDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.dto.extended.ExtendedTournamentDTO;
import bobrchess.of.by.belaruschess.exception.IncorrectDataException;
import bobrchess.of.by.belaruschess.network.connection.internal.AddTournamentConnection;
import bobrchess.of.by.belaruschess.network.connection.internal.TokenConnection;
import bobrchess.of.by.belaruschess.presenter.AddTournamentPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAddTournament;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackToken;
import bobrchess.of.by.belaruschess.util.Constants;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.util.Validator;
import bobrchess.of.by.belaruschess.view.activity.AddTournamentContractView;
import bobrchess.of.by.belaruschess.view.activity.PackageModel;

@InjectViewState
public class AddTournamentPresenterImpl extends MvpPresenter<AddTournamentContractView> implements CallBackAddTournament, CallBackToken, AddTournamentPresenter {

    private AddTournamentContractView view;
    private AddTournamentConnection addTournamentConnection;
    private TokenConnection tokenConnection;
    private Boolean viewIsReady = false;
    private Integer selectedPlaceIndex = 0;
    private Integer selectedRefereeIndex = 0;
    private Byte countSuccessfulSpinnerResponses = 0;
    private Map<Integer, PlaceDTO> placesIndexes = new HashMap<>();
    private Map<Integer, UserDTO> refereesIndexes = new HashMap<>();
    private PackageModel packageModel;
    private Integer connectivityStatus = 0;
    private TournamentDTO updatedTournament = null;
    private File updatedTournamentImage = null;

    public AddTournamentPresenterImpl() {
    }

    public AddTournamentPresenterImpl(AddTournamentContractView view) {
        addTournamentConnection = new AddTournamentConnection();
        tokenConnection = new TokenConnection();
        addTournamentConnection.attachPresenter(this);
        tokenConnection.attachPresenter(this);
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
    public void onResponse(@NotNull TokenDTO tokenDTO) {
        packageModel.addSharePref(TOKEN, tokenDTO.getToken());
        addTournamentConnection.addTournament(new TournamentDTO(updatedTournament), Util.Companion.compressImage(updatedTournamentImage), packageModel.getSharePrefValue(TOKEN));
    }

    @Override
    public void onFailure(@NonNull ErrorDTO errorDTO) {
        if (Constants.Companion.getTOKEN_IS_EXPIRED_MESSAGE().equals(errorDTO.getMessage())) {
            refreshToken();
        } else {
            view.showToast(errorDTO.getError());
            view.hideProgress();
        }
    }

    @Override
    public void addTournament(@NonNull ExtendedTournamentDTO tournamentDTO, File tournamentImage) {
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

                updatedTournament = new TournamentDTO(tournamentDTO);
                updatedTournamentImage = Util.Companion.compressImage(tournamentImage);

                addTournamentConnection.addTournament(updatedTournament, updatedTournamentImage, packageModel.getSharePrefValue(TOKEN));
            } catch (IncorrectDataException e) {
                view.showToast(e.getLocalizedMessage());
                view.hideProgress();
                view.enableButton();
            }
        }
    }

    private void refreshToken() {
        String refreshToken = packageModel.getSharePrefValue(Constants.REFRESH_TOKEN);
        if (!StringUtils.isEmpty(refreshToken)) {
            tokenConnection.refreshToken(refreshToken);
        } else {
            onUnsuccessfulRequest(Util.Companion.getInternalizedMessage(Constants.Companion.getKEY_INTERNAL_SERVER_ERROR()));
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
        addTournamentConnection.getReferees(packageModel.getSharePrefValue(TOKEN));
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
