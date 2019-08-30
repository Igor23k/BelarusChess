package bobrchess.of.by.belaruschess.view.activity.impl

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.GameDTO
import bobrchess.of.by.belaruschess.presenter.GamesListPresenter
import bobrchess.of.by.belaruschess.presenter.impl.GamesListPresenterImpl
import bobrchess.of.by.belaruschess.util.Constants.Companion.EMPTY_STRING
import bobrchess.of.by.belaruschess.util.Constants.Companion.GAME_PARAMETER
import bobrchess.of.by.belaruschess.view.activity.GamesListContractView
import bobrchess.of.by.colibritweet.adapter.GamesAdapter
import butterknife.ButterKnife

class GamesListActivity : AbstractActivity(), GamesListContractView {

    private var gamesRecyclerView: RecyclerView? = null
    private var gamesAdapter: GamesAdapter? = null
    private var presenter: GamesListPresenter? = null
    private var progressDialog: ProgressDialog? = null
    private var queryEditText: EditText? = null
    private var toolbar: Toolbar? = null
    private var searchButton: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games_list)
        ButterKnife.bind(this)
        initRecyclerView()
        registerInternetCheckReceiver()

        toolbar = findViewById(R.id.toolbar)
        searchButton = toolbar!!.findViewById(R.id.e_search_button)
        queryEditText = toolbar!!.findViewById(R.id.e_query_text)

        searchButton!!.setOnClickListener { presenter!!.searchGames() }

        queryEditText!!.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                presenter!!.searchGames()
                return@OnEditorActionListener true
            }
            false
        })

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        presenter = GamesListPresenterImpl()
        presenter!!.attachView(this)
        presenter!!.viewIsReady()
        loadGames()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        gamesRecyclerView = findViewById(R.id.users_recycler_view)
        gamesRecyclerView!!.layoutManager = LinearLayoutManager(this)

        val onGameClickListener = object : GamesAdapter.OnGameClickListener {
            override fun onGameClick(game: GameDTO) {
                val intent = Intent(this@GamesListActivity, GameActivity::class.java)
                intent.putExtra(GAME_PARAMETER, game)
                startActivity(intent)
            }
        }
        gamesAdapter = GamesAdapter(onGameClickListener)
        gamesRecyclerView!!.adapter = gamesAdapter
    }

    fun getSearchText(): String {
        return queryEditText!!.text.toString()
    }

    private fun loadGames() {
        presenter!!.loadGames()
    }

    private fun loadGames(count: Int) {
        presenter!!.loadGames(count)
    }

    fun showGames(games: List<GameDTO>) {
        gamesAdapter!!.clearItems()
        gamesAdapter!!.setItems(games)
    }

    override fun showProgress() {
        progressDialog = ProgressDialog.show(this, EMPTY_STRING, this.getString(R.string.please_wait))
    }

    override fun hideProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    override fun setConnectionStatus(connectivityStatus: Int?) {
        presenter?.setConnectivityStatus(connectivityStatus)
    }

    override fun showAlertDialog(title: Int, message: Int, buttonText: Int, cancelable: Boolean) {
    }

    override fun dialogConfirmButtonClicked() {

    }
}