package bobrchess.of.by.belaruschess.view.activity.impl

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.presenter.UserInfoPresenter
import bobrchess.of.by.belaruschess.presenter.impl.UserInfoPresenterImpl
import bobrchess.of.by.belaruschess.util.Constants.*
import bobrchess.of.by.belaruschess.util.Util.USER_INFO
import bobrchess.of.by.belaruschess.view.activity.UserInfoContractView
import bobrchess.of.by.colibritweet.adapter.TournamentsAdapter
import com.squareup.picasso.Picasso

/**
 * Created by Igor on 25.03.2018.
 */
class UserInfoActivity : AppCompatActivity(), UserInfoContractView {

    private var userImageView: ImageView? = null
    private var nameTextView: TextView? = null
    private var surnameTextView: TextView? = null
    private var statusTextView: TextView? = null
    private var locationTextView: TextView? = null
    private var ratingTextView: TextView? = null
    private var friendsCountTextView: TextView? = null
    private var coachNameTextView: TextView? = null
    private var tournamentsRecyclerView: RecyclerView? = null
    private var tournamentsAdapter: TournamentsAdapter? = null
    private var toolbar: Toolbar? = null

    private var user = UserDTO()

    private var progressDialog: ProgressDialog? = null

    private var presenter: UserInfoPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        userImageView = findViewById(R.id.user_image_view)
        nameTextView = findViewById(R.id.user_name_text_view)
        surnameTextView = findViewById(R.id.user_surname_text_view)
        statusTextView = findViewById(R.id.user_status_text_view)
        locationTextView = findViewById(R.id.user_location_text_view)
        ratingTextView = findViewById(R.id.user_rank_and_rating_text_view)
        friendsCountTextView = findViewById(R.id.participations_text_view)
        coachNameTextView = findViewById(R.id.coach_name_text_view)

        toolbar = findViewById(R.id.toolbar)
        presenter = UserInfoPresenterImpl()
        presenter!!.attachView(this)

        setSupportActionBar(toolbar)

        initRecyclerView()
        presenter!!.viewIsReady()
        loadUserInfo()
        loadUserTournaments()
    }

    private fun loadUserTournaments() {
        presenter!!.loadUserTournaments()
    }

    override fun displayUserTournaments(tournaments: List<TournamentDTO>) {
        tournamentsAdapter!!.setItems(tournaments)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_info_menu, menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
        /*R.id.action_settings -> {
        }*/
            R.id.action_tournaments_search -> {
                val intent = Intent(this, SearchTournamentActivity::class.java)
                startActivity(intent)
            }
            R.id.action_add_tournament -> {
                val intent = Intent(this, AddTournamentActivity::class.java)
                startActivity(intent)
            }
            R.id.action_search -> {
                val intent = Intent(this, SearchUserActivity::class.java)
                intent.putExtra("requestCode", USER_INFO)
                startActivity(intent)
            }
        }
        return true
    }

    private fun initRecyclerView() {
        tournamentsRecyclerView = findViewById(R.id.user_tournaments_recycler_view)
        tournamentsRecyclerView!!.layoutManager = LinearLayoutManager(this)
        val onTournamentClickListener = object : TournamentsAdapter.OnTournamentClickListener {
            override fun onTournamentClick(tournament: TournamentDTO) {
                val intent = Intent(this@UserInfoActivity, TournamentInfoActivity::class.java)
                intent.putExtra(TOURNAMENT_PARAMETER, tournament)
                startActivity(intent)
            }
        }
        tournamentsAdapter = TournamentsAdapter(onTournamentClickListener)
        tournamentsRecyclerView!!.adapter = tournamentsAdapter
    }

    private fun loadUserInfo() {
        user = getUserData(intent)
        displayUserInfo()
    }

    private fun getUserData(intent: Intent?): UserDTO {
        return intent!!.getSerializableExtra(USER_PARAMETER) as UserDTO
    }

    private fun displayUserInfo() {
        // Picasso.with(this).load("http://priscree.ru/img/7a1bbc9a11ee66.png").into(userImageView)
        Picasso.with(this).load("http://priscree.ru/img/013c16d74f1cd6.jpg").into(userImageView)
        nameTextView!!.text = user.name
        surnameTextView!!.text = user.surname
        statusTextView!!.text = user.status
        /* user.country?.let {
             locationTextView!!.text = user.country!!.gameRecord//тут нужна проверка, страна не обязательна вроде. Или сделать обязательной?? С тренером то же самое
         }
         user.coach?.let {
             coachNameTextView!!.text = getString(R.string.user_full_name, user.coach!!.gameRecord, user.coach!!.surname)
         }*/
        ratingTextView!!.text = user.rating.toString()
        friendsCountTextView!!.text = "34"
    }

    override fun showToast(resId: Int?) {
        val toast = Toast.makeText(this, resId!!, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    override fun showToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    override fun showProgress() {
        progressDialog = ProgressDialog.show(this, EMPTY_STRING, this.getString(R.string.please_wait))
    }

    override fun hideProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}