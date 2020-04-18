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
import bobrchess.of.by.belaruschess.dto.CountryDTO
import bobrchess.of.by.belaruschess.dto.RankDTO
import bobrchess.of.by.belaruschess.dto.TournamentResultDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.fragments.*
import bobrchess.of.by.belaruschess.handler.BitmapHandler
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.model.*
import bobrchess.of.by.belaruschess.presenter.impl.TournamentsResultPresenterImpl
import bobrchess.of.by.belaruschess.view.activity.TournamentsResultContractView
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.event_month_view_divider.view.*
import kotlinx.android.synthetic.main.one_time_event_item_view.view.*
import kotlinx.android.synthetic.main.tournament_event_item_view.view.*
import kotlinx.android.synthetic.main.tournament_event_item_view.view.constraint_layout_birthday_item_view
import kotlinx.android.synthetic.main.tournament_event_item_view.view.tv_birthday_event_item_forename
import kotlinx.android.synthetic.main.tournament_event_item_view.view.tv_birthday_event_item_nickname
import kotlinx.android.synthetic.main.tournament_event_item_view.view.tv_birthday_event_item_surname
import kotlinx.android.synthetic.main.tournament_result_event_item_view.view.*
import kotlinx.android.synthetic.main.user_event_item_view.view.*
import org.springframework.util.StringUtils


class EventAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>(), TournamentsResultContractView {

    private var tournamentsResultPresenterImpl: TournamentsResultPresenterImpl? = null
    private var context: Context? = null
    private var fragmentManager: FragmentManager? = null
    private var ranks: List<RankDTO>? = null
    private var countries: List<CountryDTO>? = null
    private var userTournamentsResult: List<TournamentResultDTO>? = null
    private var users: List<UserDTO>? = null

    constructor(context: Context, fragmentManager: FragmentManager, ranks: List<RankDTO>?, countries: List<CountryDTO>?, users: List<UserDTO>?, userTournamentsResult: List<TournamentResultDTO>?) : this() {
        tournamentsResultPresenterImpl = TournamentsResultPresenterImpl()
        tournamentsResultPresenterImpl!!.attachView(this)

        this.context = context
        this.fragmentManager = fragmentManager
        this.ranks = ranks
        this.countries = countries
        this.users = users
    }

    var isClickable: Boolean = true

    class UserEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class TournamentEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class EventMonthDividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class TournamentResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class OneTimeEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * getItemViewType overrides the standard function
     * it defines the different viewholder types used for the recycler view
     * 0 - month description divider
     * 1 - birthday event viewholder
     * 2 - annual event viewholder
     * 3 - one time event viewholder
     *
     * @param position: Int
     * @return Int
     */

