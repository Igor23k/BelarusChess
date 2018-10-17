package bobrchess.of.by.belaruschess.view.activity.impl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;

import bobrchess.of.by.belaruschess.R;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.presenter.RegistrationPresenter;
import bobrchess.of.by.belaruschess.presenter.impl.RegistrationPresenterImpl;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.RegistrationContractView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static bobrchess.of.by.belaruschess.util.Constants.DATE_PICKER_DIALOG;
import static bobrchess.of.by.belaruschess.util.Constants.EMPTY_STRING;
import static bobrchess.of.by.belaruschess.util.Constants.USER_PARAMETER;
import static bobrchess.of.by.belaruschess.util.Util.REGISTRATION_REQUEST;
import static bobrchess.of.by.belaruschess.util.Util.TYPE_NOT_CONNECTED;
import static bobrchess.of.by.belaruschess.util.Util.getConnectivityStatus;

public class RegistrationActivity extends MvpAppCompatActivity implements RegistrationContractView, DatePickerDialog.OnDateSetListener {

    private static Snackbar snackbar;

    @InjectPresenter
    RegistrationPresenterImpl presenter;

    @BindView(R.id.tournament_name_input)
    EditText nameText;

    @BindView(R.id.e_surname_input)
    EditText surnameText;

    @BindView(R.id.e_patronymic_input)
    EditText patronymicText;

    @BindView(R.id.e_rating_input)
    EditText ratingText;

    @BindView(R.id.e_email_input)
    EditText emailText;

    /*@BindView(R.id.e_number_input)
    EditText mobileText;*/

    @BindView(R.id.e_password_input)
    EditText passwordText;

    @BindView(R.id.e_reEnterPassword_input)
    EditText reEnterPasswordText;

    @BindView(R.id.b_registration)
    Button registrationButton;

    @BindView(R.id.t_link_authorization)
    TextView authorizationLink;

    @BindView(R.id.calendar_birthday_image)
    ImageView calendarImage;

    private ProgressDialog progressDialog;
    private ScrollView view;
    private Spinner genderSpinner;
    private Spinner coachSpinner;
    private Spinner rankSpinner;
    private Spinner countrySpinner;

    private String birthday;

    private Integer connectivityStatus;

