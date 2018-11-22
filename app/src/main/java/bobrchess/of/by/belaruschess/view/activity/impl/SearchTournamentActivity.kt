package bobrchess.of.by.belaruschess.view.activity.impl

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.presenter.SearchTournamentPresenter
import bobrchess.of.by.belaruschess.presenter.impl.SearchTournamentPresenterImpl
import bobrchess.of.by.belaruschess.util.Constants.Companion.EMPTY_STRING
import bobrchess.of.by.belaruschess.util.Constants.Companion.TOURNAMENT_PARAMETER
import bobrchess.of.by.belaruschess.view.activity.SearchTournamentContractView
import bobrchess.of.by.colibritweet.adapter.TournamentsAdapter
import butterknife.ButterKnife

/**
 * Created by Igor on 25.03.2018.
 */
class SearchTournamentActivity : AppCompatActivity(), SearchTournamentContractView {

    private var tournamentsRecyclerView: RecyclerView? = null
    private var tournamentsAdapter: TournamentsAdapter? = null
    private var presenter: SearchTournamentPresenter? = null
    private var progressDialog: ProgressDialog? = null

    // @BindView(R.id.e_query_text)
    private var queryEditText: EditText? = null

    //@BindView(R.id.toolbar)
    private var toolbar: Toolbar? = null

    //@BindView(R.id.e_search_button)
    private var searchButton: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_tournament)
        ButterKnife.bind(this)
        initRecyclerView()

        toolbar = findViewById(R.id.toolbar)
        searchButton = toolbar!!.findViewById(R.id.e_search_button)
        queryEditText = toolbar!!.findViewById(R.id.e_query_text)

        searchButton!!.setOnClickListener(View.OnClickListener { presenter!!.searchTournaments() })

        queryEditText!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                presenter!!.searchTournaments()
                return@OnEditorActionListener true
            }
            false
        })

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        presenter = SearchTournamentPresenterImpl()
        presenter!!.attachView(this)
        presenter!!.loadTournaments()
        presenter!!.viewIsReady()
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
        tournamentsRecyclerView = findViewById(R.id.tournaments_recycler_view)
        tournamentsRecyclerView!!.layoutManager = LinearLayoutManager(this)

        val onTournamentClickListener = object : TournamentsAdapter.OnTournamentClickListener {
            override fun onTournamentClick(tournament: TournamentDTO) {
                val intent = Intent(this@SearchTournamentActivity, TournamentActivity::class.java)
                intent.putExtra(TOURNAMENT_PARAMETER,  tournament)
                startActivity(intent)
            }
        }
        tournamentsAdapter = TournamentsAdapter(onTournamentClickListener)
        tournamentsRecyclerView!!.adapter = tournamentsAdapter
    }

    fun getSearchText(): String {
        return queryEditText!!.text.toString()
    }

    private fun loadTournaments() {
        presenter!!.loadTournaments()
    }

    private fun loadTournaments(count: Int) {
        presenter!!.loadTournaments(count)
    }

    fun showTournaments(tournaments: List<TournamentDTO>) {
        tournamentsAdapter!!.clearItems()
        tournamentsAdapter!!.setItems(tournaments)
    }

    override fun showToast(resId: Int?) {
        val toast = Toast.makeText(this, resId!!, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    override fun showToast(message: String?) {
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