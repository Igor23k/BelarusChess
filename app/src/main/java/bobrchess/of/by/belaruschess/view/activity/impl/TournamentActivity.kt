package bobrchess.of.by.belaruschess.view.activity.impl

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.presenter.impl.TournamentPresenterImpl
import bobrchess.of.by.belaruschess.util.Constants.TOURNAMENT_PARAMETER
import bobrchess.of.by.belaruschess.util.Constants.USER_PARAMETER
import bobrchess.of.by.belaruschess.view.activity.TournamentContractView
import bobrchess.of.by.belaruschess.view.activity.TournamentPresenter
import bobrchess.of.by.colibritweet.adapter.TournamentsAdapter
import bobrchess.of.by.colibritweet.pojo.UserTweet
import com.squareup.picasso.Picasso

/**
 * Created by Igor on 25.03.2018.
 */
class TournamentActivity : AppCompatActivity(), TournamentContractView {

    private var tournamentImageView: ImageView? = null
    private var nameTextView: TextView? = null
    private var descriptionTextView: TextView? = null
    private var judgeTextView: TextView? = null
    private var locationTextView: TextView? = null
    private var toolbar: Toolbar? = null

    private var tournament = TournamentDTO()

    private var progressDialog: ProgressDialog? = null

    private var presenter: TournamentPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament)
        tournamentImageView = findViewById(R.id.tournament_image_view)
        nameTextView = findViewById(R.id.tournament_name_text_view)
        descriptionTextView = findViewById(R.id.tournament_full_description_text_view)
        judgeTextView = findViewById(R.id.judge_text_view)
        locationTextView = findViewById(R.id.tournament_location_text_view)

        toolbar = findViewById(R.id.toolbar)
        presenter = TournamentPresenterImpl()
        presenter!!.attachView(this)
        presenter!!.viewIsReady()
        setSupportActionBar(toolbar)
        loadTournamentData()
    }

    private fun loadTournamentData() {
        tournament = getTournamentData(intent)
        displayTournamentData()
    }

    private fun displayTournamentData() {
        Picasso.with(this).load("https://www.w3schools.com/w3css/img_fjords.jpg").into(tournamentImageView)
        nameTextView!!.text = tournament.name
        descriptionTextView!!.text = tournament.fullDescription
        judgeTextView!!.text = tournament.referee!!.name + " " + tournament.referee!!.surname
        locationTextView!!.text = tournament.place!!.country!!.name + ", " + tournament!!.place!!.city + ", " + tournament!!.place!!.street + ", " + tournament!!.place!!.building
    }

    private fun getTournamentData(intent: Intent?): TournamentDTO {
        return intent!!.getSerializableExtra(TOURNAMENT_PARAMETER) as TournamentDTO
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_info_menu, menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enableButton() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun disableButton() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTournaments() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showToast(resId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}