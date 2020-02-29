package com.procrastimax.birthdaybuddy.views

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
import bobrchess.of.by.belaruschess.dto.RankDTO
import bobrchess.of.by.belaruschess.fragments.ShowTournamentEvent
import bobrchess.of.by.belaruschess.handler.BitmapHandler
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import com.procrastimax.birthdaybuddy.fragments.OneTimeEventInstanceFragment
import com.procrastimax.birthdaybuddy.fragments.ShowOneTimeEvent
import com.procrastimax.birthdaybuddy.fragments.TournamentInstanceFragment
import com.procrastimax.birthdaybuddy.models.EventTournament
import com.procrastimax.birthdaybuddy.models.EventUser
import com.procrastimax.birthdaybuddy.models.MonthDivider
import com.procrastimax.birthdaybuddy.models.OneTimeEvent
import kotlinx.android.synthetic.main.event_month_view_divider.view.*
import kotlinx.android.synthetic.main.one_time_event_item_view.view.*
import kotlinx.android.synthetic.main.tournament_event_item_view.view.*
import kotlinx.android.synthetic.main.tournament_event_item_view.view.constraint_layout_birthday_item_view
import kotlinx.android.synthetic.main.tournament_event_item_view.view.iv_birthday_event_item_image
import kotlinx.android.synthetic.main.tournament_event_item_view.view.tv_birthday_event_item_forename
import kotlinx.android.synthetic.main.tournament_event_item_view.view.tv_birthday_event_item_nickname
import kotlinx.android.synthetic.main.tournament_event_item_view.view.tv_birthday_event_item_surname
import kotlinx.android.synthetic.main.user_event_item_view.view.*
import org.springframework.util.StringUtils


class EventAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var context: Context? = null
    private var fragmentManager: FragmentManager? = null
    private var ranks: List<RankDTO>? = null

    constructor(context: Context, fragmentManager: FragmentManager, ranks: List<RankDTO>?) : this() {
        this.context = context
        this.fragmentManager = fragmentManager
        this.ranks = ranks
    }
    var isClickable: Boolean = true

    class BirthdayEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class EventMonthDividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class AnnualEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

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
            /* is AnnualEvent -> {
                 return 2
             }*/
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
                return BirthdayEventViewHolder(itemView)
            }
            2 -> {
                val itemView =
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.annual_event_item_view, parent, false)
                return AnnualEventViewHolder(itemView)
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
                return BirthdayEventViewHolder(itemView)
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
                                holder.itemView.iv_birthday_event_item_image.setImageBitmap(
                                        BitmapHandler.getBitmapAt(
                                                birthday.eventID
                                        )
                                )
                            } else {
                                holder.itemView.iv_birthday_event_item_image.setImageResource(R.drawable.ic_birthday_person)
                            }
                        }
                    }
                }
            }


            4 -> {
                //check if is birthday event and if the year is given
                EventHandler.getList()[position].let { user ->
                    if (user is EventUser) {
                        //set on click listener for item
                        holder.itemView.setOnClickListener {
                            if (isClickable) {
                                val bundle = Bundle()
                                //do this in more adaptable way
                                bundle.putInt(
                                        MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
                                        user.eventID
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
                                        user.eventID
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

                        //  textColor = ContextCompat.getColor(context, R.color.colorAccent) // если нужно выделить цветом

                        textColor = ContextCompat.getColor(context!!, R.color.textDark)

                        var rank = user.rankId?.minus(1)?.let { ranks?.get(it)?.name }
                        if (StringUtils.isEmpty(rank)) {
                            rank = "-"
                        }
                        holder.itemView.user_rank.text = rank
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
                                holder.itemView.iv_birthday_event_item_image.setImageBitmap(
                                        BitmapHandler.getBitmapAt(
                                                user.eventID
                                        )
                                )
                            } else {
                                holder.itemView.iv_birthday_event_item_image.setImageResource(R.drawable.ic_birthday_person)
                            }
                        }
                    }
                }
            }

            //annual event item view holder
            2 -> {
                //check if is birthday event and if the year is given
                EventHandler.getList()[position].let { annualEvent ->
                    /*if (annualEvent is AnnualEvent) {
                        //set on click listener for item
                        holder.itemView.setOnClickListener {
                            if (isClickable) {
                                val bundle = Bundle()
                                //do this in more adaptable way
                                bundle.putInt(
                                    MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
                                    annualEvent.eventID
                                )
                                val ft = fragmentManager.beginTransaction()
                                // add arguments to fragment
                                val newAnnualEvent = ShowAnnualEvent.newInstance()
                                newAnnualEvent.arguments = bundle
                                ft.replace(
                                    R.id.fragment_placeholder,
                                    newAnnualEvent
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
                                    annualEvent.eventID
                                )
                                val ft = fragmentManager.beginTransaction()
                                // add arguments to fragment
                                val newAnnualEvent = AnnualEventInstanceFragment.newInstance()
                                newAnnualEvent.arguments = bundle
                                ft.replace(
                                    R.id.fragment_placeholder,
                                    newAnnualEvent
                                )
                                ft.addToBackStack(null)
                                ft.commit()
                            }
                            true
                        }

                        val textColor: Int

                        //set days until
                        val daysUntil = EventHandler.getList()[position].getDaysUntil()
                        if (daysUntil == 0) {
                            textColor = ContextCompat.getColor(context, R.color.colorAccent)
                            holder.itemView.tv_days_until_annual_value.text =
                                context.resources.getText(R.string.today)
                            holder.itemView.tv_days_until_annual_value.setTextColor(textColor)
                        } else {
                            textColor = ContextCompat.getColor(context, R.color.textDark)
                            holder.itemView.tv_days_until_annual_value.text = daysUntil.toString()
                            holder.itemView.tv_days_until_annual_value.setTextColor(textColor)
                        }

                        //set startDate
                        holder.itemView.tv_annual_item_date_value.text =
                            annualEvent.getPrettyShortStringWithoutYear()
                        holder.itemView.tv_annual_item_date_value.setTextColor(textColor)

                        //set years since, if specified
                        if (annualEvent.hasStartYear) {
                            holder.itemView.tv_years_since_annual_value.text =
                                annualEvent.getXTimesSinceStarting().toString()
                        } else {
                            holder.itemView.tv_years_since_annual_value.text = "-"
                        }
                        holder.itemView.tv_years_since_annual_value.setTextColor(textColor)

                        if (annualEvent.eventAlreadyOccurred()) {
                            holder.itemView.constraint_layout_annual_item_view.background =
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ripple_recycler_view_item_dark
                                )
                        } else {
                            holder.itemView.constraint_layout_annual_item_view.background =
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ripple_recycler_view_item
                                )
                        }

                        //set name
                        holder.itemView.tv_annual_item_name.text = annualEvent.name
                        holder.itemView.tv_annual_item_name.setTextColor(textColor)
                    }*/
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
}