package bobrchess.of.by.belaruschess.view.activity.impl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bobrchess.of.by.belaruschess.R;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.presenter.AddTournamentPresenter;
import bobrchess.of.by.belaruschess.presenter.impl.AddTournamentPresenterImpl;
import bobrchess.of.by.belaruschess.view.activity.AddTournamentContractView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static bobrchess.of.by.belaruschess.util.Constants.EMPTY_STRING;
import static bobrchess.of.by.belaruschess.util.Constants.TOURNAMENT_PARAMETER;

public class AddTournamentActivity extends AppCompatActivity implements AddTournamentContractView {

    private static final String TAG = "AddTournamentActivity";
    private static final int ADD_TOURNAMENT_REQUEST = 1;

    @BindView(R.id.e_name_input)
    EditText nameText;

    @BindView(R.id.e_short_description_input)
    EditText shortDescriptionText;

    @BindView(R.id.e_full_description_input)
    EditText fullDescriptionText;

    private Toolbar toolbar;

    //добавить 2 даты, place и referee

    @BindView(R.id.b_registration)
    Button addTournamentButton;

    private ProgressDialog progressDialog;

    private AddTournamentPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tournament);
        ButterKnife.bind(this);

        toolbar = findViewById(R.id.toolbar);
        presenter = new AddTournamentPresenterImpl();
        presenter.attachView(this);
        presenter.viewIsReady();
        setSupportActionBar(toolbar);
        initButtonsListeners();
    }

    private void initButtonsListeners() {
        addTournamentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(AddTournamentActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                addTournament();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_TOURNAMENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onAddTournamentSuccess(TournamentDTO tournamentDTO) {
        enableButton();
        finish();
        Intent intent = new Intent(getApplicationContext(), TournamentActivity.class);
        putTournamentData(intent, tournamentDTO);
        startActivityForResult(intent, ADD_TOURNAMENT_REQUEST);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void putTournamentData(Intent intent, TournamentDTO tournamentDTO) {
        intent.putExtra(TOURNAMENT_PARAMETER, tournamentDTO);
    }

    @Override
    public void onAddTournamentFailed() {
        addTournamentButton.setEnabled(true);
    }

    @Override
    public void showToast(Integer resId) {
        Toast toast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showIncorrectTournamentNameText() {
        nameText.setError(this.getString(R.string.incorrect_tournament_name));
    }

    @Override
    public void onConnectionError() {
        enableButton();
        showToast(R.string.connection_error);
    }

    @Override
    public void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void addTournament() {
        presenter.addTournament();
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(this, EMPTY_STRING, "Please, wait ..."/*R.string.please_wait*/);
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void enableButton() {
        addTournamentButton.setEnabled(true);
    }

    @Override
    public void disableButton() {
        addTournamentButton.setEnabled(false);
    }

    @Override
    public TournamentDTO getTournamentData() {
        TournamentDTO tournamentData = new TournamentDTO();
        tournamentData.setName(nameText.getText().toString());
        tournamentData.setShortDescription(shortDescriptionText.getText().toString());
        tournamentData.setFullDescription(fullDescriptionText.getText().toString());
        return tournamentData;
    }
}