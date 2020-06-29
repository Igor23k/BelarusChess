package bobrchess.of.by.belaruschess.fragments


import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.adapter.EventAdapter
import bobrchess.of.by.belaruschess.adapter.RecycleViewItemDivider
import bobrchess.of.by.belaruschess.dto.*
import bobrchess.of.by.belaruschess.handler.BitmapHandler
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.handler.IOHandler
import bobrchess.of.by.belaruschess.model.EventDate
import bobrchess.of.by.belaruschess.model.EventTournament
import bobrchess.of.by.belaruschess.model.EventTournamentResult
import bobrchess.of.by.belaruschess.model.EventUser
import bobrchess.of.by.belaruschess.util.Util.Companion.transformDate
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_show_user_event.*
import org.springframework.util.StringUtils
import java.text.DateFormat
import java.util.*

/**
 * ShowUserEvent is a fragment to show all known data from a instance of EventUser
 *
 * TODO:
 * - add tiny animation for opening this fragment
 */
class ShowUserEvent : ShowEventFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: EventAdapter
    private var isFABOpen = false

    private var ranks: List<RankDTO>? = null
    private var places: List<PlaceDTO>? = null
    private var countries: List<CountryDTO>? = null
    private var userTournamentsResult: ArrayList<TournamentResultDTO>? = null
    private var coach: UserDTO? = null

    var placeItemsListType = object : TypeToken<List<PlaceDTO>>() {}.type
    var rankItemsListType = object : TypeToken<List<RankDTO>>() {}.type
    var countryItemsListType = object : TypeToken<List<CountryDTO>>() {}.type
    var userTournamentsResultItemsListType = object : TypeToken<List<TournamentResultDTO>>() {}.type
    var userItemsListType = object : TypeToken<UserDTO>() {}.type

    private fun showTournamentsResults() {
        IOHandler.clearSharedPrefEventData()
        EventHandler.clearData()
        var id = 0
        userTournamentsResult?.forEach {
            val event = EventTournamentResult(id++, EventDate.parseStringToDate(transformDate("dd/mm/yyyy", it.startDate!!), DateFormat.DEFAULT, Locale.GERMAN), it.name!!)
            event.position = it.position
            event.points = it.points
            event.imageUri = it.image
            EventHandler.addEvent(
                    event,
                    context!!,
                    writeAfterAdd = true,
                    addNewNotification = false,
                    updateEventList = true,
                    addBitmap = false
            )
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        places = Gson().fromJson(arguments?.getString("places"), placeItemsListType)
        ranks = Gson().fromJson(arguments?.getString("ranks"), rankItemsListType)
        countries = Gson().fromJson(arguments?.getString("countries"), countryItemsListType)
        userTournamentsResult = Gson().fromJson(arguments?.getString("tournamentsResult"), userTournamentsResultItemsListType)//todo удалить
        coach = Gson().fromJson(arguments?.getString("coach"), userItemsListType)
        (context as MainActivity).unlockAppBar()
        return inflater.inflate(R.layout.fragment_show_user_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        showTournamentsResults()
    }

    private fun init() {
        viewManager = LinearLayoutManager(view!!.context)
        viewAdapter = EventAdapter(view!!.context, this.fragmentManager!!, places, ranks, countries, null, userTournamentsResult)
        recyclerView = view!!.findViewById<RecyclerView>(R.id.recyclerViewTournamentsResults).apply {
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

    /**
     * updateUI updates all TextViews and other views to the current instance(Anniversary, Birthday) data
     */

    override fun updateUI() {
        (context as MainActivity).scrollable_toolbar.isTitleEnabled = true
        EventHandler.getEventToEventIndex(eventID)?.let { userEvent ->
            if (userEvent is EventUser) {
                var rank = userEvent.rankId?.minus(1)?.let { ranks?.get(it)?.name }
                if (StringUtils.isEmpty(rank)) {
                    rank = resources.getString(R.string.rank_absence)
                }
                val country = userEvent.countryId?.minus(1)?.let { countries?.get(it)?.name }

                this.user_country_and_rank_and_rating.text = "$country, $rank, ${userEvent.rating}"


                this.user_coach.visibility = TextView.VISIBLE
                if (coach != null) {
                    this.user_coach.text = resources.getString(R.string.coach)+ "   ${coach?.name} ${coach?.surname}"
                } else {
                    this.user_coach.text = resources.getString(R.string.coach_absence)
                }

                var scrollRange = -1
                (context as MainActivity).app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appbarLayout, verticalOffset ->
                    if (scrollRange == -1) {
                        scrollRange = appbarLayout.totalScrollRange
                    }
                    if (context != null) {
                        if (scrollRange + verticalOffset == 0) {
                            setToolbarTitle(context!!.resources.getString(R.string.app_name))
                        } else {
                            setToolbarTitle(userEvent.name + " " + userEvent.surname)
                        }
                    }
                })

                //only set expanded title color to white, when background is not white, background is white when no avatar image is set
                if (userEvent.imageUri != null) {
                    (context as MainActivity).scrollable_toolbar.setExpandedTitleColor(
                            ContextCompat.getColor(
                                    context!!,
                                    R.color.white
                            )
                    )
                } else {
                    (context as MainActivity).scrollable_toolbar.setExpandedTitleColor(
                            ContextCompat.getColor(
                                    context!!,
                                    R.color.darkGrey
                            )
                    )
                }

                val date: String
                date = userEvent.dateToPrettyString(DateFormat.LONG)

                userTournamentsResult?.forEach{
                    // user_tournaments_results.text = "Name - " + it.name + " Points - " + it.points +  " Position - " + it.position
                }

                user_birthday.text = resources.getString(R.string.person_show_date, date)
                updateAvatarImage()
            }
        }
    }

    override fun onDetach() {
        closeExpandableToolbar()
        super.onDetach()
    }

    private fun updateAvatarImage() {
        if (this.iv_avatar != null && this.eventID >= 0 && (context as MainActivity).collapsable_toolbar_iv != null) {
            val bitmap = BitmapHandler.getBitmapFromFile(context!!, this.eventID)
            setBitmapToToolbar(bitmap)
        }
    }

    private fun setBitmapToToolbar(bitmap: Bitmap?) {
        (context as MainActivity).collapsable_toolbar_iv.visibility = ImageView.VISIBLE
        if (bitmap != null) {
            (context as MainActivity).collapsable_toolbar_iv.setImageBitmap(bitmap)
            (context as MainActivity).collapsable_toolbar_iv.scaleType =
                    ImageView.ScaleType.CENTER_CROP
            (context as MainActivity).app_bar.setExpanded(true, true)
        } else {
            (context as MainActivity).app_bar.setExpanded(false, false)
            (context as MainActivity).collapsable_toolbar_iv.scaleType =
                    ImageView.ScaleType.FIT_CENTER
            (context as MainActivity).collapsable_toolbar_iv.setImageResource(R.drawable.ic_birthday_person)
        }
    }

    private fun closeExpandableToolbar() {
        setToolbarTitle(context!!.resources.getString(R.string.app_name))
        (context as MainActivity).collapsable_toolbar_iv.visibility = ImageView.GONE
        (context as MainActivity).lockAppbar()
    }

    /**
     * shareEvent a function which is called after the share button has been pressed
     * It provides a simple intent to share data as plain text in other apps
     */
    override fun shareEvent() {
        EventHandler.getEventToEventIndex(eventID)?.let { birthday ->
            if (birthday is EventTournament) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                var shareBirthdayMsg =
                        if (birthday.fullDescription != null) {
                            context!!.resources.getString(
                                    R.string.share_tournament_name,
                                    "${birthday.name} \"${birthday.fullDescription}\" ${birthday.shortDescription}"
                            )
                        } else if (birthday.shortDescription != null) {
                            context!!.resources.getString(
                                    R.string.share_tournament_name,
                                    "${birthday.name} ${birthday.shortDescription}"
                            )
                        } else {
                            context!!.resources.getString(
                                    R.string.share_tournament_name,
                                    birthday.name
                            )
                        }

                //   if (birthday.isYearGiven) {
                //startDate person was born
                shareBirthdayMsg += "\n" + context!!.resources.getString(
                        R.string.share_tournament_date_start,
                        EventDate.parseDateToString(birthday.eventDate, DateFormat.FULL)
                )
                //      }

                //next birthday
                shareBirthdayMsg += "\n" + context!!.resources.getString(
                        R.string.share_tournament_date_next,
                        EventDate.parseDateToString(
                                EventDate.dateToCurrentTimeContext(birthday.eventDate),
                                DateFormat.FULL
                        )
                )

                val daysUntil = birthday.getDaysUntil()
                shareBirthdayMsg += if (daysUntil == 0) {
                    //today
                    "\n" + context!!.resources.getString(
                            R.string.share_tournament_days_today
                    )
                } else {
                    // in X days
                    "\n" + context!!.resources.getQuantityString(
                            R.plurals.share_tournament_days,
                            daysUntil,
                            daysUntil
                    )
                }

                // if (birthday.isYearGiven) {
                //person will be years old
                shareBirthdayMsg += "\n" + context!!.resources.getQuantityString(
                        R.plurals.person_years_old,
                        birthday.getYearsSince() + 1,
                        birthday.name,
                        birthday.getYearsSince() + 1
                )
                //}

                intent.putExtra(Intent.EXTRA_TEXT, shareBirthdayMsg)
                startActivity(
                        Intent.createChooser(
                                intent,
                                resources.getString(R.string.intent_share_chooser_title)
                        )
                )
            }
        }
    }

    override fun editEvent() {//todo удалить кнопку когда показывается пользователь (и плэйс для не админов)

    }

    companion object {
        /**
         * newInstance returns a new instance of EventTournament
         */
        @JvmStatic
        fun newInstance(): ShowUserEvent {
            return ShowUserEvent()
        }
    }
}
