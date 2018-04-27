package bobrchess.of.by.belaruschess.view.activity.impl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import bobrchess.of.by.belaruschess.R;
import bobrchess.of.by.belaruschess.dto.UserDTO;
import bobrchess.of.by.belaruschess.presenter.AuthorizationPresenter;
import bobrchess.of.by.belaruschess.presenter.impl.AuthorizationPresenterImpl;
import bobrchess.of.by.belaruschess.presenter.impl.TournamentPresenterImpl;
import bobrchess.of.by.belaruschess.view.activity.AuthorizationContractView;
import bobrchess.of.by.belaruschess.view.activity.TournamentContractView;
import bobrchess.of.by.belaruschess.view.activity.TournamentPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TournamentActivity extends AppCompatActivity implements TournamentContractView {
    private static final String TAG = "TournamentActivity";

    private ProgressDialog progressDialog;

    private TournamentPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        ButterKnife.bind(this);
        presenter = new TournamentPresenterImpl();
        presenter.attachView(this);
        presenter.viewIsReady();
        initButtonsListeners();
    }

    private void initButtonsListeners() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void disableButton() {

    }

    @Override
    public void getTournaments() {
        presenter.getTournaments();
    }
}
