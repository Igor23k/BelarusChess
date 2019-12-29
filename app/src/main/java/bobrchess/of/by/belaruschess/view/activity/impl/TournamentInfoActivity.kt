package bobrchess.of.by.belaruschess.view.activity.impl

import android.animation.ValueAnimator
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.presenter.TournamentPresenter
import bobrchess.of.by.belaruschess.presenter.impl.TournamentPresenterImpl
import bobrchess.of.by.belaruschess.util.Constants.Companion.EMPTY_STRING
import bobrchess.of.by.belaruschess.util.Constants.Companion.REQUEST_CODE
import bobrchess.of.by.belaruschess.util.Constants.Companion.TOURNAMENT_PARAMETER
import bobrchess.of.by.belaruschess.util.Util.Companion.TOURNAMENT_PARTICIPANTS_REQUEST
import bobrchess.of.by.belaruschess.util.Util.Companion.TOURNAMENT_TABLE_REQUEST
import bobrchess.of.by.belaruschess.view.activity.TournamentContractView
import com.dd.CircularProgressButton
import com.squareup.picasso.Picasso
import java.util.*

class TournamentInfoActivity : AbstractActivity(), TournamentContractView {

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
        setContentView(R.layout.activity_tournament_info)
        registerInternetCheckReceiver()
        tournamentImageView = findViewById(R.id.tournament_image_view)
        nameTextView = findViewById(R.id.tournament_name_text_view)
        descriptionTextView = findViewById(R.id.tournament_full_description_text_view)
        judgeTextView = findViewById(R.id.judge_text_view)
        locationTextView = findViewById(R.id.tournament_location_text_view)

        toolbar = findViewById(R.id.toolbar)

        //   supportActionBar!!.setDisplayHomeAsUpEnabled(false)//todo кнопка назад, хз какой способ лучше, проверить

        presenter = TournamentPresenterImpl()
        //presenter!!.attachView(this)

        setSupportActionBar(toolbar)
        presenter!!.viewIsReady()
        val actionBar = actionBar

        actionBar?.title = "50"


        val circularButton1 = findViewById<View>(R.id.circularButton1) as CircularProgressButton
        circularButton1.text = "Зарегистрироваться"
        circularButton1.setOnClickListener {
            if (circularButton1.progress == 0) {
                circularButton1.text = ""
                simulateSuccessProgress(circularButton1)
            } else {
                circularButton1.progress = 0
            }
        }
        loadTournamentData()
    }

    private fun simulateSuccessProgress(button: CircularProgressButton) {
        var value = 0
        val widthAnimation = ValueAnimator.ofInt(1, 100)
        widthAnimation.duration = 1500
        widthAnimation.interpolator = AccelerateDecelerateInterpolator()
        widthAnimation.addUpdateListener { animation ->
            value = animation.animatedValue as Int
            button.progress = value

        }
        widthAnimation.start()
        android.os.Handler().postDelayed(
                {
                    button.text = "Вы зарегистрированы!"
                }, 2000)
    }

    private fun simulateErrorProgress(button: CircularProgressButton) {
        val widthAnimation = ValueAnimator.ofInt(1, 99)
        widthAnimation.duration = 1500
        widthAnimation.interpolator = AccelerateDecelerateInterpolator()
        widthAnimation.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            button.progress = value
            if (value == 99) {
                button.progress = -1
            }
        }
        widthAnimation.start()
    }

    private fun loadTournamentData() {
        tournament = getTournamentData(intent)// todo мб проверку?
        displayTournamentData()
    }

    private fun displayTournamentData() {
        Picasso.with(this).load(tournament.image).into(tournamentImageView)
        nameTextView!!.text = tournament.name
        descriptionTextView!!.text = tournament.fullDescription
        // judgeTextView!!.text = tournament.referee!!.gameRecord + " " + tournament.referee!!.shortDescription//проверку и локэйшн тоже
        // locationTextView!!.text = tournament.place!!.country!!.gameRecord + ", " + tournament.place!!.city + ", " + tournament.place!!.street + ", " + tournament.place!!.building
    }

    private fun getTournamentData(intent: Intent?): TournamentDTO {
        return intent!!.getSerializableExtra(TOURNAMENT_PARAMETER) as TournamentDTO
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_info_menu, menu)
        menuInflater.inflate(R.menu.menu_tournament, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_games -> {
                val intent = Intent(this, GamesListActivity::class.java)
                startActivity(intent)
            }
            R.id.action_tournaments_search -> {
                val intent = Intent(this, SearchTournamentActivity::class.java)
                startActivity(intent)
            }
            R.id.action_table -> {
                val intent = Intent(this, SearchUserActivity::class.java)
                intent.putExtra(REQUEST_CODE, TOURNAMENT_TABLE_REQUEST)
                startActivity(intent)
            }
            R.id.action_participants -> {
                val intent = Intent(this, SearchUserActivity::class.java)
                intent.putExtra(REQUEST_CODE, TOURNAMENT_PARTICIPANTS_REQUEST)
                startActivity(intent)
            }
            /*R.id.action_tours -> {
                val intent = Intent(this, TournamentActivity::class.java)
                putTournamentData(intent, tournament)
                startActivity(intent)
            }*/
            R.id.action_info -> {
                val intent = Intent(this, TournamentInfoActivity::class.java)
                putTournamentData(intent, tournament)
                startActivity(intent)
            }
            R.id.action_draw -> {
                //val intent = Intent(this, TournamentInfoActivity::class.java)
                //putTournamentData(intent, userDTO)
                // startActivityForResult(intent, AUTHORIZATION_REQUEST)
                //startActivity(intent)
                showToast(R.string.successful_draw)
            }
        }
        return true
    }

    private fun saveTournamentData() {
        tournament = getTournamentData(intent)//мб проверку?
    }

    private fun putTournamentData(intent: Intent, tournamentDTO: TournamentDTO) {
        intent.putExtra(TOURNAMENT_PARAMETER, tournamentDTO)
    }

    fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) + start


    override fun showProgress() {
        progressDialog = ProgressDialog.show(this, EMPTY_STRING, this.getString(R.string.please_wait))
    }

    override fun hideProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }

    override fun dialogConfirmButtonClicked() {

    }

    override fun setConnectionStatus(connectivityStatus: Int?) {
        presenter?.setConnectivityStatus(connectivityStatus)
    }
}