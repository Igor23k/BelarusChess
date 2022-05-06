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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import bobrchess.of.by.belaruschess.R;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.presenter.impl.PasswordResetPresenterImpl;
import bobrchess.of.by.belaruschess.util.Util;
import bobrchess.of.by.belaruschess.view.activity.PasswordResetContractView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PasswordResetActivity extends AbstractActivity implements PasswordResetContractView {

    private static Snackbar snackbar;

    @InjectPresenter
    PasswordResetPresenterImpl presenter;

    @BindView(R.id.e_email_input)
    EditText emailText;

    @BindView(R.id.b_reset)
    Button resetButton;

    @BindView(R.id.t_link_authorization)
    TextView authorizationLink;

    private ProgressDialog progressDialog;
    private boolean tokenAuthFailed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setPackageModel(getPackageModel());
        initComponents();
        registerInternetCheckReceiver();
        initActivityData();
    }

    public void initActivityData() {
        setContentView(R.layout.activity_password_reset);
        ButterKnife.bind(this);
        presenter.attachViewComponent(findViewById(R.id.scrollViewPasswordReset));
        initButtonsListeners();
    }

    private void initButtonsListeners() {
        resetButton.setOnClickListener(v -> {
            final ProgressDialog progressDialog = new ProgressDialog(PasswordResetActivity.this,
                    R.style.AppTheme_NoActionBar);
            progressDialog.setIndeterminate(true);
            reset();
        });

        authorizationLink.setOnClickListener (v -> {
            Intent intent = new Intent(getApplicationContext(), AuthorizationActivity.class);
            startActivityForResult(intent, Util.Companion.getAUTHORIZATION_REQUEST());
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        });
    }

    private void initComponents() {
        emailText = findViewById(R.id.e_email_input);
        authorizationLink = findViewById(R.id.t_link_authorization);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    public String getUserEmail() {
        return emailText.getText().toString();
    }

    public void reset() {
        try {
            presenter.reset(getUserEmail());
        } catch (Exception e) {
            showToast(R.string.incorrect_data);
            enableButton();
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setConnectionStatus(Integer connectivityStatus) {

    }

    @Override
    public void enableButton() {

    }

    @Override
    public void disableButton() {

    }

    @Override
    public void unsuccessfulTokenAuth() {

    }

    @Override
    public void showIncorrectEmailText() {
        emailText.setError(this.getString(R.string.incorrect_email));
    }

    @Override
    public void showIncorrectPasswordText() {

    }

    @Override
    public void tokenAuthorization() {

    }

    @Override
    public void dialogConfirmButtonClicked() {

    }

    @Override
    public void startActivity(@NotNull UserDTO userDTO) {

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
    public void showSnackBar(@NotNull View componentView, @NotNull String message, @Nullable Integer buttonText) {

    }

    @Override
    public void showSnackBar(@NotNull View componentView, @Nullable Integer message, @Nullable Integer buttonText) {

    }
}
