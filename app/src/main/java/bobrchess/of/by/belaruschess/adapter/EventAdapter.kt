package bobrchess.of.by.belaruschess.adapter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.*
import bobrchess.of.by.belaruschess.dto.externalFide.TopPlayerWithImageDTO
import bobrchess.of.by.belaruschess.dto.externalFide.WorldTournamentDTO
import bobrchess.of.by.belaruschess.fragments.*
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.model.*
import bobrchess.of.by.belaruschess.presenter.FideApiAdapterPresenter
import bobrchess.of.by.belaruschess.presenter.impl.FideApiAdapterPresenterImpl
import bobrchess.of.by.belaruschess.presenter.impl.TournamentsResultPresenterImpl
import bobrchess.of.by.belaruschess.util.Util.Companion.getScaledBitMapByBase64
import bobrchess.of.by.belaruschess.view.activity.TournamentsResultContractView
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.event_month_view_divider.view.*
import kotlinx.android.synthetic.main.place_event_item_view.view.*
import kotlinx.android.synthetic.main.top_player_rating_item_view.view.*
import kotlinx.android.synthetic.main.tournament_event_item_view.view.*
import kotlinx.android.synthetic.main.tournament_event_item_view.view.constraint_layout_tournament_item_view
import kotlinx.android.synthetic.main.tournament_event_item_view.view.tournament_event_item_city_value
import kotlinx.android.synthetic.main.tournament_result_event_item_view.view.*
import kotlinx.android.synthetic.main.user_event_item_view.view.*
import kotlinx.android.synthetic.main.world_tournament_event_item_view.view.*
import org.springframework.util.StringUtils


class EventAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>(), TournamentsResultContractView, FideApiAdapterContract {

    private var tournamentsResultPresenterImpl: TournamentsResultPresenterImpl? = null
    private var context: Context? = null
    private var fragmentManager: FragmentManager? = null
    private var places: List<PlaceDTO>? = null
    private var ranks: List<RankDTO>? = null
    private var countries: List<CountryDTO>? = null
    private var userTournamentsResult: List<TournamentResultDTO>? = null
    private var users: List<UserDTO>? = null
    private var fideApiAdapterPresenter: FideApiAdapterPresenter? = null

    constructor(context: Context, fragmentManager: FragmentManager, places: List<PlaceDTO>?, ranks: List<RankDTO>?, countries: List<CountryDTO>?, users: List<UserDTO>?) : this() {
        tournamentsResultPresenterImpl = TournamentsResultPresenterImpl()
        tournamentsResultPresenterImpl!!.attachView(this)

        fideApiAdapterPresenter = FideApiAdapterPresenterImpl()
        fideApiAdapterPresenter!!.attachView(this)
        fideApiAdapterPresenter!!.viewIsReady()

        this.context = context
        this.fragmentManager = fragmentManager
        this.places = places
        this.ranks = ranks
        this.countries = countries
        this.users = users
    }

    var isClickable: Boolean = true

    class UserEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class TopPlayerRatingEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class TournamentEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class EventMonthDividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class TournamentResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class OneTimeEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class PlaceEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class WorldTournamentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * getItemViewType overrides the standard function
     * it defines the different viewholder types used for the recycler view
     *
     * @param position: Int
     * @return Int
     */

