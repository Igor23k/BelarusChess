package bobrchess.of.by.belaruschess.view.activity.impl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
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
import bobrchess.of.by.belaruschess.view.activity.AddTournamentContractView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static bobrchess.of.by.belaruschess.util.Constants.DATE_PICKER_DIALOG;
import static bobrchess.of.by.belaruschess.util.Constants.EMPTY_STRING;
import static bobrchess.of.by.belaruschess.util.Constants.TIME_PICKER_DIALOG;
import static bobrchess.of.by.belaruschess.util.Constants.TOURNAMENT_PARAMETER;
import static bobrchess.of.by.belaruschess.util.Util.ADD_TOURNAMENT_REQUEST;

public class AddTournamentActivity extends MvpAppCompatActivity implements AddTournamentContractView, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @InjectPresenter
    AddTournamentPresenterImpl presenter;

    private boolean manyPeopleInTeam;

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

    //добавить place и referee

    @BindView(R.id.b_registration)
    Button addTournamentButton;

    private ProgressDialog progressDialog;

    private PlaceDTO place;
    private UserDTO referee;
    private String startDate;
    private String finishDate;
    private Spinner refereeSpinner;
    private Spinner placeSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tournament);
        ButterKnife.bind(this);

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
        team_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                manyPeopleInTeam = b;
            }
        });


        calendarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        AddTournamentActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), DATE_PICKER_DIALOG);
            }
        });

        clockImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        AddTournamentActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.show(getFragmentManager(), TIME_PICKER_DIALOG);
            }
        });
        presenter.viewIsReady();
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
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_TOURNAMENT_REQUEST) {
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
        intent.putExtra(TOURNAMENT_PARAMETER, tournamentDTO);
    }

    @Override
    public void showIncorrectTournamentNameText() {
        nameText.setError(this.getString(R.string.incorrect_tournament_name));
    }

    @Override
    public void showToast(Integer resId) {
        Toast toast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(this, EMPTY_STRING, this.getString(R.string.please_wait));
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
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag(DATE_PICKER_DIALOG);
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
}