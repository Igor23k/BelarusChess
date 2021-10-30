package bobrchess.of.by.belaruschess.fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.adapter.EventAdapter
import bobrchess.of.by.belaruschess.adapter.RecycleViewItemDivider
import bobrchess.of.by.belaruschess.dto.*
import bobrchess.of.by.belaruschess.dto.externalFide.TopPlayerDTO
import bobrchess.of.by.belaruschess.dto.externalFide.TopPlayersDTO
import bobrchess.of.by.belaruschess.dto.externalFide.WorldTournamentsDataDTO
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.handler.IOHandler
import bobrchess.of.by.belaruschess.model.*
import bobrchess.of.by.belaruschess.presenter.FideApiPresenter
import bobrchess.of.by.belaruschess.presenter.SearchPlacePresenter
import bobrchess.of.by.belaruschess.presenter.SearchTournamentPresenter
import bobrchess.of.by.belaruschess.presenter.UserPresenter
import bobrchess.of.by.belaruschess.presenter.impl.FideApiPresenterImpl
import bobrchess.of.by.belaruschess.presenter.impl.SearchPlacePresenterImpl
import bobrchess.of.by.belaruschess.presenter.impl.SearchTournamentPresenterImpl
import bobrchess.of.by.belaruschess.presenter.impl.UserPresenterImpl
import bobrchess.of.by.belaruschess.util.Constants
import bobrchess.of.by.belaruschess.util.Constants.Companion.KEY_GIRLS
import bobrchess.of.by.belaruschess.util.Constants.Companion.KEY_JUNIORS
import bobrchess.of.by.belaruschess.util.Constants.Companion.KEY_RATING_OPEN
import bobrchess.of.by.belaruschess.util.Constants.Companion.KEY_WOMEN
import bobrchess.of.by.belaruschess.util.Constants.Companion.PLACE
import bobrchess.of.by.belaruschess.util.Constants.Companion.TOP_PLAYER
import bobrchess.of.by.belaruschess.util.Constants.Companion.TOURNAMENT
import bobrchess.of.by.belaruschess.util.Constants.Companion.USER
import bobrchess.of.by.belaruschess.util.Constants.Companion.USER_BIRTHDAY_FORMAT
import bobrchess.of.by.belaruschess.util.Constants.Companion.WORLD_TOURNAMENT
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.util.Util.Companion.transformDate
import bobrchess.of.by.belaruschess.view.activity.*
import bobrchess.of.by.belaruschess.view.activity.impl.AuthorizationActivity
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_event_list.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventListFragment : AbstractFragment(), SearchTournamentContractView, FideApiContractView, UserContractView, SearchPlaceContractView {

    private var progressDialog: ProgressDialog? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: EventAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var entityType = TOURNAMENT
    private var isFABOpen = false

    private var searchTournamentPresenter: SearchTournamentPresenter? = null
    private var fideApiTournamentPresenter: FideApiPresenter? = null
    private var userPresenter: UserPresenter? = null
    private var searchPlacePresenter: SearchPlacePresenter? = null
    private var places: List<PlaceDTO>? = null
    private var ranks: List<RankDTO>? = null
    private var countries: List<CountryDTO>? = null
    private var userTournamentsResult: List<TournamentResultDTO>? = null
    private var users: MutableList<UserDTO>? = null
    private var userData: UserDTO? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_list, container, false)
    }

    var fabIsVisible = false

    fun updateUserInList(user: UserDTO?) {
        if (user != null) {
            users?.forEachIndexed { index, userItem ->
                if (user.id == userItem.id) {
                    users?.set(index, user)

                    val event = EventUser(user.id!!.toInt(), EventDate.parseStringToDate(transformDate(USER_BIRTHDAY_FORMAT, user.birthday!!), "dd/MM/yyyy", Locale.GERMAN), user.name!!, user.surname!!)
                    event.rankId = user.rank?.id
                    event.countryId = user.country?.id
                    event.coach = user.coach
                    event.rating = user.rating
                    event.imageUri = user.image
                    event.patronymic = user.patronymic

                    EventHandler.addEvent(
                            event,
                            context!!,
                            writeAfterAdd = false,
                            addNewNotification = false,
                            updateEventList = true,
                            addBitmap = false
                    )

                    return
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress()

        val activity: MainActivity? = activity as MainActivity?
        places = activity!!.getPlaces()
        ranks = activity.getRanks()
        countries = activity.getCountries()
        userData = activity.getUserData()

        fideApiTournamentPresenter = FideApiPresenterImpl()
        fideApiTournamentPresenter!!.attachView(this)
        fideApiTournamentPresenter!!.viewIsReady()

        searchTournamentPresenter = SearchTournamentPresenterImpl()
        searchTournamentPresenter!!.attachView(this)
        searchTournamentPresenter!!.viewIsReady()

        userPresenter = UserPresenterImpl()
        userPresenter!!.attachView(this)
        userPresenter!!.setPackageModel(PackageModel(this.context!!))
        userPresenter!!.viewIsReady()

        searchPlacePresenter = SearchPlacePresenterImpl()
        searchPlacePresenter!!.attachView(this)
        searchPlacePresenter!!.viewIsReady()

        setHasOptionsMenu(true)
        (context as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (context as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(false)

        (context as MainActivity).scrollable_toolbar.isTitleEnabled = false
        (context as MainActivity).toolbar.title = getString(R.string.app_name)

        isFABOpen = false

        if (userData != null) {
            fabIsVisible = userData!!.beOrganizer || userData!!.beAdmin
        }
        if (!fabIsVisible) {
            view.findViewById<FloatingActionButton>(R.id.fab_show_fab_menu).hide()
        }

        fab_layout_add_place.visibility = ConstraintLayout.INVISIBLE
        fab_layout_add_tournament.visibility = ConstraintLayout.INVISIBLE
        fab_layout_add_one_time.visibility = ConstraintLayout.INVISIBLE

        init()
        initMultifield()
        hideProgress()
    }


    private fun init() {
        viewManager = LinearLayoutManager(view!!.context)
        viewAdapter = EventAdapter(view!!.context, this.fragmentManager!!, places, ranks, countries, users)

        recyclerView = view!!.findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            scrollToPosition(traverseForFirstMonthEntry())
        }
        recyclerView.addItemDecoration(RecycleViewItemDivider(view!!.context))
        recyclerView.setPadding(
                recyclerView.paddingLeft,
                recyclerView.paddingTop,
                recyclerView.paddingRight,
                (resources.getDimension(R.dimen.fab_margin) + resources.getDimension(R.dimen.fab_size_bigger)).toInt()
        )


        fab_show_fab_menu.setOnClickListener {
            if (isFABOpen) {
                closeFABMenu()
            } else {
                showFABMenu()
            }
        }

        fab_add_tournament.setOnClickListener {
            closeFABMenu(true)
            val ft = fragmentManager!!.beginTransaction()
            ft.replace(
                    R.id.fragment_placeholder,
                    UpdateTournamentInstanceFragment.newInstance()
            )
            ft.addToBackStack(null)
            ft.commit()
        }

        fab_layout_add_place.setOnClickListener {
            closeFABMenu(true)
            val ft = fragmentManager!!.beginTransaction()
            ft.replace(
                    R.id.fragment_placeholder,
                    PlaceInstanceFragment.newInstance()
            )
            ft.addToBackStack(null)
            ft.commit()
        }
    }

    override fun onResume() {
        super.onResume()
        //when no items except of the 12 month items are in the event list, then display text message
        /* if (EventHandler.getList().size - 12 == 0) {
             tv_no_events.visibility = TextView.VISIBLE
         } else {
             tv_no_events.visibility = TextView.GONE
         }*/
        registerInternetCheckReceiver()
    }

    private fun showFABMenu() {
        isFABOpen = true
        fab_show_fab_menu.isClickable = false
        //show layouts
        if (fabIsVisible) {
            if (userData!!.beAdmin) {
                fab_layout_add_place.visibility = ConstraintLayout.VISIBLE
            }
            if (userData!!.beOrganizer) {
                fab_layout_add_tournament.visibility = ConstraintLayout.VISIBLE
            }
            fab_layout_add_one_time.visibility = ConstraintLayout.VISIBLE
        }
        this.recyclerView.animate().alpha(0.15f).apply {
            duration = 175
        }

        //move layouts
        fab_layout_add_tournament.animate()
                .translationYBy(-resources.getDimension(R.dimen.standard_55) - 20).apply {
                    duration = 100
                }.withEndAction {
                    fab_layout_add_tournament.animate().translationYBy(20.toFloat()).apply {
                        duration = 75
                    }
                }

        //move add annual event layout up
        fab_layout_add_place.animate()
                .translationYBy(-resources.getDimension(R.dimen.standard_105) - 40)
                .apply {
                    duration = 100
                }.withEndAction {
                    fab_layout_add_place.animate().translationYBy(40.toFloat()).apply {
                        duration = 75
                    }
                }

        //move add one time event layout up
        fab_layout_add_one_time.animate()
                .translationYBy(-resources.getDimension(R.dimen.standard_155) - 60).apply {
                    duration = 100
                }.withEndAction {
                    fab_layout_add_one_time.animate().translationYBy(60.toFloat()).apply {
                        duration = 75
                    }
                }


        fab_show_fab_menu.animate().duration = 100
        //some fancy overrotated animation
        fab_show_fab_menu.animate().rotationBy(80.0f).withEndAction {
            fab_show_fab_menu.animate().rotationBy(-35.0f).apply {
                duration = 75
            }.withEndAction {
                fab_show_fab_menu.isClickable = true
            }
        }
        //disable all click events on eventview adapter
        viewAdapter.isClickable = false
    }

    /**
     * @param immediateAction : Boolean indicates wether an action should take place after the animation
     */
    private fun closeFABMenu(immediateAction: Boolean = false) {
        isFABOpen = false
        //show layouts
        if (!immediateAction) {
            fab_show_fab_menu.isClickable = false
        }

        this.recyclerView.animate().alpha(1.0f)

        //move add birthday event layout down
        fab_layout_add_tournament.animate()
                .translationYBy(resources.getDimension(R.dimen.standard_55))
                .withEndAction {
                    if (!immediateAction) {
                        fab_layout_add_tournament.visibility = ConstraintLayout.INVISIBLE
                    }
                }

        //move add annual event layout down
        fab_layout_add_place.animate()
                .translationYBy(resources.getDimension(R.dimen.standard_105))
                .withEndAction {
                    if (!immediateAction) {
                        fab_layout_add_place.visibility = ConstraintLayout.INVISIBLE
                    }
                }

        //move add one time event layout down
        fab_layout_add_one_time.animate()
                .translationYBy(resources.getDimension(R.dimen.standard_155))
                .withEndAction {
                    if (!immediateAction) {
                        fab_layout_add_one_time.visibility = ConstraintLayout.INVISIBLE
                    }
                }

        fab_show_fab_menu.animate().rotationBy(-45.0f).withEndAction {
            if (!immediateAction) {
                fab_show_fab_menu.isClickable = true
            }
        }
        viewAdapter.isClickable = true
    }

    /**
     * traverseForFirstMonthEntry is a function to get the position of the month item position of the current month
     */
    private fun traverseForFirstMonthEntry(): Int {
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        for (i in 0 until EventHandler.getList().size) {
            if (EventHandler.getList()[i].getMonth() == currentMonth)
                return i
        }
        return 0
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.toolbar_main, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.toolbar_search)?.actionView as android.support.v7.widget.SearchView).apply {
            //Assume current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

            setIconifiedByDefault(true)

            //submit button in action bar disabled
            isSubmitButtonEnabled = false
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            R.id.toolbar_search -> {
                //todo
            }

            R.id.item_show_tournaments -> {
                EventHandler.clearData()//TODO
                entityType = TOURNAMENT
                searchTournamentPresenter?.loadTournaments()
            }

            R.id.item_show_users -> {
                EventHandler.clearData()//todo
                entityType = USER
                userPresenter?.loadUsers()
            }

            R.id.item_show_places -> {
                EventHandler.clearData()//todo
                entityType = PLACE
                searchPlacePresenter?.loadPlaces()
            }

            R.id.item_show_world_tournaments -> {
                EventHandler.clearData()//todo
                entityType = WORLD_TOURNAMENT
                fideApiTournamentPresenter?.loadWorldTournaments(getCurrentyear(), worldChampion = false, closestEvents = false, category = "1,2,3,4,5,6,7,8", dateStartMonth = getCurrentMonthNumber())
            }

            R.id.item_show_world_rating -> {
                EventHandler.clearData()//todo
                entityType = TOP_PLAYER
                fideApiTournamentPresenter?.loadTopPlayers()
            }

            R.id.item_logout -> {
                showLogoutConfirmation()
            }

            R.id.action_refresh -> {
                when (entityType) {
                    USER -> {
                        userPresenter?.loadUsers()
                        EventHandler.clearData()
                    }
                    TOURNAMENT -> {
                        searchTournamentPresenter?.loadTournaments()
                        EventHandler.clearData()
                    }
                    PLACE -> {
                        searchPlacePresenter?.loadPlaces()
                        EventHandler.clearData()
                    }
                }
            }
            R.id.item_update_user_info -> {
                updateUserInfoClicked()
            }
            R.id.item_help -> {
                helpClicked()
            }
            R.id.item_about -> {
                aboutClicked()
            }
            /*R.id.item_settings -> {
                settingsClicked()
            }*/
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLogoutConfirmation() {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setTitle(resources.getString(R.string.confirmation))
        alertBuilder.setMessage(resources.getString(R.string.alert_dialog_body_message_logout))

        // Set a positive button and its click listener on alert dialog
        alertBuilder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
            EventHandler.clearData()
            val intent = Intent(this.context, AuthorizationActivity::class.java)
            startActivity(intent)
            val activity: MainActivity? = activity as MainActivity?
            activity?.clearUserData(null)
            this.activity?.finish()
        }

        // don't do anything on negative button
        alertBuilder.setNegativeButton(resources.getString(R.string.no)) { _, _ ->
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = alertBuilder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }


    private fun tournamentsSearchClicked() {
        //open about fragment
        closeFABMenu(true)
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(
                R.id.fragment_placeholder,
                HelpFragment.newInstance()
        )
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun updateUserInfoClicked() {
        //open help fragment
        closeFABMenu(true)
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(
                R.id.fragment_placeholder,
                EditUserInstanceFragment.newInstance()
        )
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun helpClicked() {
        //open help fragment
        closeFABMenu(true)
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(
                R.id.fragment_placeholder,
                HelpFragment.newInstance()
        )
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun aboutClicked() {
        //open about fragment
        closeFABMenu(true)
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(
                R.id.fragment_placeholder,
                AboutFragment.newInstance()
        )
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun settingsClicked() {
        //open settings fragment
        closeFABMenu(true)
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(
                R.id.fragment_placeholder,
                SettingsFragment.newInstance()
        )
        ft.addToBackStack(null)
        ft.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(): EventListFragment {
            return EventListFragment()
        }
    }

    override fun showTournaments(tournaments: List<TournamentDTO>) {
        IOHandler.clearSharedPrefEventData()//todo тут если допусти 1 турнир есть, а в ьд поменять у него айди то станет 2 турнира, не удаляются тут они
        tournaments.forEach {
            val event = EventTournament(it.id.toInt(), EventDate.parseStringToDate(it.startDate!!, "dd/MM/yyyy", Locale.GERMAN), it.name!!)
            event.name = it.name!!
            event.toursCount = it.toursCount
            event.fullDescription = it.fullDescription!!
            event.shortDescription = it.shortDescription!!
            event.toursCount = it.toursCount
            event.imageUri = it.image!!
            event.refereeId = it.referee?.id
            //event.createdBy = it.createdBy?.id
            event.placeId = it.place?.id
            event.finishDate = EventDate.parseStringToDate(it.finishDate!!, "dd/MM/yyyy", Locale.GERMAN)
            EventHandler.addEvent(
                    event,
                    context!!,
                    writeAfterAdd = true,
                    addNewNotification = false,
                    updateEventList = true,
                    addBitmap = false
            )
        }
        updateFragments()
    }

    override fun showUsers(users: List<UserDTO>?) {
        this.users = users?.toMutableList()
        IOHandler.clearSharedPrefEventData()//todo тут если допусти 1 турнир есть, а в ьд поменять у него айди то станет 2 турнира, не удаляются тут они
        users!!.forEach {
            val event = EventUser(it.id!!.toInt(), EventDate.parseStringToDate(it.birthday!!, "dd/MM/yyyy", Locale.getDefault()), it.name!!, it.surname!!)
            event.rankId = it.rank?.id
            event.countryId = it.country?.id
            event.coach = it.coach
            event.rating = it.rating
            event.imageUri = it.image
            event.patronymic = it.patronymic

            EventHandler.addEvent(
                    event,
                    context!!,
                    writeAfterAdd = false,
                    addNewNotification = false,
                    updateEventList = true,
                    addBitmap = false
            )
        }
        updateFragments()
    }

    private fun updateFragments() {
        val transaction = this.activity?.supportFragmentManager?.beginTransaction()
        val eventFragment = this
        transaction?.replace(
                R.id.fragment_placeholder,
                eventFragment
        )?.commit()

        //start loading bitmap drawables in other thread to not block ui
        Thread(Runnable
        {
            //BitmapHandler.loadAllBitmaps(this.context!!) //убрал пока что это
            this.activity?.runOnUiThread {
                recyclerView.adapter!!.notifyDataSetChanged()
            }
        }).start()
        var intent = this.activity?.intent
        if (intent != null) {
            if (intent.getBooleanExtra(MainActivity.FRAGMENT_EXTRA_TITLE_LOADALL, false)) {
                val eventID = intent.getIntExtra(MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID, -1)
                val type = intent.getStringExtra(MainActivity.FRAGMENT_EXTRA_TITLE_TYPE)
                if (eventID > -1 && type != null) {
                    startFragments(eventID, type)
                }
            }
        }
    }

    private fun startFragments(eventID: Int, type: String) {
        val bundle = Bundle()
        //do this in more adaptable way
        bundle.putInt(
                MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
                eventID
        )

        EventHandler.getEventToEventIndex(eventID)?.let { event ->
            val eventFragment: Fragment? = when (event) {
                is EventTournament -> {
                    if (type == MainActivity.FRAGMENT_TYPE_SHOW) {
                        ShowTournamentEvent.newInstance()
                    } else {
                        UpdateTournamentInstanceFragment.newInstance()
                    }
                }
                is EventWorldTournament -> {
                    ShowWorldTournamentEvent.newInstance()
                }
                is EventTournamentResult -> {
                    if (type == MainActivity.FRAGMENT_TYPE_SHOW) {
                        ShowTournamentEvent.newInstance()
                    } else {
                        UpdateTournamentInstanceFragment.newInstance()
                    }
                }
                is EventUser -> {
                    ShowUserEvent.newInstance()
                }
                is EventTopPlayer -> {
                    ShowTopPlayerEvent.newInstance()
                }
                else -> {
                    null
                }
            }
            if (eventFragment != null) {
                val ft = this.activity?.supportFragmentManager?.beginTransaction()
                // add arguments to fragment
                eventFragment.arguments = bundle
                ft?.replace(
                        R.id.fragment_placeholder,
                        eventFragment
                )
                ft?.addToBackStack(null)
                ft?.commit()
            }
        }
    }

    override fun showProgress() {
        progressDialog = ProgressDialog.show(this.context, Constants.EMPTY_STRING, this.getString(R.string.please_wait))
    }

    override fun hideProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    override fun showPlaces(places: MutableList<out PlaceDTO>?) {
        IOHandler.clearSharedPrefEventData()//todo тут если допусти 1 турнир есть, а в ьд поменять у него айди то станет 2 турнира, не удаляются тут они
        places?.forEach {
            val event = EventPlace(it.id!!.toInt(), it.name!!)
            event.city = it.city
            event.street = it.street
            event.building = it.building
            event.imageUri = it.image
            event.capacity = it.capacity
            event.countryId = it.country?.id
            event.approved = it.approved
            EventHandler.addEvent(
                    event,
                    context!!,
                    writeAfterAdd = true,
                    addNewNotification = false,
                    updateEventList = true,
                    addBitmap = false
            )
        }
        updateFragments()
    }


    override fun showWorldTournaments(worldTournamentsDataDTO: WorldTournamentsDataDTO) {
        //btnOrder.visibility = TextView.VISIBLE
        IOHandler.clearSharedPrefEventData()
        worldTournamentsDataDTO.data?.europeanTournaments?.forEach {
            val event = EventWorldTournament(it.id!!.toInt(), EventDate.parseStringToDate(it.dateStart!!, "yyyy-MM-dd", Locale.GERMAN), it.name!!)
            event.toursCount = it.numRound?.toInt()
            event.eventType = it.eventType
            event.finishDate = EventDate.parseStringToDate(it.dateEnd!!, "yyy-MM-dd", Locale.GERMAN)
            event.country = it.country
            event.city = it.city
            EventHandler.addEvent(
                    event,
                    context!!,
                    writeAfterAdd = true,
                    addNewNotification = false,
                    updateEventList = true,
                    addBitmap = false
            )
        }
        updateFragments()
    }

    override fun showTopPlayers(topPlayersDTO: TopPlayersDTO?) {
        IOHandler.clearSharedPrefEventData()//todo
        addMonthDivider(getMonthDivider(66666666, Util.getInternalizedMessage(KEY_RATING_OPEN)))
        processTopPlayers(topPlayersDTO?.open)
        addMonthDivider(getMonthDivider(77777777, Util.getInternalizedMessage(KEY_WOMEN)))
        processTopPlayers(topPlayersDTO?.women)
        addMonthDivider(getMonthDivider(88888888, Util.getInternalizedMessage(KEY_JUNIORS)))
        processTopPlayers(topPlayersDTO?.juniors)
        addMonthDivider(getMonthDivider(99999999, Util.getInternalizedMessage(KEY_GIRLS)))
        processTopPlayers(topPlayersDTO?.girls)
        updateFragments()
    }

    private fun addMonthDivider(divider: Divider) {
        EventHandler.addEvent(
                divider,
                context!!,
                true, false, true, false
        )
    }

    private fun getMonthDivider(id: Int, text: String): Divider {
        val monthDivider = Divider(Calendar.getInstance().time, text)
        monthDivider.eventID = id
        return monthDivider
    }


    private fun processTopPlayers(list: List<TopPlayerDTO>?) {
        list?.forEach {
            val fullName = buildFullName(it.name)
            val event = EventTopPlayer(it.id!!.toInt(), EventDate.parseStringToDate(it.birthday!!, "yyy-MM-dd", Locale.GERMAN), fullName?.get(0)!!, fullName[1])
            event.position = it.position?.toInt()
            event.rating = it.rating?.toInt()
            event.blitzRating = it.blitzRating?.toInt()
            event.rapidRating = it.rapidRating?.toInt()
            event.topType = it.topType
            event.country = it.country
            event.image = it.image
            EventHandler.addEvent(
                    event,
                    context!!,
                    writeAfterAdd = true,
                    addNewNotification = false,
                    updateEventList = true,
                    addBitmap = false,
                    sortList = false
            )
        }
    }

    private fun buildFullName(name: String?): List<String>? {
        var fullName: List<String>? = name?.split(",")?.map { it.trim() }
        if (fullName!!.size < 2) {
            fullName = name?.split(" ")?.map { it.trim() }
            if (fullName!!.size < 2) {
                fullName = ArrayList()
            } else if (fullName.isEmpty()) {
                fullName = ArrayList()
                fullName[0] = name!!
                fullName[1] = name
            }
        }
        return fullName
    }

    override fun dialogConfirmButtonClicked() {

    }

    fun initMultifield() {
        /* var chooseTournamentLocationBUtton: Button? = null
         var calendar: ImageView? = null
         var listItems: Array<String>? = null
         var checkedItems: BooleanArray? = null
         val mItemsIndex = java.util.ArrayList<Int>()

         chooseTournamentLocationBUtton = view?.findViewById<View>(R.id.btnOrder) as Button
         calendar = view?.findViewById<View>(R.id.i_calendar_birthday) as ImageView
         btnOrder.visibility = TextView.INVISIBLE

         listItems = resources.getStringArray(R.array.world_tournaments_types_items)
         checkedItems = BooleanArray((listItems as Array<String>).size)

         chooseTournamentLocationBUtton.setOnClickListener {
             val mBuilder = AlertDialog.Builder(this.context!!)
             mBuilder.setTitle(R.string.tournament_location_dialog_title)
             mBuilder.setMultiChoiceItems(listItems, checkedItems) { dialogInterface, position, isChecked ->
                 if (isChecked) {
                     mItemsIndex.add(position)
                 } else {
                     mItemsIndex.remove(Integer.valueOf(position))
                 }
             }

             mBuilder.setCancelable(false)

             mBuilder.setPositiveButton(R.string.ok_label) { dialogInterface, which ->
                 var item = ""
                 for (i in mItemsIndex.indices) {
                     item += ((mItemsIndex.get(i)) + 1)
                     if (i != mItemsIndex.size - 1) {
                         item = "$item,"
                     }
                 }
                 fideApiTournamentPresenter?.loadWorldTournaments(2020, worldChampion = false, closestEvents = true, category = item, dateStartMonth = 8)
             }
             mBuilder.setNegativeButton(R.string.dismiss_label) { dialogInterface, i -> dialogInterface.dismiss() }// todo интернационализация
             mBuilder.setNeutralButton(R.string.clear_all_label) { dialogInterface, which ->
                 for (i in checkedItems.indices) {
                     checkedItems[i] = false
                     mItemsIndex.clear()
                 }
             }
             val mDialog = mBuilder.create()
             mDialog.show()
         }*/
    }

    private fun getCurrentMonthNumber(): Int {
        val cal = Calendar.getInstance()
        cal.time = Date()
        return cal[Calendar.MONTH]
    }

    private fun getCurrentyear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    override fun showUser(user: UserDTO?) {

    }
}
