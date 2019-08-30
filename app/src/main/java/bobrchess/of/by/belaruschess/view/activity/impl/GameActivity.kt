package bobrchess.of.by.belaruschess.view.activity.impl

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.GameDTO
import bobrchess.of.by.belaruschess.presenter.GamePresenter
import bobrchess.of.by.belaruschess.presenter.impl.GamePresenterImpl
import bobrchess.of.by.belaruschess.util.Constants.Companion.EMPTY_STRING
import bobrchess.of.by.belaruschess.util.Constants.Companion.GAME_PARAMETER
import bobrchess.of.by.belaruschess.view.activity.GameContractView
import com.squareup.picasso.Picasso
import java.util.*


class GameActivity : AbstractActivity(), GameContractView {

    private var firstPlayerImageView: ImageView? = null
    private var secondPlayerImageView: ImageView? = null
    private var firstPlayerNameTextView: TextView? = null
    private var secondPlayerNameTextView: TextView? = null
    private var data: TextView? = null
    private var toolbar: Toolbar? = null

    private var game = GameDTO()

    private var progressDialog: ProgressDialog? = null
    private var presenter: GamePresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        registerInternetCheckReceiver()
        firstPlayerNameTextView = findViewById(R.id.first_player__name_text_view)
        secondPlayerNameTextView = findViewById(R.id.second_player__name_text_view)
        firstPlayerImageView = findViewById(R.id.first_player__image_view)
        secondPlayerImageView = findViewById(R.id.second_player_image_view)
        data = findViewById(R.id.game_data_text_view)


        toolbar = findViewById(R.id.toolbar)
        presenter = GamePresenterImpl()
        presenter!!.attachView(this)
        setSupportActionBar(toolbar)
        presenter!!.viewIsReady()
        loadGameData()


        val actionBar = actionBar

        actionBar?.title = "50"
    }

    private fun loadGameData() {
        game = getGameData(intent)
        displayGameData()
    }

    private fun displayGameData() {
        Picasso.with(this).load(game.firstChessPlayer?.image).into(firstPlayerImageView)
        Picasso.with(this).load(game.secondChessPlayer?.image).into(secondPlayerImageView)
        firstPlayerNameTextView!!.text = getString(R.string.user_full_name_placeholder, game.firstChessPlayer?.name, game.firstChessPlayer?.surname)
        secondPlayerNameTextView!!.text = getString(R.string.user_full_name_placeholder, game.secondChessPlayer?.name, game.secondChessPlayer?.surname)
        data!!.text = game.gameRecord
    }

    interface OnGameClickListener {
        fun onGameClick(game: GameDTO)
    }

    private fun getGameData(intent: Intent?): GameDTO {
        return intent!!.getSerializableExtra(GAME_PARAMETER) as GameDTO
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
            R.id.action_search -> {
                val intent = Intent(this, SearchUserActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    override fun showProgress() {
        progressDialog = ProgressDialog.show(this, EMPTY_STRING, this.getString(R.string.please_wait))
    }

    override fun hideProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    override fun dialogConfirmButtonClicked() {

    }

    override fun setConnectionStatus(connectivityStatus: Int?) {
        presenter?.setConnectivityStatus(connectivityStatus)
    }
}