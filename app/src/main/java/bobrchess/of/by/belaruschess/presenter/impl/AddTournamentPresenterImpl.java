package bobrchess.of.by.belaruschess.presenter.impl;

import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.exception.IncorrectTournamentNameException;
import bobrchess.of.by.belaruschess.network.connection.AddTournamentConnection;
import bobrchess.of.by.belaruschess.presenter.AddTournamentPresenter;
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAddTournament;
import bobrchess.of.by.belaruschess.view.activity.AddTournamentContractView;

import static bobrchess.of.by.belaruschess.util.Constants.INCORRECT_TOURNAMENT_NAME;

/**
 * Created by Igor on 11.04.2018.
 */

public class AddTournamentPresenterImpl implements CallBackAddTournament, AddTournamentPresenter {

    private AddTournamentContractView view;
    private AddTournamentConnection addTournamentConnection;
    private Boolean viewIsReady = false;

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
    public void onFailure(Throwable t) {
        view.hideProgress();
        view.enableButton();
        view.showToast(t.getLocalizedMessage());
    }

    @Override
    public void addTournament() {
        if(viewIsReady) {
            view.disableButton();
            TournamentDTO tournamentDTO = view.getTournamentData();
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
}
