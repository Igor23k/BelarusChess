package bobrchess.of.by.belaruschess.presenter.impl;

import android.widget.TextView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import bobrchess.of.by.belaruschess.R;
import bobrchess.of.by.belaruschess.dto.ErrorDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.network.connection.SearchTournamentConnection;
import bobrchess.of.by.belaruschess.presenter.SearchTournamentPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackSearchTournament;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.SearchTournamentContractView;
import bobrchess.of.by.belaruschess.view.activity.impl.SearchTournamentActivity;
import butterknife.BindView;

/**
 * Created by Igor on 04.05.2018.
 */

@InjectViewState
public class SearchTournamentPresenterImpl  extends MvpPresenter<SearchTournamentContractView> implements CallBackSearchTournament, SearchTournamentPresenter {

    private SearchTournamentActivity view;
    private SearchTournamentConnection tournamentConnection;
    private Boolean viewIsReady = false;
    private Integer connectivityStatus = 0;

    @BindView(R.id.t_link_registration)
    TextView registrationLink;

    public SearchTournamentPresenterImpl() {
        tournamentConnection = new SearchTournamentConnection();
        tournamentConnection.attachPresenter(this);
    }

    @Override
    public void loadTournaments() {
        tournamentConnection.getTournaments();
    }

    @Override
    public void loadTournaments(Integer count) {
        tournamentConnection.getTournaments(count);
    }

    @Override
    public void searchTournaments() {
        if(viewIsReady) {
            String text = view.getSearchText();
            tournamentConnection.searchTournaments(text);
        }
    }

    @Override
    public void attachView(@NotNull SearchTournamentActivity activity) {
        view = activity;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void viewIsReady() {
        viewIsReady = true;
    }

    @Override
    public void onResponse(@NotNull List<TournamentDTO> tournamentsDTO) {
        view.showTournaments(tournamentsDTO);
    }

    @Override
    public void onFailure(@NotNull ErrorDTO errorDTO) {
        view.hideProgress();
        view.showToast(errorDTO.getError());
    }

    @Override
    public void onServerUnavailable() {

    }

    @Override
    public void onUnsuccessfulRequest(@Nullable String message) {

    }

    @Override
    public void setConnectivityStatus(Integer status) {
        this.connectivityStatus = status;
    }

    @Override
    public boolean isConnected(int status) {
        return Util.Companion.isConnected(status);
    }//todo remove
}