    override fun getItemViewType(position: Int): Int {
        when (EventHandler.getList()[position]) {
            is Divider -> {
                /* if (position < EventHandler.getList().size - 1) {
                     if (EventHandler.getList()[position + 1] !is MonthDivider) {
                         return 0
                     }
                 }
                 return -1*/

                return 0;
            }
            is EventTournament -> {
                return 1
            }
            is EventTournamentResult -> {
                return 2;
            }
            is OneTimeEvent -> {
                return 3
            }
            is EventUser -> {
                return 4
            }
            is EventPlace -> {
                return 5;
            }
            is EventTopPlayer -> {
                return 6;
            }
            is EventWorldTournament -> {
                return 7;
            }
        }
        return -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // create a new view
        when (viewType) {
            0 -> {
                val itemView =
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.event_month_view_divider, parent, false)
                return EventMonthDividerViewHolder(itemView)
            }
            1 -> {
                val itemView =
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.tournament_event_item_view, parent, false)
                return TournamentEventViewHolder(itemView)
            }
            2 -> {
                val itemView =
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.tournament_result_event_item_view, parent, false)
                return TournamentResultViewHolder(itemView)
            }
            3 -> {
                val itemView =
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.one_time_event_item_view, parent, false)
                return OneTimeEventViewHolder(itemView)
            }
            4 -> {
                val itemView =
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.user_event_item_view, parent, false)
                return UserEventViewHolder(itemView)
            }
            5 -> {
                val itemView =
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.place_event_item_view, parent, false)
                return PlaceEventViewHolder(itemView)
            }
            6 -> {
                val itemView =
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.top_player_rating_item_view, parent, false)
                return TopPlayerRatingEventViewHolder(itemView)
            }
            7 -> {
                val itemView =
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.world_tournament_event_item_view, parent, false)
                return WorldTournamentViewHolder(itemView)
            }
            else -> {
                val itemView = View(context)
                return EventMonthDividerViewHolder(itemView)
            }
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // - get element from dataset at this position
        // - replace the contents of the view with that element
        when (holder.itemViewType) {

            //EventMonthDividerViewHolder
            0 -> {
                EventHandler.getList()[position].let { monthDivider ->
                    if (monthDivider is Divider) {
                        holder.itemView.tv_divider_description_month.text =
                                monthDivider.text
                    }
                }
            }

            1 -> {
                EventHandler.getList()[position].let { event ->
                    if (event is EventTournament) {
                        //set on click listener for item
                        holder.itemView.setOnClickListener {
                            if (isClickable) {
                                val bundle = Bundle()
                                //do this in more adaptable way
                                bundle.putInt(
                                        MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
                                        event.eventID
                                )

                                val ft = fragmentManager!!.beginTransaction()
                                // add arguments to fragment
                                val newFragment = ShowTournamentEvent.newInstance()
                                newFragment.arguments = bundle
                                ft.replace(
                                        R.id.fragment_placeholder,
                                        newFragment
                                )
                                ft.addToBackStack(null)
                                ft.commit()
                            }
                        }

                        holder.itemView.setOnLongClickListener {
                            if (isClickable) {
                                val bundle = Bundle()
                                //do this in more adaptable way
                                bundle.putInt(
                                        MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
                                        event.eventID
                                )
                                val ft = fragmentManager!!.beginTransaction()
                                // add arguments to fragment
                                val newTournamentFragment = EditTournamentInstanceFragment.newInstance()
                                newTournamentFragment.arguments = bundle
                                ft.replace(
                                        R.id.fragment_placeholder,
                                        newTournamentFragment
                                )
                                ft.addToBackStack(null)
                                ft.commit()
                            }
                            true
                        }

                        val textColor: Int


                        //textColor = ContextCompat.getColor(context!!, R.color.colorAccent)
                        textColor = ContextCompat.getColor(context!!, R.color.textDark)
                        //holder.itemView.tournament_event_item_country_value.text = event.toursCount.toString()
                        //holder.itemView.tournament_event_item_country_value.setTextColor(textColor)

                        //set startDate
                        holder.itemView.tournament_event_item_date_value.text = event.getPrettyShortStringWithoutYear()
                        holder.itemView.tournament_event_item_date_value.setTextColor(textColor)


                        val place = places?.find { it.id == event.placeId}
                        var city =  place?.city
                        if (StringUtils.isEmpty(city)) {
                            city = "-"
                        }
                        holder.itemView.tournament_event_item_city_value.text = city
                        holder.itemView.tournament_event_item_city_value.setTextColor(textColor)

                      /*  if (event.eventAlreadyOccurred()) {
                            holder.itemView.constraint_layout_tournament_item_view.background =
                                    ContextCompat.getDrawable(
                                            context!!,
                                            R.drawable.ripple_recycler_view_item_dark
                                    )
                        } else {*/
                            holder.itemView.constraint_layout_tournament_item_view.background =
                                    ContextCompat.getDrawable(
                                            context!!,
                                            R.drawable.ripple_recycler_view_item
                                    )
                      //  }

                        //set fullDescription TextView visible
                        holder.itemView.tournament_event_item_name.visibility =
                                TextView.VISIBLE
                        holder.itemView.tournament_event_item_name.setTextColor(textColor)

                        //set fullDescription TextView text
                        holder.itemView.tournament_event_item_name.text = event.name

                        val image = event.imageUri

                        //when context is MainActivity
                        if (context is MainActivity) {
                            if (image != null) {
                                val bitmap = getScaledBitMapByBase64(image, context!!.resources)
                                holder.itemView.iv_tournament_event_item_image.setImageBitmap(
                                        bitmap
                                )
                            } else {
                                holder.itemView.iv_tournament_event_item_image.setImageResource(R.drawable.ic_birthday_person)
                            }
                        }
                    }
                }
            }


            4 -> {
                EventHandler.getList()[position].let { user ->
                    if (user is EventUser) {
                        //set on click listener for item
                        holder.itemView.setOnClickListener {
                            if (isClickable) {
                                eventId = user.eventID
                                coach = user.coach
                                startUserFragment()
                                //tournamentsResultPresenterImpl?.loadUserTournamentsResults(user.eventID)
                            }
                        }

                        val textColor: Int

                        //set days until

                        //  textColor = ContextCompat.getColor(context, R.color.colorAccent) // если нужно выделить цветом

                        textColor = ContextCompat.getColor(context!!, R.color.textDark)

                        val rank = ranks?.find { it.id == user.rankId}
                        var rankName = rank?.name
                        if (StringUtils.isEmpty(rankName)) {
                            rankName = "-"
                        }
                        holder.itemView.user_rank.text = rankName
                        holder.itemView.user_rank.setTextColor(textColor)

                        //set startDate
                        holder.itemView.user_birthday.text = user.getPrettyShortStringWithYear()
                        holder.itemView.user_birthday.setTextColor(textColor)

                        //set years since, if specified
                        // if (user.isYearGiven) {
                        holder.itemView.user_rating.text = (user.rating).toString()
                        /*  } else {
                              holder.itemView.tv_birthday_event_item_years_since_value.text = "-"
                          }*/
                        holder.itemView.user_rating.setTextColor(
                                textColor
                        )

                       // if (user.eventAlreadyOccurred()) {
                          /*  holder.itemView.constraint_layout_tournament_item_view.background =
                                    ContextCompat.getDrawable(
                                            context!!,
                                            R.drawable.ripple_recycler_view_item_dark
                                    )
                        } else {*/
                            holder.itemView.constraint_layout_tournament_item_view.background =
                                    ContextCompat.getDrawable(
                                            context!!,
                                            R.drawable.ripple_recycler_view_item
                                    )
                       // }

                        //set forename and shortDescription invisible
                        holder.itemView.tv_birthday_event_item_forename.visibility =
                                TextView.GONE
                        holder.itemView.tv_birthday_event_item_surname.visibility =
                                TextView.GONE

                        holder.itemView.user_event_item_name.visibility =
                                TextView.VISIBLE
                        holder.itemView.user_event_item_name.setTextColor(textColor)

                        holder.itemView.user_event_item_name.text = user.name + " " + user.surname

                        val image = user.imageUri

                        //when context is MainActivity
                        if (context is MainActivity) {
                            if (image != null) {
                                holder.itemView.iv_user_event_item_image.setImageBitmap(
                                        getScaledBitMapByBase64(image, context!!.resources)
                                )
                            } else {
                                holder.itemView.iv_user_event_item_image.setImageResource(R.drawable.ic_birthday_person)
                            }
                        }
                    }
                }
            }

            2 -> {
                EventHandler.getList()[position].let { eventTournamentResult ->
                    if (eventTournamentResult is EventTournamentResult) {
                        //set on click listener for item
                        holder.itemView.setOnClickListener {
                            if (isClickable) {
                                if (isClickable) {
                                    val bundle = Bundle()
                                    //do this in more adaptable way
                                    bundle.putInt(
                                            MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
                                            eventTournamentResult.eventID
                                    )
                                    val ft = fragmentManager!!.beginTransaction()
                                    // add arguments to fragment
                                    val newFragment = ShowTournamentEvent.newInstance()
                                    newFragment.arguments = bundle
                                    ft.replace(
                                            R.id.fragment_placeholder,
                                            newFragment
                                    )
                                    ft.addToBackStack(null)
                                    ft.commit()
                                }
                            }
                        }

                        val textColor: Int

                        textColor = ContextCompat.getColor(context!!, R.color.textDark)

                        holder.itemView.tournament_result_position.text = eventTournamentResult.position.toString()
                        holder.itemView.tournament_result_position.setTextColor(textColor)

                        holder.itemView.tournament_result_start_date.text = eventTournamentResult.getPrettyShortStringWithYear()
                        holder.itemView.tournament_result_start_date.setTextColor(textColor)

                        holder.itemView.tournament_result_points.text = eventTournamentResult.points.toString()
                        holder.itemView.tournament_result_points.setTextColor(
                                textColor
                        )

                       /* if (eventTournamentResult.eventAlreadyOccurred()) {
                            holder.itemView.constraint_layout_tournament_item_view.background =
                                    ContextCompat.getDrawable(
                                            context!!,
                                            R.drawable.ripple_recycler_view_item_dark
                                    )
                        } else {*/
                            holder.itemView.constraint_layout_tournament_item_view.background =
                                    ContextCompat.getDrawable(
                                            context!!,
                                            R.drawable.ripple_recycler_view_item
                                    )
                    //    }

                        holder.itemView.tournament_result_name.visibility = TextView.VISIBLE
                        holder.itemView.tournament_result_name.text = eventTournamentResult.name
                        holder.itemView.tournament_result_points.visibility = TextView.VISIBLE
                        holder.itemView.tournament_result_points.text = eventTournamentResult.points.toString()
                        holder.itemView.tournament_result_position.visibility = TextView.VISIBLE
                        holder.itemView.tournament_result_position.text = eventTournamentResult.position.toString()

                        val image = eventTournamentResult.imageUri

                        //when context is MainActivity
                        if (context is MainActivity) {
                            if (image != null) {
                                holder.itemView.iv_tournament_result_event_item_image.setImageBitmap(
                                        getScaledBitMapByBase64(image, context!!.resources)

                                )
                            } else {
                                holder.itemView.iv_tournament_result_event_item_image.setImageResource(R.drawable.ic_birthday_person)
                            }
                        }
                    }
                }
            }

            5 -> {
                EventHandler.getList()[position].let { eventPlace ->
                    if (eventPlace is EventPlace) {
                        //set on click listener for item
                        holder.itemView.setOnClickListener {
                            if (isClickable) {
                                val bundle = Bundle()
                                //do this in more adaptable way
                                bundle.putInt(
                                        MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
                                        eventPlace.eventID
                                )

                                val ft = fragmentManager!!.beginTransaction()
                                // add arguments to fragment
                                val fragment = ShowPlaceEvent.newInstance()
                                fragment.arguments = bundle
                                ft.replace(
                                        R.id.fragment_placeholder,
                                        fragment
                                )
                                ft.addToBackStack(null)
                                ft.commit()
                            }
                        }

                        val textColor: Int

                        textColor = ContextCompat.getColor(context!!, R.color.textDark)

                        holder.itemView.tv_place_event_item_name.visibility = TextView.VISIBLE
                        holder.itemView.tv_place_event_item_name.text = eventPlace.name

                        val country = countries?.find { it.id == eventPlace.countryId}
                        var countryName = country?.name
                        if (StringUtils.isEmpty(countryName)) {
                            countryName = "-"
                        }
                      /*  holder.itemView.place_event_item_country_value.text = countryName
                        holder.itemView.place_event_item_country_value.setTextColor(textColor)*/

                        holder.itemView.tournament_event_item_city_value.text = eventPlace.city
                        holder.itemView.tournament_event_item_city_value.setTextColor(textColor)

                        holder.itemView.place_event_item_capacity_value.text = eventPlace.capacity.toString()
                        holder.itemView.place_event_item_capacity_value.setTextColor(
                                textColor
                        )

                        if (eventPlace.eventAlreadyOccurred()) {
                            holder.itemView.constraint_layout_place_item_view.background =
                                    ContextCompat.getDrawable(
                                            context!!,
                                            R.drawable.ripple_recycler_view_item_dark
                                    )
                        } else {
                            holder.itemView.constraint_layout_place_item_view.background =
                                    ContextCompat.getDrawable(
                                            context!!,
                                            R.drawable.ripple_recycler_view_item
                                    )
                        }
                        val image = eventPlace.imageUri

                        //when context is MainActivity
                        if (context is MainActivity) {
                            if (image != null) {
                                holder.itemView.iv_place_event_item_image.setImageBitmap(
                                        getScaledBitMapByBase64(image, context!!.resources)
                                )
                            } else {
                                holder.itemView.iv_place_event_item_image.setImageResource(R.drawable.ic_birthday_person)
                            }
                        }
                    }
                }
            }

            6 -> {
                EventHandler.getList()[position].let { topPlayer ->
                    if (topPlayer is EventTopPlayer) {
                        //set on click listener for item
                        holder.itemView.setOnClickListener {
                            if (isClickable) {
                                fideApiAdapterPresenter!!.loadTopPlayer(topPlayer.externalId!!)
                            }
                        }

                        val textColor: Int


                        textColor = ContextCompat.getColor(context!!, R.color.textDark)

                        //set name
                        holder.itemView.top_player_event_item_name.text = "${topPlayer.position}. ${topPlayer.name}\n ${topPlayer.surname}"
                        holder.itemView.top_player_event_item_name.setTextColor(textColor)


                        //set birthday
                        holder.itemView.top_player_birthday.text = topPlayer.getPrettyShortStringWithYear()
                        holder.itemView.top_player_birthday.setTextColor(textColor)

                        //set country
                        holder.itemView.top_player_country.text = topPlayer.country
                        holder.itemView.top_player_country.setTextColor(textColor)

                        //rating
                        holder.itemView.top_player_rating.text = topPlayer.rating.toString()
                        holder.itemView.top_player_rating.setTextColor(textColor)
                    }
                }
            }
            7 -> {
                EventHandler.getList()[position].let { worldTournament ->
                    if (worldTournament is EventWorldTournament) {
                        //set on click listener for item
                        holder.itemView.setOnClickListener {
                            //set on click listener for item
                            if (isClickable) {
                                fideApiAdapterPresenter!!.loadWorldTournament(worldTournament.eventID)
                            }

                        }

                        val textColor: Int

                        textColor = ContextCompat.getColor(context!!, R.color.textDark)

                        //val avatarUri = worldTournament.imageUri

                        //set name
                        holder.itemView.world_tournament_event_item_name.text = worldTournament.name
                        holder.itemView.world_tournament_event_item_name.setTextColor(textColor)

                        //set startDate
                        holder.itemView.world_tournament_event_item_date_value.text = worldTournament.getPrettyShortStringWithYear()
                        holder.itemView.world_tournament_event_item_date_value.setTextColor(textColor)

                        //set city
                        holder.itemView.world_tournament_event_item_place_value.text = worldTournament.country + ", " + worldTournament.city
                        holder.itemView.world_tournament_event_item_place_value.setTextColor(textColor)
                    }
                }
            }
        }
    }

    private fun eventAdapterTopPlayerItemOpen(topPlayerDTO: TopPlayerWithImageDTO?) {
        val bundle = Bundle()

        val gson = GsonBuilder().setPrettyPrinting().create()
        val topPlayer = gson.toJson(topPlayerDTO)

        bundle.putString(
                "topPlayer",
                topPlayer
        )

        //do this in more adaptable way
        topPlayerDTO?.idNumber?.toInt()?.let {
            bundle.putInt(
                    MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
                    it
            )
        }
        val ft = fragmentManager!!.beginTransaction()
        // add arguments to fragment
        val fragment = ShowTopPlayerEvent.newInstance()
        fragment.arguments = bundle
        ft.replace(
                R.id.fragment_placeholder,
                fragment
        )
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun eventAdapterWorldTournamentItemOpen(worldTournamentDTO: WorldTournamentDTO?) {
        val bundle = Bundle()

        val gson = GsonBuilder().setPrettyPrinting().create()
        val worldTournament = gson.toJson(worldTournamentDTO)

        bundle.putString(
                "worldTournament",
                worldTournament
        )

        //do this in more adaptable way
        worldTournamentDTO?.id?.let {
            bundle.putInt(
                    MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
                    it
            )
        }
        val ft = fragmentManager!!.beginTransaction()
        // add arguments to fragment
        val fragment = ShowWorldTournamentEvent.newInstance()
        fragment.arguments = bundle
        ft.replace(
                R.id.fragment_placeholder,
                fragment
        )
        ft.addToBackStack(null)
        ft.commit()
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return if (EventHandler.getList().isEmpty()) {
            0
        } else {
            EventHandler.getList().size
        }
    }

    private var eventId: Int? = 0
    private var coach: String? = null

    private fun startUserFragment() {
        if (eventId != null) {
            val bundle = Bundle()
            //do this in more adaptable way
            bundle.putInt(
                    MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
                    eventId!!
            )
            val ft = fragmentManager!!.beginTransaction()
            // add arguments to fragment
            val newFragment = ShowUserEvent.newInstance()

           /* val gson = GsonBuilder().setPrettyPrinting().create()
            val coach = coach*/
            //val userTournamentsList = gson.toJson(userTournamentsResult)

        /*    if (coach != null) {
                bundle.putString(
                        COACH,
                        coach
                )
            }*/

           /* bundle.putString(
                    TOURNAMENTS_RESULT,
                    userTournamentsList
            )*/

            newFragment.arguments = bundle
            ft.replace(
                    R.id.fragment_placeholder,
                    newFragment
            )
            ft.addToBackStack(null)
            ft.commit()
        }
    }

    override fun userTournamentsResultAreLoaded(tournamentsResults: MutableList<TournamentResultDTO>?) {
        this.userTournamentsResult = tournamentsResults
    }

    override fun showWorldTournament(worldTournamentDTO: WorldTournamentDTO?) {
        eventAdapterWorldTournamentItemOpen(worldTournamentDTO)
    }

    override fun showTopPlayer(topPlayerDTO: TopPlayerWithImageDTO?) {
        eventAdapterTopPlayerItemOpen(topPlayerDTO)
    }

    override fun detachView() {
    }

    override fun viewIsReady() {
    }

    override fun onServerUnavailable() {
    }

    override fun onUnsuccessfulRequest(message: String?) {
    }

    override fun setConnectivityStatus(status: Int?) {
    }

}