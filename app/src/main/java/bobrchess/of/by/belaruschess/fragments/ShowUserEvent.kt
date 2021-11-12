package bobrchess.of.by.belaruschess.fragments


import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.CountryDTO
import bobrchess.of.by.belaruschess.dto.PlaceDTO
import bobrchess.of.by.belaruschess.dto.RankDTO
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.model.EventDate
import bobrchess.of.by.belaruschess.model.EventUser
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_show_user_event.*
import org.springframework.util.StringUtils
import java.text.DateFormat
import java.util.*

class ShowUserEvent : ShowEventFragment() {

    /*private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: EventAdapter*/

    private var ranks: List<RankDTO>? = null
    private var places: List<PlaceDTO>? = null
    private var countries: List<CountryDTO>? = null

    //private var userTournamentsResult: ArrayList<TournamentResultDTO>? = null
    //private var coach: UserDTO? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val activity: MainActivity? = activity as MainActivity?
        places = activity!!.getPlaces()
        ranks = activity.getRanks()
        countries = activity.getCountries()

        //userTournamentsResult = Gson().fromJson(arguments?.getString(TOURNAMENTS_RESULT), userTournamentsResultItemsListType
        //coach = Gson().fromJson(arguments?.getString(COACH), userItemsListType)
        (context as MainActivity).unlockAppBar()
        return inflater.inflate(R.layout.fragment_show_user_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        //showTournamentsResults()
    }

    private fun init() {
        /*viewManager = LinearLayoutManager(view!!.context)
        viewAdapter = EventAdapter(view!!.context, this.fragmentManager!!, places, ranks, countries, null)
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
        )*/
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
                val rank = ranks?.find { it.id == userEvent.rankId}
                var rankName = rank?.name

                if (StringUtils.isEmpty(rankName)) {
                    rankName = resources.getString(R.string.rank_absence)
                }

                val country = countries?.find { it.id == userEvent.countryId}
                var countryName = country?.name

                if (StringUtils.isEmpty(countryName)) {
                    countryName = resources.getString(R.string.country_absence)
                }

                this.user_country_and_rank_and_rating.text = "$countryName, $rankName, ${userEvent.rating}"


                this.user_coach.visibility = TextView.VISIBLE
                if (userEvent.coach != null) {
                    this.user_coach.text = resources.getString(R.string.coach) + "   ${userEvent.coach}"
                } else {
                    this.user_coach.text = resources.getString(R.string.coach_absence)
                }

                var scrollRange = -1
                (context as MainActivity).app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appbarLayout, verticalOffset ->
                    if (scrollRange == -1) {
                        scrollRange = appbarLayout.totalScrollRange
                    }
                    if (context != null) {
                       // if (scrollRange + verticalOffset == 0) {
                            //setToolbarTitle(context!!.resources.getString(R.string.app_name))
                      //  } else {
                            setToolbarTitle(userEvent.name + " " + userEvent.surname)
                     //   }
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

                /*userTournamentsResult?.forEach {
                    // user_tournaments_results.text = "Name - " + it.name + " Points - " + it.points +  " Position - " + it.position
                }*/

                user_birthday.text = resources.getString(R.string.person_show_date, date)
                updateAvatarImage(userEvent.imageUri)
            }
        }
    }

    override fun onDetach() {
        closeExpandableToolbar()
        super.onDetach()
    }

    private fun updateAvatarImage(image: String?) {
        if (this.iv_avatar != null && this.eventID >= 0 && (context as MainActivity).collapsable_toolbar_iv != null) {
            val bitmap = Util.getBitMapByBase64(image)
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
        EventHandler.getEventToEventIndex(eventID)?.let { user ->
            if (user is EventUser) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                var shareUserMsg = user.name + " " + user.surname + " " + user.patronymic
                shareUserMsg += "\n" + resources.getString(R.string.birthday) + ": " + EventDate.parseDateToString(user.birthday, DateFormat.FULL)
                shareUserMsg += "\n" + resources.getString(R.string.rating) + ": " + user.rating

                val rankId = user.rankId
                if (rankId != null) {
                    shareUserMsg += "\n" + resources.getString(R.string.rank) + ": " + ranks?.find { it.id!! == rankId }?.name
                }

                val countryId = user.countryId
                if (countryId != null) {
                    shareUserMsg += "\n" + resources.getString(R.string.country) + ": " + countries?.find { it.id!! == countryId }?.name
                }//проверить что все ок выводится. например, coach нету тут и тд.

                intent.putExtra(Intent.EXTRA_TEXT, shareUserMsg)
                startActivity(
                        Intent.createChooser(
                                intent,
                                resources.getString(R.string.intent_share_chooser_title)
                        )
                )
            }
        }
    }

    /*private fun showTournamentsResults() {//вернуть, когда будет участие в турнирах реализовано
        //IOHandler.clearSharedPrefEventData()
        //EventHandler.clearData()
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
    }*/

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.toolbar_show_event_lite, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun editEvent() {}

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
