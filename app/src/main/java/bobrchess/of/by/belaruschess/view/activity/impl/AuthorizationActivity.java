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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
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

public class AuthorizationActivity extends MvpAppCompatActivity implements AuthorizationContractView {

    private static Snackbar snackbar;

    private boolean tokenAuthAttempt = true;

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

    private ScrollView view;
    private Integer connectivityStatus;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setPackageModel(new PackageModel(this));
        tokenAuthPresenter.setPackageModel(new PackageModel(this));
        registerInternetCheckReceiver();

        if (tokenAuthPresenter.isAuthenticated()) {
            tokenAuthPresenter.tokenAuthorization();
        } else {
            initActivityData();
        }
    }

    public void unsuccessfulTokenAuth(){
        initActivityData();
    }

    public void initActivityData(){
        setContentView(R.layout.activity_authorization);
        ButterKnife.bind(this);
        presenter.attachViewComponent(findViewById(R.id.scrollViewAuthorization));
        initButtonsListeners();
    }

    public boolean isTokenAuthAttempt() {
        return tokenAuthAttempt;
    }

    public void setTokenAuthAttempt(boolean tokenAuthAttempt) {
        this.tokenAuthAttempt = tokenAuthAttempt;
    }

    public void tokenAuthCompleted(boolean successfullAuth){
        System.out.println();
    }

    private void initButtonsListeners() {
        loginButton.setOnClickListener(v -> {
            final ProgressDialog progressDialog = new ProgressDialog(AuthorizationActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            authorization();
        });


        registrationLink.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivityForResult(intent, Util.Companion.getAUTHORIZATION_REQUEST());
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        });
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            connectivityStatus = Util.Companion.getConnectivityStatus(context);
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
        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            b.setOnClickListener(v -> {
                if (connectivityStatus != Util.Companion.getTYPE_NOT_CONNECTED()) {
                    dialog.dismiss();
                }
            });
        });
        builder.show();
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
        Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
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
                .setAction(buttonText, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
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
                .setAction(buttonText, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
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
    public void authorization() {
        try {
            presenter.authorization(getUserData());
        } catch (NumberFormatException e) {
            showToast(R.string.incorrect_rating);
        }
    }

}
