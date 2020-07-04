package bobrchess.of.by.belaruschess.fragments

import android.app.SearchManager
import android.content.Context
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
import bobrchess.of.by.belaruschess.presenter.SearchUserPresenter
import bobrchess.of.by.belaruschess.presenter.impl.FideApiPresenterImpl
import bobrchess.of.by.belaruschess.presenter.impl.SearchPlacePresenterImpl
import bobrchess.of.by.belaruschess.presenter.impl.SearchTournamentPresenterImpl
import bobrchess.of.by.belaruschess.presenter.impl.SearchUserPresenterImpl
import bobrchess.of.by.belaruschess.util.Constants.Companion.PLACE
import bobrchess.of.by.belaruschess.util.Constants.Companion.TOP_PLAYER
import bobrchess.of.by.belaruschess.util.Constants.Companion.TOURNAMENT
import bobrchess.of.by.belaruschess.util.Constants.Companion.USER
import bobrchess.of.by.belaruschess.util.Constants.Companion.WORLD_TOURNAMENT
import bobrchess.of.by.belaruschess.util.Util.Companion.transformDate
import bobrchess.of.by.belaruschess.view.activity.FideApiContractView
import bobrchess.of.by.belaruschess.view.activity.SearchPlaceContractView
import bobrchess.of.by.belaruschess.view.activity.SearchTournamentContractView
import bobrchess.of.by.belaruschess.view.activity.SearchUserContractView
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_event_list.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventListFragment : AbstractFragment(), SearchTournamentContractView, FideApiContractView, SearchUserContractView, SearchPlaceContractView {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: EventAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var entityType = TOURNAMENT
    private var isFABOpen = false

    private var searchTournamentPresenter: SearchTournamentPresenter? = null
    private var fideApiTournamentPresenter: FideApiPresenter? = null
    private var searchUserPresenter: SearchUserPresenter? = null
    private var searchPlacePresenter: SearchPlacePresenter? = null
    private var places: List<PlaceDTO>? = null
    private var ranks: List<RankDTO>? = null
    private var countries: List<CountryDTO>? = null
    private var userTournamentsResult: List<TournamentResultDTO>? = null
    private var users: List<UserDTO>? = null
    private var userData: UserDTO? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_list, container, false)
    }

    var rankItemsListType = object : TypeToken<List<RankDTO>>() {}.type
    var placeItemsListType = object : TypeToken<List<PlaceDTO>>() {}.type
    var countryItemsListType = object : TypeToken<List<CountryDTO>>() {}.type
    var userItemsListType = object : TypeToken<List<UserDTO>>() {}.type
    var userItemType = object : TypeToken<UserDTO>() {}.type
    var fabIsVisible = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        places = Gson().fromJson(arguments?.getString("places"), placeItemsListType)
        ranks = Gson().fromJson(arguments?.getString("ranks"), rankItemsListType)
        countries = Gson().fromJson(arguments?.getString("countries"), countryItemsListType)
        users = Gson().fromJson(arguments?.getString("users"), userItemsListType)
        userData = Gson().fromJson(arguments?.getString("user"), userItemType)//todo сделатть это все константами и в других классах

        super.onViewCreated(view, savedInstanceState)

        fideApiTournamentPresenter = FideApiPresenterImpl()
        fideApiTournamentPresenter!!.attachView(this)
        fideApiTournamentPresenter!!.viewIsReady()

        searchTournamentPresenter = SearchTournamentPresenterImpl()
        searchTournamentPresenter!!.attachView(this)
        searchTournamentPresenter!!.viewIsReady()

        searchUserPresenter = SearchUserPresenterImpl()
        searchUserPresenter!!.attachView(this)
        searchUserPresenter!!.viewIsReady()

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
    }


    private fun init() {
        viewManager = LinearLayoutManager(view!!.context)
        viewAdapter = EventAdapter(view!!.context, this.fragmentManager!!, places, ranks, countries, users, userTournamentsResult)

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
                    TournamentInstanceFragment.newInstance()
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

        fab_layout_add_one_time.setOnClickListener {
            closeFABMenu(true)
            val ft = fragmentManager!!.beginTransaction()
            ft.replace(
                    R.id.fragment_placeholder,
                    OneTimeEventInstanceFragment.newInstance()
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
            if (userData!!.beAdmin) {//todo уточнить только ли админу или всем тренерам дать права
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
                EventHandler.clearData()
                entityType = TOURNAMENT
                searchTournamentPresenter?.loadTournaments()
            }

            R.id.item_show_users -> {
                EventHandler.clearData()
                entityType = USER
                searchUserPresenter?.loadUsers()
            }

            R.id.item_show_places -> {
                EventHandler.clearData()
                entityType = PLACE
                searchPlacePresenter?.loadPlaces()
            }

            R.id.item_show_world_tournaments -> {
                EventHandler.clearData()
                entityType = WORLD_TOURNAMENT
                fideApiTournamentPresenter?.loadTournaments(2020, worldChampion = false, closestEvents = true, category = "2,7", dateStartMonth = 8)
            }

            R.id.item_show_world_rating -> {
                EventHandler.clearData()
                entityType = TOP_PLAYER
                fideApiTournamentPresenter?.loadTopPlayersRating()
            }

            R.id.action_refresh -> {
                when (entityType) {
                    USER -> {
                        searchUserPresenter?.loadUsers()
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
            R.id.item_help -> {
                helpClicked()
            }
            R.id.item_about -> {
                aboutClicked()
            }
            R.id.item_settings -> {
                settingsClicked()
            }
        }
        return super.onOptionsItemSelected(item)
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

    override fun setConnectionStatus(connectivityStatus: Int?) {
        println()//todo  перенести в абстракт фрагмент?
    }

    override fun showTournaments(tournaments: List<TournamentDTO>) {
        //todo такой эе метод в мэйн активити, сделать один чтобы был
        IOHandler.clearSharedPrefEventData()//todo тут если допусти 1 турнир есть, а в ьд поменять у него айди то станет 2 турнира, не удаляются тут они
        tournaments.forEach {
            val event = EventTournament(it.id.toInt(), EventDate.parseStringToDate(transformDate("dd/mm/yyyy", it.startDate!!), DateFormat.DEFAULT, Locale.GERMAN), it.name!!)
            event.name = it.name!!
            event.toursCount = it.toursCount
            event.fullDescription = it.fullDescription!!
            event.shortDescription = it.shortDescription!!
            event.toursCount = it.toursCount
            event.imageUri = it.image!!
            event.refereeId = it.referee?.id
            event.placeId = it.place?.id
            event.finishDate = EventDate.parseStringToDate(transformDate("dd/mm/yyyy", it.finishDate!!), DateFormat.DEFAULT, Locale.GERMAN)
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
        this.users = users
        IOHandler.clearSharedPrefEventData()//todo тут если допусти 1 турнир есть, а в ьд поменять у него айди то станет 2 турнира, не удаляются тут они
        users!!.forEach {
            val event = EventUser(it.id!!.toInt(), EventDate.parseStringToDate(transformDate("dd/mm/yyyy", it.birthday!!), DateFormat.DEFAULT, Locale.GERMAN), it.name!!, it.surname!!)
            event.rankId = it.rank?.id
            event.countryId = it.country?.id
            event.coachId = it.coach?.id
            event.rating = it.rating
            event.imageUri = it.image
            event.patronymic = it.patronymic

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

    private fun updateFragments() {
        val bundle = Bundle()
        val gson = GsonBuilder().setPrettyPrinting().create()
        val placesList = gson.toJson(places)
        val ranksList = gson.toJson(ranks)
        val countriesList = gson.toJson(countries)
        val usersList = gson.toJson(users)
        val userTournamentsList = gson.toJson(userTournamentsResult)

        bundle.putString(
                "places",
                placesList
        )

        bundle.putString(
                "ranks",
                ranksList
        )

        bundle.putString(
                "countries",
                countriesList
        )

        bundle.putString(
                "users",
                usersList
        )

        bundle.putString(
                "tournamentsResult",
                userTournamentsList
        )

        val transaction = this.activity?.supportFragmentManager?.beginTransaction()
        var eventFragment = this
        eventFragment.arguments = bundle
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
                val eventID = intent?.getIntExtra(MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID, -1)
                val type = intent?.getStringExtra(MainActivity.FRAGMENT_EXTRA_TITLE_TYPE)
                if (eventID != null && eventID > -1 && type != null) {
                    startFragments(eventID, type)
                }
            }
            intent = null
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
                        TournamentInstanceFragment.newInstance()
                    }
                }
                /*is EventWorldTournament -> {
                    if (type == MainActivity.FRAGMENT_TYPE_SHOW) {
                        ShowWorldTournamentEvent.newInstance()
                    } else {
                        WorldTournamentInstanceFragment.newInstance()
                    }
                }*/
                is EventTournamentResult -> {
                    if (type == MainActivity.FRAGMENT_TYPE_SHOW) {
                        ShowTournamentEvent.newInstance()
                    } else {
                        TournamentInstanceFragment.newInstance()
                    }
                }
                is EventUser -> {
                    ShowUserEvent.newInstance()
                }
                is OneTimeEvent -> {
                    if (type == MainActivity.FRAGMENT_TYPE_SHOW) {
                        ShowOneTimeEvent.newInstance()
                    } else {
                        OneTimeEventInstanceFragment.newInstance()
                    }
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun showPlaces(places: MutableList<out PlaceDTO>?) {
//todo такой эе метод в мэйн активити, сделать один чтобы был
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
//todo такой эе метод в мэйн активити, сделать один чтобы был
        IOHandler.clearSharedPrefEventData()
        worldTournamentsDataDTO.data?.europeanTournaments?.forEach {
            val event = EventWorldTournament(it.id!!.toInt(), EventDate.parseStringToDate(transformDate("yyyy-mm-dd", it.dateStart!!), DateFormat.DEFAULT, Locale.GERMAN), it.name!!)
            event.toursCount = it.numRound?.toInt()
            event.eventType = it.eventType
            event.finishDate = EventDate.parseStringToDate(transformDate("dd/mm/yyyy", it.dateEnd!!), DateFormat.DEFAULT, Locale.GERMAN)
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


    override fun showTopPlayersRating(topPlayersDTO: TopPlayersDTO?) {
        IOHandler.clearSharedPrefEventData()
        //    addMonthDivider(getMonthDivider(66666666, "Open"))//todo
        processTopPlayers(topPlayersDTO?.open)
        addMonthDivider(getMonthDivider(77777777, "Women"))
        processTopPlayers(topPlayersDTO?.women)
        //   addMonthDivider(getMonthDivider(88888888, "Juniors"))
        processTopPlayers(topPlayersDTO?.juniors)
        // addMonthDivider(getMonthDivider(99999999, "Girls"))
        processTopPlayers(topPlayersDTO?.girls)

        updateFragments()
    }


    private fun addMonthDivider(monthDivider: MonthDivider) {
        EventHandler.addEvent(
                monthDivider,
                context!!,
                true, false, true, false
        )
    }

    private fun getMonthDivider(id: Int, text: String): MonthDivider {
        val monthDivider = MonthDivider(Calendar.getInstance().time, text)
        monthDivider.eventID = id
        return monthDivider
    }

    private fun processTopPlayers(list: List<TopPlayerDTO>?) {
        list?.forEach {
            val fullName = buildFullName(it.name)
            val event = EventTopPlayer(it.id!!.toInt(), EventDate.parseStringToDate(transformDate("yyyy-mm-dd", it.birthday!!), DateFormat.DEFAULT, Locale.GERMAN), fullName?.get(0)!!, fullName[1])
            event.position = it.position?.toInt()
            event.rating = it.rating?.toInt()
            event.blitzRating = it.blitzRating?.toInt()
            event.rapidRating = it.rapidRating?.toInt()
            event.topType = it.topType
            event.country = it.country
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
}
