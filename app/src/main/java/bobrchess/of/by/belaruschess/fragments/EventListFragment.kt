package com.procrastimax.birthdaybuddy.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.fragments.HelpFragment
import bobrchess.of.by.belaruschess.fragments.ShowTournamentEvent
import bobrchess.of.by.belaruschess.handler.BitmapHandler
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.handler.IOHandler
import bobrchess.of.by.belaruschess.model.EventDate
import bobrchess.of.by.belaruschess.presenter.SearchTournamentPresenter
import bobrchess.of.by.belaruschess.presenter.impl.SearchTournamentPresenterImpl
import bobrchess.of.by.belaruschess.view.activity.SearchTournamentContractView
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import com.procrastimax.birthdaybuddy.models.EventTournament
import com.procrastimax.birthdaybuddy.models.OneTimeEvent
import com.procrastimax.birthdaybuddy.views.EventAdapter
import com.procrastimax.birthdaybuddy.views.RecycleViewItemDivider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_event_list.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class EventListFragment : Fragment(), SearchTournamentContractView {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: EventAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var isFABOpen = false

    private var searchTournamentPresenter: SearchTournamentPresenter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (context as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (context as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(false)

        (context as MainActivity).scrollable_toolbar.isTitleEnabled = false
        (context as MainActivity).toolbar.title = getString(R.string.app_name)

        isFABOpen = false

        fab_layout_add_annual_event.visibility = ConstraintLayout.INVISIBLE
        fab_layout_add_birthday.visibility = ConstraintLayout.INVISIBLE
        fab_layout_add_one_time.visibility = ConstraintLayout.INVISIBLE

        viewManager = LinearLayoutManager(view.context)
        viewAdapter = EventAdapter(view.context, this.fragmentManager!!)

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            scrollToPosition(traverseForFirstMonthEntry())
        }
        recyclerView.addItemDecoration(RecycleViewItemDivider(view.context))
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

        fab_add_birthday.setOnClickListener {
            closeFABMenu(true)
            val ft = fragmentManager!!.beginTransaction()
            ft.replace(
                R.id.fragment_placeholder,
                TournamentInstanceFragment.newInstance()
            )
            ft.addToBackStack(null)
            ft.commit()
        }

        fab_add_annual_event.setOnClickListener {
            closeFABMenu(true)
            val ft = fragmentManager!!.beginTransaction()
            ft.replace(
                R.id.fragment_placeholder,
                AnnualEventInstanceFragment.newInstance()
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

        searchTournamentPresenter = SearchTournamentPresenterImpl()
        searchTournamentPresenter!!.attachView(this)
        searchTournamentPresenter!!.viewIsReady()
    }

    override fun onResume() {
        super.onResume()
        //when no items except of the 12 month items are in the event list, then display text message
        if (EventHandler.getList().size - 12 == 0) {
            tv_no_events.visibility = TextView.VISIBLE
        } else {
            tv_no_events.visibility = TextView.GONE
        }
    }

    private fun showFABMenu() {
        isFABOpen = true
        fab_show_fab_menu.isClickable = false
        //show layouts
        fab_layout_add_annual_event.visibility = ConstraintLayout.VISIBLE
        fab_layout_add_birthday.visibility = ConstraintLayout.VISIBLE
        fab_layout_add_one_time.visibility = ConstraintLayout.VISIBLE

        this.recyclerView.animate().alpha(0.15f).apply {
            duration = 175
        }

        //move layouts
        //move add birthday layout up
        fab_layout_add_birthday.animate()
            .translationYBy(-resources.getDimension(R.dimen.standard_55) - 20).apply {
                duration = 100
            }.withEndAction {
                fab_layout_add_birthday.animate().translationYBy(20.toFloat()).apply {
                    duration = 75
                }
            }

        //move add annual event layout up
        fab_layout_add_annual_event.animate()
            .translationYBy(-resources.getDimension(R.dimen.standard_105) - 40)
            .apply {
                duration = 100
            }.withEndAction {
                fab_layout_add_annual_event.animate().translationYBy(40.toFloat()).apply {
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
        fab_layout_add_birthday.animate()
            .translationYBy(resources.getDimension(R.dimen.standard_55))
            .withEndAction {
                if (!immediateAction) {
                    fab_layout_add_birthday.visibility = ConstraintLayout.INVISIBLE
                }
            }

        //move add annual event layout down
        fab_layout_add_annual_event.animate()
            .translationYBy(resources.getDimension(R.dimen.standard_105))
            .withEndAction {
                if (!immediateAction) {
                    fab_layout_add_annual_event.visibility = ConstraintLayout.INVISIBLE
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

            }
            R.id.action_refresh -> {
                searchTournamentPresenter?.loadTournaments()
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

    override fun showAlertDialog(title: Int, message: Int, buttonText: Int, cancelable: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissAlertDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showToast(resId: Int?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showToast(message: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setConnectionStatus(connectivityStatus: Int?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTournaments(tournaments: List<TournamentDTO>) {
        //разделить тут по методам, вынести отдалельно
        IOHandler.clearSharedPrefEventData()
        tournaments.forEach {
            val event = EventTournament(it.id.toInt(), EventDate.parseStringToDate(transformDate(it.startDate)!!, DateFormat.DEFAULT, Locale.GERMAN), it.name!!)
            event.name = it.name!!
            event.fullDescription = it.fullDescription!!
            event.shortDescription = it.shortDescription!!
            event.imageUri = it.image!!
            event.finishDate = EventDate.parseStringToDate(transformDate(it.finishDate)!!, DateFormat.DEFAULT, Locale.GERMAN)
            EventHandler.addEvent(
                    event,
                    this.context!!,
                    writeAfterAdd = true,
                    addNewNotification = false,
                    updateEventList = true,
                    addBitmap = false
            )
        }
        updateTournamentFragments();
    }

    private fun updateTournamentFragments(){
        val transaction = this.activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(
                R.id.fragment_placeholder,
                EventListFragment.newInstance()
        )?.commit()


        //start loading bitmap drawables in other thread to not block ui
        Thread(Runnable
        {
            BitmapHandler.loadAllBitmaps(this.context!!)
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
                /* is AnnualEvent -> {
                     if (type == FRAGMENT_TYPE_SHOW) {
                         ShowAnnualEvent.newInstance()
                     } else {
                         AnnualEventInstanceFragment.newInstance()
                     }
                 }*/
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

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
