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
import bobrchess.of.by.belaruschess.presenter.AuthorizationPresenter;
import bobrchess.of.by.belaruschess.presenter.impl.AuthorizationPresenterImpl;
import bobrchess.of.by.belaruschess.view.activity.AuthorizationContractView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static bobrchess.of.by.belaruschess.util.Constants.EMPTY_STRING;
import static bobrchess.of.by.belaruschess.util.Constants.USER_NAME_PARAMETER;
import static bobrchess.of.by.belaruschess.util.Constants.USER_PATRONYMIC_PARAMETER;
import static bobrchess.of.by.belaruschess.util.Constants.USER_RATING_PARAMETER;
import static bobrchess.of.by.belaruschess.util.Constants.USER_SURNAME_PARAMETER;

public class AuthorizationActivity extends AppCompatActivity implements AuthorizationContractView {
    private static final String TAG = "AuthorizationActivity";
    private static final int AUTHORIZATION_REQUEST = 0;

    @BindView(R.id.e_email_input)
    EditText emailText;

    @BindView(R.id.e_password_input)
    EditText passwordText;

    @BindView(R.id.b_authorization)
    Button loginButton;

    @BindView(R.id.t_link_registration)
    TextView registrationLink;

    private ProgressDialog progressDialog;

    private AuthorizationPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        ButterKnife.bind(this);
        presenter = new AuthorizationPresenterImpl();
        presenter.attachView(this);
        presenter.viewIsReady();
        initButtonsListeners();
    }

    private void initButtonsListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(AuthorizationActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                presenter.authorization();
            }
        });


        registrationLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivityForResult(intent, AUTHORIZATION_REQUEST);
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
        if (requestCode == AUTHORIZATION_REQUEST) {
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
    public void onLoginSuccess(UserDTO userDTO) {
        enableButton();
        finish();
        Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
        putUserData(intent,userDTO);
        startActivityForResult(intent, AUTHORIZATION_REQUEST);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void putUserData(Intent intent, UserDTO userDTO) {
        intent.putExtra(USER_NAME_PARAMETER, userDTO.getName());
        intent.putExtra(USER_SURNAME_PARAMETER, userDTO.getSurname());
        intent.putExtra(USER_PATRONYMIC_PARAMETER, userDTO.getPatronymic());
        intent.putExtra(USER_RATING_PARAMETER, 1234);
    }

    @Override
    public void onLoginFailed() {
        enableButton();
    }

    @Override
    public void onConnectionError() {
        enableButton();
        showToast(R.string.connection_error);
    }

    @Override
    public void showIncorrectEmailText(){
        emailText.setError(this.getString(R.string.incorrect_email));
    }

    @Override
    public void showIncorrectPasswordText(){
        passwordText.setError(this.getString(R.string.incorrect_password));
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
        loginButton.setEnabled(true);
    }

    @Override
    public void disableButton() {
        loginButton.setEnabled(false);
    }

    @Override
    public UserDTO getUserData() {
        UserDTO userData = new UserDTO();
        userData.setEmail(emailText.getText().toString());
        userData.setPassword(passwordText.getText().toString());
        return userData;
    }
}
