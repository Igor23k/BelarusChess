package com.procrastimax.birthdaybuddy.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.model.EventDate
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import com.procrastimax.birthdaybuddy.models.AnnualEvent
import kotlinx.android.synthetic.main.fragment_show_annual_event.*
import java.text.DateFormat

/**
 * ShowAnnualEvent is a fragment to show all known data from an instance of EventTournament
 */
class ShowAnnualEvent : ShowEventFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_annual_event, container, false)
    }

    /**
     * updateUI updates all TextViews and other views to the current instance(AnnualEvent, Birthday) data
     */
    override fun updateUI() {
        EventHandler.getEventToEventIndex(eventID)?.let { annualEvent ->
            if (annualEvent is AnnualEvent) {
                //set name of annual_event
                this.tv_show_annual_event_name.text = annualEvent.name

                val date =
                    EventDate.parseDateToString(
                        EventDate.dateToCurrentTimeContext(annualEvent.eventDate),
                        DateFormat.FULL
                    )

                if (annualEvent.hasStartYear) {
                    //show adapted string for first birthday of a person, 1 year, not 1 years
                    tv_show_annual_event_years.text = resources.getQuantityString(
                        R.plurals.annual_event_years,
                        annualEvent.getXTimesSinceStarting(),
                        annualEvent.getXTimesSinceStarting()
                    )

                    tv_show_annual_event_first_date.text = resources.getString(
                        R.string.annual_event_start_date,
                        EventDate.parseDateToString(
                            annualEvent.eventDate,
                            DateFormat.FULL
                        )
                    )
                } else {
                    this.tv_show_annual_event_years.visibility = TextView.GONE
                    this.tv_show_annual_event_first_date.visibility = TextView.GONE
                }

                when (annualEvent.getDaysUntil()) {
                    0 -> {
                        tv_show_annual_event_date.text = resources.getString(
                            R.string.annual_event_show_date_today,
                            annualEvent.name
                        )
                    }
                    1 -> {
                        tv_show_annual_event_date.text = resources.getString(
                            R.string.annual_event_show_date_tomorrow,
                            date
                        )
                    }
                    else -> {
                        tv_show_annual_event_date.text = resources.getQuantityString(
                            R.plurals.annual_event_show_date,
                            annualEvent.getDaysUntil(),
                            annualEvent.getDaysUntil(),
                            date,
                            annualEvent.getWeeksUntilAsString()
                        )
                    }
                }

                if (!annualEvent.note.isNullOrBlank()) {
                    this.tv_show_annual_event_note.text =
                        "${context!!.resources.getString(R.string.event_property_note)}: ${annualEvent.note}"
                    this.tv_show_annual_event_note.visibility = TextView.VISIBLE
                } else {
                    this.tv_show_annual_event_note.visibility = TextView.GONE
                }
            }
        }
    }

    /**
     * shareEvent a function which is called after the share button has been pressed
     * It provides a simple intent to share data as plain text in other apps
     */
    override fun shareEvent() {
        EventHandler.getEventToEventIndex(eventID)?.let { annualEvent ->
            if (annualEvent is AnnualEvent) {

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"

                var shareAnnualEventMsg =
                    context!!.resources.getString(
                        R.string.share_annual_event_name,
                        annualEvent.name
                    )

                //annual_event next date
                shareAnnualEventMsg += "\n" + context!!.resources.getString(
                    R.string.share_annual_event_date_next,
                    EventDate.parseDateToString(
                        EventDate.dateToCurrentTimeContext(annualEvent.eventDate),
                        DateFormat.FULL
                    )
                )

                //annual_event days until
                shareAnnualEventMsg += "\n" + context!!.resources.getQuantityString(
                    R.plurals.share_annual_event_days,
                    annualEvent.getDaysUntil(),
                    annualEvent.getDaysUntil()
                )

                if (annualEvent.hasStartYear) {
                    //annual_event date start
                    shareAnnualEventMsg += "\n" + context!!.resources.getString(
                        R.string.share_annual_event_date_start,
                        EventDate.parseDateToString(annualEvent.eventDate, DateFormat.FULL)
                    )

                    //annual_event years since
                    shareAnnualEventMsg += "\n" + context!!.resources.getQuantityString(
                        R.plurals.share_annual_event_year,
                        annualEvent.getYearsSince(),
                        annualEvent.getYearsSince()
                    )
                }
                intent.putExtra(Intent.EXTRA_TEXT, shareAnnualEventMsg)
                startActivity(
                    Intent.createChooser(
                        intent,
                        resources.getString(R.string.intent_share_chooser_title)
                    )
                )
            }
        }
    }

    override fun editEvent() {
        val bundle = Bundle()
        bundle.putInt(
            MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
            eventID
        )
        val ft = (context as MainActivity).supportFragmentManager.beginTransaction()

        // add arguments to fragment
        val newAnnualEventInstanceFragment = AnnualEventInstanceFragment.newInstance()
        newAnnualEventInstanceFragment.arguments = bundle
        ft.replace(
            R.id.fragment_placeholder,
            newAnnualEventInstanceFragment,
            AnnualEventInstanceFragment.ANNUAL_EVENT_INSTANCE_FRAGMENT_TAG
        )
        ft.addToBackStack(null)
        ft.commit()
    }

    companion object {
        /**
         * newInstance returns a new instance of this fragment
         */
        @JvmStatic
        fun newInstance(): ShowAnnualEvent {
            return ShowAnnualEvent()
        }
    }
}
