package com.procrastimax.birthdaybuddy.views

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.handler.BitmapHandler
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import com.procrastimax.birthdaybuddy.models.EventTournament
import com.procrastimax.birthdaybuddy.models.MonthDivider
import kotlinx.android.synthetic.main.tournament_event_item_view.view.*
import kotlinx.android.synthetic.main.event_month_view_divider.view.*

class EventAdapterSearching(private val context: Context, private val eventIDs: List<Int>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class BirthdayEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class EventMonthDividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * getItemViewType overrides the standard function
     * it defines the different viewholder types used for the recycler view
     * 1 - birthday event viewholder
     *
     * @param position: Int
     * @return Int
     */
    override fun getItemViewType(position: Int): Int {
        when (EventHandler.getList()[position]) {
            is EventTournament -> {
                if (eventIDs.contains(EventHandler.getList()[position].eventID))
                    return 1
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
            else -> {
                //Default is birthday event
                val itemView = View(context)
                return BirthdayEventViewHolder(itemView)
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
                holder.itemView.tv_divider_description_month.text =
                    (EventHandler.getList()[position] as MonthDivider).month_name
            }

            //BirthdayEventViewHolder
            1 -> {
                //check if is birthday event and if the year is given
                EventHandler.getList()[position].let { birthdayEvent ->
                    if (birthdayEvent is EventTournament) {

                        //set on click listener for item
                        holder.itemView.setOnClickListener {
                            //replace this activity with the MainActivity to show searched
                            val intent = Intent(context, MainActivity::class.java)
                            intent.putExtra(
                                MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
                                birthdayEvent.eventID
                            )
                            intent.putExtra(MainActivity.FRAGMENT_EXTRA_TITLE_POSITION, position)
                            intent.putExtra(
                                MainActivity.FRAGMENT_EXTRA_TITLE_TYPE,
                                MainActivity.FRAGMENT_TYPE_SHOW
                            )
                            startActivity(context, intent, null)
                        }

                        holder.itemView.setOnLongClickListener {
                            //replace this activity with the MainActivity to show searched
                            val intent = Intent(context, MainActivity::class.java)
                            intent.putExtra(
                                MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
                                birthdayEvent.eventID
                            )
                            intent.putExtra(MainActivity.FRAGMENT_EXTRA_TITLE_POSITION, position)
                            intent.putExtra(
                                MainActivity.FRAGMENT_EXTRA_TITLE_TYPE,
                                MainActivity.FRAGMENT_TYPE_EDIT
                            )
                            startActivity(context, intent, null)
                            true
                        }
/*
                        //set startDate
                        holder.itemView.tv_birthday_event_item_date_value.text =
                            birthdayEvent.getPrettyShortStringWithoutYear()

                        //set days until
                        val daysUntil = birthdayEvent.getDaysUntil()
                        if (daysUntil == 0) {
                            holder.itemView.tv_birthday_event_item_days_until_value.text =
                                context.resources.getText(R.string.today)
                        } else {
                            holder.itemView.tv_birthday_event_item_days_until_value.text =
                                birthdayEvent.getDaysUntil().toString()
                        }

                        //set years since, if specified
                        if (birthdayEvent.isYearGiven) {
                            holder.itemView.tv_birthday_event_item_years_since_value.text =
                                (birthdayEvent.getYearsSince() + 1).toString()
                        } else {
                            holder.itemView.tv_birthday_event_item_years_since_value.text =
                                "-"
                        }

                        //if a birthday has a fullDescription, only show fullDescription
                        if (birthdayEvent.fullDescription != null) {

                            //set forename and shortDescription invisible
                            holder.itemView.tv_birthday_event_item_forename.visibility =
                                TextView.GONE
                            holder.itemView.tv_birthday_event_item_surname.visibility =
                                TextView.GONE

                            //set fullDescription textview visible
                            holder.itemView.tv_birthday_event_item_nickname.visibility =
                                TextView.VISIBLE

                            //set fullDescription textview text
                            holder.itemView.tv_birthday_event_item_nickname.text =
                                birthdayEvent.fullDescription

                        } else {

                            //when shortDescription is given, set shortDescription and forename
                            if (birthdayEvent.shortDescription != null) {
                                //set forename and shortDescription invisible
                                holder.itemView.tv_birthday_event_item_forename.visibility =
                                    TextView.VISIBLE
                                holder.itemView.tv_birthday_event_item_surname.visibility =
                                    TextView.VISIBLE

                                //set fullDescription textview visible
                                holder.itemView.tv_birthday_event_item_nickname.visibility =
                                    TextView.GONE

                                holder.itemView.tv_birthday_event_item_forename.text =
                                    birthdayEvent.forename

                                //set shortDescription
                                holder.itemView.tv_birthday_event_item_surname.text =
                                    birthdayEvent.shortDescription

                                //when shortDescription is not given, set forename as fullDescription textview
                            } else {
                                //set forename and shortDescription invisible
                                holder.itemView.tv_birthday_event_item_forename.visibility =
                                    TextView.GONE
                                holder.itemView.tv_birthday_event_item_surname.visibility =
                                    TextView.GONE

                                //set fullDescription textview visible
                                holder.itemView.tv_birthday_event_item_nickname.visibility =
                                    TextView.VISIBLE

                                //set fullDescription textview text
                                holder.itemView.tv_birthday_event_item_nickname.text =
                                    birthdayEvent.forename
                            }
                        }*/

                        val avatarUri = birthdayEvent.imageUri

                        //when called from MainActivity
                        if (context is MainActivity) {
                            if (avatarUri != null) {
                                holder.itemView.iv_birthday_event_item_image.setImageBitmap(
                                    BitmapHandler.getBitmapAt(
                                        birthdayEvent.eventID
                                    )
                                )
                            } else {
                                holder.itemView.iv_birthday_event_item_image.setImageResource(R.drawable.ic_birthday_person)
                            }
                        } else {
                            //called from search activity
                            if (avatarUri != null) {
                                holder.itemView.iv_birthday_event_item_image.setImageBitmap(
                                    BitmapHandler.getBitmapAt(
                                        birthdayEvent.eventID
                                    )
                                )
                            } else {
                                holder.itemView.iv_birthday_event_item_image.setImageResource(R.drawable.ic_birthday_person)
                            }
                        }
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