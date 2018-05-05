package bobrchess.of.by.belaruschess.view.activity.impl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import bobrchess.of.by.belaruschess.R;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.presenter.RegistrationPresenter;
import bobrchess.of.by.belaruschess.presenter.impl.RegistrationPresenterImpl;
import bobrchess.of.by.belaruschess.view.activity.AuthContractView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationActivity extends AppCompatActivity implements AuthContractView {
    private static final String TAG = "RegistrationActivity";
    private static final int REGISTRATION_REQUEST = 1;

    @BindView(R.id.e_name_input)
    EditText nameText;

    @BindView(R.id.e_surname_input)
    EditText surnameText;

    @BindView(R.id.e_patronymic_input)
    EditText patronymicText;

    @BindView(R.id.e_rating_input)
    EditText ratingText;

    @BindView(R.id.e_email_input)
    EditText emailText;

    @BindView(R.id.e_number_input)
    EditText mobileText;

    @BindView(R.id.e_password_input)
    EditText passwordText;

    @BindView(R.id.e_reEnterPassword_input)
    EditText reEnterPasswordText;

    @BindView(R.id.b_registration)
    Button registrationButton;

    @BindView(R.id.t_link_authorization)
    TextView authorizationLink;

    private ProgressDialog progressDialog;

    private RegistrationPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        presenter = new RegistrationPresenterImpl();
        presenter.attachView(this);
        presenter.viewIsReady();
        initButtonsListeners();
    }

    private void initButtonsListeners() {
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                presenter.registrate();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REGISTRATION_REQUEST) {
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

    public void onLoginSuccess() {
        enableButton();
        finish();
        Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
        startActivityForResult(intent, REGISTRATION_REQUEST);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void onLoginFailed() {
        registrationButton.setEnabled(true);
    }

    public void showIncorrectEmailText() {
        emailText.setError("enter a valid email address");
    }

    public void showIncorrectPasswordText() {
        passwordText.setError("between 4 and 10 alphanumeric characters");
    }

    public void showToast(Integer resId) {
        Toast toast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", "Please, wait ..."/*R.string.please_wait*/);
    }

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

    public UserDTO getUserData() {
        UserDTO userData = new UserDTO();
        userData.setName(nameText.getText().toString());
        userData.setSurname(surnameText.getText().toString());
        userData.setPatronymic(patronymicText.getText().toString());
        userData.setEmail(emailText.getText().toString());
        userData.setPassword(passwordText.getText().toString());
        userData.setRating(Integer.valueOf(ratingText.getText().toString()));
        return userData;
    }
}