    // TODO https://demonuts.com/upload-image-from-gallery/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        presenter = new RegistrationPresenterImpl(this);
        view = findViewById(R.id.scrollViewRegistration);
        presenter.attachViewComponent(findViewById(R.id.scrollViewRegistration));
        initButtonsListeners();
        registerInternetCheckReceiver();
        genderSpinner = findViewById(R.id.genderSpinner);
        genderSpinner.setOnItemSelectedListener(new GenderItemSelectedListener());
        setGenderSpinnerAdapter(Util.getGenders());
        coachSpinner = findViewById(R.id.coachSpinner);
        coachSpinner.setOnItemSelectedListener(new CoachItemSelectedListener());
        rankSpinner = findViewById(R.id.rankSpinner);
        rankSpinner.setOnItemSelectedListener(new RankItemSelectedListener());
        countrySpinner = findViewById(R.id.countrySpinner);
        countrySpinner.setOnItemSelectedListener(new CountryItemSelectedListener());
    }

    private void initButtonsListeners() {
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                registration();
            }
        });


        authorizationLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AuthorizationActivity.class);
                startActivityForResult(intent, REGISTRATION_REQUEST);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        calendarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        RegistrationActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), DATE_PICKER_DIALOG);
            }
        });

    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REGISTRATION_REQUEST) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void startActivity(@NonNull UserDTO userDTO) {
        Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
        putUserData(intent, userDTO);
        startActivityForResult(intent, REGISTRATION_REQUEST);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void putUserData(Intent intent, @NonNull UserDTO userDTO) {
        intent.putExtra(USER_PARAMETER, userDTO);
    }

    @Override
    public void showToast(@StringRes @NonNull Integer resId) {
        Toast toast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showToast(@NonNull String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showIncorrectEmailText() {
        emailText.setError(this.getString(R.string.incorrect_email));
    }

    @Override
    public void showIncorrectPasswordText() {
        passwordText.setError(this.getString(R.string.incorrect_password));
    }

    @Override
    public void registration() {
        try {
            presenter.registration(getUserData());
        } catch (NumberFormatException e) {
            showToast(R.string.incorrect_rating);
        }
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
        registrationButton.setEnabled(true);
    }

    @Override
    public void disableButton() {
        registrationButton.setEnabled(false);
    }

    public UserDTO getUserData() throws NumberFormatException {
        UserDTO userData = new UserDTO();
        userData.setName(nameText.getText().toString());
        userData.setSurname(surnameText.getText().toString());
        userData.setPatronymic(patronymicText.getText().toString());
        userData.setEmail(emailText.getText().toString());
        userData.setPhoneNumber("29292834");
        userData.setPassword(passwordText.getText().toString());
        userData.setRating(Integer.parseInt(ratingText.getText().toString()));
        userData.setBirthday(birthday);
        //  userData.setImage(getUploadedImage());
        return userData;
    }


    @Override
    public void setGenderSpinnerAdapter(@NonNull List<String> genders) {
        genders.add(0, getString(R.string.notSelected));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, genders);
        genderSpinner.setAdapter(adapter);
    }

    @Override
    public void setCoachSpinnerAdapter(@NonNull List<String> coachesNames) {
        coachesNames.add(0, getString(R.string.notSelected));
        coachesNames.add(1, getString(R.string.absence));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, coachesNames);
        coachSpinner.setAdapter(adapter);
    }

    @Override
    public void setRankSpinnerAdapter(@NonNull List<String> ranksNames) {
        ranksNames.add(0, getString(R.string.notSelected));
        ranksNames.add(1, getString(R.string.absence));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, ranksNames);
        rankSpinner.setAdapter(adapter);
    }

    @Override
    public void setCountrySpinnerAdapter(@NonNull List<String> countriesNames) {
        countriesNames.add(0, getString(R.string.notSelected));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, countriesNames);
        countrySpinner.setAdapter(adapter);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth,
                          int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        birthday = year + "-" + monthOfYear + "-" + dayOfMonth;
    }

    public class GenderItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            RegistrationActivity.this.getPresenter().setSelectedGenderIndex(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }
    }

    public class CoachItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            RegistrationActivity.this.getPresenter().setSelectedCoachIndex(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }
    }

    public class RankItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            RegistrationActivity.this.getPresenter().setSelectedRankIndex(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }
    }

    public class CountryItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            RegistrationActivity.this.getPresenter().setSelectedCountryIndex(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }
    }

    public RegistrationPresenter getPresenter() {
        return presenter;
    }

    public String getBirthday() {
        return birthday;
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            connectivityStatus = getConnectivityStatus(context);
            presenter.setConnectivityStatus(connectivityStatus);
        }
    };

    private void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, internetFilter);//bug unregister тоже где то надо делать
    }

    @Override
    public void showNoConnectionAlertDialog(@StringRes @NonNull Integer title, @StringRes @NonNull Integer message, @StringRes @NonNull Integer buttonText, @NonNull Boolean cancelable) {
        final AlertDialog builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(buttonText, null)
                //  Log.d("TAG", "Show Dialog: " + "Message");// bug оформить норм
                .create();
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (connectivityStatus != TYPE_NOT_CONNECTED) {
                            dialog.dismiss();
                            loadSpinnersData();
                        }
                    }
                });
            }
        });
        builder.show();
    }

    @Override
    public void showServerInUnavailableAlertDialog(@StringRes @NonNull Integer title, @StringRes @NonNull Integer message, @StringRes @NonNull Integer buttonText, @NonNull Boolean cancelable) {
        final AlertDialog builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(buttonText, null)
                //  Log.d("TAG", "Show Dialog: " + "Message");// bug оформить норм
                .create();
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        loadSpinnersData();
                    }
                });
            }
        });
        builder.show();
    }

    @Override
    public void showSnackBar(@NonNull View componentView, @StringRes @NonNull Integer message, @StringRes @NonNull Integer buttonText) {
        snackbar = Snackbar
                .make(componentView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(buttonText, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        registration();
                    }
                });
        snackbar.setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    public void showSnackBar(@NonNull View componentView, @NonNull String message, @StringRes @NonNull Integer buttonText) {
        snackbar = Snackbar
                .make(componentView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(buttonText, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        registration();
                    }
                });
        snackbar.setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    public void loadSpinnersData() {
        presenter.loadSpinnersData();
    }
}