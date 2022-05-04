package bobrchess.of.by.belaruschess.view.activity.impl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import bobrchess.of.by.belaruschess.R;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.presenter.impl.AuthorizationPresenterImpl;
import bobrchess.of.by.belaruschess.presenter.impl.TokenAuthPresenterImpl;
import bobrchess.of.by.belaruschess.util.Constants;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.AuthorizationContractView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthorizationActivity extends AbstractActivity implements AuthorizationContractView {

    private static Snackbar snackbar;

    @InjectPresenter
    AuthorizationPresenterImpl presenter;

    @InjectPresenter
    TokenAuthPresenterImpl tokenAuthPresenter;

    @BindView(R.id.e_email_input)
    EditText emailText;

    @BindView(R.id.e_password_input)
    EditText passwordText;

    @BindView(R.id.b_authorization)
    Button loginButton;

    @BindView(R.id.t_link_registration)
    TextView registrationLink;

    @BindView(R.id.t_link_password_reset)
    TextView passwordResetLink;

    private ProgressDialog progressDialog;
    private boolean tokenAuthFailed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setPackageModel(getPackageModel());
        tokenAuthPresenter.setPackageModel(getPackageModel());

        registerInternetCheckReceiver();

        if (!tokenAuthPresenter.isAuthenticated()) {//тут можно отключить авторизацию токеном
            tokenAuthPresenter.tokenAuthorization();
        } else {
            initActivityData();
        }
    }

    public void unsuccessfulTokenAuth() {
        tokenAuthFailed = true;
        initActivityData();
    }

    @Override
    public void initActivityData() {
        setContentView(R.layout.activity_authorization);
        ButterKnife.bind(this);
        presenter.attachViewComponent(findViewById(R.id.scrollViewAuthorization));
        initButtonsListeners();
    }

    private void initButtonsListeners() {
        loginButton.setOnClickListener(v -> {
            final ProgressDialog progressDialog = new ProgressDialog(AuthorizationActivity.this,
                    R.style.AppTheme_NoActionBar);
            progressDialog.setIndeterminate(true);
            authorization();
        });

        registrationLink.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivityForResult(intent, Util.Companion.getAUTHORIZATION_REQUEST());
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        });

        passwordResetLink.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PasswordResetActivity.class);
            startActivityForResult(intent, Util.Companion.getAUTHORIZATION_REQUEST());
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        });
    }

    @Override
    public void dialogConfirmButtonClicked() {
        if (presenter.isConnected(getConnectivityStatus())) {
            dismissAlertDialog();
            if (!tokenAuthFailed) {
                tokenAuthPresenter.tokenAuthorization();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Util.Companion.getAUTHORIZATION_REQUEST()) {
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Util.Companion.setUserImage(userDTO.getImage());
        userDTO.setImage(null);
        putUserData(intent, userDTO);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void putUserData(Intent intent, @NonNull UserDTO userDTO) {
        intent.putExtra(Constants.Companion.getUSER_PARAMETER(), userDTO);
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
    public void showProgress() {
        progressDialog = ProgressDialog.show(this, Constants.Companion.getEMPTY_STRING(), this.getString(R.string.please_wait));
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

    public UserDTO getUserData() {
        UserDTO userData = new UserDTO();
        userData.setEmail(emailText.getText().toString());
        userData.setPassword(passwordText.getText().toString());
        return userData;
    }

    @Override
    public void showSnackBar(@NonNull View componentView, @StringRes @NonNull Integer message, @StringRes @NonNull Integer buttonText) {
        snackbar = Snackbar
                .make(componentView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(buttonText, view -> {
                    snackbar.dismiss();
                    if (!tokenAuthFailed) {
                        tokenAuthPresenter.tokenAuthorization();
                    } else {
                        authorization();
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
                .make(componentView, message, Snackbar.LENGTH_LONG)
                .setAction(buttonText, view -> {
                    snackbar.dismiss();
                    if (!tokenAuthFailed) {
                        tokenAuthPresenter.tokenAuthorization();
                    } else {
                        authorization();
                    }
                });
        snackbar.setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    public void showSnackBar(@NonNull View componentView, @StringRes int message) {
        snackbar = Snackbar
                .make(componentView, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    public void showSnackBar(@NonNull View componentView, @NonNull String message) {
        snackbar = Snackbar
                .make(componentView, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    public void authorization() {
        try {
            presenter.authorization(getUserData());
        } catch (Exception e) {
            showToast(R.string.incorrect_data);
            enableButton();
        }
    }

    @Override
    public void setConnectionStatus(Integer connectivityStatus) {
        presenter.setConnectivityStatus(connectivityStatus);
        tokenAuthPresenter.setConnectivityStatus(connectivityStatus);
    }

    @Override
    public void tokenAuthorization() {
        if (!tokenAuthFailed) {
            tokenAuthPresenter.tokenAuthorization();
        } else {
            authorization();
        }
    }
}
