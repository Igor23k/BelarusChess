package bobrchess.of.by.belaruschess.view.activity.impl

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.TypedValue
import android.widget.Toast
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.*
import bobrchess.of.by.belaruschess.fragments.EventListFragment
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.handler.IOHandler
import bobrchess.of.by.belaruschess.model.Divider
import bobrchess.of.by.belaruschess.model.EventDate
import bobrchess.of.by.belaruschess.model.EventTournament
import bobrchess.of.by.belaruschess.presenter.CountryPresenter
import bobrchess.of.by.belaruschess.presenter.PlacePresenter
import bobrchess.of.by.belaruschess.presenter.RankPresenter
import bobrchess.of.by.belaruschess.presenter.SearchTournamentPresenter
import bobrchess.of.by.belaruschess.presenter.impl.CountryPresenterImpl
import bobrchess.of.by.belaruschess.presenter.impl.PlacePresenterImpl
import bobrchess.of.by.belaruschess.presenter.impl.RankPresenterImpl
import bobrchess.of.by.belaruschess.presenter.impl.SearchTournamentPresenterImpl
import bobrchess.of.by.belaruschess.util.Constants
import bobrchess.of.by.belaruschess.util.Constants.Companion.COUNTRIES
import bobrchess.of.by.belaruschess.util.Constants.Companion.PLACES
import bobrchess.of.by.belaruschess.util.Constants.Companion.RANKS
import bobrchess.of.by.belaruschess.util.Constants.Companion.TOURNAMENTS_RESULT
import bobrchess.of.by.belaruschess.util.Constants.Companion.USER
import bobrchess.of.by.belaruschess.view.activity.CountryPresenterCallBack
import bobrchess.of.by.belaruschess.view.activity.PlacePresenterCallBack
import bobrchess.of.by.belaruschess.view.activity.RankPresenterCallBack
import bobrchess.of.by.belaruschess.view.activity.SearchTournamentContractView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_event_list.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AbstractActivity(), SearchTournamentContractView, PlacePresenterCallBack, RankPresenterCallBack, CountryPresenterCallBack {

    private var searchTournamentPresenter: SearchTournamentPresenter? = null
    private var progressDialog: ProgressDialog? = null
    private var placePresenter: PlacePresenter? = null
    private var rankPresenter: RankPresenter? = null
    private var countryPresenter: CountryPresenter? = null
    private var ranks: List<RankDTO>? = null//todo подума ь и мб передать чтобы все было одним запросом. И вообще это нужно ЛОКАЛЬНО хранить
    private var places: List<PlaceDTO>? = null//todo подума ь и мб передать чтобы все было одним запросом. И вообще это нужно ЛОКАЛЬНО хранить
    private var countries: List<CountryDTO>? = null
    private var userTournamentsResult: List<TournamentResultDTO>? = null
    private var userData: UserDTO? = null
    private var refereesAreLoaded = false
    private var tournamentsAreLoaded = false
    private var countriesAreLoaded = false
    private var placesAreLoaded = false
    private var ranksAreLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        userData = intent.getSerializableExtra("user") as UserDTO

        searchTournamentPresenter = SearchTournamentPresenterImpl()
        searchTournamentPresenter!!.attachView(this)
        searchTournamentPresenter!!.viewIsReady()

        rankPresenter = RankPresenterImpl()
        rankPresenter!!.attachView(this)
        rankPresenter!!.viewIsReady()
        rankPresenter!!.getRanks()

        placePresenter = PlacePresenterImpl()
        placePresenter!!.attachView(this)
        placePresenter!!.viewIsReady()
        placePresenter!!.getPlaces()

        countryPresenter = CountryPresenterImpl()
        countryPresenter!!.attachView(this)
        countryPresenter!!.viewIsReady()
        countryPresenter!!.getCountries()

        EventHandler.clearData()
        IOHandler.registerIO(this)
        lockAppbar()
        IOHandler.clearSharedPrefEventData()//todo убрать, чет локально сохраненные не показывает турниры
                //  loadTournamentsFromLocalStorage()
        loadTournaments()
    }

    fun unlockAppBar() {
        app_bar.isActivated = true
        setAppBarDragging(true)
    }

    fun lockAppbar() {
        this.app_bar.setExpanded(false, false)
        app_bar.isActivated = false
        setAppBarDragging(false)
    }

    private fun setAppBarDragging(isEnabled: Boolean) {
        val params = this.app_bar.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = AppBarLayout.Behavior()
        behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return isEnabled
            }
        })
        params.behavior = behavior
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val eventID = intent?.getIntExtra(FRAGMENT_EXTRA_TITLE_EVENTID, -1)
        val type = intent?.getStringExtra(FRAGMENT_EXTRA_TITLE_TYPE)
        if (toolbar.menu.findItem(R.id.toolbar_search)?.actionView != null) {

            (toolbar.menu.findItem(R.id.toolbar_search)?.actionView as android.support.v7.widget.SearchView).apply {
                //close search view
                toolbar.collapseActionView()
            }

            if (eventID != null && eventID > -1 && type != null) {
                startFragments(eventID, type)
            }
        }
    }

    private fun startFragments(eventID: Int, type: String) {

    }

    /**
     * addMonthDivider adds all 12 month dividers (dividers between events to group them in month groups)
     */
    fun addMonthDivider() {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, 1)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        for (i in 0 until 12) {
            cal.set(Calendar.MONTH, i)
            EventHandler.addEvent(
                    Divider(cal.time, resources.getStringArray(R.array.month_names)[i]),
                    this,
                    true
            )
        }
    }

    /**
     * onRequestPermissionsResult is the callback function after requesting the users permission for android permissions
     * In this case we request READ/WRITE rights on external storage and handle exporting/ importing event data from the external storage
     */
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {
        when (requestCode) {
            //writing to external
            6001 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                } else {
                    Toast.makeText(
                            this,
                            R.string.permissions_toast_denied_write,
                            Toast.LENGTH_LONG
                    ).show()
                }
            }
            //reading from external
            6002 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                } else {
                    Toast.makeText(
                            this,
                            R.string.permissions_toast_denied_read,
                            Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        fun convertPxToDp(context: Context, px: Float): Float {
            val metrics = context.resources.displayMetrics
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, metrics)
        }

        fun convertDpToPx(context: Context, dp: Float): Int {
            val metrics = context.resources.displayMetrics
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics).toInt()
        }

        const val FRAGMENT_TYPE_SHOW = "SHOW"
        const val FRAGMENT_TYPE_EDIT = "EDIT"

        const val FRAGMENT_EXTRA_TITLE_TYPE = "TYPE"
        const val FRAGMENT_EXTRA_TITLE_EVENTID = "EVENTID"
        const val FRAGMENT_EXTRA_TITLE_EVENTSTRING = "EVENTSTRING"
        const val FRAGMENT_EXTRA_TITLE_NOTIFICATIONID = "NOTIFICATIONID"
        const val FRAGMENT_EXTRA_TITLE_POSITION = "POSITION"
        const val FRAGMENT_EXTRA_TITLE_LOADALL = "LOADALL"
    }

    private fun loadTournaments() {
        searchTournamentPresenter!!.loadTournaments()
    }

    private fun loadTournaments(count: Int) {
        searchTournamentPresenter!!.loadTournaments(count)
    }

    override fun showProgress() {
        progressDialog = ProgressDialog.show(this, Constants.EMPTY_STRING, this.getString(R.string.please_wait))
    }

    override fun hideProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    override fun dialogConfirmButtonClicked() {

    }

    override fun setConnectionStatus(connectivityStatus: Int?) {
        searchTournamentPresenter?.setConnectivityStatus(connectivityStatus)
        rankPresenter?.setConnectivityStatus(connectivityStatus)
        countryPresenter?.setConnectivityStatus(connectivityStatus)
    }

    private fun transformDate(dateString: String?): String? {
        return try {
            val bdFormat = SimpleDateFormat("dd/mm/yyyy", Locale.getDefault())
            val newFormat = SimpleDateFormat("dd.mm.yyyy", Locale.getDefault())
            val date = bdFormat.parse(dateString)
            newFormat.format(date)
        } catch (e: Exception) {
            null
        }
    }

    private fun updateTournamentFragments() {
        val bundle = Bundle()

        val transaction = supportFragmentManager.beginTransaction()
        var eventFragment = EventListFragment.newInstance()
        eventFragment.arguments = bundle
        transaction.replace(
                R.id.fragment_placeholder,
                eventFragment
        ).commit()


        //start loading bitmap drawables in other thread to not block ui
        Thread(Runnable
        {
            // BitmapHandler.loadAllBitmaps(this)
            runOnUiThread {
                if (recyclerView != null) {
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
            }
        }).start()

        if (intent != null) {
            if (intent?.getBooleanExtra(FRAGMENT_EXTRA_TITLE_LOADALL, false) == true) {
                val eventID = intent?.getIntExtra(FRAGMENT_EXTRA_TITLE_EVENTID, -1)
                val type = intent?.getStringExtra(FRAGMENT_EXTRA_TITLE_TYPE)
                if (eventID != null && eventID > -1 && type != null) {
                    startFragments(eventID, type)
                }
            }
            intent = null
        }
    }

    fun loadTournamentsFromLocalStorage() {
        IOHandler.readAll(this)
        tournamentsAreLoaded = true
    }

    override fun showTournaments(tournaments: List<TournamentDTO>) {
        IOHandler.clearSharedPrefEventData()
        tournaments.forEach {
            val event = EventTournament(it.id.toInt(), EventDate.parseStringToDate(transformDate(it.startDate)!!, DateFormat.DEFAULT, Locale.GERMAN), it.name!!)
            event.name = it.name!!
            event.toursCount = it.toursCount
            event.fullDescription = it.fullDescription!!
            event.shortDescription = it.shortDescription!!
            event.toursCount = it.toursCount
            event.imageUri = it.image!!
            event.refereeId = it.referee?.id
            event.placeId = it.place?.id
            event.finishDate = EventDate.parseStringToDate(transformDate(it.finishDate)!!, DateFormat.DEFAULT, Locale.GERMAN)
            EventHandler.addEvent(
                    event,
                    this,
                    writeAfterAdd = true,
                    addNewNotification = false,
                    updateEventList = true,
                    addBitmap = false
            )
        }
        tournamentsAreLoaded = true
        updateFragments()
    }

    override fun rankIsLoaded(rankDTO: RankDTO?) {
    }

    override fun placeIsLoaded(placeDTO: PlaceDTO?) {
    }

    override fun countryIsLoaded(countryDTO: CountryDTO?) {
    }

    override fun countriesAreLoaded(list: MutableList<CountryDTO>?) {
        this.countries = list
        countriesAreLoaded = true
        updateFragments()
    }

    override fun ranksAreLoaded(ranks: MutableList<RankDTO>?) {//todo сделать чтобы это выполнилось ТОЧНО раньше чем основной код где это используется
        this.ranks = ranks
        ranksAreLoaded = true
        updateFragments()
    }

    override fun placesAreLoaded(places: MutableList<out PlaceDTO>?) {//todo сделать чтобы это выполнилось ТОЧНО раньше чем основной код где это используется
        this.places = places
        placesAreLoaded = true
        updateFragments()
    }

    private fun updateFragments() {
        if (placesAreLoaded && ranksAreLoaded && tournamentsAreLoaded && countriesAreLoaded) {
            updateTournamentFragments()
            hideProgress()
        }
    }

    fun getPlaces(): List<PlaceDTO>? {
        return places
    }

    fun getRanks(): List<RankDTO>? {
        return ranks
    }

    fun getCountries(): List<CountryDTO>? {
        return countries
    }

    fun getUserData(): UserDTO? {
        return userData
    }
}
