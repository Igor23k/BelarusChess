package bobrchess.of.by.belaruschess.view.activity.impl;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;

import java.util.Calendar;

import bobrchess.of.by.belaruschess.R;
import bobrchess.of.by.belaruschess.dto.CountryDTO;
import bobrchess.of.by.belaruschess.dto.PlaceDTO;
import bobrchess.of.by.belaruschess.dto.TournamentDTO;
import bobrchess.of.by.belaruschess.presenter.AddTournamentPresenter;
import bobrchess.of.by.belaruschess.presenter.impl.AddTournamentPresenterImpl;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.AddTournamentContractView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static bobrchess.of.by.belaruschess.util.Constants.EMPTY_STRING;
import static bobrchess.of.by.belaruschess.util.Constants.TOURNAMENT_PARAMETER;

public class AddTournamentActivity extends AppCompatActivity implements AddTournamentContractView, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "AddTournamentActivity";
    private static final int ADD_TOURNAMENT_REQUEST = 1;

    private boolean mAutoHighlight;
    private boolean manyPeopleInTeam;

    @BindView(R.id.tournament_name_input)
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

    //Убрать это
    String yearString;
    String monthStart;
    String dayStart;
    String hourStart;
    String yearFinish;
    String monthFinish;
    String dayFinish;
    String hourFinish;

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


        //проверить зачем тот чекбокс, что делает та булева переменная


        //Date and time


        // Find our View instances
        /*dateTextView = (TextView)findViewById(R.id.date_textview);
        timeTextView = (TextView)findViewById(R.id.time_textview);*/

        CheckBox team_type = findViewById(R.id.team_type_checkbox);
        team_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               manyPeopleInTeam = b;
            }
        });
        mAutoHighlight = true;
        // Show a datepicker when the dateButton is clicked
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
                dpd.setAutoHighlight(mAutoHighlight);
                dpd.show(getFragmentManager(), "Datepickerdialog");
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
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });
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
        Intent intent = new Intent(getApplicationContext(), TournamentInfoActivity.class);
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
        if(manyPeopleInTeam){
            tournamentData.setCountPlayersInTeam(3);
        }else {
            tournamentData.setCountPlayersInTeam(1);
        }
        tournamentData.setReferee(Util.getTestUser());
        PlaceDTO placeDTO = new PlaceDTO();
        placeDTO.setId(1);
        placeDTO.setBuilding("fef");
        placeDTO.setCapacity(23);
        placeDTO.setCity("fef");
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setName("vrg");
        countryDTO.setAbbreviation("");
        placeDTO.setCountry(countryDTO);
        placeDTO.setName("");
        placeDTO.setStreet("");
        tournamentData.setPlace(placeDTO);
        return tournamentData;
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String date = "You picked the following date: From- " + dayOfMonth + "/" + (++monthOfYear) + "/" + year + " To " + dayOfMonthEnd + "/" + (++monthOfYearEnd) + "/" + yearEnd;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String hourStringEnd = hourOfDayEnd < 10 ? "0" + hourOfDayEnd : "" + hourOfDayEnd;
        String minuteStringEnd = minuteEnd < 10 ? "0" + minuteEnd : "" + minuteEnd;
        String time = "You picked the following time: From - " + hourString + "h" + minuteString + " To - " + hourStringEnd + "h" + minuteStringEnd;
    }
}