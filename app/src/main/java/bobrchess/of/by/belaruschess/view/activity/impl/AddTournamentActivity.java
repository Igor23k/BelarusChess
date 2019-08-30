package bobrchess.of.by.belaruschess.view.activity.impl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.List;

import bobrchess.of.by.belaruschess.R;
import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.presenter.AddTournamentPresenter;
import bobrchess.of.by.belaruschess.presenter.impl.AddTournamentPresenterImpl;
import bobrchess.of.by.belaruschess.util.Constants;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.AddTournamentContractView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTournamentActivity extends AbstractActivity implements AddTournamentContractView, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @InjectPresenter
    AddTournamentPresenterImpl presenter;
    @BindView(R.id.e_name_input)
    EditText nameText;
    @BindView(R.id.tournament_short_description_input)
    EditText shortDescriptionText;
    @BindView(R.id.tournament_full_description_input)
    EditText fullDescriptionText;
    @BindView(R.id.clock_image)
    ImageView clockImage;
    @BindView(R.id.calendar_image)
    ImageView calendarImage;

    private Toolbar toolbar;

    //todo добавить place и referee

    @BindView(R.id.b_registration)
    Button addTournamentButton;

    private ProgressDialog progressDialog;

    private PlaceDTO place;
    private UserDTO referee;
    private String startDate;
    private String finishDate;
    private Spinner refereeSpinner;
    private Spinner placeSpinner;

    private boolean manyPeopleInTeam;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tournament);
        ButterKnife.bind(this);
        registerInternetCheckReceiver();

        toolbar = findViewById(R.id.toolbar);
        presenter.attachView(this);
        refereeSpinner = findViewById(R.id.refereeSpinner);
        refereeSpinner.setOnItemSelectedListener(new RefereeItemSelectedListener());
        placeSpinner = findViewById(R.id.s_countrySpinner);
        placeSpinner.setOnItemSelectedListener(new PlaceItemSelectedListener());
        setSupportActionBar(toolbar);
        initButtonsListeners();

        presenter.loadPlaces();
        presenter.loadReferees();

        CheckBox team_type = findViewById(R.id.team_type_checkbox);
        team_type.setOnCheckedChangeListener((compoundButton, b) -> manyPeopleInTeam = b);


        calendarImage.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    AddTournamentActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.show(getFragmentManager(), Constants.Companion.getDATE_PICKER_DIALOG());
        });

        clockImage.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            TimePickerDialog tpd = TimePickerDialog.newInstance(
                    AddTournamentActivity.this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    false
            );
            tpd.show(getFragmentManager(), Constants.Companion.getTIME_PICKER_DIALOG());
        });
        presenter.viewIsReady(); // todo нужно де ждать пока зыпросы вернутся. + лучше сделать чтобы там был 1 запрос а не а
    }

    private void initButtonsListeners() {
        addTournamentButton.setOnClickListener(v -> {
            final ProgressDialog progressDialog = new ProgressDialog(AddTournamentActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            addTournament();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Util.Companion.getADD_TOURNAMENT_REQUEST()) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void startActivity(TournamentDTO tournamentDTO) {
        Intent intent = new Intent(getApplicationContext(), TournamentInfoActivity.class);
        putTournamentData(intent, tournamentDTO);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void putTournamentData(Intent intent, TournamentDTO tournamentDTO) {
        intent.putExtra(Constants.Companion.getTOURNAMENT_PARAMETER(), tournamentDTO);
    }

    @Override
    public void showIncorrectTournamentNameText() {
        nameText.setError(this.getString(R.string.incorrect_tournament_name));
    }

    @Override
    public void dialogConfirmButtonClicked() {

    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", this.getString(R.string.please_wait));//todo
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
    public void addTournament() {
        presenter.addTournament(getTournamentData());
    }

    public TournamentDTO getTournamentData() {
        TournamentDTO tournamentData = new TournamentDTO();
        tournamentData.setName(nameText.getText().toString());
        tournamentData.setShortDescription(shortDescriptionText.getText().toString());
        tournamentData.setFullDescription(fullDescriptionText.getText().toString());
        if (manyPeopleInTeam) {
            tournamentData.setCountPlayersInTeam(3);
        } else {
            tournamentData.setCountPlayersInTeam(1);
        }
        tournamentData.setCountPlayersInTeam(1);
        tournamentData.setStartDate(startDate);
        tournamentData.setFinishDate(finishDate);
        return tournamentData;
    }

    @Override
    public void setRefereeSpinnerAdapter(List<String> refereeNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, refereeNames);
        refereeSpinner.setAdapter(adapter);
    }

    @Override
    public void setPlaceSpinnerAdapter(List<String> placesNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, placesNames);
        placeSpinner.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag(Constants.Companion.getDATE_PICKER_DIALOG());
        if (dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth,
                          int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        startDate = year + "-" + monthOfYear + "-" + dayOfMonth;
        finishDate = yearEnd + "-" + monthOfYearEnd + "-" + dayOfMonthEnd;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd,
                          int minuteEnd) {
        startDate += " " + hourOfDay + ":" + minute + ":00";
        finishDate += " " + hourOfDayEnd + ":" + minuteEnd + ":00";
    }

    public AddTournamentPresenter getPresenter() {
        return presenter;
    }

    public void setReferee(UserDTO referee) {
        this.referee = referee;
    }

    public class PlaceItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            AddTournamentActivity.this.getPresenter().setSelectedPlaceIndex(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }
    }

    public class RefereeItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            AddTournamentActivity.this.getPresenter().setSelectedRefereeIndex(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }
    }

    @Override
    public void setConnectionStatus(Integer connectivityStatus) {
        presenter.setConnectivityStatus(connectivityStatus);
    }
}