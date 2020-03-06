package bobrchess.of.by.belaruschess.view.activity.impl

import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.handler.SearchHandler
import bobrchess.of.by.belaruschess.presenter.SearchTournamentPresenter
import bobrchess.of.by.belaruschess.presenter.impl.SearchTournamentPresenterImpl
import bobrchess.of.by.belaruschess.util.Constants
import bobrchess.of.by.belaruschess.view.activity.SearchTournamentContractView
import bobrchess.of.by.belaruschess.adapter.EventAdapterSearching
import bobrchess.of.by.belaruschess.adapter.RecycleViewItemDivider
import kotlinx.android.synthetic.main.activity_searchable.*

/**
 * SearchableActivity is the activity used for searching events by their name
 * This has to be an extra activity because of the android framework functionality of the search view
 *
 * After a search has been completed by the user (clicking the magnifying glass in the search view) a query string is send to this activity which
 * returns all events which containt the query string in their names. After retrieving those events, they are displayed in the recyclerview
 */
class SearchableActivity : AbstractActivity(), SearchTournamentContractView {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var presenter: SearchTournamentPresenter? = null
    private var progressDialog: ProgressDialog? = null

    private var eventIndexList = emptyList<Int>().toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)

        setSupportActionBar(toolbar_searchable)

        presenter = SearchTournamentPresenterImpl()
        //presenter!!.attachView(this)
        presenter!!.viewIsReady()

        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                search(query)
            }
        }
    }

    private fun displayData(){
        if (this.eventIndexList.size == 0) {
            tv_failed_search.visibility = TextView.VISIBLE
            recyclerView_search.visibility = RecyclerView.GONE
        } else {
            tv_failed_search.visibility = TextView.GONE
            recyclerView_search.visibility = RecyclerView.VISIBLE
        }

        this.supportActionBar?.setDisplayShowHomeEnabled(true)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setHomeButtonEnabled(true)

        viewManager = LinearLayoutManager(this)
        viewAdapter = EventAdapterSearching(this, this.eventIndexList)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView_search).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        recyclerView.addItemDecoration(RecycleViewItemDivider(this))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun search(query: String) {
        supportActionBar?.title = this.resources.getString(R.string.searching_toolbar_title, query)
        val searchTerms = SearchHandler.splitStringToList(query)
        searchTerms?.forEach {
            presenter!!.loadTournaments()
            this.eventIndexList.addAll(SearchHandler.searchOnEventData(it))
        }
        this.eventIndexList = this.eventIndexList.distinct().toMutableList()
    }

    private fun loadTournaments() {
        presenter!!.loadTournaments()
    }

    private fun loadTournaments(count: Int) {
        presenter!!.loadTournaments(count)
    }

    override fun showTournaments(tournaments: List<TournamentDTO>) {
        displayData()
        this.eventIndexList = this.eventIndexList.distinct().toMutableList()
    }

    override fun showProgress() {
        progressDialog = ProgressDialog.show(this, Constants.EMPTY_STRING, this.getString(R.string.please_wait))
    }

    override fun hideProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    override fun showAlertDialog(title: Int, message: Int, buttonText: Int, cancelable: Boolean) {

    }

    override fun dialogConfirmButtonClicked() {

    }

    override fun setConnectionStatus(connectivityStatus: Int?) {
        presenter?.setConnectivityStatus(connectivityStatus)
    }
}
