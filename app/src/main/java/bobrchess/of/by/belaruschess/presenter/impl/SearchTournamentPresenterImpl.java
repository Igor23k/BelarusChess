package bobrchess.of.by.belaruschess.presenter.impl;

import android.widget.TextView;

import java.util.List;

import bobrchess.of.by.belaruschess.R;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.network.connection.SearchTournamentConnection;
import bobrchess.of.by.belaruschess.presenter.SearchTournamentPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackSearchTournament;
import bobrchess.of.by.belaruschess.view.activity.impl.SearchTournamentActivity;
import butterknife.BindView;

/**
 * Created by Igor on 04.05.2018.
 */

public class SearchTournamentPresenterImpl implements CallBackSearchTournament, SearchTournamentPresenter {

    private SearchTournamentActivity view;
    private SearchTournamentConnection tournamentConnection;
    private Boolean viewIsReady = false;

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
    public void attachView(SearchTournamentActivity activity) {
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
    public void onResponse(List<TournamentDTO> tournamentsDTO) {
        view.showTournaments(tournamentsDTO);
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
        view.showToast(t.getLocalizedMessage());
    }
}
