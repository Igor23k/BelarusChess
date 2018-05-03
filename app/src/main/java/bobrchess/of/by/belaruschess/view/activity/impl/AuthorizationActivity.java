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

public class AuthorizationActivity extends AppCompatActivity implements AuthorizationContractView {
    private static final String TAG = "AuthorizationActivity";
    private static final int AUTHORIZATION_REQUEST = 0;

    @BindView(R.id.input_email)
    EditText emailText;

    @BindView(R.id.input_password)
    EditText passwordText;

    @BindView(R.id.btn_login)
    Button loginButton;

    @BindView(R.id.link_signup)
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
                presenter.authorizate();
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

    public void onLoginSuccess() {
        enableButton();
        finish();
        Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
        startActivityForResult(intent, AUTHORIZATION_REQUEST);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void onLoginFailed() {
        enableButton();
    }

    public void onConnectionError() {
        enableButton();
        showToast(R.string.connection_error);
    }

    public void showIncorrectEmailText(){
        emailText.setError("enter a valid email address");
    }

    public void showIncorrectPasswordText(){
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
        loginButton.setEnabled(true);
    }

    @Override
    public void disableButton() {
        loginButton.setEnabled(false);
    }

    public UserDTO getUserData() {
        UserDTO userData = new UserDTO();
        userData.setEmail(emailText.getText().toString());
        userData.setPassword(passwordText.getText().toString());
        return userData;
    }
}