    override fun getItemViewType(position: Int): Int {
        when (EventHandler.getList()[position]) {
            is MonthDivider -> {
                if (position < EventHandler.getList().size - 1) {
                    if (EventHandler.getList()[position + 1] !is MonthDivider) {
                        return 0
                    }
                }
                return -1
            }
            is EventTournament -> {
                return 1
            }
            is EventUser -> {
                return 4
            }
            is EventTournamentResult -> {
                return 2;
            }
            is OneTimeEvent -> {
                return 3
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
            else -> {
                //Default is birthday event
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
                    if (monthDivider is MonthDivider) {
                        holder.itemView.tv_divider_description_month.text =
                                monthDivider.month_name
                    }
                }
            }

            //BirthdayEventViewHolder
            1 -> {
                //check if is birthday event and if the year is given
                EventHandler.getList()[position].let { birthday ->
                    if (birthday is EventTournament) {
                        //set on click listener for item
                        holder.itemView.setOnClickListener {
                            if (isClickable) {
                                val bundle = Bundle()
                                //do this in more adaptable way
                                bundle.putInt(
                                        MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
                                        birthday.eventID
                                )
                                val ft = fragmentManager!!.beginTransaction()
                                // add arguments to fragment
                                val newBirthdayFragment = ShowTournamentEvent.newInstance()
                                newBirthdayFragment.arguments = bundle
                                ft.replace(
                                        R.id.fragment_placeholder,
                                        newBirthdayFragment
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
                                        birthday.eventID
                                )
                                val ft = fragmentManager!!.beginTransaction()
                                // add arguments to fragment
                                val newBirthdayFragment = TournamentInstanceFragment.newInstance()
                                newBirthdayFragment.arguments = bundle
                                ft.replace(
                                        R.id.fragment_placeholder,
                                        newBirthdayFragment
                                )
                                ft.addToBackStack(null)
                                ft.commit()
                            }
                            true
                        }

                        val textColor: Int

                        //set days until
                        val daysUntil = birthday.getDaysUntil()
                        if (daysUntil == 0) {
                            textColor = ContextCompat.getColor(context!!, R.color.colorAccent)
                            holder.itemView.tv_birthday_event_item_days_until_value.text =
                                    context!!.getText(R.string.today)
                            holder.itemView.tv_birthday_event_item_days_until_value.setTextColor(
                                    textColor
                            )
                        } else {
                            textColor = ContextCompat.getColor(context!!, R.color.textDark)
                            holder.itemView.tv_birthday_event_item_days_until_value.text =
                                    daysUntil.toString()
                            holder.itemView.tv_birthday_event_item_days_until_value.setTextColor(
                                    textColor
                            )
                        }

                        //set startDate
                        holder.itemView.tv_birthday_event_item_date_value.text =
                                birthday.getPrettyShortStringWithoutYear()
                        holder.itemView.tv_birthday_event_item_date_value.setTextColor(textColor)

                        //set years since, if specified
                        // if (birthday.isYearGiven) {
                        holder.itemView.tv_birthday_event_item_years_since_value.text =
                                (birthday.id).toString()//todo
                        /*  } else {
                              holder.itemView.tv_birthday_event_item_years_since_value.text = "-"
                          }*/
                        holder.itemView.tv_birthday_event_item_years_since_value.setTextColor(
                                textColor
                        )

                        if (birthday.eventAlreadyOccurred()) {
                            holder.itemView.constraint_layout_birthday_item_view.background =
                                    ContextCompat.getDrawable(
                                            context!!,
                                            R.drawable.ripple_recycler_view_item_dark
                                    )
                        } else {
                            holder.itemView.constraint_layout_birthday_item_view.background =
                                    ContextCompat.getDrawable(
                                            context!!,
                                            R.drawable.ripple_recycler_view_item
                                    )
                        }

                        //set forename and shortDescription invisible
                        holder.itemView.tv_birthday_event_item_forename.visibility =
                                TextView.GONE
                        holder.itemView.tv_birthday_event_item_surname.visibility =
                                TextView.GONE

                        //set fullDescription TextView visible
                        holder.itemView.tv_birthday_event_item_nickname.visibility =
                                TextView.VISIBLE
                        holder.itemView.tv_birthday_event_item_nickname.setTextColor(textColor)

                        //set fullDescription TextView text
                        holder.itemView.tv_birthday_event_item_nickname.text = birthday.name

                        val avatarUri = birthday.imageUri

                        //when context is MainActivity
                        if (context is MainActivity) {
                            if (avatarUri != null) {
                                holder.itemView.iv_tournament_event_item_image.setImageBitmap(
                                        BitmapHandler.getBitmapAt(
                                                birthday.eventID
                                        )
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
                                coachId = user.coachId
                                tournamentsResultPresenterImpl?.loadUserTournamentsResults()
                            }
                        }

                        val textColor: Int

                        //set days until

                        //  textColor = ContextCompat.getColor(context, R.color.colorAccent) // если нужно выделить цветом

                        textColor = ContextCompat.getColor(context!!, R.color.textDark)

                        var rank = user.rankId?.minus(1)?.let { ranks?.get(it)?.name }
                        if (StringUtils.isEmpty(rank)) {
                            rank = "-"
                        }
                        holder.itemView.user_country_and_rank_and_rating.text = rank
                        holder.itemView.user_country_and_rank_and_rating.setTextColor(textColor)

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

                        if (user.eventAlreadyOccurred()) {
                            holder.itemView.constraint_layout_birthday_item_view.background =
                                    ContextCompat.getDrawable(
                                            context!!,
                                            R.drawable.ripple_recycler_view_item_dark
                                    )
                        } else {
                            holder.itemView.constraint_layout_birthday_item_view.background =
                                    ContextCompat.getDrawable(
                                            context!!,
                                            R.drawable.ripple_recycler_view_item
                                    )
                        }

                        //set forename and shortDescription invisible
                        holder.itemView.tv_birthday_event_item_forename.visibility =
                                TextView.GONE
                        holder.itemView.tv_birthday_event_item_surname.visibility =
                                TextView.GONE

                        //set fullDescription TextView visible
                        holder.itemView.tv_birthday_event_item_nickname.visibility =
                                TextView.VISIBLE
                        holder.itemView.tv_birthday_event_item_nickname.setTextColor(textColor)

                        //set fullDescription TextView text
                        holder.itemView.tv_birthday_event_item_nickname.text = user.name + " " + user.surname

                        val avatarUri = user.imageUri

                        //when context is MainActivity
                        if (context is MainActivity) {
                            if (avatarUri != null) {
                                holder.itemView.iv_user_event_item_image.setImageBitmap(
                                        BitmapHandler.getBitmapAt(
                                                user.eventID
                                        )
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
                                // todo если нужно, то сделать тут переход на этот турнир
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

                        if (eventTournamentResult.eventAlreadyOccurred()) {
                            holder.itemView.constraint_layout_birthday_item_view.background =
                                    ContextCompat.getDrawable(
                                            context!!,
                                            R.drawable.ripple_recycler_view_item_dark
                                    )
                        } else {
                            holder.itemView.constraint_layout_birthday_item_view.background =
                                    ContextCompat.getDrawable(
                                            context!!,
                                            R.drawable.ripple_recycler_view_item
                                    )
                        }

                        holder.itemView.tournament_result_name.visibility = TextView.VISIBLE
                        holder.itemView.tournament_result_name.text = eventTournamentResult.name
                        holder.itemView.tournament_result_points.visibility = TextView.VISIBLE
                        holder.itemView.tournament_result_points.text = eventTournamentResult.points.toString()
                        holder.itemView.tournament_result_position.visibility = TextView.VISIBLE
                        holder.itemView.tournament_result_position.text = eventTournamentResult.position.toString()

                        val avatarUri = eventTournamentResult.imageUri

                        //when context is MainActivity
                        if (context is MainActivity) {
                            if (avatarUri != null) {
                                holder.itemView.iv_tournament_result_event_item_image.setImageBitmap(
                                        BitmapHandler.getBitmapAt(
                                                eventTournamentResult.eventID
                                        )
                                )
                            } else {
                                holder.itemView.iv_tournament_result_event_item_image.setImageResource(R.drawable.ic_birthday_person)
                            }
                        }
                    }
                }
            }

            //one time event item view holder
            3 -> {
                //check if is birthday event and if the year is given
                EventHandler.getList()[position].let { oneTimeEvent ->
                    if (oneTimeEvent is OneTimeEvent) {

                        //set on click listener for item
                        holder.itemView.setOnClickListener {
                            if (isClickable) {
                                val bundle = Bundle()
                                bundle.putInt(
                                        MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
                                        oneTimeEvent.eventID
                                )
                                val ft = fragmentManager!!.beginTransaction()
                                // add arguments to fragment
                                val newOneTimeEvent = ShowOneTimeEvent.newInstance()
                                newOneTimeEvent.arguments = bundle
                                ft.replace(
                                        R.id.fragment_placeholder,
                                        newOneTimeEvent
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
                                        oneTimeEvent.eventID
                                )
                                val ft = fragmentManager!!.beginTransaction()
                                // add arguments to fragment
                                val newOneTimeEvent = OneTimeEventInstanceFragment.newInstance()
                                newOneTimeEvent.arguments = bundle
                                ft.replace(
                                        R.id.fragment_placeholder,
                                        newOneTimeEvent
                                )
                                ft.addToBackStack(null)
                                ft.commit()
                            }
                            true
                        }

                        val textColor: Int

                        //set days until
                        val daysUntil = oneTimeEvent.getDaysUntil()
                        if (daysUntil == 0 && oneTimeEvent.getYearsUntil() == 0) {
                            textColor = ContextCompat.getColor(context!!, R.color.colorAccent)
                            holder.itemView.tv_days_until_one_time_value.text =
                                    context!!.resources.getText(R.string.today)
                            holder.itemView.tv_days_until_one_time_value.setTextColor(textColor)
                        } else {
                            textColor = ContextCompat.getColor(context!!, R.color.textDark)
                            holder.itemView.tv_days_until_one_time_value.text =
                                    oneTimeEvent.getDaysUntil().toString()
                            holder.itemView.tv_days_until_one_time_value.setTextColor(textColor)
                        }

                        //set startDate
                        holder.itemView.tv_one_time_item_date_value.text =
                                oneTimeEvent.getPrettyShortStringWithoutYear()
                        holder.itemView.tv_one_time_item_date_value.setTextColor(textColor)

                        //set years until
                        holder.itemView.tv_years_one_time_value.text =
                                oneTimeEvent.getYearsUntil().toString()
                        holder.itemView.tv_years_one_time_value.setTextColor(textColor)

                        if (oneTimeEvent.eventAlreadyOccurred()) {
                            holder.itemView.constraint_layout_onetime_item_view.background =
                                    ContextCompat.getDrawable(
                                            context!!,
                                            R.drawable.ripple_recycler_view_item_dark
                                    )
                        } else {
                            holder.itemView.constraint_layout_onetime_item_view.background =
                                    ContextCompat.getDrawable(
                                            context!!,
                                            R.drawable.ripple_recycler_view_item
                                    )
                        }

                        //set name
                        holder.itemView.tv_one_time_item_name.text = oneTimeEvent.name
                        holder.itemView.tv_one_time_item_name.setTextColor(textColor)
                    }
                }
            }
        }
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
    private var coachId: Long? = 0

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

            val gson = GsonBuilder().setPrettyPrinting().create()
            val ranksList = gson.toJson(ranks)
            val countriesList = gson.toJson(countries)
            val coach = coachId?.toInt()?.let { it1 -> users?.get(it1) }
            val userTournamentsList = gson.toJson(userTournamentsResult)

            if (coach != null) {
                bundle.putString(
                        "coach",
                        gson.toJson(coach)
                )
            }
            bundle.putString(
                    "ranks",
                    ranksList
            )

            bundle.putString(
                    "countries",
                    countriesList
            )

            bundle.putString(
                    "tournamentsResult",
                    userTournamentsList
            )

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
        startUserFragment()
    }
}